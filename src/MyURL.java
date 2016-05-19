/**
 * Represents a URL
 */
public class MyURL {

    private String scheme = "http";
    private String domainName = null;
    private int port = 80;
    private String path = "/";

    /**
     * Split {@code url} into the various components of a URL
     *
     * @param url the {@code String} to parse
     */
    public MyURL(String url) throws RuntimeException{

        // TODO:  Split the url into scheme, domainName, port, and path.
        // Only the domainName is required.  Default values given above.
        // See the test file for examples of correct and incorrect behavior.
        // Hints:  (1) My implementation is mostly calls to String.indexOf and String.substring.
        // (2) indexOf can take a String as a parameter (it need not be a single character).
        /**
         * REMOVE SCHEME
         */
        if(url.length() <= 4){
            throw new RuntimeException("NO DOMAIN");
        }

        int startURL = url.indexOf(":");
        if (startURL != -1 || (startURL < 10&&startURL>0)) {
            if (url.substring(0, startURL).equals("ssh") || url.substring(0,
                    startURL).equals("ftp") || url.substring(0, startURL).equals("http")
                    || url.substring(0, startURL).equals("https")
                    || url.substring(0, startURL).equals("unusual")) {
                scheme = url.substring(0, startURL);
                url = url.substring(startURL + 3);
            }

            if(url.charAt(0)==':'){
                throw new RuntimeException("NO DOMAIN");
            }
        }

        /**
         * FIND DOMAIN
         */
        int endDomain = url.indexOf("/");
        if(endDomain != -1) {
            domainName = url.substring(0, endDomain);
            if (domainName.contains(":")) {
                port = Integer.parseInt(domainName.substring(domainName.indexOf(':') + 1));
                domainName = domainName.substring(0, domainName.indexOf(':'));
            }
            path = url.substring(endDomain);
        }else if (url.contains(":")) {
            port = Integer.parseInt(url.substring(url.indexOf(':') + 1));
            domainName = url.substring(0, url.indexOf(':'));
        }else{
            domainName = url;
        }

    }

    /**
     * If {@code newURL} has a scheme (e.g., begins with "http://", "ftp://", etc), then parse {@code newURL}
     * and ignore {@code currentURL}.  If {@code newURL} does not have a scheme, then assume it is intended
     * to be a relative link and replace the file component of {@code currentURL}'s path with {@code newURL}.
     *
     * @param newURL     a {@code String} representing the new URL.
     * @param currentURL the current URL
     */
    public MyURL(String newURL, MyURL currentURL) {

        // TODO: If newURL has a scheme, then take the same action as the other constructor.
        // If newURL does not have a scheme
        // (1) Make a copy of currentURL
        // (2) Replace the filename (i.e., the last segment of the path) with the relative link.
        // See the test file for examples of correct and incorrect behavior.
        // Hint:  Consider using String.lastIndexOf
        /**
         * REMOVE SCHEME IF IT EXISTS
         */
        int startURL = newURL.indexOf(":");
        if (startURL != -1 || (startURL < 10&&startURL>0)) {
            if (newURL.substring(0, startURL).equals("ssh") || newURL.substring(0,
                    startURL).equals("ftp") || newURL.substring(0, startURL).equals("http")
                    || newURL.substring(0, startURL).equals("https")
                    || newURL.substring(0, startURL).equals("unusual")) {
                scheme = newURL.substring(0, startURL);
                newURL = newURL.substring(startURL + 3);


            }

            /**
             * FIND DOMAIN(SAME FIRST CONSTRUCTOR)
             */
            int endDomain = newURL.indexOf("/");
            if(endDomain != -1) {
                domainName = newURL.substring(0, endDomain);
                if (domainName.contains(":")) {
                    port = Integer.parseInt(domainName.substring(domainName.indexOf(':') + 1));
                    domainName = domainName.substring(0, domainName.indexOf(':'));
                }
                path = newURL.substring(endDomain);
            }else {
                domainName = newURL;
            }
        } else {
            /**
             * ASSUME NEWURL IS A RELATIVE FILE PATH TO CURRENTURL
             */
            scheme = currentURL.scheme();
            domainName = currentURL.domainName();
            port = currentURL.port();
            int oldFileLocation = currentURL.path().lastIndexOf('/');
            String oldURL = currentURL.path().substring(0, oldFileLocation+1);
            path = oldURL + newURL;


        }


    }


    public String scheme() {
        return scheme;
    }

    public String domainName() {
        return domainName;
    }

    public int port() {
        return port;
    }

    public String path() {
        return path;
    }

    /**
     * Format this URL as a {@code String}
     *
     * @return this URL formatted as a string.
     */
    public String toString() {
        // TODO:  Format this URL as a string
        return scheme+"://"+domainName+":"+port+path;
    }

    // Needed in order to use MyURL as a key to a HashMap
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    // Needed in order to use MyURL as a key to a HashMap
    @Override
    public boolean equals(Object other) {
        if (other instanceof MyURL) {
            MyURL otherURL = (MyURL) other;
            return this.scheme.equals(otherURL.scheme) &&
                    this.domainName.equals(otherURL.domainName) &&
                    this.port == otherURL.port() &&
                    this.path.equals(otherURL.path);
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        MyURL url = new MyURL("http://:98");
        MyURL url1 = new MyURL("fred/barney/wilma.html", url);
    }
}