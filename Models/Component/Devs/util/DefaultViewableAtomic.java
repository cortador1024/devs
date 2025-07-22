/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.util;

import Component.Devs.lib.Value;
import GenCol.entity;
import java.util.HashMap;
import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 *
 * @author sysadmin
 */
public class DefaultViewableAtomic extends ViewableAtomic {
 
  protected int [] times;
  
  protected int step = 1;
  
  public DefaultViewableAtomic ( String name ) {
    super ( name );
  }
    
  public message send ( Object ... v ) {
    message m = new message ();
    for ( int i = 0, top = v. length; i < top; i += 2 ) {
      m. add ( makeContent ( ( String ) v [ i ], new Value ( v [ i + 1 ] ) ) );
    }
    return m;
  }
  
  public HashMap < String, Object > receive ( message m )  {
    HashMap < String, Object > r = new HashMap <> ();
    for ( String n : getInportNames () ) {
      Object v = getValue ( m, n );
      r. put ( n, v );
    }
    return r;
  }
  
  private Object getValue ( message x, String k ) {
	  Object r = null;
    for ( int i = 0, top = x. size (); i < top; i ++ ) {
	    if ( ! messageOnPort ( x, k, i ) ) {
	      continue;
	    }
      entity e = x. getValOnPort ( k, i );
      if ( e != null ) {
        return ( ( Value ) e ). get ();
      }
	  }
	  return r;
	}
  
}
