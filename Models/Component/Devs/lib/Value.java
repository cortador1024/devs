/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib;

import GenCol.entity;

/**
 *
 * @author sysadmin
 */
public class Value extends entity {

  private final Object value;
  
  public Value ( Object v ) {
    value = v;
  }
  
  public Object get () {
    return value;
  }

  @Override
  public String toString() {
    return String. valueOf ( value );
  }
  
}
