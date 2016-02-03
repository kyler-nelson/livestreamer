package test;

import java.util.Map;
import java.util.TreeMap;

import twitchapi.Stream;
import twitchapi.StreamList;

public class StreamListTest
{
  public static void main(String[] args)
  {
    Map<String, String> parameterList = new TreeMap<String, String>();
    parameterList.put("channel", "twitchplayspokemon,mic_feedback");
    StreamList sl = new StreamList(parameterList);
    System.out.println(sl);
  }
}
