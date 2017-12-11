package pl.regzand.bibparser.parser;

import com.sun.istack.internal.Nullable;
import pl.regzand.bibparser.Utils;
import pl.regzand.bibparser.entries.*;
import pl.regzand.bibparser.exceptions.BibException;
import pl.regzand.bibparser.exceptions.BibMissingEntryFieldException;
import pl.regzand.bibparser.exceptions.BibSyntaxException;
import pl.regzand.bibparser.exceptions.BibUnknownEntryException;
import pl.regzand.bibparser.values.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All-static class responsible for creating {@link pl.regzand.bibparser.parser.Bibliography Bibliography} objects based on given data
 */
public class BibParser {

    // ==============================================================================
    // === PATTERNS
    // ==============================================================================
    private static final Pattern SECTION_PATTERN = Pattern.compile("^\\s*([a-zA-Z][a-zA-Z0-9_-]*)\\s*=\\s*(\\S.*)");
    private static final Pattern IDENTIFICATOR_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");

    // ==============================================================================
    // === STATIC DATA
    // ==============================================================================

    // map containing BibEntry classes according to entry names connected to them
    private static final Map<String, Class<? extends BibEntry>> entryClasses = new HashMap<>();

    // fills entryClasses map
    static {
        entryClasses.put("article", BibEntryArticle.class);
        entryClasses.put("book", BibEntryBook.class);
        entryClasses.put("inproceedings", BibEntryConference.class);
        entryClasses.put("conference", BibEntryConference.class);
        entryClasses.put("booklet", BibEntryBooklet.class);
        entryClasses.put("inbook", BibEntryInbook.class);
        entryClasses.put("incollection", BibEntryIncollection.class);
        entryClasses.put("manual", BibEntryManual.class);
        entryClasses.put("mastersthesis", BibEntryMastersthesis.class);
        entryClasses.put("phdthesis", BibEntryPhdthesis.class);
        entryClasses.put("techreport", BibEntryTechreport.class);
        entryClasses.put("misc", BibEntryMisc.class);
        entryClasses.put("unpublished", BibEntryUnpublished.class);
    }

    // ==============================================================================
    // === LOADING DATA
    // ==============================================================================

    /**
     * Returns new {@link pl.regzand.bibparser.parser.Bibliography bibliography} based on file under given path.
     *
     * @param path   the path to .bib file
     * @param errOut stream to which parser will print errors, can be null
     *
     * @return bibliography created based on given file
     * @throws IOException if there is en error with accessing given file
     */
    public static Bibliography parseFile(String path, @Nullable PrintStream errOut) throws IOException {
        return parseFile(new File(path), errOut);
    }

    /**
     * Returns new {@link pl.regzand.bibparser.parser.Bibliography bibliography} based on given file.
     *
     * @param file   the file that will be parsed
     * @param errOut stream to which parser will print errors, can be null
     *
     * @return bibliography created based on given file
     * @throws IOException if there is en error with accessing given file
     */
    public static Bibliography parseFile(File file, @Nullable PrintStream errOut) throws IOException {
        return parse(new Scanner(file), errOut);
    }

    /**
     * Returns new {@link pl.regzand.bibparser.parser.Bibliography bibliography} based on the data form given InputStream.
     *
     * @param inputStream the input stream with bibliography data
     * @param errOut      stream to which parser will print errors, can be null
     *
     * @return bibliography created based on given file
     */
    public static Bibliography parse(InputStream inputStream, @Nullable PrintStream errOut) {
        return parse(new Scanner(inputStream), errOut);
    }

    /**
     * Returns new {@link pl.regzand.bibparser.parser.Bibliography bibliography} based on the data form given text.
     *
     * @param text   the bibliography data in text form
     * @param errOut stream to which parser will print errors, can be null
     *
     * @return bibliography created based on given text
     */
    public static Bibliography parse(String text, @Nullable PrintStream errOut) {
        return parse(new Scanner(text), errOut);
    }

    // ==============================================================================
    // === PARSING
    // ==============================================================================

    /**
     * Returns new {@link pl.regzand.bibparser.parser.Bibliography bibliography} based on the data form given scanner.
     *
     * @param scanner scanner with BibTeX data
     * @param errOut  stream to which parser will print errors, can be null
     *
     * @return bibliography created based on given text
     */
    private static Bibliography parse(Scanner scanner, @Nullable PrintStream errOut) {

        // create new bibliography
        Bibliography bibliography = new Bibliography();

        // find next entry
        while (scanner.findWithinHorizon("@(\\w+)\\s*\\{", 0) != null) {

            // get entry name
            String entryName = scanner.match().group(1);

            // get entry body
            String entryBody = Utils.scanToClosingBracket(scanner);

            // try to parse entry
            try {
                parseEntry(entryName, entryBody, bibliography);
            } catch (BibException e) {
                // bib exception -> notify about it
                if (errOut != null)
                    errOut.println(e.getMessage());
            } catch (Exception e) {
                // catch any other exceptions
                e.printStackTrace();
            }

        }

        // return created bibliography
        return bibliography;
    }

    /**
     * Parses given data into entry and adds it to the bibliography.
     *
     * @param entryName    name of the entry
     * @param entryBody    content of the entry
     * @param bibliography to which the parsed entry will be added
     *
     * @throws BibException              if parser encounters any problem with parsed data
     * @throws NoSuchMethodException     unlikely to happen
     * @throws IllegalAccessException    unlikely to happen
     * @throws InvocationTargetException unlikely to happen
     * @throws InstantiationException    unlikely to happen
     */
    private static void parseEntry(String entryName, String entryBody, Bibliography bibliography) throws BibException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        // split entry body
        String[] entryData = splitByChar(entryName, entryBody, ',');

        // check if entry is not empty
        if (entryData.length == 0)
            throw new BibSyntaxException("Entry body can't be empty", entryName);

        // if entry is a variable try to parse it
        if (entryName.equalsIgnoreCase("string")) {
            parseStringEntry(entryName, entryData, bibliography);
            return;
        }

        // get entry class
        Class<? extends BibEntry> entryClass = getEntryClassByName(entryName);

        // check if it's a know entry
        if (entryClass == null)
            throw new BibUnknownEntryException(entryName);

        // get entry id
        String entryId = entryData[0].trim();

        // check if entry id is valid
        if (!IDENTIFICATOR_PATTERN.matcher(entryId).matches())
            throw new BibSyntaxException("Incorrect entry id: " + entryId, entryName);

        // parse entry data
        Map<String, BibValue> entryValues = parseEntryData(entryName, Arrays.copyOfRange(entryData, 1, entryData.length), bibliography);

        // create entry
        BibEntry entry = entryClass.getConstructor(String.class).newInstance(entryId);

        // inject data into entry
        injectIntoEntry(entry, entryValues);

        // add entry to bibliography
        bibliography.addEntry(entry);
    }

    /**
     * Parses given data into variable and adds it to the bibliography.
     *
     * @param entryData    sections of entry data
     * @param bibliography to which the parsed variable will be added
     *
     * @throws BibException if parser encounters any problem with parsed data
     */
    private static void parseStringEntry(String entryName, String[] entryData, Bibliography bibliography) throws BibException {

        // check if data contains exactly one section
        if (entryData.length != 1)
            throw new BibSyntaxException("String entry has to have exactly one section", entryName);

        // group 1 - variable name
        // group 2 - variable content
        Matcher matcher = SECTION_PATTERN.matcher(entryData[0]);

        // check if entryBody matches variable body pattern
        if (!matcher.matches())
            throw new BibSyntaxException("Wrong syntax of variable entry body", entryName);

        // add variable to bibliography
        bibliography.addVariable(matcher.group(1), parseValueBlock(entryName, matcher.group(2), bibliography));
    }

    /**
     * Parses entry data (multiple entries in <code>name = value</code> format).
     *
     * @param entryName    name of entry used in exceptions messages
     * @param entryData    array of data, each in <code>name = value</code> format
     * @param bibliography bibliography from which parser will take needed variables
     *
     * @return map wheres keys are the names of entry fields and values are values of fields
     * @throws BibException if parser encounters any problem with parsed data
     */
    private static Map<String, BibValue> parseEntryData(String entryName, String[] entryData, Bibliography bibliography) throws BibException {
        Map<String, BibValue> out = new HashMap<>();

        // for each section
        for (String section : entryData) {

            // group 1 - variable name
            // group 2 - variable content
            Matcher matcher = SECTION_PATTERN.matcher(section);

            // check if section matches section pattern
            if (!matcher.matches())
                throw new BibSyntaxException("Wrong syntax of entry section: " + section, entryName);

            // add to output
            out.put(matcher.group(1), parseValueBlock(entryName, matcher.group(2), bibliography));
        }

        return out;
    }

    /**
     * Parses block of data (after <code>=</code> sign) end returns result as {@link pl.regzand.bibparser.values.BibValue BibValue}.
     *
     * @param entryName      name of entry used in exceptions messages
     * @param valueBlockBody block of data to be parsed
     * @param bibliography   bibliography from which parser will take needed variables
     *
     * @return value created form given data
     * @throws BibException if parser encounters any problem with parsed data
     */
    private static BibValue parseValueBlock(String entryName, String valueBlockBody, Bibliography bibliography) throws BibException {

        // split values
        String[] valueBlockData = splitByChar(entryName, valueBlockBody, '#');

        // parsed values
        BibValue[] values = new BibValue[valueBlockData.length];

        // parse them
        for (int i = 0; i < valueBlockData.length; i++)
            values[i] = parseValue(entryName, valueBlockData[i], bibliography);

        if (values.length == 0)
            throw new BibSyntaxException("Value block can't be empty", entryName);
        if (values.length == 1)
            return values[0];

        return new BibValueSum(values);
    }

    /**
     * Parses single element for value block, data can represent a variable name, a number or a string.
     *
     * @param entryName    name of entry used in exceptions messages
     * @param value        data to be parsed
     * @param bibliography bibliography from which parser will take needed variables
     *
     * @return value created form given data
     * @throws BibException if parser encounters any problem with parsed data
     */
    private static BibValue parseValue(String entryName, String value, Bibliography bibliography) throws BibException {

        // if its variable name
        if (IDENTIFICATOR_PATTERN.matcher(value).matches()) {

            // check if it exists
            if (!bibliography.hasVariable(value))
                throw new BibSyntaxException("Unknown variable name: " + value, entryName);

            // return variable
            return bibliography.getVariable(value);
        }

        // if its a number
        if (value.matches("[0-9]+"))
            return new BibValueNumber(Integer.parseInt(value));

        // if its a string
        if (value.charAt(0) == '"' || value.charAt(0) == '{') {
            return new BibValueString(value.substring(1, value.length() - 1));
        }

        // what -> unknown variable type
        throw new BibSyntaxException("Can't parse variable: \'" + value + "\'", entryName);
    }

    /**
     * Splits given data by given char, skips delimiter inside <code>""</code> or <code>{}</code> brackets.
     *
     * @param entryName name of entry used in exceptions messages
     * @param entryBody data to be splited
     * @param delimiter used to split data
     *
     * @return given data as array containing fragments between delimiters
     * @throws BibException if parser encounters any problem with parsed data
     */
    private static String[] splitByChar(String entryName, String entryBody, char delimiter) throws BibException {
        List<String> out = new ArrayList<>();

        // starting point of body section
        int start = 0;

        // brackets history
        Stack<Character> brackets = new Stack<>();

        // for each character
        for (int i = 0; i < entryBody.length(); i++) {

            if (entryBody.charAt(i) == '"') {
                // if there was no brackets start new one
                if (brackets.empty())
                    brackets.push('"');
                    // if there was starting bracket end it
                else if (brackets.peek() == '"')
                    brackets.pop();
            } else if (entryBody.charAt(i) == '{') {
                // just start new brace
                brackets.push('{');
            } else if (entryBody.charAt(i) == '}') {
                // if there was no brackets or last one was " we have an error
                if (brackets.empty() || brackets.peek() == '"')
                    throw new BibSyntaxException("Unexpected \'}\' char", entryName);
                    // if last one was opening { then end it
                else if (brackets.peek() == '{')
                    brackets.pop();
            } else if (entryBody.charAt(i) == delimiter) {
                // if there was no brackets -> spilt!
                if (brackets.empty()) {
                    out.add(entryBody.substring(start, i).trim());
                    start = i + 1;
                }
            }

        }

        // if there is something left add it
        if (start < entryBody.length())
            out.add(entryBody.substring(start, entryBody.length()).trim());

        // return result
        return out.toArray(new String[out.size()]);
    }

    /**
     * Injects given values into given entry based on {@link pl.regzand.bibparser.parser.BibField BibField annotation}.
     *
     * @param entry       entry containing {@link pl.regzand.bibparser.parser.BibField BibField} annotations
     * @param entryValues map filed names and their values
     *
     * @throws BibMissingEntryFieldException if there is no value for entry marked as required in given map
     */
    private static void injectIntoEntry(BibEntry entry, Map<String, BibValue> entryValues) throws BibException {

        // for every field in entry
        for (Field field : entry.getClass().getDeclaredFields()) {

            // make sure this field in injectable
            if (!field.isAnnotationPresent(BibField.class))
                continue;

            // get annotation
            BibField annotation = field.getAnnotation(BibField.class);

            // get value
            BibValue value = entryValues.get(annotation.name().toLowerCase());

            // if value is not present check if it was required
            if (value == null) {
                if (annotation.required()) {
                    throw new BibMissingEntryFieldException(entry.getId(), annotation.name());
                }
            } else {
                // if value is present check if its not a name
                if (annotation.names())
                    value = parseNames(value);
                try {
                    field.set(entry, value);

                    // should not happen
                } catch (IllegalAccessException e) {
                }
            }

        }
    }

    /**
     * Parses text containing people names
     *
     * @param data text containing people names separated by <code>and</code> keyword.
     *
     * @return value containing list of parsed people names
     * @throws BibException if parser encounters any problem with parsed data
     */
    private static BibValueList parseNames(BibValue data) throws BibException {

        // parse names
        BibValue[] names = Arrays
                .stream(data.getString().split("and"))
                .map(String::trim)
                .map(Utils::parseName)
                .map(BibValueString::new)
                .toArray(BibValue[]::new);

        return new BibValueList(names, " and ");
    }

    /**
     * Returns entry class responsible for storing entry with given name.
     *
     * @param name the name of the entry
     *
     * @return entry class responsible for storing entry with given name, can be null if class not found
     */
    @Nullable
    public static Class<? extends BibEntry> getEntryClassByName(String name) {
        return entryClasses.get(name.toLowerCase());
    }


}
