package seedu.address.model.template;

/**
 * A section of the template, representing a category with tags.
 */
public class TemplateSection {
    private String title;
    private String category;
    private String[] tags;

    public TemplateSection(String title, String category, String[] tags) {
        this.title = title;
        this.category = category;
        this.tags = tags;
    }
}
