import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
class RoomsList extends JList
{
	//need to static data
	//to keep out the room names and their Ids'
	private String [] rooms;
	private PrintWriter out;

	public RoomsList(String []r, PrintWriter o)
	{
		rooms = r;
		out = o;
		setListData(rooms);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		MouseListener ml = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					out.println("51" + ((String)getSelectedValue()).trim());
				}
			}//end of mousePressed
		};//end of listener
		addMouseListener(ml);
	}
}