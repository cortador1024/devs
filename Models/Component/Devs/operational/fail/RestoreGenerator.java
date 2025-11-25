/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.operational.fail;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.ElectricEvent;
import Component.Devs.lib.port.Content;
import Component.Devs.operational.GeneratorCoupled;
import Component.Devs.operational.TransformatorCoupled;
import Component.Devs.operational.probability.distribution.Uniform;
import java.util.HashMap;
import model.modeling.IODevs;
import model.modeling.message;

/**
 *
 * @author sysadmin
 */
public class RestoreGenerator extends DefaultViewableAtomic{

  private Uniform uniform;
  
  private final static String RESPONSE = "response";
  
  private final static String STATE = "state";

  private ElectricEvent event;
  
  public RestoreGenerator ( String n ) {
    super ( String. format ( "RestoreGenerator %s", n ) );
    addInport ( STATE );
    addOutport ( RESPONSE );
  }
  
  @Override
  protected void init () {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( STATE ) ). each ( ( Object o ) -> {
      event = ( ElectricEvent ) o;
      holdIn ( "work", event. duration ) ;
    } );
  }

  @Override
  public void deltint () {
    holdIn ( "wait", INFINITY ); 
  }
  
  @Override
  public message out () {
    event. level = 1;
    return send ( RESPONSE, event );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }


}
