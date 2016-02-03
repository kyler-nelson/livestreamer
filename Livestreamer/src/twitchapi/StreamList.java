package twitchapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class StreamList extends TwitchApi
{
  private List<Stream> streamList;

  public StreamList()
  {
    this(null);
  }
  
  public StreamList(Map<String, String> theParameterList)
  {
    super("streams", theParameterList);
  }  
  
  /**
   * Query and add the active streams from a comma-separated list of streams.
   * The current stream list is reconstructed on every call.
   * 
   * @param theChannelList Comma-separated list of channels
   */
  public void update(Map<String, String> theParameterList)
  {
    for( String key : theParameterList.keySet())
    {
      addParameter(key, theParameterList.get(key));
    }
    update(); //Update with given arguments
  }

  @Override
  public void parseJson(JsonObject theResponse)
  {
    JsonArray jsonStreamArrayList = theResponse.get("streams").asArray();
    
    if( this.streamList == null )
    {
      this.streamList = new ArrayList<Stream>();
    }
    else
    {
      this.streamList.clear();
    }
    
    //Offline streams are not included in response.
    for( JsonValue j : jsonStreamArrayList )
    {
      this.streamList.add(new Stream(j.asObject())); //Parse each value as a Stream.
    }
  }
  
  public List<Stream> getStreamList()
  {
    return streamList;
  }
  
  public String toString()
  {
    String output = "";
    for( Stream s :  this.streamList )
    {
      output += "\n<--STREAMLIST-->\n" + s;
    }
    return output;
  }
  
  public void sort()
  {
    Collections.sort(this.streamList);
  }
}
