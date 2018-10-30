package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.storage.TemplateLoadedEvent;
import seedu.address.commons.events.storage.TemplateLoadingExceptionEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Loads a template from file.
 */
public class LoadTemplateCommand extends Command {

    public static final String COMMAND_WORD = "loadtemplate";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Loads a template file. "
        + "Parameters: "
        + "FILEPATH\n"
        + "Example: " + COMMAND_WORD + " "
        + "template1.txt";
    public static final String MESSAGE_SUCCESS = "Successful load from %1$s.";
    public static final String MESSAGE_NOT_FOUND = "Loading from %1$s failed.";

    private final Path filepath;
    private boolean isSuccessful;

    //TESTING
    private final Logger logger = LogsCenter.getLogger(getClass());

    /**
     * Creates a LoadTemplateCommand to load the specified {@code Template}
     */
    public LoadTemplateCommand(Path filepath) {
        requireNonNull(filepath);
        this.filepath = filepath;
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        model.loadTemplate(filepath);

        if (!isSuccessful) {
            return new CommandResult(String.format(MESSAGE_NOT_FOUND, filepath));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, filepath));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof LoadTemplateCommand // instanceof handles nulls
            && filepath.equals(((LoadTemplateCommand) other).filepath));
    }

    @Subscribe
    public void handleTemplateLoadedEvent(TemplateLoadedEvent event) {
        isSuccessful = true;
        logger.info("-------SUCESSFUL LOAD---------");
    }

    @Subscribe
    public void handleTemplateLoadingExceptionEvent(TemplateLoadingExceptionEvent event) {
        isSuccessful = false;
        logger.info("-------FAILED LOAD---------");
    }
}

