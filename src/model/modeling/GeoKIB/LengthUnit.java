package model.modeling.GeoKIB;

public final class LengthUnit {
	private String value;

	public LengthUnit(String value) {
		this.value = value;

	}

	public String getValue() {

		return value;
	}

	public static final LengthUnit METER = new LengthUnit("m");;// 1 m
	public static final LengthUnit CENTIMETER = new LengthUnit("cm");// 10^-2 m
	public static final LengthUnit MILLIMETER = new LengthUnit("mm");// 10^-3 m
	public static final LengthUnit MICROMETER = new LengthUnit("µm");// 10^-6 m
	public static final LengthUnit NANOMETER = new LengthUnit("nm"); // 10^-9 m

	public static final LengthUnit KILOMETER = new LengthUnit("km"); // 10^3 m

}
