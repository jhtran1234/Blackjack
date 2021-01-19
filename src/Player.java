import java.util.ArrayList;
import java.util.Scanner;
class Player {
	int money, bet, handTotal, splitHandTotal;
	String name, summary;
	ArrayList<String> hand = new ArrayList<String>();
	ArrayList<String> splitHand = new ArrayList<String>();
	boolean blackJack = false, split = false, splitBJ = false, doubleDown = false, cent = false;

	private static Scanner scan = new Scanner(System.in);

	public Player() {
		money = 100;
		bet = 0;
		handTotal = 0;
		summary = "clear";
	}

	public Player(String AI) {
		money = 1000000;
		bet = 999999;
		handTotal = 0;
		summary = "empty";
	}

	
	void clear() {
		bet = 0;
		handTotal = 0;
		splitHandTotal = 0;
		hand.clear();
		splitHand.clear();
		blackJack = false;
		split = false;
		splitBJ = false;
		doubleDown = false;
	}

	void addName(String name) {
		this.name = name;
	}

	private int calculateHandTotal(ArrayList<String> hand) {
		int total = 0, aceTotal = 0;
		for(String card: hand) {
			total += getCardValue(card);
			if(card.charAt(0) == 'A') {
				aceTotal += 1;
			}
		}

		while(aceTotal > 0) { //Correctly calculates Aces in play
			if((total + 10) <= 21) {
				total += 10;
			}
			aceTotal --;
		}
		
		return total;
	}
	
	void newCard() {
		hand.add(Deck.deal());
		handTotal = calculateHandTotal(this.hand);
	}

	void bet() {
		while(this.bet <= 0) {
			System.out.println(name + ", you have " + money + ". Please enter a bet.");
			try {
				this.bet = scan.nextInt();
				while(bet > money || bet <= 0){
					System.out.println("Invalid bet");
					bet = scan.nextInt();
				}
			}
			catch (Exception e) {
				System.out.println("Invalid bet");
			}
		}
	}

	void play(Player AI) {
		System.out.println();
		System.out.println("Dealer card: " + AI.hand.get(0));
		System.out.print(name + ": Your cards: ");
		displayCards();
		System.out.println(name + ": Your card total: " + handTotal);

		if(handTotal == 21) {
			System.out.println(name + ": You have a BlackJack!");
			blackJack = true;
			return;
		}

		if(hand.get(0).equalsIgnoreCase(hand.get(1)) && 2 * bet <= money) {
			System.out.println(name + ": Would you like to spilt pairs?");
			if(scan.next().toLowerCase().charAt(0) == 'y') {
				splitPairs();
				System.out.println(name + ": Your right hand card: " + hand.get(0));
				System.out.println(name + ": Your right hand card total: " + handTotal);
			}
		}

		if(handTotal >= 9 && handTotal <= 11 && 2 * bet <= money) {
			System.out.println(name + ": Would you like to double down?");
			if(scan.next().toLowerCase().charAt(0) == 'y') {
				doubleDown();
				return;
			}
		}

		System.out.println(name + ": Would you like to hit?");
		char hit = scan.next().toLowerCase().charAt(0);
		while(hit == 'y') {
			newCard();
			System.out.print(name + ": Your cards: ");
			displayCards();
			System.out.println(name + ": Your card total: " + handTotal);

			if(split && handTotal == 21 && hand.size() == 2) {
				blackJack = true; //A split BlackJack is paid only 1:1
				System.out.println(name + ": Your right hand has a BlackJack!");
				hit = 'n';
			}
			else if (split && hand.get(0).equalsIgnoreCase("A")) {
				System.out.println(name + ": You may only hit once on a split Ace.");
				hit = 'n';
			}
			else if(split && handTotal > 21) {
				System.out.println(name + ": You BUST, dealer beats your right hand!");
				hit = 'n';
			}
			else if(handTotal <= 21) {
				System.out.println(name + ": Would you like to hit?");
				hit = scan.next().toLowerCase().charAt(0);
			}
			else {
				System.out.println(name + ": You BUST, dealer wins!");
				hit = 'n';
			}
		}
	}

	void splitPairs() {
		split = true;
		splitHand.add(hand.remove(1));
		handTotal /= 2;
		splitHandTotal = handTotal;

		System.out.println(name + ": Split pairs. An equal bet of " + bet + " has been placed for the split hand.");
		System.out.println(name + ": Your left hand card: " + splitHand.get(0));
		System.out.println(name + ": Your left hand card total: " + splitHandTotal);
		System.out.println(name + ": Would you like to hit?");
		char hit = scan.next().toLowerCase().charAt(0);
		while(hit == 'y') {
			splitHand.add(Deck.deal());
			splitHandTotal = calculateHandTotal(this.splitHand);

			System.out.print(name + ": Your cards: ");
			for(String card: splitHand){
				System.out.print(card + " ");
			}
			System.out.println();
			System.out.println(name + ": Your card total: " + splitHandTotal);
			
			if(splitHandTotal == 21 && splitHand.size() == 2) {
				splitBJ = true;
				System.out.println(name + ": Your left hand has a BlackJack!");
				hit = 'n';
			}
			else if (splitHand.get(0).equalsIgnoreCase("A")) {
				System.out.println(name + ": You may only hit once on a split Ace.");
				hit = 'n';
			}
			else if(splitHandTotal <= 21) {
				System.out.println(name + ": Would you like to hit?");
				hit = scan.next().toLowerCase().charAt(0);
			}
			else {
				System.out.println(name + ": You BUST, dealer beats your left hand!");
				hit = 'n';
			}
		}
		System.out.println();
	}

	void doubleDown() {		
		doubleDown = true;
		bet *= 2;
		newCard();
		System.out.println(name + ": Doubled down. Bet has been doubled to " + bet + ".");
	}

	void calculateOutcome(Player AI){
		if(doubleDown) {
			System.out.print(name + ": Your cards are revealed: ");
			displayCards();
		}
		
		if(split) {
			if(splitBJ) {
				System.out.println(name + ": Your left hand has a BlackJack!");
				money += bet; //Pay 1 to 1
				summary = "Left: WIN: BlackJack";
			}
			else if(splitHandTotal > 21) {
				System.out.println(name + ": Your left hand loses because you bust.");
				money -= bet;
				summary = "Left: LOSE: Player Bust";
			}
			else if(AI.handTotal > 21) {
				System.out.println(name + ": Your left hand wins because dealer busts.");
				money += bet;
				summary = "Left: WIN: Dealer Bust";
			}
			else if(AI.handTotal > splitHandTotal) {
				System.out.println(name + ": Your left hand total: " + splitHandTotal + " Dealer total: " + AI.handTotal);
				System.out.println("Dealer beats your left hand.");
				money -= bet;
				summary = "Left: LOSE: Player lost to dealer";
			}
			else if(AI.handTotal < splitHandTotal) {
				System.out.println(name + ": Your left hand total: " + splitHandTotal + " Dealer total: " + AI.handTotal);
				System.out.println("Your left hand wins!");
				money += bet;
				summary = "Left: WIN: Player beat the dealer";
			}
			else if(AI.handTotal == splitHandTotal) {
				System.out.println(name + ": Your left hand total: " + splitHandTotal + " Dealer total: " + AI.handTotal);
				System.out.println("Your left hand Pushes.");
				summary = "Left: PUSH: Player tied the dealer";
			}
			
			summary += "\n";
			
			if(blackJack) {
				System.out.println(name + ": Your right hand has a BlackJack!");
				money += bet; //Pay 1 to 1
				summary += "Right: WIN: BlackJack";
			}
			else if(handTotal > 21) {
				System.out.println(name + ": Your right hand loses because you bust.");
				money -= bet;
				summary += "Right: LOSE: Player Bust";
			}
			else if(AI.handTotal > 21) {
				System.out.println(name + ": Your right hand wins because dealer busts.");
				money += bet;
				summary += "Right: WIN: Dealer Bust";
			}
			else if(AI.handTotal > handTotal) {
				System.out.println(name + ": Your right hand total: " + handTotal + " Dealer total: " + AI.handTotal);
				System.out.println("Dealer beats your right hand.");
				money -= bet;
				summary += "Right: LOSE: Player lost to dealer";
			}
			else if(AI.handTotal < handTotal) {
				System.out.println(name + ": Your right hand total: " + handTotal + " Dealer total: " + AI.handTotal);
				System.out.println("Your right hand wins!");
				money += bet;
				summary += "Right: WIN: Player beat the dealer";
			}
			else if(AI.handTotal == handTotal) {
				System.out.println(name + ": Your right hand total: " + handTotal + " Dealer total: " + AI.handTotal);
				System.out.println("Your right hand Pushes.");
				summary += "Right: PUSH: Player tied the dealer";
			}
		}
		else if(AI.blackJack) {
			if(blackJack) {
				System.out.println(name + ": You push because both you and dealer have a BlackJack.");
				System.out.println("Push.");
				summary = "PUSH: Player tied the dealer";
			}
			else {
				System.out.println(name + ": You lose because dealer has a BlackJack.");
				money -= bet;
				summary = "LOSE: Dealer BlackJack";
			}
		}
		else if(blackJack) {
			System.out.println(name + ": You have a BlackJack!");
			money += 1.5 * bet; //Pay 3 to 2
			if(bet % 2 == 1) {
				cent = !cent;
				if(!cent) {
					money++;
				}
			}
			summary = "WIN: BlackJack";
		}
		else if(handTotal > 21) {
			System.out.println(name + ": You lose because you bust.");
			money -= bet;
			summary = "LOSE: Player Bust";
		}
		else if(AI.handTotal > 21) {
			System.out.println(name + ": You win because dealer busts.");
			money += bet;
			summary = "WIN: Dealer Bust";
		}
		else if(AI.handTotal > handTotal) {
			System.out.println(name + ": Your total: " + handTotal + " Dealer total: " + AI.handTotal);
			System.out.println("Dealer wins!");
			money -= bet;
			summary = "LOSE: Player lost to dealer";
		}
		else if(AI.handTotal < handTotal) {
			System.out.println(name + ": Your total: " + handTotal + " Dealer total: " + AI.handTotal);
			System.out.println("You win!");
			money += bet;
			summary = "WIN: Player beat the dealer";
		}
		else if(AI.handTotal == handTotal) {
			System.out.println(name + ": Your total: " + handTotal + " Dealer total: " + AI.handTotal);
			System.out.println("Push.");
			summary = "PUSH: Player tied the dealer";
		}
	}

	void displayCards() {
		for(String card: hand){
			System.out.print(card + " ");
		}
		System.out.println();
	}

	private static int getCardValue(String card) {
		int num = 0;
		switch(card.charAt(0)){
		case 'A': num = 1; break;
		case '2': num = 2; break;
		case '3': num = 3; break;
		case '4': num = 4; break;
		case '5': num = 5; break;
		case '6': num = 6; break;
		case '7': num = 7; break;
		case '8': num = 8; break;
		case '9': num = 9; break;
		case '1': num = 10; break;
		case 'J': num = 10; break;
		case 'Q': num = 10; break;
		case 'K': num = 10; break;
		}
		return num;
	}
}