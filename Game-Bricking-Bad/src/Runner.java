import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args) {
		//This is the main class who will create the objects in accordance with creator design patter
		
		
		Board gameBoard = Board.getInstance();
		//The game will ask the user what kind of a map he/she wants the game to be performed on!
		Game game = new Game();		
		gameBoard.add(game);
		gameBoard.setVisible(true);

                
           
	}

}
