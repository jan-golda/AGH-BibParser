package pl.regzand.bibparser.values;

public class BibValueNumber implements BibValue {

    private final int number;

    public BibValueNumber(int number) {
        this.number = number;
    }

    /**
     * Returns if this value if a number
     *
     * @return if this value if a number
     */
    @Override
    public boolean isNumber() {
        return true;
    }

    /**
     * Returns if this value if a string
     *
     * @return if this value if a string
     */
    @Override
    public boolean isString() {
        return false;
    }

    /**
     * Returns this value as string
     * If this value represents sum of variables it returns the result of that sum
     *
     * @return this value as string
     */
    @Override
    public String getString() {
        return "" + number;
    }

    /**
     * Returns this value as number
     *
     * @return this value as number
     * @throws UnsupportedOperationException if value can't be converted to number
     */
    @Override
    public int getNumber() throws UnsupportedOperationException {
        return number;
    }
}
