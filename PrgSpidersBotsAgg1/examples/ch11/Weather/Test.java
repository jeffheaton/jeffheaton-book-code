import com.heaton.bot.weather.*;

public class Test
{
  public static void main(String args[])
  {
/*    Weather a[] = Weather.getList();
    for(int i=0;i<a.length;i++)
    {
      System.out.println(
        a[i].city + " - " + a[i].deg );
    }
    */
    Weather.fileAggregate("wxtest.html");
  }
}
