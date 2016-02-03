package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.sun.jna.NativeLibrary;

import player.MediaPlayerPanel;

public class LivestreamerGui extends JFrame implements ActionListener
{
  private static final long serialVersionUID = 8098248739973167674L;
  private static final String DEFAULT_USERNAME = "shadowrelic";
  
  private String activeChannel;
  private LauncherPanel myLauncherPanel;
  private ChannelPanel channelPanel;
  private FollowPanel followPanel;
  private MediaPlayerPanel mediaPlayerPanel;
  
  public static void main(String[] args)
  {
    NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");
    LivestreamerGui gui = new LivestreamerGui();
    gui.init();
  }
  
  public LivestreamerGui()
  {
    super("LivestreamLauncher");
    
    final Container canvas = this.getContentPane();
    
    canvas.setLayout(new BorderLayout());
    
    this.followPanel = new FollowPanel(DEFAULT_USERNAME);
    this.myLauncherPanel = new LauncherPanel();
    this.mediaPlayerPanel = new MediaPlayerPanel();
    this.myLauncherPanel.addActionListener(this); //Listen for a launch
    
    this.add(this.followPanel, BorderLayout.WEST);
    this.add(this.myLauncherPanel, BorderLayout.EAST);
    this.followPanel.addActionListener(this); //Listen for change to channel
    
    this.mediaPlayerPanel.openMedia("C:\\Users\\Kyler\\Videos\\livestreamer\\inu_x_boku_ep1.mp4");
    this.add(this.mediaPlayerPanel, BorderLayout.SOUTH);
    
    updateLauncher(followPanel.getCurrentChannel()); //Update the launcher with default channel
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(500,500);
    this.pack();
    this.setVisible(false);
  }
  
  public void init()
  {
    this.setVisible(true);
  }
  
  /**
   * Updates the LivestreamLauncher with the current channel.
   */
  private void updateLauncher(String theChannel)
  {
    this.activeChannel = theChannel;
    if(activeChannel != null)
    {
      this.myLauncherPanel.setChannel(activeChannel);
    }
    else
    {
      throw new IllegalStateException("Invalid or empty username: \"" + activeChannel + "\"" );
    }
    
    this.myLauncherPanel.repaint();
    pack();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if( e.getActionCommand() == "update channel" )
    {
      this.channelPanel.update();             //Update with the new username
      
      //Channel panel has changed channel, update the launcher panel.
      updateLauncher(this.followPanel.getCurrentChannel());
    }
    else if( e.getActionCommand() == "refresh follow list" )
    {
      //Follow list panel has been refreshed. Add listeners to the new buttons
      //then update the launcher with the new default channel.
      this.followPanel.update();
      this.followPanel.addStreamListListener(this); //Add new listeners to the radio buttons
      updateLauncher(this.followPanel.getCurrentChannel());
      pack();
    }
    else if( e.getActionCommand() == "update follow list channel" )
    {
      //Follow list panel has changed channel, update the launcher panel.
      System.out.println("[Action event] update follow list channel: " + this.followPanel.getCurrentChannel());
      updateLauncher(this.followPanel.getCurrentChannel());
    }
    else if( e.getActionCommand() == "update follow list username" )
    {
      JTextField txtUsername = (JTextField) e.getSource();
      String username = txtUsername.getText();
      if(!this.followPanel.getChannelList().isEmpty()) //True if user has active streams
      {
        this.followPanel.update(username);             //Update with the new username
        this.followPanel.addStreamListListener(this);  //Add new listeners to the radio buttons
      }
      
      //Channel panel has changed channel, update the launcher panel.
      updateLauncher(this.followPanel.getCurrentChannel());
      
      pack();
    }
    else if( e.getActionCommand() == "launch" )
    {
      this.setVisible(false);
      this.myLauncherPanel.launch();
      this.setVisible(true);
    }
    else //Action not accounted for
    {
      throw new UnknownError("Unknown source of action event: " + e.getSource() + e.paramString() );
    }
  }
}