/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component.Devs.operational.probability.distribution;

import java.lang.System.Logger.Level;

/**
 *
 * @author sysadmin
 */
public class Normal {
  
  protected final double media;
  
  protected final double deviation;
  
  protected final boolean accumulate;
  
  public Normal ( double m, double d, boolean ac ) {
    boolean onException = false;
    try {
      assertParams ( m, d );
    } catch ( Exception ex ) {
      onException = true;
      System. getLogger ( "Weibull" ). log ( Level.INFO, ex. getMessage (), ex );
    }
    if ( onException ) {
      media = 1;
      deviation = 1;
    } else {
      media = m;
      deviation = d;
    }
    accumulate = ac;
  }
  
  private void assertParams ( double a, double b ) throws Exception {
    if ( b <= 0 ) {
      throw new Exception ( String. format ( "Not allowed the value %s for beta", b ) );
    }
    if ( a <= 0 ) {
      throw new Exception ( String. format ( "Not allowed the value %s for alpha", a ) );
    }
  }
  
  public double direct ( double v ) {
    double c = Math. exp ( Math. pow ( v - media, 2 ) / ( 2 * ( Math. pow ( deviation, 2 ) ) ) ) / ( deviation * Math. sqrt ( 2 * Math.PI ) );
    double r = accumulate ? 
      1 - c:
      c;
    return r;
  }
  
  public double inverse ( double probability ) {
    double r = accumulate ?
      media * Math. pow (- Math.log ( 1 - probability ), 1 / deviation ) :
      media * Math. pow (- Math. log ( probability ), 1 / deviation )
    ;
    return r;
  }
  
}
