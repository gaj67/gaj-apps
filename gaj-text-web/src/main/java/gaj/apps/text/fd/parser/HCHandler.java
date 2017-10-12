package gaj.apps.text.fd.parser;

import static gaj.text.handler.sax.StateSAXEventRuleFactory.newRule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.sax.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventType;
import gaj.text.handler.sax.StateSAXEventRuleFactory;

public class HCHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    private static final boolean IS_TRACE = Boolean.parseBoolean(System.getProperty("trace.handler.hc", "false"));

    private static final String SECTION_WORD_KEY = "word";
    private static final String SECTION_SEGMENTS_KEY = "segments";
    private static final String SEGMENT_WORDS_KEY = "words";
    private static final String SEGMENT_TAG_KEY = "tag";
	private static final String SEGMENT_ITEMS_KEY = "items";
	private static final String ITEM_SUBITEMS_KEY = "subitems";
	private static final String EXAMPLES_KEY = "examples";

	private final Consumer<UnstructuredData> consumer;

    private List<StateEventRule<State, SAXEvent>> rules = null;

    private UnstructuredData sectionData = null;
    private UnstructuredData segmentData = null;
    private UnstructuredData itemData = null;
    private UnstructuredData subitemData = null;

    {
        super.setTrace(IS_TRACE);
    }

    public HCHandler(Consumer<UnstructuredData> consumer) {
		this.consumer = consumer;
	}

	@Override
    public State nullState() {
        return State.INIT;
    }

    @Override
    public void setState(State state) {
        if (State.REWIND == state) {
            rewindState();
        } else {
            super.setState(state);
        }
    }

    @Override
    protected Collection<StateEventRule<State, SAXEvent>> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
            rules.add(newRule(
                    State.INIT,
                    HCEvents.START_SECTION,
                    State.SECTION, this::initSection));
            rules.add(newRule(
                    State.SECTION,
                    HCEvents.START_SECTION_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.<State> newRule(
                    SAXEventType.CHARACTERS, this::appendTextBuffer));
            rules.add(newRule(
                    null, State.WORD, State.SECTION,
                    HCEvents.END_SECTION_WORD,
                    State.REWIND, this::getSectionWord));
            rules.add(newRule(
                    State.SECTION,
                    HCEvents.START_SEGMENT,
                    State.SEGMENT, this::initSegment));
            rules.add(newRule(
                    State.SECTION, State.SEGMENT,
                    HCEvents.START_SEGMENT_TAG,
                    State.TAG, this::captureTextOn));
            rules.add(newRule(
                    State.SEGMENT, State.TAG,
                    HCEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag));
            rules.add(newRule(
                    State.TAG, State.SEGMENT,
                    HCEvents.START_SEGMENT_TAG,
                    State.TAG_FEATURE, this::captureTextOn, (Action) () -> appendTextBuffer("[")));
            rules.add(newRule(
                    State.TAG_FEATURE,
                    HCEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag, (Action) () -> appendTextBuffer("]")));
            rules.add(newRule(
                    State.SEGMENT,
                    HCEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(newRule(
                    null, State.WORD, State.SEGMENT,
                    HCEvents.END_SEGMENT_WORD,
                    State.REWIND, this::addSegmentWord));
            rules.add(newRule(
                    State.WORD, State.SEGMENT,
                    HCEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            // TODO handle intra-word elements.
            rules.add(newRule(
                    State.SEGMENT,
                    HCEvents.START_SEGMENT_ITEM,
                    State.ITEM, this::initItem));
            rules.add(newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_ITEM2,
                    State.ITEM, this::initItem));
            rules.add(newRule(
                    State.ITEM,
                    HCEvents.START_SEGMENT_EXAMPLE,
                    State.EXAMPLE, this::captureTextOn));
            rules.add(newRule(
                    null, State.EXAMPLE, State.ITEM,
                    HCEvents.END_SEGMENT_EXAMPLE,
                    State.REWIND, this::addItemExample));
            rules.add(newRule(
                    State.ITEM,
                    HCEvents.START_SEGMENT_SUBITEM,
                    State.SUBITEM, this::initSubItem));
            rules.add(newRule(
                    State.SUBITEM,
                    HCEvents.START_SEGMENT_EXAMPLE,
                    State.EXAMPLE, this::captureTextOn));
            rules.add(newRule(
                    null, State.EXAMPLE, State.SUBITEM,
                    HCEvents.END_SEGMENT_EXAMPLE,
                    State.REWIND, this::addSubItemExample));
            rules.add(newRule(
                    State.SUBITEM,
                    HCEvents.END_SEGMENT_SUBITEM,
                    State.REWIND, this::addSubItem));
            rules.add(newRule(
                    State.ITEM,
                    HCEvents.END_SEGMENT_ITEM,
                    State.REWIND, this::addItem));
            rules.add(newRule(
                    State.SEGMENT,
                    HCEvents.END_SEGMENT,
                    State.REWIND, this::addSegment));
            rules.add(newRule(
                    State.SECTION,
                    HCEvents.START_SECTION_REP,
                    null, this::addSection));
            rules.add(newRule(
                    State.SECTION,
                    HCEvents.END_SECTION_REP,
                    null, this::initSection));
            rules.add(newRule(
                    State.SECTION,
                    HCEvents.END_SECTION,
                    State.REWIND, this::addSection));
            // Expect the unexpected
            rules.add(newRule(
                    State.OTHER, SAXEventType.BEGIN_ELEMENT, State.OTHER));
            rules.add(newRule(
                    State.OTHER, SAXEventType.END_ELEMENT, State.REWIND));
            rules.add(newRule(
                    SAXEventType.BEGIN_ELEMENT, State.OTHER));
        }
        return rules;
    }

    protected void initSection() {
        sectionData = UnstructuredData.create();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSectionWord() {
        sectionData.getMap().put(SECTION_WORD_KEY, getTextBuffer());
        clearTextBuffer();
        captureTextOff();
    }

    protected void initSegment() {
        segmentData = UnstructuredData.create();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSegmentTag() {
        String tag = (String) segmentData.getMap().get(SEGMENT_TAG_KEY);
        if (tag == null) {
            segmentData.getMap().put(SEGMENT_TAG_KEY, getTextBuffer());
        } else {
            segmentData.getMap().put(SEGMENT_TAG_KEY, tag + getTextBuffer());
        }
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSegmentWord() {
        @SuppressWarnings("unchecked")
        List<String> words = (List<String>) segmentData.getMap().get(SEGMENT_WORDS_KEY);
        if (words == null) {
        	words = new ArrayList<>();
            segmentData.getMap().put(SEGMENT_WORDS_KEY, words);
        }
        words.add(getTextBuffer());
        clearTextBuffer();
        captureTextOff();
    }

    protected void initItem() {
        itemData = UnstructuredData.create();
        clearTextBuffer();
        captureTextOff();
    }

    protected void addItemExample() {
        String example = getTextBuffer();
        clearTextBuffer();
        if (IS_TRACE)
            System.out.printf("example=%s%n", example);
        @SuppressWarnings("unchecked")
        List<String> examples = (List<String>) itemData.getMap().get(EXAMPLES_KEY);
        if (examples == null) {
            examples = new ArrayList<>();
            itemData.getMap().put(EXAMPLES_KEY, examples);
        }
        examples.add(example);
    }

    protected void initSubItem() {
        subitemData = UnstructuredData.create();
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSubItemExample() {
    	String example = getTextBuffer();
    	clearTextBuffer();
        if (IS_TRACE)
            System.out.printf("example=%s%n", example);
        @SuppressWarnings("unchecked")
        List<String> examples = (List<String>) subitemData.getMap().get(EXAMPLES_KEY);
        if (examples == null) {
            examples = new ArrayList<>();
            subitemData.getMap().put(EXAMPLES_KEY, examples);
        }
        examples.add(example);
    }

    protected void addSubItem() {
    	if (IS_TRACE) System.out.printf("subitemData=%s%n", subitemData);
        if (!subitemData.getMap().isEmpty()) {
    		@SuppressWarnings("unchecked")
            List<UnstructuredData> subitems = (List<UnstructuredData>) itemData.getMap().get(ITEM_SUBITEMS_KEY);
    		if (subitems == null) {
    			subitems = new ArrayList<>();
                itemData.getMap().put(ITEM_SUBITEMS_KEY, subitems);
    		}
    		subitems.add(subitemData);
        }
        subitemData = null;
    }

    protected void addItem() {
    	if (IS_TRACE) System.out.printf("itemData=%s%n", itemData);
        if (!itemData.getMap().isEmpty()) {
    		@SuppressWarnings("unchecked")
            List<UnstructuredData> items = (List<UnstructuredData>) segmentData.getMap().get(SEGMENT_ITEMS_KEY);
    		if (items == null) {
    			items = new ArrayList<>();
                segmentData.getMap().put(SEGMENT_ITEMS_KEY, items);
    		}
    		items.add(itemData);
        }
        itemData = null;
    }

    protected void addSegment() {
        if (IS_TRACE)
            System.out.printf("segmentData=%s%n", segmentData);
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
        if (IS_TRACE) {
        	System.out.printf("sectionData=%s%n", sectionData);
        }
        if (sectionData != null) {
            consumer.accept(sectionData);
        	sectionData = null;
        }
    }

}
