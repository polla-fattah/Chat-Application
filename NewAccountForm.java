import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


public class NewAccountForm extends JDialog
{
	private JButton okButton, cancelButton;
	private JTextField []inputData = new JTextField[7];
	private JPasswordField [] password = new JPasswordField[2];
	private PrintWriter out = null;
	private BufferedReader in = null;

    public NewAccountForm(String language, PrintWriter o,BufferedReader ino)
    {
		out = o;
		in = ino;
		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setBounds(100,100,320,320);
		setTitle(names.getString("newAccountForm"));
		setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

		String labelStr[] = {"password", "confirmPassword", "userID", "nickName",
							 "gender", "birthYear", "location", "secretQuestion",
							 "secretAnswer"};
		JLabel label ;
		for (int i = 0 ; i < labelStr.length ; i++)
		{
			label = new JLabel(names.getString(labelStr[i]));
			label.setBounds(10, 25 * i+ 10, 135, 20);
			contentPane.add(label);

		}

		for (int i = 0 ; i < 2 ; i++)
		{
			password[i] = new JPasswordField();
			password[i].setBounds(150, 25 * i + 10,150,20);
			contentPane.add(password[i]);
		}

		for (int i = 0 ; i < inputData.length ; i++)
		{
			inputData[i] = new JTextField(20);
			inputData[i].setBounds(150, 25 * (i+2) + 10,150,20);
			contentPane.add(inputData[i]);
		}

		okButton = new JButton(names.getString("ok"));
		okButton.setMnemonic(minimonics.getString("ok").charAt(0));
		okButton.setBounds(45, 250,110,25);
		okButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev)
								{
									char a[] = password[0].getPassword();
									char b[] = password[0].getPassword();
									for(int i = 0; i < a.length ;i++)
										if(a[i] != b[i])
										{
											JOptionPane.showMessageDialog(null, "Passward Error");
											return;
										}
									String passOut = new String(a);
									String IdOut = inputData[0].getText();
									String nickNameOut = inputData[1].getText();
									String locOut = inputData[4].getText();
									String birthDayOut ="";
									try
									{
										birthDayOut = "" + Integer.parseInt(inputData[3].getText());
									}
									catch(Exception ed)
									{
										JOptionPane.showMessageDialog(null, "Birth day Error");
										return;
									}
									String gen = inputData[2].getText();
									if(gen.length()<1)
										return;
									out.println("2" + IdOut + ";" + nickNameOut + ";" + passOut + ";" +
										birthDayOut + ";" + locOut + ";" + inputData[5].getText() + ";" +
										inputData[6].getText() + ";" +  gen);
									String inline = "";
									try{inline = in.readLine();}
									catch(Exception e){}
									if(inline.equals("true"))
										JOptionPane.showMessageDialog(null, "you are added as a new friend welcome to you.");
									else
										JOptionPane.showMessageDialog(null, "Please try another Id ");

									System.exit(0);
									}});

		contentPane.add(okButton);

		// Action of the Ok button...

		cancelButton = new JButton(names.getString("cancel"));
		cancelButton.setMnemonic(minimonics.getString("cancel").charAt(0));
		cancelButton.setBounds(160, 250,110,25);
		cancelButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev)
								{
									System.exit(0);
								}});

		contentPane.add(cancelButton);
    }

}
