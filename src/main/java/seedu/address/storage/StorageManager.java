package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.TemplateLoadRequestedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.events.storage.TemplateLoadedEvent;
import seedu.address.commons.events.storage.TemplateLoadingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.template.Template;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private TemplateStorage templateStorage;


    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.templateStorage = new TxtTemplateStorage();
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }

    // ================ Template methods ==============================

    @Override
    public Path getTemplateFilePath() {
        return templateStorage.getTemplateFilePath();
    }

    @Override
    public Optional<Template> loadTemplate() throws IOException {
        return loadTemplate(templateStorage.getTemplateFilePath());
    }

    /**
     * Loads a Template from a text file
     */
    @Override
    public Optional<Template> loadTemplate(Path filePath) throws IOException {
        logger.fine("Attempting to load template from: " + filePath);
        return templateStorage.loadTemplate(filePath);
    }

    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    @Subscribe
    public void handleTemplateLoadRequestedEvent(TemplateLoadRequestedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Template load requested, attempting to load"));
        Optional<Template> t;
        /*TODO
        Should an Optional be used here? (Consistent with how XmlAddressBookStorage works)
        If it's empty, does it represent that an
        error has occurred during loadTemplate()? And if so, should that error be
        propagated up and caught here instead, so it can be passed into the
        ExceptionEvent instead of dealing with an empty optional with no
        exception info?
        */
        try {
            t = loadTemplate(event.filepath);
            t.ifPresent(
                template -> raise(new TemplateLoadedEvent(template, event.filepath)));
            //TODO make model listen for TemplateLoadedEvent
        } catch (IOException e) {
            raise(new TemplateLoadingExceptionEvent(e, event.filepath));
        }
    }
}
