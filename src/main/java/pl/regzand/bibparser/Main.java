package pl.regzand.bibparser;

import org.apache.commons.cli.*;
import pl.regzand.bibparser.entries.BibEntry;
import pl.regzand.bibparser.parser.BibParser;
import pl.regzand.bibparser.parser.Bibliography;
import pl.regzand.bibparser.values.BibValue;
import pl.regzand.bibparser.values.BibValueList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main project class responsible for providing console interface
 */
public class Main {

    /**
     * <p>Program starting point.<p>
     * <p>Accepted program arguments:</p>
     * <pre>
     * usage: bibparser -f &lt;path&gt; [options]
     *    -a,--authors &lt;author,...&gt;        entry authors to display
     *    -c,--categories &lt;category,...&gt;   entry categories to display
     *    -e,--empty-rows                  display empty-rows
     *    -f,--file &lt;path&gt;                 path to BibTeX file
     *    -h,--help                        print this message
     *    -v,--verbose                     display parser errors
     * </pre>
     *
     * @param args program arguments
     */
    public static void main(String[] args) {

        // parse command line
        CommandLine cmd = parseCommandLine(args);

        // parse file
        Bibliography bib = null;
        try {
            bib = BibParser.parseFile(cmd.getOptionValue("file"), (cmd.hasOption("verbose") ? System.err : null));
        } catch (IOException e) {
            System.err.println("En error occurred while opening file " + cmd.getOptionValue("file"));
            System.exit(2);
        }

        // get entries stream
        Stream<BibEntry> entries = bib.getEntries().values().stream();

        // if needed filter by categories
        if (cmd.hasOption("categories")) {

            // get categories names
            String[] categories = cmd.getOptionValue("categories").split(",");

            // get categories classes
            List<Class> classes = Arrays.stream(categories)
                    .map(BibParser::getEntryClassByName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // filter entries
            entries = entries.filter(c -> classes.contains(c.getClass()));
        }

        // if needed filter by authors
        if (cmd.hasOption("authors")) {

            // get author names
            String[] authors = Arrays.stream(cmd.getOptionValue("authors").split(","))
                    .map(String::trim)
                    .toArray(String[]::new);

            // filter entries
            entries = entries.filter(entry -> entry.getAuthors() != null && Utils.hasCommon(authors, Arrays.stream(((BibValueList) entry.getAuthors()).getValues()).map(BibValue::getString).toArray(String[]::new)));
        }

        // get table printer
        TablePrinter printer = new TablePrinter(20, 80, cmd.hasOption("empty-rows"));

        // display entries
        entries.map(printer::generateEntryTable).forEach(System.out::println);
    }

    /**
     * Parses given arguments using Commons CLI library
     *
     * @param args command line arguments
     *
     * @return
     */
    private static CommandLine parseCommandLine(String[] args) {

        // set options
        Options options = new Options();

        // simple options
        options.addOption("h", "help", false, "print this message");
        options.addOption("v", "verbose", false, "display parser errors");
        options.addOption("e", "empty-rows", false, "display empty-rows");

        // file
        Option file = new Option("f", "file", true, "path to BibTeX file");
        file.setArgName("path");
        file.setRequired(true);
        options.addOption(file);

        // border char
//        Option border = new Option("b", "border", true, "char that will be used to create table border");
//        border.setArgName("char");
//        options.addOption(border);

        // authors list
        Option authors = new Option("a", "authors", true, "entry authors to display");
        authors.setArgName("author,...");
        options.addOption(authors);

        // category list
        Option categories = new Option("c", "categories", true, "entry categories to display");
        categories.setArgName("category,...");
        options.addOption(categories);

        // create parser and formatter
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        // parse input
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            // print usage end error
            System.out.println(e.getMessage());
            formatter.printHelp("bibparser -f <path> [options]", options);

            // exit program
            System.exit(1);
            return null;
        }

        // display help and end program, if needed
        if (cmd.hasOption("help")) {
            formatter.printHelp("bibparser -f <path> [options]", options);
            System.exit(0);
            return null;
        }

        return cmd;
    }

}

