package seedu.address.storage.util;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.util.MarkdownConverter.toMarkdown;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.ModelManager;
import seedu.address.model.category.Category;
import seedu.address.model.entry.EntryDescription;
import seedu.address.model.entry.EntryInfo;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.resume.Resume;
import seedu.address.model.resume.ResumeSection;
import seedu.address.model.template.Template;
import seedu.address.model.template.TemplateSection;

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

    @Test
    public void resumeSectionToMarkdown() {
        // No entries
        assertEquals("##### test section" + System.lineSeparator(),
                MarkdownConverter.toMarkdown(new ResumeSection("test section", new ArrayList<>())));

        // Some entries with no description
        List<ResumeEntry> testEntryList = new ArrayList<>();
        ResumeEntry testEntryOne = new ResumeEntry(new Category("testCategory"), testInfo, new HashSet<>());
        EntryInfo testInfoTwo = new EntryInfo("second title", "second subheader", "second duration");
        ResumeEntry testEntryTwo = new ResumeEntry(new Category("testCategory"), testInfoTwo, new HashSet<>());
        testEntryList.add(testEntryOne);
        testEntryList.add(testEntryTwo);
        ResumeSection testSection = new ResumeSection("test section", testEntryList);
        assertEquals("##### test section" + System.lineSeparator()
                        + toMarkdown(testEntryOne)
                        + toMarkdown(testEntryTwo),
                toMarkdown(testSection));

        // Some entries with description
        testEntryOne.getDescription().addBullet("first bullet");
        testEntryTwo.getDescription().addBullet("second bullet");
        testEntryTwo.getDescription().addBullet("third bullet");
        assertEquals("##### test section" + System.lineSeparator()
                        + toMarkdown(testEntryOne)
                        + toMarkdown(testEntryTwo),
                toMarkdown(testSection));
    }

    @Test
    public void resumeSectionListToMarkdown() {
        // No sections
        assertEquals(System.lineSeparator()
                        + "---" + System.lineSeparator(),
                toMarkdown(new ArrayList<>()));

        // Some empty sections
        ResumeSection testSectionOne = new ResumeSection("first section", new ArrayList<>());
        ResumeSection testSectionTwo = new ResumeSection("second section", new ArrayList<>());
        List<ResumeSection> testSectionList = new ArrayList<>();
        testSectionList.add(testSectionOne);
        testSectionList.add(testSectionTwo);
        assertEquals(System.lineSeparator()
                        + "---" + System.lineSeparator()
                        + toMarkdown(testSectionOne)
                        + "---" + System.lineSeparator()
                        + toMarkdown(testSectionTwo)
                        + "---" + System.lineSeparator(),
                toMarkdown(testSectionList));

        // Mixed empty sections and sections with assorted content
        // Section with empty entry
        ResumeEntry testEntryEmpty = new ResumeEntry(new Category("testCategory"), testInfo, new HashSet<>());
        List<ResumeEntry> testEntryListEmptyEntry = new ArrayList<>();
        testEntryListEmptyEntry.add(testEntryEmpty);
        ResumeSection testSectionEmptyEntry = new ResumeSection("empty entry", testEntryListEmptyEntry);

        // Section with empty entry, entry with single description, entry with multiple descriptions
        ResumeEntry testEntryOne = new ResumeEntry(new Category("testCategory"), testInfo, new HashSet<>());
        testEntryOne.getDescription().addBullet("first bullet");
        ResumeEntry testEntryTwo = new ResumeEntry(new Category("testCategory"), testInfo, new HashSet<>());
        testEntryTwo.getDescription().addBullet("second bullet");
        testEntryTwo.getDescription().addBullet("third bullet");
        List<ResumeEntry> testEntryListMixedEntries = new ArrayList<>();
        testEntryListMixedEntries.add(testEntryEmpty);
        testEntryListMixedEntries.add(testEntryOne);
        testEntryListMixedEntries.add(testEntryTwo);
        ResumeSection testSectionMixedEntries = new ResumeSection("mixed entries", testEntryListMixedEntries);

        // Combine into section list
        List<ResumeSection> testSectionListMixed = new ArrayList<>();
        testSectionList.add(testSectionEmptyEntry);
        testSectionList.add(testSectionMixedEntries);

        assertEquals(System.lineSeparator()
                        + "---" + System.lineSeparator()
                        + toMarkdown(testSectionOne)
                        + "---" + System.lineSeparator()
                        + toMarkdown(testSectionTwo)
                        + "---" + System.lineSeparator()
                        + toMarkdown(testSectionEmptyEntry)
                        + "---" + System.lineSeparator()
                        + toMarkdown(testSectionMixedEntries)
                        + "---" + System.lineSeparator(),
                toMarkdown(testSectionList));
    }

    @Test
    public void resumeToMarkdown() {
        // Empty default resume with no header
        List<ResumeSection> testSectionList = new ArrayList<>();
        for (TemplateSection templateSection : Template.getDefaultTemplate().getSections()) {
            testSectionList.add(new ResumeSection(templateSection.getTitle(), new ArrayList<ResumeEntry>()));
        }
        assertEquals(toMarkdown(testSectionList),
                toMarkdown(new Resume("test resume", new ModelManager())));

        // TODO: implement test for resume with actual filtered entries
    }
}
