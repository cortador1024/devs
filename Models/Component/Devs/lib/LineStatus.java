package Component.Devs.lib;

import java.util.ArrayList;

import GenCol.entity;

public class LineStatus extends entity {
  
  public final ArrayList < SensorStatus > state;
  
  public LineStatus ( ArrayList < SensorStatus > sl ) {
    super ( "LineStatus 0" );
    state = sl;
  }
  
  private String toLineType ( int i ) {
    return i == 0 ? "Linea": "Derivacion";
  }
  
  private String toJson () {
    StringBuilder sb = new StringBuilder ( "" );
    sb. append ( "{" );
    int i = 0; for ( SensorStatus s : state ) {
      if ( s == null ) {
        i ++;
        continue;
      }
      sb. append ( String. format ( "%s: { %s, %s, ( %s, %s ), %s, %s }%s",
       i,
       String. valueOf ( s. state ), 
       s. read, 
       "gx" + i, 
       "gy" + i, 
       toLineType ( i ), 
       i,
       i < state.size ()-1 ? "," : ""
      ) );
      i ++;
    }
    sb. append ( "}" );
    String s = sb. toString ();
    sb. setLength ( 0 );
    return s;
  }
  
  @Override
  public String getName () {
    return String. format ( "LineStatus %s", state != null ?  
      toJson () :
      "{}"
    );
  }

}
