package player;

import java.io.File;

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import com.sun.jna.NativeLibrary;

public class ParseMp4
{

  public static void main(String[] args)
  {
    NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");
    
    File dir = new File("C:\\Users\\Kyler\\Videos\\The_Scorpion_King_Rise_of_the_Akkadian");
    File[] fileArray = dir.listFiles();

    MediaPlayerFactory factory = new MediaPlayerFactory();
    EmbeddedMediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
    
    for( File file : fileArray )
    {
      if( file.getPath().endsWith("mp4") )
      {
        mediaPlayer.prepareMedia(file.getAbsolutePath());
        mediaPlayer.parseMedia();
        MediaMeta meta = mediaPlayer.getMediaMeta();
        
        System.out.println("Path: " + file.getName());
        System.out.println("Description: " + meta.getDescription());
        System.out.println("Date: " + meta.getDate());
        System.out.println("Duration: " + meta.getLength());
        
        meta.setDescription("Playthrough");
        meta.setDate(file.getName().split("-")[0]);
        meta.save();
      }
    }
    
    mediaPlayer.release();
  }

}
