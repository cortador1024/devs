/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.operational.fail;

import Component.Devs.lib.DefaultViewableAtomic;
import RandomNumbers.NormalDistribution;
import java.io.BufferedWriter;
import java.util.HashMap;
import model.modeling.message;

/**
 *
 * @author sysadmin
 */
public class RestoreGenerator extends DefaultViewableAtomic{

  private static final double MEAN = 480;
  
  private static final double DESVIATION = 60;
  
  private final NormalDistribution normal = new NormalDistribution ( MEAN, DESVIATION, 1 );
  
  private double rate = 1;
  
  private String state;
  
  private final static String RESPONSE = "response";
  
  private final static String STATE = "state";
  
  private final int SOURCE_FIELD = 0;
  
  private final int STATE_FIELD = 1;
  
  private final int VALUE_FIELD = 2;

  
  public RestoreGenerator ( String n ) {
    super ( String. format ( "RG.%s", n ) );
    addInport ( STATE );
    addOutport ( RESPONSE );
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
    Object [] in = ( in = ( Object [] ) map. get ( "state" ) ) != null ? in : ( Object [] ) null;
    if ( in == null ) {
      return;
    }
    String source = ( String ) in [ SOURCE_FIELD ];
    String cause = ( String ) in [ STATE_FIELD ];
    double value = ( double ) in [ VALUE_FIELD ];
    switch ( cause ) {
      case "fail": {
        holdIn ( "working", normal. get () ) ;
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
    return send ( RESPONSE, new Object [] { 
      getName (), "restore", 1d 
    } );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
