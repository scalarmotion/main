package seedu.address.model.awareness;

import static java.util.Objects.requireNonNull;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import seedu.address.model.entry.ResumeEntry;
import seedu.address.model.util.SampleDataUtil;

/**
 * This class represents all the Awareness information held by the application.
 * Currently, this includes:
 *     - A dictionary that maps slang to the corresponding full phrases. Slang includes acronyms.
 *         - Example: "cs" --> "computer science"
 *     - A tree set of all full phrases.
 */
public class Awareness {

    private static final String SPACE = " ";

    /** The dictionary maps a given slang to its corresponding full phrase. */
    private final HashMap<String, String> dictionary;

    /** The tree set contains all full phrases. */
    private final TreeSet<String> allFullPhrases;

    /** Maps an Event name to a Resume Entry, if any */
    private TreeMap<String, ResumeEntry> nameToEntryMappings = SampleDataUtil.makeNameToEntryMappings();

    public Awareness(HashMap<String, String> dictionary, TreeSet<String> allFullPhrases) {
        requireAllNonNull(dictionary, allFullPhrases);

        this.dictionary = dictionary;
        this.allFullPhrases = allFullPhrases;

    }

    /** Constructor to create an empty Awareness object */
    public Awareness() {
        this(new HashMap<String, String>(), new TreeSet<String>());
    }

    /**
     * Given an expression, returns a possible Event name. An Event with this name may not actually exist.
     * For example, given the expression "cs", "computer science" is returned as an Event name.
     * However, "computer science" may not be an actual Event.
     *
     * Similarly, given the expression "sg cs innov", "singapore computer science innoventure workshop" may be returned.
     * This may be an actual Event.
     *
     * @param expression an expression is a String containing a combination of slang, partial phrases and full phrases.
     * @return the name of a possible Event, derived using the given expression and user defined contextual
     * information. If NO possible Event name is found, the original expression is returned.
     */
    public String getPossibleEventName(String expression) {
        requireNonNull(expression);

        return Arrays.stream(tokenizeExpression(expression))
                     .map(slang -> getFullPhraseFromSlang(slang))
                     .map(partialPhrase -> getFullPhraseFromPartialPhrase(partialPhrase))
                     .collect(Collectors.joining(SPACE));
    }

    /**
     * Returns a full phrase that corresponds to a particular slang, or the slang itself if no matching full words are
     * found.
     *
     * @param slang a string representing a slang (e.g. "compsci" or "cs"). Slang includes acronyms.
     * @return the full phrase corresponding to the given slang, or the slang itself if no corresponding full phrases
     *         exist.
     */
    private String getFullPhraseFromSlang(String slang) {
        return Optional.ofNullable(dictionary.get(slang))
                       .orElse(slang);
    }

    /**
     * Returns a full phrase that starts the with given partial phrase.
     * For example, if the partial phrase is "comp", it may match with "computer science".
     * If multiple matching full phrases are found, the lexicographically smallest full phrase is returned.
     * If no matching full phrases are found, the partial phrase itself is returned.
     *
     * @param partialPhrase a string representing an incomplete full phrase.
     * @return the name of the lexicographically smallest matching full phrase.
     */
    private String getFullPhraseFromPartialPhrase(String partialPhrase) {
        return Optional.ofNullable(allFullPhrases.ceiling(partialPhrase))
                       .filter(possibleName -> possibleName.startsWith(partialPhrase))
                       .orElse(partialPhrase);
    }

    /**
     * Returns true iff the given string can be considered a valid slang in this application.
     * To be a valid slang, the given string must obey the rules below:
     *   - Must NOT be whitespace.
     *   - Must NOT be an empty string.
     *   - Must NOT be already present in the HashMap contained within the Awareness object.
     *   - Must NOT be more than ONE WORD
     *
     * @param slang the string to be validated
     * @return true iff the given string can be considered a valid slang in this application.
     */
    public boolean isValidSlang(String slang) {
        requireNonNull(slang);
        return !isEmptyString(slang) && !isOnlyWhitespace(slang)
                 && !dictionary.containsKey(slang.trim()) && slang.split(SPACE).length == 1;
    }

    private String[] tokenizeExpression(String expression) {
        return expression.split(SPACE);
    }

    /**
     * Returns an Optional containing a ResumeEntry that matches the given possible event name.
     * Returns an empty Optional if no matching ResumeEntry is found.
     */
    public Optional<ResumeEntry> getContextualResumeEntry(String possibleEventName) {
        return Optional.ofNullable(nameToEntryMappings.get(possibleEventName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Awareness)) {
            return false;
        }

        Awareness otherAwareness = (Awareness) other;

        return this.dictionary.equals(otherAwareness.dictionary)
                 && this.allFullPhrases.equals(otherAwareness.allFullPhrases)
                 && this.nameToEntryMappings.equals(otherAwareness.nameToEntryMappings);
    }


    /**
     * Returns true iff {@code s} contains only whitespace
     */
    public static boolean isOnlyWhitespace(String s) {
        requireNonNull(s);
        return isEmptyString(s.trim());
    }
    /**
     * Returns true iff {@code s} is an empty string
     */
    public static boolean isEmptyString(String s) {
        requireNonNull(s);
        return s.equals("");
    }

}
