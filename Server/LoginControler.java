import java.net.*;
import java.io.*;
import java.util.*;
class LoginControler extends Thread
{
	private ServerSocket loginServerSocket = null;
	private DataBaseController dbController;
	private ContactController contactController;
	private Map onlineUsers;

	public LoginControler(DataBaseController d, ContactController c, Map onlineU)
	{
		dbController = d;
		contactController = c;
		onlineUsers = onlineU;
//		reserving a socket for user login
		try
		{
			loginServerSocket = new ServerSocket(2004);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		while (true)
		{
			try
			{
				while(true)
					new UserLogin(dbController, loginServerSocket.accept(), contactController, onlineUsers).start();
			}
			catch(IOException ioe)
			{
				System.out.println("Warning there is danger bug in the program please call Omed or Polla to fix it.");
				ioe.printStackTrace();
				try{
					loginServerSocket.close();
					loginServerSocket = new ServerSocket(2004);
				}
				catch(IOException oe){}
			}
		}
	}
}//end.

