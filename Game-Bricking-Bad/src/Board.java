import javax.swing.JFrame;

public class Board extends JFrame {
	
	private static Board board_instance = null;
	
	//The board is singleton.
	public Board() {
	//Leave the Constructor empty so that it does not initialize a field during board_instanceect creation.
	}
	
	public static Board getInstance() {
		if (board_instance == null) {
			board_instance = new Board();
			board_instance.setBounds(10, 10, 700, 600);
			board_instance.setTitle("Bricking Bad");		
			board_instance.setResizable(false);
			board_instance.setVisible(true);
			board_instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			return board_instance;
		}
		else {
			return board_instance;
		}
	}
}
