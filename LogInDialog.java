import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class LogInDialog extends JDialog
{
	private JButton[] buttons = new JButton[3];
	private JTextField userIdTextField;
	private JPasswordField password;

	private PrintWriter out = null;
	private BufferedReader in = null;
	private Socket echoSocket = null;

	private String language;

    public LogInDialog()
    {
		try
		{
			echoSocket = new Socket("localhost", 2004);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Server not found ");
			System.exit(1);
		}
		language = getLanguage();
		ResourceBundle names  = ResourceBundle.getBundle("Names", new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics", new Locale(language));

		setBounds(100,100,335,180);
		setTitle(names.getString("login"));
		setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

		JLabel label;
		String[] labelNames = { "logInuserID","passwordLogIn"};

		for(int i=0 ; i<labelNames.length ; i++)
		{
		label = new JLabel(names.getString(labelNames[i]));
		label.setBounds(20,10+ i* 23 ,120,20);
		contentPane.add(label);
		}

		userIdTextField = new JTextField();
		userIdTextField.setBounds(150,10,150,20);
		contentPane.add(userIdTextField);

		password = new JPasswordField();
		password.setBounds(150,35,150,20);
		contentPane.add(password);

		String[] buttonNames ={ "login", "createNewAccount", "recoverPassword"};
		LoginListener listener = new LoginListener();
		for (int i = 0 ; i < buttonNames.length ; i++)
		{
			buttons[i] =new JButton(names.getString(buttonNames[i]));
			buttons[i].setMnemonic(minimonics.getString(buttonNames[i]).charAt(0));
			if(i == 2)
				buttons[i].setBounds(10, 110,305,25);
			else
				buttons[i].setBounds(10+ i* 155, 80,150,25);
			contentPane.add(buttons[i]);
			buttons[i].addActionListener(listener);
		}
		show();
    }//end of constructor
    private String getLanguage()
    {
		String lan = "";
		try
		{
			BufferedReader input = new BufferedReader(new FileReader("language.txt"));
			lan = input.readLine();
			input.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to open critical Syetem File Defaults loaded.");
			return "en";
		}
		return lan;
	}
	private class LoginListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			Object source = ev.getSource();
			if(source == buttons[0])		//Login
			{
				String id = userIdTextField.getText().trim();
				String passStr ="";
				char []passChar = password.getPassword();
				for (int i = 0 ; i < passChar.length ; i++)
					passStr += passChar[i];
				if(id.equals("") || passStr.equals(""))
					return;
				String outline = id + " " + passStr;
				out.println("1" + outline);
				int portNumber = -1;
				try
				{
					portNumber = Integer.parseInt(in.readLine());
					out.close();
					in.close();
					echoSocket.close();
				}
				catch(Exception ex)
				{
					System.out.println("Omed");
					JOptionPane.showMessageDialog(null, "Some problem occour while conecting to the Server");
					System.exit(1);
				}
				if(portNumber == -1)
				{
					try
					{
						echoSocket = new Socket("localhost", 2004);
						out = new PrintWriter(echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, "Server not found ");
						System.exit(1);
					}
					JOptionPane.showMessageDialog(null, "User ID and/orPassword is wrong.");
					return;
				}
				try
				{
					echoSocket = new Socket("localhost", portNumber);
					out = new PrintWriter(echoSocket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
					out.println(id);
					new BlindChat(id, passStr, in, out, language);
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "00Some problem occour while conecting to the Server");
					System.exit(1);
				}

				hide();
			}
			//else if(source == buttons[1])
			//{

		}//end of method actionPerformed
	}//end of listener
}
