package seedu.address.model.resume;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.category.Category;
import seedu.address.model.entry.EntryInfo;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.template.Template;
import seedu.address.model.template.TemplateSection;

public class ResumeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorTest() {
        Model testModel = new ModelManager();
        Resume resume = new Resume(testModel);

        // default template, no entries
        List<ResumeSection> expectedSectionList = new ArrayList<>();
        Template defaultTemplate = Template.getDefaultTemplate();
        for (TemplateSection templateSection : defaultTemplate.getSections()) {
            expectedSectionList.add(new ResumeSection(templateSection.getTitle(), new ArrayList<ResumeEntry>()));
        }
        assertEquals(resume.getSectionList(), expectedSectionList);

        // TODO: add tests for "typical" ModelManager
    }

    @Test
    public void equalsTest() {
        // ResumeSection testing
        ResumeSection testSectionOne = new ResumeSection("section 1", new ArrayList<>());

        // Same object check
        assertEquals(testSectionOne, testSectionOne);

        // Type check
        assertNotEquals(testSectionOne, new ArrayList<>());

        // Same state
        assertEquals(testSectionOne, new ResumeSection("section 1", new ArrayList<>()));

        // Different state
        List<ResumeEntry> testSectionList = new ArrayList<>();
        testSectionList.add(new ResumeEntry(new Category("Test"), new EntryInfo(), new HashSet<>()));
        ResumeSection testSectionTwo = new ResumeSection("section 2", new ArrayList<>());
        assertNotEquals(testSectionOne, testSectionTwo);


        // Resume testing
        Resume testResumeOne = new Resume(new ModelManager());

        // Same object check
        assertEquals(testResumeOne, testResumeOne);

        // Type check
        assertNotEquals(testResumeOne, new ArrayList<>());

        // Same state
        Resume testResumeTwo = new Resume(new ModelManager());
        assertEquals(testResumeOne, testResumeTwo);

        // Different state
        // TODO: implement using "typical" ModelManager
    }
}
