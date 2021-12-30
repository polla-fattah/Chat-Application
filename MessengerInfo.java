
import java.sql.*;
public class MessengerInfo
{
    public static void main(String args[])
    {
        String url = "jdbc:odbc:MessengerDSN";
        Connection con;
        String createString;
        createString = "UPDATE  userInfoTbl SET location = 'Erbil'";
        Statement stmt;

        try
        {			//load odbc-jdbc bridge driver
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        }
        catch(java.lang.ClassNotFoundException e)
        {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }


	try {
            con = DriverManager.getConnection(url, "", "");
            stmt = con.createStatement();

            int temp = stmt.executeUpdate(createString);

            stmt.close();
            con.close();
        }
        catch(SQLException ex)
        {
            System.err.println("SQLExc.:" + ex.getMessage());
        }
    }

}
