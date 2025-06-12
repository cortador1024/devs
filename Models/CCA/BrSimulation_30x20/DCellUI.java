package CCA.BrSimulation_30x20;

import javafx.scene.paint.Color;
import view.CAView.CAViewUI;

public class DCellUI {
	private static double maxValue = 210;
	public static void setPhaseColor() {
		for (int i = 0; i <= maxValue; i++) {
			CAViewUI.addPhaseColor(i+":", Color.rgb(0,100,0, i / maxValue));
		}
	}
	public static double getMaxValue() {
		return maxValue;
	}
	public static void setMaxValue(double maxValue) {
		DCellUI.maxValue = maxValue;
		setPhaseColor();
	}

}
