package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalEntrys.NUS_EDUCATION;
import static seedu.address.testutil.TypicalEntrys.WORK_FACEBOOK;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.awareness.Awareness;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.resume.Resume;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.EntryBookBuilder;
import seedu.address.testutil.TypicalResumeModel;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasPerson(null);
    }

    @Test
    public void hasEntry_nullEntry_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasEntry(null);
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasEntry_entryInEntryBook_returnsTrue() {
        modelManager.addEntry(NUS_EDUCATION);
        assertTrue(modelManager.hasEntry(NUS_EDUCATION));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredPersonList().remove(0);
    }

    @Test
    public void getFilteredEntryList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredEntryList().remove(0);
    }

    @Test
    public void getFilteredEntryList_filtered_predicateWorks() {
        modelManager = new ModelManager();

        modelManager.addEntry(NUS_EDUCATION);
        modelManager.addEntry(WORK_FACEBOOK);

        // mkPredicate works for single category and tag
        assertTrue(modelManager.getFilteredEntryList("work", Arrays.asList("java")).size() == 1);

        // mkPredicate works for single category and tag, cannot find
        assertTrue(modelManager.getFilteredEntryList("education", Arrays.asList("java")).size() == 0);
    }

    @Test
    public void mkPredicate_filtered_predicateWorks() {
        modelManager = new ModelManager();

        modelManager.addEntry(NUS_EDUCATION);
        modelManager.addEntry(WORK_FACEBOOK);

        // mkPredicate works for single category and tag
        modelManager.updateFilteredEntryList(modelManager.mkPredicate("work", Arrays.asList("java")));
        assertTrue(modelManager.getFilteredEntryList().size() == 1);

        // mkPredicate works for single category and tag, cannot find
        modelManager.updateFilteredEntryList(modelManager.mkPredicate("work", Arrays.asList("nus")));
        assertTrue(modelManager.getFilteredEntryList().size() == 0);

        // mkPredicate works for tag only
        modelManager.updateFilteredEntryList(modelManager.mkPredicate("", Arrays.asList("machinelearning")));
        assertTrue(modelManager.getFilteredEntryList().size() == 1);

        // mkPredicate works for category only
        modelManager.updateFilteredEntryList(modelManager.mkPredicate("work", new ArrayList<String>()));
        assertTrue(modelManager.getFilteredEntryList().size() == 1);
    }

    @Test
    public void getAndGenerateResume() {
        Model testResumeModel = new TypicalResumeModel();
        assertFalse(testResumeModel.getLastResume().isPresent());
        testResumeModel.generateResume();
        Resume actual = testResumeModel.getLastResume().orElseThrow(AssertionError::new);
        assertEquals(actual, new Resume(testResumeModel));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();
        Awareness awareness = new Awareness();
        EntryBook entryBook = new EntryBookBuilder().withEntry(WORK_FACEBOOK).withEntry(NUS_EDUCATION).build();
        EntryBook differentEntryBook = new EntryBook();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, entryBook, userPrefs, awareness);
        ModelManager modelManagerCopy = new ModelManager(addressBook, entryBook, userPrefs, awareness);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, differentEntryBook, userPrefs,
                                                                 awareness)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, entryBook, userPrefs, awareness)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        modelManager.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setEntryBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(addressBook, entryBook, differentUserPrefs, awareness)));
    }
}
