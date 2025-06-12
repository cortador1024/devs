package CCA.BrSimulation_30x20;

import model.modeling.content;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;

import java.util.Iterator;

import GenCol.entity;

public class DCell extends TwoDimCell
{
    protected double ParticleNum = 0;
    protected entity value;
    // protected boolean clocked = true;

    private double valueKIB = 0;

    public DCell()
    {
        this(0, 0);
    }

    public DCell(int xcoord, int ycoord)
    {
        super(xcoord, ycoord);
        addInport("inKIB");
        addOutport("outKIB");
    }

    /**
     * Initialization method
     */
    public void initialize()
    {
        super.initialize();
        ParticleNum = 0;
        holdIn(Math.round(ParticleNum) + ":", INFINITY);
        //holdIn(ParticleNum + ":", INFINITY);
        // Define the Phase Color for CA Display
        DCellUI.setPhaseColor();
    }

    /**
     * External Transition Function
     */

    public void deltext(double e, message x)
    {
        // long startTime = System.currentTimeMillis();
        Continue(e);
        Iterator it = x.iterator();
        while (it.hasNext())
        {
            content c = (content) it.next();
            if (c.getPortName() == "inKIB")
            {
                value = (entity) c.getValue();
                if (value != null)
                {
                    valueKIB = Double.parseDouble(value.getName());
                    holdIn(Math.round(valueKIB) + ":", INFINITY);
                    //holdIn(valueKIB + ":", INFINITY);
                }
            }

            else
            {

                holdIn(phase, INFINITY);

            }
        }

    }

    /*
     * Internal Transition Function
     */

    public void deltint()
    {

        holdIn(phase, INFINITY);

    }

    public void deltcon(double e, message x)
    {
        deltint();
        deltext(0, x);
    }

    /*
     * Message out Function
     */
    // public message out()
    // {
    //
    // message m = new message();
    //
    // return m;
    // }

}
