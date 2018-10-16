package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;

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
    public static final String MESSAGE_SUCCESS = "Template loaded from %1$s";
    //TODO: message_file_not_found
    public static final String MESSAGE_NOT_FOUND = "The filepath is invalid";

    private final Path filepath;

    /**
     * Creates a LoadTemplateCommand to load the specified {@code Template}
     */
    public LoadTemplateCommand(Path filepath) {
        requireNonNull(filepath);
        this.filepath = filepath;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        //if (model.hasPerson(toAdd)) {
        //    throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        //}

        //TODO: check for failure and return MESSAGE_NOT_FOUND
        /*
        Problem: unlike other commands (e.g. add, delete), Model does not know
        if the load is successful. Since load raises an event, and the
        StorageManager listens for that event to attempt to load.
        */
        model.loadTemplate(filepath);

        return new CommandResult(String.format(MESSAGE_SUCCESS, filepath));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof LoadTemplateCommand // instanceof handles nulls
            && filepath.equals(((LoadTemplateCommand) other).filepath));
    }
}
