import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class PrivateInterface extends JFrame
{
	private JComboBox []cbFormatting = new JComboBox[3];
	private JButton []formattingButtons = new JButton[3];

	private JButton send;
	private JMenuItem menuItems[];
	private int pressTime;
	private JEditorPane messageTextArea, typingTextArea;

	private String id;
	private PrintWriter out;
	private String to;
	private String language;
	private Map privateRooms;
	public String toString()
	{
		return to;
	}
	public PrivateInterface(String lang, PrintWriter o, String iD, String t, Map privateR)
	{
		id = iD;
		out = o;
		to = t;
		language = lang;
		privateRooms = privateR;
	 	final int WIDTH = 530, HEIGHT = 380;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();

		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setTitle("Zibary Messenger [ "+ to+ " ]");
		setBounds((d.width - WIDTH) / 2 , (d.height - HEIGHT) / 2, WIDTH, HEIGHT);
		setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we)
			{
				privateRooms.remove(id);
			}});

		//Menu Bar
		 menuItems = new JMenuItem[30];
		String menItemNames[] = {"+", "fileM"  , "send", "-", "save", "print", "-", "preferences" ,"-", "close",
								 "+", "edit"  , "cut", "copy", "paste", "delete", "-", "selectAll",
								 "+", "friend","addAsFriend", "viewProfiles", "ignoreUser",
								 "+", "help"  ,"messengerHelp", "aboutShanMessenger"};

		JMenuBar menuBar = new MenuCreater (language , menuItems, menItemNames);
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );

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
		//messageTextArea.setContentType("text/html");
		JScrollPane messageTextAreaScrollPane = new JScrollPane(messageTextArea);
		messageTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 2;
		c.insets = new Insets(2,2,0,2);
		c.fill = GridBagConstraints.BOTH;
        gridbag.setConstraints(messageTextAreaScrollPane, c);
        contentPane.add(messageTextAreaScrollPane);

		// FormattingPanel adding
 		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.insets = new Insets(0,2,0,2);
        gridbag.setConstraints(formattingPanel, c);
        contentPane.add(formattingPanel);

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
														write(id + ":" + inputStr);
													out.println("01" + to +":" + inputStr);
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
											}});

 		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.0;
		c.ipady = 35;
		c.gridwidth = 1;
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
									write(id + ":" + inputStr);
									out.println("01" + to +":" + inputStr);
									typingTextArea.setText("");
								}});
 		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0.0;
		c.insets = new Insets(0,5,5,5);
        gridbag.setConstraints(send, c);
        contentPane.add(send);
		show();
	}//end of constructor
	public void write(String str)
	{
		String oldStr = messageTextArea.getText();
		messageTextArea.setText(oldStr +"\n"+str);
	}
}