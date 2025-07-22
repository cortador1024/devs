package Component.Devs.simulator;

import Component.Devs.util.DefaultViewableAtomic;
import java.util.HashMap;

import model.modeling.message;

public class ElectricGenerator extends DefaultViewableAtomic {
  
  private static int count = 1;
  
  private String state; // state = { ok, fail }
  
  private double rate = 2;

  private double outputTension;

  private double nominalTension;
      
  public ElectricGenerator ( String n, double t, int s ) {
    super ( String. format ( "%s Eg", n ) );
    addOutport ( "out" );
    addOutport ( "response" );
    addInport ( "in" );
    addInport ( "state" );
    outputTension = t;
    nominalTension = t;
    step = s;
    holdIn ( "ok", step ); 
    count ++;
  } 
  
  public ElectricGenerator ( String n, double t ) {
    this ( n, t, 1 );
  }
  
  @Override
  public void initialize() {
    
  }
  
  private double f ( double e ) {
    return outputTension ;
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    HashMap < String, Object > map = receive ( x );
    state = ( state = ( String ) map. get ( "state" ) ) != null ? state : "";
    switch ( state ) {
      case "fail": {
        outputTension = nominalTension * 0.5;
        state = "fail";
      } break;
      case "restore": {
        outputTension = nominalTension;
        state = "ok";
      } break;
    }
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn ( state, step ); 
  }
  
  @Override
  public message out() {
    String p = getPhase ();
    return send ( "out", f ( 0 ), "response", getPhase () );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
