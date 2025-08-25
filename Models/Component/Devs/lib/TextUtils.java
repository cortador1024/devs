/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib;

import java.util.List;
import java.util.Map;

/**
 *
 * @author sysadmin
 */
public class TextUtils {
  
  public static String toString ( Object o ) {
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
  
  public static String toString ( Object [] l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Object li : l ) {
	    sb. append ( String. format ( "%s%s", toString ( li ), i < l. length - 1 ? ";" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
	}
  
  public static String toString ( List l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Object li : l ) {
	    sb. append ( String. format ( "%s%s", li, i < l. size () -1 ? ";" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
	}
  
  public static String toString ( Map l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Object li : l. keySet () ) {
	    sb. append ( String. format ( "'%s':'%s'%s", li, toString(l.get(li)), i < l. size () -1 ? ";" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
	}
  
}
