package seedu.address.logic.observers;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Optional;

import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.ui.ContextUpdateEvent;
import seedu.address.model.Model;

/** Represents the service that continually informs the user about ResuMaker's guess */
public class AwarenessService implements CmdLineObserver {

    public static final String MESSAGE_AVAILABLE_ENTRY = "Resume entry available for: %s";
    public static final String MESSAGE_NO_ENTRY = "No resume entry for: %s";
    private static final String SPACE = " ";
    private static final String COMMAND_WORD = "nus";

    private final Model model;

    public AwarenessService(Model model) {
        requireNonNull(model);
        this.model = model;
    }

    @Override
    public Optional<BaseEvent> observe(String currentInput) {
        // TODO Make this method body more readable
        String command = currentInput.split(SPACE)[0];

        if (!command.equals(COMMAND_WORD)) {
            return Optional.empty();
        }

        String args = String.join(SPACE, Arrays.copyOfRange(currentInput.split(SPACE), 1,
                                                                    currentInput.split(SPACE).length));
        String possibleEvent = model.getPossibleEventName(args);

        if (model.getContextualResumeEntry(possibleEvent).isPresent()) {
            return Optional.of(new ContextUpdateEvent(String.format(MESSAGE_AVAILABLE_ENTRY, possibleEvent)));
        } else {
            return Optional.of(new ContextUpdateEvent(String.format(MESSAGE_NO_ENTRY, possibleEvent)));
        }

    }

}
