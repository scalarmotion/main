package seedu.address.storage.util;

import java.util.List;

import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.TextBuilder;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

import seedu.address.model.entry.EntryDescription;
import seedu.address.model.entry.EntryInfo;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.resume.Resume;
//import seedu.address.model.resume.ResumeHeader;
import seedu.address.model.resume.ResumeSection;

/**
 * Converts a given object to its Markdown representation.
 */
public final class MarkdownConverter {
    /**
     * This is a utility class only meant to be used through its static methods.
     * Hence, we create a private constructor to prevent Java from creating a
     * public constructor, which ensures that this class cannot be instantiated.
     */
    private MarkdownConverter() {
    }

    /**
     * @param resume is a Resume object which will be converted to
     * @return a String containing its Markdown representation.
     */
    public static String toMarkdown(Resume resume) {
        return new TextBuilder()
                //.append(new Text(MarkdownConverter.toMarkdown(resume.getHeader())))
                //.newLines(2)
                .append(new Text(MarkdownConverter.toMarkdown(resume.getSectionList())))
                .toString();
    }

    /**
     * param header is the Header of a Resume which will be converted to
     * return a String containing its Markdown representation.
     *
    public static String toMarkdown(ResumeHeader header) {
        // TODO: Implement when personal info structure has been confirmed
        return new TextBuilder()
                .toString();
    }

    /**
     * @param sectionList is the list of Sections of a Resume which will be converted to
     * @return a String containing its Markdown representation.
     */
    public static String toMarkdown(List<ResumeSection> sectionList) {
        TextBuilder sectionListBuilder = new TextBuilder().rule();
        for (ResumeSection section : sectionList) {
            sectionListBuilder = sectionListBuilder
                    .append(toMarkdown(section))
                    .rule();
        }
        return sectionListBuilder.toString();
    }

    /**
     * @param section is one section of a Resume which will be converted to
     * @return a String containing its Markdown representation.
     */
    public static String toMarkdown(ResumeSection section) {
        TextBuilder sectionBuilder = new TextBuilder().append(new Heading(section.title, 5)).newLine();
        for (ResumeEntry entry : section.getEntryList()) {
            sectionBuilder = sectionBuilder.append(toMarkdown(entry));
        }
        return sectionBuilder.toString();
    }

    /**
     * @param entry is an Entry in the EntryBook which will be converted to
     * @return a String containing its Markdown representation.
     */
    public static String toMarkdown(ResumeEntry entry) {
        return new TextBuilder()
                .append(new Text(MarkdownConverter.toMarkdown(entry.getEntryInfo())))
                .newLines(2)
                .append(new Text(MarkdownConverter.toMarkdown(entry.getDescription())))
                .newLines(2)
                .toString();
    }

    /**
     * @param info is the basic information about an Entry which will be converted to
     * @return a String containing its Markdown representation.
     */
    public static String toMarkdown(EntryInfo info) {
        return new TextBuilder()
                .append(new Heading(info.getTitle(), 6))
                .append(new Text(" ("))
                .append(new ItalicText(info.getDuration()))
                .append(new Text(")"))
                .newLine()
                .append(new Text(info.getSubHeader()))
                .newLines(2)
                .toString();
    }


    /**
     * @param desc is the detailed description of an Entry which will be converted to
     * @return a String containing its Markdown representation.
     */
    public static String toMarkdown(EntryDescription desc) {
        return new UnorderedList<>(desc.getDescriptionList()).toString();
    }
}
