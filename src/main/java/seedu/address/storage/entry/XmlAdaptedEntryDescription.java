package seedu.address.storage.entry;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.EntryDescription;

/** A JAXB friendly version of EntryDescription */
public class XmlAdaptedEntryDescription {

    public static final String MESSAGE_MISSING_BULLETS = "Description element, if provided, cannot be empty.";

    @XmlElement(name = "bullet")
    private List<String> bullets;

    /**
     * Default constructor needed by JAXB
     */
    public XmlAdaptedEntryDescription() {}

    /**
     * Converts this jaxb-friendly adapted entry description object into the model's EntryDescription object.
     * // TODO: Add data validation
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted entry description.
     */
    public EntryDescription toModelType() throws IllegalValueException {
        EntryDescription desc = new EntryDescription();

        if (bullets == null) {
            throw new IllegalValueException(MESSAGE_MISSING_BULLETS);
        }

        for (String bullet : bullets) {
            // TODO: Validate bullet
            desc.addBullet(bullet);
        }

        return desc;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedEntryInfo)) {
            return false;
        }

        XmlAdaptedEntryDescription entryDescription = (XmlAdaptedEntryDescription) other;
        return bullets.equals(entryDescription.bullets);

    }
}
