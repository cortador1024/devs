package Component.Devs.control;

import static Component.Devs.control.SensorController.SensorPhase.STREAM;
import static Component.Devs.control.SensorController.SensorPhase.WAIT;
import Component.Devs.lib.DefaultViewableAtomic;
import java.util.ArrayList;
import java.util.HashMap;

import model.modeling.message;

public class SensorController extends DefaultViewableAtomic	{

  private double variable;
  
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
    
  }

  private final int SENSOR_MIN_READ_COUNT = 10;
  
  private final double NOMINAL_TENSION;
  
  private ArrayList < Double > zl0 = new ArrayList <> ();
  
  private double zl1 = 0;

	public SensorController ( String name, double nt ) {
		super ( String. format ( "Sensor %s %s", name, nt ) );
		NOMINAL_TENSION = nt;
		addInport ( "tension" );
		addOutport ( "state" );
	}

	@Override
  public void initialize() {
	  holdIn ( String. valueOf ( SensorPhase.WAIT ), INFINITY );
  }
	
  @Override
  public double ta () {
    return getSigma ();
  }
	
	@Override
	public void deltext ( double e, message x ) {
	  Continue ( e );
    HashMap < String, Object > msg = receive ( x );
    Object val = null;
    val = msg. get ( "tension" );
    if ( val == null ) {
      return;
    }
    zl0. add ( ( double ) val );
    if ( zl0. size () > SENSOR_MIN_READ_COUNT ) {
      holdIn ( String. valueOf ( SensorPhase.STREAM ), 1 );
      zl0. remove ( 0 );
      return;
    } 
    holdIn ( String. valueOf ( SensorPhase.WAIT ), INFINITY );
	}
	
	@Override
	public void deltint () {
    if ( ! phaseIs ( STREAM. name ) ) {
      return;
    }
    holdIn ( WAIT. name, INFINITY );
    zl0. clear ();  
	}
	
	@Override
	public message out () {
	  if ( ! phaseIs ( STREAM. name ) ) {
	    return super. out ();
	  }
	  zl1 = zg ( zl0 );
    Object [] out = new Object [] { getName (), zf ( zl1, NOMINAL_TENSION ), zl1 };
    return send ( "state", out );
	}
  
	@Override
	public String getTooltipText() {
	  return toString ( zl0 );
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
	  SensorState r = SensorState.NORMAL;
	  r = ( Math. abs ( x ) <= 0.1 * v ) ? SensorState.INTERRUPTION :
	    ( Math. abs ( x ) <= 0.8 * v ) ? SensorState.LOW_TENSION :
	    ( Math. abs ( x ) <= 1.1 * v ) ? SensorState.NORMAL :
	    SensorState.HIGH_TENSION;
	  return r;
	}
	
}
