package pl.regzand.bibparser.parser;

import com.sun.istack.internal.Nullable;
import pl.regzand.bibparser.entries.BibEntry;
import pl.regzand.bibparser.values.BibValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Data container for BibTeX bibliography, contains BibTeX entries and variables
 *
 * @see <a href="http://www.bibtex.org/Format/">BibTeX Format Description</a>
 */
public class Bibliography {

    private Map<String, BibEntry> entries = new HashMap<>();
    private Map<String, BibValue> variables = new HashMap<>();

    /**
     * Adds entry to this bibliography
     *
     * @param entry BibTeX entry to be added
     *
     * @return the previous entry with same entry id, or null if there was none
     */
    @Nullable
    public BibEntry addEntry(BibEntry entry) {
        return this.entries.put(entry.getId().toLowerCase(), entry);
    }

    /**
     * Returns if this bibliography contains BibTeX entry with given id
     *
     * @param entryId case insensitive entry id
     *
     * @return if this bibliography contains BibTeX entry with given id
     */
    public boolean hasEntry(String entryId) {
        return this.entries.containsKey(entryId.toLowerCase());
    }

    /**
     * Returns entry represented by given entry id
     *
     * @param entryId case insensitive entry id
     *
     * @return entry with given id or null if there was no match
     */
    @Nullable
    public BibEntry getEntry(String entryId) {
        return this.entries.get(entryId.toLowerCase());
    }

    /**
     * Returns map of all BibTeX entries in this bibliography
     *
     * @return map of all BibTeX entries in this bibliography
     */
    public Map<String, BibEntry> getEntries() {
        return this.entries;
    }

    /**
     * Adds variable to this bibliography
     *
     * @param name  case insensitive variable name
     * @param value value of variable
     *
     * @return the previous value under this variable name, or null if there was none
     */
    @Nullable
    public BibValue addVariable(String name, BibValue value) {
        return this.variables.put(name.toLowerCase(), value);
    }

    /**
     * Returns if this bibliography contains variable with given name
     *
     * @param name case insensitive variable name
     *
     * @return if this bibliography contains variable with given name
     */
    public boolean hasVariable(String name) {
        return this.variables.containsKey(name.toLowerCase());
    }

    /**
     * Returns value of variable with given name
     *
     * @param name case insensitive variable name
     *
     * @return value of variable with given name or null if there was no match
     */
    public BibValue getVariable(String name) {
        return this.variables.get(name.toLowerCase());
    }

    /**
     * Returns map of all variables in this bibliography
     *
     * @return map of all variables in this bibliography
     */
    public Map<String, BibValue> getVariables() {
        return this.variables;
    }
}
