package Component.Devs.operational;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.ElectricEvent;
import Component.Devs.lib.port.Content;
import Component.Devs.lib.port.LogStruct;
import java.util.HashMap;
import model.modeling.message;

public class ElectricTransformator extends DefaultViewableAtomic {
  
  private double relation;

  private double tension;

  private double level;
  
  public ElectricTransformator ( String n, double inputTension, double outputTension ) {
    super ( String. format ( "ElectricTransformator %s(%s/%s)", n, inputTension, outputTension ) );
    addOutport ( "otension" );
    addOutport ( "log" );
    addInport ( "tension" );
    addInport ( "state" );
    relation = ( ( Number ) outputTension ). doubleValue () / ( ( Number ) inputTension ). doubleValue ();
    level = 1;
    holdIn ( "wait", INFINITY );
  }
  
  public double relation () {
    return relation;
  }
  
  @Override
  public void initialize() {
    
  }
  
  private double f ( double tt ) { 
    return tt * relation * level;
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
  public void deltint() {
    holdIn ( "send", 1 );
  }
  
  @Override
  public message out() {
    switch ( getPhase () ) {
      case "send" : { 
        return send ( "otension", f ( tension ) );
      } 
      case "change": {
        return send ( "log", new LogStruct ( tag (), getPhase (), f ( tension ), relation, level ) ); 
      }
    }
    return send ();
  }
  
  @Override
  public double ta() {
    return getSigma ();
  }

}
