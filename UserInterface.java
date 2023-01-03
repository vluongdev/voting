import java.io.*;
import java.util.Scanner;

public class UserInterface {
    public static void main(String[] args) {
        GPDatabase.dataPersist();
        CandidateDatabase.candidatePersist();
        VoterDatabase.voterPersist();

        // Declare admin, Scanner, and option variable
        String username, password;
        username = "admin";
        password = "password";
        Scanner input = new Scanner(System.in);
        int option;

        // User selects what part of program to access
        System.out.println("Welcome! Enter '1' for admin options, '2' for public options, or '3' to exit: ");
        option = input.nextInt();

        // Access admin options
        while(option == 1) {
            Scanner username1 = new Scanner(System.in);
            System.out.print("Enter username: ");
            String usernameEntry = username1.next().toLowerCase();
            Scanner password1 = new Scanner(System.in);
            System.out.print("Enter password: ");
            String passwordEntry = password1.next().toLowerCase();

            //check to see if username or password is valid
            if (usernameEntry.equals(username) && passwordEntry.equals(password)) {
                System.out.println("Login Authorized! Welcome Admin!");
                System.out.print("Type '1' to display a list of candidates, '2' to add a candidate, " +
                        "'3' to display list of voters, '4' to add a voter, '5' to view voting results: ");
                Scanner adminOption = new Scanner(System.in);
                int optionAdmin = adminOption.nextInt();

                // Display Candidate list
                if (optionAdmin == 1) {
                    // Display Senate Candidates
                    System.out.println("\nSenate Candidates:");
                    for(int i = 0; i < CandidateDatabase.senateCandidates.size(); i++) {
                        System.out.println( i+1 + ": " + CandidateDatabase.senateCandidates.get(i+1));
                    }

                    // Display House Candidates
                    System.out.println("\nHouse Candidates:");
                    for(int i = 0; i < CandidateDatabase.houseCandidates.size(); i++) {
                        System.out.println( i+1 + ": " + CandidateDatabase.houseCandidates.get(i+1));
                    }

                    // Display President/VP Candidates
                    System.out.println("\nPresident / Vice President Candidates:");
                    for(int i = 0; i < CandidateDatabase.presVicePresCandidates.size(); i++) {
                        System.out.println( i+1 + ": " + CandidateDatabase.presVicePresCandidates.get(i+1));
                    }
                }

                // Add a candidate
                else if (optionAdmin == 2) {
                    CandidateDatabase.addCandidate();
                }

                // Display voter list
                else if (optionAdmin == 3) {
                    File voters = new File("VoterID.txt");
                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(voters);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    while (scanner.hasNextLine())
                        System.out.println(scanner.nextLine());
                }

                // Add a voter
                else if (optionAdmin == 4) {
                    VoterDatabase.addVoter();
                }

                // View voting results
                else if (optionAdmin == 5) {
                    GPDatabase.getVoteStats();
                }
            }
            else {
                System.out.print("Incorrect username or password.");
            }

            // Enable continued interaction with program for admin
            System.out.println("\nWhat next?\nEnter '1' for admin options, '2' for public options, or '3' to exit: ");
            option = input.nextInt();
        }

        //Give options for user to cast a vote, view results, or exit
        if (option == 2) {
            System.out.println("\nWhat would you like to do voter?\nEnter '1' to vote, '2' to see results," +
                    " or '3' to exit: ");
            Scanner voterOption = new Scanner(System.in);
            int optionVoter = voterOption.nextInt();

            if (optionVoter == 1) {
                GPDatabase.newVote();
                System.out.println("Thank you for voting! Exiting System.");
            }

            else if (optionVoter == 2) {
                GPDatabase.getVoteStats();
            }

            else if (optionVoter == 3) {
                option = 3;
            }
        }

        //only for exiting the program
        if (option == 3) {
            System.out.println("Exiting System. Thank you! Have a good day.");
        }
    }
}