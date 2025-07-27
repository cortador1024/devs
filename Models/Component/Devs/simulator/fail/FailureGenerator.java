package Component.Devs.simulator.fail;

import Component.Devs.util.DefaultViewableAtomic;
import Component.Devs.probability.distribution.Weibull;
import RandomNumbers.WeibullDistribution;
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
  
  private final WeibullDistribution wdist;
  
  public FailureGenerator ( String n ) {
    super ( String. format ( "%s Fail", n ) );
    addInport ( DEFAULT_IN );
    addOutport ( DEFAULT_OUT );
    wdist = new WeibullDistribution ( ALPHA, BETA, 1);
    holdIn ( "working", weibull. inverse ( Math. random () ) ); 
  }
  
  @Override
  public void initialize() {
    
  }
  
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    String state = ( String ) map. get ( DEFAULT_IN );
    switch ( state ) {
      case "restore": {
        double d = wdist. get ();
        d =  weibull. inverse ( Math. random () );
        holdIn ( "working",d );
      } break;
    } 
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out() {
    return send ( DEFAULT_OUT, "fail", "level", 1 );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }
  
}
