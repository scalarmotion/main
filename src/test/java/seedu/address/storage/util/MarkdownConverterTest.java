package seedu.address.storage.util;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.util.MarkdownConverter.toMarkdown;

import java.util.HashSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.category.Category;
import seedu.address.model.entry.EntryDescription;
import seedu.address.model.entry.EntryInfo;
import seedu.address.model.entry.ResumeEntry;

public class MarkdownConverterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private EntryInfo testInfo = new EntryInfo("entry title", "entry subheader", "entry duration");

    @Test
    public void entryInfoToMarkdown() {
        // All elements present
        assertEquals("###### entry title (_entry duration_)" + System.lineSeparator()
                        + "entry subheader" + System.lineSeparator()
                        + System.lineSeparator(),
                toMarkdown(testInfo));
    }

    @Test
    public void entryDescriptionToMarkdown() {
        EntryDescription testDescription = new EntryDescription();
        // Empty list
        assertEquals("",
                toMarkdown(testDescription));

        // One element
        testDescription.addBullet("first bullet");
        assertEquals("- first bullet",
                toMarkdown(testDescription));

        // More elements
        testDescription.addBullet("second bullet");
        testDescription.addBullet("third bullet");
        assertEquals("- first bullet" + System.lineSeparator()
                        + "- second bullet" + System.lineSeparator()
                        + "- third bullet",
                toMarkdown(testDescription));
    }

    @Test
    public void entryToMarkdown() {
        // No description
        ResumeEntry testEntry = new ResumeEntry(new Category("testCategory"), testInfo, new HashSet<>());
        assertEquals(toMarkdown(testInfo)
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + System.lineSeparator(),
                toMarkdown(testEntry));

        // Add description
        testEntry.getDescription().addBullet("first bullet");
        testEntry.getDescription().addBullet("second bullet");
        assertEquals(toMarkdown(testInfo)
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + toMarkdown(testEntry.getDescription())
                        + System.lineSeparator()
                        + System.lineSeparator(),
                toMarkdown(testEntry));
    }
}
