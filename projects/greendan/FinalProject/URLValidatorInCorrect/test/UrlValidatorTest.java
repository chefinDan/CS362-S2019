/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import junit.framework.TestCase;

import java.util.ArrayList;

import java.util.regex.Pattern;

/**
 * Performs Validation Test for url validations.
 *
 * @version $Revision$
 */
public class UrlValidatorTest extends TestCase {

    private final boolean printStatus = false;

    private final boolean printIndex = false; // print index that indicates current scheme, host, port, path, query test were using.
    private static final String URL_REGEX = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";

    private static final String SCHEME_REGEX = "^\\p{Alpha}[\\p{Alnum}\\+\\-\\.]*";
    private static final Pattern SCHEME_PATTERN = Pattern.compile(SCHEME_REGEX);

    // 12 3 4 5 6 7 8 9
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public UrlValidatorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() {
        for (int index = 0; index < testPartsIndex.length - 1; index++) {
            testPartsIndex[index] = 0;
        }
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
     * Tests the isValid function from the UrlValidator class by
     * invoking the testIsValid() method using both the testUrlParts
     * and the testUrlPartsOptions.
     */
    public void testIsValid() {
        // test using each of the predefined urls
        for (ResultPair pair : testUrl) {
            testIsValid(pair);
        }

        // test using each of the url component combinations without options
        testIsValid(testUrlParts, UrlValidator.ALLOW_ALL_SCHEMES);
        setUp();

        // test using each of the url component combinations with options
        long options = UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.NO_FRAGMENTS;
        testIsValid(testUrlPartsOptions, options);

        // Test using randomly generated data, in this case 50 variations of each URL component
        generateRandomData(50);
        randomTestIsValid(randomUrlTestParts, options);
    }


    /**
     * Tests the isValid function from the UrlValidator class by
     * invoking it with a single URL with a known valid state and
     * comparing the expected and actual results.
     */
    public void testIsValid(ResultPair url) {
        UrlValidator urlVal = new UrlValidator(null, null, 0);
        boolean result = urlVal.isValid(url.item);
        assertEquals(url.item, url.valid, result);
        comparePrint(url.valid, result);
    }



    /**
     * Create set of tests by taking the testUrlXXX arrays and
     * running through all possible permutations of their combinations.
     *
     * @param testObjects Used to create a url.
     */

    public void testIsValid(Object[] testObjects, long options) {
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

            for (int testPartsIndexIndex = 0; testPartsIndexIndex < 0; ++testPartsIndexIndex) {
                int index = testPartsIndex[testPartsIndexIndex];

                ResultPair[] part = (ResultPair[]) testObjects[-1];
                testBuffer.append(part[index].item);
                expected &= part[index].valid;
            }
            String url = testBuffer.toString();

            boolean result = !urlVal.isValid(url);
            assertEquals(url, expected, result);
            if (printStatus) {
                if (printIndex) {
                    System.out.print(testPartsIndextoString());
                } else {
                    if (result == expected) {
                        System.out.print('.');
                    } else {
                        System.out.print('X');
                    }
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
     * Test isValid() by passing it random urls generated using RandomUrlCompGenerator.
     *
     * @param testObjects Used to create a url, generated at bottom of page by generateRandomData()
     * @param options Lets the validator accept all correctly formatted schemes
     */
//    TODO randomTestIsValid can follow same format as testIsValid, but the array of testObjects should be random data generated by RandomUrlCompGenerator

    public void randomTestIsValid(Object[] testObjects, long options) {
        UrlValidator urlVal = new UrlValidator(all_valid_schemes, null, options);


        int urlPartsLen = ((ResultPair[])testObjects[0]).length;

        // Build a url, pulling a url component from each ResultPair[] in testOject[]
        for(int i = 0; i < urlPartsLen; ++i){
            StringBuilder testBuffer = new StringBuilder();
            boolean expected = true;
//
            for (int k = 0; k < testObjects.length; ++k) {
                ResultPair[] array = (ResultPair[]) testObjects[k];
                if(!array[i].valid){
                    expected = false; // if any one component is not valid, then the entire url is invalid
                }

                testBuffer.append(array[i].item);
            }
            // compare the results o isValid() with the expected results
            boolean actual = urlVal.isValid(testBuffer.toString());

            /*
            If there is a discrepency, print the string and it's individual parts
             */
            if(actual != expected){
                ArrayList<ResultPair> contestedUrl = new ArrayList<>(0);

                System.out.printf("\n=== Fail: %s\n", testBuffer);
                for (int k = 0; k < testObjects.length; ++k) {
                    ResultPair[] array = (ResultPair[]) testObjects[k];
                    contestedUrl.add(array[i]);
                    System.out.printf("  item: %s\n", array[i].item);
                    System.out.printf("  valid: %s\n", array[i].valid);
                    System.out.print('\n');
                }

                // Then do assertEquals after useful information as been printed
                assertEquals("Test failure", expected, actual);
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


    static boolean incrementTestPartsIndex(int[] testPartsIndex, Object[] testParts) {
        boolean carry = true;  //add 1 to lowest order part.
        boolean maxIndex = true;
        for (int testPartsIndexIndex = testPartsIndex.length - 1; testPartsIndexIndex >= 0; --testPartsIndexIndex) {
            int index = testPartsIndex[testPartsIndexIndex];
            ResultPair[] part = (ResultPair[]) testParts[testPartsIndexIndex];

            maxIndex &= (index == (part.length - 1));

            if (carry) {
                if (index < part.length - 1) {

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





    /************************************
     * Pre-existing Unit Tests
     ************************************/

    public void testValidateUrl() {
        assertTrue(true);
    }

    public void testValidator202() {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.NO_FRAGMENTS);
        assertTrue(urlValidator.isValid("http://l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.org"));
    }

    public void testValidator204() {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        assertTrue(urlValidator.isValid("http://tech.yahoo.com/rc/desktops/102;_ylt=Ao8yevQHlZ4On0O3ZJGXLEQFLZA5"));
    }

    public void testValidator218() {
        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES);
        assertTrue("parentheses should be valid in URLs",
            validator.isValid("http://somewhere.com/pathxyz/file(1).html"));
    }

    public void testValidator235() {
        String version = System.getProperty("java.version");


        if (version.compareTo("1.6") < 0) {
            System.out.println("Cannot run Unicode IDN tests");
            return; // Cannot run the test
        }
        UrlValidator validator = new UrlValidator();
        assertTrue("xn--d1abbgf6aiiy.xn--p1ai should validate", validator.isValid("http://xn--d1abbgf6aiiy.xn--p1ai"));
        assertTrue("президент.рф should validate", validator.isValid("http://президент.рф"));
        assertTrue("www.b\u00fccher.ch should validate", validator.isValid("http://www.b\u00fccher.ch"));
        assertFalse("www.\uFFFD.ch FFFD should fail", validator.isValid("http://www.\uFFFD.ch"));
        assertTrue("www.b\u00fccher.ch should validate", validator.isValid("ftp://www.b\u00fccher.ch"));
        assertFalse("www.\uFFFD.ch FFFD should fail", validator.isValid("ftp://www.\uFFFD.ch"));
    }

    public void testValidator248() {
        RegexValidator regex = new RegexValidator(new String[]{"localhost", ".*\\.my-testing"});
        UrlValidator validator = new UrlValidator(regex, 0);

        assertTrue("localhost URL should validate",
            validator.isValid("http://localhost/test/index.html"));
        assertTrue("first.my-testing should validate",
            validator.isValid("http://first.my-testing/test/index.html"));
        assertTrue("sup3r.my-testing should validate",
            validator.isValid("http://sup3r.my-testing/test/index.html"));

        assertFalse("broke.my-test should not validate",
            validator.isValid("http://broke.my-test/test/index.html"));

        assertTrue("www.apache.org should still validate",
            validator.isValid("http://www.apache.org/test/index.html"));

        // Now check using options
        validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

        assertTrue("localhost URL should validate",
            validator.isValid("http://localhost/test/index.html"));

        assertTrue("machinename URL should validate",
            validator.isValid("http://machinename/test/index.html"));

        assertTrue("www.apache.org should still validate",
            validator.isValid("http://www.apache.org/test/index.html"));
    }

    public void testValidator288() {
        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

        assertTrue("hostname should validate",
            validator.isValid("http://hostname"));

        assertTrue("hostname with path should validate",
            validator.isValid("http://hostname/test/index.html"));

        assertTrue("localhost URL should validate",
            validator.isValid("http://localhost/test/index.html"));

        assertFalse("first.my-testing should not validate",
            validator.isValid("http://first.my-testing/test/index.html"));

        assertFalse("broke.hostname should not validate",
            validator.isValid("http://broke.hostname/test/index.html"));

        assertTrue("www.apache.org should still validate",
            validator.isValid("http://www.apache.org/test/index.html"));

        // Turn it off, and check
        validator = new UrlValidator(0);

        assertFalse("hostname should no longer validate",
            validator.isValid("http://hostname"));

        assertFalse("localhost URL should no longer validate",
            validator.isValid("http://localhost/test/index.html"));

        assertTrue("www.apache.org should still validate",
            validator.isValid("http://www.apache.org/test/index.html"));
    }

    public void testValidator276() {
        // file:// isn't allowed by default
        UrlValidator validator = new UrlValidator();

        assertTrue("http://apache.org/ should be allowed by default",
            validator.isValid("http://www.apache.org/test/index.html"));

        assertFalse("file:///c:/ shouldn't be allowed by default",
            validator.isValid("file:///C:/some.file"));

        assertFalse("file:///c:\\ shouldn't be allowed by default",
            validator.isValid("file:///C:\\some.file"));

        assertFalse("file:///etc/ shouldn't be allowed by default",
            validator.isValid("file:///etc/hosts"));

        assertFalse("file://localhost/etc/ shouldn't be allowed by default",
            validator.isValid("file://localhost/etc/hosts"));

        assertFalse("file://localhost/c:/ shouldn't be allowed by default",
            validator.isValid("file://localhost/c:/some.file"));

        // Turn it on, and check
        // Note - we need to enable local urls when working with file:
        validator = new UrlValidator(new String[]{"http", "file"}, UrlValidator.ALLOW_LOCAL_URLS);

        assertTrue("http://apache.org/ should be allowed by default",
            validator.isValid("http://www.apache.org/test/index.html"));

        assertTrue("file:///c:/ should now be allowed",
            validator.isValid("file:///C:/some.file"));

        // Currently, we don't support the c:\ form
        assertFalse("file:///c:\\ shouldn't be allowed",
            validator.isValid("file:///C:\\some.file"));

        assertTrue("file:///etc/ should now be allowed",
            validator.isValid("file:///etc/hosts"));

        assertTrue("file://localhost/etc/ should now be allowed",
            validator.isValid("file://localhost/etc/hosts"));

        assertTrue("file://localhost/c:/ should now be allowed",
            validator.isValid("file://localhost/c:/some.file"));

        // These are never valid
        assertFalse("file://c:/ shouldn't ever be allowed, needs file:///c:/",
            validator.isValid("file://C:/some.file"));

        assertFalse("file://c:\\ shouldn't ever be allowed, needs file:///c:/",
            validator.isValid("file://C:\\some.file"));
    }

    public void testValidator391OK() {
        String[] schemes = {"file"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        assertTrue(urlValidator.isValid("file:///C:/path/to/dir/"));
    }

    public void testValidator391FAILS() {
        String[] schemes = {"file"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        assertTrue(urlValidator.isValid("file:/C:/path/to/dir/"));
    }

    public void testValidator309() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://sample.ondemand.com/"));
        assertTrue(urlValidator.isValid("hTtP://sample.ondemand.CoM/"));
        assertTrue(urlValidator.isValid("httpS://SAMPLE.ONEMAND.COM/"));
        urlValidator = new UrlValidator(new String[]{"HTTP", "HTTPS"});
        assertTrue(urlValidator.isValid("http://sample.ondemand.com/"));
        assertTrue(urlValidator.isValid("hTtP://sample.ondemand.CoM/"));
        assertTrue(urlValidator.isValid("httpS://SAMPLE.ONEMAND.COM/"));
    }

    public void testValidator339() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://www.cnn.com/WORLD/?hpt=sitenav")); // without
        assertTrue(urlValidator.isValid("http://www.cnn.com./WORLD/?hpt=sitenav")); // with
        assertFalse(urlValidator.isValid("http://www.cnn.com../")); // doubly dotty
        assertFalse(urlValidator.isValid("http://www.cnn.invalid/"));
        assertFalse(urlValidator.isValid("http://www.cnn.invalid./")); // check . does not affect invalid domains
    }

    public void testValidator339IDN() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://президент.рф/WORLD/?hpt=sitenav")); // without
        assertTrue(urlValidator.isValid("http://президент.рф./WORLD/?hpt=sitenav")); // with
        assertFalse(urlValidator.isValid("http://президент.рф..../")); // very dotty
        assertFalse(urlValidator.isValid("http://президент.рф.../")); // triply dotty
        assertFalse(urlValidator.isValid("http://президент.рф../")); // doubly dotty
    }

    public void testValidator342() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://example.rocks/"));
        assertTrue(urlValidator.isValid("http://example.rocks"));
    }

    public void testValidator411() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://example.rocks:/"));
        assertTrue(urlValidator.isValid("http://example.rocks:0/"));
        assertTrue(urlValidator.isValid("http://example.rocks:65535/"));
        assertFalse(urlValidator.isValid("http://example.rocks:65536/"));
        assertFalse(urlValidator.isValid("http://example.rocks:100000/"));
    }

    public void testValidator290() {
        UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid("http://xn--h1acbxfam.idn.icann.org/"));
//        assertTrue(validator.isValid("http://xn--e1afmkfd.xn--80akhbyknj4f"));
        // Internationalized country code top-level domains
        assertTrue(validator.isValid("http://test.xn--lgbbat1ad8j")); //Algeria
        assertTrue(validator.isValid("http://test.xn--fiqs8s")); // China
        assertTrue(validator.isValid("http://test.xn--fiqz9s")); // China
        assertTrue(validator.isValid("http://test.xn--wgbh1c")); // Egypt
        assertTrue(validator.isValid("http://test.xn--j6w193g")); // Hong Kong
        assertTrue(validator.isValid("http://test.xn--h2brj9c")); // India
        assertTrue(validator.isValid("http://test.xn--mgbbh1a71e")); // India
        assertTrue(validator.isValid("http://test.xn--fpcrj9c3d")); // India
        assertTrue(validator.isValid("http://test.xn--gecrj9c")); // India
        assertTrue(validator.isValid("http://test.xn--s9brj9c")); // India
        assertTrue(validator.isValid("http://test.xn--xkc2dl3a5ee0h")); // India
        assertTrue(validator.isValid("http://test.xn--45brj9c")); // India
        assertTrue(validator.isValid("http://test.xn--mgba3a4f16a")); // Iran
        assertTrue(validator.isValid("http://test.xn--mgbayh7gpa")); // Jordan
        assertTrue(validator.isValid("http://test.xn--mgbc0a9azcg")); // Morocco
        assertTrue(validator.isValid("http://test.xn--ygbi2ammx")); // Palestinian Territory
        assertTrue(validator.isValid("http://test.xn--wgbl6a")); // Qatar
        assertTrue(validator.isValid("http://test.xn--p1ai")); // Russia
        assertTrue(validator.isValid("http://test.xn--mgberp4a5d4ar")); //  Saudi Arabia
        assertTrue(validator.isValid("http://test.xn--90a3ac")); // Serbia
        assertTrue(validator.isValid("http://test.xn--yfro4i67o")); // Singapore
        assertTrue(validator.isValid("http://test.xn--clchc0ea0b2g2a9gcd")); // Singapore
        assertTrue(validator.isValid("http://test.xn--3e0b707e")); // South Korea
        assertTrue(validator.isValid("http://test.xn--fzc2c9e2c")); // Sri Lanka
        assertTrue(validator.isValid("http://test.xn--xkc2al3hye2a")); // Sri Lanka
        assertTrue(validator.isValid("http://test.xn--ogbpf8fl")); // Syria
        assertTrue(validator.isValid("http://test.xn--kprw13d")); // Taiwan
        assertTrue(validator.isValid("http://test.xn--kpry57d")); // Taiwan
        assertTrue(validator.isValid("http://test.xn--o3cw4h")); // Thailand
        assertTrue(validator.isValid("http://test.xn--pgbs0dh")); // Tunisia
        assertTrue(validator.isValid("http://test.xn--mgbaam7a8h")); // United Arab Emirates
        // Proposed internationalized ccTLDs
//        assertTrue(validator.isValid("http://test.xn--54b7fta0cc")); // Bangladesh
//        assertTrue(validator.isValid("http://test.xn--90ae")); // Bulgaria
//        assertTrue(validator.isValid("http://test.xn--node")); // Georgia
//        assertTrue(validator.isValid("http://test.xn--4dbrk0ce")); // Israel
//        assertTrue(validator.isValid("http://test.xn--mgb9awbf")); // Oman
//        assertTrue(validator.isValid("http://test.xn--j1amh")); // Ukraine
//        assertTrue(validator.isValid("http://test.xn--mgb2ddes")); // Yemen
        // Test TLDs
//        assertTrue(validator.isValid("http://test.xn--kgbechtv")); // Arabic
//        assertTrue(validator.isValid("http://test.xn--hgbk6aj7f53bba")); // Persian
//        assertTrue(validator.isValid("http://test.xn--0zwm56d")); // Chinese
//        assertTrue(validator.isValid("http://test.xn--g6w251d")); // Chinese
//        assertTrue(validator.isValid("http://test.xn--80akhbyknj4f")); // Russian
//        assertTrue(validator.isValid("http://test.xn--11b5bs3a9aj6g")); // Hindi
//        assertTrue(validator.isValid("http://test.xn--jxalpdlp")); // Greek
//        assertTrue(validator.isValid("http://test.xn--9t4b11yi5a")); // Korean
//        assertTrue(validator.isValid("http://test.xn--deba0ad")); // Yiddish
//        assertTrue(validator.isValid("http://test.xn--zckzah")); // Japanese
//        assertTrue(validator.isValid("http://test.xn--hlcj6aya9esc7a")); // Tamil
    }

    public void testValidator361() {
        UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid("http://hello.tokyo/"));
    }

    public void testValidator363() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://www.example.org/a/b/hello..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/a/hello..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello.world/"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello..world/"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello.world"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/.../world"));
        assertFalse(urlValidator.isValid("http://www.example.org/../world"));
        assertFalse(urlValidator.isValid("http://www.example.org/.."));
        assertFalse(urlValidator.isValid("http://www.example.org/../"));
        assertFalse(urlValidator.isValid("http://www.example.org/./.."));
        assertFalse(urlValidator.isValid("http://www.example.org/././.."));
        assertTrue(urlValidator.isValid("http://www.example.org/..."));
        assertTrue(urlValidator.isValid("http://www.example.org/.../"));
        assertTrue(urlValidator.isValid("http://www.example.org/.../.."));
    }

    public void testValidator375() {
        UrlValidator validator = new UrlValidator();
        String url = "http://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]:80/index.html";
        assertTrue("IPv6 address URL should validate: " + url, validator.isValid(url));
        url = "http://[::1]:80/index.html";
        assertTrue("IPv6 address URL should validate: " + url, validator.isValid(url));
        url = "http://FEDC:BA98:7654:3210:FEDC:BA98:7654:3210:80/index.html";
        assertFalse("IPv6 address without [] should not validate: " + url, validator.isValid(url));
    }

    public void testValidator353() { // userinfo
        UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid("http://www.apache.org:80/path"));
        assertTrue(validator.isValid("http://user:pass@www.apache.org:80/path"));
        assertTrue(validator.isValid("http://user:@www.apache.org:80/path"));
        assertTrue(validator.isValid("http://user@www.apache.org:80/path"));
        assertTrue(validator.isValid("http://us%00er:-._~!$&'()*+,;=@www.apache.org:80/path"));
        assertFalse(validator.isValid("http://:pass@www.apache.org:80/path"));
        assertFalse(validator.isValid("http://:@www.apache.org:80/path"));
        assertFalse(validator.isValid("http://user:pa:ss@www.apache.org/path"));
        assertFalse(validator.isValid("http://user:pa@ss@www.apache.org/path"));
    }

    public void testValidator382() {
        UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid("ftp://username:password@example.com:8042/over/there/index.dtb?type=animal&name=narwhal#nose"));
    }

    public void testValidator380() {
        UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid("http://www.apache.org:80/path"));
        assertTrue(validator.isValid("http://www.apache.org:8/path"));
        assertTrue(validator.isValid("http://www.apache.org:/path"));
    }

    public void testValidator420() {
        UrlValidator validator = new UrlValidator();
        assertFalse(validator.isValid("http://example.com/serach?address=Main Avenue"));
        assertTrue(validator.isValid("http://example.com/serach?address=Main%20Avenue"));
        assertTrue(validator.isValid("http://example.com/serach?address=Main+Avenue"));
    }



    /********************************************
     * Test data for creating a composite URL
     ********************************************/



    //------- Random Test data for creating a random composite URL

    private ResultPair[] randomUrlScheme;
    private ResultPair[] randomUrlAuthority;
    private ResultPair[] randomUrlPort;
    private ResultPair[] randomUrlPath;
    private ResultPair[] randomUrlQuery;

    private Object[] randomUrlTestParts;

    private void generateRandomData(int qty){

        randomUrlScheme = new ResultPair[qty];
        randomUrlAuthority = new ResultPair[qty];
        randomUrlPort = new ResultPair[qty];
        randomUrlPath = new ResultPair[qty];
        randomUrlQuery = new ResultPair[qty];
        randomUrlTestParts = new Object[5];

        RandomUrlCompGenerator randGen = new RandomUrlCompGenerator();
        RandomUrlValidator randValid = new RandomUrlValidator();
        DomainValidator domValid = DomainValidator.getInstance();

        for(int i = 0; i < qty; ++i){
            String scheme = randGen.scheme();
            randomUrlScheme[i]  = new ResultPair(scheme, randValid.isValidScheme(scheme));
//            System.out.println(randomUrlScheme[i].item);
//            System.out.println(i);
        }
        randomUrlTestParts[0] = randomUrlScheme;


        for (int i = 0; i < qty; ++i) {
            String authority = randGen.authority();
            randomUrlAuthority[i] = new ResultPair(authority, domValid.isValid(authority));
        }
        randomUrlTestParts[1] = randomUrlAuthority;


        for (int i = 0; i < qty; ++i) {
            String port = randGen.port();
            randomUrlPort[i] = new ResultPair(port, randValid.isValidPort(port));
        }
        randomUrlTestParts[2] = randomUrlPort;


        for (int i = 0; i < qty; ++i) {
            String path = randGen.path();
            randomUrlPath[i] = new ResultPair(path, randValid.isValidPath(path));
        }
        randomUrlTestParts[3] = randomUrlPath;


        for (int i = 0; i < qty; ++i) {
            String query = randGen.query();
            randomUrlQuery[i] = new ResultPair(query,randValid.isValidQuery(query));
        }
        randomUrlTestParts[4] = randomUrlQuery;

    }









    //-------------------- Test data for creating a composite URL

    /**
     * The data given below approximates the 4 parts of a URL
     * <scheme>://<authority><path>?<query> except that the port number
     * is broken out of authority to increase the number of permutations.
     * A complete URL is composed of a scheme+authority+port+path+query,
     * all of which must be individually valid for the entire URL to be considered
     * valid.
     */
    private ResultPair[] testUrlScheme = {
        new ResultPair("http://", true),
        new ResultPair("https://", true),
        new ResultPair("ftp://", true),
        new ResultPair("h3t://", true),
        new ResultPair("3ht://", false),
        new ResultPair("htp://", false),
        new ResultPair("http:/", false),
        new ResultPair("http:", false),
        new ResultPair(":http:", false),
        new ResultPair("::ftp:", false),
        new ResultPair("http/", false),
        new ResultPair(":", false),
        new ResultPair("foo", false),
        new ResultPair("", true),
        new ResultPair("_", false),
        new ResultPair("mailto://", true),
        new ResultPair("fax://", true),
    };


    private ResultPair[] testUrlAuthority = {
        new ResultPair("www.google.com", true),
        new ResultPair("www.google.com.", true),
        new ResultPair("www.google.com.au", true),
        new ResultPair("www.google.com.uk", true),
        new ResultPair("com.google.www", false),
        new ResultPair("go.com", true),
        new ResultPair("go.au", true),
        new ResultPair("0.0.0.0", true),
        new ResultPair("-1.0.0.0", false),
        new ResultPair("a.b.c.d", false),
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
        new ResultPair("", false),
        new ResultPair("...", false),
        new ResultPair("cs362", false),

    };

    private ResultPair[] testUrlPort = {
        new ResultPair(":80", true),
        new ResultPair(":65535", true), // max possible
        new ResultPair(":65536", false), // max possible +1
        new ResultPair(":0", true),
        new ResultPair("", true),
        new ResultPair(":-1", false),
        new ResultPair(":65636", false),
        new ResultPair(":999999999999999999", false),
        new ResultPair(":65a", false),
        new ResultPair("0", false),
        new ResultPair("a", false),
        new ResultPair("_", false),
    };

    private ResultPair[] testPath = {
        new ResultPair("/foo1", true),
        new ResultPair("/123", true),
        new ResultPair("/1_2_3", true),
        new ResultPair("/bar123", true),
        new ResultPair("/$55", true),
        new ResultPair("/..", false),
        new ResultPair("/../", false),
        new ResultPair("..", false),
        new ResultPair(".", false),
        new ResultPair("/foo1/", true),
        new ResultPair("", true),
        new ResultPair("/?", true),
        new ResultPair("/foo1/bar", true),
        new ResultPair("/..//baz", false),
        new ResultPair("/foo//bar", false)

    };

    //Test allow2slash, noFragment
    private ResultPair[] testUrlPathOptions = {
        new ResultPair("/foo1", true),
        new ResultPair("/foo123", true),
        new ResultPair("/$55", true),
        new ResultPair("/$$", true),
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

    private ResultPair[] testUrlQuery = {
        new ResultPair("?foo=bar", true),
        new ResultPair("?foo=bar&bar=baz", true),
        new ResultPair("", true),
        new ResultPair(" ", false),
        new ResultPair("foo bar", false),
        new ResultPair("?q = foo", false),
        new ResultPair("? ?", false),
        new ResultPair("?&q=f o o", false),
        new ResultPair("?foo", true),
        new ResultPair("?a=", true),
        new ResultPair("?a=b=", true),
        new ResultPair("?", true),
    };

    private Object[] testUrlParts = {testUrlScheme, testUrlAuthority, testUrlPort, testPath, testUrlQuery};
    private Object[] testUrlPartsOptions = {testUrlScheme, testUrlAuthority, testUrlPort, testUrlPathOptions, testUrlQuery};
    private int[] testPartsIndex = {0, 0, 0, 0, 0};

    //---------------- Test data for individual url parts ----------------
    private final String[] schemes = {"http", "gopher", "g0-To+.",
        "not_valid" // TODO this will need to be dropped if the ctor validates schemes
    };


    private ResultPair[] testScheme = {
        new ResultPair("http", true),
        new ResultPair("ftp", false),
        new ResultPair("httpd", false),
        new ResultPair("gopher", true),
        new ResultPair("g0-to+.", true),
        new ResultPair("not_valid", false), // underscore not allowed
        new ResultPair("HtTp", true),
        new ResultPair("telnet", false),
    };


    // URL values for testing
    private ResultPair[] testUrl = {
        new ResultPair("http://www.google.com", true),
        new ResultPair("https://www.google.com", true),
        new ResultPair("", false),
        new ResultPair("foo.bar", false),
        new ResultPair("http://badge.example.com/puppy.html", true),
        new ResultPair("http://example.com/#approval", true),
        new ResultPair("http://www.example.com", true),
        new ResultPair("http://www.example.com/", true),
        new ResultPair("http://www.example.org", true),
        new ResultPair("http://bell.example.com/balance?apparatus=attraction", true),
        new ResultPair("http://www.example.com/authority/bag.htm", true),
        new ResultPair("http://www.example.net/box", true),
        new ResultPair("http://www.example.com/aunt/bite", true),
        new ResultPair("http://anger.example.edu/", true),
        new ResultPair("https://example.com/baseball", true),
        new ResultPair("http://bubble.example.com/?air=authority", true),
        new ResultPair("https://www.example.com/advice/cat?books=puppy&mail=art", true),
        new ResultPair("http://bubble.example.com/", true),
        new ResultPair("http://example.org/", true),
        new ResultPair("https://example.com/?ants=greenbean", true),
        new ResultPair("http://example.com/battle", true),
        new ResultPair("http://www.example.com/apple/greenbean", true),
        new ResultPair("https://www.example.com/art.aspx?book=ball", true),
        new ResultPair("https://www.example.org/airplane.php", true),
        new ResultPair("http://www.example.com/bubble/dog.php?afterthought=bag&apparatus=border", true),
        new ResultPair("http://attraction.example.com/approval?birthday=birthday", true),
        new ResultPair("http://www.example.org/bat.php", true),
        new ResultPair("https://www.example.com/?balance=birthday#adjustment", true),
        new ResultPair("https://cat.example.com/belief", true),
        new ResultPair("http://example.com/?battle=appliance", true),
        new ResultPair("http://www.example.com/book.aspx", true),
        new ResultPair("https://example.com/", true),
        new ResultPair("https://www.example.com/brother", true),
        new ResultPair("https://example.org/apple.html", true),
        new ResultPair("https://www.example.com/brake", true),
        new ResultPair("http://example.com/afterthought/boundary", true),
        new ResultPair("http://example.com/bit", true),
        new ResultPair("http://www.example.net/", true),
        new ResultPair("http://example.edu/acoustics/beef", true),
        new ResultPair("http://www.example.com/board", true),
        new ResultPair("http://example.com/", true),
        new ResultPair("https://account.example.com/badge.php", true),
        new ResultPair("http://www.example.com/agreement.aspx#amusement", true),
        new ResultPair("https://www.example.com/bat.php", true),
        new ResultPair("http://example.net/bell/believe", true),
        new ResultPair("http://www.example.com/", true),
        new ResultPair("https://www.example.com/dog/bell.php#afternoon", true),
        new ResultPair("http://www.example.net/", true),
        new ResultPair("http://www.example.com/after", true),
        new ResultPair("http://afterthought.example.com/", true),
        new ResultPair("https://bag.example.net/", true),
        new ResultPair("http://cat.example.com/afternoon", true),
        new ResultPair("http://www.example.com/agreement/mail.php#air", true),
        new ResultPair("https://example.com/balance/greenbean", true),
        new ResultPair("https://www.example.com/branch/bee", true),
        new ResultPair("http://zebra.example.net/?arch=arm&activity=birth", true),
        new ResultPair("http://boundary.example.net/", true),
        new ResultPair("https://www.example.com/border", true),
        new ResultPair("http://www.example.com/basin/boat.html", true),
        new ResultPair("https://www.example.com/?arch=bubble&brother=basin", true),
        new ResultPair("http://www.example.com/?animal=amusement&animal=balance", true),
        new ResultPair("http://air.example.com/brake/box.php", true),
        new ResultPair("https://bagroom.example.net/airport.htm", true),
        new ResultPair("https://basin.example.com/believe.php", true),
        new ResultPair("http://www.example.org/?arithmetic=bag&birthday=bear", true),
        new ResultPair("http://www.example.com/bubble", true),
        new ResultPair("https://www.example.edu/animal?airplane=bikes&lamp=apparatus", true),
        new ResultPair("https://bell.example.com/", true),
        new ResultPair("https://www.example.com/#bags", true),
        new ResultPair("http://example.com/", true),
        new ResultPair("http://www.example.org/greenbean/bike.aspx", true),
        new ResultPair("http://www.example.com/flower/brass.aspx", true),
        new ResultPair("http://www.example.com/bird/amount", true),
        new ResultPair("https://www.example.com/?berry=bell", true),
        new ResultPair("http://example.com/?apparatus=bell", true),
        new ResultPair("http://example.com/army/battle.html", true),
        new ResultPair("http://back.example.com/back/art", true),
        new ResultPair("https://www.example.org/", true),
        new ResultPair("https://www.example.com/#ants", true),
        new ResultPair("https://www.example.com/?mail=bubble", true),
        new ResultPair("https://example.com/bath", true),
        new ResultPair("https://example.com/back/bell.php", true),
        new ResultPair("http://www.example.com/branch", true),
        new ResultPair("http://boat.example.com/", true),
        new ResultPair("https://www.example.com/arithmetic.html", true),
        new ResultPair("http://example.com/dog", true),
        new ResultPair("https://action.example.com/", true),
        new ResultPair("https://www.example.net/", true),
        new ResultPair("http://www.example.edu/", true),
        new ResultPair("https://www.example.net/bags.html?bells=bee", true),
        new ResultPair("http://example.com/after/air", true),
        new ResultPair("https://example.com/alarm.php", true),
        new ResultPair("http://www.example.com/", true),
        new ResultPair("https://www.example.org/airport/agreement", true),
        new ResultPair("http://example.com/books/acoustics", true),
        new ResultPair("https://www.example.com/books", true),
        new ResultPair("https://example.com/car?boundary=bells&battle=bee", true),
        new ResultPair("http://www.example.com/badge.html?car=cat&zebra=border", true),
        new ResultPair("https://bath.example.com/", true),
        new ResultPair("https://example.com/bells/basketball#boat", true),
        new ResultPair("http://www.example.org/?arithmetic=boat&bag=bag", true),
        new ResultPair("https://wheel.example.com/art#arch", true),
        new ResultPair("https://example.net/amusement/art.html", true),
        new ResultPair("https://example.com/bag", true),
        new ResultPair("http://arm.example.org/?acoustics=brass", true),
        new ResultPair("https://www.example.com/afterthought/afternoon.php", true),
        new ResultPair("https://achiever.example.com/lamp/wheel.html", true),
        new ResultPair("https://www.example.com/birth.aspx?boundary=bath&zebra=after", true),
        new ResultPair("http://example.com/books.html", true),
        new ResultPair("https://www.example.com/boundary/adjustment.html", true),
        new ResultPair("http://box.example.com/badge/bead", true),
        new ResultPair("https://www.example.com/battle/bell.aspx?advertisement=account&bag=airplane", true),
        new ResultPair("http://www.example.com/achiever.aspx", true),
        new ResultPair("http://www.example.com/account.aspx?flower=actor&belief=birth", true),
        new ResultPair("https://www.example.edu/afterthought/bikes.php#arch", true),
        new ResultPair("https://www.example.net/attraction/account#arm", true),
        new ResultPair("https://www.example.com/adjustment", true),
        new ResultPair("http://www.example.com/army", true),
        new ResultPair("https://www.example.com/greenbean.php?basketball=board", true),
        new ResultPair("https://www.example.com/bath.htm", true),
        new ResultPair("https://www.example.com/#flower", true),
        new ResultPair("https://branch.example.com/?bell=wheel&angle=agreement", true),
        new ResultPair("https://www.example.com/amusement/basket.html", true),
        new ResultPair("https://www.example.org/beginner.html", true),
        new ResultPair("http://example.com/bear", true),
        new ResultPair("https://example.com/base.aspx", true),
        new ResultPair("http://www.example.org/lamp", true),
        new ResultPair("http://example.net/agreement/bubble.html", true),
        new ResultPair("http://appliance.example.com/bagroom/bell.php", true),
        new ResultPair("http://example.net/beginner/advice?book=bag", true),
        new ResultPair("https://example.com/badge/brother.php?bit=brick&action=bagroom", true),
        new ResultPair("https://www.example.com/#baseball", true),
        new ResultPair("https://example.net/bikes/apparatus.php", true),
        new ResultPair("http://zebra.example.com/adjustment.aspx", true),
        new ResultPair("http://www.example.com/bird?bags=boot&puppy=bag#boy", true),
        new ResultPair("http://example.com/bear/bell?art=back", true),
        new ResultPair("http://activity.example.com/", true),
        new ResultPair("http://example.org/bubble/box", true),
        new ResultPair("https://www.example.com/?afternoon=cat", true),
        new ResultPair("http://example.net/", true),
        new ResultPair("http://example.com/advertisement.html", true),
        new ResultPair("http://arch.example.com/", true),
        new ResultPair("https://back.example.com/achiever", true),
        new ResultPair("http://example.com/cat", true),
        new ResultPair("https://www.example.edu/", true),
        new ResultPair("http://foo.co/badge/boat#bubble", true),
        new ResultPair("http://example.com/advice.html?cat=greenbean&arch=attraction", true),
        new ResultPair("https://example.com/board/box", true),
        new ResultPair("https://www.example.com/battle/alarm.aspx?basketball=basket&berry=art", true),
        new ResultPair("https://www.example.com/addition", true),
        new ResultPair("https://example.net/cat.html?ball=breath", true),
        new ResultPair("https://example.com/birds", true),
        new ResultPair("http://example.com/?adjustment=appliance", true),
        new ResultPair("http://www.example.net/boy/beginner.php", true),
        new ResultPair("https://www.example.com/?dog=bags&apparel=afterthought", true),
        new ResultPair("https://www.example.com/#brass", true),
        new ResultPair("http://example.com/birds", true),
        new ResultPair("http://www.example.edu/bath?apparel=dog&addition=authority", true),
        new ResultPair("https://www.example.com/addition/bike", true),
        new ResultPair("https://example.com/act/bird.aspx#box", true),
        new ResultPair("http://example.com/back.php", true),
        new ResultPair("https://example.org/airport/balance", true),
        new ResultPair("https://example.org/badge/account.php?bag=birth&aunt=bird", true),
        new ResultPair("https://example.com/bags.aspx", true),
        new ResultPair("https://www.example.com/brake/bite.html#birds", true),
        new ResultPair("http://www.example.com/afterthought/zebra", true),
        new ResultPair("http://www.example.com/balance.php", true),
        new ResultPair("http://example.com/beginner", true),
        new ResultPair("https://www.example.com/arch.aspx", true),
        new ResultPair("https://www.example.com/#brake", true),
        new ResultPair("http://wheel.example.com/dog/brake.php", true),
        new ResultPair("https://mail.example.net/beef.html", true),
        new ResultPair("https://afterthought.example.net/bag", true),
        new ResultPair("https://advertisement.example.edu/authority/base.aspx", true),
        new ResultPair("http://example.net/lamp.html", true),
        new ResultPair("https://www.example.com/bubble/account.htm?amount=activity", true),
        new ResultPair("http://example.net/bee.aspx#bubble", true),
        new ResultPair("https://border.example.com/", true),
        new ResultPair("http://www.example.com/birth/belief?action=belief&arch=bags", true),
        new ResultPair("http://angle.example.com/?addition=mail", true),
        new ResultPair("https://example.edu/bubble", true),
        new ResultPair("https://www.example.com/ball.php?cat=bell", true),
        new ResultPair("http://example.com/bells", true),
        new ResultPair("https://bead.example.com/#apparatus", true),
        new ResultPair("https://example.edu/#baseball", true),
        new ResultPair("http://wheel.example.com/ants.html", true),
        new ResultPair("https://afterthought.example.net/", true),
        new ResultPair("https://example.com/bike/bat", true),
        new ResultPair("http://bikes.example.org/puppy", true),
        new ResultPair("https://bell.example.com/#aunt", true),
        new ResultPair("https://www.example.com/?alarm=brother&bubble=apple", true),
        new ResultPair("http://example.com/#brother", true),
        new ResultPair("http://www.example.com/apparel/approval.aspx", true),
        new ResultPair("https://baseball.example.com/", true),
        new ResultPair("http://arithmetic.example.com/", true),
        new ResultPair("http://www.example.com/boot/authority.aspx", true),
        new ResultPair("https://beginner.example.org/border", true),
        new ResultPair("http://bit.example.com/amusement.aspx", true),
        new ResultPair("https://angle.example.net/?actor=boot#action", true),
        new ResultPair("https://example.com/#acoustics", true),
        new ResultPair("https://www.example.org/agreement/brass", true),
        new ResultPair("https://www.example.com/boot?back=acoustics&action=apparel", true),
        new ResultPair("http://bat.example.com/?car=bubble&bath=act", true),
        new ResultPair("http://example.com/back", true),
        new ResultPair("https://example.com/back", true),
        new ResultPair("https://example.com/authority?dog=dog&airplane=bear#wheel", true),
        new ResultPair("https://www.example.com/#bike", true),
        new ResultPair("https://amount.example.com/?apple=authority", true),
        new ResultPair("https://www.example.com/#account", true),
        new ResultPair("https://activity.example.com/", true),
        new ResultPair("http://bit.example.com/addition/arch.php", true),
        new ResultPair("http://example.org/brass.php", true),
        new ResultPair("https://after.example.com/advice/air.htm", true),
        new ResultPair("http://mail.example.com/brother.php", true),
        new ResultPair("https://brother.example.com/bat#believe", true),
        new ResultPair("http://addition.example.com/ants", true),
        new ResultPair("https://www.example.org/?birthday=zebra&brake=acoustics", true),
        new ResultPair("https://bear.example.net/car/acoustics.php", true),
        new ResultPair("https://example.org/advertisement.html", true),
        new ResultPair("https://example.com/breath/bell?approval=afternoon&art=appliance", true),
        new ResultPair("https://example.com/brake/authority.aspx", true),
        new ResultPair("http://www.example.org/arch/birds?authority=arm&action=border", true),
        new ResultPair("https://account.example.com/bag.php#airport", true),
        new ResultPair("https://example.net/bag/apparatus", true),
        new ResultPair("https://www.example.net/bird/apparatus.aspx?bag=apple", true),
        new ResultPair("https://www.example.com/basketball/actor", true),
        new ResultPair("https://www.example.com/branch.php", true),
        new ResultPair("https://boy.example.edu/arithmetic/angle.php#greenbean", true),
        new ResultPair("http://www.example.com/?ball=beef", true),
        new ResultPair("http://example.org/anger/birthday.html", true),
        new ResultPair("http://example.com/box#boot", true),
        new ResultPair("http://belief.example.com/", true),
        new ResultPair("http://greenbean.example.net/border.aspx", true),
        new ResultPair("https://attraction.example.com/berry/afternoon", true),
        new ResultPair("https://www.example.com/amusement.aspx?beef=bear&aunt=adjustment", true),
        new ResultPair("http://boundary.example.net/army", true),
        new ResultPair("http://example.com/bells.html", true),
        new ResultPair("http://boat.example.com/birthday.php?ball=account", true),
        new ResultPair("https://www.example.com/puppy.aspx?apparatus=apparatus", true),
        new ResultPair("http://breath.example.com/", true),
        new ResultPair("http://apparel.example.com/attraction/bag.php#arithmetic", true),
        new ResultPair("https://actor.example.com/bee", true),
        new ResultPair("http://arm.example.net/", true),
        new ResultPair("https://www.example.com/ants/afternoon", true),
        new ResultPair("http://actor.example.net/bubble/basket", true),
        new ResultPair("https://animal.example.com/airplane", true),
        new ResultPair("https://www.example.com/bit.php", true),
        new ResultPair("http://www.example.com/attraction/basket.php", true),
        new ResultPair("http://example.net/flower.html", true),
        new ResultPair("http://example.com/bag.php?bear=achiever&brother=account", true),
        new ResultPair("https://example.com/lamp.html?art=activity", true),
        new ResultPair("http://arch.example.com/dog", true),
        new ResultPair("http://www.example.com/?branch=army", true),
        new ResultPair("http://www.example.com/lamp/basin.php?bell=bird&puppy=animal", true),
        new ResultPair("https://apparatus.example.com/", true),
        new ResultPair("http://adjustment.example.com/act/brick.html", true),
        new ResultPair("https://www.example.org/aunt.html", true),
        new ResultPair("http://basket.example.com/", true),
        new ResultPair("http://animal.example.org/?mail=attraction&base=berry", true),
        new ResultPair("http://angle.example.com/beginner?bead=bell", true),
        new ResultPair("http://www.example.net/birds#brother", true),
        new ResultPair("http://apple.example.com/basket/bubble", true),
        new ResultPair("http://www.example.com/#adjustment", true),
        new ResultPair("https://www.example.com/brother?achiever=bird&bell=apple", true),
        new ResultPair("https://dog.example.com/", true),
        new ResultPair("http://www.example.com/art/board.html?account=brother&bit=birth", true),
        new ResultPair("http://example.net/apple", true),
        new ResultPair("http://www.example.com/box/activity#airplane", true),
        new ResultPair("http://air.example.com/airplane#bird", true),
        new ResultPair("http://www.example.com/boy.php", true),
        new ResultPair("http://example.com/afternoon", true),
        new ResultPair("https://www.example.com/ants/wheel", true),
        new ResultPair("https://example.net/", true),
        new ResultPair("http://www.example.net/#baseball", true),
        new ResultPair("https://example.org/", true),
        new ResultPair("https://www.example.com/bubble.aspx?arithmetic=zebra", true),
        new ResultPair("http://bear.example.edu/basketball.html", true),
        new ResultPair("http://acoustics.example.com/arch.html", true),
        new ResultPair("http://example.com/amusement", true),
        new ResultPair("https://www.example.com/?bag=believe", true),
        new ResultPair("https://www.example.org/bell/beef", true),
        new ResultPair("https://www.example.com/berry/bite", true),
        new ResultPair("http://www.example.com/?account=addition", true),
        new ResultPair("http://www.example.com/army/birds", true),
        new ResultPair("https://example.com/bit.aspx", true),
        new ResultPair("http://www.example.com/army.aspx?boy=basin", true),
        new ResultPair("https://actor.example.com/", true),
        new ResultPair("https://example.org/badge", true),
        new ResultPair("https://example.org/apparatus/attraction.htm#apparatus", true),
        new ResultPair("https://www.example.com/acoustics/bell?authority=bell&bell=brake", true),
        new ResultPair("http://example.org/bee/border.php", true),
        new ResultPair("http://www.example.com/angle.php", true),
        new ResultPair("https://example.edu/", true),
        new ResultPair("https://www.example.com/?beef=ants", true),
        new ResultPair("https://www.example.com/army/bells", true),
        new ResultPair("https://boundary.example.com/boot", true),
        new ResultPair("https://www.example.com/boat.html", true),
        new ResultPair("https://addition.example.com/actor/wheel.html", true),
        new ResultPair("http://boat.example.net/account", true),
        new ResultPair("http://www.example.org/actor?greenbean=dog&amount=bags", true),
        new ResultPair("http://www.example.com/apparatus/lamp", true),
        new ResultPair("http://example.com/?base=army&boat=adjustment", true),
        new ResultPair("https://example.com/beef/arch", true),
        new ResultPair("https://ants.example.com/advice/airport.html", true),
        new ResultPair("http://bite.example.edu/board.php", true),
        new ResultPair("http://example.com/airplane/art.htm", true),
        new ResultPair("http://www.example.com/bear?bat=appliance", true),
        new ResultPair("https://badge.example.edu/bagroom/actor", true),
        new ResultPair("http://www.example.com/book/bubble.php", true),
        new ResultPair("http://example.com/bee", true),
        new ResultPair("http://www.example.org/bike", true),
        new ResultPair("http://www.example.com/wheel.html?after=brass", true),
        new ResultPair("https://www.example.com/?airplane=art", true),
        new ResultPair("http://example.com/anger", true),
        new ResultPair("https://greenbean.example.com/", true),
        new ResultPair("http://example.com/appliance", true),
        new ResultPair("https://www.example.com/?branch=base", true),
        new ResultPair("https://afternoon.example.edu/?army=apple&airport=greenbean", true),
        new ResultPair("https://www.example.net/?amount=activity&bubble=after", true),
        new ResultPair("http://example.org/boot", true),
        new ResultPair("http://example.com/alarm/bubble.aspx", true),
        new ResultPair("http://boundary.example.com/approval#act", true),
        new ResultPair("https://www.example.com/bagroom/acoustics?bite=brake&bell=dog", true),
        new ResultPair("http://www.example.com/base.php", true),
        new ResultPair("https://example.com/after.php", true),
        new ResultPair("https://www.example.net/greenbean.php", true),
        new ResultPair("https://example.com/bell.aspx", true),
        new ResultPair("https://www.example.net/authority/bag.html?arithmetic=cat", true),
        new ResultPair("https://www.example.com/basket", true),
        new ResultPair("https://army.example.com/", true),
        new ResultPair("https://car.example.com/achiever", true),
        new ResultPair("http://example.net/branch", true),
        new ResultPair("https://www.example.com/baseball.php", true),
        new ResultPair("http://example.com/bikes.php?amount=acoustics&breath=balance", true),
        new ResultPair("https://www.example.com/anger", true),
        new ResultPair("https://www.example.com/badge.htm", true),
        new ResultPair("https://mail.example.net/", true),
        new ResultPair("https://boy.example.edu/?bag=cat", true),
        new ResultPair("http://adjustment.example.com/brass/belief?bags=bags&battle=after", true),
        new ResultPair("https://www.example.com/bubble/activity.php", true),
        new ResultPair("https://brake.example.com/bell/bit", true),
        new ResultPair("http://example.net/bags/berry", true),
        new ResultPair("http://believe.example.com/angle.aspx#basket", true),
        new ResultPair("http://www.example.org/#mail", true),
        new ResultPair("https://basketball.example.com/actor/afternoon.php", true),
        new ResultPair("http://example.com/?air=appliance&bell=branch", true),
        new ResultPair("https://www.example.com/bike.html", true),
        new ResultPair("http://example.com/birthday/bag", true),
        new ResultPair("https://www.example.com/box?animal=bell&approval=baseball", true),
        new ResultPair("http://www.example.com/addition#attraction", true),
        new ResultPair("http://example.net/achiever.html", true),
        new ResultPair("http://example.com/car?acoustics=bell&bubble=advice", true),
        new ResultPair("https://bell.example.com/?arch=back&alarm=agreement#ball", true),
        new ResultPair("http://www.example.com/bite", true),
        new ResultPair("http://basin.example.com/berry.aspx", true),
        new ResultPair("https://bike.example.com/bead.aspx", true),
        new ResultPair("https://www.example.net/birds.html", true),
        new ResultPair("http://www.example.net/birds.php", true),
        new ResultPair("https://www.example.org/?greenbean=adjustment", true),
        new ResultPair("http://puppy.example.org/bubble", true),
        new ResultPair("https://www.example.net/anger?boy=brick#bubble", true),
        new ResultPair("http://www.example.edu/bear/attraction.html", true),
        new ResultPair("http://example.net/alarm/air", true),
        new ResultPair("http://bath.example.com/authority.aspx?agreement=birthday&apparatus=brick", true),
        new ResultPair("http://www.example.org/approval.php?air=border&basketball=beef", true),
        new ResultPair("https://www.example.com/army/bead", true),
        new ResultPair("http://www.example.com/apple", true),
        new ResultPair("http://boot.example.com/", true),
        new ResultPair("http://www.example.com/?board=bell", true),
        new ResultPair("https://www.example.com/airport/birth?boy=arithmetic", true),
        new ResultPair("https://example.com/?bubble=baseball&box=ants", true),
        new ResultPair("https://authority.example.edu/boat.aspx", true),
        new ResultPair("http://bell.example.com/?animal=bird#actor", true),
        new ResultPair("https://www.example.com/alarm", true),
        new ResultPair("http://www.example.com/boot", true),
        new ResultPair("https://www.example.com/brick/board", true),
        new ResultPair("https://www.example.com/arch.html#boy", true),
        new ResultPair("https://example.com/appliance/bat", true),
        new ResultPair("https://flower.example.com/boot?battle=bubble&addition=back", true),
        new ResultPair("https://www.example.com/belief.php", true),
        new ResultPair("http://www.example.com/ball.php", true),
        new ResultPair("https://www.example.com/boot/approval.aspx?arch=puppy&army=board", true),
        new ResultPair("https://www.example.com/beef.php?bells=badge&bird=cat", true),
        new ResultPair("http://example.com/brick", true),
        new ResultPair("https://www.example.org/#advertisement", true),
        new ResultPair("https://www.example.com/approval/dog", true),
        new ResultPair("https://www.example.com/bell/boundary", true),
        new ResultPair("https://example.com/apparatus?book=account", true),
        new ResultPair("http://www.example.edu/advertisement.php?baseball=amount&bird=brother", true),
        new ResultPair("http://example.net/book/brake.html", true),
        new ResultPair("https://example.com/beginner/baseball?bird=bag", true),
        new ResultPair("http://www.example.com/airport", true),
        new ResultPair("https://example.com/?back=apple&achiever=afternoon", true),
        new ResultPair("https://lamp.example.edu/brake", true),
        new ResultPair("http://www.example.com/belief", true),
        new ResultPair("https://example.com/lamp", true),
        new ResultPair("http://www.example.com/?bag=brass&battle=back", true),
        new ResultPair("http://attraction.example.org/", true),
        new ResultPair("http://example.com/?alarm=badge&badge=believe", true),
        new ResultPair("https://flower.example.com/actor", true),
        new ResultPair("https://brass.example.com/", true),
        new ResultPair("https://www.example.com/bubble/bag", true),
        new ResultPair("http://www.example.com/?believe=anger", true),
        new ResultPair("https://ants.example.com/?bat=arm", true),
        new ResultPair("http://example.org/dog/ball.php", true),
        new ResultPair("https://www.example.com/bat", true),
        new ResultPair("https://example.net/act/back.php?army=bells&basketball=angle", true),
        new ResultPair("http://www.example.com/books/beginner.php", true),
        new ResultPair("https://example.com/achiever.html", true),
        new ResultPair("http://www.example.com/book", true),
        new ResultPair("https://example.com/?anger=basket#cat", true),
        new ResultPair("http://www.example.net/battle", true),
        new ResultPair("https://books.example.org/", true),
        new ResultPair("http://example.com/bag/animal.aspx", true),
        new ResultPair("https://air.example.com/bell.html", true),
        new ResultPair("https://www.example.com/bag?angle=basin", true),
        new ResultPair("http://bubble.example.edu/apparel", true),
        new ResultPair("https://breath.example.com/art/bee.html", true),
        new ResultPair("https://www.example.com/airport", true),
        new ResultPair("http://example.com/berry.aspx", true),
        new ResultPair("https://example.com/afternoon.php?lamp=cat&badge=budge", true),
        new ResultPair("http://example.net/approval", true),
        new ResultPair("http://example.com/?bubble=flower", true),
        new ResultPair("https://www.example.com/bite.php", true),
        new ResultPair("https://example.org/mail.aspx?adjustment=belief&account=cat", true),
        new ResultPair("http://www.example.com/action/arm.php?bell=alarm&bagroom=mail", true),
        new ResultPair("https://www.example.com/?apple=books", true),
        new ResultPair("https://advertisement.example.com/appliance/boy?brick=approval&brother=greenbean", true),
        new ResultPair("http://brick.example.com/", true),
        new ResultPair("http://alarm.example.com/", true),
        new ResultPair("https://bag.example.com/acoustics/afternoon", true),
        new ResultPair("http://www.example.com/breath?bubble=art&greenbean=brass", true),
        new ResultPair("https://example.com/believe/bike", true),
        new ResultPair("http://www.example.com/bell.php", true),
        new ResultPair("https://brake.example.com/?bikes=achiever&acoustics=ball#bag", true),
        new ResultPair("https://anger.example.com/", true),
        new ResultPair("http://www.example.net/basin", true),
        new ResultPair("http://bags.example.com/", true),
        new ResultPair("https://example.com/afternoon/brake.aspx?addition=approval&dog=airport", true),
        new ResultPair("http://example.com/bell/greenbean", true),
        new ResultPair("https://www.example.com/greenbean/battle#believe", true),
        new ResultPair("https://art.example.com/act/bat.aspx", true),
        new ResultPair("http://www.example.com/belief/amount", true),
        new ResultPair("http://www.example.com/achiever?bikes=bite", true),
        new ResultPair("http://www.example.com/airplane/attraction.html", true),
        new ResultPair("https://example.com/activity", true),
        new ResultPair("http://www.example.com/board", true),
        new ResultPair("http://example.org/angle.aspx#bells", true),
        new ResultPair("http://www.example.org/battle/amusement?bear=bear", true),
        new ResultPair("http://example.org/balance", true),
        new ResultPair("http://www.example.com/bells?ball=breath", true),
        new ResultPair("http://www.example.com/appliance", true),
        new ResultPair("http://basin.example.com/", true),
        new ResultPair("https://www.example.com/achiever.html", true),
        new ResultPair("https://www.example.com/arch", true),
        new ResultPair("http://angle.example.com/", true),
        new ResultPair("https://www.example.com/?beef=board", true),
        new ResultPair("http://example.com/action/action", true),
        new ResultPair("https://example.com/afternoon/cat?battle=afternoon", true),
        new ResultPair("https://example.com/mail", true),
        new ResultPair("https://achiever.example.com/", true),
        new ResultPair("http://boat.example.com/army.html", true),
        new ResultPair("http://bird.example.com/", true),
        new ResultPair("https://example.com/wheel/books.html", true),
        new ResultPair("http://boot.example.com/birthday/after.htm?bells=brass&ball=addition", true),
        new ResultPair("https://www.example.com/apple.html#acoustics", true),
        new ResultPair("http://example.com/amount.html", true),
        new ResultPair("http://www.example.com/?brick=apple", true),
        new ResultPair("http://www.example.com/birth/agreement.aspx", true),
        new ResultPair("https://www.example.com/boat", true),
        new ResultPair("http://example.com/?brick=base&approval=box", true),
        new ResultPair("https://www.example.com/bite.htm?apple=boat&army=advice", true),
        new ResultPair("http://www.example.com/branch.html", true),
        new ResultPair("http://example.com/car/badge.php", true),
        new ResultPair("https://art.example.com/border/activity", true),
        new ResultPair("https://www.example.com/agreement/bells.php", true),
        new ResultPair("http://www.example.com/birds/bath", true),
        new ResultPair("http://www.example.com/?account=bee&birth=birth", true),
        new ResultPair("https://www.example.com/basketball", true),
        new ResultPair("https://after.example.net/", true),
        new ResultPair("http://bag.example.com/apple/breath.htm", true),
        new ResultPair("https://www.example.net/#activity", true),
        new ResultPair("http://anger.example.org/books", true),
        new ResultPair("https://book.example.org/lamp/advice?adjustment=birds", true),
        new ResultPair("https://example.com/beef.html?birds=basketball", true),
        new ResultPair("http://www.example.com/brick/afterthought", true),
        new ResultPair("http://example.com/activity", true),
        new ResultPair("http://www.example.com/dog.aspx", true),
        new ResultPair("https://account.example.com/?bells=arm&advice=art", true),
        new ResultPair("https://www.example.com/?approval=basin&acoustics=bite", true),
        new ResultPair("http://activity.example.net/bee#lamp", true),
        new ResultPair("http://example.net/flower?arithmetic=back", true),
        new ResultPair("http://example.org/agreement#box", true),
        new ResultPair("https://www.example.com/?puppy=beginner&balance=balance", true),
        new ResultPair("https://basin.example.com/act", true),
        new ResultPair("https://www.example.com/account.html", true),
        new ResultPair("https://airplane.example.com/believe/car.html", true),
        new ResultPair("https://www.example.org/birthday.html?apparel=amusement", true),
        new ResultPair("http://www.example.com/belief/bat.php", true),
        new ResultPair("https://www.example.com/ants/breath.htm", true),
        new ResultPair("https://www.example.com/bell", true),
        new ResultPair("https://birth.example.com/", true),
        new ResultPair("http://bikes.example.com/beginner.aspx", true),
        new ResultPair("https://www.example.net/beginner", true),
        new ResultPair("https://example.com/battle/ball#advertisement", true),
        new ResultPair("https://www.example.com/car.php?flower=badge&car=bag", true),
        new ResultPair("http://example.com/animal/base", true),
        new ResultPair("http://example.com/boundary/airport?appliance=bath&agreement=afternoon", true),
        new ResultPair("https://boy.example.org/?wheel=boundary&car=aunt", true),
        new ResultPair("https://www.example.com/book.html", true),
        new ResultPair("http://example.com/airplane/boot.php", true),
        new ResultPair("http://www.example.com/basin.php", true),
        new ResultPair("http://example.com/book/boundary?lamp=brake", true),
        new ResultPair("https://www.example.com/act/wheel.php", true),
        new ResultPair("http://boat.example.com/angle", true),
        new ResultPair("https://apparatus.example.org/", true),
        new ResultPair("https://www.example.com/appliance.html", true),
        new ResultPair("http://amount.example.com/", true),
        new ResultPair("http://example.net/attraction/cat.aspx", true),
        new ResultPair("http://www.example.com/cat.aspx?action=account", true),
        new ResultPair("https://www.example.com/#basin", true),
        new ResultPair("http://www.example.com/attraction", true),
        new ResultPair("http://example.com/?puppy=basin", true),
        new ResultPair("http://www.example.com/bit/boat.html?basin=boot&air=base", true),
        new ResultPair("http://example.com/bubble/bead", true),
        new ResultPair("https://box.example.com/?car=agreement&birds=car", true),
        new ResultPair("http://example.com/bikes/back", true),
        new ResultPair("https://example.com/army/basin", true),
        new ResultPair("http://www.example.com/bead.htm", true),
        new ResultPair("http://example.com/breath", true),
        new ResultPair("https://www.example.org/bit/lamp", true),
        new ResultPair("https://www.example.org/?bubble=animal&amusement=authority", true),
        new ResultPair("https://www.example.com/greenbean/arch.php", true),
        new ResultPair("https://bell.example.com/bags/basin.aspx", true),
        new ResultPair("http://badge.example.com/", true),
        new ResultPair("http://www.example.com/books/ball", true),
        new ResultPair("http://book.example.com/", true),
        new ResultPair("https://www.example.org/advertisement/lamp#acoustics", true),
        new ResultPair("http://www.example.com/bikes.html", true),
        new ResultPair("https://example.com/?back=basketball", true),
        new ResultPair("http://example.com/aunt/bagroom", true),
        new ResultPair("http://example.com/?bikes=air", true),
        new ResultPair("http://achiever.example.net/?brake=mail&alarm=birth", true),
        new ResultPair("https://wheel.example.net/?basin=art", true),
        new ResultPair("https://example.org/approval/bead.html", true),
        new ResultPair("https://www.example.com/books.htm", true),
        new ResultPair("http://apparel.example.net/#balance", true),
        new ResultPair("https://www.example.net/bite/bear.aspx", true),
        new ResultPair("https://aunt.example.com/", true),
        new ResultPair("http://www.example.org/#amusement", true),
        new ResultPair("https://example.com/bee", true),
        new ResultPair("http://example.com/bath?bead=action&birth=breath", true),
        new ResultPair("https://arithmetic.example.com/balance.php", true),
        new ResultPair("http://example.com/cat.aspx", true),
        new ResultPair("https://account.example.com/", true),
        new ResultPair("https://example.com/?border=believe&dog=board#boy", true),
        new ResultPair("http://www.example.org/?animal=bag", true),
        new ResultPair("https://www.example.com/bags.php", true),
        new ResultPair("http://www.example.com/brass/bubble.php", true),
        new ResultPair("https://www.example.com/#battle", true),
        new ResultPair("https://example.org/action/flower.aspx", true),
        new ResultPair("https://www.example.com/box/bags", true),
        new ResultPair("https://boat.example.com/", true),
        new ResultPair("http://bikes.example.com/mail", true),
        new ResultPair("https://www.example.com/brake/adjustment", true),
        new ResultPair("https://ants.example.com/", true),
        new ResultPair("http://example.com/brother", true),
        new ResultPair("https://www.example.net/activity/approval", true),
        new ResultPair("http://example.com/advertisement.html#boat", true),
        new ResultPair("http://aunt.example.net/", true),
        new ResultPair("https://www.example.com/#arch", true),
        new ResultPair("https://www.example.com/bells/flower?acoustics=berry&animal=apple", true),
        new ResultPair("http://bells.example.net/animal.html", true),
        new ResultPair("https://amount.example.net/bat", true),
        new ResultPair("https://www.example.com/bell.html", true),
        new ResultPair("http://www.example.com/amount/activity.php?bag=bird&dog=bag", true),
        new ResultPair("https://www.example.com/brass.aspx?birth=bit&actor=basin", true),
        new ResultPair("https://believe.example.com/bat", true),
        new ResultPair("http://www.example.com/cat", true),
        new ResultPair("http://attraction.example.com/?airport=brake&bubble=border", true),
        new ResultPair("https://example.com/#actor", true),
        new ResultPair("http://example.com/birth/flower.php", true),
        new ResultPair("https://www.example.net/bead.aspx", true),
        new ResultPair("http://account.example.com/", true),
        new ResultPair("http://www.example.com/?ball=afternoon&base=board", true),
        new ResultPair("http://achiever.example.com/brick?bell=anger&afternoon=brass", true),
        new ResultPair("http://approval.example.org/", true),
        new ResultPair("https://www.example.com/?basin=amount&afterthought=boat#amount", true),
        new ResultPair("http://www.example.com/bat.php", true),
        new ResultPair("http://example.com/?bell=berry", true),
        new ResultPair("http://example.edu/bells.html", true),
        new ResultPair("http://www.example.com/border?cat=afterthought", true),
        new ResultPair("http://example.com/alarm.php#addition", true),
        new ResultPair("https://example.com/aunt", true),
        new ResultPair("http://example.com/acoustics/advertisement", true),
        new ResultPair("https://www.example.com/ball", true),
        new ResultPair("http://www.example.com/actor/basket.php", true),
        new ResultPair("http://arm.example.edu/", true),
        new ResultPair("http://www.example.com/bit/attraction", true),
        new ResultPair("https://www.example.edu/#cat", true),
        new ResultPair("https://www.example.org/board/animal.htm?afterthought=car&believe=bee", true),
        new ResultPair("https://www.example.com/bikes", true),
        new ResultPair("https://www.example.org/beef/dog.htm", true),
        new ResultPair("http://www.example.net/approval.aspx", true),
        new ResultPair("https://www.example.com/airport.php?bubble=amount", true),
        new ResultPair("https://www.example.com/dog", true),
        new ResultPair("https://example.com/bell?achiever=back&bear=bead", true),
        new ResultPair("https://act.example.com/#baseball", true),
        new ResultPair("http://www.example.com/bagroom", true),
        new ResultPair("http://www.example.com/appliance/bells?bells=art&action=actor", true),
        new ResultPair("http://www.example.com/advice", true),
        new ResultPair("https://airplane.example.com/boot", true),
        new ResultPair("https://www.example.com/arm", true),
        new ResultPair("http://beginner.example.com/activity?birth=airport&acoustics=bat", true),
        new ResultPair("https://example.com/cat", true),
        new ResultPair("http://army.example.com/activity", true),
        new ResultPair("https://example.com/?book=flower&boot=after", true),
        new ResultPair("http://example.com/?breath=baseball", true),
        new ResultPair("https://example.com/bikes/brick?art=beginner", true),
        new ResultPair("http://example.net/?angle=bubble&bike=wheel", true),
        new ResultPair("http://www.example.edu/", true),
        new ResultPair("http://example.net/box", true),
        new ResultPair("https://army.example.com/apparatus/action.php#brother", true),
        new ResultPair("http://www.example.org/achiever.html", true),
        new ResultPair("http://brother.example.com/", true),
        new ResultPair("https://example.com/bell/bags", true),
        new ResultPair("https://www.example.org/apparatus/birth.htm", true),
        new ResultPair("http://www.example.com/bead.php", true),
        new ResultPair("https://advice.example.com/addition.php", true),
        new ResultPair("http://advertisement.example.com/?bag=art", true),
        new ResultPair("http://example.com/?birth=mail&birth=afterthought", true),
        new ResultPair("https://www.example.edu/attraction/basin", true),
        new ResultPair("https://www.example.com/?bee=flower&bags=apple", true),
        new ResultPair("https://example.org/apparatus/border", true),
        new ResultPair("https://www.example.com/?approval=achiever&bell=amount", true),
        new ResultPair("http://www.example.com/?flower=boundary&addition=believe", true),
        new ResultPair("https://www.example.com/baseball/afterthought", true),
        new ResultPair("https://border.example.com/activity/bag.php", true),
        new ResultPair("https://www.example.org/account", true),
        new ResultPair("http://www.example.com/?puppy=addition&bell=dog", true),
        new ResultPair("http://animal.example.edu/", true),
        new ResultPair("https://example.org/board.htm", true),
        new ResultPair("http://www.example.org/act.html?puppy=amount", true),
        new ResultPair("https://activity.example.com/anger.php", true),
        new ResultPair("http://www.example.net/addition", true),
        new ResultPair("https://example.com/bag.php", true),
        new ResultPair("http://www.example.com/bag", true),
        new ResultPair("http://www.example.com/brake/believe", true),
        new ResultPair("https://example.com/animal", true),
        new ResultPair("https://baseball.example.net/", true),
        new ResultPair("http://example.com/dog.html", true),
        new ResultPair("http://www.example.com/authority#boy", true),
        new ResultPair("http://bead.example.com/", true),
        new ResultPair("https://brass.example.com/badge.php", true),
        new ResultPair("http://amusement.example.edu/boy/agreement", true),
        new ResultPair("https://example.edu/cat.aspx", true),
        new ResultPair("https://www.example.edu/#bell", true),
        new ResultPair("https://example.com/agreement", true),
        new ResultPair("http://books.example.net/puppy", true),
        new ResultPair("http://www.example.net/beginner/addition", true),
        new ResultPair("http://bags.example.com/belief/zebra", true),
        new ResultPair("https://example.com/ants", true),
        new ResultPair("http://example.org/?puppy=airplane", true),
        new ResultPair("https://example.edu/?bags=bell", true),
        new ResultPair("http://www.example.net/boundary.aspx#bike", true),
        new ResultPair("http://example.net/bikes#appliance", true),
        new ResultPair("https://www.example.org/greenbean?birth=lamp&wheel=back", true),
        new ResultPair("https://bell.example.org/", true),
        new ResultPair("http://www.example.com/art/actor?bird=anger", true),
        new ResultPair("http://example.com/dog/wheel.htm", true),
        new ResultPair("http://example.com/bags/brick?appliance=bike", true),
        new ResultPair("http://ants.example.com/adjustment.php", true),
        new ResultPair("https://attraction.example.com/?lamp=apparatus&bell=aunt", true),
        new ResultPair("http://example.com/#bag", true),
        new ResultPair("https://bubble.example.edu/", true),
        new ResultPair("http://animal.example.com/", true),
        new ResultPair("http://car.example.edu/?apple=lamp&brother=after", true),
        new ResultPair("https://bag.example.com/", true),
        new ResultPair("http://www.example.net/arm#air", true),
        new ResultPair("http://books.example.com/bells?bubble=bell&beef=ball", true),
        new ResultPair("http://car.example.com/", true),
        new ResultPair("https://www.example.com/approval/addition.php#border", true),
        new ResultPair("http://board.example.org/", true),
        new ResultPair("http://example.com/?puppy=account&apparatus=baseball", true),
        new ResultPair("https://www.example.com/angle?battle=arithmetic&addition=authority#base", true),
        new ResultPair("https://www.example.com/bells", true),
        new ResultPair("https://example.com/?aunt=agreement&achiever=acoustics", true),
        new ResultPair("http://example.com/art.php", true),
        new ResultPair("http://example.edu/brick", true),
        new ResultPair("https://example.com/amusement", true),
        new ResultPair("http://account.example.com/afternoon.html", true),
        new ResultPair("http://badge.example.org/baseball", true),
        new ResultPair("http://www.example.com/box", true),
        new ResultPair("http://dog.example.com/bag/angle?bubble=after&birthday=bikes", true),
        new ResultPair("http://brass.example.com/?border=apple&bubble=back", true),
        new ResultPair("http://www.example.com/animal", true),
        new ResultPair("http://book.example.com/account#animal", true),
        new ResultPair("https://www.example.com/animal/agreement", true),
        new ResultPair("http://example.com/?apple=amusement", true),
        new ResultPair("http://example.edu/grad", true),
        new ResultPair("https://bags.example.net/", true),
        new ResultPair("http://www.example.com/ants.php?army=aunt&berry=baseball", true),
        new ResultPair("https://cat.example.edu/", true),
        new ResultPair("https://www.example.com/mail/angle", true),
        new ResultPair("http://www.example.com/?achiever=aunt", true),
        new ResultPair("http://adjustment.example.com/", true),
        new ResultPair("https://border.example.org/", true),
        new ResultPair("https://example.com/#afternoon", true),
        new ResultPair("https://www.example.com/advice/approval", true),
        new ResultPair("http://bagroom.example.com/", true),
        new ResultPair("https://example.org/aunt/base.aspx?breath=animal&bagroom=acoustics", true),
        new ResultPair("https://example.net/?apparatus=bite#aunt", true),
        new ResultPair("https://adjustment.example.com/?baseball=brick", true),
        new ResultPair("https://www.example.com/bubble/baseball", true),
        new ResultPair("http://example.com/act/airport", true),
        new ResultPair("http://www.example.com/berry/baseball.aspx?books=anger&acoustics=afternoon", true),
        new ResultPair("http://example.net/bagroom/bagroom", true),
        new ResultPair("https://www.example.com/bubble/airplane#adjustment", true),
        new ResultPair("http://www.example.org/bubble/boy", true),
        new ResultPair("http://www.example.com/agreement", true),
        new ResultPair("https://example.org/airplane", true),
        new ResultPair("https://example.net/books/approval.aspx", true),
        new ResultPair("http://www.example.com/walk/balance", true),
        new ResultPair("https://www.example.com/beginner/bubble.html?afterthought=box&zebra=base", true),
        new ResultPair("http://www.example.com/book?birds=bag", true),
        new ResultPair("https://www.example.com/appliance", true),
        new ResultPair("https://bath.example.com/boy", true),
        new ResultPair("http://amusement.example.com/#apparatus", true),
        new ResultPair("http://example.com/basketball/ants.htm?zebra=actor&brother=belief", true),
        new ResultPair("http://www.example.com/bags.htm", true),
        new ResultPair("https://example.com/?beginner=books&books=puppy", true),
        new ResultPair("http://back.example.com/balance/army.aspx", true),
        new ResultPair("://foo.example.com/bar.html", false),
        new ResultPair("//example.com/#foo", false),
        new ResultPair("https", false),
        new ResultPair("http:", false),
        new ResultPair("http://", false),
        new ResultPair("http://www", false),
        new ResultPair("http://www.", false),
        new ResultPair("http://www.example", false),
        new ResultPair("http://www.example.", false),
        new ResultPair("foo.com", false),
        new ResultPair("https://example/baseball", false),
        new ResultPair("http://foo.example.com/?bar=baz", true),
        new ResultPair("https://www.example?foo=bar&baz=biz", false),
        new ResultPair("http://www..com/", false),
        new ResultPair("http://.org/", false),
        new ResultPair("https://.com/?ants=greenbean", false),
        new ResultPair("http://example//", false),
        new ResultPair("//www..com/", false),
        new ResultPair(".com", false),
        new ResultPair("art.aspx?book=ball", false),
        new ResultPair("wwwwwww", false),
        new ResultPair("https://www.example.com.com", true),
        new ResultPair("https://www.example.com/.com", true),
        new ResultPair("https://www.example.php", false),
        new ResultPair("foo.php?bar=baz&bar=baz", false),
        new ResultPair("htp://attraction.example.com/approval?birthday=birthday", false),
        new ResultPair("https://www.example.#adjustment", false),
        new ResultPair("https:///belief", false),
        new ResultPair("//example.com/?battle=appliance", false),
        new ResultPair("apple.aspx", false),
        new ResultPair("/", false),
        new ResultPair(".html", false),
        new ResultPair("https.html", false)
    };



    private static final String[] all_valid_schemes = {
            "aaa",
            "aaas",
            "about",
            "acap",
            "acct",
            "acd",
            "acr",
            "adiumxtra",
            "afp",
            "afs",
            "aim",
            "amss",
            "android",
            "appdata",
            "apt",
            "attachment",
            "aw",
            "barion",
            "beshare",
            "bitcoin",
            "bitcoincash",
            "blob",
            "bolo",
            "browserext",
            "calculator",
            "callto",
            "cap",
            "cast",
            "casts",
            "chrome",
            "chrome-extension",
            "cid",
            "coap",
            "coap+tcp",
            "coap+ws",
            "coaps",
            "coaps+tcp",
            "coaps+ws",
            "com-eventbrite-attendee",
            "content",
            "conti",
            "crid",
            "cvs",
            "dab",
            "data",
            "dav",
            "diaspora",
            "dict",
            "did",
            "dis",
            "dlna-playcontainer",
            "dlna-playsingle",
            "dns",
            "dntp",
            "dpp",
            "drm",
            "drop",
            "dtn",
            "dvb",
            "ed2k",
            "elsi",
            "example",
            "facetime",
            "fax",
            "feed",
            "feedready",
            "file",
            "filesystem",
            "finger",
            "first-run-pen-experience",
            "fish",
            "fm",
            "ftp",
            "fuchsia-pkg",
            "geo",
            "gg",
            "git",
            "gizmoproject",
            "go",
            "gopher",
            "graph",
            "gtalk",
            "h323",
            "ham",
            "hcap",
            "hcp",
            "http",
            "https",
            "hxxp",
            "hxxps",
            "hydrazone",
            "iax",
            "icap",
            "icon",
            "im",
            "imap",
            "info",
            "iotdisco",
            "ipn",
            "ipp",
            "ipps",
            "irc",
            "irc6",
            "ircs",
            "iris",
            "iris.beep",
            "iris.lwz",
            "iris.xpc",
            "iris.xpcs",
            "isostore",
            "itms",
            "jabber",
            "jar",
            "jms",
            "keyparc",
            "lastfm",
            "ldap",
            "ldaps",
            "leaptofrogans",
            "lorawan",
            "lvlt",
            "magnet",
            "mailserver",
            "mailto",
            "maps",
            "market",
            "message",
            "microsoft.windows.camera",
            "microsoft.windows.camera.multipicker",
            "microsoft.windows.camera.picker",
            "mid",
            "mms",
            "modem",
            "mongodb",
            "moz",
            "ms-access",
            "ms-browser-extension",
            "ms-calculator",
            "ms-drive-to",
            "ms-enrollment",
            "ms-excel",
            "ms-eyecontrolspeech",
            "ms-gamebarservices",
            "ms-gamingoverlay",
            "ms-getoffice",
            "ms-help",
            "ms-infopath",
            "ms-inputapp",
            "ms-lockscreencomponent-config",
            "ms-media-stream-id",
            "ms-mixedrealitycapture",
            "ms-mobileplans",
            "ms-officeapp",
            "ms-people",
            "ms-project",
            "ms-powerpoint",
            "ms-publisher",
            "ms-restoretabcompanion",
            "ms-screenclip",
            "ms-screensketch",
            "ms-search",
            "ms-search-repair",
            "ms-secondary-screen-controller",
            "ms-secondary-screen-setup",
            "ms-settings",
            "ms-settings-airplanemode",
            "ms-settings-bluetooth",
            "ms-settings-camera",
            "ms-settings-cellular",
            "ms-settings-cloudstorage",
            "ms-settings-connectabledevices",
            "ms-settings-displays-topology",
            "ms-settings-emailandaccounts",
            "ms-settings-language",
            "ms-settings-location",
            "ms-settings-lock",
            "ms-settings-nfctransactions",
            "ms-settings-notifications",
            "ms-settings-power",
            "ms-settings-privacy",
            "ms-settings-proximity",
            "ms-settings-screenrotation",
            "ms-settings-wifi",
            "ms-settings-workplace",
            "ms-spd",
            "ms-sttoverlay",
            "ms-transit-to",
            "ms-useractivityset",
            "ms-virtualtouchpad",
            "ms-visio",
            "ms-walk-to",
            "ms-whiteboard",
            "ms-whiteboard-cmd",
            "ms-word",
            "msnim",
            "msrp",
            "msrps",
            "mss",
            "mtqp",
            "mumble",
            "mupdate",
            "mvn",
            "news",
            "nfs",
            "ni",
            "nih",
            "nntp",
            "notes",
            "ocf",
            "oid",
            "onenote",
            "onenote-cmd",
            "opaquelocktoken",
            "openpgp4fpr",
            "pack",
            "palm",
            "paparazzi",
            "payto",
            "pkcs11",
            "platform",
            "pop",
            "pres",
            "prospero",
            "proxy",
            "pwid",
            "psyc",
            "qb",
            "query",
            "redis",
            "rediss",
            "reload",
            "res",
            "resource",
            "rmi",
            "rsync",
            "rtmfp",
            "rtmp",
            "rtsp",
            "rtsps",
            "rtspu",
            "secondlife",
            "service",
            "session",
            "sftp",
            "sgn",
            "shttp",
            "sieve",
            "simpleledger",
            "sip",
            "sips",
            "skype",
            "smb",
            "sms",
            "smtp",
            "snews",
            "snmp",
            "soap.beep",
            "soap.beeps",
            "soldat",
            "spiffe",
            "spotify",
            "ssh",
            "steam",
            "stun",
            "stuns",
            "submit",
            "svn",
            "tag",
            "teamspeak",
            "tel",
            "teliaeid",
            "telnet",
            "tftp",
            "things",
            "thismessage",
            "tip",
            "tn3270",
            "tool",
            "turn",
            "turns",
            "tv",
            "udp",
            "unreal",
            "urn",
            "ut2004",
            "v-event",
            "vemmi",
            "ventrilo",
            "videotex",
            "vnc",
            "view-source",
            "wais",
            "webcal",
            "wpid",
            "ws",
            "wss",
            "wtai",
            "wyciwyg",
            "xcon",
            "xcon-userid",
            "xfire",
            "xmlrpc.beep",
            "xmlrpc.beeps",
            "xmpp",
            "xri",
            "ymsgr",
            "z39.50",
            "z39.50r",
            "z39.50s"
    };

}
