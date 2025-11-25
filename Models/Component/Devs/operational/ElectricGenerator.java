package Component.Devs.operational;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.ElectricEvent;
import Component.Devs.lib.port.LogStruct;
import java.util.HashMap;

import model.modeling.message;

public class ElectricGenerator extends DefaultViewableAtomic {
  
  private double tension;

  private double nominal;
  
  private double level = 1;
  
  public ElectricGenerator ( String n, double t ) {
    super ( String. format ( "ElectricGenerator %s", n, t ) );
    addOutport ( "otension" );
    addOutport ( "log" );
    addInport ( "tension" );
    addInport ( "state" );
    tension = t;
    nominal = t;
    holdIn ( "send", 1 ); 
  } 
  
  private double f () {
    return tension * level;
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( "state" ) ). each ( ( Object v ) -> {
      ElectricEvent event = ( ElectricEvent ) v;
      level = event. level;
      holdIn ( "change", 0 );
    } );
    over ( map. get ( "tension" ) ). each ( ( Object v ) -> {
      tension = ( double ) v;
      holdIn ( "send", 0 );
    } );
  }
  
  @Override
  public void deltint () {
    holdIn ( "send", 1 );
  }
  
  @Override
  public message out () {
    switch ( getPhase () ) {
      case "send" : { 
        return send ( "otension", f () );
      } 
      case "change": {
        return send ( "log", new LogStruct ( tag (), getPhase (), f (), nominal, level ) ); 
      }
    }
    return send ();
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

  double generation () {
    return nominal;
  }

}
