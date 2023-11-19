/** 
 * FILE NAME: Game.java
 * WHO: Cody Wan and Kealani Finegan
 * WHAT: Implements a class representing the 2D color game
 *       Game class contains methods that control the flow of the game; it includes: generatePattern() that generates a
 *       random sequence of color, ifContinue() that checks user answer and determine if the game continues or not, and
 *       storeRecord() that store user score after the game is over
 * FOR: CS230 Fall 2017 Final Project
 */

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.Set;


public class Game {
    private final int STARTING_PATTERN_SIZE = 3;
    private final int MAX_PATTERN_SIZE = 8;
    private final int NUM_COLOR_BLOCK = 4;
    private final String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"};
    private LinkedList<Integer> Pattern; //pattern of colors/images
    private Hashtable<String, Integer> Scoreboard; //to store the all player's scores
    private int pattern_size; //the size of the pattern (increases per level)

    /**
     * constructor
     * Creates an new game with no parameters, all values empty, and pattern set to initial size of 3
     */
    public Game() {
        this.Pattern = new LinkedList<Integer>();
        this.Scoreboard = new Hashtable<String, Integer>();
        this.pattern_size = STARTING_PATTERN_SIZE;
    }

    /**
     * constructor
     * Greates an new game that takes an intance of a user as a parameter and sets
     * instance variable nUser to the input param. All other values are empty, and
     * pattern set to initial size of 3
     *
     * @param user instance of a user
     */
    public Game(User user) {
        this.Pattern = new LinkedList<Integer>();
        this.Scoreboard = new Hashtable<String, Integer>();
        this.pattern_size = STARTING_PATTERN_SIZE;
    }

    /**
     * Gets the sequence of colors/images from Pattern
     *
     * @return Returns the current pattern of the game
     */
    public LinkedList<Integer> getPattern() {
        return Pattern;
    }

    /**
     * Gets size/length of pattern
     *
     * @return Returns the size of the pattern
     */
    public int getPattern_size() {
        return pattern_size;
    }

    /**
     * Sets pattern_size to increase as level increases; maximum pattern size is 8
     */
    public void nextLevel() {
        pattern_size = Math.min(MAX_PATTERN_SIZE, pattern_size + 1);
    }

    /**
     * Resets pattern to a new empty LinkedList and the pattern size back to its initial value of 3
     */
    public void newGame() {
        this.Pattern = new LinkedList<Integer>();
        this.pattern_size = STARTING_PATTERN_SIZE;
    }

    /**
     * getter for colors
     * @return an array of String that contain all the colors in the game
     */
    public String[] getColors() {
        return colors;
    }

    /**
     * Generates a random sequence of colors/images, adding them them to the LinkedList (Pattern)
     */
    public void generatePattern(int size) {
        Random rand = new Random();
        Pattern.clear();
        Pattern.add(rand.nextInt(NUM_COLOR_BLOCK)); //adds random number to Pattern
        while (Pattern.size() < size) {
            int num = rand.nextInt(NUM_COLOR_BLOCK);
            if (num != Pattern.peekLast()) //checks that same color/image will not be repeated right after the preceeding one
            {
                Pattern.add(num);
            }
        }
    }

    /**
     * getter for NUM_COLOR_BLOCK
     * @return NUM_COLOR_BLOCK
     */
    public int getNUM_COLOR_BLOCK() {
        return NUM_COLOR_BLOCK;
    }

    /**
     * Checks user's answers against the Pattern. If correct returns true, otherwise false
     *
     * @param userInput The stack that is the user's answers
     * @return boolean Returns true if user answers and Pattern match, otherwise returns false
     */
    public boolean ifContinue(Stack<Integer> userInput) {
        LinkedList<Integer> answer = new LinkedList<Integer>(userInput);
        for (int i = 0; i < Pattern.size(); i++) {
            try {
                if (!Pattern.get(i).equals(answer.get(i)))
                    return false;
            } catch (IndexOutOfBoundsException er) {
                return false;
            }
        }
        return true;
    }

    /**
     * Puts current user into the Hashtable that stores all the usernames and corresponding scores
     *
     * @param user The user that will be stored in the hashtable
     */
    public void storeRecord(User user) {
        Scoreboard.put(user.getNameBday(), user.getPoints());
    }


    /**
     * Returns a String with all info regarding the scores and names of the users
     *
     * @return The String representation of the game stats
     */
    public String toString() {
        String res = "";
        Set<String> keys = Scoreboard.keySet();
        for (String key : keys) {
            res += (key + ": " + Scoreboard.get(key) + "\n");
        }
        return res;
    }

    /**
     * Returns a the Hashtable to allow access to the scoreboard (used in GamePanels class)
     *
     * @return The Hashtable holding the game stats
     */
    public Hashtable<String, Integer> getScoreboard() {
        return Scoreboard;
    }

    public static void main(String[] args) {
        User user1 = new User("Cody 1996");
        Game game = new Game(user1);
    }
}
