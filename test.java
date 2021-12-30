import javax.swing.*;
import java.awt.*;
import java.sql.*;

class test
{
	public static void main(String args[])
	{
		new F();
	}
}

class F extends JFrame
{
	F()
	{
		setSize(300,300);
		JTextArea a = new JTextArea();
		JScrollPane sp = new JScrollPane(a);
		Container c = getContentPane();
		c.add(sp);
		show();
		String hhh="";
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection( "jdbc:odbc:MessengerDSN", "", "");
			con.setAutoCommit(true);
			Statement stmt = con.createStatement();
			String sss = "select userID from userInfoTbl";
			ResultSet rs = stmt.executeQuery(sss);
			while(rs.next())
				hhh +=rs.getString("userID")+"\n";
			a.setText(hhh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}