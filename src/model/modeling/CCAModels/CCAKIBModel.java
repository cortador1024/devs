package model.modeling.CCAModels;

import java.util.ArrayList;
import java.util.List;
import model.modeling.CCAModels.CCAKIB.CCAGeoKIB;
import model.modeling.GeoKIB.GeoMap;
import model.modeling.GeoKIB.Length;
import model.modeling.GeoKIB.LengthUnit;
import model.modeling.GeoKIB.MapRegion;

public class CCAKIBModel extends CCAGeoKIB {

	public CCAKIBModel(String name, int xsize1, int ysize1, int xsize2,
			int ysize2) {
		this(name, new MapRegion(xsize1, ysize1),
				new MapRegion(xsize2, ysize2));

	}

	public CCAKIBModel(String name, int xsize1, int ysize1, Length gridSize1,
			int xsize2, int ysize2, Length gridSize2) {
		this(name, xsize1, ysize1, 0, 0, gridSize1, xsize2, ysize2, 0, 0,
				gridSize2);

	}

	public CCAKIBModel(String name, int xsize1, int ysize1, double west1,
			double north1, Length gridSize1, int xsize2, int ysize2,
			double west2, double north2, Length gridSize2) {

		this(name, xsize1, ysize1, north1,
				north1 + ysize1 * gridSize1.getLengthNumber(), west1,
				west1 + xsize1 * gridSize1.getLengthNumber(),
				gridSize1.getLengthUnit(), xsize2, ysize2, north2,
				north2 + ysize2 * gridSize2.getLengthNumber(), west2,
				west2 + xsize2 * gridSize2.getLengthNumber(),
				gridSize2.getLengthUnit());

	}

	public CCAKIBModel(String name, MapRegion mapR_A, MapRegion mapR_B,
			double newLocationX_A, double newLocationY_A, double newLocationX_B,
			double newLocationY_B, LengthUnit commonUnit) {

		this(name, mapR_A.rearrange(newLocationX_A, newLocationY_A, commonUnit),
				mapR_B.rearrange(newLocationX_B, newLocationY_B, commonUnit));

	}

	public CCAKIBModel(String name, int xsize1, int ysize1, double north1,
			double south1, double west1, double east1, LengthUnit lu1,
			int xsize2, int ysize2, double north2, double south2, double west2,
			double east2, LengthUnit lu2) {

		super(name,
				new GeoMap(new MapRegion(xsize1, ysize1, north1, south1, west1,
						east1, lu1)),
				new GeoMap(new MapRegion(xsize2, ysize2, north2, south2, west2,
						east2, lu2)));

	}

	public CCAKIBModel(String name, MapRegion mapR_A, MapRegion mapR_B) {

		this(name, mapR_A, "0", mapR_B, "0");
	}

	// Major construction function
	public CCAKIBModel(String name, MapRegion mapR_A, String defaultA,
			MapRegion mapR_B, String defaultB) {

		super(name, new GeoMap(mapR_A, defaultA), new GeoMap(mapR_B, defaultB));

	}

	@Override
	protected GeoMap measurement_conversion(List<GeoMap> input_maps) {
		// TODO Auto-generated method stub
		return null;
	}

}
