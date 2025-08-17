/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.registrator;

import Component.Devs.lib.DefaultViewableAtomic;
import java.io.BufferedWriter;
import java.lang.System.Logger;
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
  
  public RegistratorController() {
    super ( "RegistratorController");
    addInport ( "in" );
  }

  @Override
  public void initialize() {
    output = null;
    time = 0;
    try {
      Path path = Paths. get ( "/var/log/devs/access.log" );
      if ( ! Files. exists ( path ) ) {
        Files. createDirectories ( path. getParent () );
      }
      output = Files. newBufferedWriter ( path, CREATE, APPEND );
      current = Calendar. getInstance (). getTime ();
      write ( "---- Session %s -----\n", df. format ( current ) );
      
    } catch ( Exception ex ) {
      
    }
  }

  @Override
  public double ta () {
    return getSigma ();
  }

  public void deltext(double e, message x) {
    super. deltext ( e, x ); 
    Continue ( e );
    try {
      time += e;
      write ( "%s;%s;received\n", time, toString ( receive ( x ) ) );
    } catch ( Exception ex ){
     
    }
  }

  
  private void write ( String format, Object ... val ) {
    try {
      output. append ( String. format ( format, val ) );
      output. flush ();
    } catch ( Exception ex ) {
    }
  }

}
