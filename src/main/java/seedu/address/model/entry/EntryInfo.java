package seedu.address.model.entry;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * represents
 */
public class EntryInfo {

    public static final String MESSAGE_ENTRYINFO_CONSTRAINTS =
            "EACH ENTRY HAS TO BE ASSOCIATED WITH TITLE,SUBHEADER AND DURATION";

    private List<String> entryInfo = new LinkedList<String>();

    /**
     * @param title
     * @param subheader
     * @param duration
     */
    public EntryInfo(String title, String subheader, String duration) {
        requireAllNonNull(title, subheader, duration);
        entryInfo.add(title);
        entryInfo.add(subheader);
        entryInfo.add(duration);
    }

    public EntryInfo(List<String> entryInfo) {
        requireAllNonNull(entryInfo);
        checkArgument(isValidEntryInfo(entryInfo), MESSAGE_ENTRYINFO_CONSTRAINTS);
        this.entryInfo.addAll(entryInfo);
    }


    /**
     * Returns true if a given list of strings contains title, subheader and
     * duration.
     */
    public static boolean isValidEntryInfo(List<String> entryInfo) {
        return true;
    }

    public List<String> getEntryInfo() {
        return entryInfo;
    }

    /**
     * @return Title of the entry.
     */
    public String getTitle() {
        return entryInfo.get(0);
    }

    /**
     * @return Subheader of the entry.
     */
    // precond: for v1.1, input for subheader is compulsory
    public String getSubHeader() {
        return entryInfo.get(1);
    }

    /**
     * @return duration the entry is associated with.
     */
    public String getDuration() {
        return entryInfo.get(2);
    }


    public void setTitle(String title) {
        entryInfo.set(0, title);
    }

    public void setSubHeader(String subHeader) {
        entryInfo.set(1, subHeader);
    }

    public void setDuration(String duration) {
        entryInfo.set(2, duration);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EntryInfo
                && entryInfo.equals(((EntryInfo) other).getEntryInfo()));
    }

    @Override
    public int hashCode() {
        return entryInfo.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Title: ")
                .append(getTitle())
                .append(" SubHeader: ")
                .append(getSubHeader())
                .append(" Duration: ")
                .append(getDuration());
        return builder.toString();
    }
}
