package test;

import java.io.IOException;
import java.util.ArrayList;

import livestreamer.Livestreamer;
import livestreamer.Quality;

public class TestLivestreamer
{
  public static void main(final String[] args) throws IOException, InterruptedException {
    ArrayList<String> argList = new ArrayList<String>();
    argList.add("C:\\Program Files (x86)\\Livestreamer\\livestreamer.exe");
    argList.add("twitch.tv/twitchplayspokemon");
    argList.add(Quality.BEST.toString());
    argList.add("--stdout");
    Livestreamer.launchStream("twitchplayspokemon", Quality.BEST);
  }
}
