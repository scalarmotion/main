package seedu.address.model.resume;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.template.Template;
import seedu.address.model.template.TemplateSection;

public class ResumeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorTest() {
        Model testModel = new ModelManager();
        Resume resume = new Resume("testname", testModel);
        // basic attributes
        assertEquals(resume.name, "testname");

        // default template, no entries
        List<ResumeSection> expectedSectionList = new ArrayList<>();
        Template defaultTemplate = Template.getDefaultTemplate();
        for (TemplateSection templateSection : defaultTemplate.getSections()) {
            expectedSectionList.add(new ResumeSection(templateSection.getTitle(), new ArrayList<ResumeEntry>()));
        }
        assertEquals(resume.getSectionList(), expectedSectionList);
    }
}
