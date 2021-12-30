import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


public class UserInfoDialog extends JDialog
{
	private JButton okButton, cancelButton;
	private JTextField []data = new JTextField[4];
	private final PrintWriter out;
    public UserInfoDialog(String vals[], String language, boolean state, PrintWriter o)
    {
		out = o;
		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

		setBounds(100,100,320,190);
		setTitle(names.getString("userinfoDialog"));
		setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
		String labelStr[] = { "nickName", "infoBirthYear", "location", "infoGender"};
		JLabel label ;
		for (int i = 0 ; i < labelStr.length ; i++)
		{
			label = new JLabel(names.getString(labelStr[i]));
			label.setBounds(10, 25 * i+ 10, 135, 20);
			contentPane.add(label);

		}

		for (int i = 0 ; i < data.length ; i++)
		{
			data[i] = new JTextField(20);
			data[i].setBounds(150, 25 * i+ 10,150,20);
			data[i].setEditable(state);
			data[i].setText(vals[i]);
			contentPane.add(data[i]);
		}

		okButton = new JButton(names.getString("update"));
		okButton.setMnemonic(minimonics.getString("update").charAt(0));
		okButton.setBounds(45, 130,110,25);
		okButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
				{
					out.println("05" + data[0].getText() + ";" +data[1].getText() + ";" +data[2].getText() + ";" +data[3].getText());
					hide();
				}});
		contentPane.add(okButton);
		okButton.setEnabled(state);

		// Action of the Ok button...

		cancelButton = new JButton(names.getString("close"));
		cancelButton.setMnemonic(minimonics.getString("close").charAt(0));
		cancelButton.setBounds(160, 130,110,25);
		cancelButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){hide();}});
		contentPane.add(cancelButton);
		setVisible(true);

    }//end of constructer
}
