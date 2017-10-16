package gaj.apps.text.fd.parser;

import static gaj.text.handler.sax.StateSAXEventRuleFactory.newRule;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.sax.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventType;
import gaj.text.handler.sax.StateSAXEventRuleFactory;

public class KDictHandler extends ContextfStatefulSAXEventRuleHandler<KDictState> {

    private static final boolean IS_TRACE = Boolean.parseBoolean(System.getProperty("trace.handler.hm", "false"));

    private static final String SECTION_WORD_KEY = "word";
    private static final String SECTION_SEGMENTS_KEY = "segments";
    private static final String SEGMENT_WORD_KEY = "word";
    private static final String SEGMENT_TAG_KEY = "tag";
    private static final String SEGMENT_EXAMPLES_KEY = "examples";

	private final Consumer<UnstructuredData> consumer;

    private List<StateEventRule<KDictState, SAXEvent>> rules = null;

    private UnstructuredData sectionData = null;
    private UnstructuredData segmentData = null;

    {
        setTrace(IS_TRACE);
    }

    public KDictHandler(Consumer<UnstructuredData> consumer) {
		this.consumer = consumer;
	}

	@Override
    public KDictState nullState() {
        return KDictState.INIT;
    }

    @Override
    public void setState(KDictState state) {
        if (KDictState.REWIND == state) {
            rewindState();
        } else {
            super.setState(state);
        }
    }

    @Override
    protected Iterable<StateEventRule<KDictState, SAXEvent>> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
            // Capture all text by default.
            rules.add(StateSAXEventRuleFactory.newRule(SAXEventType.CHARACTERS, this::appendTextBuffer));
            // Open section.
            rules.add(newRule(KDictState.INIT, KDictEvents.START_SECTION, KDictState.SECTION, this::initSection));
            rules.add(newRule(KDictState.SECTION, KDictEvents.START_SECTION_WORD, KDictState.WORD, this::captureTextOn));
            rules.add(newRule(KDictState.WORD, KDictEvents.END_SECTION_WORD, KDictState.REWIND, this::getSectionWord));
            // Handle pre-segment data.
            rules.add(newRule(KDictState.SECTION, KDictEvents.START_SEGMENT_TAG, KDictState.TAG, this::captureTextOn));
            rules.add(newRule(KDictState.TAG, KDictEvents.END_SEGMENT_TAG, KDictState.REWIND, this::getSegmentTag));
            rules.add(newRule(KDictState.SECTION, KDictEvents.START_SEGMENT_WORD, KDictState.WORD_VARIANT, this::captureTextOn));
            rules.add(newRule(KDictState.WORD_VARIANT, KDictEvents.END_SEGMENT_WORD, KDictState.REWIND, this::getSegmentWord));
            // Handle explicit segment.
            rules.add(newRule(KDictState.SECTION, KDictEvents.START_SEGMENT, KDictState.SEGMENT));
            rules.add(newRule(KDictState.SEGMENT, KDictEvents.START_SEGMENT_EXAMPLE, KDictState.EXAMPLE, this::captureTextOn));
            rules.add(newRule(KDictState.EXAMPLE, KDictEvents.END_SEGMENT_EXAMPLE, KDictState.REWIND, this::addSegmentExample));
            rules.add(newRule(KDictState.SEGMENT, KDictEvents.END_SEGMENT, KDictState.REWIND, this::addSegment));
            // Close section.
            rules.add(newRule(KDictState.SECTION, KDictEvents.END_SECTION, KDictState.REWIND, this::addSection));
            // Expect the unexpected.
            rules.add(newRule(SAXEventType.BEGIN_ELEMENT, KDictState.OTHER));
            rules.add(newRule(SAXEventType.END_ELEMENT, KDictState.REWIND));
        }
        return rules;
    }

    protected void initSection() {
        sectionData = UnstructuredData.create();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSectionWord() {
        sectionData.getMap().put(SECTION_WORD_KEY, getTextBuffer().trim());
        clearTextBuffer();
        captureTextOff();
        // Special case, since there are optional pre-segment data after the section word.
        segmentData = UnstructuredData.create();
    }

    protected void getSegmentTag() {
        String tag = (String) segmentData.getMap().get(SEGMENT_TAG_KEY);
        if (tag == null) {
            segmentData.getMap().put(SEGMENT_TAG_KEY, getTextBuffer().trim());
        } else {
            segmentData.getMap().put(SEGMENT_TAG_KEY, tag + "[" + getTextBuffer().trim() + "]");
        }
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSegmentWord() {
        segmentData.getMap().put(SEGMENT_WORD_KEY, getTextBuffer().trim());
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSegmentExample() {
        @SuppressWarnings("unchecked")
        List<String> examples = (List<String>) segmentData.getMap().get(SEGMENT_EXAMPLES_KEY);
        if (examples == null) {
            examples = new ArrayList<>();
            segmentData.getMap().put(SEGMENT_EXAMPLES_KEY, examples);
        }
        String example = getTextBuffer().trim();
        if (IS_TRACE) System.out.printf("example=%s%n", example);
        examples.add(example);
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSegment() {
        if (IS_TRACE) System.out.printf("segmentData=%s%n", segmentData);
        @SuppressWarnings("unchecked")
        List<UnstructuredData> segments = (List<UnstructuredData>) sectionData.getMap().get(SECTION_SEGMENTS_KEY);
        if (segments == null) {
            segments = new ArrayList<>();
            sectionData.getMap().put(SECTION_SEGMENTS_KEY, segments);
        }
        segments.add(segmentData);
        segmentData = null;
    }

    protected void addSection() {
        if (IS_TRACE) System.out.printf("sectionData=%s%n", sectionData);
        // Handle any dangling segment.
        if (segmentData != null) addSegment();
        consumer.accept(sectionData);
        sectionData = null;
    }

}
