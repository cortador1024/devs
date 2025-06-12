package facade.simulation.CCAsimulation;

import facade.modeling.FModel;
import facade.simulation.FCoupledSimulator;
import model.modeling.CCAModels.CCAsSpaceModel;
import model.simulation.realTime.TunableCoordinator.Listener;

public class FCCASimulator extends FCoupledSimulator
{
    public FCCASimulator(
        CCAsSpaceModel model,
        FModel rootModel,
        Listener listener,
        short modelType
    )
    {
        super(model, rootModel, listener, modelType);
    }
}
