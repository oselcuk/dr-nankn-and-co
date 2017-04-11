/**
 * Created by William Simon (wsimon7) on 4/10/2017.
 *
 * JUnit test class to verify the password is valid
 **/

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.DrNankn.cleanwater.Activities.LoginActivity;

public class PasswordTest {

    private static final String testString1 = "a1c";
    private static final String testString2 = "ac";
    private static final String testString3 = "0000";
    private static final String testString4 = "    ";
    private static final String testString5 = "";
    private static final String testString6 = "aGoOdPaSsWoRd142!";
    private static final String testString7 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789~!@#$%^&*(){}|[]:;<>,.?/";

    LoginActivity loginActivity;


    @Before
    public void setUp() {
        loginActivity = new LoginActivity();
    }

    /*
     * Test to determine if a password is valid or not
     */
    @Test
    public void isValidPassword() {

        /* Test 1 */
        assertEquals(false, loginActivity.validatePassString(testString1));
        /* Test 2 */
        assertEquals(false, loginActivity.validatePassString(testString2));
        /* Test 3 */
        assertEquals(true, loginActivity.validatePassString(testString3));
        /* Test 4 */
        assertEquals(true, loginActivity.validatePassString(testString4));
        /* Test 5 */
        assertEquals(false, loginActivity.validatePassString(testString5));
        /* Test 6 */
        assertEquals(true, loginActivity.validatePassString(testString6));
        /* Test 7 */
        assertEquals(true, loginActivity.validatePassString(testString7));
    }
}
