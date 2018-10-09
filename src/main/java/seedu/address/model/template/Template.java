package seedu.address.model.template;

import java.util.ArrayList;

/**
 * A template which specifies the categories and layout for the resume,
 * and which tags are to be included.
 */
public class Template {

    private ArrayList<TemplateSection> sections;

    public Template() {
        this.sections = new ArrayList<TemplateSection>();
    }

    public void addSection(String title, String category, String[] tags) {
        this.sections.add(new TemplateSection(title, category, tags));
    }

    public ArrayList<TemplateSection> getSections() {
        return new ArrayList<TemplateSection>(sections);
    }
}
