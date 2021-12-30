import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.event.*;

public class FriendTree extends JTree
{
	private Set friends;
	private String id;
	private PrivateInterface privateInterface;
	private Map privateRooms;
	private String language;
	private PrintWriter out;
	FriendTree(DefaultMutableTreeNode root,String strF[],String iD, Map privateR, String lang, PrintWriter o)
	{
		super(root);
		id = iD;
		friends = getFriends(strF);
		privateRooms = privateR;
		language = lang;
		out = o;
		for (Iterator i = friends.iterator() ; i.hasNext();)
			root.add(new DefaultMutableTreeNode((Friend)i.next()));
		setCellRenderer(new MyRenderer());

		//addTreeSelectionListener(this);


		MouseListener ml = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int selRow = getRowForLocation(e.getX(), e.getY());
				TreePath selPath = getPathForLocation(e.getX(), e.getY());
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				if(node == null)
					return;
				Object nodeInfo = node.getUserObject();
				if(selRow != -1)
				{
					if(e.getClickCount() == 2)
					{
						if (node.isLeaf())
						{
							privateInterface = (PrivateInterface)privateRooms.get(node.toString());
							if(privateInterface == null)
							{
									privateInterface = new PrivateInterface(language,out, id,node.toString(),privateRooms);
									privateInterface.show();
									privateRooms.put(node.toString(),privateInterface);
							}
							else
							{
								privateInterface.show();
							}
						}
					}//if(e.getClickCount() == 2)
				}//end of if(selRow != -1)
			}//end of mousePressed
		};//end of listener
		addMouseListener(ml);
	}//end of constructor

	public void setStatus(String strFriend)
	{
		Friend frn = new Friend(strFriend);
		for(Iterator i = friends.iterator();i.hasNext();)
		{
			Friend f = (Friend) i.next();
			if(f.getName().equals(frn.getName()))
				f.setStatus(frn.getStatus());
		}
		setCellRenderer(new MyRenderer());
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		if (node == null) return;
		Object nodeInfo = node.getUserObject();
		if (node.isLeaf())
		{
			privateInterface = (PrivateInterface)privateRooms.get(node.toString());
			if(privateInterface == null)
			{
				privateInterface = new PrivateInterface(language,out, id,node.toString(),privateRooms);
				privateInterface.show();
				privateRooms.put(node.toString(),privateInterface);
			}
			else
			{
				privateInterface.show();
			}

		}
	}

	public Set getFriends(String []strFriend)
	{
		Set f = new HashSet();

		for (int i = 0 ; i < strFriend.length ; i++)
		{
			if(!strFriend[i].equals(""))
				f.add(new Friend(strFriend[i]));
			else
				f.add(new Friend("1" + id));
		}
		return f;
	}

    private class MyRenderer extends DefaultTreeCellRenderer
    {
		ImageIcon onLineIcon, offLineIcon;

		public MyRenderer()
		{
			onLineIcon  = new ImageIcon("images/online.gif");
			offLineIcon = new ImageIcon("images/offline.gif");
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
														boolean leaf, int row,boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row, hasFocus);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			Friend nodeInfo = (Friend)node.getUserObject();
			if(leaf)
				if (nodeInfo.getStatus())
				{
					setIcon(onLineIcon);
				}
				else
				{
					setIcon(offLineIcon);
				}
			return this;
		}
	}//inner
}//
