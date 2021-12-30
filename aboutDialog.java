import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class aboutDialog extends JDialog
{
	JButton okButton;
	public aboutDialog(String language)
	{
		ResourceBundle names  = ResourceBundle.getBundle("Names", new Locale(language));
		ResourceBundle minimonics  = ResourceBundle.getBundle("Minimonics", new Locale(language));

		setBounds(100,100,295,285);
		setTitle(names.getString("aboutDialog"));
		setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        aboutPanel about = new aboutPanel();
		about.setBounds(5, 10, 250, 215);
		contentPane.add(about);

		okButton = new JButton(names.getString("ok"));
		okButton.setMnemonic(minimonics.getString("ok").charAt(0));
		okButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							hide();
						}});

		okButton.setBounds(95, 225, 100, 25);
		contentPane.add(okButton);
		show();
	}
}
class aboutPanel extends JPanel
{
	private Font f, f1,f2;
	public void paintComponent(Graphics g)
	{
	   super.paintComponent(g);
	   f2=new Font("Verdana",Font.BOLD+Font.ITALIC, 22);
	   f=new Font("SansSerif", Font.BOLD, 14);
	   f1=new Font("Trebusbi", Font.PLAIN,12);
	   g.setColor(Color.red);
	   g.drawRect(30,15,215,35);
	   g.setFont(f2);
	   g.setColor(Color.blue);
	   g.drawString("Chat Application", 35, 40);
	   g.setColor(Color.blue);
	   g.drawLine(25,60,265,60);
	   g.setFont(f);
	   g.setColor(Color.black);
	   g.drawString("By : Polla & Omed", 75, 80);
	   g.setFont(f1);
	   g.drawString("Designed for - Final year project", 50, 100);
	   g.drawString("Copyright - 2004 : Soft - Eng", 60,115);
	   g.setColor(Color.blue);
	   g.drawLine(25,125,265,125);
	   g.setColor(Color.black);
	   g.setFont(f);
	   g.drawString("Kurdistan Region - Erbil", 60,145);
	   g.drawString("University Of Salahaddin",58,165);
	   g.drawString("Engineering College",71,180);
	   g.drawString("Software Department",69,195);
	   g.setColor(Color.blue);
	   g.drawLine(25,205,265,205);
	}
}