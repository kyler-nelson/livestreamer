package player;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import com.sun.jna.NativeLibrary;


@SuppressWarnings("serial")
public class StreamTest extends JFrame{
  private static final String MRL = "C:\\Users\\Kyler\\Videos\\livestreamer\\2015-04-28_livestreamer.mp4";
  //private static final String MRL2 = "https://www.youtube.com/watch?v=orMgNh0o38A";
  private MediaPlayerPanel mediaPlayerPanel;
  
  StreamTest()
  {
    NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");
    
    this.setTitle("Test");

    /* Initialize the JFrame */
    this.setLocation(100, 100);
    this.setSize(1050, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
    mediaPlayerPanel = new MediaPlayerPanel(this);
    
    this.setContentPane(mediaPlayerPanel);

    mediaPlayerPanel.openMedia(MRL);
    
    EmbeddedMediaPlayer emp =  mediaPlayerPanel.getEmbeddedMediaPlayer();
    System.out.println("Duration: " + emp.getMediaMeta().getLength());
    
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