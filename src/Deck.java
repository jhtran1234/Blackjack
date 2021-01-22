import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
class Deck {
	static ArrayList<String> cards = new ArrayList<>();
	static Stack<String> shoe = new Stack<>();
	private static Random rand = new Random();
	static int numDecks = 6; //Number of decks in shoe
	static int cutCard; //Sets when the dealer will shuffle
	static int high = 0, low = 0, neutral = 0; //Used for card counting stats
	
	static String deal() {
		String card = shoe.pop();
		int val = Player.getCardValue(card);
		if(val <= 6 && val != 1){
			low ++;
		}
		else if (val >= 10 || val == 1) {
			high ++;
		}
		else {
			neutral ++;
		}
		
		return card;
	}
	
	/*
	 * @return true if dealer is shuffling shoe
	 */
	static boolean remainingShoe() {
		if(shoe.size() <= cutCard) {
			shuffle();
			return true;
		}
		return false;
	}
	
	static void setNumDecks(int decks) {
		numDecks = decks;
	}
	
	static void setCutCard(int card) {
		cutCard = card;
	}
	
	static void setShoeSize(int size) {
		numDecks = size;
	}

	static void shuffle() {
		cards.clear();
		shoe.clear();
		high = 0;
		low = 0;
		
		for(int deck = 0; deck < numDecks * 4; deck ++) {
			for(int card = 1; card <= 13; card++) {
				switch(card) {
				case 1: cards.add("A"); break;
				case 2: cards.add("2"); break;
				case 3: cards.add("3"); break;
				case 4: cards.add("4"); break;
				case 5: cards.add("5"); break;
				case 6: cards.add("6"); break;
				case 7: cards.add("7"); break;
				case 8: cards.add("8"); break;
				case 9: cards.add("9"); break;
				case 10: cards.add("10"); break;
				case 11: cards.add("J"); break;
				case 12: cards.add("Q"); break;
				case 13: cards.add("K"); break;
				}
			}
		}
		
		//Adds and shuffles all cards to shoe
		while(cards.size() > 0) {
			shoe.push(cards.remove(rand.nextInt(cards.size())));
		}
		
		cutCard = rand.nextInt(30) + 50 + 2 * numDecks;
	}
}
