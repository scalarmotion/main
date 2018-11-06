package seedu.address.model.template;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.exceptions.InvalidTemplateFileException;
import seedu.address.model.category.Category;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.tag.Tag;

/**
 * A template which specifies the categories and layout for the resume,
 * and which tags are to be included.
 */
public class Template {

    private static final String DELIMITER = ":";
    private static final String AND_DELIMITER = "&";
    private static final String OR_DELIMITER = " ";
    private static final String TEMPLATE_LINE_REGEX = "^[^:]+:~([^:\\s])+:[^:]*$";

    private ArrayList<TemplateSection> sections;
    private String stringRepresentation = "";

    public Template(String filepath) {
        sections = new ArrayList<TemplateSection>();
        stringRepresentation = filepath + "\n\n";
    }

    /**
     * Returns a default template with no filters
     */
    public static Template getDefaultTemplate() {
        Template t = new Template("default_template");
        try {
            t.addSection("Work Experience" + DELIMITER + "~work" + DELIMITER);
            t.addSection("Education" + DELIMITER + "~education" + DELIMITER);
            t.addSection("Projects" + DELIMITER + "~projects" + DELIMITER);
        } catch (InvalidTemplateFileException e) {
            //Exception will never occur in this case
        }
        return t;
    }

    /**
     * Adds a section to the Template
     */
    public void addSection(String line) throws InvalidTemplateFileException {
        if (!line.matches(TEMPLATE_LINE_REGEX)) {
            throw new InvalidTemplateFileException("Specified file has invalid format.");
        }

        /*
         * Work Experience:~work:nus&java nus&c
         * returns (nus AND java) OR (nus AND c)
         */
        String[] parts = line.split(DELIMITER);

        String title = parts[0];
        String cateName = parts[1].substring(1); // remove ~
        String tags = parts.length == 3 ? parts[2].trim() : "";

        Predicate<ResumeEntry> tagPredicate = createTagPredicate(tags);
        Predicate<ResumeEntry> catePredicate = createCategoryPredicate(cateName);

        sections.add(new TemplateSection(title, catePredicate, tagPredicate));

        stringRepresentation += String.format("%s: ~%s\n%s\n", title, cateName, tags.equals("") ? "" : tags + "\n");
    }

    /**
     * Returns a predicate which checks if entry matches tags
     */
    private Predicate<ResumeEntry> createTagPredicate(String tags) {
        if (tags.equals("")) { //no filters
            return entry -> true;
        }

        /*
         * nus&java nus&c recent
         * split sum of products into array of products:
         * [nus&java, nus&c, recent]
         * TODO: see if can be renamed to be clearer
         */
        String[] products = tags.split(OR_DELIMITER);

        ArrayList<ArrayList<Tag>> expressions = new ArrayList<>();

        for (int i = 0; i < products.length; i++) {
            //nus&java
            ArrayList<Tag> andTags = new ArrayList<>();

            for (String and : products[i].split(AND_DELIMITER)) { //[nus, java]
                andTags.add(new Tag(and));
            }
            expressions.add(andTags);
        }

        return entry -> {
            Set<Tag> entryTags = entry.getTags();
            boolean fitsOneExpression = false;
            for (ArrayList<Tag> expression : expressions) {

                boolean fitsAllInExpression = true;
                for (Tag tag : expression) {

                    if (!entryTags.contains(tag)) {
                        fitsAllInExpression = false;
                        break;
                    }
                }

                if (fitsAllInExpression) {
                    fitsOneExpression = true; //found an OR which it fits
                    break;
                }
            }
            return fitsOneExpression;
        };
    }

    private Predicate<ResumeEntry> createCategoryPredicate(String category) {
        return entry -> entry.getCategory().equals(new Category(category));
    }

    public ArrayList<TemplateSection> getSections() {
        return new ArrayList<TemplateSection>(sections);
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof Template)) {
            return false;
        }

        // state check
        Template other = (Template) obj;
        return stringRepresentation.equals(other.stringRepresentation);
    }
}
