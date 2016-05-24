
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by kurmasz on 1/20/15.
 */
public class Perl {


    /**
     * Place {@code query} in the environment and run a perl script {@code filename}
     * @param filename the file to run
     * @param query the query string
     * @return the text output of the perl process.
     */
    public static String runPerl(String filename, String query) {

        // place to store the output
        StringBuffer lines = new StringBuffer();

        try {
            ProcessBuilder pb = new ProcessBuilder("perl", filename);
            Map<String, String> env = pb.environment();
            if (query != null) {
                env.put("QUERY_STRING", query);
            }

            Process p = pb.start();

            // Grab each line generated and place it in a List
            Scanner input = new Scanner(p.getInputStream());
            while (input.hasNext()) {
                lines.append(input.nextLine());
                lines.append('\n');
            }

            Scanner err = new Scanner(p.getErrorStream());
            while (err.hasNext()) {
                System.err.println(err.nextLine());
            }
        } catch (IOException e) {
            System.err.println("There was a problem: " + e);
            e.printStackTrace(System.err);
        }
        return lines.toString();
    }
}