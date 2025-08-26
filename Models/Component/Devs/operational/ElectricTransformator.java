package Component.Devs.operational;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.port.Content;
import Component.Devs.lib.port.LogStruct;
import java.util.HashMap;
import model.modeling.message;

public class ElectricTransformator extends DefaultViewableAtomic {
  
  private final double rate = 0.3333;
  
  private double relation;

  private double tension;

  private String state;

  private double value;

  private double level;
  
  public ElectricTransformator ( String n, double t0, double t1 ) {
    super ( String. format ( "ElectricTransformator %s(%s/%s)", n, t0, t1 ) );
    addOutport ( "otension" );
    addOutport ( "log" );
    addInport ( "tension" );
    addInport ( "request" );
    relation = ( ( Number ) t0 ). doubleValue () / ( ( Number ) t1 ). doubleValue ();
    holdIn ( "wait", INFINITY );
    level = 1;
  }
  
  @Override
  public void initialize() {

  }
  
  private double f ( double tt, double t ) { 
    return tt * relation * level;
  }
  
  @Override
  public void deltext(double e, message x) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( "request" ) ).each ( ( Object v ) -> {
      Content in = ( Content ) v;
      level = ( double ) in. value;
      switch ( in. state ) {
        case "fail": {
          state = "failure";
        } break;
        case "restore": {
          state = "working";
        } break;
      }
      holdIn ( "change", 1 );
    } ); 
    over ( map. get ( "tension" ) ).each ( ( Object v ) -> { 
      tension = ( double ) v;
      holdIn ( "send", 1 );
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
        return send ( "otension", value = f ( tension, getSigma () ) );
      } 
      case "change": {
        return send ( "log", new LogStruct ( tag (), getPhase (), state, String. format ( "tension:%s, relation:%s, level:%s", value, relation, level ) ) ); 
      }
    }
    return send ();
  }
  
  @Override
  public double ta() {
    return getSigma ();
  }

}
