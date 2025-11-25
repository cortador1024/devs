/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib;

import Component.Devs.lib.port.InfoStruct;
import Component.Devs.operational.GeneratorCoupled;
import Component.Devs.operational.TransformatorCoupled;
import GenCol.entity;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import model.modeling.IODevs;
import model.modeling.message;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

/**
 *
 * @author sysadmin
 */
public class DefaultViewableAtomic extends ViewableAtomic {
 
  private int state = 0;
  
  public DefaultViewableAtomic ( String name ) {
    super ( name );
  }
  
  @Override
  public void initialize () {
    if ( state == 1 ) {
      return;
    }
    state = 1;
    init ();
  }
  
  private Method method ( Object o, String name, Class ... c ) {
    if ( o == null ) {
      return null;
    }
    Method r = null;
    Class parent = o. getClass ();
    boolean finish = false;
    while ( parent != null ) {
      try {
        r = parent. getDeclaredMethod ( name );
      } catch ( Exception ex ) {

      }
      if ( r == null ) {
        parent = parent. getSuperclass ();
        continue;
      }
      break;
    }
    return r;
  }
  
  public ElectricEvent event () {
    ElectricEvent e = null;
    try {
      Method method = method ( getMyParent (), "event" );
      if ( method == null ) {
        throw new Exception ();
      }
      e = ( ElectricEvent ) method. invoke ( getParent () );
    } catch ( Exception ex ) {
      int a = 0;
    }
    return e == null ? new ElectricEvent () : e;
  }
  
  
  public String tag () {
    String n = super. getName ();
    String pn = "";
    ViewableDigraph p = getMyParent ();
    while ( p != null ) {
      pn = p. getName () + "/" + pn ;
      p = p. getMyParent ();
    }
    return String. format ( "%s%s", pn, n );
  }
  
  protected void holdIn ( Object o, double d ) {
    super. holdIn ( String. valueOf ( o ), d );
  }

  protected boolean phaseIs ( Object o ) {
    return super. phaseIs ( String. valueOf ( o ) );
  }
  
  public message send ( Object ... v ) {
    message m = new message ();
    for ( int i = 0, top = v. length; i < top; i += 2 ) {
      m. add ( makeContent ( ( String ) v [ i ], new InfoStruct ( v [ i + 1 ] ) ) );
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

  protected void init() {

  }
  
  public class IterationBlock {
    
    Object [] values;
    
    public IterationBlock ( Object o ) {
      if ( o instanceof Object [] ) {
        values = ( Object [] ) o;
        return;
      }
      values = new Object [] { o };
    }
    
    public IterationBlock ( Object [] ol ) {
      values = ol;
    }
    
    public void onException ( Object o ) {
      
    }
    
    public IterationBlock each ( Consumer < Object > consumer ) {
      for ( Object oi : values ) try {
        consumer. accept ( oi );
      } catch ( Exception ex ) {
        onException ( oi );
      }
      return this;
    }
  }
  
  protected IterationBlock over ( Object o ) {
    return new IterationBlock ( o );
  }
  
  private Object getValue ( message x, String k ) {
    HashSet l = new HashSet ();
    for ( int i = 0, top = x. size (); i < top; i ++ ) {
	    if ( ! messageOnPort ( x, k, i ) ) {
	      continue;
	    }
      entity e = x. getValOnPort ( k, i );
      if ( e != null ) {
        l. add ( ( ( InfoStruct ) e ). get () );
      }
	  }
	  return l. toArray ();
	}

  protected Object ifNull ( Object v, Object df ) {
    return ( v == null ) ? df : v;
  }
  
  protected Object [] copy ( Object [] source ) {
    Object [] r = new Object [ source. length ];
    for ( int i = 0, top = source. length; i < top; i ++ ) {
      r [ i ] = source [ i ];
    }
    return r;
  }
  
  protected Object [] copy ( List source ) {
    Object [] r = new Object [ source. size () ];
    for ( int i = 0, top = source. size (); i < top; i ++ ) {
      r [ i ] = source. get ( i );
    }
    return r;
  }

}
