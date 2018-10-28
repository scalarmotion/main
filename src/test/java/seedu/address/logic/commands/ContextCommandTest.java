package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ContextCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullExpression_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ContextCommand(null);
    }

    @Test
    public void constructor_validExpression_success() {
        String expression = "  orbital  ";
        assertEquals(new ContextCommand("orbital"), new ContextCommand(expression.trim()));
    }

    @Test
    public void execute_noMatchingResumeEntry_unableToAddResumeEntry() throws Exception {

        Model model = new ModelManager();

        // currently there are no pre-filled ResumeEntries loaded via sample data, or via Storage.
        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(ContextCommand.MESSAGE_NO_RESUME_ENTRY, "double degree programme"));
        String result = new ContextCommand("ddp").execute(model, commandHistory).feedbackToUser;

    }

}
