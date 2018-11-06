package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.ResumeSaveEvent;
import seedu.address.commons.events.model.TemplateLoadRequestedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.events.storage.TemplateLoadedEvent;
import seedu.address.commons.events.storage.TemplateLoadingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.InvalidTemplateFileException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.resume.Resume;
import seedu.address.model.template.Template;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private TemplateStorage templateStorage;
    private ResumeStorage resumeStorage;


    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.templateStorage = new TxtTemplateStorage();
        this.resumeStorage = new MarkdownResumeStorage();
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
    public Template loadTemplate() throws IOException, InvalidTemplateFileException {

        return loadTemplate(templateStorage.getTemplateFilePath());
    }

    @Override
    public Template loadTemplate(Path filePath) throws IOException, InvalidTemplateFileException {
        requireNonNull(filePath);

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

    @Override
    @Subscribe
    public void handleTemplateLoadRequestedEvent(TemplateLoadRequestedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Template load from "
                + event.filepath.toString() + " requested, attempting to load"));
        /*
         * loadTemplate directly returns a Template rather than Optional<Template>, so that
         * in case loading throws an exception, it will be propagated here, and can be caught to raise
         * a TemplateLoadingExceptionEvent (rather than having an Optional which gives no information).
         * Although existing code e.g. for retrieving AddressBook uses Optional, in those cases, Optional is
         * checked in MainApp, which will simply initialize with the default AddressBook. In this case of Templates,
         * exceptions should be passed to an event.
         */
        Template t;
        try {
            t = loadTemplate(event.filepath);
            raise(new TemplateLoadedEvent(t, event.filepath));
        } catch (IOException | InvalidTemplateFileException e) {
            raise(new TemplateLoadingExceptionEvent(e, event.filepath));
        }
    }

    // ================ Resume methods ==============================

    @Override
    public Path getResumeFilePath() {
        return resumeStorage.getResumeFilePath();
    }

    @Override
    public void saveResume(Resume resume) throws IOException {
        saveResume(resume, resumeStorage.getResumeFilePath());
    }

    @Override
    public void saveResume(Resume resume, Path filePath) throws IOException {
        requireNonNull(filePath);
        requireNonNull(resume);
        logger.fine("Attempting to write to data file: " + filePath);
        resumeStorage.saveResume(resume, filePath);
    }


    @Subscribe
    public void handleResumeSaveEvent(ResumeSaveEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Save resume to "
                + event.filepath.toString() + " requested, attempting to save"));

        try {
            saveResume(event.data, event.filepath);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
}
