package test;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.sun.jna.NativeLibrary;

import player.VLC;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * An example of how to stream a media file using RTP and use the "duplicate" output to also display
 * the video locally in an embedded media player.
 * <p>
 * Note that the duplicated output does not play it's own <em>stream</em>, so video displayed by
 * client applications will lag the local duplicated output.
 * <p>
 * The client specifies an MRL of <code>rtp://@230.0.0.1:5555</code>
 */
public class RtpStream {
  public static void main(String[] args) throws Exception {
    NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");
    String media = "https://www.youtube.com/watch?v=orMgNh0o38A";
    String options = formatRtpStream("230.0.0.1", 5555);
    System.out.println("Streaming '" + media + "' to '" + options + "'");
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
    EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
    Canvas canvas = new Canvas();
    canvas.setBackground(Color.black);
    CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
    mediaPlayer.setVideoSurface(videoSurface);
    
    mediaPlayer.setPlaySubItems(true); // <--- This is very important for YouTube media
    
    JFrame f = new JFrame("vlcj duplicate output test");
    //f.setIconImage(new ImageIcon(RtpStream.class.getResource("/icons/vlcj-logo.png")).getImage());
    f.add(canvas);
    f.setSize(800, 600);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
    mediaPlayer.playMedia(media,
        options,
        ":no-sout-rtp-sap",
        ":no-sout-standard-sap",
        ":sout-all",
        ":sout-keep"
        );
    // Don't exit
    Thread.currentThread().join();
  }
  private static String formatRtpStream(String serverAddress, int serverPort) {
    StringBuilder sb = new StringBuilder(60);
    sb.append(":sout=#duplicate{dst=display,dst=rtp{dst=");
    sb.append(serverAddress);
    sb.append(",port=");
    sb.append(serverPort);
    sb.append(",mux=ts}}");
    return sb.toString();
  }
}