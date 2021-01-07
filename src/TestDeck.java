
public class TestDeck {
	public static void main(String args[]) {
		Deck.shuffle();
		
		
		for(int i = 0; i < 52*18; i ++) {
			if(!Deck.remainingShoe()) {
				System.out.println(Deck.deal());
			}
			else {
				System.out.println("RESHUFFLE");
				System.exit(0);
			}
		}
	}
}
