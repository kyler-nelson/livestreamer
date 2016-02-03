package livestreamer;

/**
 * Quality of the stream, to match Livestreamer parameters.
 * @author Kyler Nelson
 */
public enum Quality
{
  WORST, BEST;
  
  public static Quality getQuality( String s )
  {
    Quality q = null;
    switch(s)
    {
      case "low": case "worst":
        q = Quality.WORST; break;
      case "high": case "best":
        q = Quality.BEST; break;
      default:
        q = Quality.BEST; break;
    }
    return q;
  }
  
  public String toString()
  {
    return super.toString().toLowerCase();
  }
}
