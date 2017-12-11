package pl.regzand.bibparser.parser;

import org.junit.jupiter.api.Test;
import pl.regzand.bibparser.entries.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BibParserTest {

    @Test
    void parse() {

        // parse
        Bibliography bib = BibParser.parse(getClass().getClassLoader().getResourceAsStream("BibParserTestInput.bib"), null);


        // ==============================================================================
        // === TEST VARIABLES
        // ==============================================================================

        // check number of variables
        assertEquals(15, bib.getVariables().size(), "Bibliography should contain 15 variables");

        // create tests
        Map<String, String> variables = new HashMap<>();

        //@formatter:off

        variables.put("jan",        "January");
        variables.put("feb",        "February");
        variables.put("mar",        "March");
        variables.put("apr",        "April");
        variables.put("may",        "May");
        variables.put("jun",        "June");
        variables.put("jul",        "July");
        variables.put("aug",        "August");
        variables.put("sep",        "September");
        variables.put("oct",        "October");
        variables.put("nov",        "November");
        variables.put("dec",        "December");
        variables.put("STOC-key",   "OX{\\singleletter{stoc}}");
        variables.put("ACM",        "The OX Association for Computing Machinery");
        variables.put("STOC",       " Symposium on the Theory of Computing");

        //@formatter:on

        // run tests
        variables.forEach((name, value) -> {

            // check if variable is present
            assertTrue(bib.hasVariable(name), "Bibliography should contain variable: " + name);

            // check variable value
            assertEquals(value, bib.getVariable(name).getString(), "Unexpected value for variable: " + name);

        });


        // ==============================================================================
        // === TEST ENTRIES
        // ==============================================================================

        // check number of entries
        assertEquals(27, bib.getEntries().size(), "Bibliography should contain 27 entries");

        // create tests
        Map<String, Class<? extends BibEntry>> entries = new HashMap<>();

        //@formatter:off

        entries.put("article-minimal",      BibEntryArticle.class);
        entries.put("article-full",         BibEntryArticle.class);
        entries.put("inbook-minimal",       BibEntryInbook.class);
        entries.put("inbook-full",          BibEntryInbook.class);
        entries.put("book-minimal",         BibEntryBook.class);
        entries.put("book-full",            BibEntryBook.class);
        entries.put("whole-set",            BibEntryBook.class);
        entries.put("booklet-minimal",      BibEntryBooklet.class);
        entries.put("booklet-full",         BibEntryBooklet.class);
        entries.put("incollection-minimal", BibEntryIncollection.class);
        entries.put("incollection-full",    BibEntryIncollection.class);
        entries.put("whole-collection",     BibEntryBook.class);
        entries.put("manual-minimal",       BibEntryManual.class);
        entries.put("manual-full",          BibEntryManual.class);
        entries.put("mastersthesis-minimal",BibEntryMastersthesis.class);
        entries.put("mastersthesis-full",   BibEntryMastersthesis.class);
        entries.put("misc-minimal",         BibEntryMisc.class);
        entries.put("misc-full",            BibEntryMisc.class);
        entries.put("inproceedings-minimal",BibEntryConference.class);
        entries.put("inproceedings-full",   BibEntryConference.class);
        entries.put("phdthesis-minimal",    BibEntryPhdthesis.class);
        entries.put("phdthesis-full",       BibEntryPhdthesis.class);
        entries.put("techreport-minimal",   BibEntryTechreport.class);
        entries.put("techreport-full",      BibEntryTechreport.class);
        entries.put("unpublished-minimal",  BibEntryUnpublished.class);
        entries.put("unpublished-full",     BibEntryUnpublished.class);
        entries.put("random-note-crossref", BibEntryMisc.class);

        //@formatter:on

        // run tests
        entries.forEach((name, value) -> {

            // check if entry is present
            assertTrue(bib.hasEntry(name), "Bibliography should contain entry: " + name);

            // check variable value
            assertEquals(value, bib.getEntry(name).getClass(), "Entry " + name + " should be of type " + value.getName());

        });

    }

}