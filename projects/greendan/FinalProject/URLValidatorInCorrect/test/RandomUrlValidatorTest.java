import junit.framework.TestCase;

/**
 * Performs validation tests on the components that make up the Url.
 */
public class RandomUrlValidatorTest extends TestCase {
    // whether or not to print verbose testing information
    private final boolean printStatus = true;
    private final boolean printIndex = false;


    public RandomUrlValidatorTest(String testName) {
        super(testName);
    }


    /**
     * Helper function that compares an expected boolean value to an
     * actual boolean value. If the two are a match, then a predefined
     * success symbol is printed. Otherwise, a predefined failure symbol
     * is printed.
     *
     * Note: If the printStatus value is set to false, this method will
     * not print anything.
     *
     * @param expected - an expected boolean value
     * @param actual - an actual boolean value
     */
    private void comparePrint(boolean expected, boolean actual) {
        final String successSymbol = "✓";
        final String failureSymbol = "✗";

        if (printStatus) {
            if (expected == actual) {
                System.out.print(successSymbol);
            } else {
                System.out.print(failureSymbol);
            }
        }
    }


    /**
     * Tests the functionality of the UrlValidator.isValidPath method
     * for accuracy, based on the known valid state of a series of predetermined
     * URL path and path options values.
     */
    public void testIsValidPath() {
        if (printStatus) {
            System.out.print("\ntestIsValidPath():\t\t");
        }

        // initialize a new UrlValidator object
        RandomUrlValidator urlVal = new RandomUrlValidator();

        // execute the isValidPath method on each of the known testUrlPath
        // values to check if isValidPath() is producing expected results.
        for (ResultPair pair : testPath) {
            boolean result = urlVal.isValidPath(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
        }

        urlVal = new RandomUrlValidator(true);

        // execute the isValidPath method on each of the known testUrlPathOptions
        // values to check if isValidPath() is producing expected results.
        for (ResultPair pair : testUrlPathOptions) {
            boolean result = urlVal.isValidPath(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
        }
    }


    /**
     * Tests the functionality of the UrlValidator.isValidQuery method
     * for accuracy, based on the known valid state of a series of predetermined
     * URL query values.
     */
    public void testIsValidQuery() {
        if (printStatus) {
            System.out.print("\ntestIsValidQuery():\t\t");
        }

        // initialize a new UrlValidator object
        RandomUrlValidator urlVal = new RandomUrlValidator();

        // execute the isValidQuery method on each of the known testUrlQuery
        // values to check if isValidQuery() is producing expected results.
        for (ResultPair pair : testUrlQuery) {
            boolean result = urlVal.isValidQuery(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
        }
    }


    /**
     * Tests the functionality of the UrlValidator.isValidScheme method
     * for accuracy, based on the known valid state of a series of predetermined
     * URL scheme values.
     */
    public void testIsValidScheme() {
        if (printStatus) {
            System.out.print("\ntestIsValidScheme():\t");
        }

        // initialize a new UrlValidator object
        RandomUrlValidator urlVal = new RandomUrlValidator();

        // execute the isValidScheme method on each of the known testUrlScheme
        // values to check if isValidScheme() is producing expected results.
        for (ResultPair pair : testScheme) {
            boolean result = urlVal.isValidScheme(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
        }
    }





    //
    // component values for testing
    //

    // URL query values for testing
    private ResultPair[] testUrlQuery = {
        new ResultPair("?foo=bar", true),
        new ResultPair("?foo=bar&bar=baz", true),
        new ResultPair("", true),
        new ResultPair(".", false),
        new ResultPair("foo", false),
        new ResultPair("??q=foo", false),
        new ResultPair("?q=?", false),
        new ResultPair("?&q=foo", false),
        new ResultPair("?foo", true),
        new ResultPair("?a=", true),
        new ResultPair("?a=b=", false),
        new ResultPair("?", true),
    };

    // URL path values for testing
    private ResultPair[] testPath = {
        new ResultPair("/foo1", true),
        new ResultPair("/bar123", true),
        new ResultPair("/$55", true),
        new ResultPair("/..", false),
        new ResultPair("/../", false),
        new ResultPair("..", false),
        new ResultPair("/foo1/", true),
        new ResultPair("", true),
        new ResultPair("/foo1/bar", true),
        new ResultPair("/..//baz", false),
        new ResultPair("/foo//bar", false)
    };

    // URL path options values for testing
    private ResultPair[] testUrlPathOptions = {
        new ResultPair("/foo1", true),
        new ResultPair("/foo123", true),
        new ResultPair("/$55", true),
        new ResultPair("/..", false),
        new ResultPair("/../", false),
        new ResultPair("..", false),
        new ResultPair("/foo1/", true),
        new ResultPair("/#", false),
        new ResultPair("", true),
        new ResultPair("/foo1/bar", true),
        new ResultPair("/foo123/bar", true),
        new ResultPair("/$23/baz", true),
        new ResultPair("/../baz", false),
        new ResultPair("/..//baz", false),
        new ResultPair("/foo1//bar", true),
        new ResultPair("/#/baz", false)
    };


    private ResultPair[] testScheme = {
        new ResultPair("http", true),
        new ResultPair("ftp", false),
        new ResultPair("httpd", false),
        new ResultPair("gopher", true),
        new ResultPair("g0-to+.", true),
        new ResultPair("not_valid", false), // underscore not allowed
        new ResultPair("HtTp", true),
        new ResultPair("telnet", false)
    };


}
