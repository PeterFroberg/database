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
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    private JTextArea textArea = new JTextArea("Getting your data....");
    private JTextField textFieldName = new JTextField();

    private JTextField textFieldEmail = new JTextField();

    private JTextField textFieldHomepage = new JTextField();

    private JTextField textFieldComment = new JTextField();

    private JButton submitButton = new JButton("Submit Data");

    public GuestBook(String[] args) {
        this.args = args;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Setting up space
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
        submitButton.addActionListener(e -> {
            submitButtonClicked();
        });
        getContentPane().add("North", jPanel);

        setSize(640, 480);
        setVisible(true);
        getComments();
    }


    private String checkHTML(String strToCheck) {
        Pattern pattern = Pattern.compile("<.*>");
        Matcher matcher = pattern.matcher(strToCheck);
        return matcher.replaceAll("CENCUR");
    }

    private void submitButtonClicked() {
        databaseHandler.connectToDatabase();
        String checkedNameStr = checkHTML(textFieldName.getText());
        String checkedEmailStr = checkHTML(textFieldEmail.getText());
        String checkedHomepageStr = checkHTML(textFieldHomepage.getText());
        String checkedCommentStr = checkHTML(textFieldComment.getText());
        databaseHandler.insertDbPost(checkedNameStr, checkedEmailStr, checkedHomepageStr, checkedCommentStr);
        getComments();
    }

    private void getComments() {
        textArea.setText("");
        databaseHandler.connectToDatabase();
        ResultSet resultSet = databaseHandler.getComments();
        try {
            while (resultSet.next()) {
                textArea.append("Namn" + resultSet.getString("namn") + ": " +
                        " Epost: " + resultSet.getString("epost") +
                        " Hemsida: " + resultSet.getString("namn") + "\n" +
                        "Kommentar: "+ resultSet.getString("kommentar") +"\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public static void main(String[] args) {
        new GuestBook(args);
    }
}
