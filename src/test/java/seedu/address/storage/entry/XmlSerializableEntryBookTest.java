package seedu.address.storage.entry;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.EntryBook;
import seedu.address.model.category.Category;
import seedu.address.model.entry.EntryInfo;
import seedu.address.testutil.TypicalEntrys;

public class XmlSerializableEntryBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlSerializableEntryBookTest");
    private static final Path TYPICAL_ENTRIES_FILE = TEST_DATA_FOLDER.resolve("typicalEntryBook.xml");
    private static final Path INVALID_ENTRYTITLE_FILE = TEST_DATA_FOLDER.resolve("invalidEntryTitle.xml");
    private static final Path MISSING_BULLETS_FILE = TEST_DATA_FOLDER.resolve("missingBullets.xml");
    private static final Path MISSING_CATEGORY_FILE = TEST_DATA_FOLDER.resolve("missingCategory.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalEntriesFile_success() throws Exception {
        XmlSerializableEntryBook dataFromFile = XmlUtil.getDataFromFile(TYPICAL_ENTRIES_FILE,
                XmlSerializableEntryBook.class);
        EntryBook entryBookFromFile = dataFromFile.toModelType();
        EntryBook typicalEntryBook = TypicalEntrys.getTypicalEntryBook();
        assertEquals(entryBookFromFile, typicalEntryBook);
    }

    @Test
    public void toModelType_invalidEntryTitle_failure() throws Exception {
        XmlSerializableEntryBook dataFromFile = XmlUtil.getDataFromFile(INVALID_ENTRYTITLE_FILE,
                XmlSerializableEntryBook.class);

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(EntryInfo.MESSAGE_ENTRYINFO_CONSTRAINTS);

        dataFromFile.toModelType();

    }

    @Test
    public void toModelType_missingBullets_failure() throws Exception {
        XmlSerializableEntryBook dataFromFile = XmlUtil.getDataFromFile(MISSING_BULLETS_FILE,
                XmlSerializableEntryBook.class);

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlAdaptedEntryDescription.MESSAGE_MISSING_BULLETS);

        dataFromFile.toModelType();

    }

    @Test
    public void toModelType_missingCategory_failure() throws Exception {
        XmlSerializableEntryBook dataFromFile = XmlUtil.getDataFromFile(MISSING_CATEGORY_FILE,
                XmlSerializableEntryBook.class);

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(String.format(XmlAdaptedResumeEntry.MISSING_FIELD_MESSAGE_FORMAT,
                                                   Category.class.getSimpleName()));

        dataFromFile.toModelType();

    }


}
