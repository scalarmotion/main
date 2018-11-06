package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

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
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage, TemplateStorage, ResumeStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    @Override
    Path getTemplateFilePath();

    @Override
    Template loadTemplate() throws IOException, InvalidTemplateFileException;

    @Override
    Template loadTemplate(Path filePath) throws IOException, InvalidTemplateFileException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleAddressBookChangedEvent(AddressBookChangedEvent abce);

    /**
     * Attempts to load the template from the hard disk.
     * Raises {@link TemplateLoadedEvent} if it is successful, or {@link TemplateLoadingExceptionEvent}
     * if there was an error during saving.
     */
    void handleTemplateLoadRequestedEvent(TemplateLoadRequestedEvent event);

    @Override
    void saveResume(Resume resume) throws IOException;

    @Override
    void saveResume(Resume resume, Path filePath) throws IOException;

    void handleResumeSaveEvent(ResumeSaveEvent sre);
}
