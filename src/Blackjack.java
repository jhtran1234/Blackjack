import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
public class Blackjack
{
	private static ArrayList<Player> players = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	
	private static Player p1 = new Player();
	private static Player p2 = new Player();
	private static Player p3 = new Player();
	private static Player p4 = new Player();
	private static Player p5 = new Player();
	private static Player p6 = new Player();
	private static Player AI = new Player("AI");
	
	public static void main(String[] args) {
		System.out.println("Welcome to the casino. This is the Blackjack table. Please play responsibly.");
		System.out.println();
		System.out.println("How many players will be playing today? Limit: 6 players.");
		int numPlayers = scan.nextInt();
		while(numPlayers < 1 || numPlayers > 6) {
			System.out.println("Invalid number of players. Limit: 6 players.");
			numPlayers = scan.nextInt();
		}
		
		if(numPlayers >= 1) {
			players.add(p1);
		}
		if(numPlayers >= 2) {
			players.add(p2);
		}
		if(numPlayers >= 3) {
			players.add(p3);
		}
		if(numPlayers >= 4) {
			players.add(p4);
		}
		if(numPlayers >= 5) {
			players.add(p5);
		}
		if(numPlayers == 6) {
			players.add(p6);
		}
		
		for(int i = 1; i <= players.size(); i ++) {
			System.out.println("Player " + i + " please enter your name:");
			players.get(i - 1).addName(scan.nextLine()); //might need buffer clear
		}
		
		char play = 'y';
		while(players.size() > 0 && play == 'y') {
			for(Player p : players) {
				p.clear();
			}
			
			AI.clear();
			AI.newCard();
			AI.newCard();

			for(Player p : players) {
				p.bet();
				p.newCard();
				p.newCard();
			}

			for(Player p : players) {
				p.hit(AI);
			}

			System.out.println();
			System.out.print("Dealer reveals cards: ");
			AI.displayCards();
			System.out.println("Dealer card total: " + AI.handTotal);
			while(AI.handTotal < 17) {
				System.out.println();
				System.out.println("Dealer hits.");
				AI.newCard();
				System.out.print("Dealer cards: ");
				AI.displayCards();
				System.out.println("Dealer card total: " + AI.handTotal);
			}
			if(AI.handTotal > 21) {
				System.out.println("Dealer BUST, remaining players win!");
			}
			
			for(Player p : players) {
				p.calculateOutcome(AI);
			}
			
			System.out.println();
			System.out.println("Game Summary:");
			
			for(Player p : players) {
				System.out.println(p.name + ": " + p.summary);
			}
			System.out.println();
			
			for(Player p : players) {
				if(p.money <= 0){
					System.out.println(p.name + " is broke and has to leave the casino.");
					p.summary = "Inactive";
					players.remove(p);
				}
			}
			System.out.println();
			
			if(players.size() > 0) {
				System.out.println("Play again?");
				play = scan.next().toLowerCase().charAt(0);
			}
			else {
				System.out.println("Ya'll broke.");
			}
		}
		for(Player p : players) {
			/*Display final earnings*/
		}
	}
}