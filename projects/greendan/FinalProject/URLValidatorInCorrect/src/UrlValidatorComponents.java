import java.util.regex.Pattern;

public class UrlValidatorComponents {
    // regex patterns
    private static final String PATH_REGEX = "^(/[-\\w:@&?=+,.!/~*'%$_;()]*)?$";
    private static final Pattern PATH_PATTERN = Pattern.compile(PATH_REGEX);
    private static final String PATH_REGEX_PARENT = "^((?!\\.\\.).)*$";
    private static final Pattern PATH_PATTERN_PARENT = Pattern.compile(PATH_REGEX_PARENT);

    // flags
    private boolean ALLOW_DOUBLE_SLASHES = false;


    /**
     * Constructor
     */
    public UrlValidatorComponents() {
        this.ALLOW_DOUBLE_SLASHES = false;
    }

    public UrlValidatorComponents(boolean ALLOW_DOUBLE_SLASHES) {
        this.ALLOW_DOUBLE_SLASHES = ALLOW_DOUBLE_SLASHES;
    }


    /**
     * Returns the number if instances of a specified element within a target element.
     * @param needle - the element being searched and counted
     * @param haystack - the text to search within
     * @return int - the number of times the needle is present in the haystack.
     */
    private int instancesOf(String needle, String haystack) {
        int currentIndex = 0;
        int instances = 0;

        while (currentIndex != -1) {
            currentIndex = haystack.indexOf(needle, currentIndex);
            if (currentIndex > -1) {
                currentIndex++;
                instances++;
            }
        }

        return instances;
    }


    /**
     * Checks if a string represents a valid URL path.
     * @param path - the path to inspect.
     * @return boolean - true if the path represents a valid URL path,
     *  false if not.
     */
    public boolean isValidPath(String path) {
        // a null path is invalid.
        if (path == null) {
            return false;
        }

        // if it doesn't match the defined PATH_REGEX, then it's invalid.
        if (!PATH_PATTERN.matcher(path).matches()) {
            return false;
        }

        // if it tries to access to use a parent reference, it's invalid.
        if (!PATH_PATTERN_PARENT.matcher(path).matches()) {
            return false;
        }

        // if there are double slashes that aren't allowed, it's invalid.
        if (!ALLOW_DOUBLE_SLASHES && instancesOf("//", path) > 0) {
            return false;
        }

        // if all validation checks pass, it's valid.
        return true;
    }


}
