package Component.Devs;

import Component.Devs.lib.SensorStatus;
import Component.Devs.lib.Maneuver;
import Component.Devs.SensorController.SensorState;
import java.util.ArrayList;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class SegmentController extends ViewableAtomic 	{
	
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
  private SensorState xl0 = SensorState.NORMAL;
  // xl1 represent the previous state of the segment component
  private SensorState xl1 = SensorState.NORMAL;
  // xl2 represent the current measurement of the segment component
  private int xl2 = 0;
  // xl3 represent the current action from the environment
  private SegmentAction xl3 = null;
  // xl5 represent the number of contiguous reading on the same state input
  private int xl5 = 0;
  
  
  public SegmentController(String name ) {
		super ( name );
		addInport ( "stateIn" );
		addInport ( "request" );
		addOutport ( "stateOut" );
		addOutport ( "response" );
	}
	
	@Override
  public void initialize() {
	  holdIn ( String. valueOf ( SegmentPhase.WAIT ), INFINITY );
  }
  
  @Override
  public String getPhase() {
    return String. valueOf ( phase );
  }
  
  @Override
  public double ta () {
    return sigma;
  }
  
  private SensorState xf ( SensorState x, SensorState y ) {
    if ( x == SensorState.NORMAL && y == SensorState.NORMAL) 
      return SensorState.NORMAL;
    if ( x == SensorState.HIGH_TENSION && y == SensorState.NORMAL) 
      return SensorState.HIGH_TENSION;
    if ( x == SensorState.LOW_TENSION && y == SensorState.NORMAL) 
      return SensorState.LOW_TENSION;
    if ( x == SensorState.ERROR && y == SensorState.NORMAL) 
      return SensorState.ERROR;
    if ( x == SensorState.INTERRUPTION && y == SensorState.NORMAL) 
      return SensorState.INTERRUPTION;
    if ( x == SensorState.NORMAL && y == SensorState.HIGH_TENSION ) 
      return SensorState.NORMAL;
    if ( x == SensorState.LOW_TENSION && y == SensorState.HIGH_TENSION ) 
      return SensorState.ERROR;
    if ( x == SensorState.HIGH_TENSION && y == SensorState.HIGH_TENSION ) 
      return SensorState.HIGH_TENSION;
    if ( x == SensorState.ERROR && y == SensorState.HIGH_TENSION ) 
      return SensorState.ERROR;
    if ( x == SensorState.INTERRUPTION && y == SensorState.HIGH_TENSION ) 
      return SensorState.INTERRUPTION;
    if ( x == SensorState.NORMAL && y == SensorState.LOW_TENSION ) 
      return SensorState.NORMAL;
    if ( x == SensorState.LOW_TENSION && y == SensorState.LOW_TENSION ) 
      return SensorState.LOW_TENSION;
    if ( x == SensorState.HIGH_TENSION && y == SensorState.LOW_TENSION ) 
      return SensorState.ERROR;
    if ( x == SensorState.INTERRUPTION && y == SensorState.LOW_TENSION ) 
      return SensorState.INTERRUPTION;
    if ( x == SensorState.ERROR && y == SensorState.LOW_TENSION ) 
      return SensorState.ERROR;
    if ( x == SensorState.NORMAL && y == SensorState.INTERRUPTION ) 
      return SensorState.NORMAL;
    if ( x == SensorState.LOW_TENSION && y == SensorState.INTERRUPTION ) 
      return SensorState.LOW_TENSION;
    if ( x == SensorState.HIGH_TENSION && y == SensorState.INTERRUPTION ) 
      return SensorState.HIGH_TENSION;
    if ( x == SensorState.INTERRUPTION && y == SensorState.INTERRUPTION ) 
      return SensorState.INTERRUPTION;
    if ( x == SensorState.ERROR && y == SensorState.INTERRUPTION ) 
      return SensorState.ERROR;
    if ( x == SensorState.NORMAL && y == SensorState.ERROR ) 
      return SensorState.ERROR;
    if ( x == SensorState.LOW_TENSION && y == SensorState.ERROR ) 
      return SensorState.ERROR;
    if ( x == SensorState.HIGH_TENSION && y == SensorState.ERROR ) 
      return SensorState.ERROR;
    if ( x == SensorState.INTERRUPTION && y == SensorState.ERROR ) 
      return SensorState.ERROR;
    if ( x == SensorState.ERROR && y == SensorState.ERROR ) 
      return SensorState.ERROR;
    return null;
  }
  
  private void processMessage ( SensorStatus xe ) {
        
    SensorState xe0 = ( SensorState ) xe. state;
    int xe1 = ( int ) xe. read;
    int atCase = 
      xl1 == xe0 && xl5 == 3 ? 
        1 :
      xl1 == xe0 && xl5 < 4 ?
        2 :
      xl1 != xe0 ? 
        3 :  
        0
      ;
    xl0 = xf ( xe0, xl0 );
    xl1 = xe0;
    xl2 = xe1;
    
    switch ( atCase ) {
      case 1: {
        
        xl0 = SensorState.NORMAL;
        xl1 = SensorState.NORMAL;
        xl2 = 0;
        xl3 = SegmentAction.RESET;
        xl5 = 0;
        holdIn ( String. valueOf ( SegmentPhase.STREAM ),
          0
        );
      } break;
      case 2: {
        xl3 = null;
        xl5 ++;
        holdIn ( String. valueOf ( SegmentPhase.STREAM ),
          0
        );
      } break;
      case 3: {
        xl3 = null;
        holdIn ( String. valueOf ( SegmentPhase.STREAM ),
          0
        );
        xl5 = 0;
      } break;
    }
    System. out. printf ( "SegmentController.delta_ext %s %s %s %s %s\n", xl0, xl1, xl2, xl3, xl5 );
  }
  
  private void processMessage ( Maneuver maneuver ) {
    SegmentAction me = maneuver. action;
    xl3 = me;
    holdIn ( String. valueOf ( SegmentPhase.ACTION ),
      0 
    );
  }
  
	@Override
  public void deltext ( double e, message x ) {
	  Continue ( e );
    ArrayList < Object > queue = new ArrayList <> ();
    queue. add ( x. getValOnPort ( "stateIn", 0 ) );
    queue. add ( x. getValOnPort ( "request", 0 ) );
    
    while ( ! queue. isEmpty () ) {
      Object top = queue. remove ( 0 );
      if ( top instanceof SensorStatus ) {
        processMessage ( ( SensorStatus ) top );
        continue;
      }
      if ( top instanceof Maneuver ) {
        processMessage ( ( Maneuver ) top );
        continue;
      }
    }
  }
  
  @Override
  public void deltint() {
    holdIn ( String. valueOf ( SegmentPhase.WAIT ),
      INFINITY
    );
  }
  
  @Override
  public message out() {
    if ( phaseIs ( String. valueOf ( SegmentPhase.WAIT ) ) ) {
      return super. out ();
    }
    message m = new message ();
    if ( phaseIs ( String. valueOf ( SegmentPhase.STREAM ) ) ) {
      m. add ( makeContent ( "stateOut", new SensorStatus ( xl0, xl2 ) ) );  
    } else
    if ( phaseIs ( String. valueOf ( SegmentPhase.ACTION ) ) && xl3 == SegmentAction.QUERY ) {
      m. add ( makeContent ( "response", new SensorStatus ( xl0, xl2 ) ) );  
    }
    return m;
  }
  
  
}
