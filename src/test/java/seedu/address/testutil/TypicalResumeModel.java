package seedu.address.testutil;

import java.util.Optional;

import seedu.address.model.ModelManager;
import seedu.address.model.template.Template;

/**
 * An extension of the ModelManager for testing purposes. Returns the default template as its loaded template
 * to bypass the usual requirement for templates to be loaded through storage.
 */
public class TypicalResumeModel extends ModelManager {
    public TypicalResumeModel() {
        super();
    }

    @Override
    public Optional<Template> getLoadedTemplate() {
        return Optional.of(Template.getDefaultTemplate());
    }
}
