package Component.Devs.lib;

import Component.Devs.SegmentController.SegmentAction;
import GenCol.entity;

public class Maneuver extends entity {
    
  public SegmentAction action;
  
  public Maneuver(SegmentAction a ) {
    super( String. format ( "Maneuver %s", a ) );
    action = a;
  }
  
  @Override
  public String getName() {
    return String. format ( "Maneuver %s", action );
  }
}
