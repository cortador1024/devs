package Component.Devs.operational;

import Component.Devs.lib.DefaultViewableAtomic;
import java.io.BufferedWriter;
import java.util.HashMap;
import model.modeling.message;

public class ElectricTransformator extends DefaultViewableAtomic {
  
  private final double rate = 0.3333;
  
  private static int counter = 0;
  
  private int advance = 0;
  
  private double relation;
  private double tension;
  private String state;
  private double input;
  private double level;
  
  public ElectricTransformator ( String n, double t0, double t1 ) {
    super ( n );
    addOutport ( "out" );
    addOutport ( "response" );
    addInport ( "in" );
    addInport ( "state" );
    
    relation = ( ( Number ) t0 ). doubleValue () / ( ( Number ) t1 ). doubleValue ();
    holdIn ( state = "working", INFINITY );
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
    Object [] inState = ( inState = ( Object [] ) map. get ( "state" ) ) != null ? inState : ( Object [] ) null;
    if ( inState != null ) {
      String cause = ( String ) inState [ 0 ];
      switch ( cause ) {
        case "fail": {
          level = ( double ) inState [ 1 ];
          state = "failure";
        } break;
        case "restore": {
          state = "working";
          level = 1;
        } break;
      }
    }
    Object val = map. get ( "in" );
    if ( val != null ) {
      tension = ( double ) val;
    }
    holdIn ( state = "working", 1 );
  }
  
  @Override
  public void deltint() {
    holdIn ( state, 1 );    
  }
  
  @Override
  public message out() {
    Object out = f ( tension, getSigma () );
    return send ( "out", out, "response", new Object [] { getName (), getPhase (), out } );
  }
  
  @Override
  public double ta() {
    return getSigma ();
  }

}
