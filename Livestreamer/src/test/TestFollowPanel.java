package test;

import gui.FollowPanel;
import gui.LauncherPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;

import livestreamer.Quality;

public class TestFollowPanel
{

  public static void main(String[] args)
  {
    JFrame frame = new JFrame("Test");
    JPanel panel = new FollowPanel("shadowrelic");
    frame.add(panel);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500,500);
    frame.setVisible(true);
  }

}
