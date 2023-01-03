import java.io.*;
import java.util.*;
public class CandidateDatabase {
	public static Hashtable<Integer,String> senateCandidates = new Hashtable<>();
	public static Hashtable<Integer,String> houseCandidates = new Hashtable<>();
	public static Hashtable<Integer,String> presVicePresCandidates = new Hashtable<>();
	static Scanner input = new Scanner(System.in);
	
	//Method for adding new Candidates. This should be an admin access only method
	public static void addCandidate()  {
		System.out.println("Please enter what position the candidate(s) is running for:(Senate = S, House = H, " +
				"President/VicePresident = P");
		String position = input.nextLine();
		position = position.toUpperCase();
		System.out.println("Please enter the Candidate(s) First and Last Name(if Pres/VP enter " +
				"as PresName/VPresName: ");
		String candidateName = input.nextLine();
		//This catch is to add a constraint to the entry and ensure it is only one character
		if (position.length()>1) {
			System.out.println("Please either S, H, or P");
			addCandidate();
		}
		//These else ifs will organize the candidate into the correct position for what they are running for
		else if(Character.compare(position.charAt(0),'S') == 0 ) {
			FileWriter candidateWriter = null;
			try {
				candidateWriter = new FileWriter("CandidateData.txt", true);
				candidateWriter.write((senateCandidates.size()+1)+ "|" + position + "|" + candidateName + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (candidateWriter != null) try { candidateWriter.close();} catch (IOException ignore) {}
			}
			senateCandidates.put(senateCandidates.size()+1, candidateName);
		}
		else if(Character.compare(position.charAt(0),'H') == 0 ) {
			FileWriter candidateWriter = null;
			try {
				candidateWriter = new FileWriter("CandidateData.txt", true);
				candidateWriter.write((houseCandidates.size()+1)+ "|" + position + "|" + candidateName + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (candidateWriter != null) try { candidateWriter.close();} catch (IOException ignore) {}
			}
			houseCandidates.put(houseCandidates.size()+1, candidateName);
		}
		else if(Character.compare(position.charAt(0),'P') == 0 ) {
			FileWriter candidateWriter = null;
			try {
				candidateWriter = new FileWriter("CandidateData.txt", true);
				candidateWriter.write((presVicePresCandidates.size()+1)+ "|" + position + "|" +
						candidateName + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (candidateWriter != null) try { candidateWriter.close();} catch (IOException ignore) {}
			}
			presVicePresCandidates.put(presVicePresCandidates.size()+1, candidateName);
		}
		else {
			System.out.println("Please enter S, H, or P: ");
			
		}
		System.out.println("Would you like to register another a candidate? y or n");
		String confirm = input.nextLine();
		if(Character.compare(confirm.charAt(0),'y') == 0 || Character.compare(confirm.charAt(0),'Y' ) == 0){
			addCandidate();
		}

	}
	//Reads the Candidate Data File and sets up the hashtables needed for processing candidates and votes
	public static void candidatePersist() {
		File candidateFile = new File("CandidateData.txt");
		Scanner candidateData = null;
		try {
			candidateData = new Scanner(candidateFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (candidateData.hasNextLine()) {
			String candidateInfo = candidateData.nextLine();
			String[] infoSplit = candidateInfo.split("\\|");
			//This if statement is really important for if the data is hand added into the file
			//since without out it an accidental double tap of enter at the end of the file will
			// break the entire program
			if(infoSplit[0].length() != 0) {
				String candNumString = infoSplit[0];
				int candNum = Integer.parseInt(candNumString);
				if (Character.compare(infoSplit[1].charAt(0), 'S') == 0) {
					senateCandidates.put(candNum, infoSplit[2]);
				}
				else if (Character.compare(infoSplit[1].charAt(0), 'H') == 0) {
					houseCandidates.put(candNum, infoSplit[2]);
				}
				else if (Character.compare(infoSplit[1].charAt(0), 'P') == 0) {
					presVicePresCandidates.put(candNum, infoSplit[2]);
				}
			}
				
		}
		candidateData.close();
	}
	
	public static void main(String[] args) {
		candidatePersist();
		addCandidate();
	}

}
