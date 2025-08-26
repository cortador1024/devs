package Component.Devs.control;

import static Component.Devs.control.SensorController.SensorPhase.STREAM;
import static Component.Devs.control.SensorController.SensorPhase.WAIT;
import static Component.Devs.control.SensorController.SensorState.HIGH_TENSION;
import static Component.Devs.control.SensorController.SensorState.INTERRUPTION;
import static Component.Devs.control.SensorController.SensorState.LOW_TENSION;
import static Component.Devs.control.SensorController.SensorState.NORMAL;
import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.TextUtils;
import Component.Devs.lib.port.LogStruct;
import Component.Devs.operational.probability.distribution.Weibull;
import java.util.ArrayList;
import java.util.HashMap;

import model.modeling.message;

public class SensorController extends DefaultViewableAtomic	{

  private static final double ALPHA = 0.547;
  
  private static final double BETA = 1.180;
  
  private final Weibull weibull = new Weibull ( ALPHA, BETA, true );
  
  
  private final String IN = "tension";
  
  private final String OUT = "ostate";
  
  private final String LOG = "log";
  
  public enum SensorPhase {
    
    WAIT ( "wait" ),
    STREAM ( "stream" );
    
    private final String name;
    
    SensorPhase ( String n ) {
      name = n;
    }
    
    @Override
    public String toString() {
      return name;
    }
    
  }
  
  public enum SensorState {
    
    NORMAL ( "normal" ),
    
    LOW_TENSION ( "baja_tension" ),
    
    HIGH_TENSION ( "alta_tension" ),
    
    INTERRUPTION ( "interrupcion" ),
    
    ERROR  ( "error" );
    
    public final String name ;
    
    SensorState ( String n ) {
      name = n;
    }

    @Override
    public String toString() {
      return name;
    }
    
    static SensorState fromInt ( int i ) {
      return values () [ i ];
    }
    
  }

  private final int SENSOR_MIN_READ_COUNT = 10;
  
  private final double NOMINAL_TENSION;
  
  private double variable;
  
  private ArrayList < Double > zl0 = new ArrayList <> ();
  
  private double zl1 = 0;

	public SensorController ( String name, double nt ) {
		super ( String. format ( "SensorController %s (%s)", name, nt ) );
		NOMINAL_TENSION = nt;
		addInport ( IN );
		addOutport ( OUT );
    addOutport ( LOG );
	}

	@Override
  public void initialize () {
	  // holdIn ( WAIT, 14 + Math. random () * 10 );
    holdIn ( WAIT, INFINITY );
  }
	
  @Override
  public double ta () {
    return getSigma ();
  }
	
	@Override
	public void deltext ( double e, message x ) {
	  Continue ( e );
    HashMap < String, Object > msg = receive ( x );
    over ( msg. get ( "tension" ) ). each ( ( Object val ) -> {
      zl0. add ( ( double ) val );
    } );
    holdIn ( WAIT, INFINITY );
    if ( zl0. size () < SENSOR_MIN_READ_COUNT ) {
      return;
    } 
    zl0. remove ( 0 );
    holdIn ( STREAM, 1 /* weibull. inverse ( Math. random () ) */ );
	}
	
	@Override
	public void deltint () {
    if ( phaseIs ( STREAM ) ) {
      zl0. clear ();  
    }
    holdIn ( WAIT, INFINITY );
	}
	
	@Override
	public message out () {
	  zl1 = zg ( zl0 );
    return send ( OUT, new Object [] { zf ( zl1, NOMINAL_TENSION ), zl1 }, 
      LOG, new LogStruct ( tag (), getPhase (), "active", new Object [] { copy ( zl0 ), zl1 } ) 
    );
	}
  
	@Override
	public String getTooltipText() {
	  return TextUtils. toString ( zl0 );
	}
	
	private double zg ( ArrayList l ) {
	  if ( l == null || l. isEmpty () ) {
	    return 0;
	  }
	  if ( l. size () == 1 ) {
	    return ( double ) l. get ( 0 );
	  }
	  double r = 0;
	  for ( Object o : l ) {
	    r += ( double ) o;
	  }
	  return r / l. size ();
	}
	
	private SensorState zf ( double x, double v ) {
	  SensorState r = NORMAL;
	  r = ( Math. abs ( x ) <= 0.1 * v ) ? 
        INTERRUPTION :
	    ( Math. abs ( x ) <= 0.8 * v ) ? 
        LOW_TENSION :
	    ( Math. abs ( x ) > 1.1 * v ) ? 
        HIGH_TENSION
      : r
    ;
	  return r;
	}
	
}
