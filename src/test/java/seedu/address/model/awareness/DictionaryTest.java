package seedu.address.model.awareness;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;



public class DictionaryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void registerValidMapping_success() throws Exception {
        Dictionary dictionary = new Dictionary();
        dictionary.registerMapping("cs", "computer science");

        String expectedEventName = "computer science";
        String actualEventName = dictionary.getPossibleEventName("cs");

        assertEquals(actualEventName, expectedEventName);
    }

    @Test
    public void registerTrailingWhitespaceMapping_success() throws Exception {
        Dictionary dictionary = new Dictionary();

        dictionary.registerMapping(" sep  ", "    student exchange programme  ");
        String expectedEventName = "student exchange programme";
        String actualEventName = dictionary.getPossibleEventName("sep");

        assertEquals(actualEventName, expectedEventName);
    }

    @Test
    public void registerDuplicateFullPhrase_success() throws Exception {
        Dictionary dictionary = new Dictionary();

        dictionary.registerMapping(" sep  ", "    student exchange programme  ");
        dictionary.registerMapping(" abroad  ", "student exchange programme");

        String expectedEventName = "student exchange programme";
        String actualEventNameA = dictionary.getPossibleEventName("sep");
        String actualEventNameB = dictionary.getPossibleEventName("abroad");

        assertEquals(actualEventNameA, expectedEventName);
        assertEquals(actualEventNameB, expectedEventName);
    }


    @Test
    public void registerDuplicateSlang_failure() throws Exception {
        Dictionary dictionary = new Dictionary();
        dictionary.registerMapping("cs", "computer science");

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Dictionary.MESSAGE_SLANG_CONSTRAINTS);

        dictionary.registerMapping("cs", "counter strike");
    }

    @Test
    public void registerEmptySlang_failure() throws Exception {
        Dictionary dictionary = new Dictionary();

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Dictionary.MESSAGE_SLANG_CONSTRAINTS);
        dictionary.registerMapping("", "computer science");
    }

    @Test
    public void registerWhitespaceSlang_failure() throws Exception {
        Dictionary dictionary = new Dictionary();

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Dictionary.MESSAGE_SLANG_CONSTRAINTS);
        dictionary.registerMapping("    ", "computer science");
    }

    @Test
    public void registerMultiWordSlang_failure() throws Exception {
        Dictionary dictionary = new Dictionary();

        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Dictionary.MESSAGE_SLANG_CONSTRAINTS);
        dictionary.registerMapping("comp sci", "computer science");
    }



    @Test
    public void equals() throws Exception {

        Dictionary dictionary = new Dictionary();
        dictionary.registerMapping("cs", "computer science");

        Dictionary otherDictionary = new Dictionary();

        // identical object --> true
        assertTrue(dictionary.equals(dictionary));

        // Dictionary with different mappings --> false
        assertFalse(dictionary.equals(otherDictionary));

        // Dictionary with same mappings --> true
        otherDictionary.registerMapping("cs", "computer science");
        assertTrue(dictionary.equals(dictionary));

    }
}
