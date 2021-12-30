import java.sql.*;
import java.util.*;

public class DataBaseController
{
	Connection con = null;
	Statement stmt = null;

    public DataBaseController()
    {
		try
		{	//load odbc-jdbc bridge driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			con = DriverManager.getConnection( "jdbc:odbc:MessengerDSN", "", "");
			con.setAutoCommit(true);
			stmt = con.createStatement();
		}
		catch(ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
    }

////////////////////////////////////////////////////////////////////////

    public boolean checkPassword(String userId, String pass)
    {
		String SQLStatment = "select userID,passWord from userInfoTbl where userID = '" + userId + "' And passWord = '" + pass + "'";
		boolean result = false;

		try
		{
			ResultSet rs = stmt.executeQuery(SQLStatment);
			result = rs.next();
			rs.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		return result;
	}

////////////////////////////////////////////////////////////////////////

    public String[] getUserInfo(String userId)
    {
		String SQLStatment = "select * from userInfoTbl where userID = '" + userId + "'";
		String[] result = new String[4];

		try
		{
			ResultSet rs = stmt.executeQuery(SQLStatment);
			if(rs.next())
			{
				result[0] = rs.getString("nickName");
				result[1] = rs.getString("dateOfBirth");
				result[2] = rs.getString("location");
				result[3] = rs.getString("Gender");
			}
			rs.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		return result;
	}

////////////////////////////////////////////////////////////////////////

    public String[] getQuestionAnserPassword( String userId)
    {
		String SQLStatment = "select * from userInfoTbl where userID = '" + userId + "'";
		String[] result = new String[3];
		try
		{
			ResultSet rs = stmt.executeQuery(SQLStatment);
			if(rs.next())
			{
				result[0] = rs.getString("secretQuestion");
				result[1] = rs.getString("secretAnswer");
				result[2] = rs.getString("passWord");
			}
			rs.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		return result;
	}

////////////////////////////////////////////////////////////////////////

    public String[] getFriends(String userId)
    {
		String SQLStatment = "select * from idFriendTbl where userID = '" + userId + "'";
		String[] result = null;
		Set temp = new HashSet();

		try
		{
			ResultSet rs = stmt.executeQuery(SQLStatment);
			while(rs.next())
				temp.add(rs.getString("Friends"));
			rs.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		result = new String[temp.size()];
		Iterator itr = temp.iterator();
		for (int i = 0 ; i < result.length ; i++)
			result[i] = (String)itr.next();

		return result;
	}
////////////////////////////////////////////////////////////////////////

    public String[] getIgnoredUsers(String userId)
    {
		String SQLStatment = "select * from ignoredUsers where userId = '" + userId + "'";
		String[] result = null;
		Set temp = new HashSet();

		try
		{
			ResultSet rs = stmt.executeQuery(SQLStatment);
			while(rs.next())
				temp.add(rs.getString("ignoredUser"));
			rs.close();
		}
		catch(SQLException ex)
		{System.out.println("Polla");}
		result = new String[temp.size()];
		Iterator itr = temp.iterator();
		for (int i = 0 ; i < result.length ; i++)
			result[i] = (String)itr.next();
		return result;
	}

////////////////////////////////////////////////////////////////////////

    public String[] getOfflineMessage(String To)
    {
		String SQLStatment = "select FromUser , Messege from MessageTbl where To = '" + To + "'";
		String[] result = null;
		Set temp = new HashSet();

		try
		{
			ResultSet rs = stmt.executeQuery(SQLStatment);
			while(rs.next())
				temp.add( rs.getString("FromUser") + rs.getString("Messege"));

			rs.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		result = new String[temp.size()];
		Iterator itr = temp.iterator();
		for (int i = 0 ; i < result.length ; i++)
		{
			result[i] = (String)itr.next();
		}
      	SQLStatment = "DELETE  * FROM MessageTbl where To = '" + To + "'";
		try
		{
            stmt = con.createStatement();
            stmt.executeUpdate(SQLStatment);
        }
        catch(SQLException ex)
        {}

		return result;
	}

////////////////////////////////////////////////////////////////////////

    public boolean changePassword(String ID, String pass)
    {
      	String createString = "UPDATE  userInfoTbl SET passWord = '" + pass +"' where userID = '" + ID + "'";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
        return (temp!=0);
	}

////////////////////////////////////////////////////////////////////////

    public boolean updateUserInfo(String id, String nick, int birth, String loc, String gen)
    {
      	String createString = "UPDATE  userInfoTbl SET nickName = '" + nick +"', dateOfBirth = " + birth +
      							", location = '" + loc +"', Gender = '" + gen + "' where userID = '" + id + "'";
        int temp=0;
		try
		{

            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
        return (temp != 0);
	}
////////////////////////////////////////////////////////////////////////

    public boolean removeFriend(String userId, String friendId)
    {
      	String createString = "DELETE  * FROM idFriendTbl where userID = '" + userId + "' AND Friends = '" + friendId +
      							"' OR userID = '" + friendId + "' AND Friends = '" + userId + "'";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
           return false;
        }
        return (temp != 0);
	}

////////////////////////////////////////////////////////////////////////

    public boolean addFriend(String userId, String friendId)
    {
      	String createString = "INSERT INTO idFriendTbl values('" + userId + "','"+friendId+"')";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
			return false;
        }
        return (temp!=0);
	}
////////////////////////////////////////////////////////////////////////

    public boolean cancelIgnore(String userId, String ignoredId)
    {
      	String createString = "DELETE  * FROM ignoredUsers where userId = '" + userId + "' AND ignoredUser = '" + ignoredId + "'";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
            return false;
        }
        return (temp!=0);
	}

////////////////////////////////////////////////////////////////////////

    public boolean ignoreUser(String userId, String ignoredUserId)
    {
      	String createString = "INSERT INTO ignoredUsers values('" + userId + "','" + ignoredUserId + "')";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
			return false;
        }
        return (temp!=0);
	}

////////////////////////////////////////////////////////////////////////

    public boolean holdNewAcount(String userId, String nick,String pass, int birth, String loc, String question, String answer, String gen)
    {
      	String createString = "INSERT INTO userInfoTbl values('" + userId + "','" +nick+ "','" +pass+ "','"  +birth+ "','" +loc+ "','" +question+ "','" +answer+ "','" + gen +"')";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
			return false;
        }
        return (temp!=0);
	}

////////////////////////////////////////////////////////////////////////

    public boolean holdOfflineMessege(String toId, String fromId, String messege)
    {
      	String createString = "INSERT INTO MessageTbl values('" + toId + "','" +fromId+ "','" + messege +"')";
        int temp=0;
		try
		{
            stmt = con.createStatement();
            temp = stmt.executeUpdate(createString);
        }
        catch(SQLException ex)
        {
			return false;
        }
        return (temp!=0);
	}
}
