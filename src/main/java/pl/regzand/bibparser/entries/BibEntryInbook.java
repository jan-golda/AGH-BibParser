package pl.regzand.bibparser.entries;

import pl.regzand.bibparser.exceptions.BibException;
import pl.regzand.bibparser.exceptions.BibMissingEntryFieldException;
import pl.regzand.bibparser.parser.BibField;
import pl.regzand.bibparser.values.BibValue;

/**
 * Data container for BibTeX @Inbook entry
 *
 * @see <a href="https://pl.wikipedia.org/wiki/BibTeX#Struktura_plik.C3.B3w_bazy_bibliograficznej">BibTeX entry fields</a>
 */
public class BibEntryInbook extends BibEntry {

    @BibField(name = "author", required = false, names = true)
    public BibValue author;
    @BibField(name = "editor", required = false, names = true)
    public BibValue editor;

    // ==============================================================================
    // === INJECTABLE FIELDS
    // ==============================================================================
    @BibField(name = "title", required = true)
    public BibValue title;
    @BibField(name = "chapter", required = false)
    public BibValue chapter;
    @BibField(name = "pages", required = false)
    public BibValue pages;
    @BibField(name = "publisher", required = true)
    public BibValue publisher;
    @BibField(name = "year", required = true)
    public BibValue year;
    @BibField(name = "volume")
    public BibValue volume;
    @BibField(name = "number")
    public BibValue number;
    @BibField(name = "series")
    public BibValue series;
    @BibField(name = "type")
    public BibValue type;
    @BibField(name = "address")
    public BibValue address;
    @BibField(name = "edition")
    public BibValue edition;
    @BibField(name = "month")
    public BibValue month;
    @BibField(name = "note")
    public BibValue note;
    @BibField(name = "key")
    public BibValue key;

    /**
     * Creates entry
     *
     * @param id unique entry id
     */
    public BibEntryInbook(String id) {
        super("InBook", id);
    }

    /**
     * Validates entry, should be called after all fields are injected
     *
     * @throws BibException
     */
    @Override
    public void validate() throws BibException {
        if (author == null && editor == null)
            throw new BibMissingEntryFieldException(this.getId(), "author\" or \"editor");
        if (chapter == null && pages == null)
            throw new BibMissingEntryFieldException(this.getId(), "chapter\" or \"pages");
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
        return (author != null ? author : editor);
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

    public BibValue getEditor() {
        return editor;
    }

    public BibValue getChapter() {
        return chapter;
    }

    public BibValue getPages() {
        return pages;
    }

    public BibValue getPublisher() {
        return publisher;
    }

    public BibValue getVolume() {
        return volume;
    }

    public BibValue getNumber() {
        return number;
    }

    public BibValue getSeries() {
        return series;
    }

    public BibValue getType() {
        return type;
    }

    public BibValue getAddress() {
        return address;
    }

    public BibValue getEdition() {
        return edition;
    }
}
