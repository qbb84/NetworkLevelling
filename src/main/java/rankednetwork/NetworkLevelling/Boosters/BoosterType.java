package rankednetwork.NetworkLevelling.Boosters;

/**
 * BoosterType represents different types of boosters each having a unique name,
 * duration and boost increase percentage.
 */
public enum BoosterType {

	MINOR("Minor", 60, 1.1),
	HALF("Half", 60, 1.5),
	DOUBLE("Double", 60, 2.0),
	TRIPLE("Triple", 30, 3.0),
	CUSTOM("Custom", 0, 0.0);

	private final long timeInMinutes;
	private final double boostIncreasePercentage;
	private final String boosterTypeName;

	/**
	 * Constructor for BoosterType
	 *
	 * @param boosterTypeName         name of the booster type
	 * @param timeInMinutes           duration of the booster in minutes
	 * @param boostIncreasePercentage boost increase percentage
	 */
	BoosterType(String boosterTypeName, long timeInMinutes, double boostIncreasePercentage) {
		this.boosterTypeName = boosterTypeName;
		this.boostIncreasePercentage = boostIncreasePercentage;
		this.timeInMinutes = timeInMinutes;
	}

	/**
	 * Returns the duration of the booster
	 *
	 * @return duration in minutes
	 */
	public long getBoosterTime() {
		return timeInMinutes;
	}

	/**
	 * Returns the name of the booster type
	 *
	 * @return booster type name
	 */
	public String getBoosterTypeName() {
		return boosterTypeName;
	}

	/**
	 * Returns the boost increase percentage
	 *
	 * @return boost increase percentage
	 */
	public double getBoostIncreasePercentage() {
		return boostIncreasePercentage;
	}

	/**
	 * Provides a string representation of the BoosterType
	 *
	 * @return a string containing the booster type name, duration and boost increase percentage
	 */
	@Override
	public String toString() {
		return "BoosterType{" +
				"timeInMinutes=" + timeInMinutes +
				", boostIncreasePercentage=" + boostIncreasePercentage +
				", boosterTypeName='" + boosterTypeName + '\'' +
				'}';
	}
}
