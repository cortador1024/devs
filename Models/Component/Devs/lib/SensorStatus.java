package Component.Devs.lib;

import Component.Devs.SensorController.SensorState;
import GenCol.entity;

public class SensorStatus extends entity {

  public double read;
  
  public SensorState state;
  
  public SensorStatus ( SensorState s, double r ) {
    super( String. format ( "SensoStatus %s, %s", s, r ) );
    state = s;
    read = r;
  }
  
  @Override
  public String getName () {
    return String. format ( "SensorStatus %s, %s", state, read );
  }
  
}
