package povertyByState;

public class StateDataStructure {
	/**************************************
	* Current structure of the file.      *
	* 1. State Postal Code                *
	* 2. State FIPS Code                  *
	* 3. District ID                      *
	* 4. Name                             *
	* 5. Estimated Total Population       *
	* 6. Estimated Population 5-17        *
	* 7. Estimated Children in Poverty    * 
	**************************************/
	private String StatePostalCode;
	private String FipsCode;
	private String DistrictID;
	private int EstTotalPop;
	private int EstPop5to17;
	private int EstPop5to17Poverty;
	
	//Blank Constructor method
	public StateDataStructure(){
		setStatePostalCode("");
		setFipsCode("");
		setDistrictID("");
		setEstTotalPop(-1);
		setEstPop5to17(-1);
		setEstPop5to17Poverty(-1);
	}
	
	public String getStatePostalCode() {
		return StatePostalCode;
	}//getStatePostalCode
	public void setStatePostalCode(String statePostalCode) {
		StatePostalCode = statePostalCode;
	}//setStatePostalCode
	
	public String getFipsCode() {
		return FipsCode;
	}//getFipsCode
	public void setFipsCode(String fipsCode) {
		FipsCode = fipsCode;
	}//setFipsCode
	
	public String getDistrictID() {
		return DistrictID;
	}//getDistrictID
	public void setDistrictID(String districtID) {
		DistrictID = districtID;
	}//setgetDistrictID

	public int getEstTotalPop() {
		return EstTotalPop;
	}//getEstTotalPop
	public void setEstTotalPop(int estTotalPop) {
		EstTotalPop = estTotalPop;
	}//setEstTotalPop
	
	public int getEstPop5to17() {
		return EstPop5to17;
	}//getEstPop5to17
	public void setEstPop5to17(int estPop5to17) {
		EstPop5to17 = estPop5to17;
	}//getEstPop5to17
	
	public int getEstPop5to17Poverty() {
		return EstPop5to17Poverty;
	}//getEstPop5to17Poverty
	public void setEstPop5to17Poverty(int estPop5to17Poverty) {
		EstPop5to17Poverty = estPop5to17Poverty;
	}//setEstPop5to17Poverty
}//StateDataStructure