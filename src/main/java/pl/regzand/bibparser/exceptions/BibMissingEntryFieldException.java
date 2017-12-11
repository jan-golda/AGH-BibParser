package pl.regzand.bibparser.exceptions;

/**
 * Exception thrown by {@link pl.regzand.bibparser.parser.BibParser BibParser} when entry there was not value for field marked as required
 */
public class BibMissingEntryFieldException extends BibException {

    private final String entryId;
    private final String fieldName;

    public BibMissingEntryFieldException(String entryId, String fieldName) {
        super("Parser encountered missing value for required field \"" + fieldName + "\" in \"" + entryId + "\" entry");
        this.entryId = entryId;
        this.fieldName = fieldName;
    }

    /**
     * Returns id of the entry for which this exception occurred
     *
     * @return id of the entry
     */
    public String getEntryId() {
        return entryId;
    }

    /**
     * Returns name if the field that was required
     *
     * @return namoe of the field
     */
    public String getFieldName() {
        return fieldName;
    }
}
