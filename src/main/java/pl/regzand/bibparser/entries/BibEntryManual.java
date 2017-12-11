package pl.regzand.bibparser.entries;

import pl.regzand.bibparser.parser.BibField;
import pl.regzand.bibparser.values.BibValue;

/**
 * Data container for BibTeX @Manual entry
 *
 * @see <a href="https://pl.wikipedia.org/wiki/BibTeX#Struktura_plik.C3.B3w_bazy_bibliograficznej">BibTeX entry fields</a>
 */
public class BibEntryManual extends BibEntry {

    @BibField(name = "title", required = true)
    public BibValue title;

    // ==============================================================================
    // === INJECTABLE FIELDS
    // ==============================================================================
    @BibField(name = "author", names = true)
    public BibValue author;
    @BibField(name = "organization")
    public BibValue organization;
    @BibField(name = "address")
    public BibValue address;
    @BibField(name = "edition")
    public BibValue edition;
    @BibField(name = "month")
    public BibValue month;
    @BibField(name = "year")
    public BibValue year;
    @BibField(name = "note")
    public BibValue note;
    @BibField(name = "key")
    public BibValue key;

    /**
     * Creates entry
     *
     * @param id unique entry id
     */
    public BibEntryManual(String id) {
        super("Manual", id);
    }

    // ==============================================================================
    // === GETTERS
    // ==============================================================================

    /**
     * Returns title of BibTeX entry
     *
     * @return title of BibTeX entry
     */
    @Override
    public BibValue getTitle() {
        return title;
    }

    /**
     * Returns author of BibTeX entry
     *
     * @return author of BibTeX entry
     */
    @Override
    public BibValue getAuthors() {
        return author;
    }

    /**
     * Returns year of publication of BibTeX entry
     *
     * @return year of publication of BibTeX entry
     */
    @Override
    public BibValue getYear() {
        return year;
    }

    /**
     * Returns month of publication of BibTeX entry
     *
     * @return month of publication of BibTeX entry
     */
    @Override
    public BibValue getMonth() {
        return month;
    }

    /**
     * Returns note about BibTeX entry
     *
     * @return note about BibTeX entry
     */
    @Override
    public BibValue getNote() {
        return note;
    }

    /**
     * Returns key of BibTeX entry
     *
     * @return key of BibTeX entry
     */
    @Override
    public BibValue getKey() {
        return key;
    }

    public BibValue getOrganization() {
        return organization;
    }

    public BibValue getAddress() {
        return address;
    }

    public BibValue getEdition() {
        return edition;
    }
}
