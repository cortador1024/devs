package Component.Devs;

import Component.Devs.lib.SensorStatus;
import Component.Devs.lib.LineStatus;
import Component.Devs.SensorController.SensorPhase;
import Component.Devs.SensorController.SensorState;
import java.util.ArrayList;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class LineController extends ViewableAtomic 	{
  
  public enum LinePhase {
    
    WAIT ( "wait" ),
    STREAM ( "stream" );
    
    private final String name;
    
    LinePhase ( String n ) {
      name = n;
    }
    
    @Override
    public String toString() {
      return name;
    }  
     
  }
  
  private ArrayList < SensorStatus > queue = new ArrayList <> ();
	
  private ArrayList < SensorStatus > yl0 = new ArrayList <> ();
  
  private ArrayList < Integer > yl1 = new ArrayList <> ();
  
	public LineController ( String name) {
		super ( String. format ( "LineController %s", name ) );
		addInport ( "stateIn0" );
		addInport ( "stateIn1" );
		addInport ( "stateIn2" );
		addOutport ( "stateOut" );
	}
	
	@Override
  public void initialize() {
	  
	  holdIn ( String. valueOf ( LinePhase.WAIT ), INFINITY );
	  clearList ( queue );
	  clearList ( yl0 );
  }
	
	@Override
  public double ta() {
    return sigma;
  }
	
  @Override
  public String getPhase() {
    return String. valueOf ( phase );
  }
  
  public int yi ( SensorState s ) {
    return s. ordinal ();
  }
  
  public int yh ( SensorState s ) {
    int o = yi ( s );
    return o != 0 ? 
      o : 
      -1
    ;
  }
	  
	public SensorStatus yg ( SensorStatus x ) {
	  return yh ( x. state ) == -1 ? 
      null : 
	    x
    ;
	}
	
	public SensorStatus yf ( SensorStatus x, SensorStatus y ) {
	  if ( yh ( x. state ) != 4 && yh ( x. state ) <= yh ( y. state ) ) {
      return y;
    }
	  if ( yh ( x. state ) != 4 && yh ( y. state ) == -1 ) {
	    return new SensorStatus ( SensorState.ERROR, y. read );
    }
	  return new SensorStatus ( SensorState.ERROR, y. read );
	}
	
	private boolean isQueueSet ( ArrayList < SensorStatus > q ) {
	  int count = 0;
	  for ( SensorStatus o : q ) {
	    if ( o == null ) {
	      continue;
	    }
	    count ++;
	  }
	  return q. size () == count;
	}
	
	private void clearList ( ArrayList < SensorStatus > q ) {
	  q. clear ();
	  for ( int i = 0, top = 3; i < top; i ++ ) {
	    q. add ( null );
	  }
	}
	
	private void setList ( ArrayList < SensorStatus > q, int i, SensorStatus v ) {
	  if ( v == null ) {
	    return;
	  }
    q. set ( i, v );
	}
  
	private void setResponse ( SensorStatus x ) { 
	  SensorStatus s = yg ( x );
	  if ( s == null ) {
	    return;
	  }
	  yl0. set ( 0, s );
	}
	
	private void setResponse ( SensorStatus x, SensorStatus y, int i ) {
    SensorStatus s = yf( x, y);
    if ( s == null ) {
      return;
    }
    yl0. set ( i, s );
  }
	
	private SensorStatus getValue ( message x, String k ) {
	  SensorStatus s = null;
	  for ( int i = 0, top = x. size (); i < top; i ++ ) {
	    if ( ! messageOnPort ( x, k, i ) ) {
	      continue;
	    }
      return ( SensorStatus ) x. getValOnPort ( k, i );
	  }
	  return ( SensorStatus ) null;
	}
	
	@Override
  public void deltext ( double e, message x ) {
	  Continue ( e );
	  setList ( queue, 0, getValue ( x, "stateIn0" ) );
    setList ( queue, 1, getValue ( x, "stateIn1" ) );
    setList ( queue, 2, getValue ( x, "stateIn2" ) );
    if ( ! isQueueSet ( queue ) ) {
      holdIn ( String. valueOf ( LinePhase. WAIT ), INFINITY );
      return;
    }
    SensorStatus yl00 = queue. remove ( 0 );
    setResponse ( yl00 );
        
    int i = 1; while ( ! queue. isEmpty () ) {
      SensorStatus yl0i = queue. remove ( 0 );
      setResponse ( yl00, yl0i, i );
      i ++;
    }
    holdIn ( String. valueOf ( LinePhase. STREAM ), 0 );
  }
  
  @Override
  public void deltint() {
    clearList ( queue );
    clearList ( yl0 );
    holdIn ( String. valueOf ( LinePhase. WAIT ), INFINITY );
  }
  
  @Override
  public message out () {
    if ( ! phaseIs ( String. valueOf ( SensorPhase.STREAM ) ) ) {
      return super. out ();
    }
    message m = new message ();
    m. add ( makeContent ( "stateOut", new LineStatus ( yl0 ) ) );
    return m;
  }
	
	

}
