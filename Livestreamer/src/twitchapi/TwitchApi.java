package twitchapi;

import java.util.Map;

import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import com.eclipsesource.json.JsonObject;

public abstract class TwitchApi
{
  public static final String BASE_URL = "https://api.twitch.tv/kraken/";
  public final String endpoint;
  private Map<String, String> parameterList;

  public TwitchApi(String theEndpoint)
  {
    this(theEndpoint, null);
  }
  
  public TwitchApi(String theEndpoint, Map<String, String> theParameterList)
  {
    this.endpoint = theEndpoint;
    this.parameterList = theParameterList;
    update();
  }
  
  /**
   * Parse an existing JSON object. Twitch objects provide a self
   * reference. This is used for parsing the endpoint.
   * @param theJsonObject
   */
  public TwitchApi(JsonObject theJsonObject)
  {
    String endpoint = theJsonObject.get("_links").asObject().get("self").asString();
    this.endpoint = endpoint.replace(BASE_URL,"");
    parseJson(theJsonObject);
  }
  
  /**
   * Query the API and update the object.
   */
  public void update()
  {
    Request request = new Request(Verb.GET, BASE_URL + this.endpoint);
    
    if( getParameterList() != null && !getParameterList().isEmpty() )
    {
      for( String argument : getParameterList().keySet() )
      {
        request.addQuerystringParameter(argument, getParameterList().get(argument));
      }
    }
    
    System.out.println("[TwitchApi Request] " + request.getCompleteUrl());

    Response response = request.send();
    parseJson(JsonObject.readFrom( response.getBody() ));
  }

  /**
   * Handle how to de-serialize the JsonObject.
   * @param theResponse
   */
  abstract public void parseJson( JsonObject theResponse );

  /**
   * Objects are the same if they have the same endpoint.
   * @return
   */
  public boolean equals(Object o)
  {
    if( o == null )
    {
      return false;
    }
    if(!(o instanceof TwitchApi))
    {
      return false;
    }
    TwitchApi api = (TwitchApi) o;
    return this.endpoint.equals(api.endpoint);
  }
  
  /**
   * Output as a list of values for the object.
   */
  abstract public String toString();

  private Map<String, String> getParameterList()
  {
    return parameterList;
  }

  public void addParameter(String key, String value)
  {
    parameterList.put(key, value);
  }
}
