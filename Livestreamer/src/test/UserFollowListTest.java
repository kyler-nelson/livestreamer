package test;

import twitchapi.Stream;
import twitchapi.UserFollowList;

public class UserFollowListTest
{
  public static void main(String[] args)
  {
    UserFollowList ufl = new UserFollowList("shadowrelic");
    System.out.println(ufl);
  }
}
