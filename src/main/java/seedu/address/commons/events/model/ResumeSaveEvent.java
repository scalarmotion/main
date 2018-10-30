package seedu.address.commons.events.model;

import java.nio.file.Path;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.resume.Resume;

/** Indicates that a resume is being saved. */
public class ResumeSaveEvent extends BaseEvent {

    public final Resume data;

    public final Path filepath;

    public ResumeSaveEvent(Resume data, Path filepath) {
        this.data = data;
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "save resume to " + filepath.toString();
    }
}
