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

//    Returns a pseudo-random port
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

//    create VERY random query, but with legal format
    public String query(){

        boolean questionMarkAdded = false;
        StringBuilder query = new StringBuilder();
        int pairs = (int)(Math.random() * 5);
        String[] query_chars = {"_", "-", "%20", "+", "*", "."};

//
            for(int p = 0; p < pairs; ++p){

                StringBuilder key = new StringBuilder();
                StringBuilder value = new StringBuilder();
                StringBuilder pair = new StringBuilder();

                String randchar = query_chars[(int)(Math.random()*1000 %query_chars.length)];
                key.append( RandomStringUtils.randomAlphanumeric( (int)(Math.random()*100 %6 +1) ) );
                key.append(randchar);
                value.append( RandomStringUtils.randomAlphabetic( (int)(Math.random()*100 %6 +1) ) );
                value.append(randchar);

                if(!questionMarkAdded){
                    query.append('?');
                    questionMarkAdded = true;
                }

                pair.append(key);

                pair.append('=');

                pair.append(value);
                query.append(pair);

                if(p+1 != pairs){
                    query.append('&');
                }
            }

        return query.toString();
    }

    public static void main(String[] args){
        randomUrlCompGenerator rand = new randomUrlCompGenerator();
        String query = rand.query();
        System.out.println(query);
    }
}
