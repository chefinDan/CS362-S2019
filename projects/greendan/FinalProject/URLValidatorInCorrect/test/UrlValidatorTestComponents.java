import junit.framework.TestCase;

/**
 * Performs validation tests on the components that make up the Url.
 */
public class UrlValidatorTestComponents extends TestCase {
    // whether or not to print verbose testing information
    private final boolean printStatus = true;

    public UrlValidatorTestComponents(String testName) {
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
     * Tests the functionality of the UrlValidator.isValidAuthority method
     * for accuracy, based on the known valid state of a series of predetermined
     * URL authority values.
     */
    public void testIsValidAuthority() {
        if (printStatus) {
            System.out.print("\ntestIsValidAuthority():\t");
        }

        // initialize a new UrlValidator object
        UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);

        // execute the isValidAuthority method on each of the known testUrlAuthority
        // values to check if isValidAuthority() is producing expected results.
        for (ResultPair pair : testUrlAuthority) {
            boolean result = urlVal.isValidAuthority(pair.item);
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
        UrlValidator urlVal = new UrlValidator(schemes, 0);

        // execute the isValidScheme method on each of the known testUrlScheme
        // values to check if isValidScheme() is producing expected results.
        for (ResultPair pair : testScheme) {
            boolean result = urlVal.isValidScheme(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
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
        UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);

        // execute the isValidPath method on each of the known testUrlPath
        // values to check if isValidPath() is producing expected results.
        for (ResultPair pair : testPath) {
            boolean result = urlVal.isValidPath(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
        }

        // re-initialize a new UrlValidator object to handle path options
        urlVal = new UrlValidator(null, null,
            UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.ALLOW_2_SLASHES + UrlValidator.NO_FRAGMENTS);

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
        UrlValidator urlVal = new UrlValidator(schemes, 0);

        // execute the isValidQuery method on each of the known testUrlQuery
        // values to check if isValidQuery() is producing expected results.
        for (ResultPair pair : testUrlQuery) {
            boolean result = urlVal.isValidQuery(pair.item);
            assertEquals(pair.item, pair.valid, result);
            comparePrint(pair.valid, result);
        }
    }




    /*****************************************
     * Test data for individual URL parts
     ****************************************/

    private final String[] schemes = {"http", "gopher", "g0-To+.", "not_valid"};

    // URL scheme value for testing
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

    // URL authority values for testing
    private ResultPair[] testUrlAuthority = {
        new ResultPair("www.google.com", true),
        new ResultPair("www.google.com.", true),
        new ResultPair("go.com", true),
        new ResultPair("go.au", true),
        new ResultPair("0.0.0.0", true),
        new ResultPair("255.255.255.255", true),
        new ResultPair("256.256.256.256", false),
        new ResultPair("255.com", true),
        new ResultPair("1.2.3.4.5", false),
        new ResultPair("1.2.3.4.", false),
        new ResultPair("1.2.3", false),
        new ResultPair(".1.2.3.4", false),
        new ResultPair("go.a", false),
        new ResultPair("go.a1a", false),
        new ResultPair("go.cc", true),
        new ResultPair("go.1aa", false),
        new ResultPair("aaa.", false),
        new ResultPair(".aaa", false),
        new ResultPair("aaa", false),
        new ResultPair("", false)
    };

    // URL path values for testing
    private ResultPair[] testPath = {
        new ResultPair("/test1", true),
        new ResultPair("/t123", true),
        new ResultPair("/$23", true),
        new ResultPair("/..", false),
        new ResultPair("/../", false),
        new ResultPair("/test1/", true),
        new ResultPair("", true),
        new ResultPair("/test1/file", true),
        new ResultPair("/..//file", false),
        new ResultPair("/test1//file", false)
    };

    // URL path options values for testing
    private ResultPair[] testUrlPathOptions = {
        new ResultPair("/test1", true),
        new ResultPair("/t123", true),
        new ResultPair("/$23", true),
        new ResultPair("/..", false),
        new ResultPair("/../", false),
        new ResultPair("/test1/", true),
        new ResultPair("/#", false),
        new ResultPair("", true),
        new ResultPair("/test1/file", true),
        new ResultPair("/t123/file", true),
        new ResultPair("/$23/file", true),
        new ResultPair("/../file", false),
        new ResultPair("/..//file", false),
        new ResultPair("/test1//file", true),
        new ResultPair("/#/file", false)
    };

    // URL query values for testing
    private ResultPair[] testUrlQuery = {
        new ResultPair("?action=view", true),
        new ResultPair("?action=edit&mode=up", true),
        new ResultPair("", true)
    };
}
