/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.operational.fail;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.port.Content;
import Component.Devs.operational.probability.distribution.Weibull;
import java.util.HashMap;
import model.modeling.message;

/**
 *
 * @author sysadmin
 */
public class RestoreGenerator extends DefaultViewableAtomic{

  private static final double ALPHA = 3.047;
  
  private static final double BETA = 1.180;
  
  private final Weibull weibull = new Weibull ( ALPHA, BETA, true );
  
  private final static String RESPONSE = "response";
  
  private final static String STATE = "state";
  
  private int min;
  
  private int max;
  
  public RestoreGenerator ( String n, int m0, int m1 ) {
    super ( String. format ( "RestoreGenerator %s", n ) );
    addInport ( STATE );
    addOutport ( RESPONSE );
    min = m0;
    max = m1;
  }
  
  @Override
  public void initialize() {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( STATE ) ). each ( ( Object o ) -> {
      Content in = ( Content ) o;
      switch ( in. state ) {
        case "fail": {
          holdIn ( "working", Math. floor ( Math. random () * ( max - min ) ) ) ;
        } break;
      }
    } );
  }
  
  @Override
  public void deltint () {
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out () {
    return send ( RESPONSE, new Content ( "restore", 1d ) );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
