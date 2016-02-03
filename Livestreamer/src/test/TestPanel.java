package test;

import gui.LauncherPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;

import livestreamer.Quality;

public class TestPanel
{
  public static void main(String[] args)
  {
    JFrame frame = new JFrame("Test");
    JPanel panel = new LauncherPanel("twitchplayspokemon", Quality.BEST);
    frame.add(panel);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500,500);
    frame.setVisible(true);
  }
}
