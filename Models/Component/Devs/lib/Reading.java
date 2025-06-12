package Component.Devs.lib;

import GenCol.entity;

public class Reading extends entity  {

  public final double read;
  
  public Reading ( double r ) {
    super( String. format ( "Reading %s", r ) );
    read = r;
  }
  
  @Override
  public String getName() {
    return String. format ( "Reading %s", read );
  }
  
  
}
