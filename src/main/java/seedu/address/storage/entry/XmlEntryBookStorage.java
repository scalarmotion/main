package seedu.address.storage.entry;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.ReadOnlyEntryBook;

/**
 * A class to access entrybook data stored as an xml file on the hard disk.
 */
public class XmlEntryBookStorage implements EntryBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlEntryBookStorage.class);

    private Path filePath;

    public XmlEntryBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("Entrybook file " + filePath + " not found");
            return Optional.empty();
        }

        try {
            XmlSerializableEntryBook xmlEntryBook = XmlUtil.getDataFromFile(filePath, XmlSerializableEntryBook.class);

            return Optional.of(xmlEntryBook.toModelType());
        } catch (JAXBException jaxbe) {
            logger.info("There is a problem in the XML formatting in " + filePath + ": " + jaxbe.getMessage());
            throw new DataConversionException(jaxbe);
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

}
