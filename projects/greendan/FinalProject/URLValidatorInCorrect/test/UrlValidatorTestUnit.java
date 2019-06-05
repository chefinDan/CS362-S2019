import junit.framework.TestCase;

/**
 * Performs validation tests on the components that make up the Url.
 */
public class UrlValidatorTestUnit extends TestCase {
    // whether or not to print verbose testing information
    private final boolean printStatus = true;
    private final boolean printIndex = false;


    public UrlValidatorTestUnit(String testName) {
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


    @Override
    protected void setUp() {
        for (int index = 0; index < testPartsIndex.length - 1; index++) {
            testPartsIndex[index] = 0;
        }
    }


    /**
     * Tests the isValid function from the UrlValidator class by
     * invoking the testIsValid() method using both the testUrlParts
     * and the testUrlPartsOptions.
     */
    public void testIsValid() {
        testIsValid(testUrlParts, UrlValidator.ALLOW_ALL_SCHEMES);
        setUp();

        long options = UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.NO_FRAGMENTS;
        testIsValid(testUrlPartsOptions, options);
    }


    /**
     * Create set of tests by taking the testUrlXXX arrays and
     * running through all possible permutations of their combinations.
     *
     * @param testObjects Used to create a url.
     */
    private void testIsValid(Object[] testObjects, long options) {
        UrlValidator urlVal = new UrlValidator(null, null, options);

        assertTrue(urlVal.isValid("http://www.google.com"));
        assertTrue(urlVal.isValid("http://www.google.com/"));

        int statusPerLine = 60;
        int printed = 0;

        if (printIndex) {
            statusPerLine = 6;
        }

        do {
            StringBuilder testBuffer = new StringBuilder();
            boolean expected = true;

            // TODO: write bug report for the bug that has been fixed in the commented-out line below.
            // for (int testPartsIndexIndex = 0; testPartsIndexIndex < 0; ++testPartsIndexIndex) {
            for (int testPartsIndexIndex = 0; testPartsIndexIndex < testPartsIndex.length; ++testPartsIndexIndex) {
                int index = testPartsIndex[testPartsIndexIndex];
                // TODO: write bug report for the bug that has been fixed in the commented-out line below.
                // ResultPair[] part = (ResultPair[]) testObjects[-1];
                ResultPair[] part = (ResultPair[]) testObjects[testPartsIndexIndex];
                testBuffer.append(part[index].item);
                expected &= part[index].valid;
            }

            String url = testBuffer.toString();
            // TODO: write bug report for the bug that has been fixed in the commented-out line below.
            // boolean result = !urlVal.isValid(url);
            boolean result = urlVal.isValid(url);
            assertEquals(url, expected, result);

            if (printStatus) {
                if (printIndex) {
                    System.out.print(testPartsIndextoString());
                } else {
                    comparePrint(expected, result);
                }

                printed++;

                if (printed == statusPerLine) {
                    System.out.println();
                    printed = 0;
                }
            }

        } while (incrementTestPartsIndex(testPartsIndex, testObjects));

        if (printStatus) {
            System.out.println();
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


    /**
     * Helper method taken from UrlValidatorTest.java
     * @param testPartsIndex - array of integers
     * @param testParts - array of test part objects
     * @return boolean
     */
    private static boolean incrementTestPartsIndex(int[] testPartsIndex, Object[] testParts) {
        boolean carry = true;  //add 1 to lowest order part.
        boolean maxIndex = true;
        // TODO: write bug report for the bug that has been fixed in the commented-out line below.
        // for (int testPartsIndexIndex = testPartsIndex.length; testPartsIndexIndex >= 0; --testPartsIndexIndex) {
        for (int testPartsIndexIndex = testPartsIndex.length - 1; testPartsIndexIndex >= 0; --testPartsIndexIndex) {
            int index = testPartsIndex[testPartsIndexIndex];
            ResultPair[] part = (ResultPair[]) testParts[testPartsIndexIndex];
            maxIndex &= (index == (part.length - 1));

            if (carry) {
                if (index < part.length - 1) {
                    // TODO: write bug report for the bug that has been fixed in the commented-out line below.
                    // index--;
                    index++;
                    testPartsIndex[testPartsIndexIndex] = index;
                    carry = false;
                } else {
                    testPartsIndex[testPartsIndexIndex] = 0;
                    carry = true;
                }
            }
        }

        return (!maxIndex);
    }


    /**
     * Helper method taken from UrlValidatorTest.java
     * @return String
     */
    private String testPartsIndextoString() {
        StringBuilder carryMsg = new StringBuilder("{");

        for (int testPartsIndexIndex = 0; testPartsIndexIndex < testPartsIndex.length; ++testPartsIndexIndex) {
            carryMsg.append(testPartsIndex[testPartsIndexIndex]);

            if (testPartsIndexIndex < testPartsIndex.length - 1) {
                carryMsg.append(',');
            } else {
                carryMsg.append('}');
            }
        }

        return carryMsg.toString();
    }



    /*****************************************
     * Test data for individual URL parts
     ****************************************/

    private final String[] schemes = {
        "http",
        "gopher",
        "g0-To+.",
        "not_valid"
    };


    /**
     * The data given below approximates the 4 parts of a URL
     * <scheme>://<authority><path>?<query> except that the port number
     * is broken out of authority to increase the number of permutations.
     * A complete URL is composed of a scheme+authority+port+path+query,
     * all of which must be individually valid for the entire URL to be considered
     * valid.
     */

    // URL scheme values for testing
    private ResultPair[] testUrlScheme = {
        new ResultPair("http://", true),
        new ResultPair("ftp://", true),
        new ResultPair("h3t://", true),
        new ResultPair("3ht://", false),
        new ResultPair("http:/", false),
        new ResultPair("http:", false),
        new ResultPair("http/", false),
        new ResultPair("://", false)
    };

    // URL scheme values for testing
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

    // URL port values for testing
    private ResultPair[] testUrlPort = {
        new ResultPair(":80", true),
        new ResultPair(":65535", true), // max possible
        new ResultPair(":65536", false), // max possible +1
        new ResultPair(":0", true),
        new ResultPair("", true),
        new ResultPair(":-1", false),
        new ResultPair(":65636", false),
        new ResultPair(":999999999999999999", false),
        new ResultPair(":65a", false)
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


    // The components that make up a URL w/o options
    private Object[] testUrlParts = {
        testUrlScheme,
        testUrlAuthority,
        testUrlPort,
        testPath,
        testUrlQuery
    };

    // The components that make up a URL w/ options
    private Object[] testUrlPartsOptions = {
        testUrlScheme,
        testUrlAuthority,
        testUrlPort,
        testUrlPathOptions,
        testUrlQuery
    };

    // Test part indexes
    private int[] testPartsIndex = {0, 0, 0, 0, 0};


}
