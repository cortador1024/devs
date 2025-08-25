/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib.port;

import Component.Devs.lib.TextUtils;
import GenCol.entity;

/**
 *
 * @author sysadmin
 */
public class InfoStruct extends entity {
  
  private final Object value ;
  
  public InfoStruct ( Object v ) {
    value = v;
  }
  
  public Object get () {
    return value;
  }
  
  @Override
  public String toString () {
    return TextUtils. toString ( value );
  }
  
}
