package seedu.address.storage.entry;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEntryBook;

/**
 * Represents a storage for {@link seedu.address.model.EntryBook}.
 */
public interface EntryBookStorage {

    /**
     * Returns EntryBook data as a {@link seedu.address.model.ReadOnlyEntryBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException;

}
