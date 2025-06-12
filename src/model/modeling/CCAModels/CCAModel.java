package model.modeling.CCAModels;

import model.modeling.CAModels.TwoDimCell;
import model.modeling.CAModels.TwoDimCellSpace;
import model.modeling.GeoKIB.LengthUnit;
import model.modeling.GeoKIB.MapRegion;

public class CCAModel extends TwoDimCellSpace {
	private MapRegion mapRegion;

	public CCAModel(String nm, MapRegion mapR) {
		super(nm, mapR.get_num_cols(), mapR.get_num_rows());
		this.mapRegion = mapR;

		// addInport("inKIB");
		addOutport("outKIB");

	}

	public CCAModel(String nm, int xDim, int yDim) {

		this(nm, new MapRegion(xDim, yDim));

	}

	public CCAModel(TwoDimCellSpace model) {
		this(model.getName(),
				new MapRegion(model.xDimCellspace, model.yDimCellspace));

	}

	public void addCCACell(TwoDimCell _cell) {
		addCell(_cell);

		_cell.addOutport("outKIB");
		_cell.addInport("inKIB");

		addInport("inKIB:" + _cell.getXcoord() + "," + _cell.getYcoord());
		addOutport("outKIB");
		addCoupling(this,
				"inKIB:" + _cell.getXcoord() + "," + _cell.getYcoord(), _cell,
				"inKIB");
		addCoupling(_cell, "outKIB", this, "outKIB");
		_cell.initialize();

	}

	public MapRegion getMapRegion() {
		return mapRegion;
	}

	public void setMapRegion(MapRegion mapRegion) {
		this.mapRegion = mapRegion;
	}

}
