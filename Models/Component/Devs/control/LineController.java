package Component.Devs.control;

import static Component.Devs.control.LineController.LinePhase.STREAM;
import static Component.Devs.control.LineController.LinePhase.WAIT;
import Component.Devs.control.SensorController.SensorState;
import Component.Devs.lib.DefaultViewableAtomic;
import java.util.ArrayList;
import java.util.HashMap;

import model.modeling.message;

public class LineController extends DefaultViewableAtomic {

  
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

  private Object [] yl0 = new Object [ 3 ] ;
  
  private Object [] array = new Object [ 3 ];
  
  private ArrayList < Integer > yl1 = new ArrayList <> ();
  
  private static final String RESPONSE = "response";
  
  private static final String REQUEST = "request";
  
	public LineController ( String name ) {
		super ( String. format ( "LineController %s", name ) );
		addInport ( REQUEST );
		addOutport ( RESPONSE );
	}
	
	@Override
  public void initialize() {
	  
	  holdIn ( WAIT, INFINITY );
	  clear ( yl0 );
    clear ( array );
  }
	
	@Override
  public double ta() {
    return getSigma ();
  }
	
  public int yi ( SensorState s ) {
    return s. ordinal ();
  }
  
  public Object yh ( SensorState s ) {
    int o = yi ( s );
    return o != 0 ? 
      o : 
      null
    ;
  }
	  
	public Object [] yg ( Object [] x ) {
    Object r = yh ( ( SensorState ) x [ 0 ] );
	  return r != null ? 
      new Object [] { r, x [ 1 ] } :
      null
    ;
	}
	
	public Object [] yf ( Object [] x, Object [] y ) {
	  SensorState sx = ( SensorState ) x [ 0 ];
    SensorState sy = ( SensorState ) y [ 0 ];
    try {
      Object val = null;
      int yhx = ( val = yh ( sx ) ) != null ? ( int ) val : -1;
      int yhy = ( val = yh ( sy ) ) != null ? ( int ) val : -1;
      if ( yhx == -1 && yhy == yhx ) {
        return null;
      }
      if ( yhx != 4 && yhx <= yhy ) {
        return y;
      }
    } catch ( Exception ex ) {
      System. out. println ( "" );
    }
	  return new Object [] { SensorState.ERROR, y [ 1 ] };
	}
	
	private boolean isReady ( Object [] q ) {
	  int count = 0;
	  for ( Object o : q ) {
	    if ( o == null ) {
	      continue;
	    }
	    count ++;
	  }
	  return q. length == count;
	}
	
	private void clear ( Object [] q ) {
	  for ( int i = 0, top = q. length; i < top; i ++ ) {
	    q [ i ] = null;
	  }
	}
	
	@Override
  public void deltext ( double e, message x ) {
    Continue ( e );
	  HashMap < String, Object > msg = receive ( x );
    Object [] val = null;
    val = ( Object [] ) msg. get ( "stateIn0" );
    if ( val != null ) {
      array [ 0 ] = val;
    }
    val = ( Object [] ) msg. get ( "stateIn1" );
    if ( val != null ) {
      array [ 1 ] = val;
    }
    val = ( Object [] ) msg. get ( "stateIn2" );
    if ( val != null ) {
      array [ 2 ] = val;
    }
    if ( ! isReady ( array ) ) {
      holdIn ( WAIT, INFINITY );
      return;
    }
    yl0 [ 0 ] = yg ( ( Object [] ) array [ 0 ] );
    for ( int i = 1, top = array. length; i < top; i ++ ) {
      yl0 [ i ] = yf ( ( Object [] ) array [ 0 ], ( Object [] ) array [ i ] );
    }
    holdIn ( STREAM, 0 );
  }
  
  @Override
  public void deltint() {
    clear ( array );
    clear ( yl0 );
    holdIn ( WAIT, INFINITY );
  }
  
  @Override
  public message out () {
    if ( ! phaseIs ( STREAM ) ) {
      return super. out ();
    }
    return send ( RESPONSE, new Object [] { getName (), yl0 }  );
  }
	
}
