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
import seedu.address.model.category.Category;
import seedu.address.model.entry.EntryInfo;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.template.Template;
import seedu.address.model.template.TemplateSection;
import seedu.address.testutil.TypicalEntrys;
import seedu.address.testutil.TypicalResumeModel;

public class ResumeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorTest() {
        Model testModel = new TypicalResumeModel();

        // default template, no entries
        Resume blankResume = new Resume(testModel);
        List<ResumeSection> expectedBlankSectionList = new ArrayList<>();
        Template defaultTemplate = Template.getDefaultTemplate();
        for (TemplateSection templateSection : defaultTemplate.getSections()) {
            expectedBlankSectionList.add(new ResumeSection(templateSection.getTitle(), new ArrayList<ResumeEntry>()));
        }
        assertEquals(expectedBlankSectionList, blankResume.getSectionList());

        // default template, typical entries
        List<ResumeEntry> typicalEntries = TypicalEntrys.getTypicalEntries();
        for (ResumeEntry entry : typicalEntries) {
            testModel.addEntry(entry);
        }
        Resume typicalResume = new Resume(testModel);
        // filtering entries manually to get expected section list
        List<ResumeSection> expectedTypicalSectionList = new ArrayList<>();
        for (TemplateSection templateSection : defaultTemplate.getSections()) {
            List<ResumeEntry> testEntryList = new ArrayList<>();
            for (ResumeEntry entry : typicalEntries) {
                if (templateSection.getCategoryPredicate().test(entry) && templateSection.getTagPredicate().test(entry)) {
                    testEntryList.add(entry);
                }
            }
            expectedTypicalSectionList.add(new ResumeSection(templateSection.getTitle(), testEntryList));
        }
        assertEquals(expectedTypicalSectionList, typicalResume.getSectionList());
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
        ResumeSection testSectionTwo = new ResumeSection("section 2", testSectionList);
        assertNotEquals(testSectionOne, testSectionTwo);


        // Resume testing
        Resume testResumeOne = new Resume(new TypicalResumeModel());

        // Same object check
        assertEquals(testResumeOne, testResumeOne);

        // Type check
        assertNotEquals(testResumeOne, new ArrayList<>());

        // Same state
        Resume testResumeTwo = new Resume(new TypicalResumeModel());
        assertEquals(testResumeOne, testResumeTwo);

        // Different state
        Model typicalModel = new TypicalResumeModel();
        typicalModel.addEntry(TypicalEntrys.NUS_EDUCATION);
        assertNotEquals(new Resume(new TypicalResumeModel()), new Resume(typicalModel));
    }
}
