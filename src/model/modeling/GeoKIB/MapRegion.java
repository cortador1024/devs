package model.modeling.GeoKIB;

/**
 * A set of region settings for geographic maps.
 */
public class MapRegion {

	/**
	 * The number of rows in the maps.
	 */
	protected int num_rows;

	/**
	 * The number of columns in the maps.
	 */
	protected int num_cols;

	/**
	 * The value of the northern boundary.
	 */
	protected double north_bound;

	/**
	 * The value of the southern boundary.
	 */
	protected double south_bound;

	/**
	 * The value of the western boundary.
	 */
	protected double west_bound;

	/**
	 * The value of the eastern boundary.
	 */
	protected double east_bound;

	/**
	 * The value of the length Unit and Length.
	 */
	protected LengthUnit lengthUnit;

	protected Length widthLength, heightLength;

	/**
	 * Create map region settings with the specified parameters.
	 */

	public MapRegion(int cols, int rows, double north, double south,
			double west, double east, LengthUnit lengthU) {
		this.num_cols = cols;
		this.num_rows = rows;
		this.north_bound = north;
		this.south_bound = south;
		this.west_bound = west;
		this.east_bound = east;
		this.lengthUnit = lengthU;
		this.heightLength = new Length((south - north) / rows, lengthU);
		this.widthLength = new Length((east - west) / cols, lengthU);

	}

	public MapRegion(int cols, int rows) {

		this(cols, rows, 0.0, 100.0, 0.0, 100.0, LengthUnit.METER);

	}

	public MapRegion(int cols, int rows, double west, double north,
			Length widthLength, Length heightLength, LengthUnit commonLengthU) {
		this.num_cols = cols;
		this.num_rows = rows;
		this.west_bound = west;
		this.north_bound = north;

		if (widthLength.getLengthUnit() != commonLengthU) {
			this.widthLength = widthLength.unifyTo(commonLengthU);

		} else {
			this.widthLength = widthLength;

		}

		if (heightLength.getLengthUnit() != commonLengthU) {
			this.heightLength = heightLength.unifyTo(commonLengthU);
		} else {
			this.heightLength = heightLength;

		}

		this.lengthUnit = commonLengthU;

		this.south_bound = this.north_bound
				+ this.heightLength.getLengthNumber() * rows;

		this.east_bound = this.west_bound
				+ this.widthLength.getLengthNumber() * cols;

	}

	/**
	 * Return the number of rows in the region setting.
	 */
	public int get_num_rows() {
		return this.num_rows;
	}

	/**
	 * Return the number of columns in the region setting.
	 */
	public int get_num_cols() {
		return this.num_cols;
	}

	/**
	 * Return the value of the north boundary in the region setting.
	 */
	public double get_north() {
		return this.north_bound;
	}

	/**
	 * Return the value of the south boundary in the region setting.
	 */
	public double get_south() {
		return this.south_bound;
	}

	/**
	 * Return the value of the west boundary in the region setting.
	 */
	public double get_west() {
		return this.west_bound;
	}

	/**
	 * Return the value of the east boundary in the region setting.
	 */
	public double get_east() {
		return this.east_bound;
	}

	public LengthUnit getLengthUnit() {
		return lengthUnit;
	}

	public Length getWidthLength() {
		return widthLength;
	}

	public Length getHeightLength() {
		return heightLength;
	}

	public void setNorth_bound(double north_bound) {
		this.north_bound = north_bound;
	}

	public void setSouth_bound(double south_bound) {
		this.south_bound = south_bound;
	}

	public void setWest_bound(double west_bound) {
		this.west_bound = west_bound;
	}

	public void setEast_bound(double east_bound) {
		this.east_bound = east_bound;
	}

	public void setLengthUnit(LengthUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	public void setWidthLength(Length widthLength) {
		this.widthLength = widthLength;
	}

	public void setHeightLength(Length heightLength) {
		this.heightLength = heightLength;
	}

	public MapRegion rearrange(double newCordX, double newCordY,
			LengthUnit commonLengthU) {
		MapRegion originMap = this;
		MapRegion destMap = new MapRegion(originMap.get_num_cols(),
				originMap.get_num_rows(), newCordX, newCordY,
				originMap.getWidthLength(), originMap.getHeightLength(),
				commonLengthU);

		return destMap;
	}

}
