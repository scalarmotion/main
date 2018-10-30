package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.util.FileUtil;
import seedu.address.model.resume.Resume;

public class MarkdownResumeStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "MarkdownResumeStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private Path addToTestDataPathIfNotNull(String userPrefsFileInTestDataFolder) {
        return userPrefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(userPrefsFileInTestDataFolder)
                : null;
    }
    /*
    private Resume getTypicalResume() {
        // Does not work because still pending ability to import resume manually
        return new Resume(new ModelManager());
    }*/

    @Test
    public void saveResume_nullResume_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveResume(null, "SomeFile.md");
    }

    /*@Test
    public void saveResume_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveResume(getTypicalResume(), null);
    }*/

    /**
     * Saves {@code resume} at the specified {@code resumeFileInTestDataFolder} filepath.
     */
    private void saveResume(Resume resume, String resumeFileInTestDataFolder) {
        try {
            new MarkdownResumeStorage(addToTestDataPathIfNotNull(resumeFileInTestDataFolder))
                    .saveResume(resume);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file", ioe);
        }
    }

    /**
     * Reads a {@code resume} from the specified {@code filePath}.
     */
    private String readTestResume(Path filePath) {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            throw new AssertionError("The file should exist");
        }

        String resumeText;

        try {
            resumeText = FileUtil.readFromFile(filePath);
        } catch (IOException e) {
            throw new AssertionError("There should not be an error reading the file", e);
        }

        return resumeText;
    }

    /*
    @Test
    public void saveResume_allInOrder_success() throws DataConversionException, IOException {
        Path resumeFilePath = testFolder.getRoot().toPath().resolve("testresume.md");
        MarkdownResumeStorage markdownResumeStorage = new MarkdownResumeStorage(resumeFilePath);
        assertEquals(markdownResumeStorage.getResumeFilePath(), resumeFilePath);
        String original = MarkdownConverter.toMarkdown(getTypicalResume());

        //Try writing when the file doesn't exist
        markdownResumeStorage.saveResume(getTypicalResume());
        String readBack = readTestResume(resumeFilePath);
        assertEquals(original, readBack);

        //Try saving when the file exists
        markdownResumeStorage.saveResume(getTypicalResume());
        readBack = readTestResume(resumeFilePath);
        assertEquals(original, readBack);
    }*/

}
