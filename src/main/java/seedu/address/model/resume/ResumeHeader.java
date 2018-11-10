package seedu.address.model.resume;

import seedu.address.model.UserParticulars;

/**
 * Represents information that is only printed once in the resume such as the user's personal particulars.
 */
public class ResumeHeader {
    private final UserParticulars userParticulars;

    public ResumeHeader(UserParticulars userParticulars) {
        this.userParticulars = userParticulars;
    }

    public String getName() {
        return userParticulars.getName();
    }

    public String getPhone() {
        return userParticulars.getMobile();
    }

    public String getEmail() {
        return userParticulars.getEmail();
    }

    public String getAddress() {
        return userParticulars.getAddress();
    }
}
