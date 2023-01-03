import java.io.*;
import java.util.*;
import java.text.*;

public class GPDatabase {
	
	public static ArrayList<Integer> voterVoted = new ArrayList<>();
	public static Hashtable<Integer,Integer> senateVotes = new Hashtable<>();
	public static Hashtable<Integer,Integer> houseVotes = new Hashtable<>();
	public static Hashtable<Integer,Integer> presVicePresVotes = new Hashtable<>();
	
	//This method boots up the entire database with the data stored in the file storage
	//by calling each databases Data Persistence method
	//This method should be ran on startup
	public static void dataPersist() {
		//boots up the voter information
		VoterDatabase.voterPersist();
		//boots up the candidate information
		CandidateDatabase.candidatePersist();
		//Uses the candidate database to start setting up the vote data sets by 
		//assigning the candidate numbers to the correct vote data sets
		for ( int i = 0; i < CandidateDatabase.senateCandidates.size(); i++) {
			senateVotes.put(i+1,0);
		}
		for ( int i = 0; i < CandidateDatabase.houseCandidates.size(); i++) {
			houseVotes.put(i+1,0);
		}
		for ( int i = 0; i < CandidateDatabase.presVicePresCandidates.size(); i++) {
			presVicePresVotes.put(i+1,0);
		}
		//This section reads the Votes file, which stores the votes entered during a previous run.
		File votesFile = new File("Votes.txt");
		Scanner voteScanner=null;
		try {
			voteScanner = new Scanner(votesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//All data in each database is entered in with | separating each section of the entries
		//This allows for the data in the files to be easily split into their own strings which
		// allows for ease of use.
		while (voteScanner.hasNextLine()) {
			String voteData = voteScanner.nextLine();
			String[] dataSplit = voteData.split("\\|");
			//This if statement is really important for if the data is hand added into the file
			//since without out it an accidental double tap of enter at the end of the file will
			// break the entire program
			if(dataSplit[0].length() != 0) {
				int ssnInt = Integer.parseInt(dataSplit[0]);
				int sVoteInt = Integer.parseInt(dataSplit[1]);
				int hVoteInt = Integer.parseInt(dataSplit[2]);
				int pVoteInt = Integer.parseInt(dataSplit[3]);
				voterVoted.add(ssnInt);
				if(senateVotes.containsKey(sVoteInt)) {
					senateVotes.replace(sVoteInt, senateVotes.get(sVoteInt)+1);
				}
				if(houseVotes.containsKey(hVoteInt)) {
					houseVotes.replace(hVoteInt, houseVotes.get(hVoteInt)+1);
				}
				if(presVicePresVotes.containsKey(pVoteInt)) {
					presVicePresVotes.replace(pVoteInt, presVicePresVotes.get(pVoteInt)+1);
				}
			}
			
		}
		voteScanner.close();
	}
	
	//Method for collecting votes
	public static void newVote() {
		Scanner input = new Scanner(System.in);
		//Gets the voters Name and Address and uses it to find their SSN to make sure they both are a valid voter,
		//and that they have not voted previously
		System.out.println("Please enter your First and Last Name: ");
		String voterName = input.nextLine();
		System.out.println("Please enter your Address: ");
		String voterAddress = input.nextLine();
		String voterIdent = voterName + " " + voterAddress;
		
		if (!VoterDatabase.validVoter.containsKey(voterIdent)) {
			System.out.println("This Voter is not registered in the database, please speak to an Administrator " +
					"to get registered.");
		}
		else {
			int voterSSN = VoterDatabase.validVoter.get(voterIdent);
			if(voterVoted.contains(voterSSN)){
				System.out.println("This Voter has already casted their votes.");
			}
			else {
				//Presents the Voter with their Senate Candidates and gets their selection
				for(int i = 0; i < CandidateDatabase.senateCandidates.size(); i++) {
					System.out.println( i+1 + ": " + CandidateDatabase.senateCandidates.get(i+1));
				}
				System.out.println("Please enter the number of the Senate Candidate that you wish to vote for: ");
				int senateVote = input.nextInt();
				//Updates the vote tally for the senate
				senateVotes.replace(senateVote, senateVotes.get(senateVote)+1);
			
				//Presents the Voter with their House Candidates and gets their selection
				for(int i = 0; i < CandidateDatabase.houseCandidates.size(); i++) {
					System.out.println( i+1 + ": " + CandidateDatabase.houseCandidates.get(i+1));
				}
				System.out.println("Please enter the number of the House Candidate that you wish to vote for: ");
				int houseVote = input.nextInt();
				//Updates the vote tally for the house
				senateVotes.replace(houseVote, houseVotes.get(houseVote)+1);
			
				//Presents the Voter with their Presidential Candidates and gets their selection
				for(int i = 0; i < CandidateDatabase.presVicePresCandidates.size(); i++) {
				System.out.println( i+1 + ": " + CandidateDatabase.presVicePresCandidates.get(i+1));
				}
				System.out.println("Please enter the number of the Senate Candidate that you wish to vote for: ");
				int presVote = input.nextInt();
				presVicePresVotes.replace(presVote, presVicePresVotes.get(presVote)+1);
				FileWriter voteWriter = null;
				try {
					voteWriter = new FileWriter("Votes.txt", true);
					voteWriter.write(voterSSN+ "|" + senateVote + "|" + houseVote + "|" + presVote +"\n");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (voteWriter != null) try { voteWriter.close();} catch (IOException ignore) {}
				}
				//Updates the array that keeps track of prior voters
				voterVoted.add(voterSSN);
			}
		}
		input.close();
	}
	//Method that displays the current number of votes each candidate has, as well as their percentage of the total
	//This should be an admin access only method
	public static void getVoteStats() {
		//Allows for formatting the percentage to 3 decimal places
		DecimalFormat decFormat = new DecimalFormat ("#.000");
		double voteCount = voterVoted.size();
		System.out.println("The Senate votes are as follows: ");
		for(int i = 0; i < senateVotes.size(); i++) {
			double votePercent = (senateVotes.get(i+1))/voteCount;
			votePercent *= 100;
			System.out.println(CandidateDatabase.senateCandidates.get(i+1) + " has " + senateVotes.get(i+1) +
					" votes, equaling " + decFormat.format(votePercent) + "% of the total vote.");
		}
		System.out.println("\nThe House votes are as follows: ");
		for(int i = 0; i < houseVotes.size(); i++) {
			double votePercent = (houseVotes.get(i+1))/voteCount;
			votePercent *= 100;
			System.out.println(CandidateDatabase.houseCandidates.get(i+1) + " has " + houseVotes.get(i+1) +
					" votes, equaling " + decFormat.format(votePercent) + "% of the total vote.");
		}
		System.out.println("\nThe Presidential votes are as follows: ");
		for(int i = 0; i < presVicePresVotes.size(); i++) {
			double votePercent = (presVicePresVotes.get(i+1))/voteCount;
			votePercent *= 100;
			System.out.println(CandidateDatabase.presVicePresCandidates.get(i+1) + " has " +
					presVicePresVotes.get(i+1) +  " votes, equaling " + decFormat.format(votePercent) +
					"% of the total vote.");
		}
	}
	
	public static void main(String[] args){
		dataPersist();
		newVote();
		//getVoteStats();
		//System.out.println("Senate Votes: "+ senateVotes.toString() + " House Votes: "+ houseVotes.toString()+
		// " Pres Votes: " + presVicePresVotes.toString());
		//System.out.println(VoterDatabase.validVoter);
	}
}
