package povertyByState;

//This object is built to hold all the summarized information about the states.
public class StatePoverty {

	private String StateName;
	private int TotalChildPopulation;
	private int NumberChildInPoverty;
	private int TotalPopulation;
	private double PercentPopulationInPoverty;
	
	public StatePoverty(String sName) {
		this.setStateName(sName);
	}//StatePoverty

	public StatePoverty() {
		// TODO Auto-generated constructor stub
	}//StatePoverty empty

	public String getStateName() {
		return StateName;
	}//getStateName()

	public void setStateName(String stateName) {
		this.StateName = stateName;
	}//setStateName()	

	public int getTotalChildPopulation() {
		return TotalChildPopulation;
	}//getTotalChildPopulation()

	public void setTotalChildPopulation(int totalChildPopulation) {
		TotalChildPopulation = totalChildPopulation;
	}//setTotalChildPopulation()

	public int getNumberChildInPoverty() {
		return NumberChildInPoverty;
	}//getNumberChildInPoverty()

	public void setNumberChildInPoverty(int numberChildInPoverty) {
		NumberChildInPoverty = numberChildInPoverty;
	}//setNumberChildInPoverty()

	public double getPercentPopulationInPoverty() {
		return PercentPopulationInPoverty;
	}//getPercentPopulationInPoverty()

	public void setPercentPopulationInPoverty(double percentPopulationInPoverty) {
		PercentPopulationInPoverty = percentPopulationInPoverty;
	}//setPercentPopulationInPoverty()

	public int getTotalPopulation() {
		return TotalPopulation;
	}//getTotalPopulation

	public void setTotalPopulation(int totalPopulation) {
		TotalPopulation = totalPopulation;
	}//setTotalPopulation
}//StatePoverty