package CCA.BrSimulation2X7;

import org.javafmi.wrapper.Simulation;

import model.modeling.CAModels.indexEntity;

public class ModelicaModel_ODEwithPDE {
	private double[][] CXCL12Array;
	private double[][] CXCL12iArray;

	private X4ODE[][] X4ODEArray;
	private X7ODE[][] X7ODEArray;

	private boolean[][] hasX4;
	private boolean[][] hasX7;
	private boolean[][] hasL12;

	private boolean[][] changed;

	private int width, height;
	private double ODEstepSize = 0.01;
	private double PDEstepSize = 0.1;

	private String ODEpath, PDEpath;

	private double MaxColorValue = 0;

	private double X4DistanceMoved = 0.0;

	private Simulation ODEsimulation, PDEsimulation;

	public ModelicaModel_ODEwithPDE(int x, int y) {
		width = x;
		height = y;
		CXCL12Array = new double[x][y];
		CXCL12iArray = new double[x][y];
		X4ODEArray = new X4ODE[x][y];
		X7ODEArray = new X7ODE[x][y];

		hasX4 = new boolean[x][y];
		hasX7 = new boolean[x][y];
		hasL12 = new boolean[x][y];
		changed = new boolean[x][y];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				CXCL12Array[i][j] = 0.0;
				CXCL12iArray[i][j] = 0.0;

				X4ODEArray[i][j] = new X4ODE();
				X7ODEArray[i][j] = new X7ODE();

				hasX4[i][j] = false;
				hasX7[i][j] = false;
				hasL12[i][j] = false;
				changed[i][j] = true;
			}
		}

		if (System.getProperty("os.name").startsWith("Mac")) {
			if (Param.barrestin) {
				ODEpath = "Models/Modelica/FMU/Mac/BrODE2X7.fmu";
				PDEpath = "Models/Modelica/FMU/Mac/BrPDE2X7.fmu";
			} else {
				ODEpath = "Models/Modelica/FMU/Mac/BrODE2X7.fmu";
				PDEpath = "Models/Modelica/FMU/Mac/BrPDE2X7.fmu";
			}
		} else if (System.getProperty("os.name").startsWith("Win")) {
			ODEpath = "Models/Modelica/FMU/Win/BrODE2X7.fmu";
			PDEpath = "Models/Modelica/FMU/Win/BrPDE2X7.fmu";

		} else {
			throw new IllegalStateException("FMU file not find for " + System.getProperty("os.name") + "\n");

		}

		ODEsimulation = new Simulation(ODEpath);
		PDEsimulation = new Simulation(PDEpath);

	}

	public void execute(double stopTime) {

		this.execute(0, stopTime);

	}

	public void execute(double startTime, double stopTime) {
		this.execute(startTime, stopTime, PDEstepSize);

	}

	public void execute(double startTime, double stopTime, double stepSize) {
		for (int step = 0; step < (stopTime - startTime) / stepSize; step++) {
			PDEsimulation.init(startTime, startTime + stepSize);
			
			// PDE Simulation

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					PDEsimulation.write("CXCL12[" + (i + 1) + "," + (j + 1) + "]").with(CXCL12Array[i][j]);
					if (this.getHasL12(i, j) == true) {
						PDEsimulation.write("L12[" + (i + 1) + "," + (j + 1) + "]").with(0.0);
						PDEsimulation.write("CXCL12[" + (i + 1) + "," + (j + 1) + "]").with(CXCL12Array[i][j] + 10*PDEstepSize);
					} else {
						PDEsimulation.write("L12[" + (i + 1) + "," + (j + 1) + "]").with(0.0);
					}
				}
			}

			PDEsimulation.doStep(stepSize);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {

					double temp = PDEsimulation.read("CXCL12[" + (i + 1) + "," + (j + 1) + "]").asDouble();
					CXCL12Array[i][j] = temp;
					if (temp > MaxColorValue) {
						MaxColorValue = Math.ceil(temp);
					}
				}
			}

			// ODE Simulation
			ODEsimulation.init(startTime, startTime + stepSize);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {

					ODEsimulation.write("CXCL12[" + (i + 1) + "," + (j + 1) + "]").with(CXCL12Array[i][j]);

					if (changed[i][j]) {
						if (this.getHasX4(i, j) == true) {
							ODEsimulation.write("C4[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].C4);
							ODEsimulation.write("C4i[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].C4i);
							ODEsimulation.write("R4[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].R4);
							ODEsimulation.write("R4i[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].R4i);
							if (Param.barrestin) {
								ODEsimulation.write("R4B[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].R4B);
								ODEsimulation.write("C4B[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].C4B);
								ODEsimulation.write("C4Bii[" + (i + 1) + "," + (j + 1) + "]")
										.with(X4ODEArray[i][j].C4Bii);
								ODEsimulation.write("B4[" + (i + 1) + "," + (j + 1) + "]").with(X4ODEArray[i][j].B4);
								ODEsimulation.write("X4[" + (i + 1) + "," + (j + 1) + "]").with(1.0);
							}

							ODEsimulation.write("CXCL12i[" + (i + 1) + "," + (j + 1) + "]").with(CXCL12iArray[i][j]);

						} else if (this.getHasX7(i, j) == true) {
							ODEsimulation.write("C7[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].C7);
							ODEsimulation.write("C7i[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].C7i);
							ODEsimulation.write("R7[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].R7);
							ODEsimulation.write("R7i[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].R7i);
							if (Param.barrestin) {
								ODEsimulation.write("R7B[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].R7B);
								ODEsimulation.write("C7B[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].C7B);
								ODEsimulation.write("R7Bii[" + (i + 1) + "," + (j + 1) + "]")
										.with(X7ODEArray[i][j].R7Bii);
								ODEsimulation.write("C7Bii[" + (i + 1) + "," + (j + 1) + "]")
										.with(X7ODEArray[i][j].C7Bii);
								ODEsimulation.write("B7[" + (i + 1) + "," + (j + 1) + "]").with(X7ODEArray[i][j].B7);
								ODEsimulation.write("X7[" + (i + 1) + "," + (j + 1) + "]").with(1.0);
							}

							ODEsimulation.write("CXCL12i[" + (i + 1) + "," + (j + 1) + "]").with(CXCL12iArray[i][j]);

						}

					}
				}
			}
			// for (int i = 0; i < (stopTime - startTime) / stepSize; i++) {
			ODEsimulation.doStep(ODEstepSize);
			// }

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {

					double temp = ODEsimulation.read("CXCL12[" + (i + 1) + "," + (j + 1) + "]").asDouble();
					CXCL12Array[i][j] = temp;
					if (temp > MaxColorValue) {
						MaxColorValue = Math.ceil(temp);
					}
					if (changed[i][j]) {
						if (this.getHasX4(i, j) == true) {
							X4ODEArray[i][j].C4 = ODEsimulation.read("C4[" + (i + 1) + "," + (j + 1) + "]").asDouble();
							X4ODEArray[i][j].C4i = ODEsimulation.read("C4i[" + (i + 1) + "," + (j + 1) + "]")
									.asDouble();
							X4ODEArray[i][j].R4 = ODEsimulation.read("R4[" + (i + 1) + "," + (j + 1) + "]").asDouble();
							X4ODEArray[i][j].R4i = ODEsimulation.read("R4i[" + (i + 1) + "," + (j + 1) + "]")
									.asDouble();
							if (Param.barrestin) {
								X4ODEArray[i][j].R4B = ODEsimulation.read("R4B[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X4ODEArray[i][j].C4B = ODEsimulation.read("C4B[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X4ODEArray[i][j].C4Bii = ODEsimulation.read("C4Bii[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X4ODEArray[i][j].B4 = ODEsimulation.read("B4[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
							}
							CXCL12iArray[i][j] = ODEsimulation.read("CXCL12i[" + (i + 1) + "," + (j + 1) + "]")
									.asDouble();

						} else if (this.getHasX7(i, j) == true) {
							X7ODEArray[i][j].C7 = ODEsimulation.read("C7[" + (i + 1) + "," + (j + 1) + "]").asDouble();
							X7ODEArray[i][j].C7i = ODEsimulation.read("C7i[" + (i + 1) + "," + (j + 1) + "]")
									.asDouble();
							X7ODEArray[i][j].R7 = ODEsimulation.read("R7[" + (i + 1) + "," + (j + 1) + "]").asDouble();
							X7ODEArray[i][j].R7i = ODEsimulation.read("R7i[" + (i + 1) + "," + (j + 1) + "]")
									.asDouble();
							if (Param.barrestin) {
								X7ODEArray[i][j].R7B = ODEsimulation.read("R7B[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X7ODEArray[i][j].C7B = ODEsimulation.read("C7B[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X7ODEArray[i][j].R7Bii = ODEsimulation.read("R7Bii[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X7ODEArray[i][j].C7Bii = ODEsimulation.read("C7Bii[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
								X7ODEArray[i][j].B7 = ODEsimulation.read("B7[" + (i + 1) + "," + (j + 1) + "]")
										.asDouble();
							}
							CXCL12iArray[i][j] = ODEsimulation.read("CXCL12i[" + (i + 1) + "," + (j + 1) + "]")
									.asDouble();

						}

					}
				}
			}

			if (MaxColorValue != 0) {
				DCellUI.setMaxValue(MaxColorValue);
				MaxColorValue = 0;
			}
			PDEsimulation.reset();
			ODEsimulation.reset();
			// simulation.terminate();
		}

	}

	public double getR4(int x, int y) {
		return X4ODEArray[x][y].R4;
	}

	public void setR4(int x, int y, double newV) {

		X4ODEArray[x][y].R4 = newV;
	}

	public double getR4i(int x, int y) {
		return X4ODEArray[x][y].R4i;
	}

	public void setR4i(int x, int y, double newV) {

		X4ODEArray[x][y].R4i = newV;
	}

	public double getC4(int x, int y) {
		return X4ODEArray[x][y].C4;
	}

	public void setC4(int x, int y, double newV) {

		X4ODEArray[x][y].C4 = newV;
	}

	public double getC4i(int x, int y) {
		return X4ODEArray[x][y].C4i;
	}

	public void setC4i(int x, int y, double newV) {

		X4ODEArray[x][y].C4i = newV;
	}

	public double getR4B(int x, int y) {
		return X4ODEArray[x][y].R4B;
	}

	public void setR4B(int x, int y, double newV) {

		X4ODEArray[x][y].R4B = newV;
	}

	public double getC4B(int x, int y) {
		return X4ODEArray[x][y].C4B;
	}

	public void setC4B(int x, int y, double newV) {

		X4ODEArray[x][y].C4B = newV;
	}

	public double getC4Bii(int x, int y) {
		return X4ODEArray[x][y].C4Bii;
	}

	public void setC4Bii(int x, int y, double newV) {

		X4ODEArray[x][y].C4Bii = newV;
	}

	public double getB4(int x, int y) {
		return X4ODEArray[x][y].B4;
	}

	public void setB4(int x, int y, double newV) {

		X4ODEArray[x][y].B4 = newV;
	}

	public double getR7(int x, int y) {
		return X7ODEArray[x][y].R7;
	}

	public void setR7(int x, int y, double newV) {

		X7ODEArray[x][y].R7 = newV;
	}

	public double getR7i(int x, int y) {
		return X7ODEArray[x][y].R7i;
	}

	public void setR7i(int x, int y, double newV) {

		X7ODEArray[x][y].R7i = newV;
	}

	public double getC7(int x, int y) {
		return X7ODEArray[x][y].C7;
	}

	public void setC7(int x, int y, double newV) {

		X7ODEArray[x][y].C7 = newV;
	}

	public double getC7i(int x, int y) {
		return X7ODEArray[x][y].C7i;
	}

	public void setC7i(int x, int y, double newV) {

		X7ODEArray[x][y].C7i = newV;
	}

	public double getR7B(int x, int y) {
		return X7ODEArray[x][y].R7B;
	}

	public void setR7B(int x, int y, double newV) {

		X7ODEArray[x][y].R7B = newV;
	}

	public double getC7B(int x, int y) {
		return X7ODEArray[x][y].C7B;
	}

	public void setC7B(int x, int y, double newV) {

		X7ODEArray[x][y].C7B = newV;
	}

	public double getR7Bii(int x, int y) {
		return X7ODEArray[x][y].R7Bii;
	}

	public void setR7Bii(int x, int y, double newV) {

		X7ODEArray[x][y].R7Bii = newV;
	}

	public double getC7Bii(int x, int y) {
		return X7ODEArray[x][y].C7Bii;
	}

	public void setC7Bii(int x, int y, double newV) {

		X7ODEArray[x][y].C7Bii = newV;
	}

	public double getB7(int x, int y) {
		return X7ODEArray[x][y].B7;
	}

	public void setB7(int x, int y, double newV) {

		X7ODEArray[x][y].B7 = newV;
	}

	public double getCXCL12(int x, int y) {

		return CXCL12Array[x][y];
	}

	public void setCXCL12(int x, int y, double newV) {

		CXCL12Array[x][y] = newV;
	}

	public void accumulateValue(int x, int y, double newV) {

		CXCL12Array[x][y] += newV;
	}

	public double getCXCL12i(int x, int y) {

		return CXCL12iArray[x][y];
	}

	public void setCXCL12i(int x, int y, double newV) {

		CXCL12iArray[x][y] = newV;
	}

	public boolean getHasX4(int x, int y) {
		return hasX4[x][y];
	}

	public void setHasX4(int x, int y, boolean _hasX4) {
		this.hasX4[x][y] = _hasX4;
	}

	public boolean getHasX7(int x, int y) {
		return hasX7[x][y];
	}

	public void setHasX7(int x, int y, boolean _hasX7) {
		this.hasX7[x][y] = _hasX7;
	}

	public boolean getHasL12(int x, int y) {
		return hasL12[x][y];
	}

	public void setHasL12(int x, int y, boolean _hasL12) {
		this.hasL12[x][y] = _hasL12;
	}

	public void moveX4(indexEntity input) {
		if (input.getSource_i() >= 0 && input.getSource_j() >= 0) {
			if (input.getSource_i() != input.getI() || input.getSource_j() != input.getJ()) {

				this.setR4(input.getI(), input.getJ(), this.getR4(input.getSource_i(), input.getSource_j()));
				this.setC4(input.getI(), input.getJ(), this.getC4(input.getSource_i(), input.getSource_j()));
				this.setC4i(input.getI(), input.getJ(), this.getC4i(input.getSource_i(), input.getSource_j()));
				this.setR4i(input.getI(), input.getJ(), this.getR4i(input.getSource_i(), input.getSource_j()));

				this.setR4B(input.getI(), input.getJ(), this.getR4B(input.getSource_i(), input.getSource_j()));
				this.setC4B(input.getI(), input.getJ(), this.getC4B(input.getSource_i(), input.getSource_j()));
				this.setC4Bii(input.getI(), input.getJ(), this.getC4Bii(input.getSource_i(), input.getSource_j()));
				this.setB4(input.getI(), input.getJ(), this.getB4(input.getSource_i(), input.getSource_j()));

				this.setCXCL12i(input.getI(), input.getJ(), this.getCXCL12i(input.getSource_i(), input.getSource_j()));

				this.setC4(input.getSource_i(), input.getSource_j(), 0.0);
				this.setR4(input.getSource_i(), input.getSource_j(), 0.0);
				this.setC4i(input.getSource_i(), input.getSource_j(), 0.0);
				this.setR4i(input.getSource_i(), input.getSource_j(), 0.0);

				this.setR4B(input.getSource_i(), input.getSource_j(), 0.0);
				this.setC4B(input.getSource_i(), input.getSource_j(), 0.0);
				this.setC4Bii(input.getSource_i(), input.getSource_j(), 0.0);
				this.setB4(input.getSource_i(), input.getSource_j(), 0.0);

				this.setCXCL12i(input.getSource_i(), input.getSource_j(), 0.0);

				this.setHasX4(input.getI(), input.getJ(), true);
				changed[input.getI()][input.getJ()] = true;
				this.setHasX4(input.getSource_i(), input.getSource_j(), false);
				changed[input.getSource_i()][input.getSource_j()] = true;

				X4DistanceMoved += input.getI() - input.getSource_i();

			} else {
				this.setHasX4(input.getI(), input.getJ(), true);
				changed[input.getI()][input.getJ()] = true;
			}

		} else {

			throw new IllegalStateException("input source value not set.");
		}

	}

	public void moveX7(indexEntity input) {
		if (input.getSource_i() >= 0 && input.getSource_j() >= 0) {
			if (input.getSource_i() != input.getI() || input.getSource_j() != input.getJ()) {

				this.setR7(input.getI(), input.getJ(), this.getR7(input.getSource_i(), input.getSource_j()));
				this.setC7(input.getI(), input.getJ(), this.getC7(input.getSource_i(), input.getSource_j()));
				this.setC7i(input.getI(), input.getJ(), this.getC7i(input.getSource_i(), input.getSource_j()));
				this.setR7i(input.getI(), input.getJ(), this.getR7i(input.getSource_i(), input.getSource_j()));

				this.setR7B(input.getI(), input.getJ(), this.getR7B(input.getSource_i(), input.getSource_j()));
				this.setC7B(input.getI(), input.getJ(), this.getC7B(input.getSource_i(), input.getSource_j()));
				this.setR7Bii(input.getI(), input.getJ(), this.getR7Bii(input.getSource_i(), input.getSource_j()));
				this.setC7Bii(input.getI(), input.getJ(), this.getC7Bii(input.getSource_i(), input.getSource_j()));
				this.setB7(input.getI(), input.getJ(), this.getB7(input.getSource_i(), input.getSource_j()));

				this.setCXCL12i(input.getI(), input.getJ(), this.getCXCL12i(input.getSource_i(), input.getSource_j()));

				this.setC7(input.getSource_i(), input.getSource_j(), 0.0);
				this.setR7(input.getSource_i(), input.getSource_j(), 0.0);
				this.setC7i(input.getSource_i(), input.getSource_j(), 0.0);
				this.setR7i(input.getSource_i(), input.getSource_j(), 0.0);

				this.setR7B(input.getSource_i(), input.getSource_j(), 0.0);
				this.setC7B(input.getSource_i(), input.getSource_j(), 0.0);
				this.setR7Bii(input.getSource_i(), input.getSource_j(), 0.0);
				this.setC7Bii(input.getSource_i(), input.getSource_j(), 0.0);
				this.setB7(input.getSource_i(), input.getSource_j(), 0.0);

				this.setHasX7(input.getI(), input.getJ(), true);
				changed[input.getI()][input.getJ()] = true;
				this.setHasX7(input.getSource_i(), input.getSource_j(), false);
				changed[input.getSource_i()][input.getSource_j()] = true;
			} else {
				this.setHasX7(input.getI(), input.getJ(), true);
				changed[input.getI()][input.getJ()] = true;
			}

		} else {

			throw new IllegalStateException("input source value not set.");
		}

	}

	public void moveL12(indexEntity input) {
		if (input.getSource_i() >= 0 && input.getSource_j() >= 0) {
			if (input.getSource_i() != input.getI() || input.getSource_j() != input.getJ()) {

				this.setHasL12(input.getI(), input.getJ(), true);
				changed[input.getI()][input.getJ()] = true;
				this.setHasL12(input.getSource_i(), input.getSource_j(), false);
				changed[input.getSource_i()][input.getSource_j()] = true;
			} else {
				this.setHasL12(input.getI(), input.getJ(), true);
				changed[input.getI()][input.getJ()] = true;
			}

		} else {

			throw new IllegalStateException("input source value not set.");
		}

	}

	private double getsolConc(int col) {
		double ConcSumResult = 0.0;
		double vol = Math.pow(Param._gridspacelength, 3) * 1e-15; // volume in L
		double Nav = 6.02e23;
		double nMperM = 1e9;
		double conversion = nMperM / (Nav * vol);
		for (int j = 0; j < height; j++) {
			ConcSumResult += CXCL12Array[col][j] * conversion;
		}
		return ConcSumResult / height;
	}

	public double computeConcAbsGrad() {
		double AbsGradEnd = getsolConc(Param.X4column + Param.colSpan);
		double AbsGraStart = getsolConc(Param.X4column);

		double AbsGradResult = (AbsGradEnd - AbsGraStart) / (Param.colSpan * Param._gridspacelength);
		return AbsGradResult;
	}

	public double getAverageMigration() {
		return (X4DistanceMoved / Param.X4count) * Param._gridspacelength;

	}

}
