package facade.modeling.CCAmodeling;

import java.util.Optional;

import facade.modeling.FModel;
import facade.modeling.CAmodeling.FCACellModel;
import facade.modeling.CAmodeling.FCASpaceModel;
import model.modeling.IODevs;
import model.modeling.digraph;
import model.modeling.CAModels.TwoDimCell;
import model.modeling.CCAModels.CCAModel;
import util.SortedEnumerableList;

public class FCCAModel extends FCASpaceModel
{
    public FCCAModel(CCAModel model)
    {
        this(model, null);

    }

    public FCCAModel(CCAModel model, FModel parent)
    {
        super(model, parent);
    }

    @Override
    protected SortedEnumerableList<FModel> createChildModels(
        digraph model,
        FModel fModel
    )
    {
        return createChildModels(model, fModel, (IODevs c) -> {
            if (c instanceof TwoDimCell)
            {
                return Optional.of(
                    new FCACellModel((TwoDimCell) c, fModel)
                );
            }
            else if (c instanceof CCAModel)
            {
                return Optional.of(
                    new FCCAModel((CCAModel) c, fModel)
                );
            }
            return Optional.empty();
        });
    }
}
