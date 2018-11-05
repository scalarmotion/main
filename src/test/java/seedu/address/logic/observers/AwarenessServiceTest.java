package seedu.address.logic.observers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.events.ui.ContextUpdateEvent;
import seedu.address.model.AddressBook;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.awareness.Awareness;
import seedu.address.model.util.SampleDataUtil;

public class AwarenessServiceTest {

    private static final Awareness typicalAwareness = SampleDataUtil.getSampleAwareness();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullModel() {
        thrown.expect(NullPointerException.class);
        AwarenessService awarenessService = new AwarenessService(null);
    }

    @Test
    public void observe_miscInput() {
        thrown.expect(NoSuchElementException.class);

        Model model = new ModelManager();
        AwarenessService awarenessService = new AwarenessService(model);
        awarenessService.observe("test input").get();

    }

    @Test
    public void observe_relevantInput() {
        Model model = new ModelManager(new AddressBook(), new EntryBook(), new UserPrefs(), typicalAwareness);
        AwarenessService awarenessService = new AwarenessService(model);

        ContextUpdateEvent actual = (ContextUpdateEvent) awarenessService.observe("nus ta ma1101r").get();

        ContextUpdateEvent expected = new ContextUpdateEvent(String.format(AwarenessService.MESSAGE_AVAILABLE_ENTRY,
                                                                                   "teaching assistant ma1101r"));

        assertEquals(actual.message, expected.message);
    }

}
