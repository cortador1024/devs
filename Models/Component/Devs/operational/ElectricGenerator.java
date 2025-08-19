package Component.Devs.operational;

import Component.Devs.lib.DefaultViewableAtomic;
import java.io.BufferedWriter;
import java.util.Collections;
import java.util.HashMap;

import model.modeling.message;

public class ElectricGenerator extends DefaultViewableAtomic {
  
  static private final Object [] EMPTY_ARRAY = new Object [ 0 ];
  
  private String state = ""; // cause = { ok, fail }
  
  private double level = 1;
  
  private double outputTension;

  private double nominalTension;
  
  public ElectricGenerator ( String n, double t ) {
    super ( String. format ( "EG.%s", n ) );
    addOutport ( "out" );
    addOutport ( "response" );
    addInport ( "in" );
    addInport ( "state" );
    outputTension = t;
    nominalTension = t;
    holdIn ( state = "working", 1 ); 
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
    Object [] in = ( in = ( Object [] ) map. get ( "state" ) ) != null ? in : ( Object [] ) null;
    if ( in == null ) {
      return ;
    }
    String cause = ( String ) in [ 0 ];
    level = ( double ) in [ 1 ];
    switch ( cause ) {
      case "fail": {
        outputTension = nominalTension * ( double ) level;
        state = "failure";
      } break;
      case "restore": {
        outputTension = nominalTension;
        state = "working";
      } break;
    }
  }
  
  @Override
  public void deltint() {
    holdIn ( state, 1 ); 
  }
  
  @Override
  public message out() {
    return send ( 
      "out", f ( getSigma () ), 
      "response", new Object [] { 
        getName (), getPhase () 
      } 
    );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
