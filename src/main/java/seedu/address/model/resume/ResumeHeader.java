package seedu.address.model.resume;

import seedu.address.model.entry.PersonalInfo;

/**
 * Represents information that is only printed once in the resume such as the user's personal particulars.
 */
public class ResumeHeader {
    public final PersonalInfo personalInfo;

    public ResumeHeader(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
}
