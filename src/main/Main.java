/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Component.Devs.CoupledController;
import controller.SimLauncher;
import model.simulation.coordinator;

/**
 *
 * @author sysadmin
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      // To Window
      SimLauncher launcher = new SimLauncher ();
      
      // To Console
      /*
      coordinator c = new coordinator ( new CoupledController () );
      c. initialize ();
      c. simulate ( 1000 );
      */
    } catch ( Exception ex ) {
      System. out. println ( ex. getMessage () );
    }
    
  }
  
}
