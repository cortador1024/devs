package model.modeling.GeoKIB;

public class Length {

	private double lengthNumber;
	private LengthUnit lengthUnit;

	public Length(double ln, LengthUnit lu) {
		lengthNumber = ln;
		lengthUnit = lu;

	}

	public Length unifyToM(Length l) {
		if (l.getLengthUnit() == LengthUnit.KILOMETER) {
			return new Length(lengthNumber * 1000, LengthUnit.METER);
		} else if (l.getLengthUnit() == LengthUnit.CENTIMETER) {
			return new Length(lengthNumber * 0.01, LengthUnit.METER);
		} else if (l.getLengthUnit() == LengthUnit.MILLIMETER) {
			return new Length(lengthNumber * 0.001, LengthUnit.METER);
		} else if (l.getLengthUnit() == LengthUnit.MICROMETER) {
			return new Length(lengthNumber * 10E-6, LengthUnit.METER);
		} else if (l.getLengthUnit() == LengthUnit.NANOMETER) {
			return new Length(lengthNumber * 10E-9, LengthUnit.METER);
		} else if (l.getLengthUnit() == LengthUnit.METER) {
			return this;
		} else
			throw new IllegalStateException(
					"The length unit is not defined.\n");
	}

	public Length unifyToM() {
		return unifyToM(this);
	}

	public Length unifyTo(LengthUnit lu) {

		return unifyTo(this, lu);

	}

	public Length unifyTo(Length l, LengthUnit lu) {
		Length tempL = l.unifyToM(l);
		if (lu == LengthUnit.KILOMETER) {
			return new Length(tempL.getLengthNumber() * 0.001,
					LengthUnit.KILOMETER);
		} else if (lu == LengthUnit.CENTIMETER) {
			return new Length(tempL.getLengthNumber() * 100,
					LengthUnit.CENTIMETER);
		} else if (lu == LengthUnit.MILLIMETER) {
			return new Length(tempL.getLengthNumber() * 1000,
					LengthUnit.MILLIMETER);
		} else if (lu == LengthUnit.MICROMETER) {
			return new Length(tempL.getLengthNumber() * 10E6,
					LengthUnit.MICROMETER);
		} else if (lu == LengthUnit.NANOMETER) {
			return new Length(tempL.getLengthNumber() * 10E9,
					LengthUnit.NANOMETER);
		} else if (lu == LengthUnit.METER) {
			return tempL;
		} else
			throw new IllegalStateException(
					"The length unit is not defined.\n");
	}

	public double getLengthNumber() {
		return lengthNumber;
	}

	public void setLengthNumber(double lengthNumber) {
		this.lengthNumber = lengthNumber;
	}

	public LengthUnit getLengthUnit() {
		return lengthUnit;
	}

	public void setLengthUnit(LengthUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

}
