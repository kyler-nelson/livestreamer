package livestreamer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Livestreamer
{
  public static final String LIVESTREAMER_PATH = "C:/Program Files (x86)/Livestreamer/livestreamer.exe";
  public static final String OUTPUT_PATH = "C:/Users/Kyler/Videos/livestreamer/";
  
  private static final Livestreamer INSTANCE = new Livestreamer();
  private Process process;
  private boolean isRunning;
  
  public Livestreamer()
  {
    process = null;
  }
  
  public static int launchStream(String channel, Quality quality)  throws IOException, InterruptedException
  {
    ArrayList<String> argList = new ArrayList<String>();
    argList.add("livestreamer");
    argList.add("twitch.tv/" + channel);
    argList.add(quality.toString());
    return executeCommand(argList);
  }
  
  public static int executeCommand( List<String> commandList )
  {
    int exitValue = -1;
    
    if( INSTANCE.isRunning )
    {
      System.out.println("Launcher is already running, stop previous process.");
      INSTANCE.process.destroy();
      return 0;
    }
    
      ProcessBuilder pb = new ProcessBuilder(commandList);
      try
      {
        INSTANCE.process = pb.start();
      } catch (IOException e)
      {
        e.printStackTrace();
      }

      INSTANCE.isRunning = true;
      
      try
      {
        exitValue = INSTANCE.process.waitFor();
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      INSTANCE.isRunning = false;

    return exitValue;
  }
  
  public boolean isRunning()
  {
    return this.isRunning;
  }
  
  public static String getPath()
  {
    String path = LIVESTREAMER_PATH;
    File f = new File(path);
    if( !f.exists() || !f.isFile() )
    {
        throw new NullPointerException("Cannot find location of livestreamer.");
    }
    return path;
  }
}
