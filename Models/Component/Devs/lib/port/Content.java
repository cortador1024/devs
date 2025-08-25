/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib.port;

/**
 *
 * @author sysadmin
 */
public class Content {

  public final String state;
  
  public final double value;
  
  public Content ( String s, double v ) {
    state = s;
    value = v;
  }
  
  @Override
  public String toString () {
    return String. format ( "{ %s, %s }", state, value );
  }

}
