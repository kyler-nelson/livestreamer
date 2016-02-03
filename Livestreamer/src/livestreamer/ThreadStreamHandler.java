package livestreamer;

import java.io.*;

class ThreadedStreamHandler extends Thread
{
  InputStream source;
  DataInputStream in;
  DataOutputStream out;

  /**
   * 
   * TODO this currently hangs if the admin password given for the sudo command is wrong.
   * 
   * @param inputStream
   * @param streamType
   * @param outputStream
   */
  ThreadedStreamHandler(InputStream inputStream)
  {
    source = inputStream;
    in = new DataInputStream(new BufferedInputStream(inputStream));
    out = new DataOutputStream(new BufferedOutputStream(System.out));
  }
  
  public void run()
  {
//    readBytes();
    readConsole();

  }

  
  public void readBytes()
  {
    byte[] datainBytes = null;
    try
    {
      while(true)
      {
        datainBytes = new byte[in.available()];
        while(in.read() >= 0)
        {
          datainBytes = new byte[in.available()];
          
          if(datainBytes.length != 0)
          {
            in.readFully(datainBytes, 0, datainBytes.length);
            out.writeChars(datainBytes.toString()+"\n");
          }
        }
      }
    }
    catch( EOFException eof)
    {
      ;
    }
    catch (IOException ioe)
    {
      // TODO handle this better
      ioe.printStackTrace();
    }
    catch (Throwable t)
    {
      // TODO handle this better
      t.printStackTrace();
    }
    finally
    {
      try
      {
        in.close();
        out.close();
      }
      catch (IOException e)
      {
        // ignore this one
      }
    }
  }
  
  public void readConsole()
  {
    BufferedReader d = new BufferedReader(new InputStreamReader(source));
    String line = "";
    
    try
    {
      while((line = d.readLine()) != null)
      {
        System.out.println(line);
      }
    }
    catch( EOFException eof)
    {
      ;
    }
    catch (IOException ioe)
    {
      // TODO handle this better
      ioe.printStackTrace();
    }
    catch (Throwable t)
    {
      // TODO handle this better
      t.printStackTrace();
    }
    finally
    {
      try
      {
        in.close();
        out.close();
      }
      catch (IOException e)
      {
        // ignore this one
      }
    }
  }
}









