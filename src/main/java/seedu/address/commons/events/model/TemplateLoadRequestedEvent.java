package seedu.address.commons.events.model;

import java.nio.file.Path;

import seedu.address.commons.events.BaseEvent;

/** Indicates there has been a call to load a Template from a file*/
public class TemplateLoadRequestedEvent extends BaseEvent {

    public final Path filepath;

    public TemplateLoadRequestedEvent(Path filepath) {
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "Request to load template from " + filepath.toString();
    }
}
