package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.commands.TagListCommand;

public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_help_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommandParser.MESSAGE_USAGE));
    }

    @Test
    public void parse_tagList_returnsTagListCommand() {
        // no leading and trailing whitespaces
        TagListCommand expectedTagListCommand =
                new TagListCommand("work", Arrays.asList("java"));
        assertParseSuccess(parser, "list ~work #java", expectedTagListCommand);
        assertParseSuccess(parser, "ls ~work #java", expectedTagListCommand);
    }

}
