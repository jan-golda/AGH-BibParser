package pl.regzand.bibparser.exceptions;

/**
 * Exception thrown by {@link pl.regzand.bibparser.parser.BibParser BibParser} when it encounters unknown entry type in parsed data
 */
public class BibUnknownEntryException extends BibException {

    private final String entryName;

    public BibUnknownEntryException(String entryName) {
        super("Parser encountered unknown entry: " + entryName);

        this.entryName = entryName;
    }

    /**
     * Returns name of the unrecognized entry
     *
     * @return name of the entry
     */
    public String getEntryName() {
        return entryName;
    }

}
