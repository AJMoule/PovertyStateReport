package povertyByState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ReportExecution {
	//This will hold a list of all the 50  US States 2 letter abbreviation plus Puerto Rico (PR) and District of Columbia (DC) - Length = 52
	private static final String[] ALL_THE_STATES = {"AL","AK","AZ","AR","CA","CO","CT","DE","DC","FL","GA","HI","ID","IL",
											"IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV",
											"NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX",
											"UT","VT","VA","WA","WV","WI","WY","PR"};
	//ArrayList being used to store the data of each record then can filter off those records.
	private static ArrayList<StateDataStructure> stateDataInput = new ArrayList<StateDataStructure>();
	private static ArrayList<StatePoverty> statePovertyInfo = new ArrayList<StatePoverty>();
	private static Scanner myScanner = new Scanner(System.in);
	private static int finalWhiteSpace = -1;
	
	public static void main(String[] args) {
		//This will get the information from the user and print out the report
		String dataSourceFilePath = null; //Where the user has the data File for the program to process
		String destinationFilePath = null; //Where the program will drop the report for the user to view
		int numberOfRecords = 0; //Number records in data file.

		//Need to get the file paths from the user. Using the console to get this information.
		dataSourceFilePath = GetFilePaths("Source");
		destinationFilePath = GetFilePaths("Destination");
		numberOfRecords = GetRecordCount();
		
		//We know have the source path, destination path and record count can start to process the file
		ReadTheFileInTime(dataSourceFilePath,numberOfRecords);
		TimeToDoTheMath();//Does all the math on the states to figure out the poverty information
		//After the math is complete need to Write a file to the dest Path
		PrintTheFileOut(destinationFilePath);
	}//main
	
	private static void PrintTheFileOut(String tempDest) 
	{
		File destFilePath = new File(tempDest);
		try {
			FileWriter fw = new FileWriter(destFilePath);
			PrintWriter pw = new PrintWriter(fw);
			NumberFormat nf = new DecimalFormat("##.##");
			for(StatePoverty osp : statePovertyInfo) 
			{
				pw.println("State: " + osp.getStateName());
				pw.println("Total Child Pop: " + osp.getTotalChildPopulation());
				pw.println("Number in Poverty: " + osp.getNumberChildInPoverty());
				pw.println("Percent Population: " + nf.format((osp.getPercentPopulationInPoverty() * 100))+"%");
			}//for
			//flush and close stream
			pw.close();
		}//try
		catch(IOException ioe) {System.out.println(ioe);}//catch
	}//PrintTheFileOut
	
	private static void ReadTheFileInTime(String sourcePath, int rowsToRead) {
		File sourceFile = new File(sourcePath);
		//Path dest = Paths.get(destPath);
		
		//check to see if source file is readable if it is then start to process if not then out of luck
		try {
			FileReader fr = new FileReader(sourceFile);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			int count = 0;
			while(((line = br.readLine()) != null))
			{
				if(count <= 2) {count++;}//only need to increment count we are skipping the first 3 rows read in
				else{
					SendLineProcessRecord(line);
				}//else
			}//while
			//Close everything to clear up memory.
			br.close();
			myScanner.close();
		}//try
		catch(IOException ioe) {System.out.println("I'm sorry but that file could not be read.");}//catch
	}//ReadTheFileInTime
	
	private static void TimeToDoTheMath() {
		//Loop through the All states array and create a StatePoverty object with those states after that can have it loop through them all
		for(int i = 0; i < ALL_THE_STATES.length; i++) 
		{
			//use this array to loop through the states and start to sum them all up
			StatePoverty myState = new StatePoverty(ALL_THE_STATES[i]);
			statePovertyInfo.add(myState);
		}//for
		
		//Loop through the statePovertyInfo and find all the stateDataInput that match the states and add up their values
		for(StatePoverty sp : statePovertyInfo) 
		{
			int tempTotalPop = 0;
			int temp5to17Pop = 0;
			int tempPoverty = 0;
			double tempPercent = 0;
			for(StateDataStructure sds : stateDataInput) 
			{
				if(sds.getStatePostalCode().equals(sp.getStateName())) 
				{
					tempTotalPop = tempTotalPop + sds.getEstTotalPop();
					temp5to17Pop = temp5to17Pop + sds.getEstPop5to17();
					tempPoverty = tempPoverty + sds.getEstPop5to17Poverty();
				}//if
			}//foreach
			sp.setTotalPopulation(tempTotalPop);
			sp.setTotalChildPopulation(temp5to17Pop);
			sp.setNumberChildInPoverty(tempPoverty);
			//find the percentage of children in poverty: taking the number of children in poverty divide by the total number of children
			tempPercent = (double)tempPoverty / (double)temp5to17Pop;
			sp.setPercentPopulationInPoverty(tempPercent);
		}//foreach
	}//TimeToDoTheMath
	
	private static void SendLineProcessRecord(String theLine) {
		//create temp StateDataStructure				
		StateDataStructure temp = new StateDataStructure();
		temp.setStatePostalCode(theLine.substring(0,2));//will always be the place where the state is
		temp.setFipsCode(theLine.substring(3,5));//FIPS Code is always here
		temp.setDistrictID(theLine.substring(6,11));//District ID is always here
		//time to grab all the population data and put into the temp object
		temp.setEstPop5to17Poverty(FindThePopulations(theLine,"poverty"));
		temp.setEstPop5to17(FindThePopulations(theLine,"5to17"));
		temp.setEstTotalPop(FindThePopulations(theLine,"total"));				
		
		//After everything is done we add the temp object into stateDataInput
		stateDataInput.add(temp);
	}//SendLineProcessRecord
	
	private static int FindThePopulations(String someLine, String lookingFor)
	{
		String tString = null;
		String reverseString = new StringBuilder(someLine).reverse().toString();
		char[] myChars = reverseString.toCharArray(); //reverses the string to find the pop easier
		int fCommaLocation = -1;
		int sCommaLocation = -1;
		int lastQuote = -1;
		
		//With the reverse string now the the first set of numbers will always be there and are for 5 to 17 in Poverty
		if(lookingFor.equals("poverty"))
		{
			//look to see if it starts with a quote
			if(myChars[0] == '"') 
			{
				//we have atleast 1 comma and need to know where the final quote is
				for(int i = 1; i < 15; i++)//don't need to go higher than 1 billion for a number 
				{
					if(myChars[i] == '"') 
					{
						lastQuote = i;
						//once you find last quote leave the array don't want to be in it any longer
						break;
					}//if
					if(myChars[i] == ',' && fCommaLocation == -1) {fCommaLocation = i;}
					else if(myChars[i] == ',' && fCommaLocation > -1) {sCommaLocation = i;}//elseif
				}//for
				//build the string of numbers back together
				if(sCommaLocation > -1)
				{
					tString = reverseString.substring(1,fCommaLocation);
					tString += reverseString.substring((fCommaLocation+1),sCommaLocation);
					tString += reverseString.substring((sCommaLocation+1),lastQuote);
				}//if
				else 
				{
					tString = reverseString.substring(1,fCommaLocation);
					tString += reverseString.substring((fCommaLocation+1),lastQuote);
				}//else
				tString = new StringBuilder(tString).reverse().toString();
			}//if
			else //this means that there was not a quote at 0 location so good to just grab the next 3 substring cause they numbers.
			{
				int whiteSpace = -1;
				//find the whitespace character
				for(int i = 1; i < 5; i++) {
					if(Character.isWhitespace(myChars[i])) 
					{
						whiteSpace = i;
						break;
					}//if
				}//for
				if(whiteSpace > -1) 
				{
					tString = reverseString.substring(0,whiteSpace);
				}//if
				tString = new StringBuilder(tString).reverse().toString();
			}//else
		}//if
		else if(lookingFor.equals("5to17")) 
		{
			int whiteSpace = -1;
			//find the whitespace character this is where the next number will start
			for(int i = 0; i < myChars.length; i++) {
				if(Character.isWhitespace(myChars[i])) 
				{
					whiteSpace = i;
					break;
				}//if
			}//for
			//found the next substring beginning one over from whitespace
			int index = whiteSpace + 1;
			if(myChars[index]=='"') 
			{
				for(int i = (index+1); i < (index+15); i++)//don't need to go higher than 1 billion for a number 
				{
					if(myChars[i] == '"') 
					{
						lastQuote = i;
						finalWhiteSpace = lastQuote + 1; //setting final whitespace to figure out where to go into for total pop
						//need this to be atleast where the final quote was +1 due to it moving into a whitespace char space
						//once you find last quote leave the array don't want to be in it any longer
						break;
					}//if
					if(myChars[i] == ',' && fCommaLocation == -1) {fCommaLocation = i;}
					else if(myChars[i] == ',' && fCommaLocation > -1) {sCommaLocation = i;}//elseif
				}//for
				//build the string of numbers back together
				if(sCommaLocation > -1)
				{
					tString = reverseString.substring((index+1),fCommaLocation);
					tString += reverseString.substring((fCommaLocation+1),sCommaLocation);
					tString += reverseString.substring((sCommaLocation+1),lastQuote);
				}//if
				else 
				{
					tString = reverseString.substring((index+1),fCommaLocation);
					tString += reverseString.substring((fCommaLocation+1),lastQuote);
				}//else
				tString = new StringBuilder(tString).reverse().toString();
			}//if
			else //this means that there was not a quote at 0 location so good to just grab the next 2-3 substring cause they numbers.
			{
				//find the whitespace character
				for(int i = index; i < (index+5); i++) {
					if(Character.isWhitespace(myChars[i])) 
					{
						whiteSpace = i; 
						finalWhiteSpace = whiteSpace;
						break;
					}//if
				}//for
				if(whiteSpace > -1) 
				{
					tString = reverseString.substring(index,whiteSpace);
				}//if
				tString = new StringBuilder(tString).reverse().toString();
			}//else
		}//elseif
		else if(lookingFor.equals("total")) 
		{
			//this is to grab the total population
			int index = finalWhiteSpace+1; //setting where we need to start off with looking for quotes and numbers
			int whiteSpace = -1;
			if(myChars[index]=='"') 
			{
				for(int i = (index+1); i < (index+15); i++)//don't need to go higher than 1 billion for a number 
				{
					if(myChars[i] == '"') 
					{
						lastQuote = i;
						break;
					}//if
					if(myChars[i] == ',' && fCommaLocation == -1) {fCommaLocation = i;}
					else if(myChars[i] == ',' && fCommaLocation > -1) {sCommaLocation = i;}//elseif
				}//for
				//build the string of numbers back together
				if(sCommaLocation > -1)
				{
					tString = reverseString.substring((index+1),fCommaLocation);
					tString += reverseString.substring((fCommaLocation+1),sCommaLocation);
					tString += reverseString.substring((sCommaLocation+1),lastQuote);
				}//if
				else 
				{
					tString = reverseString.substring((index+1),fCommaLocation);
					tString += reverseString.substring((fCommaLocation+1),lastQuote);
				}//else
				tString = new StringBuilder(tString).reverse().toString();
			}//if
			else //this means that there was not a quote at 0 location so good to just grab the next 2-3 substring cause they numbers.
			{
				//find the whitespace character
				for(int i = index; i < (index+5); i++) {
					if(Character.isWhitespace(myChars[i])) 
					{
						whiteSpace = i; 
						break;
					}//if
				}//for
				if(whiteSpace > -1) 
				{
					tString = reverseString.substring(index,whiteSpace);
				}//if
				tString = new StringBuilder(tString).reverse().toString();
			}//else
		}//elseif
		int returnValue = Integer.parseInt(tString);
		return returnValue;
	}//FindThePopulations
	
	private static String GetFilePaths(String sPath) {
		String temp = null;
		myScanner = new Scanner(System.in);
		System.out.println("What is the " + sPath + " Path for the data file?");
		temp = myScanner.next();		
		return temp;
	}//GetFilePaths
	
	private static int GetRecordCount() {
		boolean getTheInt = false;
		int someNumber = 0;
		myScanner = new Scanner(System.in);
		while(!getTheInt)
		{
			try {
				System.out.println("How many records are in the file?");
				someNumber = myScanner.nextInt();
				if(someNumber > 0)
				{
					getTheInt = true;
				}//if
				else {
					System.out.println("Please provide an non-decimal positive number!");
					getTheInt = false;
					someNumber = 0;
					myScanner = new Scanner(System.in);
				}//else
			}//try
			catch(InputMismatchException e) {
				System.out.println("Please provide an non-decimal positive number!");
				getTheInt = false;
				someNumber = 0;
				myScanner = new Scanner(System.in);
			}//catch
		}//while
		return someNumber;
	}//GetRecordCount
}//ReportExecution