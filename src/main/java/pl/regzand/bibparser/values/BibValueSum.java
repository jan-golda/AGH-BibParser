package pl.regzand.bibparser.values;

import java.util.Arrays;

public class BibValueSum implements BibValue {

    private final BibValue[] values;

    public BibValueSum(BibValue[] values) {
        this.values = values;
    }

    /**
     * Returns if this value if a number
     *
     * @return if this value if a number
     */
    @Override
    public boolean isNumber() {
        return false;
    }

    /**
     * Returns if this value if a string
     *
     * @return if this value if a string
     */
    @Override
    public boolean isString() {
        return true;
    }

    /**
     * Returns this value as string
     * If this value represents sum of variables it returns the result of that sum
     *
     * @return this value as string
     */
    @Override
    public String getString() {
        return Arrays.stream(values)
                .map(BibValue::getString)
                .reduce("", ((a, b) -> a + b));
    }

    /**
     * Returns this value as number
     *
     * @return this value as number
     * @throws UnsupportedOperationException if value can't be converted to number
     */
    @Override
    public int getNumber() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
