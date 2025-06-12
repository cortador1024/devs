package model.modeling.GeoKIB;

public class GeoMap {
    /**
     * The region settings for this geographic map.
     */
    protected MapRegion region;

    /**
     * A set of values stored in this geographic map.
     */
    protected String[][] values;

    protected String defaultValue;

    /**
     * Indicates whether the values of this map are an indication of average value
     * (such as rainfall density) or a cumulative value (such as number of people).
     */
    protected boolean is_average_value = true;

    protected GeoMap() {
	
    }

    public GeoMap(MapRegion map_region) {
	this.region = map_region;
	values = new String[map_region.get_num_cols()][map_region.get_num_rows()];
	defaultValue = null;
	for (int i = 0; i < map_region.get_num_cols(); i++) {
	    for (int j = 0; j < map_region.get_num_rows(); j++) {
		values[i][j] = defaultValue;
	    }
	}

    }

    public GeoMap(MapRegion map_region, String defaultValue) {
	this.region = map_region;
	values = new String[map_region.get_num_cols()][map_region.get_num_rows()];
	for (int i = 0; i < map_region.get_num_cols(); i++) {
	    for (int j = 0; j < map_region.get_num_rows(); j++) {
		values[i][j] = defaultValue;
	    }
	}

    }

    public GeoMap(MapRegion map_region, String[][] values) {
	if (values[0].length != map_region.get_num_rows() || values.length != map_region.get_num_cols()) {
	    throw new IllegalArgumentException("An attempt was made to create a GeoMap object with an array of "
		    + "values of a size that does not match the region.\n");
	}
	this.region = map_region;
	this.values = values;
    }

    /**
     * Create a new GeoMap object as a copy of the provided object.
     */
    public GeoMap(GeoMap original_map) {
	MapRegion mapRegion = original_map.get_region();
	int numRows = mapRegion.get_num_rows();
	int numCols = mapRegion.get_num_cols();
	String[][] initialVals = new String[numCols][numRows];
	int colNum, rowNum;
	for (colNum = 0; colNum < numCols; colNum++) {
	    for (rowNum = 0; rowNum < numRows; rowNum++) {
		initialVals[colNum][rowNum] = original_map.get_value(colNum, rowNum);
	    }
	}
	this.region = mapRegion;
	this.values = initialVals;
    }

    /**
     * Return the object with the map region settings for this GeoMap object.
     */
    public MapRegion get_region() {
	return this.region;
    }

    /**
     * Return the value at the map cell with the specified row and column number.
     */
    public String get_value(int col_num, int row_num) {
	if (this.region == null) {
	    throw new IllegalStateException("The GeoMap object was not properly initialized.");
	}
	if (row_num > this.region.get_num_rows() || row_num < 0) {
	    throw new IllegalArgumentException("An out-of-bounds row number was used for a GeoMap object.");
	}
	if (col_num > this.region.get_num_cols() || col_num < 0) {
	    throw new IllegalArgumentException("An out-of-bounds column number was used for a GeoMap object.");
	}
	return values[col_num][row_num];
    }

    /**
     * Change the value of the specified map cell.
     */
    public void set_value(int col_num, int row_num, String value) {
	if (row_num < 0 || row_num >= this.region.get_num_rows()) {
	    throw new IllegalArgumentException(
		    "A row number of " + row_num + " was used in the set_value method of GeoMap.\n");
	}
	if (col_num < 0 || col_num >= this.region.get_num_cols()) {
	    throw new IllegalArgumentException(
		    "A column number of " + col_num + " was used in the set_value method of GeoMap.\n");
	}

	this.values[col_num][row_num] = value;
    }

    public boolean is_cumulative_value() {
	return !this.is_average_value;
    }

    public void set_is_cumulative_value(boolean is_cumulative) {
	this.is_average_value = !is_cumulative;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
