package seedu.address.storage.entry;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.category.Category;
import seedu.address.model.entry.EntryDescription;
import seedu.address.model.entry.EntryInfo;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the ResumeEntry.
 */
public class XmlAdaptedResumeEntry {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Entry's %s field is missing!";

    @XmlAttribute
    private String category;
    @XmlElement
    private XmlAdaptedEntryInfo entryInfo;
    @XmlElement
    private XmlAdaptedEntryDescription entryDesc;
    @XmlElement
    private List<String> tags = new LinkedList<String>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedResumeEntry() {}


    /**
     * Converts this jaxb-friendly adapted resume entry object into the model's ResumeEntry object.
     * // TODO: Add data validation
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted resume entry.
     */
    public ResumeEntry toModelType() throws IllegalValueException {

        if (category == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                                                                  Category.class.getSimpleName()));
        }

        if (!Category.isValidCategoryName(category)) {
            throw new IllegalValueException(Category.MESSAGE_CATE_CONSTRAINTS);
        }

        Category modelCategory = new Category(category);

        /* tags are optional */
        HashSet<Tag> modelTags = new HashSet<Tag>();

        for (String t : tags) {
            if (!Tag.isValidTagName(t)) {
                throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
            }

            modelTags.add(new Tag(t));
        }

        /* entry info is optional */
        EntryInfo modelEntryInfo;

        if (entryInfo == null) {
            modelEntryInfo = new EntryInfo();
        } else {
            modelEntryInfo = entryInfo.toModelType();
        }

        /* entry description is optional*/
        if (entryDesc == null) {
            return new ResumeEntry(modelCategory, modelEntryInfo, modelTags);
        } else {

            EntryDescription modelDescription = entryDesc.toModelType();
            return new ResumeEntry(modelCategory, modelEntryInfo, modelTags, modelDescription);
        }

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedResumeEntry)) {
            return false;
        }

        XmlAdaptedResumeEntry otherResumeEntry = (XmlAdaptedResumeEntry) other;

        return Objects.equals(category, otherResumeEntry.category)
                && Objects.equals(entryInfo, otherResumeEntry.entryInfo)
                && Objects.equals(entryDesc, otherResumeEntry.entryDesc)
                && Objects.equals(tags, otherResumeEntry.tags);
    }
}
