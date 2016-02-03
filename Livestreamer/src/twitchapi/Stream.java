package twitchapi;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Stream extends TwitchApi implements Comparable<Stream>
{
  private boolean isOnline;
  private Channel channel;
  private long id;
  private int viewerCount;
  private String game;

  /**
   * GET /streams/:channel/
   * @param theChannel
   */
  public Stream(String theChannel)
  {
    super("streams/" + theChannel);
  }
  
  /**
   * GET /streams/:channel/
   * @param theChannel
   */
  public Stream(JsonObject jo)
  {
    super(jo);
  }

  @Override
  public void parseJson(JsonObject theResponse)
  {
    JsonValue jvalue = theResponse.get("stream");

    //The value isn't defined in the response for Stream arrays
    if( jvalue == null )
    {
      setOnline(true);
    }
    else if( !jvalue.asString().equals("null") ) // Null if the stream is offline
    {
      theResponse = theResponse.get("stream").asObject(); //Grab the stream object
      setOnline(true);
    }
    else
    {
      setOnline(false);
    }

    if( isOnline() )
    {
      setId(theResponse.get("_id").asLong());
      setViewerCount(theResponse.get("viewers").asInt());
      setChannel(theResponse.get("channel").asObject());
      setGame(theResponse.get("game").asString());
    }
  }

  public boolean isOnline()
  {
    return isOnline;
  }

  public void setOnline(boolean isOnline)
  {
    this.isOnline = isOnline;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public int getViewerCount()
  {
    return viewerCount;
  }

  public void setViewerCount(int viewerCount)
  {
    this.viewerCount = viewerCount;
  }

  public String getGame()
  {
    return game;
  }

  public void setGame(String game)
  {
    this.game = game;
  }

  public Channel getChannel()
  {
    return channel;
  }

  public void setChannel(JsonObject theJsonObject)
  {
    if(theJsonObject == null)
    {
      throw new NullPointerException("Null JSON object for channel for given stream");
    }
    
    this.channel = new Channel(theJsonObject);
  }
  
  @Override
  /**
   * Compare to another stream by the stream id.
   */
  public int compareTo(Stream s)
  {
    return (int)(s.getId() - this.getChannel().getId());
  }
  
  public String toString()
  {
    return "Stream:"
        + "\n  isOnline: " + isOnline() 
        + "\n  viewerCount: " + getViewerCount() 
        + "\n  Game: " + getGame() 
        + "\n  Id" + getId()
        + "\n  " + getChannel();
  }
}
