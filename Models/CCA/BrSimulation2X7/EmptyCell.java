package CCA.BrSimulation2X7;

import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;

public class EmptyCell extends TwoDimCell {

	public EmptyCell() {
		this(0, 0);
	}

	public EmptyCell(int xcoord, int ycoord) {
		super(xcoord, ycoord);
		super.name = "empyt cell: " + xcoord + ", " + ycoord;
	}

	public void initialize() {
		super.initialize();
		passivate();
	}

	public void deltext(double e, message x) {

		Continue(e);

		passivate();
	}

	public void deltint() {
		passivate();
	}

}
