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

        /*
        *   Generate the random data needed for the random testing
         */

    }

    public void testIsValid() {
//        testIsValid(testUrlParts, UrlValidator.ALLOW_ALL_SCHEMES);
        setUp();

        long options = UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.NO_FRAGMENTS;
//        testIsValid(testUrlPartsOptions, options);

        generateRandomData(50);
        randomTestIsValid(randomUrlTestParts, options);

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


    private static boolean incrementTestPartsIndex(int[] testPartsIndex, Object[] testParts) {
        boolean carry = true;  //add 1 to lowest order part.
        boolean maxIndex = true;

        // TODO: write bug report for the bug that has been fixed in the commented-out line below.
        // for (int testPartsIndexIndex = testPartsIndex.length; testPartsIndexIndex >= 0; --testPartsIndexIndex) {
        for (int testPartsIndexIndex = testPartsIndex.length - 1; testPartsIndexIndex >= 0; --testPartsIndexIndex) {
            int index = testPartsIndex[testPartsIndexIndex];
            ResultPair[] part = (ResultPair[]) testParts[testPartsIndexIndex];
//            ResultPair[] part = (ResultPair[]) testParts[testPartsIndexIndex];
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







    //-------------------- Random Test data for creating a random composite URL

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
        new ResultPair("ftp://", true),
        new ResultPair("h3t://", true),
        new ResultPair("3ht://", false),
        new ResultPair("http:/", false),
        new ResultPair("http:", false),
        new ResultPair("http/", false),
        new ResultPair("://", false)};

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

    //Test allow2slash, noFragment
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

    private ResultPair[] testUrlQuery = {
        new ResultPair("?action=view", true),
        new ResultPair("?action=edit&mode=up", true),
        new ResultPair("", true)
    };

    private Object[] testUrlParts = {
        testUrlScheme,
        testUrlAuthority,
        testUrlPort,
        testPath,
        testUrlQuery
    };

    private Object[] testUrlPartsOptions = {
        testUrlScheme,
        testUrlAuthority,
        testUrlPort,
        testUrlPathOptions,
        testUrlQuery
    };

    private int[] testPartsIndex = {0, 0, 0, 0, 0};

    //---------------- Test data for individual url parts ----------------
    private final String[] schemes = {"http", "gopher", "g0-To+.", "not_valid"};

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
