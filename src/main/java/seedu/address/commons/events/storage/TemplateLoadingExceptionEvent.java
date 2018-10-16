package seedu.address.commons.events.storage;

import java.nio.file.Path;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates an exception during loading of a template
 */
public class TemplateLoadingExceptionEvent extends BaseEvent {

    public final Exception exception;
    public final Path filepath;

    public TemplateLoadingExceptionEvent(Exception exception, Path filepath) {
        this.exception = exception;
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "Exception while attempting to load " + filepath + ":\n" + exception.toString();
    }
}
