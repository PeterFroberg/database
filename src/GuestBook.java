import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuestBook extends JFrame {
    String[] args;
    /**
     * Create a databasehandler to take care of database communication
     */
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Create GUI components for the application
     */
    private JTextArea textArea = new JTextArea("Getting your data....");
    private JTextField textFieldName = new JTextField();
    private JTextField textFieldEmail = new JTextField();
    private JTextField textFieldHomepage = new JTextField();
    private JTextField textFieldComment = new JTextField();
    private JButton submitButton = new JButton("Submit Data");

    public GuestBook() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        /**
         * Setting up GUI space, by adding GUI components
         */
        getContentPane().add("Center", new JScrollPane(this.textArea));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(5, 2));
        jPanel.add(new JLabel("Name:"));
        jPanel.add(this.textFieldName);
        jPanel.add(new JLabel("E-mail:"));
        jPanel.add(this.textFieldEmail);
        jPanel.add(new JLabel("Homepage:"));
        jPanel.add(this.textFieldHomepage);
        jPanel.add(new JLabel("Comment:"));
        jPanel.add(this.textFieldComment);
        jPanel.add(new JLabel(""));
        jPanel.add(this.submitButton);
        /**
         * creates a ActionListener for the submit button by using lambda expression
         * that use function submitButtonClicked()
         */
        submitButton.addActionListener(e -> {
            submitButtonClicked();
        });
        getContentPane().add("North", jPanel);
        setSize(640, 480);
        setVisible(true);
        getComments();
    }

    /**
     * Function to Censor textstrings from HTML-code
     * @param strToCheck - String to check for HTML-code and if found replace with "CENCUR" text
     * @return - returns the checked/modified textstring
     */
    private String checkHTML(String strToCheck) {
        Pattern pattern = Pattern.compile("<.*>");
        Matcher matcher = pattern.matcher(strToCheck);
        return matcher.replaceAll("CENCUR");
    }

    /**
     * Function to run when actionlistener is activated by the button pressed
     * The function starts a connection to the database and then tries to
     * insert a record in the database by suing the function insertDbPost with
     * information from the textfileds in the gui
     * and finnaly gets new comments from the database
     */
    private void submitButtonClicked() {
        databaseHandler.connectToDatabase();
        String checkedNameStr = checkHTML(textFieldName.getText());
        String checkedEmailStr = checkHTML(textFieldEmail.getText());
        String checkedHomepageStr = checkHTML(textFieldHomepage.getText());
        String checkedCommentStr = checkHTML(textFieldComment.getText());
        databaseHandler.insertDbPost(checkedNameStr, checkedEmailStr, checkedHomepageStr, checkedCommentStr);
        getComments();
    }

    /**
     * Gets new comments from the database and update the gui with new comments
     * The function starts a connection to the database and then tries to get
     * comments from the database using the databasehandler function getComments()
     * Clears the textfield and the inserts new comments
     */
    private void getComments() {
        databaseHandler.connectToDatabase();
        ResultSet resultSet = databaseHandler.getComments();
        if(resultSet != null) {
            textArea.setText("");
            try {
                while (resultSet.next()) {
                    textArea.append("Namn" + resultSet.getString("namn") + ": " +
                            " Epost: " + resultSet.getString("epost") +
                            " Hemsida: " + resultSet.getString("namn") + "\n" +
                            "Kommentar: " + resultSet.getString("kommentar") + "\n\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new GuestBook();
    }
}
