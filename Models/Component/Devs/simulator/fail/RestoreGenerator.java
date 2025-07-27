/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.simulator.fail;

import Component.Devs.probability.distribution.Weibull;
import Component.Devs.util.DefaultViewableAtomic;
import java.util.HashMap;
import model.modeling.MessageInterface;
import model.modeling.message;

/**
 *
 * @author sysadmin
 */
public class RestoreGenerator extends DefaultViewableAtomic{

  private static final double ALPHA = 46.047;
  
  private static final double BETA = 1.180;
  
  private Weibull weibull = new Weibull ( ALPHA, BETA, false );
  
  private double rate = 1;
  
  private String state;
  
  private final static String DEFAULT_OUT = "response";
  
  private final static String DEFAULT_IN = "state";
  
  public RestoreGenerator ( String n ) {
    super ( String. format ( "%s Restore", n ) );
    addInport ( DEFAULT_IN );
    addOutport ( DEFAULT_OUT );
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public void initialize() {
    
  }
  
  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x );
    Continue ( e );
    HashMap < String, Object > map = receive ( x );
    state = ( String ) map. get ( DEFAULT_IN );
    switch ( state ) {
      case "fail": {
        holdIn ( "working", weibull. inverse ( Math. random () ) );
      } break;
    } 
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out() {
    return send ( DEFAULT_OUT, "restore" );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }
}
