package Component.Devs.simulator;

import Component.Devs.lib.Reading;
import Component.Devs.probability.distribution.Weibull;
import GenCol.entity;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class ElectricGenerator extends ViewableAtomic {
  
  
  private final Weibull weibull = new Weibull ( 1d, 1d, true );
  
  private double tension = 0;
  
  private double activeTension;
  
  private final double rate = 0.3333;
      
  public ElectricGenerator ( String n, double t ) {
    super ( String. format ( "%s Eg", n ) );
    addOutport ( "out" );
//    addOutport ( "response" );
//    addInport ( "in" );
//    addInport ( "state" );
    tension = t;
    activeTension = t;
  }
  
  @Override
  public void initialize() {
    super. initialize();
        sigma = rate;
    phase = "ok";
    // holdIn ( "ok", rate );
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
    sigma = rate;
    phase = "ok";
//    holdIn ( "ok", rate );
  }
  
  @Override
  public message out() {
    message m = new message ();
    m. add ( makeContent ( "out", new Reading ( f ( 0 ) ) ) );
    m. add ( makeContent ( "response", new entity ( getPhase () ) ) );
    return m;
  }
  
  @Override
  public double ta () {
    return sigma;
  }

}
