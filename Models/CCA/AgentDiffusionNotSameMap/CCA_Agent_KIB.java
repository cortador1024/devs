package CCA.AgentDiffusionNotSameMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import model.modeling.CAModels.indexEntity;
import model.modeling.CCAModels.CCAKIBModel;
import model.modeling.GeoKIB.GeoMap;
import model.modeling.GeoKIB.Length;
import model.modeling.GeoKIB.LengthUnit;
import model.modeling.GeoKIB.MapRegion;

public class CCA_Agent_KIB extends CCAKIBModel {

	// private int[][] CAResultData;

	private ArrayList<Integer> cca1_statusChangeX, cca1_statusChangeY;
	private ArrayList<Integer> cca2_statusChangeX, cca2_statusChangeY;

	// private boolean[][] statusChanged;

	protected indexEntity value;

	// public static long extTime = 0;
	// public static long outTime = 0;
	// public static long cellExtTime = 0;

	public CCA_Agent_KIB(String name, MapRegion mapR_A, MapRegion mapR_B,
			double newLocationX_A, double newLocationY_A, double newLocationX_B,
			double newLocationY_B, LengthUnit commonUnit) {

		super(name, mapR_A, mapR_B, newLocationX_A, newLocationY_A,
				newLocationX_B, newLocationY_B, commonUnit);

		// super(name, xsize1, ysize1, 1000000, 1000000,
		// new Length(237581, LengthUnit.METER).unifyToM(), xsize2, ysize2,
		// 1500000, 1000000,
		// new Length(13413333, LengthUnit.CENTIMETER).unifyToM());

		this.getMapA().setDefaultValue("0");
		this.getMapB().setDefaultValue("0");

		// store the index of the changed cells
		cca1_statusChangeX = new ArrayList<Integer>();
		cca1_statusChangeY = new ArrayList<Integer>();

		cca2_statusChangeX = new ArrayList<Integer>();
		cca2_statusChangeY = new ArrayList<Integer>();

	}

	public void initialize() {
		super.initialize();
		passivate();
	}

	public void deltext(double e, message x) {
		Continue(e);
		Iterator it = x.iterator();
		while (it.hasNext()) {

			content c = (content) it.next();
			if (c.getPortName() == "inMapA") {
				value = (indexEntity) c.getValue();

				if (value != null) {
					// Keep the index of input of changed cells
					cca1_statusChangeX.add(value.getI());
					cca1_statusChangeY.add(value.getJ());
					// update the memory data storage with only status changed
					// cells
					this.getMapA().set_value(value.getI(), value.getJ(),
							value.getInput());
				}
			}

			else if (c.getPortName() == "inMapB") {
				value = (indexEntity) c.getValue();

				if (value != null) {
					// Keep the index of input of changed cells
					cca2_statusChangeX.add(value.getI());
					cca2_statusChangeY.add(value.getJ());
					// update the memory data storage with only status changed
					// cells
					this.getMapB().set_value(value.getI(), value.getJ(),
							value.getInput());
				}
			}
		}

		MemoryDataA.add(mapA);
		MemoryDataB.add(mapB);

		holdIn("busy", 0);

	}

	public void deltint() {

		passivate();

	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	public message out() {
		message m = new message();
		for (int i = 0; i < cca1_statusChangeX.size(); i++) {
			int x = cca1_statusChangeX.get(i);
			int y = cca1_statusChangeY.get(i);

			content con2 = makeContent(toMapB_OutPortsNames[x][y],
					new entity("" + MemoryDataA.get(0).get_value(x, y)));
			m.add(con2);

		}

		cca1_statusChangeX.clear();
		cca1_statusChangeY.clear();

		cca2_statusChangeX.clear();
		cca2_statusChangeY.clear();

		MemoryDataA.clear();
		MemoryDataB.clear();

		// outputMemoryData();

		return m;
	}

	public String[][] getCCA1OutPortsNames() {
		return toMapA_OutPortsNames;
	}

	public String[][] getCCA2OutPortsNames() {
		return toMapB_OutPortsNames;
	}

	class Index {
		int i, j;

		public Index(int i, int j) {
			this.i = i;
			this.j = j;
		}

	}

}
