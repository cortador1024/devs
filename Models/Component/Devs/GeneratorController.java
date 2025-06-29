/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs;

import Component.Devs.simulator.CoupledSimulator;
import Component.Devs.simulator.ElectricGenerator;
import Component.Devs.simulator.ElectricTransformator;
import java.awt.Dimension;
import java.awt.Point;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

/**
 *
 * @author sysadmin
 */
public class GeneratorController extends ViewableDigraph{

  private final CoupledSimulator cs0;

  public GeneratorController() {
    super ( "GeneratorController" );
    addOutport ( "out" );
    add ( cs0 = new CoupledSimulator ( "Cs0", new ElectricGenerator ( "Eg0", 33 ) ) );
    addCoupling ( cs0, "out", this, "out" );
  }
  
}
