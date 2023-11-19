/** 
 * FILE NAME: GamePanels.java
 * WHO: Cody Wan and Kealani Finegan
 * WHAT: Implements a class creating all panels for the the GameGUI (UserInfoPanel, GamePanel, and ScoreBoardPanel)
 * FOR: CS230 Fall 2017 Final Project
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Iterator;

public class GamePanels extends JPanel {
    private Game game; //create a game instance
    private User user; //create a user instance
    private JTabbedPane main; //for the different tabs

    //set to global var so they can be updated across different tabs
    private JLabel playAgain;
    private JLabel scores;
    private boolean gameOn;
    private JLabel answer;
    private JLabel points;


    /**
     * constructor
     * Creates an new gamePanel that takes instance of a game and sets up all tabs for the GUI
     */
    public GamePanels(Game g) {
        this.game = g;
        this.user = new User();
        main = new JTabbedPane();

        //Instantiate these with initial values
        playAgain = new JLabel();
        scores = new JLabel(this.game.toString());
        points = new JLabel("<html>Your points will<br></br>appear here<br></br></html>");
        answer = new JLabel("<html>Log in on the user panel and<br></br> join the game!</html>");
        gameOn = false;

        //add tabs to main container
        main.addTab("User Info", new UserInfoPanel());
        main.addTab("Game", new GamePanel());
        main.addTab("Score Board", new ScoreBoardPanel());
        setLayout(new BorderLayout(5, 5)); //border layout with 5 pixels between components
        add(main);
    }

    /**
     * Represents the panel where the user inputs their infomrmation (username and birthyear)
     */
    private class UserInfoPanel extends JPanel {
        private JPanel north;
        private JLabel name;
        private JLabel birthday;
        private JTextField name_text;
        private JTextField bday_text;
        private JButton submit;
        private JPanel south;
        private JLabel display;

        /**
         * constructor
         * Creates an new UserInfoPanel and adds necessary components (text fields for input,
         * a button to submit, and labels for directions
         */
        private UserInfoPanel() {
            north = new JPanel();
            name = new JLabel("Name: ");
            birthday = new JLabel("Birthday: ");
            name_text = new JTextField(15);
            bday_text = new JTextField(15);
            submit = new JButton("Submit");
            south = new JPanel();
            display = new JLabel("<html>This is 2D Color Game. <br></br>" +
                    "Enter your information above to create a profile and compete against your friends!</html>");

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //used for vertical alignment of the north and south panels
            setBackground(Color.WHITE);
            add(northPanel());
            add(southPanel());

        }

        /**
         * Represents the area where the user enters their name and birthday/year to begin game
         */
        private JPanel northPanel() {
            submit.addActionListener(new Listener());
            north.setBackground(Color.white);
            north.add(name);
            north.add(name_text);
            north.add(birthday);
            north.add(bday_text);
            north.add(submit);
            return north;
        }

        /**
         * Represents the area where directions appear
         * After the user enters their credentials, the text is updated to welcome the player and give further instructions
         */
        private JPanel southPanel() {
            south.setBackground(Color.white);
            south.add(display);
            return south;
        }


        /**
         * Listener for the submit button
         * Clears any previous game stats related to last user, adds new user to the socreboard, and
         * updates text appropriately
         */
        private class Listener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submit) {
                    gameOn = true;
                    user.setNameBday(name_text.getText() + " " + bday_text.getText());
                    game.storeRecord(user); //stores user in game hastable with initial values
                    user.newGame();
                    user.setPoints(0);
                    game.newGame();
                    answer.setText("<html>Click on 'Begin' button <br></br>to start the game!<br></br>" +
                            "Press buttons on the top right<br></br> to enter your answer<br></br>" +
                            "</html>");
                    display.setText("<html>Welcome, " + user.getNameBday() + "<br>" +
                            "<br>" +
                            "The Game is about to begin; go to Game tab and let's get started. Good Luck! </html>");
                    playAgain.setText("<html>Hi, " + user.getNameBday() + "<br></br>" +
                            "'undo': undo your<br></br>last input<br></br>" +
                            "'play again': replay <br></br>the sequence (costs 10 points) </html>");
                    points.setText("<html>Your points: " + user.getPoints() + "<br></br></html>");
                }
            }
        }
    }

    /**
     * Represents the panel where the game play will occur
     */
    private class GamePanel extends JPanel {
        private int nROW = game.getNUM_COLOR_BLOCK() / 2 + 1;
        private int nCOL = 4;
        private int count = 0;

        private JButton undo;
        private JButton submit;
        private JButton begin;
        private JButton play_again;

        private JPanel[][] panelHolder;
        private BufferedImage[] images;
        private BufferedImage[] colors;
        private JButton[] colorButtons;
        private JLabel[] colorLabel;

        private int timerCount;
        private int timerCount_pre;
        private boolean timerBegin;
        private boolean started;
        private boolean ifShowing;
        private Timer timer;
        private Iterator<Integer> iter;


        /**
         * constructor
         * Creates an new GamePanel and adds necessary components (button to enter answers, images
         * to show pattern, and labels for directions and current player stats
         */
        private GamePanel() {

            int nColorROW = nROW - 1;
            int nColorCOL = nCOL / 2;
            int nButtonROW = nROW - 1;

            undo = new JButton("Undo");
            submit = new JButton("Submit");
            begin = new JButton("Begin");
            play_again = new JButton("Play Again");
            images = new BufferedImage[4];
            colors = new BufferedImage[game.getNUM_COLOR_BLOCK()];
            colorLabel = new JLabel[game.getNUM_COLOR_BLOCK()];
            colorButtons = new JButton[game.getNUM_COLOR_BLOCK()];
            timerCount = 0;
            timerBegin = true;
            ifShowing = false;

            panelHolder = new JPanel[nROW][nCOL];
            setLayout(new GridLayout(nROW, nCOL)); //give gridlayout specific indexing so they can be referenced later
            playAgain.setHorizontalAlignment(SwingConstants.LEFT);
            answer.setHorizontalAlignment(SwingConstants.LEFT);

            try {
                images[0] = ImageIO.read(new File("P1.jpg"));
                images[1] = ImageIO.read(new File("P2.jpg"));
                images[2] = ImageIO.read(new File("P3.jpg"));
                images[3] = ImageIO.read(new File("P4.jpg"));

                colors[0] = ImageIO.read(new File("RED.jpeg"));
                colors[1] = ImageIO.read(new File("GREEN.png"));
                colors[2] = ImageIO.read(new File("BLUE.jpg"));
                colors[3] = ImageIO.read(new File("YELLOW.png"));

                colorLabel[0] = new JLabel(new ImageIcon(colors[0]));
                colorLabel[1] = new JLabel(new ImageIcon(colors[1]));
                colorLabel[2] = new JLabel(new ImageIcon(colors[2]));
                colorLabel[3] = new JLabel(new ImageIcon(colors[3]));

            } catch (IOException e) {
                System.out.println(e.toString());
            }

            // initialize panelHolders on which we later place color blocks and buttons
            for (int m = 0; m < nROW; m++) {
                for (int n = 0; n < nCOL; n++) {
                    panelHolder[m][n] = new JPanel();
                    add(panelHolder[m][n]);
                }
            }

            // place colors blocks on panels
            for(int i = 0; i < nColorROW; i++) {
                for (int j = 0; j < nColorCOL; j++) {
                    panelHolder[i][j].add(colorLabel[count]);
                    count++;
                }
            }

            // initialize color buttons
            count = 0;
            for(int i = 0; i < game.getNUM_COLOR_BLOCK(); i++)
            {
                colorButtons[i] = new JButton(game.getColors()[i]);
                colorButtons[i].setPreferredSize(new Dimension(200,200));
                colorButtons[i].addActionListener(new Listener());
            }
            // place color buttons on panels
            for(int i = 0; i < nButtonROW; i++) {
                for (int j = nColorCOL; j < nCOL; j++) {
                    panelHolder[i][j].add(colorButtons[count]);
                    count++;
                }
            }

            // place other buttons on the panel
            panelHolder[2][0].add(answer);
            panelHolder[2][1].add(points);
            panelHolder[2][1].add(playAgain);

            panelHolder[2][2].add(begin);
            panelHolder[2][2].add(play_again);

            panelHolder[2][3].add(undo);
            panelHolder[2][3].add(submit);

            //add action listeners for functionality
            begin.addActionListener(new Listener());
            submit.addActionListener(new Listener());
            play_again.addActionListener(new Listener());
            undo.addActionListener(new Listener());


            // we use Timer to implement displaying a sequence of image through GUI
            // say given a iterator [0,1,2,3] which refers to the color block in the order of RED , GREEN, BLUE, YELLOW,
            // we want to replace each color block by some image from images[] and after 800 mil-seconds, we replace
            // image with the color block's original color. We do this for every color block given in [0,1,2,3] and in
            // the given order, creating an 'illusion' that a sequence of image has be shown through GUI.
            timer = new Timer(800, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (iter.hasNext()) { // if there's more items left in the iterator
                        ifShowing = true;
                        timerCount = iter.next();
                        System.out.println(timerCount);

                        if (timerBegin) {
                            // replace color with an image
                            colorLabel[timerCount].setIcon(new ImageIcon(images[timerCount]));
                            timerBegin = false;
                            timerCount_pre = timerCount;
                        }

                        if (timerCount != timerCount_pre) {
                            // replace the image at previous round back with color
                            colorLabel[timerCount_pre].setIcon(new ImageIcon(colors[timerCount_pre]));
                            // replace the new color block with image
                            colorLabel[timerCount].setIcon(new ImageIcon(images[timerCount]));
                            timerCount_pre = timerCount;
                        }
                    } else {
                        // replace the image at previous round back with color; since this is the last color block to be
                        // changed, no need to replace any other color block with image
                        colorLabel[timerCount_pre].setIcon(new ImageIcon(colors[timerCount_pre]));
                        timerBegin = true;
                        timer.stop();
                        ifShowing = false;
                    }
                }
            });
        }

        /**
         * The listener for the various buttons
         */
        private class Listener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == begin && gameOn) { //upon clicking begin and if gameOn is true, pattern is generated
                    game.generatePattern(game.getPattern_size());
                    iter = game.getPattern().iterator();
                    timer.start();
                    started = true;
                }
                if (e.getSource() == play_again && gameOn && started) //if already started and you have enough points, can replay pattern
                {
                    System.out.println("Play Again");
                    try {
                        user.usePlayAgain();
                        game.storeRecord(user); //update user stats
                        points.setText("<html>Your points: " + user.getPoints() + "<br></br></html>");
                        iter = game.getPattern().iterator();
                        timer.start();
                    } catch (RuntimeException er) {
                        //can only use replay if you have enough points
                        points.setText("<html>Your points: " + user.getPoints() + "<br></br>" +
                                "Sorry you don't have<br></br>enough points to<br></br>use 'play again'!</html>");
                        System.out.println(er.toString());
                    }
                }

                //if user clicks buttons, corresponding values added to screen and to user's answer stack
                for(int i = 0; i < colorButtons.length ; i++)
                {
                    if(e.getSource() == colorButtons[i] && gameOn && !ifShowing)
                    {
                        user.enterAnswer(i);
                        answer.setText(user.AnswerToString());
                    }
                }
                //undo button allows user to pop answers off stack (delete their previous move)
                if (e.getSource() == undo && gameOn) {
                    try {
                        user.undo();
                        answer.setText(user.AnswerToString());
                    } catch (EmptyStackException er) { // handle the situation where there's no more elements to pop
                    }
                }

                //if game is able to be played, check to see if entered answer is correct and continue; stop otherwise
                if (e.getSource() == submit && gameOn && started) {
                    gameOn = false;
                    started = false;
                    if (game.ifContinue(user.getAnswer())) {
                        user.pointsUp(); //increase points
                        game.storeRecord(user); //update user stats
                        System.out.println(game.toString()); //printout stats for testing purposes
                        //update text
                        answer.setText("<html>That is CORRECT!<br></br>hit begin and" +
                                "<br></br>move on to next level!</html>");
                        points.setText("<html>Your points: " + user.getPoints() + "<br></br></html>");
                        //start new round
                        user.newGame();
                        game.nextLevel();
                        gameOn = true;
                    } else {
                        game.storeRecord(user); //update user stats
                        System.out.println(user.getPoints());
                        //update to different text if user loses and add user and new stats to scoreboard
                        answer.setText("<html>Sorry that is not correct<br></br>thank you for playing~</html>");
                        points.setText("<html>Your points: " + user.getPoints() + "<br></br></html>");
                        System.out.println(game.toString());
                        String ranking = (game.toString()).replaceAll("(\r\n|\n)", "<br/>"); //replace newline regex with html break
                        scores.setText("<html>" + ranking + "</html>");
                        gameOn = false;
                    }
                }
            }
        }
    }

    /**
     * Represents the panel where the scoreboard will display all users and corresponding scores
     */
    private class ScoreBoardPanel extends JPanel {
        private JPanel titlePanel, scoresPanel, searchPanel; //three panels to display different information
        private JLabel userName, searchResult; //labels for searchPanel
        private JTextField searchName; //text field for searching up a user
        private JButton search; //button for search

        private ScoreBoardPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(800, 600));
            titlePanel = new JPanel(); //displays the title of the game
            scoresPanel = new JPanel(); //displays the users and scores
            searchPanel = new JPanel(); //allows user to look up other player's usernames and scores

            titlePanel.setBackground(Color.white);
            scoresPanel.setBackground(Color.white);
            searchPanel.setBackground(Color.white);
      
      /*set to exact sizing of the screen on default opening, able to re-size although past a
       * certain larger dimension panels take up equal space to fill frame
       */
            titlePanel.setPreferredSize(new Dimension(800, 100));
            scoresPanel.setPreferredSize(new Dimension(800, 400));
            searchPanel.setPreferredSize(new Dimension(800, 100));
            scoresPanel.setBorder(BorderFactory.createLineBorder(Color.black));

            JLabel l1 = new JLabel("<html>2D COLOR GAME<br></br>Player Scores</html>");
            titlePanel.add(l1);

            scoresPanel.add(scores);

            userName = new JLabel("Search for a user: ");
            searchName = new JTextField(15);
            search = new JButton("Search");
            search.addActionListener(new Listener());
            searchResult = new JLabel("<html>Search result will appear here</html>");
            searchPanel.add(userName);
            searchPanel.add(searchName);
            searchPanel.add(search);
            searchPanel.add(searchResult);

            //add all panels to main scoreboard panel
            add(titlePanel);
            add(scoresPanel);
            add(searchPanel);
        }

        /*
         * Listener for search button
         * If username is found, update the searchResult label with found user's information,
         * otherwise set text to alert user that their search was unsucessful
         */
        private class Listener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == search) {
                    String user = searchName.getText();
                    if (game.getScoreboard().containsKey(user)) {
                        searchResult.setText("<html> Found! <br></br> Username: " + user + "   Score: " + game.getScoreboard().get(user) + "</html>");
                    } else {
                        searchResult.setText("<html>User not found</html>");
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
    }
}