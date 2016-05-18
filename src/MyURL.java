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
    public MyURL(String url) {

        // TODO:  Split the url into scheme, domainName, port, and path.
        // Only the domainName is required.  Default values given above.
        // See the test file for examples of correct and incorrect behavior.
        // Hints:  (1) My implementation is mostly calls to String.indexOf and String.substring.
        // (2) indexOf can take a String as a parameter (it need not be a single character).

        /**
         * REMOVE SCHEME
         */
        int startURL = url.indexOf(":");
        if(url.substring(0, startURL).equals("ssh") || url.substring(0,
                startURL).equals("ftp")|| url.substring(0,startURL).equals("html")
                || url.substring(0, startURL).equals("unusual")) {
            url = url.substring(startURL+3);
        }

        /**
         * FIND DOMAIN
         */
        int endDomain = url.indexOf('/');
        System.out.println(endDomain);
        domainName = url.substring(0, endDomain);
        if(domainName.contains(":")){
            port = Integer.parseInt(domainName.substring(domainName.indexOf(':')));
        }
        path = url.substring(endDomain);

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
        int endDomain = newURL.indexOf(":");
        int lastFile = newURL.lastIndexOf("/");
        String addFile = newURL.substring(lastFile);

        int currentFile = currentURL.path.lastIndexOf("/");
        currentURL.path = currentURL.path.substring(0, currentFile);
        currentURL.path = currentURL.path+addFile;


        domainName = newURL.substring(0, endDomain);
        MyURL copy = currentURL;



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
        return String.format("");
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

    public static void main(String[] args){
        MyURL test = new MyURL("http://www.google.com/place1/place2/george.html");
    }
}