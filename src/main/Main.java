/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.SimLauncher;

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
      SimLauncher launcher = new SimLauncher ();
    } catch ( Exception ex ) {
      System. out. println ( ex. getMessage () );
    }
    
  }
  
}
