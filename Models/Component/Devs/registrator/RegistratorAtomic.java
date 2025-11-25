/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.registrator;

import Component.Devs.control.SensorAtomic.SensorState;
import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.TextUtils;
import Component.Devs.lib.port.LogStruct;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.WARNING;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import model.modeling.message;

/**
 *
 * @author sysadmin
 */
public class RegistratorAtomic extends DefaultViewableAtomic {

  private BufferedWriter logContent;
  
  private BufferedWriter outContent;
  
  private Logger log = System. getLogger ( "default" );
  
  private SimpleDateFormat sdf = new SimpleDateFormat ( "dd/MM/yyyy HH:mm:ss" );

  private double time;
  
  private Date current;
  
  private String [] EMPTY_STRING = new String [ 0 ];
  
  private LinkedHashMap < Integer, SegmentRecord > segments = new LinkedHashMap <> ();
  
  private DecimalFormat snf = TextUtils. decimalFormat ( ',', 2 );
  
  public RegistratorAtomic( String n ) {
    super ( String. format ( "RegistratorAtomic %s", n ) );
    addInport ( "in" );
  }
  
  protected void init () {
    logContent = null;
    time = 0d;
    segments. put ( 0, new SegmentRecord ( "s01", "normal", 0 ) );
    segments. put ( 1, new SegmentRecord ( "s11", "normal", 0 ) );
    segments. put ( 2, new SegmentRecord ( "s12", "normal", 0 ) );
    
    Path logPath = Paths. get ( "/var/log/devs/access.log" );
    Path outPath = Paths. get ( "/var/log/devs/interruptions.log" );
    try {
      if ( ! Files. exists ( outPath ) ) {
        Files. createDirectories ( outPath. getParent () );
      }
      logContent = Files. newBufferedWriter ( logPath, CREATE, TRUNCATE_EXISTING );
      if ( ! Files. exists ( logPath ) ) {
        Files. createDirectories ( logPath. getParent () );
      }
      outContent = Files. newBufferedWriter ( outPath, CREATE, TRUNCATE_EXISTING );
      write ( "INIT;DURATION;SEGMENT;TYPE" );
      current = Calendar. getInstance (). getTime ();
      log ( "---- Session %s -----", sdf. format ( current ) );
    } catch ( IOException ex ) {
      log. log ( WARNING, String. format ( "Couldn't open the %s file for appending data", String. valueOf ( outPath ) ), ex. getMessage () );
    }
  }

  @Override
  public double ta () {
    return getSigma ();
  }

  @Override
  public void deltext ( double e, message x ) {
    super. deltext ( e, x ); 
    Continue ( e );
    time += e;
    current = Calendar. getInstance (). getTime ();
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( "in" ) ). each ( ( Object v ) -> {
      log ( "%s;received;%s", snf. format ( time ), TextUtils. toString ( v ) );
      if ( v instanceof LogStruct == false ) {
        return;
      }
      LogStruct ls = ( LogStruct ) v;
      String [] path = parsePath ( ls. source );
      if ( path. length == 0 ) {
        return;
      }
      String [] component = parseLastComponent ( path );
      if ( component == null ) {
        return;
      }
      String name = component [ 1 ];
      String type = component [ 0 ];
      if ( ! "LineController". contentEquals (  type ) ) {
        return;
      }
      Object [] segmentsInfo = ( Object [] ) ls. values [ 2 ];
      for ( int i = 0, top = segmentsInfo. length; i < top; i ++ ) {
        SegmentRecord segment = segments. get ( i );
        Object [] info = ( Object [] ) segmentsInfo [ i ];
        String state = info != null ? ( ( SensorState ) info [ 0 ]  ). name :
          "normal" ;
        double duration = segment. state ( state, time - 1 );
        if ( duration <= 0 ) {
          continue;
        }
        write ( "%s;%s;%s;%s", snf. format ( segment. init ), snf. format ( duration ), segment.name, segment. state );
      }
    } );
   
  }

  private void write ( String format, Object ... val ) {
    try {
      outContent. append ( String. format ( format, val ) );
      outContent. newLine();
      outContent. flush ();
    } catch ( IOException ex ) {
      log. log ( WARNING, String. format ( format, val ), ex. getMessage () );
    }
  }
  
  private void log ( String format, Object ... val ) {
    try {
      logContent. append ( String. format ( format, val ) );
      logContent. newLine();
      logContent. flush ();
    } catch ( IOException ex ) {
      log. log ( WARNING, String. format ( format, val ), ex. getMessage () );
    }
  }

  private String[] parsePath(String source) {
    if ( source == null || source. isEmpty () ) {
      return EMPTY_STRING;
    }
    return source. split ( "/" );
  }

  private String [] parseLastComponent ( String [] path ) {
    if ( path. length == 0 ) {
      return null;
    }
    String component = path [ path. length - 1 ];
    return component. split ( " " );
  }

  private static class SegmentRecord {

    final String name;
    
    String current;
    
    double init;
    
    String state;
    
    double time;
    
    public SegmentRecord ( String n, String s, double e  ) {
      name = n;
      current = s;
      state = s;
      time = e;
    }
    
    public double state ( String s, double t ) {
      if ( current. contentEquals ( s ) ) {
        return 0d;
      }
      state = current;
      init = time;
      current = s;
      double interval = t - time ;
      time = t;
      return interval;
    }
  }

}
