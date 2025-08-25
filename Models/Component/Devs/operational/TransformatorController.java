/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.operational;

import Component.Devs.operational.ElectricTransformator;
import Component.Devs.operational.fail.FailureGenerator;
import Component.Devs.operational.fail.RestoreGenerator;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedWriter;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

/**
 *
 * @author sysadmin
 */
public class TransformatorController extends ViewableDigraph{

  private ElectricTransformator e0;
  private FailureGenerator f0;
  private RestoreGenerator r0;

  
  public TransformatorController () {
    this ( "t0", 13d, 33d );
  }
  
  public TransformatorController( String n, double t0, double t1 ) {
    super ( n );
    addInport ( "tension" );
    addOutport ( "otension" );
    addOutport ( "log" );
    
    add ( e0 = new ElectricTransformator ( n + "/t0", t0, t1 ) );
    add ( f0 = new FailureGenerator ( n + "/f0" ) );
    add ( r0 = new RestoreGenerator ( n + "/r0" ) );
    
    addCoupling ( this, "tension", e0, "tension" );
    addCoupling ( e0, "otension", this, "otension" );
    
    addCoupling ( e0, "log", this, "log" );
    
    addCoupling ( f0, "response", e0, "state" );
    addCoupling ( f0, "response", r0, "state" );
    addCoupling ( r0, "response", e0, "state" );
    addCoupling ( r0, "response", f0, "state" );
    
  }

}
