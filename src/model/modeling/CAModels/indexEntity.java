package model.modeling.CAModels;

import GenCol.entity;

public class indexEntity extends entity {
	private String input;
	private int i, j;
	private int source_i = -1, source_j = -1;

	public indexEntity(int i, int j, String input) {
		super("i:"+i+",j:"+j+" "+input);
		this.i = i;
		this.j = j;
		this.input = input;

	}
	
	public indexEntity(int i, int j, int source_i, int source_j, String input) {
		
		super("i:"+i+",j:"+j+" "+input);
		this.i = i;
		this.j = j;
		this.source_i = source_i;
		this.source_j = source_j;
		this.input = input;		
		
	}

	public String getInput() {
		return input;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getSource_i() {
		return source_i;
	}

	public void setSource_i(int source_i) {
		this.source_i = source_i;
	}

	public int getSource_j() {
		return source_j;
	}

	public void setSource_j(int source_j) {
		this.source_j = source_j;
	}

}
