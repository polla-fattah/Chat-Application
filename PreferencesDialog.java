import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class PreferencesDialog extends JDialog
{
	private JList ignoredList;
	private JButton[] ignoredButtons = new JButton[2];
    private DefaultListModel listModel;
	private String[]ignoredUsers;

	private JButton[] filteredButtons = new JButton[2];
	private JTextField addFilterWordText;

	private JRadioButton[] languageRadioButtons = new JRadioButton[2];
	private JButton[] languageButtons = new JButton[2];
	private JButton[] changePassButtons = new JButton[2];
	private ButtonGroup group;

	private JPasswordField[] passwordFields = new JPasswordField[3];
	private ResourceBundle names;
	private ResourceBundle minimonics;

	private PrintWriter out;
	private String language;

    public PreferencesDialog(String lang, String[]ignoredUs, PrintWriter ou)
    {
		ignoredUsers = ignoredUs;
		out = ou;
		language = lang;
		names  = ResourceBundle.getBundle("Names",new Locale(language));
		minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setBounds(100,100,480,400);
		setTitle(names.getString("preferencesTitle"));
		setResizable(false);
		Container contentPane = getContentPane();

        JTabbedPane tabbedPane = new JTabbedPane();

        Component panel1 = ignoredUsersPanel();
        tabbedPane.addTab(names.getString("ignoredUsers"), null, panel1, null);

        Component panel2 = filteredWordPanel();
        tabbedPane.addTab(names.getString("filteredWords"), null, panel2,null);

        Component panel3 = languagePanel();
        tabbedPane.addTab(names.getString("language"), null, panel3, null);

       Component panel4 = hiPanel();
        tabbedPane.addTab(names.getString("changePass"), null, panel4, null);

		tabbedPane.setSelectedIndex(0);
        //Add the tabbed pane to this panel.
        contentPane.setLayout(new GridLayout(1, 1));
        contentPane.add(tabbedPane);
        show();
    }

/////////////////////////////////////////////////////////////////

	protected Component ignoredUsersPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);
		listModel = new DefaultListModel();
		for (int i = 0; i < ignoredUsers.length ; i++)
			if(!ignoredUsers[i].equals(""))
				listModel.addElement(ignoredUsers[i]);
		ignoredList = new JList(listModel);
		JScrollPane scroll = new JScrollPane(ignoredList);
		scroll.setBounds( 10, 10,450 ,295 );
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);


		String []ignoredButtonNames = { "remove", "close"};
		for(int i=0 ; i<ignoredButtonNames.length ; i++)
		{
			ignoredButtons[i] =new JButton(names.getString(ignoredButtonNames[i]));
			ignoredButtons[i].setMnemonic(minimonics.getString(ignoredButtonNames[i]).charAt(0));
			ignoredButtons[i].setBounds( 140 +i*110 , 315,100 ,25 );
			panel.add(ignoredButtons[i]);
		}
		ignoredButtons[0].addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							String userSelected = JOptionPane.showInputDialog("please Enter user ID you want to cancel Ignore");
							if( userSelected == null)
								return;
							listModel.removeElement(userSelected);
							for (int i = 0 ; i < ignoredUsers.length ; i++)
								if(ignoredUsers[i].equals(userSelected))
								{
									ignoredUsers[i] = "";
									break;
								}
							out.println("10" + userSelected);
							System.out.println("10" + userSelected);
						}});

		ignoredButtons[1].addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							hide();
						}});
		return panel;
	}

/////////////////////////////////////////////////////////////

	protected Component filteredWordPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);
		ignoredList = new JList();

		JScrollPane scroll = new JScrollPane(ignoredList);
		scroll.setBounds( 10, 10, 450, 270 );
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);

		addFilterWordText = new JTextField();
		addFilterWordText.setBounds(10, 285, 450,20);
		panel.add(addFilterWordText);

		String []filteredButtonNames = { "add", "remove"};
		for(int i=0 ; i< filteredButtonNames.length ; i++)
		{
			filteredButtons[i] =new JButton(names.getString(filteredButtonNames[i]));
			filteredButtons[i].setMnemonic(minimonics.getString(filteredButtonNames[i]).charAt(0));
			filteredButtons[i].setBounds( 140 +i*110 , 315,100 ,25 );
			panel.add(filteredButtons[i]);
		}
		return panel;
	}

/////////////////////////////////////////////////////////////

	protected Component languagePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);

		String []languageText = {"english", "kurdish"};
		group = new ButtonGroup();
		boolean isTrue = false;
		if(language.equals("en"))
			isTrue = true;

		for(int i=0 ; i< languageText.length ; i++)
		{
			if(i==0)
				languageRadioButtons[i] = new JRadioButton(names.getString(languageText[i]),isTrue);
			else
				languageRadioButtons[i] = new JRadioButton(names.getString(languageText[i]),!isTrue);
			languageRadioButtons[i].setMnemonic(minimonics.getString(languageText[i]).charAt(0));
			group.add(languageRadioButtons[i]);
			languageRadioButtons[i].setBounds(90,55 + i*50 ,100,25);
			panel.add(languageRadioButtons[i]);
			languageRadioButtons[i].setActionCommand(names.getString(languageText[i]));

		}


		String []languageButtonNames = { "ok", "close"};

		for(int i=0 ; i< languageButtonNames.length ; i++)
		{
			languageButtons[i] =new JButton(names.getString(languageButtonNames[i]));
			languageButtons[i].setMnemonic(minimonics.getString(languageButtonNames[i]).charAt(0));
			languageButtons[i].setBounds( 140 +i*110 , 315,100 ,25 );
			panel.add(languageButtons[i]);
		}
		languageButtons[0].addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							String command = group.getSelection().getActionCommand();
							String outline = "";
							try
							{
								BufferedWriter fileOut = new BufferedWriter(new FileWriter("Language.txt"));
								if(command.equals(names.getString("english")))
									outline = "en";
								else
									outline = "ku";
									System.out.println(outline);
								fileOut.write(outline);
								fileOut.close();
							}
							catch(Exception exc)
							{exc.printStackTrace();
							}
							JOptionPane.showMessageDialog(null, "please Restart Application to Apply changes");
						}});
		languageButtons[1].addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							hide();
						}});

		return panel;
	}

/////////////////////////////////////////////////////////////////////

	protected Component hiPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);

		String[] labelText = { "currentPassword", "newPassword", "ConfirmNewPassword"};
		JLabel tempLabel;
		for(int i=0 ; i<labelText.length ; i++)
		{
			tempLabel = new JLabel(names.getString(labelText[i]));
			tempLabel.setBounds(95,60+ i*25 ,140,20);
			panel.add(tempLabel);
		}

		for(int i=0 ; i < passwordFields.length ; i++)
		{
			passwordFields[i] = new JPasswordField();
			passwordFields[i].setBounds(235,60+ i*25,150,20);
			panel.add(passwordFields[i]);
		}

		String[] buttonNames ={ "ok", "close"};

		for (int i = 0 ; i < buttonNames.length ; i++)
		{
			changePassButtons[i] =new JButton(names.getString(buttonNames[i]));
			changePassButtons[i].setMnemonic(minimonics.getString(buttonNames[i]).charAt(0));
			changePassButtons[i].setBounds( 140 +i*110 , 315,100 ,25 );
			panel.add(changePassButtons[i]);
		}
		return panel;
	}
}

