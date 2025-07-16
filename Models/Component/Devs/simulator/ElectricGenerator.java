package Component.Devs.simulator;

import Component.Devs.lib.PortValue;
import Component.Devs.probability.distribution.Weibull;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class ElectricGenerator extends ViewableAtomic {
  
  
  private final Weibull weibull = new Weibull ( 1d, 1d, true );
  
  private final double tension;
  
  private final double rate = 0.3333;
      
  public ElectricGenerator ( String n, double t ) {
    super ( String. format ( "%s Eg", n ) );
    addOutport ( "out" );
//    addOutport ( "response" );
//    addInport ( "in" );
//    addInport ( "state" );
    tension = t;
  }
  
  @Override
  public void initialize() {
    super. initialize();
    holdIn ( "wait", rate );
  }
  
  private double f ( double e ) {
//    double w0 = 2 * Math. PI * 50 ;
//    double r = Math. sin ( w0 * e ) ;
    return tension ;
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn ( "ok", rate );
  }
  
  @Override
  public message out() {
    message m = new message ();
    m. add ( makeContent ( "out", new PortValue ( f ( 0 ) ) ) );
    m. add ( makeContent ( "response", new PortValue ( getPhase () ) ) );
    holdIn ( "wait", rate );
    return m;
  }
  
  @Override
  public double ta () {
    return sigma;
  }

}
