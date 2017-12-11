package pl.regzand.bibparser.exceptions;

/**
 * {@link pl.regzand.bibparser.parser.BibParser BibParser} exceptions class
 */
public abstract class BibException extends Exception {

    public BibException(String message) {
        super(message);
    }

}
