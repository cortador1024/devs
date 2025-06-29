/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib;

import java.util.HashMap;

/**
 *
 * @author sysadmin
 */
public class DevRegistry {
  
  static public final HashMap < Class, Integer > registry = new HashMap <> ();
  
  static public String register ( Class c ) {
    int o = registry. get ( c ) == null ? 0 : ( int ) registry. get ( c ) ;
    Object t = registry. put ( c, o + 1 );
    return String. format ( "%s%s", c.getSimpleName(), o );
  }
  
}
