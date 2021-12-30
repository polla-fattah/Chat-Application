import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class recoverPasswordDialog extends JDialog
{
	private JButton[] buttons = new JButton[2];
	private JTextField[] inputTextFields = new JTextField[3];
	private JLabel resultLabel;

    public recoverPasswordDialog(String language)
    {
		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setBounds(100,100,320,180);
		setTitle(names.getString("recoverPasswordTitle"));
		setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

		JLabel tempLabel;
		String[] labelText = { "logInuserID", "recoverSecretQuestion", "recoverSecretAnswer", "result"};

		for(int i=0 ; i<labelText.length ; i++)
		{
			tempLabel = new JLabel(names.getString(labelText[i]));
			tempLabel.setBounds(10,10+ i*25 ,120,20);
			contentPane.add(tempLabel);
		}

		for(int i=0 ; i < inputTextFields.length ; i++)
		{
			inputTextFields[i] = new JTextField();
			inputTextFields[i].setBounds(150,10+ i*25,150,20);
			contentPane.add(inputTextFields[i]);
		}

		resultLabel = new JLabel("Nothing here and remove");
		resultLabel.setBounds(150,85,150,20);
		contentPane.add(resultLabel);

		String[] buttonNames ={ "recover", "cancel"};

		for (int i = 0 ; i < buttonNames.length ; i++)
		{
			buttons[i] =new JButton(names.getString(buttonNames[i]));
			buttons[i].setMnemonic(minimonics.getString(buttonNames[i]).charAt(0));
			buttons[i].setBounds(20+ i* 135, 120,130,25);
			contentPane.add(buttons[i]);
		}


		// Action of the Ok button...
    }

}
