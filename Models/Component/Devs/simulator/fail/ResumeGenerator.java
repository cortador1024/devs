/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.simulator.fail;

import Component.Devs.lib.DevRegistry;
import Component.Devs.lib.Reading;
import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 *
 * @author sysadmin
 */
public class ResumeGenerator extends ViewableAtomic{

  public ResumeGenerator ( String n ) {
    super ( DevRegistry. register ( ResumeGenerator.class ) );
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
