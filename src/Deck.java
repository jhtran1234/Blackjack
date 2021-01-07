import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
class Deck {
	static ArrayList<String> cards = new ArrayList<>();
	static Stack<String> shoe = new Stack<>();
	static int numDecks = 6; //Number of decks in shoe
	
	static void clear() {
		shoe.clear();
		
	}
	
	static void setShoeSize(int size) {
		numDecks = size;
		
		String currentCard = "";
		for(int deck = 0; deck < numDecks; deck ++) {
			for(int suite = 0; suite < 4; suite ++) {
				for(int card = 1; card <= 13; card++) {
					currentCard = "";
					switch(suite) {
					case 0: currentCard = "";
					case 1: currentCard = "";
					case 2: currentCard = "";
					case 3: currentCard = "";
					}
					
					switch(card) {
					case 1: currentCard += "A";
					case 2: currentCard += "A";
					case 3: currentCard += "A";
					case 4: currentCard += "A";
					case 5: currentCard += "A";
					case 6: currentCard += "A";
					case 7: currentCard += "A";
					case 8: currentCard += "A";
					case 9: currentCard += "A";
					case 10: currentCard += "A";
					case 11: currentCard += "A";
					case 12: currentCard += "A";
					case 13: currentCard += "A";
					}
				}
			}
		}
	}
	
	static String deal() {
		return shoe.pop();
	}
}
