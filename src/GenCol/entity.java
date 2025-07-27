/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package GenCol;

public class entity implements EntityInterface{

  protected String name;


  public entity(){
    name = "anEntity";
  }
  
  
  public entity(String nm){
    name = nm;
  }
  
  public boolean eq(String nm){
    return getName().equals(nm);
  }

  @Override
  public Object equalName(String nm){
    if ( eq ( nm ) ) {
      return this;
    }
    return null;
  }

  @Override
  public int hashCode () {
    return name.hashCode();
  }

  @Override
  public boolean equals ( Object o ){    //overrides pointer equality of Object
    if ( o == null ) {
      return false;
    }
    if ( o instanceof entity == false ) {
      return false;
    }
    return eq ( ( ( entity ) o ). getName () );
  }

  @Override
  public ExternalRepresentation getExtRep (){
    return new ExternalRepresentation. ByteArray();
  }

  @Override
  public String getName(){
    return name;
  }


  public synchronized void addSelf ( ensembleCollection < Object > c ){
    c. add ( this );
  }

  public synchronized void removeSelf(ensembleCollection<Object> c){
    c. remove ( this );
  }

/**/

  @Override
  public String toString(){
    return getName();
  }

  public void print(){
    System. out. println ( name );
  }

}

