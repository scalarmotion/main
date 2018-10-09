package seedu.address.model.entry;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.category.Category;
import seedu.address.model.tag.Tag;

/**
 * class encapsulating entries under education, experience or project section
 */
public class MajorResumeEntry implements Taggable {

    private Category category;
    private EntryInfo entryInfo; // contains title,subheader and duration of the entry
    private Set<Tag> tags = new HashSet<>();
    private EntryDescription description = new EntryDescription();

    /**
     * Constructs a {@code MajorResumeEntry}.
     *
     * @param category A valid category name.
     * @param entryInfo A valid entryInfo.
     * @param tags A set of tags, can be empty.
     *
     */
    public MajorResumeEntry(String category, List<String> entryInfo, Set<Tag> tags) {
        requireAllNonNull(category, tags, entryInfo);
        this.category = new Category(category);
        this.tags.addAll(tags);
        this.entryInfo = new EntryInfo(entryInfo);
    }

    public Category getCategory() {
        return category;
    }

    public EntryInfo getEntryInfo() {
        return entryInfo;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public EntryDescription getDescription() {
        return description;
    }

    /**
     * Returns true if both entries of the same category, entryInfo and
     * tags
     * This defines a weaker notion of equality between two entries.
     */
    public boolean isSameEntry(MajorResumeEntry otherMajorEntry) {
        if (otherMajorEntry == this) {
            return true;
        }

        return otherMajorEntry != null
                && otherMajorEntry.getCategory().equals(category)
                && otherMajorEntry.getEntryInfo().equals(entryInfo)
                && otherMajorEntry.getTags().equals(getTags());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MajorResumeEntry)) {
            return false;
        }

        MajorResumeEntry otherMajorEntry = (MajorResumeEntry) other;
        return otherMajorEntry.category.equals(category)
                && otherMajorEntry.getTags().equals(tags)
                && otherMajorEntry.getDescription().equals(description)
                && otherMajorEntry.getEntryInfo().equals(entryInfo);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(category, tags, description, entryInfo);
    }

    @Override
    // note: Description is omitted
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(category.cateName)
                .append(entryInfo)
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
