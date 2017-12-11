package pl.regzand.bibparser.values;

public interface BibValue {

    /**
     * Returns if this value if a number
     *
     * @return if this value if a number
     */
    public boolean isNumber();

    /**
     * Returns if this value if a string
     *
     * @return if this value if a string
     */
    public boolean isString();

    /**
     * Returns this value as string
     * If this value represents sum of variables it returns the result of that sum
     *
     * @return this value as string
     */
    public String getString();

    /**
     * Returns this value as number
     *
     * @return this value as number
     * @throws UnsupportedOperationException if value can't be converted to number
     */
    public int getNumber() throws UnsupportedOperationException;

}
