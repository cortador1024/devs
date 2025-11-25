package Component.Devs.operational.fail;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.ElectricEvent;
import Component.Devs.operational.GeneratorCoupled;
import Component.Devs.operational.TransformatorCoupled;
import Component.Devs.operational.probability.distribution.Uniform;
import Component.Devs.operational.probability.distribution.Weibull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import model.modeling.IODevs;

import model.modeling.message;

public class FailureGenerator extends DefaultViewableAtomic {
  
  private final static String RESPONSE = "response";
  
  private final static String STATE = "state";
  
  private ElectricEvent event;
  
  public FailureGenerator ( String n ) {
    super ( String. format ( "FailureGenerator %s", n ) );
    addInport ( STATE );
    addOutport ( RESPONSE );
  }
  
  @Override
  public void init () {
    event = event ();
    holdIn ( "wait", event. start );
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( STATE ) ). each ( ( Object o ) -> {
      ElectricEvent in = ( ElectricEvent ) o;
      if ( ! Objects. equals ( in, event ) ) {
        return;
      }
      event = event ();
      holdIn ( "work", event. start );
    } );
  }
  
  @Override
  public void deltint () {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out () {
    return send ( RESPONSE, event );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
