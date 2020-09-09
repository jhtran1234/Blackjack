import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
public class Blackjack
{
	private static final String[] cards = {"2","2","2","2","3","3","3","3","4","4","4","4","5","5","5","5","6","6","6","6","7","7","7","7","8","8","8","8","9","9","9","9","10","10","10","10","J","J","J","J","Q","Q","Q","Q","K","K","K","K","A","A","A","A"};
	private static ArrayList<Integer> used = new ArrayList<Integer>();
	private static Scanner scan = new Scanner(System.in);
	private static Random rand = new Random();

	private int money, bet, handTotal;
	private String summary;
	private ArrayList<String> hand = new ArrayList<String>();

	public Blackjack(String in) // For the House
	{
		money = 1000000; //House has one M
		bet = 999999;
		handTotal = 0;
		summary = "empty";
	}

	public Blackjack()
	{
		money = 100;
		bet = 0;
		handTotal = 0;
		summary = "clear";
	}

	private void clear()
	{
		bet = 0;
		handTotal = 0;
		hand.clear();
		used.clear();
	}

	private void newCard() //WE NEED A NEW WAY OF SHUFFLING
	{
		int newCard = rand.nextInt(52), check = 1;
		while(check == 1){
			newCard = rand.nextInt(52);
			check = 0;
			for(int element: used){
				if(newCard == element)
					check = 1; //Card generated has already been used
			}
		}
		used.add(newCard);
		hand.add(cards[newCard]);
		handTotal = 0; //Recalculate total
		int aceTotal = 0;
		for(String element: hand)
		{
			handTotal += getCardValue(element);
			if(element.equals("A"))
				aceTotal += 1;
		}
		while(aceTotal > 0)
		{
			if((handTotal + 10) <= 21)
				handTotal += 10;
			aceTotal --;
		}
	}

	private void betSystem(int playerNum)
	{
		System.out.println();
		System.out.println("Player " + playerNum + ": Your cash: " + money);
		System.out.println("Player " + playerNum + ": Place a bet");
		bet = scan.nextInt();
		while(bet > money || bet <= 0)
		{
			System.out.println("Invalid bet");
			bet = scan.nextInt();
		}
		newCard();
		newCard();
	}

	private void hitSystem(int playerNum, Blackjack AI)
	{
		System.out.println();
		System.out.println("Dealer card: " + AI.hand.get(0));
		System.out.print("Player " + playerNum + ": Your cards: ");
		displayCards();
		System.out.println("Player " + playerNum + ": Your card total: " + handTotal);
		System.out.println("Player " + playerNum + ": Would you like to hit?");
		char hit = scan.next().toLowerCase().charAt(0);
		while(hit == 'y')
		{
			newCard();
			System.out.print("Player " + playerNum + ": Your cards: ");
			displayCards();
			System.out.println("Player " + playerNum + ": Your card total: " + handTotal);
			if(handTotal <= 21)
			{
				System.out.println("Player " + playerNum + ": Would you like to hit?");
				hit = scan.next().toLowerCase().charAt(0);
			}
			else
			{
				System.out.println("Player " + playerNum + ": You BUST, dealer wins!");
				handTotal = 0;
				hit = 'n';
			}
		}
	}

	private void winSystem(int playerNum, Blackjack AI)
	{
		if(handTotal == 0)
		{
			System.out.println("Player " + playerNum + ": You lose because you bust.");
			money -= bet;
			summary = "LOSE: Player Bust";
		}
		else if(AI.handTotal > 21)
		{
			System.out.println("Player " + playerNum + ": You win because dealer busts.");
			money += bet;
			summary = "WIN: Dealer Bust";
		}
		else if(AI.handTotal > handTotal)
		{
			System.out.println("Player " + playerNum + ": Your total: " + handTotal + " Dealer total: " + AI.handTotal);
			System.out.println("Dealer wins!");
			money -= bet;
			summary = "LOSE: Player lost to dealer";
		}
		else if(AI.handTotal < handTotal)
		{
			System.out.println("Player " + playerNum + ": Your total: " + handTotal + " Dealer total: " + AI.handTotal);
			System.out.println("You win!");
			money += bet;
			summary = "WIN: Player beat the dealer";
		}
		else if(AI.handTotal == handTotal)
		{
			System.out.println("Player 1: Your total: " + handTotal + " Dealer total: " + AI.handTotal);
			System.out.println("Push.");
			summary = "PUSH: Player tied the dealer";
		}
	}

	private static int getCardValue(String card)
	{
		int num = 0;
		switch(card){
		case "0": num = 0; break;
		case "1": num = 1; break;
		case "2": num = 2; break;
		case "3": num = 3; break;
		case "4": num = 4; break;
		case "5": num = 5; break;
		case "6": num = 6; break;
		case "7": num = 7; break;
		case "8": num = 8; break;
		case "9": num = 9; break;
		case "10": num = 10; break;
		case "J": num = 10; break;
		case "Q": num = 10; break;
		case "K": num = 10; break;
		case "A": num = 1; break;
		}
		return num;
	}

	private void displayCards()
	{
		for(String element: hand)
		{
			System.out.print(element + " ");
		}
		System.out.println();
	}

	public static void main(String[] args)
	{
		System.out.println("Welcome to the casino. This is the Blackjack table. Please play responsibly.");
		System.out.println();
		System.out.println("Would you like to customize your game?");
		char custom = scan.next().toLowerCase().charAt(0);
		boolean cardCount = false;
		int deckSize = 4, runningCount = 0, trueCount = 0;
		if(custom == 'y'){
			System.out.println("Would you like to enable automatic card counting:");
			custom = scan.next().toLowerCase().charAt(0);
			if(custom == 'y')
				cardCount = true;
			System.out.println("Please enter number of decks:");
			deckSize = scan.nextInt();
		}
		System.out.println("How many players will be playing today? Limit: 4 players.");
		int numPlayers = scan.nextInt();
		while(numPlayers < 1 || numPlayers > 4)
		{
			System.out.println("Invalid number of players. Limit: 4 players.");
			numPlayers = scan.nextInt();
		}
		Blackjack p1 = new Blackjack();
		Blackjack p2 = new Blackjack();
		Blackjack p3 = new Blackjack();
		Blackjack p4 = new Blackjack();
		Blackjack AI = new Blackjack("AI");
		char play = 'y';
		boolean cond = true; //Switched from false when following lines were still operating
		switch(numPlayers)
		{
		case 1: cond = p1.money > 0; break;
		case 2: cond = p1.money > 0 || p2.money > 0; break;
		case 3: cond = p1.money > 0 || p2.money > 0 || p3.money > 0; break;
		case 4: cond = p1.money > 0 || p2.money > 0 || p3.money > 0 || p4.money > 0; break;
		default: System.out.println("Fatal error on number of players."); break;
		}
		while(cond && play == 'y') //Game starts here
		{
			p1.clear();
			p2.clear();
			p3.clear();
			p4.clear();
			AI.clear();
			AI.newCard();
			AI.newCard();
			
			if(cardCount){
				System.out.println("Running count: " + runningCount);
				System.out.println("True count: " + trueCount);
			}

			//Insert Bets
			if(numPlayers >= 1 && p1.money > 0)
				p1.betSystem(1);
			if(numPlayers >= 2 && p2.money > 0)
				p2.betSystem(2);
			if(numPlayers >= 3 && p3.money > 0)
				p3.betSystem(3);
			if(numPlayers == 4 && p4.money > 0)
				p4.betSystem(4);
			
			
			/*Must display all cards to players before game
			 * 
			 * 
			 * 
			 */

			//Perform Hits
			if(numPlayers >= 1 && p1.money > 0)
				p1.hitSystem(1, AI);
			if(numPlayers >= 2 && p2.money > 0)
				p2.hitSystem(2, AI);
			if(numPlayers >= 3 && p3.money > 0)
				p3.hitSystem(3, AI);
			if(numPlayers == 4 && p4.money > 0)
				p4.hitSystem(4, AI);

			System.out.println();
			System.out.print("Dealer reveals cards: ");
			AI.displayCards();
			System.out.println("Dealer card total: " + AI.handTotal);
			while(AI.handTotal < 17 && AI.handTotal <= 21)
			{
				System.out.println();
				System.out.println("Dealer hits.");
				AI.newCard();
				System.out.print("Dealer cards: ");
				AI.displayCards();
				System.out.println("Dealer card total: " + AI.handTotal);
			}
			if(AI.handTotal > 21)
				System.out.println("Dealer BUST, remaining players win!");

			if(numPlayers >= 1 && p1.money > 0)
				p1.winSystem(1, AI);
			if(numPlayers >= 2 && p2.money > 0)
				p2.winSystem(2, AI);
			if(numPlayers >= 3 && p3.money > 0)
				p3.winSystem(3, AI);
			if(numPlayers == 4 && p4.money > 0)
				p4.winSystem(4, AI);
			System.out.println();
			System.out.println("Game Summary:");
			if(numPlayers >= 1)
				System.out.println("Player 1: " + p1.summary);
			if(numPlayers >= 2)
				System.out.println("Player 2: " + p2.summary);
			if(numPlayers >= 3)
				System.out.println("Player 3: " + p3.summary);
			if(numPlayers == 4)
				System.out.println("Player 4: " + p4.summary);
			System.out.println();
			if(numPlayers >= 1 && p1.money <= 0)
			{
				if(!p1.summary.equalsIgnoreCase("Inactive"))
					System.out.println("Player 1 is broke and has to leave the casino.");
				p1.summary = "Inactive";
			}
			if(numPlayers >= 2 && p2.money <= 0)
			{
				if(!p2.summary.equalsIgnoreCase("Inactive"))
					System.out.println("Player 2 is broke and has to leave the casino.");
				p2.summary = "Inactive";
			}
			if(numPlayers >= 3 && p3.money <= 0)
			{
				if(!p3.summary.equalsIgnoreCase("Inactive"))
					System.out.println("Player 3 is broke and has to leave the casino.");
				p3.summary = "Inactive";
			}
			if(numPlayers == 4 && p4.money <= 0)
			{
				if(!p4.summary.equalsIgnoreCase("Inactive"))
					System.out.println("Player 4 is broke and has to leave the casino.");
				p4.summary = "Inactive";
			}
			System.out.println();

			switch(numPlayers)
			{
			case 1: cond = p1.money > 0; break;
			case 2: cond = p1.money > 0 || p2.money > 0; break;
			case 3: cond = p1.money > 0 || p2.money > 0 || p3.money > 0; break;
			case 4: cond = p1.money > 0 || p2.money > 0 || p3.money > 0 || p4.money > 0; break;
			default: System.out.println("Fatal error on number of players."); break;
			}
			if(cond)
			{
				System.out.println("Play again?");
				play = scan.next().toLowerCase().charAt(0);
			}
			else
				System.out.println("Ya'll broke.");
		}
	}
}