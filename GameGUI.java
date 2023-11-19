/** 
 * FILE NAME: GameGUI.java
 * WHO: Cody Wan and Kealani Finegan
 * WHAT: Implements a class which instantiates and launches the GUI for the game
 * FOR: CS230 Fall 2017 Final Project
 */

import javax.swing.*;

public class GameGUI {
  
  /**
   * Creates an new gameGUI with an instance of a Game and adds all panels
   */
  public static void main(String[] args) {
    // create and show a Frame
    JFrame frame = new JFrame("2D Color Game by Kealani Finegan and Cody Wan");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //create an instance of the 2D color game
    Game game = new Game();
    
    //create a panel, and add it to the frame
    GamePanels panel = new GamePanels(game);
    frame.getContentPane().add(panel);
    
    frame.pack();
    frame.setVisible(true);
  }
}