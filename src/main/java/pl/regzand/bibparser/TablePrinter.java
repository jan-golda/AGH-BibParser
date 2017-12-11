package pl.regzand.bibparser;

import pl.regzand.bibparser.entries.BibEntry;
import pl.regzand.bibparser.parser.BibField;
import pl.regzand.bibparser.values.BibValue;
import pl.regzand.bibparser.values.BibValueList;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Class responsible for creating table in ASCII from BibEntry
 */
public class TablePrinter {

    private static final char hBorder = '-';
    private static final char vBorder = '|';
    private static final char cBorder = '+';

    private final int col1Width;
    private final int col2Width;

    private final boolean emptyRows;

    private final String line;
    private final String headerLine;

    private final String format;
    private final String headerFormat;

    /**
     * Creates table printer
     *
     * @param col1Width width (in chars) of first column
     * @param col2Width width (in chars) of second column
     * @param emptyRows if table should contains rows for entry values that are null
     */
    public TablePrinter(int col1Width, int col2Width, boolean emptyRows) {
        this.col1Width = col1Width;
        this.col2Width = col2Width;

        this.emptyRows = emptyRows;

        this.line = cBorder + String.join("", Collections.nCopies(col1Width, "" + hBorder)) + cBorder + String.join("", Collections.nCopies(col2Width, "" + hBorder)) + cBorder + '\n';
        this.headerLine = cBorder + String.join("", Collections.nCopies(col1Width + 1 + col2Width, "" + hBorder)) + cBorder + '\n';

        this.format = vBorder + " %-" + (col1Width - 2) + "s " + vBorder + " %-" + (col2Width - 2) + "s " + vBorder + '\n';
        this.headerFormat = vBorder + " %-" + (col1Width + col2Width + 1 - 2) + "s " + vBorder + '\n';
    }

    /**
     * Returns table in ASCII displaying content of given BibEntry
     *
     * @param entry entry to be displayed in table
     *
     * @return string containing table in ASCII displaying content of given BibEntry
     */
    public String generateEntryTable(BibEntry entry) {
        StringBuilder out = new StringBuilder();

        // header
        out.append(headerLine);
        out.append(String.format(headerFormat, entry.getEntryName().toUpperCase() + " (" + entry.getId() + ")"));
        out.append(line);

        // for every filed in entry marked as @BibField
        for (Field field : entry.getClass().getDeclaredFields()) {

            // make sure this is @BibField
            if (!field.isAnnotationPresent(BibField.class))
                continue;

            // get value
            BibValue bibValue = null;
            try {
                bibValue = (BibValue) field.get(entry);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // check if displaying empty rows
            if (bibValue == null && !emptyRows)
                continue;

            // check if it a list
            if (bibValue instanceof BibValueList) {

                // get values
                BibValue[] list = ((BibValueList) bibValue).getValues();

                // check if list is not empty
                if (list.length == 0)
                    continue;

                // display first row
                out.append(String.format(format, field.getAnnotation(BibField.class).name(), list[0].getString()));

                // display other rows
                for (int i = 1; i < list.length; i++)
                    out.append(String.format(format, "", list[i].getString()));

            } else {
                // print content
                out.append(String.format(format, field.getAnnotation(BibField.class).name(), bibValue.getString()));
            }


        }

        // print line
        out.append(line);

        return out.toString();
    }

}
