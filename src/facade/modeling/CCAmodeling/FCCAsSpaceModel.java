package facade.modeling.CCAmodeling;

import java.util.ArrayList;

import facade.modeling.FCoupledModel;
import facade.modeling.FModel;
import model.modeling.digraph;
import model.modeling.CCAModels.CCAKIBModel;
import model.modeling.CCAModels.CCAModel;
import model.modeling.CCAModels.CCAsSpaceModel;
import util.SortedArrayList;
import util.SortedEnumerableList;

public class FCCAsSpaceModel extends FCoupledModel
{
    private ArrayList<FCCAModel> fccaModels;
    private FCCAKIBModel fccakib;

    public FCCAsSpaceModel(CCAsSpaceModel model)
    {
        this(model, null);
    }

    public FCCAsSpaceModel(CCAsSpaceModel model, FModel parent)
    {
        super(model, parent);
    }

    @Override
    public CCAsSpaceModel getModel()
    {
        return (CCAsSpaceModel) super.getModel();
    }

    public ArrayList<CCAModel> getCCAModel()
    {
        return getModel().getCCAs();
    }

    public ArrayList<FCCAModel> getFCCAModel()
    {
        return fccaModels;
    }

    public FCCAKIBModel getFCCAKIB()
    {
        return fccakib;
    }

    public CCAKIBModel getCCAKIB()
    {
        return getModel().getCCAKIB();
    }

    @Override
    protected SortedEnumerableList<FModel> createChildModels(
        digraph model,
        FModel fModel
    )
    {
        fccaModels = new ArrayList<FCCAModel>();

        SortedEnumerableList<FModel> childModels = new SortedArrayList<FModel>(
            (FModel lhs, FModel rhs) -> {
                return SortedEnumerableList.DefaultStringComparator.compare(
                    lhs.toString(),
                    rhs.toString()
                );
            }
        );

        for (CCAModel cca : getCCAModel())
        {
            FCCAModel fcca = new FCCAModel(cca, fModel);
            fccaModels.add(fcca);
            childModels.add(fcca);
            childModels.addAll(fcca.getChildren());
        }

        fccakib = new FCCAKIBModel(getCCAKIB(), fModel);
        childModels.add(fccakib);
        //childModels.addAll(fccakib.getChildren());
        return childModels;
    }
}
