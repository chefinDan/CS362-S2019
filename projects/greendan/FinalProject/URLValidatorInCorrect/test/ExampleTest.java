import junit.framework.TestCase;

public class ExampleTest extends TestCase {
    public void testIsTrue() {
        assertTrue(true);
    }

    public void testIsFalse() {
        assertFalse(false);
    }

    public void testFail() {
        assertTrue(false);
    }

}
