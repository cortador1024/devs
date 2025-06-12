package CCA.AgentDiffusionNotSameMap;

import model.modeling.content;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import model.modeling.CAModels.TwoDimCellSpace;

import java.util.Iterator;

import GenCol.entity;

public class DiffusionCell extends TwoDimCell
{
    protected int HeatValue = 0;
    protected entity value;
    public int isSource = 0;
    // protected boolean clocked = true;
    private String prePhase = "0:";

    private int valueN = 0;
    private int valueS = 0;
    private int valueW = 0;
    private int valueE = 0;
    private int valueAgent = 0;
    private int preValue = 0;

    public DiffusionCell()
    {
        this(0, 0);
    }

    public DiffusionCell(int xcoord, int ycoord)
    {
        super(xcoord, ycoord);
    }

    /**
     * Initialization method
     */
    public void initialize()
    {
        super.initialize();
        // reset the value
        valueAgent = 0;
        valueN = 0;
        valueS = 0;
        valueW = 0;
        valueE = 0;
        if (isSource == 1)
        {
            HeatValue = 1000;
            holdIn(HeatValue + "...", 1);
        }
        else
        {
            HeatValue = 0;
            holdIn(HeatValue + "...", 1);
        }

        // Define the Phase Color for CA Display
        DiffusionUI.setPhaseColor();
    }

    /**
     * External Transition Function
     */

    public void deltext(double e, message x)
    {
        // long startTime = System.currentTimeMillis();
        Continue(e);
        if (isSource == 0)
        {
            Iterator it = x.iterator();
            while (it.hasNext())
            {
                content c = (content) it.next();
                if (c.getPortName() == "inN")
                {
                    value = (entity) c.getValue();
                    if (value != null)
                    {
                        valueN = Integer.parseInt(value.getName());
                    }
                }
                else if (c.getPortName() == "inS")
                {
                    value = (entity) c.getValue();
                    if (value != null)
                    {
                        valueS = Integer.parseInt(value.getName());
                    }
                }
                else if (c.getPortName() == "inW")
                {
                    value = (entity) c.getValue();
                    if (value != null)
                    {
                        valueW = Integer.parseInt(value.getName());
                    }
                }
                else if (c.getPortName() == "inE")
                {
                    value = (entity) c.getValue();
                    if (value != null)
                    {
                        valueE = Integer.parseInt(value.getName());
                    }
                }
                else if (c.getPortName() == "inKIB")
                {
                    value = (entity) c.getValue();
                    if (value != null)
                    {
                        valueAgent = Integer.parseInt(
                            value.getName()
                        );
                    }
                }

            }

            if (HeatValue != newHeatValue())
            {
                preValue = HeatValue;
                HeatValue = newHeatValue();
                valueAgent = 0;
                holdIn(HeatValue + "...", 1);
            }
            else
            {
                preValue = HeatValue;
                holdIn(phase, 1);
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
    public message out()
    {

        message m = new message();

        if (HeatValue != preValue && HeatValue != 0)
        {
            m.add(makeContent("outN", new entity(HeatValue + "")));

            m.add(makeContent("outE", new entity(HeatValue + "")));

            m.add(makeContent("outS", new entity(HeatValue + "")));

            m.add(makeContent("outW", new entity(HeatValue + "")));

        }

        return m;
    }

    // use FTCS scheme to solve PDE
    private int newHeatValue()
    {
        int coef = 4;
        if (xcoord == 0 || xcoord == getWidth() - 1)
        {
            coef--;
        }
        if (ycoord == 0 || ycoord == getHeight() - 1)
        {
            coef--;
        }
        return (int) (valueAgent
            + HeatValue
            + 0.2
                * (valueN
                    + valueS
                    + valueE
                    + valueW
                    - coef * HeatValue));
    }

}
