package CCA.BrSimulation2X7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import model.modeling.CAModels.indexEntity;
import model.modeling.CCAModels.CCAKIBModel;
import model.modeling.GeoKIB.LengthUnit;
import model.modeling.GeoKIB.MapRegion;

public class FunctionKIB extends CCAKIBModel {
	private String[][] Macro_OutPortsNames;

	private String[][] Micro_OutPortsNames;

	private ModelicaModel_ODEwithPDE ODEPDEModelica;

	protected indexEntity value;

	// protected inputEntity value;
	private int width1, height1;
	private int width2, height2;

	// public static long extTime = 0;
	// public static long outTime = 0;
	// public static long cellExtTime = 0;

	public FunctionKIB(String name, MapRegion mapR_A, MapRegion mapR_B, double newLocationX_A, double newLocationY_A,
			double newLocationX_B, double newLocationY_B, LengthUnit commonUnit) {

		this(name, mapR_A, mapR_B, newLocationX_A, newLocationY_A, newLocationX_B, newLocationY_B, commonUnit, null);

	}

	public FunctionKIB(String name, MapRegion mapR_A, MapRegion mapR_B, double newLocationX_A, double newLocationY_A,
			double newLocationX_B, double newLocationY_B, LengthUnit commonUnit, String[][] initialStatus) {

		super(name, mapR_A, mapR_B, newLocationX_A, newLocationY_A, newLocationX_B, newLocationY_B, commonUnit);

		width1 = mapR_A.get_num_cols();
		height1 = mapR_A.get_num_rows();

		width2 = mapR_B.get_num_cols();
		height2 = mapR_B.get_num_rows();

		ODEPDEModelica = new ModelicaModel_ODEwithPDE(width2, height2);

		Macro_OutPortsNames = toMapA_OutPortsNames;
		Micro_OutPortsNames = toMapB_OutPortsNames;

		if (initialStatus != null) {
			for (int i = 0; i < initialStatus.length; i++) {
				for (int j = 0; j < initialStatus[0].length; j++) {
					if (initialStatus[i][j] == "CXCR4") {
						if (Param.barrestin) {

							ODEPDEModelica.setC4(i, j, 0);
							ODEPDEModelica.setC4i(i, j, 0);
							ODEPDEModelica.setR4(i, j, 702061);
							ODEPDEModelica.setR4i(i, j, 321043);
							ODEPDEModelica.setR4B(i, j, 32495.6);
							ODEPDEModelica.setC4B(i, j, 0);
							ODEPDEModelica.setC4Bii(i, j, 0);
							ODEPDEModelica.setB4(i, j, 467504);
							ODEPDEModelica.setCXCL12i(i, j, 0);
						} else {

							ODEPDEModelica.setC4(i, j, 0);
							ODEPDEModelica.setC4i(i, j, 0);
							ODEPDEModelica.setR4(i, j, 64545.5);
							ODEPDEModelica.setR4i(i, j, 645454);
							ODEPDEModelica.setCXCL12i(i, j, 0);
						}

					} else if (initialStatus[i][j] == "CXCR7") {

						if (Param.barrestin) {

							ODEPDEModelica.setC7(i, j, 0);
							ODEPDEModelica.setC7i(i, j, 0);
							ODEPDEModelica.setR7(i, j, 4758830);
							ODEPDEModelica.setR7i(i, j, 96768.2);
							ODEPDEModelica.setR7B(i, j, 322569);
							ODEPDEModelica.setC7B(i, j, 0);
							ODEPDEModelica.setR7Bii(i, j, 967430);
							ODEPDEModelica.setC7Bii(i, j, 0);
							ODEPDEModelica.setB7(i, j, 80662.9);
							ODEPDEModelica.setCXCL12i(i, j, 0);
							// ODEPDEModelica.set(i, j, 0);
						} else {
							ODEPDEModelica.setC7(i, j, 0);
							ODEPDEModelica.setC7i(i, j, 0);
							ODEPDEModelica.setR7(i, j, 1450250);
							ODEPDEModelica.setR7i(i, j, 4349750);
							ODEPDEModelica.setCXCL12i(i, j, 0);
						}
					} else {
						if (Param.barrestin) {

							ODEPDEModelica.setC4(i, j, 0);
							ODEPDEModelica.setC4i(i, j, 0);
							ODEPDEModelica.setR4(i, j, 0);
							ODEPDEModelica.setR4i(i, j, 0);
							ODEPDEModelica.setR4B(i, j, 0);
							ODEPDEModelica.setC4B(i, j, 0);
							ODEPDEModelica.setC4Bii(i, j, 0);
							ODEPDEModelica.setB4(i, j, 0);
							ODEPDEModelica.setC7(i, j, 0);
							ODEPDEModelica.setC7i(i, j, 0);
							ODEPDEModelica.setR7(i, j, 0);
							ODEPDEModelica.setR7i(i, j, 0);
							ODEPDEModelica.setR7B(i, j, 0);
							ODEPDEModelica.setC7B(i, j, 0);
							ODEPDEModelica.setC7Bii(i, j, 0);
							ODEPDEModelica.setC7Bii(i, j, 0);
							ODEPDEModelica.setB7(i, j, 0);
							ODEPDEModelica.setCXCL12i(i, j, 0);
						} else {
							ODEPDEModelica.setC4(i, j, 0);
							ODEPDEModelica.setC4i(i, j, 0);
							ODEPDEModelica.setR4(i, j, 0);
							ODEPDEModelica.setR4i(i, j, 0);

							ODEPDEModelica.setC7(i, j, 0);
							ODEPDEModelica.setC7i(i, j, 0);
							ODEPDEModelica.setR7(i, j, 0);
							ODEPDEModelica.setR7i(i, j, 0);

							ODEPDEModelica.setCXCL12i(i, j, 0);

						}

					}
				}
			}

		}

		addOutport("AvgX4Migration");
		addOutport("ConcAbsGrad");

	}

	public void initialize() {
		super.initialize();
//		for (int j = 0; j < height2; j++) {
//			PDEModelica.setCXCL12(0, j, 40.0);
//
//		}
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
					try {
						// get secret data from ABM
						DataTransformMacro2Micro(value.getI(), value.getJ(), Double.parseDouble(value.getInput()));
					} catch (NumberFormatException error) {
						if (value.getInput() == "X4") {
							ODEPDEModelica.moveX4(value);

						} else if (value.getInput() == "X7") {
							ODEPDEModelica.moveX7(value);

						} else if (value.getInput() == "L12") {
							ODEPDEModelica.moveL12(value);

						}
					}

				}
			}
		}

		ODEPDEModelica.execute(360);
		holdIn("active", 0);
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
		for (int i = 0; i < width1; i++) {
			for (int j = 0; j < height1; j++) {
				// content conMacro = makeContent(Macro_OutPortsNames[i][j], new entity("" +
				// Value2Macro(i, j)));
				if (ODEPDEModelica.getHasX4(i, j)) {
					content conAttract = makeContent(Macro_OutPortsNames[i][j], new entity(getHighestAttract(i, j)));

					// m.add(conMacro);
					m.add(conAttract);
				}

			}
		}

		for (int i = 0; i < width2; i++) {
			for (int j = 0; j < height2; j++) {
				content conMicro = makeContent(Micro_OutPortsNames[i][j], new entity("" + Value2Micro(i, j)));
				m.add(conMicro);

			}
		}

		content X4Migration = makeContent("AvgX4Migration", new entity(ODEPDEModelica.getAverageMigration() + ""));

		m.add(X4Migration);

		content ConcAbsGrad = makeContent("ConcAbsGrad", new entity(ODEPDEModelica.computeConcAbsGrad() + ""));

		m.add(ConcAbsGrad);

		return m;

	}

	public String[][] getMacroOutPortsNames() {
		return Macro_OutPortsNames;
	}

	public String[][] getMicroOutPortsNames() {
		return Micro_OutPortsNames;
	}

	private double Value2Micro(int i, int j) {
		double result = 0;
		result = ODEPDEModelica.getCXCL12(i, j);
		return result;
	}

	private double Value2Macro(int i, int j) {
		double result = 0;
		result = ODEPDEModelica.getCXCL12(i, j);
		return result;
	}

	private void DataTransformMacro2Micro(int i, int j, double v) {
		ODEPDEModelica.accumulateValue(i, j, v);

	}

	private String getHighestAttract(int i, int j) {
		int highestAttractDirectionIndex = 0;
		// The Stay means all other neighbors' attraction are smaller than
		// itself

		String[] attractDirection = {"outSW", "outS", "outSE", "outW", "Stay", "outE", "outNW", "outN", "outNE"};
		double[] neighborValue = {(i - 1 >= 0 && j - 1 >= 0) ? Value2Macro(i - 1, j - 1) : 0.0,
				(i - 1 >= 0) ? Value2Macro(i - 1, j) : 0.0,
				(i - 1 >= 0 && j + 1 < height1) ? Value2Macro(i - 1, j + 1) : 0.0,
				(j - 1 >= 0) ? Value2Macro(i, j - 1) : 0.0, Value2Macro(i, j),
				(j + 1 < height1) ? Value2Macro(i, j + 1) : 0.0,
				(i + 1 < width1 && j - 1 >= 0) ? Value2Macro(i + 1, j - 1) : 0.0,
				(i + 1 < width1) ? Value2Macro(i + 1, j) : 0.0,
				(i + 1 < width1 && j + 1 < height1) ? Value2Macro(i + 1, j + 1) : 0.0};

		highestAttractDirectionIndex = getDestinationIndex(neighborValue, getNumReceptors(i, j));

		return attractDirection[highestAttractDirectionIndex];

	}

	private double getNumReceptors(int i, int j) {
		double X4Receptors = ODEPDEModelica.getC4(i, j) + ODEPDEModelica.getC4B(i, j) + ODEPDEModelica.getR4(i, j)
				+ ODEPDEModelica.getR4B(i, j);

		return X4Receptors;

	}

	private int getDestinationIndex(double[] valueArray, double numReceptors) {
//		if (valueArray.length != 9) {
//			throw new IllegalStateException("The number of neighboors should be 9 for Moore.\n");
//		}
		int neighbors = valueArray.length;
		double length = Param._gridspacelength;
		double vol = Math.pow(length, 3) * 1e-15;
		double Nav = 6.02e23;
		double KD = Param._Kd_R4_L12 * Nav * vol * 1e-9; // nM converted to units of # molecules per gridspace (to have
															// same units as cxcl12 later in the FRO algorithm)

		double inhibitorConc = Param._inhibitorConc; // didn't convert units; the ratio of inhibitorConc/Kd_inhib_R4
														// cancels it out
		double Kd_inhib_R4 = Param._Kd_inhib_R4; // didn't convert units, see inhibitorConc
		double inhibitorRatio = inhibitorConc / Kd_inhib_R4;

		double sensAdjust = 0;
		double sensAdjustInterceptY = Param._sensAdjustInterceptY;
		double sensAdjustInterceptX = Param._sensAdjustInterceptX;

		double sensAdjustSlope = -sensAdjustInterceptY / sensAdjustInterceptX;

		if (numReceptors <= sensAdjustInterceptX) {
			sensAdjust = sensAdjustSlope * numReceptors + sensAdjustInterceptY;
		} else {
			sensAdjust = 0;
		}

		double[] cxcl12conc = valueArray; // this vector holds the cxcl12 concentrations of the neighbors
		double[] numReceptorOccupied = new double[neighbors];
		double[] prob = new double[neighbors];

		for (int i = 0; i < neighbors; i++) // initialize all vectors to zero.
		{
			numReceptorOccupied[i] = 0.0;
			prob[i] = 0.0;
		}

		int k = 0;

		// Calculate fractional receptor occupancy multiplied by 1/9 of total unoccupied
		// receptors
		// Also search for minimum value in neighborhood.
		int min = 0;
		for (k = 0; k < neighbors; k++) {
			double cxcl12Ratio = cxcl12conc[k] / KD;
			double denominator = (cxcl12Ratio + inhibitorRatio + 1) * 9;

			numReceptorOccupied[k] = (cxcl12Ratio / denominator) * numReceptors; // the denominator takes into account
																					// the division by 9

			if (numReceptorOccupied[k] < numReceptorOccupied[min]) {
				min = k;
			}
		}

		// Take difference from minimum value and add sensitivity adjustment factor
		// (sensAdjust)
		for (k = 0; k < neighbors; k++) {
			prob[k] = numReceptorOccupied[k] - numReceptorOccupied[min] + sensAdjust;
		}

		// determine the sum of all the probabilities
		double sum = 0;
		for (int i = 0; i < neighbors; i++) {
			sum += prob[i];
		}

		if (sum > 0.0) {
			// normalize
			for (int i = 0; i < neighbors; i++) {
				prob[i] /= sum;
			}

			// compute cumulative array
			double[] cumProb = new double[neighbors];
			cumProb[0] = prob[0];
			for (int i = 1; i < neighbors; i++) // initialization all cumProb[0] to 0.
			{
				cumProb[i] = 0;
			}

			for (int i = 1; i < neighbors; i++) {
				cumProb[i] = (cumProb[i - 1] + prob[i]);
			}

			// linear search
			double r = (new Random()).nextDouble();

			for (k = 0; k < neighbors && cumProb[k] < r; k++) {
			}

		} else {
			// prob[i] = 0 for all i.
			// Pick from the neighbors with equal probability.
			k = (new Random()).nextInt(neighbors);
		}

		return k;
	}

	class Index {
		int i, j;

		public Index(int i, int j) {
			this.i = i;
			this.j = j;
		}

	}

}
