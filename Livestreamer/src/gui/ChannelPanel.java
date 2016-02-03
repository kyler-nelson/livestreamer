package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import twitchapi.Channel;

@SuppressWarnings("serial")
public class ChannelPanel extends JPanel
{
  private JTextField txtUsername;
  private JButton btnRefresh;
  private Channel channel;
  
  public ChannelPanel(String theUsername)
  {
    super();
    
    this.channel = new Channel(theUsername);
    
    this.setLayout(new GridLayout(1,3));
    
    JLabel lblChannel = new JLabel("Channel:");
    this.add(lblChannel);
    
    //Username
    this.txtUsername = new JTextField(theUsername);
    this.txtUsername.setActionCommand("update channel");
    add(txtUsername);
    
    //Refresh
    this.btnRefresh = new JButton("Refresh");
    this.btnRefresh.setActionCommand("update channel");
    add(btnRefresh);
  }
  
  public void addActionListener( ActionListener l )
  {
    this.txtUsername.addActionListener(l);
    this.btnRefresh.addActionListener(l);
  }

  public void update()
  {
    this.channel.update();
  }
  
  public void update(String username)
  {
    channel = new Channel(username);
  }

  public String getCurrentUsername()
  {
    return this.txtUsername.getText();
  }
}
