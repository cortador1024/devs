package Component.Devs.operational.fail;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.operational.probability.distribution.Weibull;
import RandomNumbers.WeibullDistribution;
import java.io.BufferedWriter;
import java.util.HashMap;

import model.modeling.message;

public class FailureGenerator extends DefaultViewableAtomic {
  
  private static final double ALPHA = 46.047;
  
  private static final double BETA = 1.180;
  
  private Weibull weibull = new Weibull ( ALPHA, BETA, true );
  
  private double rate = 1;
  
  private String state;
  
  private final static String DEFAULT_OUT = "response";
  
  private final static String DEFAULT_IN = "state";
  private BufferedWriter output;
  
  public FailureGenerator ( String n ) {
    super ( String. format ( "FG.%s", n ) );
    addInport ( DEFAULT_IN );
    addOutport ( DEFAULT_OUT );
    holdIn ( "working", weibull. inverse ( Math. random () ) * 1440 ) ;     
  }
  
  @Override
  public void initialize() {
    
  }
  
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    Object [] inState = ( inState = ( Object [] ) map. get ( "state" ) ) != null ? inState : ( Object [] ) null;
    if ( inState != null ) {
      String cause = ( String ) inState [ 0 ];
      switch ( cause ) {
        case "restore": {
          holdIn ( "working", weibull. inverse ( Math. random () ) * 1440 );
        } break;
      }
    }
  }
  
  @Override
  public void deltint() {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out() {
    Object [] out = new Object [] { getName (),"fail", 0d };
    return send ( DEFAULT_OUT, out );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
