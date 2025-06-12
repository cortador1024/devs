package facade.modeling.CCAmodeling;

import facade.modeling.FAtomicModel;
import facade.modeling.FModel;
import facade.modeling.CAmodeling.FCASpaceModel;
import model.modeling.CCAModels.CCAKIBModel;
import model.modeling.GeoKIB.GeoMap;

public class FCCAKIBModel extends FAtomicModel
{
    GeoMap mapA;
    GeoMap mapB;
    
    public FCCAKIBModel(CCAKIBModel model)
    {
        this(model, null);
        mapA = model.getMapA();
        mapB = model.getMapB();
        
    }

    public FCCAKIBModel(CCAKIBModel model, FModel parent)
    {
        super(model, parent);
        mapA = model.getMapA();
        mapB = model.getMapB();
    }
    
    public GeoMap getMapA() {
        return mapA;
    }
    
    public GeoMap getMapB() {
        return mapB;
    }
}
