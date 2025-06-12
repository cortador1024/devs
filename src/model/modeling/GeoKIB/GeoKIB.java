package model.modeling.GeoKIB;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import model.modeling.KIB.KIB;

public abstract class GeoKIB extends KIB {

	protected GeoMap mapA;
	protected GeoMap mapB;

	protected MapRegion mapA_region;

	int mapA_NumRows;
	int mapA_NumCols;

	protected MapRegion mapB_region;

	int mapB_NumRows;
	int mapB_NumCols;

	protected String[][] toMapB_OutPortsNames;

	protected String[][] toMapA_OutPortsNames;

	public LinkedList<GeoMap> MemoryDataA;
	public LinkedList<GeoMap> MemoryDataB;

	public GeoKIB(String name, GeoMap _mapA, GeoMap _mapB) {
		super(name);
		this.mapA = _mapA;
		this.mapB = _mapB;

		this.mapA_region = _mapA.get_region();
		this.mapB_region = _mapB.get_region();

		mapA_NumRows = this.mapA_region.get_num_rows();
		mapA_NumCols = this.mapA_region.get_num_cols();
		mapB_NumRows = this.mapB_region.get_num_rows();
		mapB_NumCols = this.mapB_region.get_num_cols();

		toMapB_OutPortsNames = new String[mapB_NumCols][mapB_NumRows];
		toMapA_OutPortsNames = new String[mapA_NumCols][mapA_NumRows];

		MemoryDataA = new LinkedList<GeoMap>();
		MemoryDataB = new LinkedList<GeoMap>();

		addInport("inMapA");
		addInport("inMapB");

		for (int i = 0; i < mapB_NumCols; i++) {
			for (int j = 0; j < mapB_NumRows; j++) {
				toMapB_OutPortsNames[i][j] = "outMapB:" + i + "," + j;

				addOutport(toMapB_OutPortsNames[i][j]);

			}
		}

		for (int i = 0; i < mapA_NumCols; i++) {
			for (int j = 0; j < mapA_NumRows; j++) {
				toMapA_OutPortsNames[i][j] = "outMapA:" + i + "," + j;

				addOutport(toMapA_OutPortsNames[i][j]);

			}
		}

	}

	protected GeoMap spatial_conversion(GeoMap source_map, GeoMap dest_map) {
		MapRegion source_region = source_map.get_region();
		MapRegion dest_region = dest_map.get_region();

		if (source_region == null || dest_region == null) {
			throw new IllegalStateException(
					"spatial_conversion was called for a GeoKibFunction in which the source and destination "
							+ "regions were not both set.\n");
		}

		double destNorthBound = dest_region.get_north();
		double destSouthBound = dest_region.get_south();
		double destEastBound = dest_region.get_east();
		double destWestBound = dest_region.get_west();
		double destCellHeight = (destSouthBound - destNorthBound)
				/ dest_region.get_num_rows();
		double destCellWidth = (destEastBound - destWestBound)
				/ dest_region.get_num_cols();

		double sourceNorthBound = source_region.get_north();
		double sourceSouthBound = source_region.get_south();
		double sourceEastBound = source_region.get_east();
		double sourceWestBound = source_region.get_west();
		double sourceCellHeight = (sourceSouthBound - sourceNorthBound)
				/ source_region.get_num_rows();
		double sourceCellWidth = (sourceEastBound - sourceWestBound)
				/ source_region.get_num_cols();

		double cellAreaRatio = (destCellHeight * destCellWidth)
				/ (sourceCellHeight * sourceCellWidth);

		double destCellNorth, destCellSouth, destCellEast, destCellWest;
		double sourceCellNorth, sourceCellSouth, sourceCellEast, sourceCellWest;
		int sourceRow, sourceCol;

		double overlapCellHeight, overlapCellWidth;
		double weightedSum, areaSum;
		double sourceValue;
		boolean isCumulative = source_map.is_cumulative_value();

		String[][] destValues = new String[dest_region
				.get_num_cols()][dest_region.get_num_rows()];
		GeoMap newDestMap;
		String[][] newDestMapValues = new String[dest_region
				.get_num_cols()][dest_region.get_num_rows()];

		for (int destCol = 0; destCol < dest_region.get_num_cols(); destCol++) {
			destCellWest = destWestBound + (destCellWidth * destCol);
			destCellEast = destCellWest + destCellWidth;
			for (int destRow = 0; destRow < dest_region
					.get_num_rows(); destRow++) {
				destCellNorth = destNorthBound + (destCellHeight * destRow);
				destCellSouth = destCellNorth + destCellHeight;

				// For each cell of the destination, compute a weighted average
				// of the values from the corresponding area of the source.
				weightedSum = 0;
				areaSum = 0;

				// Find the coresponding cell of the source map that matches
				// with the northwest corner of
				// the destination cell.
				// Loop through the rows of the source map.
				// Loop through the columns of the source map.

				sourceRow = source_region.get_num_rows() - (int) (Math.ceil(
						(sourceSouthBound - destCellNorth) / sourceCellHeight));
				sourceCellNorth = sourceNorthBound
						+ (sourceRow * sourceCellHeight);
				sourceCellSouth = sourceCellNorth + sourceCellHeight;

				while (destCellSouth > sourceCellNorth
						&& sourceRow < source_region.get_num_rows() && sourceRow >=0) {
					overlapCellHeight = min(destCellSouth, sourceCellSouth)
							- max(destCellNorth, sourceCellNorth);

					sourceCol = (int) ((destCellWest - sourceWestBound)
							/ sourceCellWidth);
					// Find the coordinates of the boundaries of the of the
					// source cell.
					sourceCellWest = sourceWestBound
							+ (sourceCol * sourceCellWidth);
					sourceCellEast = sourceCellWest + sourceCellWidth;

					while (destCellEast > sourceCellWest
							&& sourceCol < source_region.get_num_cols()
							&& sourceCol >= 0) {
						// Find the area of overlap between the destination and
						// source cells.
						sourceValue = Double.parseDouble(
								source_map.get_value(sourceCol, sourceRow));
						overlapCellWidth = min(destCellEast, sourceCellEast)
								- max(destCellWest, sourceCellWest);

						weightedSum += overlapCellHeight * overlapCellWidth
								* sourceValue;
						areaSum += overlapCellHeight * overlapCellWidth;

						sourceCellWest = sourceCellEast;
						sourceCellEast += sourceCellWidth;
						sourceCol++;
					}

					sourceCellNorth = sourceCellSouth;
					sourceCellSouth += sourceCellHeight;
					sourceRow++;
				}
				if (areaSum != 0.0) {
					destValues[destCol][destRow] = "" + weightedSum / areaSum;
				} else {
					destValues[destCol][destRow] = "";
				}

				// If the value is a cumulative value, multiply by the values by
				// the ratio of the cell areas.
				if (isCumulative) {
					destValues[destCol][destRow] = ""
							+ (Double.parseDouble(destValues[destCol][destRow])
									* (cellAreaRatio));
				}

			}
		}
		// Transfer the destination values to the destination map.
		for (int destCol = 0; destCol < dest_region.get_num_cols(); destCol++) {
			for (int destRow = 0; destRow < dest_region
					.get_num_rows(); destRow++) {

				newDestMapValues[destCol][destRow] = destValues[destCol][destRow];
			}
		}
		newDestMap = new GeoMap(dest_region, newDestMapValues);
		return newDestMap;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	protected abstract GeoMap measurement_conversion(List<GeoMap> input_maps);

	// Create a GeoMap object with values obtained by adding the values of all
	// the
	// maps at corresponding locations.
	// All the input objects must use the same MapRegion object. Null values are
	// treated as 0.
	protected GeoMap map_sum(List<GeoMap> input_maps) {
		int numInputs = input_maps.size();
		if (numInputs < 1) {
			System.out.println(
					"Warning: No input maps were provided to the map_sum method of GeoKib.");
			return null;
		}
		GeoMap firstMap = input_maps.get(0);
		MapRegion sumRegion = firstMap.get_region();
		if (sumRegion == null) {
			throw new IllegalArgumentException(
					"An argument of map_sum had a null region.\n");
		}
		int numRows = sumRegion.get_num_rows();
		int numCols = sumRegion.get_num_cols();

		double[][] valueSums = new double[numRows][numCols];

		String[][] stringSums = new String[numRows][numCols];
		double valueHere;

		for (GeoMap inputMap : input_maps) {
			if (inputMap.get_region() != sumRegion) {
				throw new IllegalArgumentException(
						"The arguments of map_sum did not all have the same region object.\n");
			}
		}

		for (int rowNum = 0; rowNum < numRows; rowNum++) {
			for (int colNum = 0; colNum < numCols; colNum++) {

				for (GeoMap inputMap : input_maps) {
					try {
						valueHere = Double.parseDouble(
								inputMap.get_value(rowNum, colNum));
						valueSums[rowNum][colNum] += valueHere;
						stringSums[rowNum][colNum] = ""
								+ valueSums[rowNum][colNum];

					} catch (Exception e) {
						System.out.println(
								"One value in the map is not parsable to double");
						valueSums[rowNum][colNum] = 0.0;
						stringSums[rowNum][colNum] = firstMap.getDefaultValue();
					}
				}

			}
		}

		GeoMap sumResult = new GeoMap(sumRegion, stringSums);
		sumResult.set_is_cumulative_value(firstMap.is_cumulative_value());
		return sumResult;
	}

	public String[][] getMapB_OutPortsNames() {

		return toMapB_OutPortsNames;
	}

	public String[][] getMapA_OutPortsNames() {

		return toMapA_OutPortsNames;
	}

	public GeoMap getMapA() {
		return mapA;
	}

	public GeoMap getMapB() {
		return mapB;
	}

	// public message out()
	// {
	// message m = new message();
	//
	// for (int i = 0; i < destNumCols; i++)
	// {
	// for (int j = 0; j < destNumRows; j++)
	// {
	// content conDest = makeContent(
	// Dest_OutPortsNames[i][j],
	// new entity("" + source_map.get_value(i, j
	// )));
	// m.add(conDest);
	//
	// }
	// }
	//
	// return m;
	// }

}
