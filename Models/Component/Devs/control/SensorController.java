package Component.Devs.control;

import Component.Devs.lib.SensorStatus;
import Component.Devs.lib.Reading;
import java.util.ArrayList;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class SensorController extends ViewableAtomic	{

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
    
    private final String name ;
    
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
	public String getPhase() {
	  return String. valueOf ( phase );
	}
	
  @Override
  public double ta () {
    return sigma;
  }
	
	@Override
	public void deltext ( double e, message x ) {
	  Continue ( e );
	  Reading ze = ( Reading ) x. getValOnPort ( "tension", 0 );
	  int atCase = zl0. size () < SENSOR_MIN_READ_COUNT ? 
      1 : 
      2
    ;
	  switch ( atCase ) {
	    case 1: {
	      holdIn ( String. valueOf ( SensorPhase.WAIT ), 
          INFINITY 
        );
	    } break;
	    case 2: {
	      holdIn ( String. valueOf ( SensorPhase.STREAM ),
          // tiempo de proceso del sensor para enviar la lectura $variable
	        variable
	      );
	      zl0. remove ( 0 );
	    } break;
	  }
	  zl0. add ( ( double ) ze. read );
	}
	
	@Override
	public void deltint () {
	  int atCase = phaseIs ( String. valueOf ( SensorPhase. STREAM ) ) ? 
      2 : 
      1
    ;
	  switch ( atCase ) {
	    case 2: {
	      holdIn ( String. valueOf ( SensorPhase.WAIT ),
	        INFINITY
	      );
	      zl0. clear ();  
	    } break;
	  }
	}
	
	private String toString ( ArrayList < Double > l ) {
	  StringBuilder sb = new StringBuilder ();
	  int i = 0; for ( Double li : l ) {
	    sb. append ( String. format ( "%s%s", li, i < l. size () -1 ? "\n" : "" ) );
	    i ++;
	  }
	  String s = sb. toString ();
	  sb. setLength ( 0 );
	  return s;
	}
	
	@Override
	public message out () {
	  
	  if ( ! phaseIs ( String. valueOf ( SensorPhase.STREAM ) ) ) {
	    return super. out ();
	  }
	  zl1 = zg ( zl0 );
	  System. out. println ( String. format ( "{ zl0: [ %s ], zl1: %s }", toString ( zl0 ), zl1 ) );
    
    message m = new message ();
	  m. add ( makeContent ( "state", new SensorStatus ( zf ( zl1, NOMINAL_TENSION ), zl1 ) ) );
	  return m;
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
