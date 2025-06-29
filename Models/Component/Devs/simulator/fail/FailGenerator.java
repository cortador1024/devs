package Component.Devs.simulator.fail;

import Component.Devs.lib.Reading;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class FailGenerator extends ViewableAtomic {
  
  public FailGenerator ( String n ) {
    super ( n );
    addInport ( "in" );
    addOutport ( "out" );
  }
  
  @Override
  public void initialize() {
    super. initialize();
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
  }
  
  @Override
  public void deltint() {
    super. deltint ();
  }
  
  @Override
  public message out() {
    message m = new message ();
    m. add ( makeContent ( "out", new Reading ( 0 ) ) );
    return m;
  }
  
  @Override
  public double ta () {
    return 1;
  }
  
}
