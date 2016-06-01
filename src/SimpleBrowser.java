import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * This class can serve as starter code for a simple web browser.
 * It provides a basic GUI setup:  and address bar, and a scrollable panel on which to draw.
 * It loads text from a local file and uses {@link StarterDisplay} to render it. 
 * <p/>
 * Created by kurmasz on 12/17/14.
 */
public class SimpleBrowser {


  private JFrame frame;
  protected JTextField addressBar;
  private JScrollPane scrollPane;
  private StarterDisplay display;
  private String homeLoc;

  // Caching images prevents the browser from repeatedly fetching the same image from the server
  // (This repeated fetching is especially annoying when scrolling.)
  protected ImageCache cache = new ImageCache();

  // The URL of the currently displayed document;
  protected MyURL currentURL = null;

  protected SimpleBrowser(String frameName, String initialLocation, JPanel displayPanel) {
    homeLoc = initialLocation;
    
    frame = new JFrame(frameName);
    frame.setSize(500, 500);
    addressBar = new JTextField(initialLocation);

    JPanel barPanel = new JPanel();
    barPanel.setLayout(new BorderLayout());
    JButton home = new JButton("Home");
    barPanel.add(home, BorderLayout.WEST);
    barPanel.add(addressBar, BorderLayout.CENTER);
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenSize.width /= 2;
    screenSize.height /= 2;

    displayPanel.setPreferredSize(screenSize);
    scrollPane = new JScrollPane(displayPanel);


    frame.getContentPane().add(barPanel, BorderLayout.NORTH);
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();

    // Respond to the user pressing <enter> in the address bar.
    addressBar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String textInBar = addressBar.getText();

        // Replace this with the code that loads
        // text from a web server
        loadPage(textInBar);
      }
    });
    
    home.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loadPage(homeLoc);
      }
    });
    

    displayPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        clicked(e.getPoint());
      }
    });
  }

  public SimpleBrowser(String frameName, String initialLocation, StarterDisplay display_in) {
    this(frameName, initialLocation, (JPanel) display_in);
    display = display_in;
  }

  protected void clicked(Point point) {
    // Respond to a mouse click in the display
    // TODO:  Override/replace this method when you add support for links.
    Color c = display.getColor(point);
    if (c != null) {
      display.setColor(c);
      display.repaint();
    }
  }

  protected void loadPage(String textInBar) {
    // TODO:  Replace this method with a method that loads text from a URL instead of a file.
    // This code here is just so that the simple browser will do something until you get the 
    // networking part working.
    
    File file = new File(textInBar);
    List<String> contents = null;
    try {
      currentURL = new MyURL(textInBar);

      // WARNING!! This code is missing a lot of important
      // checks ("does the file exist", "is it a text file", "is it readable", etc.)
      contents = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    } catch (IOException e) {
      System.out.println("Can't open file " + file);
      e.printStackTrace();
    }
    display.setText(contents);
    frame.repaint();
  }

  // Fetch an image from from the server, or return null if 
  // the image isn't available.
  protected Image fetchImage(MyURL url) {
    // TODO:  implement me.
    // Hint:  Use a new WebTransactionClient object.
    return null;
  }

  /**
   * Return the image at the given url.
   *
   * @param urlString the URL of the image to load.
   * @return The desired image, or {@code null} if the image isn't available.
   */
  public Image getCachedImage(String urlString) {
    MyURL url = new MyURL(urlString, currentURL);

    // This unusual syntax (the "new ImageCache.ImageLoader" stuff) is an "anonymous inner class.  It is Java's way
    // of allowing us to pass the fetchImage method as a parameter to the ImageCache.getImage.  You may have seen this 
    // syntax before with ActionListeners.  If not, I will be happy to explain it to you.
    return cache.getImage(url, new ImageCache.ImageLoader() {
      @Override
      public Image loadImage(MyURL url) {
        return fetchImage(url);
      }
    });
  }


  public static void main(String[] args) {

    // Notice that the display object (the StarterDisplay) is created *outside* of the 
    // SimpleBrowser object.  This is an example of "dependency injection" (also called 
    // "inversion of control").  In general, dependency injection simplifies unit testing.
    // I this case, I used dependency injection so that I could more easily write a subclass
    // of this browser that uses a completely different display class.
    String initial = args.length > 0 ? args[0]  : "sampleInput/starterSample.txt";
    new SimpleBrowser("CIS 371 Starter Browser", initial, new StarterDisplay());
  }


}
