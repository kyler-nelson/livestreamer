package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import livestreamer.Livestreamer;
import livestreamer.Quality;

import org.scribe.utils.Preconditions;

public class LauncherPanel extends JPanel
{

 
  private static final long serialVersionUID = -1055441191121508020L;
  
  private String channel;
  private Quality streamQuality;
  private boolean isStreaming;
  
  private JButton btnLauncher;
  private JLabel lblStream;
  private JLabel lblQuality;

  public LauncherPanel()
  {
    this("", Quality.BEST);
  }
  
  public LauncherPanel( String theChannel, String theStreamQuality)
  {
    this(theChannel,Quality.getQuality(theStreamQuality));
  }
  
  public LauncherPanel( String theChannel, Quality theStreamQuality )
  {
    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createEtchedBorder());
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(1, 1, 1, 1);
    
    gbc.anchor = GridBagConstraints.CENTER;
    this.btnLauncher = new JButton("Launch");
    this.btnLauncher.setActionCommand("launch");
    this.add(btnLauncher, gbc);
    
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.WEST;
    this.lblStream = new JLabel("Active stream:");
    this.add(lblStream, gbc);
    
    gbc.gridy++;
    this.lblQuality = new JLabel("Quality: ");
    this.add(lblQuality, gbc);
    
    setChannel(theChannel);
    setStreamQuality(theStreamQuality);
  }
  
  public void launch()
  {
    launch( getChannel(), getStreamQuality() );
  }
  
  private void launch( String theChannel, Quality theQuality )
  {
    try
    {
      Livestreamer.launchStream(theChannel, theQuality);
    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
  
  public void addActionListener( ActionListener l )
  {
    this.btnLauncher.addActionListener(l);
  }

  public String getChannel()
  {
    return channel;
  }

  public void setChannel(String theChannel)
  {
    if( theChannel == null || theChannel.isEmpty() )
    {
      this.channel = "";
      this.lblStream.setText("Active stream: none");
    }
    else
    {
      this.channel = theChannel;
      this.lblStream.setText("Active stream: " + theChannel);
    }
  }

  public Quality getStreamQuality()
  {
    return streamQuality;
  }

  public void setStreamQuality(Quality theStreamQuality)
  {
    Preconditions.checkNotNull(theStreamQuality, "Quality value is null");

    this.streamQuality = theStreamQuality;
    this.lblStream.setText("Quality: " + theStreamQuality.toString());
  }
  
  public boolean isStreaming()
  {
    return isStreaming;
  }

  public void setStreaming(boolean isStreaming)
  {
    this.isStreaming = isStreaming;
  }

}
