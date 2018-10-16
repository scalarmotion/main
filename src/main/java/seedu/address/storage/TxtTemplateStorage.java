package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.template.Template;

/**
 * Represents a storage for {@link seedu.address.model.template.Template}.
 */
public class TxtTemplateStorage implements TemplateStorage {

    private static final Logger logger = LogsCenter.getLogger(TxtTemplateStorage.class);

    private Path filePath;

    public TxtTemplateStorage() {
    }

    public TxtTemplateStorage(Path filePath) {
        this.filePath = filePath;
    }

    //@Override
    public Path getTemplateFilePath() {
        return filePath;
    }

    //@Override
    public Optional<Template> loadTemplate() throws IOException {
        return loadTemplate(filePath);
    }

    /**
     * Similar to {@link #loadTemplate()}
     *
     * @param filePath location of the data. Cannot be null.
     * @throws IOException if the file is not found.
     */
    public Optional<Template> loadTemplate(Path filePath)
        throws IOException {
        /*TODO:
        should it return a Optional<Template> or just Template?
        XmlAddrBkStorage returns Optional to Main, and Main creates a default
        AddrBk if Optional is empty.

        TxtTemplateStorage returns <VALUE> to StorageManager's
        HandleTemplateLoadRequested() which was triggered by a TemplateLoadRequestedEvent
        HandleTemp... then raises a TemplateLoadedEvent or TemplateLoadingExceptionEvent
        */
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("Template file " + filePath + " not found");
            return Optional.empty();
        }

        File file = filePath.toFile();
        Scanner s = new Scanner(file);
        Template template = new Template();

        while (s.hasNextLine()) {
            String curr = s.nextLine();
            template.addSection(curr); //TODO: should this be in template?
        }
        return Optional.of(template);
    }
}


