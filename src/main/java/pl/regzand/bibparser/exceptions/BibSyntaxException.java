package pl.regzand.bibparser.exceptions;

/**
 * Exception thrown by {@link pl.regzand.bibparser.parser.BibParser BibParser} when it encounters syntax error in parsed data
 */

import com.sun.istack.internal.Nullable;

public class BibSyntaxException extends BibException {

    private final String message;
    private final String entryName;

    public BibSyntaxException(String message, String entryName) {
        super("Parser encountered syntax exception \"" + message + "\" in @" + entryName + " entry");
        this.message = message;
        this.entryName = entryName;
    }

    /**
     * Returns syntax error message returned by parser
     *
     * @return syntax error message
     */
    public String getSyntaxMessage() {
        return message;
    }

    /**
     * Returns name of the entry in which this error occurred
     *
     * @return name of entry or null if error occurred outside any entry
     */
    @Nullable
    public String getEntryName() {
        return entryName;
    }
}
