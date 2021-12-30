import javax.swing.*;
import java.util.*;

public class MenuCreater extends JMenuBar
{
	public MenuCreater(String language, JMenuItem []menuItem, String []key)
	{
		int itemIndex = 0;
		JMenu menu = null;
		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		for (int i  = 0 ; i < key.length ; i++)
		{
			if(key[i].equals("+"))
			{
				i++;
				menu = new JMenu(names.getString(key[i]));
				menu.setMnemonic(minimonics.getString(key[i]).charAt(0));
				add(menu);
			}
			else if(key[i].equals("-"))
			{
				menu.addSeparator();
			}
			else
			{
				menuItem[itemIndex] = new JMenuItem(names.getString(key[i]));
				menuItem[itemIndex].setMnemonic(minimonics.getString(key[i]).charAt(0));

				menu.add(menuItem[itemIndex]);
				itemIndex++;
			}
		}//end of for(i)

	}//end of constructor

}//end of class MenuCreater