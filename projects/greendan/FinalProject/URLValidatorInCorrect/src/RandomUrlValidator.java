import java.util.Arrays;
import java.util.regex.Pattern;

public class RandomUrlValidator {
    //
    // regex patterns
    //

    // path regex
    private static final String PATH_REGEX = "^(/[-\\w:@&?=+,.!/~*'%$_;()]*)?$";
    private static final Pattern PATH_PATTERN = Pattern.compile(PATH_REGEX);

    // path parent regex
    private static final String PATH_PARENT_REGEX = "^((?!\\.\\.).)*$";
    private static final Pattern PATH_PARENT_PATTERN = Pattern.compile(PATH_PARENT_REGEX);

    // query regex
    private static final String QUERY_REGEX = "^\\?(\\w+(=[\\w.-]*)?(&\\w+(=[\\w.-]*)?)*)?$";
    private static final Pattern QUERY_PATTERN = Pattern.compile(QUERY_REGEX);


    //
    // flags
    //
    private boolean ALLOW_DOUBLE_SLASHES = false;


    //
    // finite lists
    //
    private static final String[] schemes = {
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


    /**
     * Constructor
     */
    public RandomUrlValidator() {
        this.ALLOW_DOUBLE_SLASHES = false;
    }

    public RandomUrlValidator(boolean ALLOW_DOUBLE_SLASHES) {
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
        if (!PATH_PARENT_PATTERN.matcher(path).matches()) {
            return false;
        }

        // if there are double slashes that aren't allowed, it's invalid.
        if (!ALLOW_DOUBLE_SLASHES && instancesOf("//", path) > 0) {
            return false;
        }

        // if all validation checks pass, it's valid.
        return true;
    }



    public boolean isValidQuery(String query) {
        // a null or empty string is considered a valid query
        if (query == null || query.equals("")) {
            return true;
        }

        // otherwise, the query is valid if it matches the
        // specified query regular expression pattern
        return QUERY_PATTERN.matcher(query).matches();
    }


    /**
     * Checks if a given String represents a valid scheme
     * @param scheme - the scheme to inspect
     * @return boolean - true if the scheme is valid, false if not.
     */
    public boolean isValidScheme(String scheme) {
        // NOTE: binary search will only work as long as the
        // schemes[] array remains sorted.

        // URL schemes are case-insensitive
        scheme = scheme.toLowerCase();

        // if the scheme is in the list of valid schemes, it's valid.
        return Arrays.binarySearch(schemes, scheme) > -1;
    }
}
