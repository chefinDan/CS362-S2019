import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.awt.desktop.SystemSleepEvent;
import java.lang.reflect.Array;


/*
*   This class is responsible for generatig random URL components. It follows the input boundries described in the
*   project documentation.
 */

public class randomUrlCompGenerator {

    public randomUrlCompGenerator(){

    }

//    Returns a psuedo-random port
    public String port(){

        boolean colonAdded = false;
        StringBuilder port = new StringBuilder();
        int length = (int)(Math.random() *12);

        for(int k = 0; k < length; ++k ) {
            //                    System.out.println("First")
            if (1 == (int) (Math.random() * 100 % 2)) {
                if (!colonAdded) {
                    port.append(':');
                    colonAdded = true;
                }
            } else {

                int number = (int) (Math.random() * 9);
                port.append(number);
            }
        }

        return port.toString();
    }

    public static void main(String[] args){
        randomUrlCompGenerator rand = new randomUrlCompGenerator();
        String randPort = rand.port();
        System.out.println(randPort);
    }
}
