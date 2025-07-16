package Component.Devs.simulator.fail;

import Component.Devs.lib.Reading;
import Component.Devs.probability.distribution.Weibull;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class FailureGenerator extends ViewableAtomic {
  
  private static final double ALPHA = 46.047;
  
  private static final double BETA = 1.180;
  
  private Weibull weibull = new Weibull ( ALPHA, BETA, true );
  
  public FailureGenerator ( String n ) {
    super ( String. format ( "%s Fail", n ) );
    addInport ( "state" );
    addOutport ( "response" );
  }
  
  @Override
  public void initialize() {
    super. initialize ();
    holdIn ( "wait", weibull. inverse ( Math. random () ) );
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn ( "fail", weibull. inverse ( Math. random () ) ); 
  }
  
  @Override
  public message out() {
    message m = new message ();
    m. add ( makeContent ( "out", new Reading ( 0 ) ) );
    return m;
  }
  
  @Override
  public double ta () {
    return sigma;
  }
  
}
