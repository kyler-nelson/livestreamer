package twitchapi;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Constructs a list of Channels followed by the User from the Twitch API.
 * The class maintains a list of active Streams from the list of Channels.
 * 
 * @author Kyler Nelson
 * @version 2014-09-22
 */
public class UserFollowList extends TwitchApi
{
  //private static final int LIST_LIMIT = 100;
  
  private String username;
  private ArrayList<Channel> channelList;
  
  public UserFollowList(String theUsername)
  {
    super("users/" + theUsername + "/follows/channels");
  }
  
  public void parseJson( JsonObject theResponse )
  {
    if( this.channelList == null )
    {
      this.channelList = new ArrayList<Channel>(); //Create empty list of channels
    }
    else
    {
      this.channelList.clear();
    }
    
    //Array of Follows JSON objects.
    JsonArray jsonFollowsListArray = theResponse.get("follows").asArray();
    for( JsonValue jvalue : jsonFollowsListArray )
    {
      //The follows channel JSON object has a channel object and other information
      //channel JSON objects have some null values not used in the Channel object
      JsonObject jsonChannel = jvalue.asObject().get("channel").asObject();
      this.channelList.add(new Channel(jsonChannel)); //Add the Channel
    }
  }

  public ArrayList<Channel> getChannelList()
  {
    return channelList;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }
    
  public String toString()
  {
    String output = "";
    for( Channel c : this.channelList )
    {
      output += "\n<--USERFOLLOWLIST-->\n" + c;
    }
    return output;
  }
}
