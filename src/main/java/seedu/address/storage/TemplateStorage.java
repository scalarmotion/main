package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.model.template.Template;

/**
 * Represents a storage for {@link seedu.address.model.template.Template}.
 */
public interface TemplateStorage {

    Path getTemplateFilePath();

    Template loadTemplate() throws IOException;

    /**
     * Similar to {@link #loadTemplate()}
     *
     * @param filePath location of the data. Cannot be null.
     * @throws IOException if the file is not found.
     */
    Template loadTemplate(Path filePath)
        throws IOException;
}
