package gaj.apps.text.fd.parser;

import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventFactory;
import gaj.text.handler.sax.SAXEventType;

public abstract class KDictEvents {

    /*
     *  <section data-src="kdict">
     *      <h2>word</h2>
     *      { 
     *      <b>word-form</b>?
     *      <span class="pron"/>?
     *      <i>tag</i>? <i>tag-features</i>?
     *      <b>word-form</b>?
     *      <div class="ds-single">*
     *          <i>info</i>?  <b>info</b>? definition?
     *          <span class="illustration">example</span>?
     *          <span class="trans"/>*
     *      </div>
     *      }*
     *  </section>
     */
    public static final SAXEvent START_SECTION = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "section", "data-src", "kdict");
    public static final SAXEvent START_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "h2");
    public static final SAXEvent END_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "h2");
    public static final SAXEvent START_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "i");
    public static final SAXEvent END_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "i");
    public static final SAXEvent START_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "b");
    public static final SAXEvent END_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "b");
    public static final SAXEvent START_SEGMENT_PRONUNCIATION = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "span", "class", "pron");
    public static final SAXEvent END_SEGMENT_PRONUNCIATION = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "span");
    public static final SAXEvent START_SEGMENT = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "ds-single");
    public static final SAXEvent START_SEGMENT_EXAMPLE = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "span", "class", "illustration");
    public static final SAXEvent END_SEGMENT_EXAMPLE = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "span");
    public static final SAXEvent START_SEGMENT_TRANSLATION = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "span", "class", "trans");
    public static final SAXEvent END_SEGMENT_TRANSLATION = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "span");
    public static final SAXEvent END_SEGMENT = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SECTION = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "section");

    private KDictEvents() {}

}
