/**
 * @(#)HighScoreMenu.java
 *
 *
 * @author 
 * @version 1.i00 2018/4/24
 */
import java.awt.*;
import javax.swing.*;

public class HighScoreMenu extends JPanel
{

	public String first, second, third;
	public JPanel panel;

	/**
	 * Constructor that sets three possible positions as highscores
	 */
    public HighScoreMenu() 
    {
    	first = "______";
		second = "______";
		third = "______";
		panel = new JPanel();
		repaint();
    }

	/**
	 * This method calls the repaint and returns highscore panel
	 * @return JPanel The highscore panel
	 */
	public JPanel c()
	{		
		repaint();
		return panel;
	}

	/**
	 * Pain method that decides the look of highscore screen
	 * @param g The graphics component to paint with
	 */
	public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	g.setFont(new Font("Monospaced", Font.PLAIN, 90));
    	g.setColor(Color.BLUE);
    	g.drawString(first, 300, 400);
    	g.drawString(second, 300, 500);
    	g.drawString(third, 300, 600);
    }
}