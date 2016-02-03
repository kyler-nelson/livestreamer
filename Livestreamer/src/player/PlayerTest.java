package player;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import com.sun.jna.NativeLibrary;

@SuppressWarnings("serial")
public class PlayerTest extends JFrame{
  private static final String MRL = "haloonline.mp4";
  private static final String MRL2 = "rtp://@192.168.0.4:5004";
  private static final String MRL3 = "https://www.youtube.com/watch?v=Q919xywM0As";
  private String mediaOptions[] = {""};
  private MediaPlayerPanel mediaPlayerPanel;
  
  PlayerTest()
  {
    this.setTitle("Test");

    //:sout=#rtp{dst=192.168.0.4,port=5004,mux=ts,sap,name=Test stream} :sout-keep
    
    /* Initialize the JFrame */
    this.setLocation(100, 100);
    this.setSize(1050, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mediaPlayerPanel = new MediaPlayerPanel(this);
    
    this.setContentPane(mediaPlayerPanel);
    
    if( !mediaPlayerPanel.openMedia(MRL2, mediaOptions) )
    {
      System.out.println("Media could not be opened.");
    }

    this.setVisible(true);    
  }

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new PlayerTest();
      }
    });
  }
}