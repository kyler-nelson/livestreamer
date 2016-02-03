package oauth;

import java.io.InputStream;

import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.model.Response;

public class RequestTest
{
   
  private static final String CLIENT_SECRET = "h4vr3wqv698am6x1o9r3v4x6nu2lrgd";
  private static final String CLIENT_ID = "poqk6o641vyuyq7dnju2e8se1lecdrh";  
  private static final String REDIRECT_URI = "http://127.0.0.1";  
  
  
  public static void main(String[] args)
  {
    Verb v = Verb.GET;
    Request request = new Request(v, "https://api.twitch.tv/kraken/oauth2/authorize");
    request.addQuerystringParameter("response_type", "token");       
    request.addQuerystringParameter("client_id", CLIENT_ID);    
    request.addQuerystringParameter("redirect_uri", REDIRECT_URI);
    request.addQuerystringParameter("scope", "user_read");
    Response response = request.send();
    System.out.println(request.getCompleteUrl());
    System.out.println(response.getBody());
    
  }
}
