import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;


/*
*   This class is responsible for generatig random URL components. It follows the input boundries described in the
*   project documentation.
 */

public class RandomUrlCompGenerator {


    private static String[] valid_schemes;
    private static String[] scheme_chars = {".",":","/"};
    private static String[] hostname_chars = {"."};
    private static String[] query_chars = {"_", "-", "%20", "+", "*", "."};

    public RandomUrlCompGenerator(){
        RandomUrlValidator randUrlVal = new RandomUrlValidator();
        valid_schemes = randUrlVal.getSchemes();
    }

    /**
     *
     * @return a 50/50 true false split
     */
    private boolean rand5050(){
        Random rand = new Random();
        return rand.nextBoolean();
//        if(1 == (int)Math.random()*100 %2);
    }

    private boolean randBoolN(int n) {
        return (n == (int) (Math.random() * n));
    }

    private int randNum(int lower, int upper){
        return (int)(Math.random()*upper + lower);
    }

    /**
     *
     * @return Returns a pseudo-random port
     */
    public String port(){

        boolean colonAdded = false;
        StringBuilder port = new StringBuilder();
        int length = (int)(Math.random() *12);

        for(int k = 0; k < length; ++k ) {
            if (rand5050()){
                if (!colonAdded) {
                    port.append(':');
                    colonAdded = true;
                }
            }
            else if(randBoolN(2)) {
                port.append(RandomStringUtils.randomAlphabetic(1,6));
            }
            else if(randBoolN(2)){
                port.append(RandomStringUtils.randomAlphanumeric(1, 12));
            }
            else if(randBoolN(2)){
                int number = (int) (Math.random() * 30);
                port.append(number);
            }
            else{
                int number = (int) (Math.random() * 9);
                port.append(number);
            }
        }

        return port.toString();
    }

    /**
     *
     * @return a VERY random query, but with legal format
     */
    public String query(){

        boolean questionMarkAdded = false;
        StringBuilder query = new StringBuilder();
        int pairs = (int)(Math.random() * 6);

            for(int p = 0; p < pairs; ++p){

                StringBuilder key = new StringBuilder();
                StringBuilder value = new StringBuilder();
                StringBuilder pair = new StringBuilder();

                String randchar = query_chars[randNum(0, query_chars.length)];

                key.append( RandomStringUtils.randomAlphanumeric(1, 6) );
                key.append(randchar);

                value.append( RandomStringUtils.randomAlphabetic( 1, 6));
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

    /**
     *
     * @return a psuedo random scheme, about half will be from RandomUrlValidator.schemes
     */
    public String scheme() {
        StringBuilder random_scheme = new StringBuilder();

        if (rand5050()) {
            random_scheme.append(valid_schemes[randNum(0, valid_schemes.length)]);
        }
        else {
            random_scheme.append( RandomStringUtils.randomAlphanumeric(0, 3));
            random_scheme.append( scheme_chars[randNum(0, scheme_chars.length)]);
            random_scheme.append( RandomStringUtils.randomAlphanumeric(0, 3));

            if (randBoolN(3)){
                random_scheme.append(RandomStringUtils.randomAlphanumeric(36));
            }

            if (randBoolN(6)){
                random_scheme.append(":/");
            }
            else if (randBoolN(6)){
                random_scheme.append("/:");
            }
            else if (randBoolN(6)){
                random_scheme.append("://");
            }
        }

        return random_scheme.toString();
    }


    private String hostname() {
        StringBuilder hostname = new StringBuilder();

        if(randBoolN(4)){
            hostname.append("www.");
        }

        if (rand5050()) {
            hostname.append(RandomStringUtils.randomAlphanumeric(1, 3));
        }

        if(randBoolN(4)){
            hostname.append(hostname_chars[randNum(0, hostname_chars.length)]);
        }

        if (rand5050()){
            hostname.append(RandomStringUtils.randomAlphanumeric(1, 6));
        }

        if(randBoolN(6)){
            hostname.append(hostname_chars[randNum(0, hostname_chars.length)]);
        }

        if(rand5050()){
            hostname.append(RandomStringUtils.randomAlphanumeric(1, 6));
        }
        return hostname.toString();
    }

    private String tld(){
        StringBuilder tld = new StringBuilder();
        tld.append(RandomStringUtils.randomAlphabetic(2, 6));
        return tld.toString().toLowerCase();
    }
    
    public String authority(){
        String hostname = hostname();
        return hostname;
    }


    public static void main(String[] args){
        RandomUrlCompGenerator rand = new RandomUrlCompGenerator();
        RandomUrlValidator randValid = new RandomUrlValidator();
        int valid = 0, invalid = 0;

        for (int i = 0; i < 100; ++i){
            String port = rand.port();
            if(randValid.isValidPort(port)){
                valid++;
            }
            else{
                invalid++;
            }
        }

        System.out.println(valid);
        System.out.println(invalid);

    }
}
