/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.registrator;

import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.TextUtils;
import GenCol.entity;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import model.modeling.message;

/**
 *
 * @author sysadmin
 */
public class RegistratorController extends DefaultViewableAtomic {

  private BufferedWriter output;
  
  private Logger log = System. getLogger ( "default" );
  
  private SimpleDateFormat df = new SimpleDateFormat ( "dd/MM/yyyy HH:mm:ss" );

  private long time;
  
  private Date current;
  
  public RegistratorController( String n ) {
    super ( String. format ( "RegistratorController %s", n ) );
    addInport ( "in" );
  }

  @Override
  public void initialize() {
    output = null;
    time = 0;
    Path path = Paths. get ( "/var/log/devs/access.log" );
    try {
      
      if ( ! Files. exists ( path ) ) {
        Files. createDirectories ( path. getParent () );
      }
      output = Files. newBufferedWriter ( path, CREATE, APPEND );
      current = Calendar. getInstance (). getTime ();
      write ( "---- Session %s -----\n", df. format ( current ) );
      
    } catch ( IOException ex ) {
      log. log ( Level.WARNING, String. format ( "Couldn't open the %s file for appending data", String. valueOf ( path ) ), ex. getMessage () );
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
    String InPortInName = "in";
    x.forEach( ( Object o ) -> {
      log. log ( Level.INFO, "" );
    });
    for ( int i = 0; i < x.size(); i++ )
      {
        if ( messageOnPort( x, InPortInName, i ) )
        {
          entity ent = x.getValOnPort( InPortInName, i );
          write ( "%s;received;%s;%s\n", time, df. format ( current ), TextUtils. toString ( ent ) );
          ent.removeSelf(x);
        }
      }
    /*
    HashMap < String, Object > map = receive ( x );
    over ( map. get ( "in" ) ). each ( ( Object v ) -> {
      current = Calendar. getInstance (). getTime ();
      write ( "%s;received;%s;%s\n", time, df. format ( current ), TextUtils. toString ( v ) );
    } );
    */
  }

  
  private void write ( String format, Object ... val ) {
    try {
      output. append ( String. format ( format, val ) );
      output. flush ();
    } catch ( IOException ex ) {
      log. log ( Level.WARNING, String. format ( format, val ), ex. getMessage () );
    }
  }

}
