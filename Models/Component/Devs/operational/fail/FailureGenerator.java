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
  
  private final static String RESPONSE_PORT = "response";
  
  private final static String STATE_PORT = "state";
  
  private final int SOURCE_FIELD = 0;
  
  private final int STATE_FIELD = 1;
  
  private final int VALUE_FIELD = 2;
  
  public FailureGenerator ( String n ) {
    super ( String. format ( "FG.%s", n ) );
    addInport ( STATE_PORT );
    addOutport ( RESPONSE_PORT );
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
    Object [] in = ( in = ( Object [] ) map. get ( "state" ) ) != null ? in : ( Object [] ) null;
    if ( in == null ) {
      return;
    }
    String source = ( String ) in [ SOURCE_FIELD ];
    String cause = ( String ) in [ STATE_FIELD ];
    double value = ( double ) in [ VALUE_FIELD ];
    switch ( cause ) {
      case "restore": {
        holdIn ( "working", weibull. inverse ( Math. random () ) * 1440 );
      } break;
    }
  }
  
  @Override
  public void deltint() {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out () {
    return send ( RESPONSE_PORT, new Object [] { 
      getName (), "fail", 0d 
    } );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
