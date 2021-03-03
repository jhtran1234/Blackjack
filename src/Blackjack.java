import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
public class Blackjack {
	private static ArrayList<Player> players = new ArrayList<>();
	private static Scanner scan = new Scanner(System.in);
	private static Player AI = new Player("AI");
	
	public static void main(String[] args) {
		boolean cardCount = false;
		int money = 100;
		
		System.out.println("Welcome to the casino. This is the Blackjack table. Please play responsibly.");
		System.out.println("BlackJack pays 3 to 2.");
		System.out.println("Dealer stands on soft 17.");
		System.out.println();
		System.out.println("Would you like to customize your gameplay?");
		char customize = scan.next().toLowerCase().charAt(0);
		if(customize == 'y') {
			System.out.println("Would you like to enable card counting assistance?");
			customize = scan.next().toLowerCase().charAt(0);
			if(customize == 'y') {
				cardCount = true;
			}
			System.out.println("How much money should each player start with? (Default: 100)");
			money = scan.nextInt();
			System.out.println("How much decks should be in the shoe? (Default: 6)");
			Deck.setNumDecks(scan.nextInt());
		}
		System.out.println("How many players will be playing today? Limit: 7 players.");
		int numPlayers = scan.nextInt();
		while(numPlayers < 1 || numPlayers > 7) {
			System.out.println("Invalid number of players. Limit: 7 players.");
			numPlayers = scan.nextInt();
		}
		
		for(int i = 1; i <= numPlayers; i ++) {
			players.add(new Player(money));
		}
		
		for(int i = 1; i <= players.size(); i ++) {
			System.out.println("Player " + i + " please enter your name:");
			players.get(i - 1).addName(scan.next()); //might need buffer clear
		}
		
		char play = 'y';
		Deck.shuffle();
		while(players.size() > 0 && play == 'y') {
			if(Deck.remainingShoe()) {
				System.out.println("Dealer is shuffling shoe.");
			}
			
			for(Player p : players) {
				p.clear();
			}
			AI.clear();

			if(cardCount) {
				System.out.println("High: " + Deck.high);
				System.out.println("Low: " + Deck.low);
				System.out.println("Count: " + (Deck.low - Deck.high));
				double trueCount = ((double) (Deck.low - Deck.high) * 52) / ((double) (Deck.numDecks * 52 - Deck.low + Deck.neutral + Deck.high));
				System.out.println("True Count: " + String.format("%.1f", trueCount));
				System.out.println();
			}
			
			AI.newCard();
			AI.newCard();
			
			for(Player p : players) {
				p.bet();
				p.newCard();
				p.newCard();
			}

			if(AI.handTotal != 21) {
				for(Player p : players) {
					p.play(AI);
				}
			}
			
			System.out.println();
			System.out.print("Dealer reveals cards: ");
			AI.displayCards();
			System.out.println("Dealer card total: " + AI.handTotal);
			
			if(AI.handTotal == 21) {
				for(Player p : players) {
					System.out.print(p.name + ": Your cards: ");
					p.displayCards();
				}
				
				System.out.println("Dealer has a BlackJack!");
				AI.blackJack = true;
			}
			
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
			System.out.println();
			
			for(Player p : players) {
				p.calculateOutcome(AI);
			}
			System.out.println();
			
			System.out.println("Game Summary:");
			for(Player p : players) {
				System.out.println(p.name + ": " + p.summary);
			}
			System.out.println();
			
			for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
				Player p = iterator.next();
			    if(p.money <= 0) {
					System.out.println(p.name + " is broke and has to leave the casino.");
					p.summary = "Inactive";
			        iterator.remove();
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
			System.out.println(p.name + " is leaving with $" + p.money + ".");
		}
	}
}