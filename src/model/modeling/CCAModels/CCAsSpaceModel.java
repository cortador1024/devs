package model.modeling.CCAModels;

import java.util.ArrayList;
import view.modeling.ViewableDigraph;

public class CCAsSpaceModel extends ViewableDigraph
{

    private CCAModel ccaModel1, ccaModel2;
    private CCAKIBModel ccaKib;

    public CCAsSpaceModel(
        String nm,
        CCAModel _ccaModel1,
        CCAModel _ccaModel2,
        CCAKIBModel _ccaKib
    )
    {
        super(nm);
        ccaModel1 = _ccaModel1;
        ccaModel2 = _ccaModel2;
        ccaKib = _ccaKib;
        
        doCoupling();
    }

    public void doCoupling()
    {
//        addCoupling(ccaModel1, "outKIB", ccaKib, "inCCA1");
//        addCoupling(ccaModel2, "outKIB", ccaKib, "inCCA2");
//        addCoupling(ccaKib, "outCCA1", ccaModel1, "inKIB");
//        addCoupling(ccaKib, "outCCA2", ccaModel2, "inKIB");

        addCoupling(ccaModel1, "outKIB", ccaKib, "inMapA");
        addCoupling(ccaModel2, "outKIB", ccaKib, "inMapB");
        
        int ax = ccaModel1.xDimCellspace;
        int ay = ccaModel1.yDimCellspace;
        
        int bx = ccaModel2.xDimCellspace;
        int by = ccaModel2.yDimCellspace;

        for (int i = 0; i < ax; i++)
        {
            for (int j = 0; j < ay; j++)
            {

                addCoupling(
                    ccaKib,
                    "outMapA:" + i + "," + j,
                    ccaModel1,
                    "inKIB:" + i + "," + j
                );

            }
        }

        for (int i = 0; i < bx; i++)
        {
            for (int j = 0; j < by; j++)
            {

                addCoupling(
                    ccaKib,
                    "outMapB:" + i + "," + j,
                    ccaModel2,
                    "inKIB:" + i + "," + j
                );

            }
        }

    }

    public ArrayList<CCAModel> getCCAs()
    {
        ArrayList<CCAModel> ccas = new ArrayList<CCAModel>();
        ccas.add(ccaModel1);
        ccas.add(ccaModel2);
        return ccas;

    }

    public CCAKIBModel getCCAKIB()
    {

        return ccaKib;

    }

}
