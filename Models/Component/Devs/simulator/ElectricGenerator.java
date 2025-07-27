package Component.Devs.simulator;

import Component.Devs.probability.distribution.Weibull;
import Component.Devs.util.DefaultViewableAtomic;
import RandomNumbers.WeibullDistribution;
import java.util.HashMap;

import model.modeling.message;

public class ElectricGenerator extends DefaultViewableAtomic {
  
  private String state; // state = { ok, fail }
  
  private double outputTension;

  private double nominalTension;
      
  public ElectricGenerator ( String n, double t ) {
    super ( String. format ( "%s Eg", n ) );
    addOutport ( "out" );
    addOutport ( "response" );
    addInport ( "in" );
    addInport ( "state" );
    outputTension = t;
    nominalTension = t;
    holdIn ( "ok", INFINITY ); 
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
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    state = ( state = ( String ) map. get ( "state" ) ) != null ? state : "";
    switch ( state ) {
      case "fail": {
        Object level = ( level = map. get ( "level" ) ) == null ? 0d: level;
        outputTension = nominalTension * ( double ) level;
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
    holdIn ( state, INFINITY ); 
  }
  
  @Override
  public message out() {
    String p = getPhase ();
    return send ( "out", f ( getSigma () ), "response", getPhase () );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
