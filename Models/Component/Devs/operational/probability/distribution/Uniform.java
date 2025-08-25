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
public class Uniform {
  
  protected final double alpha;
  
  protected final double beta;
  
  protected final boolean accumulate;
  
  public Uniform ( double a, double b, boolean ac ) {
    boolean onException = false;
    try { assertParams ( a, b ); } catch ( Exception ex ) {
      System. getLogger ( "Weibull" ). log ( Level.INFO, ex. getMessage (), ex );
    }
    accumulate = ac;
    if ( onException == false ) {
      alpha = a;
      beta = b;
      return;
    } 
    alpha = 1;
    beta = 1;    
  }
  
  private void assertParams ( double a, double b ) throws Exception {
    if ( b <= 0 ) {
      throw new Exception ( String. format ( "Not allowed the value %s for beta", b ) );
    }
    if ( a <= 0 ) {
      throw new Exception ( String. format ( "Not allowed the value %s for alpha", a ) );
    }
  }
  
  public double direct ( double x ) {
    double r = accumulate ? 
      1 - Math. exp ( Math. pow ( x / alpha, beta ) ) :
      Math. exp ( Math. pow ( x / alpha, beta ) );
    return r;
  }
  
  public double inverse ( double probability ) {
    double r = accumulate ?
      alpha * Math. pow ( - Math.log ( 1 - probability ), 1 / beta ) :
      alpha * Math. pow ( - Math. log ( probability ), 1 / beta )
    ;
    return r;
  }
  
}
