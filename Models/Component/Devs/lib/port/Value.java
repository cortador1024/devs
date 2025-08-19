/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib.port;

import GenCol.entity;

/**
 *
 * @author sysadmin
 */
public class Value extends entity {
  
  private final Object value ;
  
  public Value ( Object v ) {
    value = v;
  }
  
  public Object get () {
    return value;
  }
  
}
