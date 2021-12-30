import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class changePasswordDialog extends JDialog
{
	private JButton[] buttons = new JButton[2];
	private JTextField[] passwordFields = new JPasswordField[3];

    public changePasswordDialog(String language)
    {
		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setBounds(100,100,320,155);
		setTitle(names.getString("changePassword"));
		setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

		JLabel tempLabel;
		String[] labelText = { "currentPassword", "newPassword", "ConfirmNewPassword"};

		for(int i=0 ; i<labelText.length ; i++)
		{
			tempLabel = new JLabel(names.getString(labelText[i]));
			tempLabel.setBounds(10,10+ i*25 ,140,20);
			contentPane.add(tempLabel);
		}

		for(int i=0 ; i < passwordFields.length ; i++)
		{
			passwordFields[i] = new JPasswordField();
			passwordFields[i].setBounds(150,10+ i*25,150,20);
			contentPane.add(passwordFields[i]);
		}

		String[] buttonNames ={ "ok", "cancel"};

		for (int i = 0 ; i < buttonNames.length ; i++)
		{
			buttons[i] =new JButton(names.getString(buttonNames[i]));
			buttons[i].setMnemonic(minimonics.getString(buttonNames[i]).charAt(0));
			buttons[i].setBounds(20+ i* 140, 95,130,25);
			contentPane.add(buttons[i]);
		}


		// Action of the Ok button...
    }

}
