import java.io.*;
import java.util.*;
public class VoterDatabase {
	public static ArrayList<String> addressList = new ArrayList<>();
	public static Hashtable<String,Integer> validVoter = new Hashtable<>();
	static Scanner input = new Scanner(System.in);
	private static String invalid = "invalid";
	
	//Method for getting the voter SSN and validating it
	//only meant to be called by addVoter()
	private static String generateSSN() {
		
		Random rand = new Random();
		int upperbound = 100000000;
		int int_random = rand .nextInt(upperbound);
		System.out.println(int_random);
		String ssnString = String.format("%09d",int_random);
		return ssnString;
	}
	
	//Method for getting and validating the voters Date of Birth
	//only meant to be called by addVoter()
	private static String enterDOB() {
		
		//Initialized outside so that they aren't wiped outside of the loop
		boolean validMNTH, validDay, validYear;
		String bMonth = null, bDay = null, bYear = null;
		int monthInt = 0, dayInt= 0, yearInt= 0;
		
		//While loop allows for mistake corrections without having to restart the entire process
		validMNTH = false;
		while (validMNTH != true) {
			System.out.println("Please enter the Voter's Birth Month in the form mm: ");
			 bMonth = input.next();
			 monthInt = Integer.parseInt(bMonth);
			 
			if (monthInt > 12 || monthInt < 0) {
				System.out.println("Invalid Integer entered for Month");
				validMNTH = false;
			}
			else if (monthInt < 12 || monthInt > 0) {
				validMNTH = true;
			}
		}
		
		//While loop is for the same function as the Month Version
		validDay = false;
		while (validDay == false) {
			System.out.println("Please enter the Voter's Birth Day in the form dd: ");
			bDay = input.next();
			dayInt = Integer.parseInt(bDay);
			
			//Validates that the day does actually exist inside of the month previously entered. Didn't deal with leap year, I will if yall would like me too
			if (monthInt == 1 || monthInt == 3|| monthInt == 5|| monthInt == 7|| monthInt == 8|| monthInt == 10|| monthInt == 12) {
				if(dayInt>31) {
					System.out.println("This day is invalid for the previously set Month");
				}
				else validDay = true;
			}
			else if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) {
				if (dayInt>30) {
					System.out.println("This day is invalid for the previously set Month");
				}
				else validDay = true;
			}
			else if (monthInt == 2) {
				if (dayInt>29) {
					System.out.println("This day is invalid for the previously set Month");
				}
				else validDay = true;
			}
			
		}
		//Same as Month and Day
		validYear = false;
		while (validYear == false) {
			System.out.println("Please enter the Voter's Birth Year in the form yyyy: ");
			bYear = input.next();
			yearInt = Integer.parseInt(bYear);
			{
				if(yearInt > 2003) {
					System.out.println("The input year deems the voter to young to vote");
					validYear = false;
				}
				else if(yearInt < 1906) {
					System.out.println("The oldest living American was born in 1906, so this year is invalid");
					validYear = false;
				}
				else validYear = true;
			}
		}
		String DOB = bMonth+ "/"+bDay+"/"+bYear;
		return DOB;
	}
	//Allows for the voters address to be entered
	//only meant to be called by addVoter()
	private static String enterAddress() {
		System.out.println("Please enter the Voter's Address: ");
		String voterAddress = input.nextLine();
		if (addressList.contains(voterAddress)) {
			System.out.println("This Address is already registered to a voter in the database. Please enter a different address: ");
			return enterAddress();
		}
		
		else {
			addressList.add(voterAddress);
			return voterAddress;
		}
	}
	
	//Method to add a new Voter to the database file
	//THis should be an Admin Access only method
	public static void addVoter() {
		String voterSSN = generateSSN();
		if( voterSSN.equals(invalid)) {
			return;	
		}
		System.out.println("Please enter the Voter's First and Last Name: ");
		String voterName = input.nextLine();
		String voterDOB = enterDOB();
		input.nextLine();
		String voterAddress = enterAddress();
		//This section writes the Voters information into the file with the correct format
		FileWriter voterWriter = null;
		try {
			voterWriter = new FileWriter("VoterID.txt", true);
			voterWriter.write(voterSSN + "|" + voterName + "|" + voterDOB + "|" + voterAddress + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (voterWriter != null) try { voterWriter.close();} catch (IOException ignore) {}
		}
		int voterSSNInt = Integer.parseInt(voterSSN);
		validVoter.put(voterName + " " + voterAddress, voterSSNInt);
		//Will allow for multiple entries with one User Interface call
		System.out.println("Would you like to register another a voter? y or n");
		String confirm = input.next();
		if(Character.compare(confirm.charAt(0),'y') == 0 || Character.compare(confirm.charAt(0),'Y' ) == 0){
			addVoter();
		}
		
	}
	//Reads the Voter Information File and updates the data sets in the program with the past data
	public static void voterPersist(){
		File voterFile = new File("VoterID.txt");
		Scanner voterData = null;
		try {
			voterData = new Scanner(voterFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//This stores the SSNs already in the file to the ArrayList
		while (voterData.hasNextLine()) {
			String voterInfo = voterData.nextLine();
			String[] infoSplit = voterInfo.split("\\|");
			//This if statement is really important for if the data is hand added into the file
			//since without out it an accidental double tap of enter at the end of the file will break the entire program
			if(infoSplit[0].length() != 0) {
				String ssnVoter = infoSplit[0];
				String voterNameAddress = infoSplit[1] + " " + infoSplit[3];
				int ssnInt = Integer.parseInt(ssnVoter);
				validVoter.put(voterNameAddress, ssnInt);
			}
		}
		voterData.close();
	}
	
	
	//This is mainly for testing purposes but the ArrayList update will come in handy for the Final Program
	public static void main(String[] args){
		voterPersist();
		addVoter();
		
		input.close();
	}
}
