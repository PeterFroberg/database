import java.sql.*;

public class DatabaseHandler {
    private Connection dbConnection;
    private PreparedStatement sqlStatment;

    /**
     * Connects to the database
     */
    public void connectToDatabase(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            dbConnection = DriverManager.getConnection("jdbc:mysql://" + System.getenv("host") + "/" + System.getenv("dbname"), System.getenv("dbusername"), System.getenv("dbpassword"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts new data in the database
     * @param name - Name to in insert in namn-column in the database
     * @param epost - epsot to in insert in namn-column in the database
     * @param hemsida - hemsida to in insert in namn-column in the database
     * @param comment - comment to in insert in namn-column in the database
     */
    public void insertDbPost(String name, String epost, String hemsida, String comment){
        try {
            sqlStatment = dbConnection.prepareStatement("INSERT INTO guestbook (namn, epost, hemsida, kommentar) VALUES(?, ?, ?, ?);");
            sqlStatment.setString(1,name);
            sqlStatment.setString(2,epost);
            sqlStatment.setString(3,hemsida);
            sqlStatment.setString(4,comment);
            boolean result = sqlStatment.execute();
            sqlStatment.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrives comments from the database
     * @return if successful returns a resultset with new comments otherwise returns null
     */
    public ResultSet getComments(){
        try {

            sqlStatment = dbConnection.prepareStatement("SELECT * FROM guestbook");
            ResultSet resultSet = sqlStatment.executeQuery();
            sqlStatment.close();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
