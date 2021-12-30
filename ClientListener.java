import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;

class ClientListener extends Thread
{
	private BufferedReader in;
	private PrintWriter out;
	private Map privateRooms;
	private String language;
	private String id;
	private PublicInterface publicInterface;
	private FriendTree friendTree;
	ClientListener(BufferedReader i, PrintWriter o, Map privateR, String lang, String iD,FriendTree friendTr)
	{
		in = i;
		out = o;
		privateRooms = privateR;
		friendTree = friendTr;
		language = lang;
		id = iD;
	}
	public void run()
	{
		String input;
		byte message = -1;
		PrivateInterface privateInterface;
		while(true)
		{
			try
			{
				input = in.readLine();
				message = Byte.parseByte(input.substring(0,2));
				input = input.substring(2);
				switch(message)
				{
				case 1://receiving private messages
					String sender = input.substring(0, input.indexOf(":"));
					privateInterface = (PrivateInterface)privateRooms.get(sender);
					if(privateInterface == null)
					{
						privateInterface = new PrivateInterface(language,out, id,sender,privateRooms);
						privateInterface.write(input);
						privateRooms.put(sender,privateInterface);
					}
					else
					{
						privateInterface.write(input);
					}
				break;

				case 2://get an other user info
					new UserInfoDialog(input.split(";"), language, false, out);
				break;
				case 5://get an other user info
					if(input.equals("true"))
						JOptionPane.showMessageDialog(null, "Your profile changed successfully");
					else
						JOptionPane.showMessageDialog(null, "Fialed changing your profile");
				break;
				case 20://a frend becomes online
					friendTree.setStatus("1" + input);
				break;
				case 21://a frend becomes offline
					friendTree.setStatus("0" + input);
				break;

				case 25://get your user info

					new UserInfoDialog(input.split(";"), language, true, out);
				break;

				case 50://get a message from server to the public room
					if(publicInterface != null)
						publicInterface.write(input);
				break;
				case 51://add a user from public room
					if(publicInterface != null)
						publicInterface.addUser(input);
				break;
				case 52://remove a user from public room
					if(publicInterface != null)
					{
						publicInterface.removeUser(input);
						publicInterface = null;
					}
				break;
				case 54://get a response to enter public room
					String name = input.substring(0, input.indexOf(":"));
					String existsUsers = input.substring( input.indexOf(":") + 1);
					if(publicInterface != null)
					{
						publicInterface.closeMe();
					}
					publicInterface = new PublicInterface(language, existsUsers, out, id, name, privateRooms);

				break;
				}//end of switch
			}//end of try
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}//end of while
	}//end of run
}//end of Listener