/**
 * @(#)MainProgram.java
 * @Ayaan,Wilson,Noah
 * @version 1.00 2018/4/6
 */
 
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;
import java.util.Scanner;
import java.awt.event.*;
import java.io.*;


public class MainProgram
{
	private JFrame frame;
	private JPanel background;
	private Training training;
	private MenuScreen menu;
	private Player p;
	private Random r;// Random Generator to do Random Damage to Dummy
	private int pressTime1,pressTime2;//These variables store the millisecond press times of two pairs of Z Keys
	private String key1,key2;// These variables store the String Value of the Z or X Key Press
    private Queue<String> combos;// This queue stores two Values ("Z" and "Z")
    private static Character lastKey = null;// This Character variable stores the last key
                                            // press so that two keys may not be in effect at same time
    private int count;
	TreeMap<Integer, String> tmap;
	HighScoreMenu hsmenu;
    /**
     * Constructor: Creates a new instance of a a main menu and combines all components
     */
    public MainProgram() 
    {
    	count = 0;
    	tmap = new TreeMap<Integer, String>();
    	frame = new JFrame();
    	menu = new MenuScreen();
    	background = new JPanel();
    	p= new Player();
    	training = new Training();
        initFrame(frame);
        pressTime1=0; pressTime2=0;
        key1="";key2="";
        playListener();
        playerMovementListener(frame);
        playerAnimationListener(frame);
        BackgroundSelectListener(frame);
        combos = new LinkedList<>();
        combos.add("Z");combos.add("Z");
        r= new Random();
        
        try
    	{
    		Scanner scr = new Scanner(new File("HighScores.txt"));
	    	while(scr.hasNextLine())
	    	{
	    		tmap.put(scr.nextInt(),scr.next());
	    	}
    	}
    	catch(Exception e){	}
    	
    	Integer[] scores = tmap.keySet().toArray(new Integer[tmap.keySet().size()]);

	}
	
    /**
     * Adds an action listener to play button to start game
     */
	public void playListener()
    {
        menu.play.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startGame();
            }
        });
    }
	
    /**
     * This method creates the primary display frame as well as adds a listener to quit
     * @param f A JFrame defined as instance
     */
	public void initFrame(JFrame f)
    {
       f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       f.setExtendedState(Frame.MAXIMIZED_BOTH);
       f.setUndecorated(true);
       f.add(initBackground(background));
       f.setVisible(true);
       //Anytime exc pressed exits game
       f.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    System.exit(0);

                }
            }
        });
    }

    /**
     * This method is created as an alternative to move player with key listener on right and left key
     * This method will increment to the player class which will modify the training class which will be called from here
     * @param f JFrame The current frame in which to set the key listener
     */
    public void playerMovementListener(JFrame f)
    {
        f.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    //if the right arrow key is pressed, increment x 20 pixels to right, set right facing booleans to true
                    training.addPosX(20);
                    training.rightFacing=true;
                    training.leftFacing=false;
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    //if the right arrow key is pressed, increment x 20 pixels to left, set left facing booleans to true
                    training.minusPosX(20);
                    training.rightFacing=false;
                    training.leftFacing=true;
                }
                checkPlayerBounds();// If a player goes over a certain x boundary,
                                    //the player is wrapped around from the other side of screen
                }
        });

    }

    /**
     * Method controls player punch and kicks also calls dummy animation methods
     * @param f The active JFrame for animation
     */
    public void playerAnimationListener(JFrame f)
    {
        //This method deals with Z and X actions
        f.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_Z)
                {
                    if (lastKey == null || lastKey != e.getKeyChar())
                    {
                        lastKey = e.getKeyChar();
                        //The above two lines deal with setting a key variable to make sure you cannot hold key and get actions
                        training.punch=true;//Z was pressed so punch boolean is activated initiating a punch
                        checkDummyDist();//Decides whether to play a whoosh punch or impact punch depending on dummy distance
                        if(!key1.equals("")&&key2.length()==0) key2+="Z";//If key1 has a value add Z to key2
                        else if(key1.equals("")) key1+="Z";// If key1 doesnt have value add value to it
                        // JOptionPane.showMessageDialog(frame,key1+" "+key2);
                        if(key1.length()==1&&key2.length()==1)//If both key1 and key 2 have values calculate the ms diff with method
                            calculateCombos();
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_X)
                {
                    if (lastKey == null || lastKey != e.getKeyChar())
                    {
                        lastKey = e.getKeyChar();
                        //The above two lines deal with setting a key variable to make sure you cannot hold key and get actions
                        training.kick=true;//X was pressed so kick boolean is true
                        checkDummyDist();//Decides whether to play a whoosh punch or impact punch depending on dummy distance
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    training.jump=true;//If Spacebar pressed jump boolean set true
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    training.health=900;//If Enter pressed the health of the training dummy is restored to a full (900)
                }

            }
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_Z)
                {
                    lastKey=null;//When Z Key is released set the last pressed key to null so new action can be taken
                    training.punch=false;//Sets the punch boolean to false stopping punch actions when Z released
                    training.setCombo(false);// When Z key is released combo is set to false to stop combo
                    training.dummyHit=false; //Sets dummy back to non hit version when punch stopped
                  if(training.win)
                    	endGame();

                }
                if(e.getKeyCode() == KeyEvent.VK_X)
                {
                    lastKey=null;//When X key is released last pressed key set to null so new action can be taken
                    training.kick=false;// Sets the kick boolean to false stopping kick action when x released
                    training.dummyHit=false;// Sets the dummy back to normal as not being hit once attack cancelled
                    if(training.win)
                    {
                    	endGame();
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    lastKey=null;// When space is released clears last pressed key so new action can be taken again
                    training.jump=false;// Sets jump boolean to false leaving character in normal position
                }
            }
        });
    }

    /**
     * This listener selects a different background, as well as calls the music changing method for each level
     * @param f The actice Jframe on which to animate
     */
    public void BackgroundSelectListener(JFrame f)
    {
        f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //If numbers pressed the frame changes levels (different music and backgrounds)
                if (e.getKeyCode() == KeyEvent.VK_4)
                {
                    changeLevel("Training.jpg","trainingMusic.wav");
                }
                if (e.getKeyCode() == KeyEvent.VK_1)
                {
                    changeLevel("Final-Destination.gif","Wolfborn.wav");
                }
                if (e.getKeyCode() == KeyEvent.VK_2)
                {
                    changeLevel("SpaceRoom.png","narutoMusic.wav");
                }
                if (e.getKeyCode() == KeyEvent.VK_3)
                {
                    changeLevel("bossroom.jpg","bossmusic.wav");
                }
            }
        });
    }

    /**
     * This method changes the background image and music playing of the frame
     * @param background The string name of the background image
     * @param music The string ame of the .wav music file
     */
    public void changeLevel(String background, String music)
    {
        training.setBackground(background);
        training.playing=true;//When playing=true, any previously running music stopped
        training.playTrainingMusic(music);
        training.playing=false;// Since new music is running no need to stop anything so runs until stopped manually later
    }

    /**
     * This method Checks the distance of player from dummy and decides whether to play an impact sound,
     * empty swoosh, or to subtract from enemy life based on the distance to choose whether hit or not
     */
    public void checkDummyDist()
    {
        if(training.pingX()>75&&training.pingX()<900) {//for the dummy to be hit and for impact sounds dummy distance defines
            //controls whether there will be a hit image or a hard punch or kick
            training.dummyHit=true;
            if(training.punch)
            {
                training.playSound("hardPunch.wav");
               int damage=r.nextInt(10);// Generates random damage from 1-10
                training.subtractHealth(damage);// The health is subtracted by the random damage
            }
            if(training.kick)
            {
                training.playSound("hardkick.wav");
                int damage=r.nextInt(10);//Generates random damage from 1-10
                training.subtractHealth(damage);//health is subtracted by random damage
            }
        }
        //Otherwise if not at the required distance to make an impact the dummy will not be shown as hit and will
        //alternate between light sounds depending on punch or kick
        if(training.punch)
            training.playSound("punch.wav");
        if(training.kick)
            training.playSound("emptykick.wav");
       // else
            //training.dummyHit=false;
    }

    /**
     * This method adds the two parts of the main menu to one displayed panel
     * @param panel A background JPanel
     * @return JPanel A JPanel with buttons and background
     */
	public JPanel initBackground(JPanel panel)
    {
       panel.setBackground(Color.BLACK);
       panel.add(menu.buttons(), BorderLayout.NORTH);
       panel.add(menu.background(), BorderLayout.CENTER);
       return panel;
    }
	
	/**
	 * This method removes the menu screen and adds the first room into view
	 */
	public void startGame()
    {
    	frame.remove(background);
    	frame.add(training);
    	training.playTrainingMusic("trainingMusic.wav");
    	frame.revalidate();
    }

    /**
     * This method checks the x value of player and if over, wraps player position back to 0
     */
    public void checkPlayerBounds()
    {
        if(training.pingX()>1100||training.pingX()<0)//checks the current X values of player on screen
        {
            training.setPlayerDefault();//If outside of accepted range the player is automatically redrawn at the inital point
        }
    }

    /**
     * This method calculates the difference between two tick values
     * and if key pressed close enough activates combo procedure
     */
    public void calculateCombos()
    {
        int diff=0;
        if(pressTime1!=0) pressTime2= training.getTick();//If there is a value for first press add ms time to press 2
        if(pressTime1==0) pressTime1= training.getTick();// If there is no value for the first press add ms time to press 1
        if(pressTime1!=0&&pressTime2!=0)// If press1 and press2 have values calculate a difference and set the times back to 0
        {
           diff = pressTime2 - pressTime1;// gets millisecond difference between two key presses
           pressTime1=0;pressTime2=0;
        }
        System.out.println("Difference: "+diff);
        if(diff>0&&diff<10)checkInCombo(key1,key2);// If the millisecond difference is within 100 ms of each other registers as a valid combo time
            //then need to see if proper combo that is in queue (ie. ZX is not valid even with 100 ms diff while ZZ is bc in queue)

            //System.out.println("combo");
    }

    /**
     * This method checks to see whether all the values match up with the queue and initiates combo state on
     * @param ke1 String The first key press
     * @param ke2 String The second user key press
     */
    public void checkInCombo(String ke1, String ke2)
    {
        String k1=combos.remove();
        String k2=combos.remove();
        // k1 and k2 hold the values of keys in combo which are valid combo pairs (should be ZZ)
        if(k1.equals(ke1)&&k2.equals(ke2))
        {//If the keys pressed fed into this method are ZZ they equal k1 and k2 which should be ZZ as well
            training.setCombo(true);
            key1 = key1.replace(key1, "");// key1 is set back to blank
            key2 = key2.replace(key2, "");// key2 is set back to blank
            count++;
           // System.out.println("combo");
        }
        combos.add("Z");
        combos.add("Z");
        //queue is refilled with the Z values
    }

    /**
     * When the dummy is no longer on screen the highscore menu prompt appears
     */
	public void endGame()
    {
	   	hsmenu = new HighScoreMenu();

	   	String name = JOptionPane.showInputDialog(frame,"Enter Name", "Name");
	   	
	   	tmap.put(count, name);
	    //Using a treemap scores are put into it to get the highest one out in order
	    Integer[] scores = tmap.keySet().toArray(new Integer[tmap.keySet().size()]);
	    if(scores.length > 0)
	    {
	    	hsmenu.first = " " + tmap.get(scores[scores.length - 1]) + " " + scores[scores.length - 1];
	    }
	    if(scores.length > 1)
	    {
	    	hsmenu.second = " " + tmap.get(scores[scores.length - 2]) + " " + scores[scores.length - 2];
	    }
	    if(scores.length > 2)
	    {
	    	hsmenu.third = " " + tmap.get(scores[scores.length - 3]) + " " + scores[scores.length - 3];
	    }
	    
	    try//prints to file to save the attempts
	    {
	    	PrintStream out = new PrintStream(new FileOutputStream("HighScores.txt"));
			System.setOut(out);
			for(int i = 0; i < tmap.size(); i++)
			{
				System.out.println(scores[i] + " " + tmap.get(scores[i]));
				
			}
	    }
	    catch(Exception e){	}
        highscoreFrame();
    }

    /**
     * Creates a highscore frame to display scores on
     */
    public void highscoreFrame() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
        f.setUndecorated(true);

        f.add(hsmenu);
        f.revalidate();
        f.setVisible(true);
       highscoreExitListener(f);
        }

    /**
     * Adds an action listener for highscore menu
     * @param f The frame to add listener on
     */
    public void highscoreExitListener(JFrame f)
        {
            f.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                        f.setVisible(false);
                    count=0;
                }
            });
        }

	/**
	 * This is the main method that initializes the main menu class
	 * @param args String array
	 */
	public static void main(String[] args)
    {
    	MainProgram test = new MainProgram();
    }
}
