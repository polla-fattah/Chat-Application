import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;

class BlindChat extends JFrame
{
	private JMenuItem []menuItem;

	private String[]friends;
	private String[]rooms;
	private String[]ignoredUsers;
	private Map privateRooms;

	private BufferedReader in ;
	private PrintWriter out;

	private String id;
	private String pass;
	private String language;
	private DefaultMutableTreeNode root;
	BlindChat(String iD, String passStr, BufferedReader i, PrintWriter o, String lang)
	{
		id = iD;
		pass = passStr;
		in = i;
		out = o;
		language = lang;
		String offlineMessages[] = null;
		privateRooms = new HashMap();
		try
		{
			friends = in.readLine().split(";");
			rooms = in.readLine().split(";");
			ignoredUsers = in.readLine().split(";");
			offlineMessages = in.readLine().split(";");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, "Some problem occour while conecting to the Server");
		}
		if(!offlineMessages[0].equals(""))
		{
			OfflineMessageDialog ofl = new OfflineMessageDialog(language);
			ofl.show(offlineMessages);
		}
	 	final int WIDTH = 430, HEIGHT = 450;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();

		setTitle("Zibary Chat");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocation((d.width - WIDTH) / 2 , (d.height - HEIGHT) / 2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//menu bar
		menuItem = new JMenuItem[5];
		String key[] = {"+", "login", "disConnect", "-", "myProfile", "preferences", "-", "close",
						"+", "help","-", "about"};
		MenuListener Ml = new MenuListener();

		MenuCreater menuBar = new  MenuCreater(language, menuItem, key);//the "en" should be changed
		setJMenuBar(menuBar);

		for (int index = 0 ; index < menuItem.length ; index++)
			menuItem[index].addActionListener(Ml);

//the whole panel

		//friend Tree
        Container contentPane = getContentPane();
 		contentPane.setLayout(null);

		root = new DefaultMutableTreeNode(new Friend("0Freind List:"));
		FriendTree tree = new FriendTree(root,friends, id, privateRooms, language , out);
		tree.expandRow(0);
		JScrollPane friendScroll = new JScrollPane(tree);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		friendScroll.setBounds(5, 5, 205, 385);
		contentPane.add(friendScroll);

		RoomsList roomsList = new RoomsList(rooms, out);
		roomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane roomsScroll = new JScrollPane(roomsList);
		roomsScroll.setBounds(215, 5, 205, 385);
		contentPane.add(roomsScroll);
		ClientListener cl = new ClientListener(in, out, privateRooms, language, id, tree);
		cl.start();
		show();
	}
	private class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			if(menuItem[0] == source)
			{
				out.println("99");
				System.exit(0);
			}
			else if(menuItem[1] == source)
			{
				out.println("02" + id);
			}
			else if(menuItem[2] == source)
			{
				new PreferencesDialog(language, ignoredUsers, out);
			}
			else if(menuItem[3] == source)
			{
				System.exit(0);
			}
			else if(menuItem[4] == source)
			{
				new aboutDialog(language);

			}
		}
	}
}
