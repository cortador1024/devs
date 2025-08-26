package Component.Devs.control;

import static Component.Devs.control.SegmentController.SegmentAction.QUERY;
import static Component.Devs.control.SegmentController.SegmentAction.RESET;
import static Component.Devs.control.SegmentController.SegmentPhase.ACTION;
import static Component.Devs.control.SegmentController.SegmentPhase.STREAM;
import static Component.Devs.control.SegmentController.SegmentPhase.WAIT;
import Component.Devs.control.SensorController.SensorState;
import static Component.Devs.control.SensorController.SensorState.ERROR;
import static Component.Devs.control.SensorController.SensorState.HIGH_TENSION;
import static Component.Devs.control.SensorController.SensorState.INTERRUPTION;
import static Component.Devs.control.SensorController.SensorState.LOW_TENSION;
import static Component.Devs.control.SensorController.SensorState.NORMAL;
import Component.Devs.lib.DefaultViewableAtomic;
import Component.Devs.lib.port.LogStruct;
import java.util.HashMap;

import model.modeling.message;

public class SegmentController extends DefaultViewableAtomic 	{

  private final String IN = "state";
  private final String REQUEST = "request";
  
  private final String OUT = "ostate";
  private final String RESPONSE = "response";
  
  private final String LOG = "log";
  
  private int index = 0;

  public enum SegmentPhase {
    
    WAIT ( "wait" ),
    ACTION ( "action" ), 
    STREAM ( "stream" );
    
    private final String name;
    
    SegmentPhase ( String n ) {
      name = n;
    }
    
    @Override
    public String toString() {
      return name;
    }  
  }
  
  public enum SegmentAction {
    
    QUERY ( "consultar" ),
    RESET ( "restablecer" );
    
    private final String name;
    
    SegmentAction ( String n ) {
      name = n;
    }
    
    @Override
    public String toString() {
      return name;
    }  
     
  }
  // xl0 represent the current state of the segment component
  private SensorState xl0 = NORMAL;
  // xl1 represent the previous state of the segment component
  private SensorState xl1 = NORMAL;
  // xl2 represent the current measurement of the segment component
  private double xl2 = 0;
  // xl3 represent the current action from the environment
  private SegmentAction xl3 = null;
  // xl5 represent the number of contiguous reading on the same state input
  private int xl5 = 0;
  
  
  public SegmentController ( String name, int i ) {
		super ( String. format ( "SegmentController %s", name ) );
		addInport ( IN );
		addInport ( REQUEST );
		addOutport ( OUT );
    addOutport ( RESPONSE );
		addOutport ( LOG );
    index = i;
	}
	
	@Override
  public void initialize() {
	  holdIn ( WAIT, INFINITY );
  }
  
  @Override
  public double ta () {
    return getSigma ();
  }
  
  private SensorState xf ( SensorState x, SensorState y ) {
    if ( x == NORMAL && y == NORMAL ) 
      return NORMAL;
    if ( x == HIGH_TENSION && y == NORMAL ) 
      return HIGH_TENSION;
    if ( x == LOW_TENSION && y == NORMAL ) 
      return LOW_TENSION;
    if ( x == ERROR && y == NORMAL ) 
      return ERROR;
    if ( x == INTERRUPTION && y == NORMAL ) 
      return INTERRUPTION;
    if ( x == NORMAL && y == HIGH_TENSION ) 
      return NORMAL;
    if ( x == LOW_TENSION && y == HIGH_TENSION ) 
      return ERROR;
    if ( x == HIGH_TENSION && y == HIGH_TENSION ) 
      return HIGH_TENSION;
    if ( x == ERROR && y == HIGH_TENSION ) 
      return ERROR;
    if ( x == INTERRUPTION && y == HIGH_TENSION ) 
      return INTERRUPTION;
    if ( x == NORMAL && y == LOW_TENSION ) 
      return NORMAL;
    if ( x == LOW_TENSION && y == LOW_TENSION ) 
      return LOW_TENSION;
    if ( x == HIGH_TENSION && y == LOW_TENSION ) 
      return ERROR;
    if ( x == INTERRUPTION && y == LOW_TENSION ) 
      return INTERRUPTION;
    if ( x == ERROR && y == LOW_TENSION ) 
      return ERROR;
    if ( x == NORMAL && y == INTERRUPTION ) 
      return NORMAL;
    if ( x == LOW_TENSION && y == INTERRUPTION ) 
      return LOW_TENSION;
    if ( x == HIGH_TENSION && y == INTERRUPTION ) 
      return HIGH_TENSION;
    if ( x == INTERRUPTION && y == INTERRUPTION ) 
      return INTERRUPTION;
    if ( x == ERROR && y == INTERRUPTION ) 
      return ERROR;
    if ( x == NORMAL && y == ERROR ) 
      return ERROR;
    if ( x == LOW_TENSION && y == ERROR ) 
      return ERROR;
    if ( x == HIGH_TENSION && y == ERROR ) 
      return ERROR;
    if ( x == INTERRUPTION && y == ERROR ) 
      return ERROR;
    if ( x == ERROR && y == ERROR ) 
      return ERROR;
    return null;
  }
  
  private void onInput ( Object [] xe ) {
    SensorState xe0 = ( SensorState ) xe [ 0 ];
    double xe1 = ( double) xe [ 1 ];
    xl0 = xf ( xe0, xl0 );
    xl1 = xe0;
    xl2 = xe1;
    if ( xl1 == xe0 && xl5 == 3 ) {
      xl0 = NORMAL;
      xl1 = NORMAL;
      // xl2 = 0;
      xl3 = RESET;
      xl5 = 0;
    } else
    if ( xl1 == xe0 && xl5 < 4 ) {
      xl3 = null;
      xl5 ++;
    } else
    if ( xl1 != xe0 ) {
      xl3 = null;
      xl5 = 0;
    }
  }
  
  private void onRequest ( String request ) {
    xl3 = SegmentAction. valueOf ( request );
  }
  
  
	@Override
  public void deltext ( double e, message x ) {
	  Continue ( e );
    HashMap < String, Object > msg = receive ( x );
    over ( msg. get ( IN ) ).each ( ( Object o ) -> { 
      onInput ( ( Object [] ) o );
      holdIn ( STREAM, 0 );
    } );
    over ( msg. get ( REQUEST ) ).each ( ( Object o ) -> {
      onRequest ( ( String ) o );
      holdIn ( ACTION, 0 );
    } );
  }
  
  @Override
  public void deltint() {
    holdIn ( WAIT, INFINITY );
  }
  
  @Override
  public message out() {
    if ( phaseIs ( STREAM ) ) {
      return send ( 
        OUT, new Object [] { xl0, xl2, index }, 
        LOG, new LogStruct ( tag (), getPhase (), "active", new Object [] { xl0, xl2, index } ) 
      );
    }
    if ( phaseIs ( ACTION ) && xl3 == QUERY ) {
      return send ( RESPONSE, new Object [] { xl0, xl2 } );
    }
    return send ();
  }
  
  
}
