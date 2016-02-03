package twitchapi;

import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class ApiTest
{
  
 private static final String ACCESS_TOKEN = "rvmptaemqaa4rhssolrpalkdc7p9n";

 public static void main(String[] args)
 {
   Verb v = Verb.GET;
   Request request = new Request(v, "https://api.twitch.tv/kraken/streams/followed");
   request.addQuerystringParameter("access_token", "rvmptaemqaa4rhssolrpalkdc7p9n");
   Response response = request.send();
   System.out.println(request.getCompleteUrl());
   System.out.println(response.getBody());
   
 }
}
