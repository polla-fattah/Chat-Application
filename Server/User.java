import java.net.*;
import java.io.*;
import java.util.*;

public class User extends Thread
{
	private PrintWriter out;
	private BufferedReader in;
	private String ID;
	private DataBaseController dataBaseController;
	private Map onlineUserList;
	private int portListened;
	private Set listenersSet;
	private boolean endMe;
	private Map publicRoomMap;
	private PublicRooms currentPublic = null;

	User (PrintWriter o, BufferedReader i,String id, DataBaseController d, Map online, int port, Set listenersS, Map pub)
	{
		in = i;
		ID = id;
		out = o;
		portListened = port;
		dataBaseController = d;
		onlineUserList = online;
		listenersSet = listenersS;
		endMe = false;
		publicRoomMap = pub;
	}
	public void run()
	{
		String input;
		byte message = -1;
		while(true)
		{
			try
			{
				input = in.readLine();

				if(endMe) return;
				try
				{
					message = Byte.parseByte(input.substring(0,2));
				}
				catch(NumberFormatException nfe)
				{
					continue;
				}
				input = input.substring(2);


				switch(message)
				{
				case 1://sending private messages
					String to = input.substring(0, input.indexOf(":"));

					User userOut = (User) onlineUserList.get(to);
					if(userOut != null)
					{
						String outString = ID + (input.substring(input.indexOf(":")));
						userOut.output("01" + outString);
					}
					else
					{
						String outString = input.substring(input.indexOf(":"));
						dataBaseController.holdOfflineMessege(to, ID , outString);
					}
				break;
				case 2://to get user info.

					if(input.equals(ID))
						out.println("25" + combineStr(dataBaseController.getUserInfo(input)));
					else
						out.println("02" + combineStr(dataBaseController.getUserInfo(input)));
				break;

				case 4://change password
					out.println( "04" + dataBaseController.changePassword(ID, input));
				break;

				case 5://update user information
					String info[] = input.split(";");//String id, String nick, int birth, String loc, String gen)
					out.println( "05" + dataBaseController.updateUserInfo(ID, info[0], Integer.parseInt(info[1]), info[2], info[3]));
					System.out.println( "05" + dataBaseController.updateUserInfo(ID, info[0], Integer.parseInt(info[1]), info[2], info[3]));
				break;
				case 6://remove friend
					dataBaseController.removeFriend(ID, input);
				break;
				case 7://request add friend
					User friend = (User) onlineUserList.get(input);
					friend.output( "07" + combineStr(dataBaseController.getUserInfo(ID)));
				break;
				case 81://acepting to be friend
						if(dataBaseController.addFriend(ID, input))
						{
							User userPath = (User) onlineUserList.get(input);
							userPath.output("81" + ID);
						}
				break;
				case 82://rejecting to be a friend
						String requestedUser = input.substring(0,input.indexOf(":"));
						String messageStr = input.substring(input.indexOf(":") + 1);
						User userPath = (User) onlineUserList.get(requestedUser);
						userPath.output("80" + ID);
				break;
				case 9://Ignore user
					dataBaseController.ignoreUser(ID, input);
				break;
				case 10://Cancel Ignore
					dataBaseController.cancelIgnore(ID, input);
				break;
				case 50://send a message to public room
					if(currentPublic != null)
						currentPublic.sendMessage(ID + ":" + input);
				break;
				case 51://request for choosing a public room
					if(currentPublic != null)
						currentPublic.removeUser(this);
					currentPublic = (PublicRooms) publicRoomMap.get(input);
					currentPublic.addUser(this);
				break;
				case 52://request for leaving the public room
					System.out.println("HITTR");
					if(currentPublic != null)
					{
						currentPublic.removeUser(this);
						currentPublic = null;
					}
				break;

				case 99:
					closeMe();
					return;
				}//end of switch
			}//end of try
			catch(Exception e)
			{
				closeMe();
				return;
			}
		}//end of while true
	}//end of run
	public String toString()
	{
		return ID;
	}
	private String combineStr(String data[])
	{
		String temp = "";
		for (int i = 0 ; i < data.length ; i++)
			temp += data[i] + ";";
		return temp;
	}
	public void closeMe()
	{
		User userPath;
		SocketListener sl;
		endMe = true;
		//leaving the room
		if(currentPublic != null)
		{
			currentPublic.removeUser(this);
			currentPublic = null;
		}

		String []friends = dataBaseController.getFriends(ID);
		onlineUserList.remove(ID);
		for (int i = 0; i < friends.length ; i++)
		{
			userPath = (User) onlineUserList.get(friends[i]);
			if(userPath != null)
				userPath.output("21" + ID);
		}

		for (Iterator i = listenersSet.iterator(); i.hasNext();)
		{
			sl = (SocketListener) i.next();
			if( sl.listener.getLocalPort() == portListened)
				sl.connected--;
		}
	}//end of closeMe
	public void output(String str)
	{
		out.println(str);
	}
	protected void finalize()
	{
		System.out.println("Ameed");
		System.gc();
	}
}//end of class
