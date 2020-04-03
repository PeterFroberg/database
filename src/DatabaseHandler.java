import java.sql.*;

public class DatabaseHandler {
    private Connection dbConnection;
    private PreparedStatement sqlStatment;

    public void connectToDatabase(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection("jdbc:mysql://" + System.getenv("host") + "/" + System.getenv("dbname"), System.getenv("dbusername"), System.getenv("dbpassword"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
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
    public ResultSet getComments(){
        try {

            sqlStatment = dbConnection.prepareStatement("SELECT * FROM guestbook");
            ResultSet resultSet = sqlStatment.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
