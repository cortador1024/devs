/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib;

import static model.modeling.DevsInterface.INFINITY;

/**
 *
 * @author sysadmin
 */
public class ElectricEvent {
  
  public double start;
  
  public double duration;
    
  public double level;
  
  public ElectricEvent ( double s, double d, double l ) {
    start = s;
    duration = d;
    level = l;
  }
  
  public ElectricEvent ( double s, double d ) {
    start = s;
    duration = d;
    level = 0;
  }

  public ElectricEvent() {
    start = INFINITY;
    duration = 0;
    level  = 0;
  }
  
}
