import junit.framework.TestCase;

public class basicUrlValidatorTests extends TestCase {


    private final boolean printStatus = false;
    private final boolean printIndex = false;//print index that indicates current scheme,host,port,path, query test were using.

    public basicUrlValidatorTests(String testName) {
        super(testName);
    }

    public void testInstantiateUrlValidator(){
        UrlValidator validate = new UrlValidator();
        assertNotNull(validate);
    }
}