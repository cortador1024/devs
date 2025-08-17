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
  
  private final static String DEFAULT_OUT = "response";
  
  private final static String DEFAULT_IN = "state";
  
  public RestoreGenerator ( String n ) {
    super ( String. format ( "RG.%s", n ) );
    addInport ( DEFAULT_IN );
    addOutport ( DEFAULT_OUT );
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
    Object [] inState = ( inState = ( Object [] ) map. get ( "state" ) ) != null ? inState : ( Object [] ) null;
    if ( inState != null ) {
      String cause = ( String ) inState [ 0 ];
      switch ( cause ) {
        case "fail": {
          holdIn ( "working", normal. get () ) ;
        } break;
      }
    }
  }
  
  @Override
  public void deltint() {
    super. deltint ();
    holdIn ( "wait", INFINITY );
  }
  
  @Override
  public message out() {
    Object [] out = new Object [] { getName (), "restore", 1d };
    return send ( DEFAULT_OUT, out );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }

}
