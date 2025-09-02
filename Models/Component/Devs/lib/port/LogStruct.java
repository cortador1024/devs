/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.lib.port;

import Component.Devs.lib.TextUtils;
import java.util.Objects;

/**
 *
 * @author sysadmin
 */
public class LogStruct {

  public final String source;
  
  public final Object [] values;
  
  public LogStruct ( String s, Object ... v ) {
    source = s;
    values = v;
  }
  
   
  @Override
  public int hashCode() {
    return Objects. hash ( source, values );
  }
  
  @Override
  public String toString () {
    return String. format ( "%s;%s", source, TextUtils. toString ( values ) );
  }
  
}
