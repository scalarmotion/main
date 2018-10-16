package seedu.address.model.template;

import java.util.function.Predicate;

import seedu.address.model.entry.ResumeEntry;

/**
 * A section of the template, representing a category with tags.
 */
public class TemplateSection {
    private String title;
    private Predicate<ResumeEntry> catePredicate;
    private Predicate<ResumeEntry> tagPredicate;

    public TemplateSection(String title, Predicate<ResumeEntry> catePredicate,
        Predicate<ResumeEntry> tagPredicate) {
        this.title = title;
        this.catePredicate = catePredicate;
        this.tagPredicate = tagPredicate;
    }

    public String getTitle() {
        return title;
    }

    public Predicate<ResumeEntry> getCategoryPredicate() {
        return catePredicate;
    }

    public Predicate<ResumeEntry> getTagPredicate() {
        return tagPredicate;
    }
}
