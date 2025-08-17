/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib;

import Component.Devs.operational.port.Value;
import GenCol.entity;
import java.io.BufferedWriter;
import java.lang.System.Logger.Level;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 *
 * @author sysadmin
 */
public class DefaultViewableAtomic extends ViewableAtomic {
 
  public DefaultViewableAtomic ( String name ) {
    super ( name );
  }
  
  protected String toString ( Object o ) {
    if ( o instanceof Object [] ) {
      return toString ( ( Object [] ) o );
    } 
    if ( o instanceof List ) {
      return toString ( ( List ) o );
    }
    if ( o instanceof Map ) {
      return toString ( ( Map ) o );
    }
    return String. valueOf ( o );
  }
  
  protected String toString ( Object [] l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Object li : l ) {
	    sb. append ( String. format ( "%s%s", toString ( li ), i < l. length - 1 ? ";" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
	}
  
  protected String toString ( List l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Object li : l ) {
	    sb. append ( String. format ( "%s%s", li, i < l. size () -1 ? ";" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
	}
  
  protected String toString ( Map l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Object li : l. keySet () ) {
	    sb. append ( String. format ( "'%s':'%s'%s", li, toString(l.get(li)), i < l. size () -1 ? ";" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
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
