import java.net.*;
import java.io.*;
import java.util.*;

class ContactController extends Thread
{
	private DataBaseController dbController;
	private Map onlineUserList;
	private Set listenersSet;
	private int portNumber;
	private ServerMessanger father;
	private Map publicRoom;

	public ContactController(DataBaseController d,Map onlineUserL, ServerMessanger s, Map pub)
	{
		father = s;
		dbController = d;
		portNumber = 5000;
		onlineUserList = onlineUserL;
		listenersSet = new HashSet();
		publicRoom = pub;
	}

	public void run()
	{
		SocketListener socketListener;
		ServerSocket contactServerSocket = null;
		while(true)
		{
			try
			{
				while(true)
				{
					socketListener = getServerListener();
					contactServerSocket = socketListener.listener;
					new UserContact(contactServerSocket.accept()).start();
					socketListener.connected++;
				}
			}
			catch(IOException ioe)
			{
				System.out.println("Hi Mr Problem.");
				ioe.printStackTrace();
				try{
					contactServerSocket.close();
				}
				catch(IOException oe){}
			}
		}
	}//end of run

	//To dicide which socket to be listened
	private SocketListener getServerListener()
	{
		SocketListener temp;
		for (Iterator i = listenersSet.iterator(); i.hasNext();)
		{
			temp = (SocketListener) i.next();
			if(temp.connected < 30)
			{
				portNumber = temp.listener.getLocalPort();
				return temp;
			}
		}

		int size = listenersSet.size() + 5001;
		for (int i = 5000 ; i < size ; i++)
		{
			if(!isSocketExist(i))
			{
				SocketListener soc = null;
				try
				{
					portNumber = i;
					soc = new SocketListener(0, new ServerSocket(portNumber));
				}
				catch (BindException e)
				{
					size++;
					continue;
				}
				catch (IOException e)
				{
					e.printStackTrace();
					continue;
				}
				listenersSet.add(soc);
				return  soc;
			}
		}
		return null;
	}//end of SocketListener getServerListener()

	private boolean isSocketExist(int n)
	{
		SocketListener temp;
		for (Iterator i = listenersSet.iterator(); i.hasNext();)
		{
			temp = (SocketListener) i.next();
			if(temp.listener.getLocalPort()  == n)
				return true;
		}
		return false;
	}//end of isSocketExist

	public int getPortNumber()
	{
		return portNumber;
	}
//////////////////////////////////////////////////////
	private class UserContact extends Thread
	{
		private Socket loginSocket;
		public UserContact(Socket loginSoc)
		{
			loginSocket = loginSoc;
		}
		public void run()
		{
			String inputLine, outputLine;
			try
			{
				PrintWriter out = new PrintWriter( loginSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(loginSocket.getInputStream()));
				outputLine = "";
				inputLine = in.readLine();

				//splitting userID from password
				String friends[] = dbController.getFriends(inputLine);

				out.println(creatFriendStr(friends));
				out.println(father.getRooms());
				out.println(combineStr(dbController.getIgnoredUsers(inputLine), ";"));
				out.println(combineStr(dbController.getOfflineMessage(inputLine), ";"));

				User user = new User(out, in, inputLine, dbController, onlineUserList, portNumber, listenersSet, publicRoom);
				user.start();
				User fr;
				//I am online
				for (int i = 0 ; i < friends.length ; i++)
					if(onlineUserList.containsKey(friends[i]))
					{
						fr = (User)onlineUserList.get(friends[i]);
						fr.output("20" + inputLine);
					}

				onlineUserList.put(inputLine,user);
			}
			catch (IOException e){e.printStackTrace();}
		}//end run

		private String combineStr(String data[],String seperator)
		{
			String temp = "";
			for (int i = 0 ; i < data.length ; i++)
				temp += data[i] + seperator;
			return temp;
		}

		private String creatFriendStr(String data[])
		{
			String temp = "";
			for (int i = 0 ; i < data.length ; i++)
				temp += (onlineUserList.containsKey(data[i])? 1 : 0) + data[i] + ";";
			return temp;
		}
		protected void finalize()
		{
			System.gc();
		}
	}//end of UserLogin inner class
}//end.
