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

  private final Object value;
  
  public Value ( Object v ) {
    super ( "Value:" );
    value = v;
  }
  
  public Object get () {
    return value;
  }

  @Override
  public String toString() {
    return String. format ( "%s = %s", name, toText ( value ) );
  }
  
  private String toText ( Object v ) {
    if ( v instanceof Object [] ) {
      Object [] va = ( Object [] ) v;
      StringBuilder b = new StringBuilder ();
      for ( int i = 0, top = va. length; i < top; i ++ ) {
        b. append ( String. valueOf ( va [ i ] ). concat ( i < top - 1 ? ", " : "" ) );
      }
      String s = b. toString ();
      b. setLength ( 0 );
      return s;
    }
    return String. valueOf ( v );
  }
  
}
