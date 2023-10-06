import view.GameView;

public class App {
    public static void main(String[] args) throws Exception {
    	System.out.println("inicio");
    	GameView gameView = new GameView();
    	gameView.renderGame();
        //Communicator communicator = new RPCCommunicator();
        //MatchBuilder game = new Game(communicator);
        //Controller controller = new GUIController(game);

        //controller.start();
    }

}