package livestreamer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;

import player.VLC;

public class VideoStreamTest
{
  public static void main(String[] args) throws IOException
  {
    ArrayList<String> argList = new ArrayList<String>();
    argList.add(Livestreamer.getPath());
    argList.add("http://www.twitch.tv/twitchplayspokemon");
    argList.add(Quality.BEST.toString());
    argList.add("-p '" + VLC.getVlcPath() + "'");
    argList.add("-a '{filename} sout:#rtp{{mux=ts,dst=192.168.0.4,port=5004}}'");
        
    try
    {

      //ProcessBuilder pb = new ProcessBuilder(argList);
      String livestreamer = "C:/Program Files (x86)/Livestreamer/livestreamer.exe";
      String arg = "twitch.tv/twitchplayspokemon best";
      String player =  "-p 'C:/Program Files (x86)/VideoLAN/VLC/vlc.exe'";
      //String stream = "-a \"{filename} :sout=#display\"";
      String rtp = "--sout-rtp-dst=192.168.0.4";
      ProcessBuilder pb = new ProcessBuilder(livestreamer, "twitch.tv/twitchplayspokemon", "best", "--stdout");
      
      String vlc = "C:/Program Files (x86)/VideoLAN/VLC/vlc.exe";
      ProcessBuilder pb2 = new ProcessBuilder(vlc, "-");
      
      Process p, p2;
      System.out.println("Command: " + pb.command() );
      p = pb.start();
      p2 = pb2.start();

      ThreadedStreamHandler input = new ThreadedStreamHandler(p.getInputStream());
      ThreadedStreamHandler error = new ThreadedStreamHandler(p.getErrorStream());
      input.start();
      error.start();
      
      p.waitFor();
      input.interrupt();
      error.interrupt();

    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return;
  }
}
