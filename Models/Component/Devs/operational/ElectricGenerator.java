package Component.Devs.operational;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.port.Content;
import Component.Devs.lib.port.LogStruct;
import Component.Devs.operational.probability.distribution.Weibull;
import java.util.HashMap;

import model.modeling.message;

public class ElectricGenerator extends DefaultViewableAtomic {
  
  static private final Object [] EMPTY_ARRAY = new Object [ 0 ];
  
  private String state = ""; // cause = { ok, fail }
  
  private double level = 1;
  
  private double tension;

  private double nominalTension;
  
  private Weibull weibull = new Weibull ( 21d, 3d, true );
  
  private double value;
  
  public ElectricGenerator ( String n, double t ) {
    super ( n );
    addOutport ( "otension" );
    addOutport ( "log" );
    addInport ( "tension" );
    addInport ( "state" );
    tension = t;
    nominalTension = t;
    holdIn ( "send", weibull. inverse ( Math. random () ) ); 
  } 
  
  @Override
  public void initialize() {
    
  }
  
  private double f ( double e ) {
    return tension ;
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( "state" ) ). each ( ( Object v ) -> {
      Content in = ( Content ) v;
      level = ( double ) in. value;
      tension = nominalTension * level;
      switch ( in. state ) {
        case "fail": {
          state = "failure";
        } break;
        case "restore": {
          state = "working";
        } break;
      }
      holdIn ( "change", weibull. inverse ( Math. random () ) );
    } );
    over ( map. get ( "tension" ) ). each ( ( Object v ) -> {
      tension = ( double ) v;
      holdIn ( "send", 1 );
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
        return send ( "otension", value = f ( getSigma () ) );
      } 
      case "change": {
        return send ( "log", new LogStruct ( tag (), getPhase (), state, String. format ( "tension:%s", value ) ) ); 
      }
    }
    return send ();
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
