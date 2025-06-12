package Component.Devs;

import Component.Devs.lib.Reading;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Transformator extends ViewableAtomic {
  
  private static int counter = 0;
  
  private int advance = 0;
  
  private double nominalTension = 0;
  
  private double tensionOut = 0;
  
  private double relation;
  
  public Transformator ( String n, int e, double t0, double t1 ) {
    super ( n );
    addOutport ( "out" );
    addInport ( "in" );
    holdIn ( "wait", INFINITY );
    advance = e;
    nominalTension = t1;
    relation = ( ( Number ) t1 ). doubleValue () / ( ( Number ) t0 ). doubleValue ();
    tensionOut = 0;
  }
  
  @Override
  public void initialize() {
    super. initialize();
    
  }
  
  private double transformation ( double t ) { 
    double o = t * relation;
    return ( ( Number ) o ). doubleValue ();
  }
  
  @Override
  public void deltext(double e, message x) {
    super. deltext ( e, x );
    double tensionIn = ( ( Reading ) x. getValOnPort ( "in", 0 ) ). read;
    tensionOut = transformation ( tensionIn );
    holdIn ( "ok", 0 );
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn("wait", INFINITY);
  }
  
  @Override
  public message out() {
    message m = new message ();
    if ( ! phaseIs ( "ok" ) ) {
      return m;
    }
    m. add ( makeContent ( "out", new Reading ( tensionOut ) ) );
    return m;
  }
  
  @Override
  public double ta() {
    return advance;
  }
  

}
