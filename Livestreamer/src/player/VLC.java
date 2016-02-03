package player;

import java.io.File;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

public class VLC
{
  public static final String VLC_PATH = "C:/Program Files (x86)/VideoLAN/VLC/vlc.exe";
  public static final String OUTPUT_PATH = "C:/Users/Kyler/Videos/vlc/";
  
  public static String getVlcPath()
  {
    String path = VLC_PATH;
    File f = new File(path);
    if( !f.exists() || !f.isFile() )
    {
      path = WindowsRuntimeUtil.getVlcInstallDir();
      if(path == null)
      {
        throw new NullPointerException("Cannot find location of VLC player.");
      }
    }
    return path;
  }
}
