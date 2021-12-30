import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class OfflineMessageDialog extends JDialog
{
	JEditorPane editorPane;
    public OfflineMessageDialog(String language)
    {
		ResourceBundle names  = ResourceBundle.getBundle("Names",new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics",new Locale(language));

 		Container contentPane = getContentPane();
        contentPane.setLayout(null);


		setBounds(100,100,400,380);
		setTitle(names.getString("offlineMessageDialog"));
		setResizable(false);

		JButton button = new JButton(names.getString("ok"));
		button.setMnemonic(minimonics.getString("ok").charAt(0));
		button.setBounds(150, 320, 100,25);
		contentPane.add(button);
		button.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
															{
																hide();
															}
														});
		editorPane = new JEditorPane();
		JScrollPane scroll = new JScrollPane(editorPane);
		scroll.setBounds( 10, 10,375 ,300 );
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll);
    }
	public void show(String str[])
	{
		String temp = "";
		for (int i = 0; i  < str.length ; i++)
			temp += str[i] + "\n";
		editorPane.setText(temp);
		setVisible(true);
	}

}
