package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class TagCommandParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final TagCommandParser parser = new TagCommandParser();

    // TODO: parse sub cmds
    // @Test
    // public void parseCommand_add() throws Exception {
    //     Person person = new PersonBuilder().build();
    //     AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
    //     assertEquals(new AddCommand(person), command);
    // }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        parser.parse("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parse("unknownCommand");
    }
}
