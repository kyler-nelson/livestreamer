package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import twitchapi.Channel;
import twitchapi.Stream;
import twitchapi.StreamList;
import twitchapi.UserFollowList;

public class FollowPanel extends JPanel
{
  /**
   * 
   */
  private static final long serialVersionUID = -6068286313539183654L;
  private UserFollowList userFollowList;
  private StreamList streamList;
  
  private JTextField txtUsername;
  private JButton btnRefresh;
  private Map<JRadioButton,Channel> buttonChannelMap;
  private ButtonGroup btngroupChannels;
  
  public FollowPanel(String theUsername)
  {
    super(new GridBagLayout());
    
    this.btngroupChannels = new ButtonGroup();                   //Maintain selected radio button
    this.buttonChannelMap = new HashMap<JRadioButton,Channel>(); //Maintain list of radio buttons
    
    GridBagLayout layoutManager = (GridBagLayout) getLayout();
    this.setBorder(BorderFactory.createEtchedBorder());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.NORTH;
    
    //Username
    this.txtUsername = new JTextField(theUsername);
    this.txtUsername.setActionCommand("update follow list username");
    layoutManager.setConstraints(txtUsername, gbc);
    add(txtUsername);
    
    //Refresh
    this.btnRefresh = new JButton("Refresh");
    this.btnRefresh.setActionCommand("refresh follow list");
    layoutManager.setConstraints(btnRefresh, gbc);
    add(btnRefresh);
    
    //Update the username for follow list
    //Create and update radio buttons for active streams
    update(theUsername);
  }
  
  private Map<String, String> parameterizeFollowList()
  {
    Map<String, String> parameterMap = new HashMap<String, String>();
    String value = "";
    ArrayList<Channel> channelList = userFollowList.getChannelList();
    
    if(!channelList.isEmpty())
    {
      value = channelList.get(0).getUsername();
      for(int i = 1; i < channelList.size(); i++)
      {
        value += "," + channelList.get(i).getUsername();
      }
    }
    parameterMap.put("channel", value);
    return parameterMap;
  }

  //Prepares a new list of followers and active streams
  //for those followers
  public void update(String theUsername)
  {
    this.userFollowList = new UserFollowList(theUsername);
    this.streamList = new StreamList(parameterizeFollowList());
    streamList.sort(); //Sort by user name
    update();
  }
  
  public void update()
  {
    //Align all radio buttons added to the left vertical of the panel.
    //This will add the radio buttons below the other buttons in the layout.
    GridBagLayout layoutManager = (GridBagLayout) this.getLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx=0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridy = GridBagConstraints.RELATIVE;
    gbc.anchor = GridBagConstraints.WEST;
    
    //Remove any radio buttons attached to radio button list
    //Empty on initialization
    Iterator<JRadioButton> itr = this.buttonChannelMap.keySet().iterator();
    while( itr.hasNext() )
    {
      JRadioButton jbtn = itr.next();
      for(ActionListener al : jbtn.getActionListeners()) //Remove any action listeners
      {
        jbtn.removeActionListener(al);
      }
      this.btngroupChannels.remove(jbtn);
      this.remove(jbtn);
      itr.remove();
    }
    
    //Create a new button group after removing buttons
    this.btngroupChannels = new ButtonGroup();
    
    //Add a radio button for each active stream and display channel name.
    for( Stream s : streamList.getStreamList() )
    {
      Channel c = s.getChannel();
      String buttonString = c.getDisplayName()+ " (" +s.getGame() + ": " + s.getViewerCount() + ")";
      JRadioButton btnChannel = new JRadioButton(buttonString);
      btnChannel.setActionCommand("update follow list channel");
      layoutManager.setConstraints(btnChannel, gbc);
      this.buttonChannelMap.put(btnChannel, c);      //Add to list of current buttons
      this.btngroupChannels.add(btnChannel);         //Add to button group
      add(btnChannel);                               //Add to the panel
    }
    
  
    //Set the default channel
    setDefaultChannel();
    
    this.revalidate();
    this.repaint();
  }
  
  public void addActionListener( ActionListener l )
  {
    this.txtUsername.addActionListener(l);
    txtUsername.registerKeyboardAction(l, KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0, false),
        JComponent.WHEN_FOCUSED);
    this.btnRefresh.addActionListener(l);
    addStreamListListener(l);
  }
  
  public void addStreamListListener( ActionListener l )
  {
    for(JRadioButton jbtn : this.buttonChannelMap.keySet())
    {
      jbtn.addActionListener(l);
    }
  }

  public String getUsername()
  {
    return this.txtUsername.getText();
  }

  public String getCurrentChannel()
  {
    String channel = "";
    if( !buttonChannelMap.isEmpty() )
    {
      for( JRadioButton btn : buttonChannelMap.keySet() )
      {
        if(btn.isSelected())
        {
          channel = buttonChannelMap.get(btn).getUsername();
        }
      }
    }
    else
    {
      System.out.println("getCurrentChannel: No channel selected.");
    }
    return channel;
  }

  /**
   * Grab the first button in the button group and set as selected.
   */
  public void setDefaultChannel()
  {
    System.out.println("setDefaultChannel: Entering");
    //Set the default channel if not empty
    if( !buttonChannelMap.isEmpty() && 
        this.btngroupChannels.getButtonCount() > 0  )
    {
      System.out.println("setDefaultChannel: Clicking element.");
      this.btngroupChannels.getElements().nextElement().doClick();
    }
    else //Add a placeholder radio button
    {
      JLabel lblEmpty = new JLabel("No streams available. Refresh the page.");
      this.add(lblEmpty);
    }
  }
  
  public ArrayList<Channel> getChannelList()
  {
    return userFollowList.getChannelList();
  }
}
