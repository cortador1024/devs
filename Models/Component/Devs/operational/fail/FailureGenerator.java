package Component.Devs.operational.fail;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.port.Content;
import Component.Devs.operational.probability.distribution.Weibull;
import java.util.HashMap;

import model.modeling.message;

public class FailureGenerator extends DefaultViewableAtomic {
  
  private static final double ALPHA = 10.047;
  
  private static final double BETA = 1.180;
  
  private final Weibull weibull = new Weibull ( ALPHA, BETA, true );
  
  private final static String RESPONSE = "response";
  
  private final static String STATE = "state";
  
  public FailureGenerator ( String n ) {
    super ( String. format ( "FailureGenerator %s", n ) );
    addInport ( STATE );
    addOutport ( RESPONSE );
  }
  
  @Override
  public void initialize() {
    holdIn ( "working", 30 );
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( STATE ) ).each ( ( Object o ) -> {
      Content in = ( Content ) o;
      switch ( in. state ) {
        case "restore": {
          holdIn ( "working", 30 );
        } break;
      }
    } );
  }
  
  @Override
  public void deltint () {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out () {
    return send ( RESPONSE, new Content ( "fail", 0 ) );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
