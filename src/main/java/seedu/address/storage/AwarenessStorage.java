package seedu.address.storage;

import java.io.FileNotFoundException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.awareness.Awareness;

/**
 * Represents storage for the Awareness object. Provides a high-level API to read the Awareness data.
 */
public interface AwarenessStorage {

    public Awareness readAwarenessData() throws DataConversionException, FileNotFoundException;

}
