import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class PublicInterface extends JFrame
{
	private JComboBox []cbFormatting = new JComboBox[3];
	private JButton []formattingButtons = new JButton[3];

	private JButton send,ignoreUser;
	private JMenuItem menuItems[];

	private JEditorPane messageTextArea, typingTextArea;

	private JList userList;
    private DefaultListModel listModel;

	private PrintWriter out;
	private String ID;
	private String name;
	private PrivateInterface privateInterface;
	private Map privateRooms;
	private String language;
	private int pressTime;
	public PublicInterface(String lang, String existsUsers, PrintWriter o, String id,String na, Map privateR)
	{
		ID = id;
		out = o;
		name = na;
		privateRooms = privateR;
		language = lang;
	 	final int WIDTH = 700, HEIGHT = 500;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();

		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setTitle(name + " Room");
		setBounds((d.width - WIDTH) / 2 , (d.height - HEIGHT) / 2, WIDTH, HEIGHT);
		setResizable(false);
		//Menu Bar
		menuItems = new JMenuItem[30];
		String menItemNames[] = {"+", "fileM"  , "new", "send", "-", "save", "print", "printAsPlainText" , "-", "joinChatRoom", "goToChatUser", "-", "editMyNickname", "preferences" ,"-", "close",
								 "+", "edit"  , "cut", "copy", "paste", "delete", "-", "selectAll",
								 "+", "friend","addAsFriend", "viewProfiles", "ignoreUser", "-", "sendAMessage", "-", "sendFile",
								 "+", "format", "font", "size", "color", "-", "bold", "underline", "italic", "-", "defaultFontAndColor",
								 "+", "help"  ,"messengerHelp", "howToUseMessenger", "aboutShanMessenger"};

		JMenuBar menuBar = new MenuCreater (language , menuItems, menItemNames);
		setJMenuBar(menuBar);
		addWindowListener(new WindowAdapter(){
			    public void windowClosing(WindowEvent e)
			    {
			        out.println("52");
			    }});

        Container contentPane = getContentPane();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        contentPane.setLayout(gridbag);

		//the formatting midil tools
		JPanel formattingPanel=new JPanel();
		formattingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		Insets buttonInset = new Insets(0, 0, 0, 0);
		String[] buttonNames = {"Bold", "Italic", "Underline"};
		for (int i = 0 ; i < buttonNames.length ; i++)
		{
			formattingButtons[i]=new JButton(new ImageIcon("images/" + buttonNames[i] + ".gif"));
			formattingButtons[i].setToolTipText(buttonNames[i]);
			formattingButtons[i].setMargin(buttonInset);
			formattingPanel.add(formattingButtons[i]);

		}

		String[] cbNames = {"Font", "Font Size", "Color"};
		String cbContenst[][] = new String[3][];

        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String envfonts[] = gEnv.getAvailableFontFamilyNames();
		cbContenst[0] = envfonts;
		String sizes[] = {"8", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
		cbContenst[1] = sizes;
		String colors[] = {"Black", "Green", "Orange", "Pink", "Cyan", "Magenta", "Yellow", "White", "Gray", "Light Gray", "Dark Gray", "Red", "Blue"};
		cbContenst[2] = colors;

		Insets cbInset = new Insets(0, 2, 0, 2);
		for (int i = 0 ; i < cbNames.length ; i++)
		{
			cbFormatting[i] = new JComboBox( cbContenst[i]);
			cbFormatting[i].setToolTipText(cbNames[i]);
			cbFormatting[i].setMaximumRowCount( 4 );
			formattingPanel.add(cbFormatting[i]);
		}


		// Message Text Area
		messageTextArea = new JEditorPane();
		messageTextArea.setEditable(false);
		JScrollPane messageTextAreaScrollPane = new JScrollPane(messageTextArea);
		messageTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(2,2,0,2);
		c.fill = GridBagConstraints.BOTH;
        gridbag.setConstraints(messageTextAreaScrollPane, c);
        contentPane.add(messageTextAreaScrollPane);

		// User List
		String usersStr[] = existsUsers.split(";");
		listModel = new DefaultListModel();
		for (int i = 0; i < usersStr.length ; i++)
			listModel.addElement(usersStr[i]);
		userList = new JList(listModel);
		MouseListener ml = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					privateInterface = (PrivateInterface)privateRooms.get(userList.getSelectedValue().toString());
					if(privateInterface == null)
					{
							privateInterface = new PrivateInterface(language,out, ID,userList.getSelectedValue().toString(), privateRooms);
							privateInterface.show();
							privateRooms.put(userList.getSelectedValue().toString(),privateInterface);
					}
					else
					{
						privateInterface.show();
					}
				}

			}//end of mousePressed
		};//end of listener
		userList.addMouseListener(ml);

		JScrollPane userListScrollPane = new JScrollPane(userList);
 		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.0;
		c.ipadx = 40;
        gridbag.setConstraints(userListScrollPane, c);
        contentPane.add(userListScrollPane);


		// FormattingPanel adding
 		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.insets = new Insets(0,2,0,2);
        gridbag.setConstraints(formattingPanel, c);
        contentPane.add(formattingPanel);

		// Ignore User
		ignoreUser = new JButton(names.getString("ignoreUserB"));
		ignoreUser.setMargin( new Insets(1,1,1,1));
		ignoreUser.setMnemonic(minimonics.getString("ignoreUserB").charAt(0));
 		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(5,5,5,5);
        gridbag.setConstraints(ignoreUser, c);
        contentPane.add(ignoreUser);

		// Typing Text Area
		typingTextArea = new JEditorPane();
		JScrollPane typingTextAreaScrollPane = new JScrollPane(typingTextArea);
		typingTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 		typingTextArea.addKeyListener(new KeyListener(){
											public void keyTyped(KeyEvent e)
											{}
											public void keyPressed(KeyEvent e)
											{
												if(language.equals("ku"))
												{
													if(pressTime >1)
														diside(e.getKeyCode());
													else
														pressTime++;
												}

											}
											public void keyReleased(KeyEvent e)
											{
												if(language.equals("ku"))
												{
													pressTime = 0;
													diside(e.getKeyCode());
												}

												if(e.getKeyCode() == KeyEvent.VK_ENTER)
												{
													String inputStr = typingTextArea.getText();
													inputStr = inputStr.trim();
													if(inputStr.equals(""))
														return;
													out.println("50" + inputStr);
													typingTextArea.setText("");
												}
											}
											private void diside(int code)
											{
												if(code == 91 || code == 93 || code == 47 || code ==222 || code ==192)
												{
													String str = typingTextArea.getText();
													int last = str.length();
													str = str.substring(0,str.length()-1);
													switch(code)
													{
														case 47:
															str += "\u0219";
														break;
														case 91:
															str += "\u00FB";
														break;
														case 93:
															str += "\u00E7";
														break;
														case 192:
															str += "\u00EA";
														break;
														case 222:
															str += "\u00EE";
														break;
													}
													typingTextArea.setText(str);
												}

											}

											});


 		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.0;
		c.ipady = 35;
		c.insets = new Insets(0,2,5,2);
      	gridbag.setConstraints(typingTextAreaScrollPane, c);
        contentPane.add(typingTextAreaScrollPane);

 		send = new JButton(names.getString("send"));
 		send.setMnemonic(minimonics.getString("send").charAt(0));
  		send.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev)
  								{
 									String inputStr = typingTextArea.getText();
 									inputStr = inputStr.trim();
 									if(inputStr.equals(""))
 										return;
 									out.println("50" + inputStr);
 									typingTextArea.setText("");
 								}});

 		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0.0;
		c.insets = new Insets(0,5,5,5);
        gridbag.setConstraints(send, c);
        contentPane.add(send);
		show();
	}
	public void closeMe()
	{
		hide();
	}
	public String toString()
	{
		return name;
	}
	public void addUser(String userID)
	{
		listModel.addElement(userID);
	}
	public void removeUser(String userID)
	{
		listModel.removeElement(userID);
	}
	public void actionPerformed(String UserID)
	{
		if (UserID.equals(""))
			return;

		int index = userList.getSelectedIndex();
		int size = listModel.getSize();

		if (index == -1 || (index+1 == size))
		{
			listModel.addElement(UserID);
			userList.setSelectedIndex(size);

		}
		else
		{
			listModel.insertElementAt(UserID, index+1);
			userList.setSelectedIndex(index+1);
		}
	}//end of action actionPerformed
	public void write(String str)
	{
		String oldStr = messageTextArea.getText();
		messageTextArea.setText(oldStr +"\n"+str);
	}

}