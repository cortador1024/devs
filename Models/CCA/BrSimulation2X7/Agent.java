package CCA.BrSimulation2X7;

import java.util.ArrayList;
import java.util.Random;

import GenCol.entity;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import model.modeling.CAModels.indexEntity;

public class Agent extends TwoDimCell {
	protected entity KIBmsg;
	protected indexEntity msg, originLocation;
	// 8 neighbors and 1 itself
	protected String[] goingDirections = {"outN", "outNE", "outE", "outSE", "outS", "outSW", "outW", "outNW", "Stay"};
	protected String[] neighborIn = {"inN", "inNE", "inE", "inSE", "inS", "inSW", "inW", "inNW"};
	protected String[] neighborOut = {"outN", "outNE", "outE", "outSE", "outS", "outSW", "outW", "outNW"};
	protected String[] comingDirections = {"No", "No", "No", "No", "No", "No", "No", "No"};

	protected String attractDirection = "none";
	protected ArrayList<Integer> comingShuffle = new ArrayList<Integer>();
	Random r = new Random();

	// status is the phase
	private String status;
	private String goDirection = "Stay";
	private String comeDirection = "No";
	private String finalDirection = "No";

	private int step;
	// protected boolean clocked = true;
	private double CXCL12Rate = 0;

	public Agent() {
		this(0, 0);
	}

	public Agent(int xcoord, int ycoord) {
		super(xcoord, ycoord);
		status = "EMPTY";
		step = 1;
		addInport("inKIB");
		addOutport("outKIB");
	}

	public Agent(int xcoord, int ycoord, String _status) {
		super(xcoord, ycoord);
		status = _status;
		step = 1;
		addInport("inKIB");
		addOutport("outKIB");
	}

	public Agent(int xcoord, int ycoord, String _status, int _step) {
		super(xcoord, ycoord);
		status = _status;
		step = _step;
		addInport("inKIB");
		addOutport("outKIB");

	}

	/**
	 * Initialization method
	 */
	public void initialize() {
		super.initialize();
		if (status == "EMPTY") {
			holdIn("EMPTY", INFINITY);
		} else {
			originLocation = new indexEntity(getXcoord(), getYcoord(), "Origin");
			holdIn("Has Agent: " + status, step);
		}
		// Define the Phase Color for CA Display
		AgentUI.setPhaseColor();
	}

	/**
	 * External Transition Function
	 */

	public void deltext(double e, message x) {

		Continue(e);
		for (int i = 0; i < x.getLength(); i++) {
			if (somethingOnPort(x, "inKIB")) {
				KIBmsg = x.getValOnPort("inKIB", i);
				if (KIBmsg != null && KIBmsg.toString().contains("out")) {
					attractDirection = KIBmsg.toString();
				}
			}

			for (int j = 0; j < neighborIn.length; j++) {
				if (somethingOnPort(x, neighborIn[j])) {
					msg = (indexEntity) x.getValOnPort(neighborIn[j], i);
					if (msg != null && msg.getInput().contains("SENSING")) {
						if (status == "EMPTY")
							comingDirections[j] = "WANTMOVE";
					} else if (msg != null && msg.getInput().contains("COMING")) {
						finalDirection = neighborOut[j];
					} else if (msg != null) {
						phase = "Has Agent: " + msg.getInput();
						status = msg.getInput();
						originLocation = new indexEntity(msg.getI(), msg.getJ(), msg.getInput());
					}
				}
			}
		}

		for (int i = 0; i < comingDirections.length; i++) {
			if (comingDirections[i] != "No") {
				comingShuffle.add(i);
			}
		}
		int pickCome = 0;
		if (comingShuffle.size() > 0) {
			pickCome = r.nextInt(comingShuffle.size());
			comeDirection = neighborIn[comingShuffle.get(pickCome)];
		}

		if (attractDirection != "none") {
			holdIn(phase, 0);
		} else if (goDirection == "Stay" && comeDirection == "No" && finalDirection == "No") {
			holdIn(phase, step);
		} else if (comeDirection != "No") {
			holdIn("COMING:- " + comeDirection, 0);
		} else if (finalDirection != "No") {
			holdIn(status + ":- MOVING to " + finalDirection, 0);
		}

		// reset the direction array
		for (int i = 0; i < comingDirections.length; i++) {
			comingDirections[i] = "No";
		}
		comingShuffle.clear();

		// System.out.println(getXcoord() + ", " + getYcoord() + ": " + phase);

	}

	/*
	 * Internal Transition Function
	 */

	public void deltint() {

		if (status == "EMPTY") {
			holdIn("EMPTY", INFINITY);
		} 
		else if (status == "TAKEN") {
			holdIn("TAKEN", INFINITY);
		}else if (attractDirection != "none") {
			holdIn(phase, 0);
		} else if (phase.contains("Has Agent")) {
			holdIn(phase, step);
		} else {
			holdIn(phase, INFINITY);
		}

		originLocation = new indexEntity(getXcoord(), getYcoord(), "Origin");

	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	/*
	 * Message out Function
	 */
	public message out() {

		message m = new message();

		if (phase.contains("Has Agent")) {
			if (phase.contains("CXCR4")) {
				goDirection = attractDirection;

			} else {
				goDirection = goingDirections[r.nextInt(goingDirections.length)];
			}
			for (int i = 0; i < neighborOut.length; i++) {
				if (goDirection == neighborOut[i]) {
					m.add(makeContent(neighborOut[i], new indexEntity(getXcoord(), getYcoord(), "SENSING")));
					break;
				}
			}
			if (phase.contains("CXCL12")) {
//				m.add(makeContent("outKIB", new indexEntity(getXcoord(),
//						getYcoord(), "" + CXCL12Rate)));
				m.add(makeContent("outKIB", new indexEntity(getXcoord(), getYcoord(), originLocation.getI(),
						originLocation.getJ(), "L12")));
			} else if (phase.contains("CXCR4") && attractDirection == "none") {
				m.add(makeContent("outKIB",
						new indexEntity(getXcoord(), getYcoord(), originLocation.getI(), originLocation.getJ(), "X4")));

			} else if (phase.contains("CXCR7")) {
				m.add(makeContent("outKIB",
						new indexEntity(getXcoord(), getYcoord(), originLocation.getI(), originLocation.getJ(), "X7")));

			}
		} else if (phase.contains("COMING")) {
			for (int i = 0; i < neighborOut.length; i++) {
				if (comeDirection == neighborIn[i]) {
					m.add(makeContent(neighborOut[i], new indexEntity(getXcoord(), getYcoord(), phase)));
					status = "TAKEN";
					break;
				}
			}
		} else if (phase.contains("MOVING")) {
			for (int i = 0; i < neighborOut.length; i++) {
				if (finalDirection == neighborOut[i]) {
					m.add(makeContent(neighborOut[i], new indexEntity(getXcoord(), getYcoord(), status)));
					// m.add(makeContent("outKIB", new indexEntity(getXcoord(),
					// getYcoord(), "Empty")));
					status = "EMPTY";
					break;
				}
			}

		}

		goDirection = "Stay";
		comeDirection = "No";
		finalDirection = "No";
		attractDirection = "none";

		return m;

	}

	public String getStatus() {
		return status;
	}

	public void setInitialStatus(String status) {
		this.status = status;
	}

}
