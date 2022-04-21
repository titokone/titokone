package fi.helsinki.cs.titokone;

/**
 * Base/radix conversion.
 */
public enum ValueBase {
	BIN (2, "Bin", "", "b"),
	DEC (10, "Dec", "", ""),
	HEX (16, "Hex", "0x", "");

	private final int base;
	private final String name;
	private final String prefix;
	private final String suffix;

	/**
	 * Base conversion constructor.
	 * @param base as a radix.
	 * @param name human readable name for this base.
	 * @param prefix human readable prefix.
	 * @param suffix human readable suffix.
	 */
	ValueBase(int base, String name, String prefix, String suffix) {
		this.base = base;
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public int getBase() { return base; }
	public String getName() { return name; }

	public String toString(int value) {
		if (this == ValueBase.HEX || this == ValueBase.BIN)
			return prefix + Long.toString(value & 0xFFFFFFFFL, base) + suffix;
		return prefix + Integer.toString(value, base) + suffix;
	}

	/**
	 * Get a ValueBase enum value from integer base.
	 * @param base as an integer value.
	 * @return value of corresponding ValueBase enumeration.
	 */
	public static ValueBase getBase(int base) {
		switch (base) {
		case 2:
			return ValueBase.BIN;
		case 10:
			return ValueBase.DEC;
		case 16:
			return ValueBase.HEX;
		default:
			return ValueBase.DEC;
		}
	}
}
