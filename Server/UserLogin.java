import java.net.*;
import java.io.*;
import java.util.*;

class UserLogin extends Thread
{
	DataBaseController dbController;
	private Socket loginSocket = null;
	private ContactController contactController;
	private Map onlineUsers;

	public UserLogin(DataBaseController d, Socket loginSoc, ContactController c, Map onlineU)
	{
		dbController = d;
		loginSocket = loginSoc;
		contactController = c;
		onlineUsers = onlineU;
	}
	public void run()
	{
		String inputLine;
		byte message;
		try
		{
			PrintWriter out = new PrintWriter( loginSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(loginSocket.getInputStream()));

			inputLine = in.readLine();
			try
			{
				message = Byte.parseByte(inputLine.substring(0,1));
			}
			catch(NumberFormatException nfe)
			{
				return;
			}
			inputLine = inputLine.substring(1);
			switch(message)
			{
			case 1://splitting userID from password
				String data[] = inputLine.split(" ");

				if(dbController.checkPassword(data[0], data[1]))
				{
					User u = (User)onlineUsers.get(data[0]);
					if(u != null)
						u.closeMe();
					out.println("" + contactController.getPortNumber());
				}
				else
					out.println("-1");
			break;
			case 2:
				String info[] = inputLine.split(";");
				out.println(""+dbController.holdNewAcount(info[0], info[1],info[2], Integer.parseInt(info[3]), info[4], info[5], info[6], info[7]));
			break;
			case 3://get Secret data
//				out.println(combineStr(dataBaseController.getQuestionAnserPassword(ID)));
			break;


			}//end switch


			out.close();
			in.close();
			loginSocket.close();

		}
		catch (IOException e){}
	}//end run
	protected void finalize()
	{
		System.gc();
	}
}//end of UserLogin inner class
