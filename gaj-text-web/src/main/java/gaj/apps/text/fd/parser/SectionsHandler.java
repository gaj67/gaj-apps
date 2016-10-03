package gaj.apps.text.fd.parser;

import java.util.function.Consumer;

import gaj.text.handler.sax.DelegatingSAXEventHandler;
import gaj.text.handler.sax.SAXEvent;

public class SectionsHandler extends DelegatingSAXEventHandler {

	private final Consumer<UnstructuredData> consumer;

	public SectionsHandler(Consumer<UnstructuredData> consumer) {
		this.consumer = consumer;
		
	}

    @Override
    public void handle(SAXEvent event) {
        if (HMEvents.START_SECTION.matches(event)) {
            setHandler(new HMHandler(consumer));
            super.handle(event);
        } else if (HCEvents.START_SECTION.matches(event)) {
            setHandler(new HCHandler(consumer));
            super.handle(event);
        } else if (HMEvents.END_SECTION.matches(event)) {
        	// Also matches HCEvents.END_SECTION.
            super.handle(event);
            setHandler(null);
        } else {
            super.handle(event);
        }
    }

}
