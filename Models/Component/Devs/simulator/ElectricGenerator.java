package Component.Devs.simulator;

import Component.Devs.lib.Reading;
import RandomNumbers.WeibullDistribution;
import java.util.ArrayList;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class ElectricGenerator extends ViewableAtomic {
  
  private static int counter = 0;
  
  private final WeibullDistribution distribution = new WeibullDistribution ( 1d, 1d, 1 );
  
  private int advance = 0;
  
  private double tension = 0;
  
  private int step = 0;
  
  private ArrayList < Object [] > perturbations = new ArrayList <> ();

  private double activeTension;
  
  public ElectricGenerator ( String n, double t ) {
    super ( n );
    addOutport ( "out" );
    addInport ( "in" );
    tension = t;
    step = 1;
    activeTension = t;
  }
  
  public void perturbation ( int start, int length, double value ) {
    perturbations. add ( new Object [] { start, length, value } );
  }
  
  @Override
  public void initialize() {
    super. initialize();
    sigma = advance;
    phase = "ok";
  }
  
  private double perturbation ( int e ) {
    for ( Object [] r : perturbations ) {
      int e0 = ( int ) r [ 0 ];
      int d = ( int ) r [ 1 ];
      if ( e0 <= e && e <= e0 + d ) {
        return ( double ) r [ 2 ];
      }
    }
    return ( double ) 1;
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
    sigma = advance;
    phase = "ok";
    step ++;
  }
  
  @Override
  public message out() {
    message m = new message ();
    activeTension = f ( step );
    double v = activeTension * tension;
    m. add ( makeContent ( "out", new Reading ( v ) ) );
    return m;
  }
  
  @Override
  public double ta () {
    return advance;
  }
  

}
