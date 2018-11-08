package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.EntryBookChangedEvent;
import seedu.address.commons.events.model.ResumeSaveEvent;
import seedu.address.commons.events.model.TemplateLoadRequestedEvent;
import seedu.address.commons.events.storage.TemplateLoadedEvent;
import seedu.address.commons.events.storage.TemplateLoadingExceptionEvent;
import seedu.address.model.awareness.Awareness;
import seedu.address.model.category.CategoryManager;
import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.person.Person;
import seedu.address.model.resume.Resume;
import seedu.address.model.tag.TagManager;
import seedu.address.model.template.Template;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final FilteredList<Person> filteredPersons;
    private final Awareness awareness;
    private final UserParticulars userParticulars;
    private Optional<Template> loadedTemplate;
    private Resume lastGeneratedResume;
    private final VersionedEntryBook versionedEntryBook;
    private final FilteredList<ResumeEntry> filteredEntries;

    // Tag and Category managers
    private final TagManager tagManager;
    private final CategoryManager categoryManager;

    /**
     * Initializes a ModelManager with the given entrybook, userPrefs and awareness object
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyEntryBook entryBook, UserPrefs userPrefs,
                        Awareness awareness) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedAddressBook(addressBook);
        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        userParticulars = userPrefs.getUserParticulars();
        loadedTemplate = Optional.empty();
        this.awareness = awareness;
        versionedEntryBook = new VersionedEntryBook(entryBook);
        filteredEntries = new FilteredList<>(versionedEntryBook.getEntryList());

        tagManager = new TagManager();
        categoryManager = new CategoryManager();
    }

    public ModelManager() {
        this(new AddressBook(), new EntryBook(), new UserPrefs(), new Awareness());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        versionedAddressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public ReadOnlyEntryBook getEntryBook() {
        return versionedEntryBook;
    }


    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    private void indicateEntryBookChanged() {
        raise (new EntryBookChangedEvent(versionedEntryBook));
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    // to be modified
    public boolean hasEntry(ResumeEntry entry) {
        requireNonNull(entry);
        return versionedEntryBook.hasEntry(entry);
    }

    @Override
    public void addEntry(ResumeEntry entry) {
        versionedEntryBook.addEnty(entry);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        indicateEntryBookChanged();
    }
    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteEntry(ResumeEntry target) {
        versionedEntryBook.removeEntry(target);
        indicateEntryBookChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public void updateEntry(ResumeEntry target, ResumeEntry editedEntry) {
        requireAllNonNull(target, editedEntry);

        versionedEntryBook.updateEntry(target, editedEntry);
        indicateAddressBookChanged();
    }
    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedEntryBook}, if arguments given, will filter full list instead.
     */
    @Override
    public ObservableList<ResumeEntry> getFilteredEntryList() {
        return FXCollections.unmodifiableObservableList(filteredEntries);
    }

    @Override
    public ObservableList<ResumeEntry> getFilteredEntryList(Predicate<ResumeEntry> predicate) {
        FilteredList<ResumeEntry> entries = getFullEntryList();
        entries.setPredicate(predicate);

        return FXCollections.unmodifiableObservableList(entries);
    }

    @Override
    public ObservableList<ResumeEntry> getFilteredEntryList(String category, List<String> tags) {
        return getFilteredEntryList(mkPredicate(category, tags));
    }

    @Override
    public Predicate<ResumeEntry> mkPredicate(String category, List<String> tags) {
        Predicate<ResumeEntry> predicate = category.length() > 0
            ? categoryManager.mkPredicate(category)
            : PREDICATE_SHOW_ALL_ENTRIES;

        return tags.size() > 0
            ? tagManager.mkPredicate(predicate, tags)
            : predicate;
    }

    private FilteredList<ResumeEntry> getFullEntryList() {
        return new FilteredList(versionedEntryBook.getEntryList());
    }

    @Override
    public void updateFilteredEntryList(Predicate<ResumeEntry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }
    //=========== Particulars ===============================================================================

    @Override
    public UserParticulars getUserParticulars() {
        return userParticulars;
    }

    //=========== Template ==================================================================================

    @Override
    public void loadTemplate(Path filepath) {
        raise(new TemplateLoadRequestedEvent(filepath));
    }

    @Override
    public Optional<Template> getLoadedTemplate() {
        return loadedTemplate;
        //will be up to the Generation part to raise NewResultAvailableEvent to say no template loaded
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
        indicateAddressBookChanged();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    @Override
    public void commitEntryBook() {
        versionedEntryBook.commit();
    }

    //=========== Awareness accessors =======================================================================
    @Override
    public String getPossibleEventName(String expression) {
        return awareness.getPossibleEventName(expression);
    }

    @Override
    public Optional<ResumeEntry> getContextualResumeEntry(String possibleEventName) {
        return awareness.getContextualResumeEntry(possibleEventName);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && filteredPersons.equals(other.filteredPersons)
                && versionedEntryBook.equals(other.versionedEntryBook)
                && filteredEntries.equals(other.filteredEntries)
                && categoryManager.equals(other.categoryManager)
                && tagManager.equals(other.tagManager)
                && loadedTemplate.equals(other.loadedTemplate)/*
                && awareness.equals(other.awareness)
                && lastGeneratedResume.equals(other.lastGeneratedResume)*/;
    }

    //=========== Resume generation =======================================================================
    public void generateResume() {
        lastGeneratedResume = new Resume(this);
    }

    public Optional<Resume> getLastResume() {
        return Optional.ofNullable(lastGeneratedResume);
    }

    public void saveLastResume(Path filepath) {
        raise(new ResumeSaveEvent(lastGeneratedResume, filepath));
    }

    //=========== Listener for template loading =============================================================
    @Subscribe
    public void handleTemplateLoadedEvent(TemplateLoadedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                "Template loaded from " + event.filepath.toString() + " to Model"));
        loadedTemplate = Optional.of(event.getTemplate());
    }

    @Subscribe
    public void handleTemplateLoadingExceptionEvent(TemplateLoadingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Exception when attempting to load template from "
                + event.filepath.toString()));
        loadedTemplate = Optional.empty();
    }
}
