package seedu.address.model.template;

import java.util.ArrayList;
import java.util.function.Predicate;

import seedu.address.model.tag.Tag;

/**
 * A template which specifies the categories and layout for the resume,
 * and which tags are to be included.
 */
public class Template {

    private ArrayList<TemplateSection> sections;

    public Template() {
        this.sections = new ArrayList<TemplateSection>();
    }

    /**
     * Returns a default template with no filters
     */
    private static Template getDefaultTemplate() {
        Template t = new Template();
        t.addSection("Work Experience", "~work", new String[]{});
        t.addSection("Education", "~education", new String[]{});
        t.addSection("Projects", "~projects", new String[]{});
        return t;
    }

    /**
     * Adds a section to the Template
     */
    public void addSection(String title, String category, String[] tags) {
        Predicate<Tag> predicate = t -> true;
        this.sections.add(new TemplateSection(title, category, predicate));
    }

    public ArrayList<TemplateSection> getSections() {
        return new ArrayList<TemplateSection>(sections);
    }
}
