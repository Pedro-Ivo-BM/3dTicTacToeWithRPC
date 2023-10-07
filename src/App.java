import view.GameView;

public class App {
	public static void main(String[] args) throws Exception {
		System.out.println("inicio");
		GameView gameView = new GameView();
		gameView.renderGame();
	}

}