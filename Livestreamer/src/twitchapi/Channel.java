package twitchapi;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Channel extends TwitchApi implements Comparable<Channel>
{
  private String username;
  private String game;
  private String status;
  private Instant updatedAt;
  private Instant createdAt;
  private int id;
  private URL url;
  private String displayName;

  /**
   * GET /channels/:channel/
   * @param theChannel
   */
  public Channel(String theChannel)
  {
    super("channels/" + theChannel);
  }
  
  /**
   * GET /streams/:channel/
   * @param theChannel
   */
  public Channel(JsonObject jo)
  {
    super(jo);
  }
  
  /**
   * @param theUsername The Twitch username to query.
   */
  public void parseJson(JsonObject jsonChannel)
  {
    try
    {
      setUsername(jsonChannel.get("name").asString());
      setGame(jsonChannel.get("game"));
      setStatus(jsonChannel.get("status"));
      setUpdatedAt(jsonChannel.get("updated_at").asString());
      setCreatedAt(jsonChannel.get("created_at").asString());
      setId(jsonChannel.get("_id").asInt());
      setUrl(jsonChannel.get("url").asString());
      setDisplayName(jsonChannel.get("display_name").asString());
    }
    catch ( NullPointerException e )
    {
      System.out.println("Null found in channel: " + jsonChannel);
      e.printStackTrace();
    }
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String updatedAt) {
    this.createdAt = Instant.parse(updatedAt);
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getGame() {
    return game;
  }

  public void setGame(JsonValue jvalue) {
    this.game = (jvalue != null && !jvalue.isNull())? jvalue.asString() :"";
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(JsonValue jvalue) {
    this.status = (jvalue != null)? jvalue.asString() :"";
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = Instant.parse(updatedAt);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(String theUrl) {
    try {
      this.url = new URL(theUrl);
    }catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String theDisplayName) {
    this.displayName = theDisplayName;
  }

  public String toString()
  {
    return "Channel:" 
        + "\n  " + getUsername()
        + "\n  " + getStatus() 
        + "\n  " + getGame() 
        + "\n  " + getCreatedAt() 
        + "\n  " + getUpdatedAt() 
        + "\n  " + getId()
        + "\n  " + getUrl() 
        + "\n  " + getDisplayName();
  }

  @Override
  public int compareTo(Channel c)
  {
    return c.getUsername().compareTo(this.getUsername());
  }

}
