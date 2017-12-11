package pl.regzand.bibparser.entries;

import pl.regzand.bibparser.exceptions.BibException;
import pl.regzand.bibparser.values.BibValue;

/**
 * Data container for BibTeX entry
 *
 * @see <a href="http://www.bibtex.org/Format/">BibTeX Format Description</a>
 */
public abstract class BibEntry {

    // ==============================================================================
    // === CLASS
    // ==============================================================================

    private final String entryName;
    private final String id;

    /**
     * Creates BibEntry
     *
     * @param entryName name of entry type
     * @param id        unique entry id
     */
    public BibEntry(String entryName, String id) {
        this.entryName = entryName;
        this.id = id;
    }

    /**
     * Returns BibTeX entry entryName
     *
     * @return BibTeX entry entryName
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * Returns BibTeX entry id
     *
     * @return BibTeX entry id
     */
    public String getId() {
        return id;
    }

    /**
     * Validates entry, should be called after all fields are injected
     *
     * @throws BibException
     */
    public void validate() throws BibException {

    }

    // ==============================================================================
    // === ABSTRACT METHODS
    // ==============================================================================

    /**
     * Returns title of BibTeX entry
     *
     * @return title of BibTeX entry
     */
    public abstract BibValue getTitle();

    /**
     * Returns author of BibTeX entry
     *
     * @return author of BibTeX entry
     */
    public abstract BibValue getAuthors();

    /**
     * Returns year of publication of BibTeX entry
     *
     * @return year of publication of BibTeX entry
     */
    public abstract BibValue getYear();

    /**
     * Returns month of publication of BibTeX entry
     *
     * @return month of publication of BibTeX entry
     */
    public abstract BibValue getMonth();

    /**
     * Returns note about BibTeX entry
     *
     * @return note about BibTeX entry
     */
    public abstract BibValue getNote();

    /**
     * Returns key of BibTeX entry
     *
     * @return key of BibTeX entry
     */
    public abstract BibValue getKey();

}
