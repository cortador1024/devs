package CellularAutomata.AsyncABM;

import GenCol.entity;

public class abEntity extends entity{
	private String input;
	private int sigma;

	public abEntity(String input, int sig) {
		super(input);
		this.input = input;
		this.sigma = sig;

	}

	public String getInput() {
		return input;
	}
	
	public int getSigma() {
		return sigma;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public void setSigma(int sig) {
		this.sigma = sig;
	}

}
