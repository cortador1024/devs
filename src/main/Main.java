/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Component.Devs.GeneralCoupled;
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
      
      if ( args. length > 0 ) {
        coordinator c = new coordinator ( new GeneralCoupled () );
        c. initialize ();
        c. simulate ( Integer. valueOf ( args [ 0 ] ) );
        return;
      }
      SimLauncher launcher = new SimLauncher ();
    } catch ( Exception ex ) {
      System. out. println ( ex. getMessage () );
    }
    
  }
  
}
