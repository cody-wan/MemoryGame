/** 
 * FILE NAME: User.java
 * WHO: Cody Wan and Kealani Finegan
 * WHAT: Implements a class to represent a single user/player in the 2D color game
 * FOR: CS230 Fall 2017 Final Project
 */

import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class User {
    // playAgainCost, pointsWin and colors are constantly used throughout so we make them final to prevent unexpected
    // change and we declare them here so that it's easier to change the value if need be
    private final int playAgainCost = 10;
    private final int pointsWin = 20;
    private final String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"};
    private String NameBday; //user's name
    private int points; //user's points
    private Stack<Integer> Answer; //their answers for one round
    //private Scanner scan;

    /**
     * constructor
     * Creates an new User with no parameters, all values empty/set to zero
     */
    public User() {
        this.NameBday = "null";
        this.points = 0;
        this.Answer = new Stack<Integer>();
        //this.scan = new Scanner(System.in);
    }

    /**
     * constructor
     * creates an new game that takes a String of the user's name as a parameter and sets
     * instance variable NameBday to the input param. All other values are empty/set to zero
     *
     * @param NameBday instance of a user
     */
    public User(String NameBday) {
        this.NameBday = NameBday;
        this.points = 0;
        this.Answer = new Stack<Integer>();
        //this.scan = new Scanner(System.in);
    }

    /**
     * Subtracts 10 points from user's points when called, if points are less than zero an error is thrown
     */
    public void usePlayAgain() {
        this.points -= playAgainCost;
        if (points < 0) {
            points = 0;
            throw new RuntimeException();
        }
    }


    /**
     * Gets the Stack of the user's answers
     *
     * @return Returns the Stack of the user's answers
     */
    public Stack<Integer> getAnswer() {
        return Answer;
    }

    /**
     * Gets the String value of the user's name
     *
     * @return Returns a String of the user's name
     */
    public String getNameBday() {
        return NameBday;
    }

    /**
     * Gets the number of points a user has
     *
     * @return Returns integer that is the points the user has
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets points for the user
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Sets name for the user
     */
    public void setNameBday(String nameBday) {
        NameBday = nameBday;
    }

    /**
     * Given an interger as a parameter, enters given value into Stack of user's answer
     *
     * @param num The value to be added to the user's answer Stack
     */
    public void enterAnswer(int num) {
        Answer.push(num);
    }

    /**
     * Pops/undos the last entered answer
     *
     * @return Stack The Answer stack with the last value removed
     */
    public Integer undo() {
        return Answer.pop();
    }

    /**
     * Increases points by 20
     */
    public void pointsUp() {
        points += pointsWin;
    }

    /**
     * Allows for a new game to be plated by clearing all previous answers from the stack
     */
    public void newGame() {
        Answer.clear();
    }

    /**
     * Returns a String with the current answers of the user, with Strings of color names corresponding
     * to initial numerical value
     *
     * @return The String representation of the user's Answer stack
     */
    public String AnswerToString() {
        Vector<Integer> v = new Vector<Integer>(Answer);
        String s = "<html><br></br>";
        String temp;
        for (int i = 0; i < v.size(); i++) {
            temp = colors[v.elementAt(i)];
            s += temp + " <br></br>";
        }
        s += "</html>";
        return s;
    }

    /**
     * main method with some basic testing to make sure all methods work as they should
     *
     * @param args An array of strings to be passed as command line arguments (but not utilized here)
     */
    public static void main(String[] args) {
        User user = new User("Cody 1996");
        //user.enterAnswer();
        System.out.println(user.toString());
        //user.enterAnswer();
        System.out.println(user.toString());
        user.undo();
        System.out.println(user.toString());
        //user.enterAnswer();
        System.out.println(user.toString());
    }
}
