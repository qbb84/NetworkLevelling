package ranked.network.NetworkLevelling.Boosters;

public enum BoosterType {

	MINOR("Minor", 60, 0.1),
	HALF("Half", 60, 0.5),
	DOUBLE("Double", 60, 1.0),
	TRIPLE("Triple", 30, 2.0);

	private final long timeInMinutes;
	private final double boostIncreasePercentage;
	private final String boosterTypeName;

	BoosterType(String boosterTypeName, long timeInMinutes, double boostIncreasePercentage){
		this.boosterTypeName = boosterTypeName;
		this.boostIncreasePercentage = boostIncreasePercentage;
		this.timeInMinutes = timeInMinutes;
	}

	public long getBoosterTime() {
		return timeInMinutes;
	}



	public String getBoosterTypeName() {
		return boosterTypeName;
	}


	public double getBoostIncreasePercentage() {
		return boostIncreasePercentage;
	}



	@Override
	public String toString() {
		return "BoosterType{" +
				"timeInMinutes=" + timeInMinutes +
				", boostIncreasePercentage=" + boostIncreasePercentage +
				", boosterTypeName='" + boosterTypeName + '\'' +
				'}';
	}
}
