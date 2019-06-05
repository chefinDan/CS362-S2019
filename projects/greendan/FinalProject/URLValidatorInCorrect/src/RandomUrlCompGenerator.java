import org.apache.commons.lang3.RandomStringUtils;


/*
*   This class is responsible for generatig random URL components. It follows the input boundries described in the
*   project documentation.
 */

public class RandomUrlCompGenerator {


    private static String[] valid_schemes;

    public RandomUrlCompGenerator(){
        RandomUrlValidator randUrlVal = new RandomUrlValidator();
        valid_schemes = randUrlVal.getSchemes();
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

    /**
     *
     * @return a VERY random query, but with legal format
     */
    public String query(){

        boolean questionMarkAdded = false;
        StringBuilder query = new StringBuilder();
        int pairs = (int)(Math.random() * 5);
        String[] query_chars = {"_", "-", "%20", "+", "*", "."};

            for(int p = 0; p < pairs; ++p){

                StringBuilder key = new StringBuilder();
                StringBuilder value = new StringBuilder();
                StringBuilder pair = new StringBuilder();

                String randchar = query_chars[(int)(Math.random()*100 %query_chars.length)];
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

    /**
     *
     * @return a psuedo random scheme, about half will be from RandomUrlValidator.schemes
     */
    public String scheme() {

        StringBuilder random_scheme = new StringBuilder();
        String[] scheme_chars = {".",":","/"};

        if (1 == (int) (Math.random() * 100 % 2)) {
            random_scheme.append(valid_schemes[(int) (Math.random() * 100 % valid_schemes.length)]);
//            random_scheme.append("://");
        } else {

            random_scheme.append(RandomStringUtils.randomAlphanumeric((int) (Math.random() * 100 % 3)));
            random_scheme.append(scheme_chars[(int)(Math.random()*100 %scheme_chars.length)]);
            random_scheme.append(RandomStringUtils.randomAlphanumeric((int) (Math.random() * 100 % 3)));

            if (1 == (int) (Math.random() * 100 % 3)){
                random_scheme.append(RandomStringUtils.randomAlphanumeric(36));
            }

            if (1 == (int) (Math.random() * 100 % 6)){
                random_scheme.append(":/");
            }
            else if (1 == (int) (Math.random() * 100 % 6)){
                random_scheme.append("/:");
            }
            else if (1 == (int) (Math.random() * 100 % 6)){
                random_scheme.append("://");
            }
        }

        return random_scheme.toString();
    }


    public static void main(String[] args){
        RandomUrlCompGenerator rand = new RandomUrlCompGenerator();
        int good = 0, bad = 0;

        for(int i = 0; i < 10000; i++){
            String scheme = rand.scheme();
            RandomUrlValidator randValidate = new RandomUrlValidator();
            if (randValidate.isValidScheme(scheme)){
//                System.out.printf("%s: Good\n", scheme);
                good++;
            }
            else{
//                System.out.printf("%s: Bad\n", scheme);
                bad++;
            }
        }

    }
}
