/**
 * @(#)MenuScreen.java
 *
 *
 * @Ayaan,Wilson,Noah
 * @version 1.00 2018/5/2
 */

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.applet.*;

public class MenuScreen implements ActionListener
{

	JPanel buttonPanel, backgroundPanel;
	public JButton play, quit, art;
	public MenuScreen(){}

    /**
     * Sets a play and quit button onto one final button panel Component 1/2 of final menu panel
     * @return JPanel A Panel with incorporated JButtons
     */
	public JPanel buttons()
	{
		buttonPanel = new JPanel();
		play = new JButton("P l a y");
		quit = new JButton("Q u i t");
		art = new JButton("A R T");
		art.addActionListener(new ActionListener ()
	 	{
	 		public void actionPerformed(ActionEvent event)
		 	{
		 		JFrame f = new JFrame();
		 		second s = new second();
		 		f.add(s);
		 		f.setVisible(true);
		 		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 		f.setSize(2500,2500);
		 	}
		 });
	    initButton(play);
	    initButton(quit);
	    initButton(art);
		quit.addActionListener(this);//see code block below
		return buttonPanel;
	}

    /**
     * This method initializes and defines part of the properties of the buttons
     * @param button A JButton to be added to menu
     */
	public void initButton(JButton button)
    {
        buttonPanel.setBackground(Color.BLACK);
        Font font = new Font("Monospaced", 100, 100);
        button.setSize(500,500);
        button.setFont(font);
        button.setFocusable(false);
        buttonPanel.add(button);
    }

    /**
     * Incorporates the JLabel from other method into one final main menu picture panel
     * @return JPanel A Panel with 2 integrated JLabels (Image/Title)
     */
	public JPanel background()
	{
		//initialize
		backgroundPanel = new JPanel();
		backgroundPanel.add(mainBackground());
		return backgroundPanel;
	}


    /**
     * This method sets a central main menu image as well as title in custom font
     * @return JLabel returns a JLabel with an integrated picture and title
     */
	public JLabel mainBackground()
	{
		JLabel label=new JLabel(new ImageIcon("backgrounds.gif"));
		label.setLayout(new BorderLayout());
		JLabel labeltext = new JLabel("Reptar Returns");
		Font font = new Font("Papyrus",200,200);
		labeltext.setFont(font);
		labeltext.setForeground(Color.WHITE);
		labeltext.setHorizontalAlignment(JLabel.CENTER);
		label.add(labeltext);
	return label;
	}

    //Sets a key listener for quit on esc press
    @Override
    public void actionPerformed(ActionEvent e)
    {
		if(e.getSource() == quit)
        {

            System.exit(0);
        }
    }

}



