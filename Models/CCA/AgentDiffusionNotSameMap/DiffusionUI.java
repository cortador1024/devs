package CCA.AgentDiffusionNotSameMap;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import view.CAView.CAViewUI;

public class DiffusionUI
{
    public static void setPhaseColor()
    {
//        Image hatch = createHatch();
//        ImagePattern pattern = new ImagePattern(hatch, 0, 0, 20, 20, false); 
        for (int i = 0; i <= 1000; i++)
        {
            CAViewUI.addPhaseColor(
                i + "...",
                Color.rgb(200, 0, 0, i / 1000.0)
            );
        }
    }

//    private static Image createHatch()
//    {
//        Pane pane = new Pane();
//        pane.setPrefSize(20, 20);
//        Line fw = new Line(-5, -5, 25, 25);
//        Line bw = new Line(-5, 25, 25, -5);
//        fw.setStroke(Color.ALICEBLUE);
//        bw.setStroke(Color.ALICEBLUE);
//        fw.setStrokeWidth(5);
//        bw.setStrokeWidth(5);
//        pane.getChildren().addAll(fw, bw);
//        new Scene(pane);
//        return pane.snapshot(null, null);
//    }

}
