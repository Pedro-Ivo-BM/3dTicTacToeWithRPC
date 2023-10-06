package view;

import java.rmi.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import RPC.PlayerManager;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

import java.awt.ScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class GameView {
	private PlayerManager player;
	int firstPlayer;
	private JButton[][][] cells = new JButton[3][3][3];
	private Set<JButton> allButtons = new HashSet<>();

	private JFrame frame;
	private JLayeredPane layeredPane;
	private JTextField textField;
	private JTextPane chat;
	private JButton giveUpButton;
	private JButton sendMessageButton;
	

	/**
	 * Launch the application.
	 */

	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void renderGame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameView() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 722, 671);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(21, 11, 675, 610);
		frame.getContentPane().add(layeredPane);
		layeredPane.setLayout(new CardLayout(0, 0));
		
		/// TELA DE MENU INICIAL
		JPanel menu = new JPanel();
		layeredPane.add(menu, "name_603373602579100");
		menu.setLayout(null);
		
		JButton btnPlayAsServer = new JButton("Jogar Como Player 1");
		btnPlayAsServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame(true);
			}
		});
		btnPlayAsServer.setBounds(254, 211, 171, 23);
		menu.add(btnPlayAsServer);
		
		JButton btnPlayAsClient = new JButton("Jogar como Player 2");
		btnPlayAsClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame(false);
			}
		});
		btnPlayAsClient.setBounds(249, 245, 176, 23);
		menu.add(btnPlayAsClient);
		
		JTextArea txtrJogoDaVelha = new JTextArea();
		txtrJogoDaVelha.setText("JOGO DA VELHA 3D");
		txtrJogoDaVelha.setBounds(264, 93, 138, 30);
		txtrJogoDaVelha.setEnabled(false);
		menu.add(txtrJogoDaVelha);
		
		JTextArea txtrComunicaoRpc = new JTextArea();
		txtrComunicaoRpc.setText("Comunicação RPC");
		txtrComunicaoRpc.setBounds(274, 134, 124, 23);
		txtrComunicaoRpc.setEnabled(false);
		menu.add(txtrComunicaoRpc);
		
		//// TELA DO JOGO
		JPanel game = new JPanel();
		layeredPane.add(game, "name_603428771636100");
		game.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("JOGO");
		lblNewLabel.setBounds(304, 11, 46, 14);
		game.add(lblNewLabel);
		
		JPanel gameGrid = new JPanel();
		gameGrid.setBounds(0, 45, 675, 565);
		game.add(gameGrid);
		gameGrid.setLayout(new GridLayout(0, 2, 0, 0));
		
		/// JOGO DO LADO ESQUERDO
		JPanel celulas = new JPanel();
		gameGrid.add(celulas);
		
		JPanel panelBoards = new JPanel();
		celulas.add(panelBoards);
		panelBoards.setLayout(new GridLayout(3, 0, 2, 2));
		
		///CRIANDO CELULAS DO JOGO
		for (int board = 0; board < 3; board++) {
			JPanel panelBoard = new JPanel();
			panelBoard.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			panelBoards.add(panelBoard);
			panelBoard.setLayout(new GridLayout(3, 3, 0, 0));

			for (int line = 0; line < 3; line++) {
				for (int column = 0; column < 3; column++) {
					JButton button = new JButton(" ");
					panelBoard.add(button);
					cells[board][line][column] = button;
					allButtons.add(button);
					button.setEnabled(false);
					button.setForeground(Color.BLACK);
					button.setFont(new Font("Trebuchet MS", Font.ROMAN_BASELINE, 40));
					button.setActionCommand("" + board + line + column);
					
					/// AQUI OCORRE A JOGADA NA CÉLULA SELECIONADA.
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {						
							String choosedCell = e.getActionCommand();
							System.out.println("escolhido: " + choosedCell);
							int board = Character.getNumericValue(choosedCell.charAt(0));
							int line = Character.getNumericValue(choosedCell.charAt(1));
							int column = Character.getNumericValue(choosedCell.charAt(2));
							
							
							boolean isValid = player.gameManager.makePlay(board, line, column, player.thisPlayer);
							
							if(isValid) {
								try {
									cells[board][line][column].setText("" + player.thisPlayer);
									player.guestRPCConnection.setPlayerAction(choosedCell, player.thisPlayer);
									
									player.actualPlayer = player.actualPlayer == 1? 2 : 1;
									changeButtonsHabilitation(false);
									player.guestRPCConnection.setActualPlayer(player.actualPlayer);
									
									
									
									
								} catch (RemoteException e1) {
									
									e1.printStackTrace();
								}
								
							} else {
								sendTextToChatLocally("[SISTEMA] JOGADA INVÁLIDA, JOGUE NOVAMENTE"); 
								
							}
							
						}
					});
				}
			}
		}
		
		/// MINI MENU DO LADO DIREITO COM BOTOES E CHAT
		JPanel miniMenu = new JPanel();
		gameGrid.add(miniMenu);
		miniMenu.setLayout(null);
		
		giveUpButton = new JButton("Desistir");
		giveUpButton.setBounds(127, 11, 125, 23);
		giveUpButton.setEnabled(false);
		miniMenu.add(giveUpButton);
		
		textField = new JTextField();
		textField.setBounds(10, 452, 317, 43);
		textField.setEnabled(false);
		miniMenu.add(textField);
		textField.setColumns(10);
		
	    sendMessageButton = new JButton("Enviar Mensagem");
		sendMessageButton.setBounds(107, 516, 163, 23);
		sendMessageButton.setEnabled(false);
		sendMessageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textField.getText();
				if (!message.equals("")) {
					sendTextToChat(message);
					textField.setText("");
				}
			}
		});
		miniMenu.add(sendMessageButton);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setBounds(10, 230, 317, 206);
		miniMenu.add(scrollPane);
		
		chat = new JTextPane();
		chat.setBounds(10, 230, 317, 206);
		chat.setEditable(false);
		scrollPane.add(chat);
		
		
		

	}

//// AREA DE CONTROLE DO JOGO E DA VIEW
	/// FUNÇÕES DA VIEW
	public void startGame(boolean asPlayer1) {
		//Container container = frame.getContentPane();
		CardLayout layout = (CardLayout) layeredPane.getLayout();
		layout.next(layeredPane);
		
		
		if(asPlayer1) {
			try {
				player = new PlayerManager(1);
				LocateRegistry.createRegistry(PlayerManager.PORT1);
				Naming.rebind(PlayerManager.HOST1, player);
				sendTextToChatLocally("Esperando outro jogador conectar...");
				
				firstPlayer = (new Random()).nextBoolean()? 1 : 2;
				
				listenToOtherPlayerConnecting(true);
								
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				player = new PlayerManager(2);
				LocateRegistry.createRegistry(PlayerManager.PORT2);
				Naming.rebind(PlayerManager.HOST2, player);
				sendTextToChatLocally("Esperando outro jogador conectar...");
				
				listenToOtherPlayerConnecting(false);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	private void sendTextToChatLocally(String text) {
		String currentText = chat.getText();
		chat.setText(currentText.equals("") ? text : currentText + '\n' + text);
	}
	
	private void sendTextToChat(String text)  {
		
		String currentText = chat.getText();
		chat.setText(currentText.equals("") ? text : currentText + '\n' + "[VOCE] " + text);
		try {
			player.guestRPCConnection.sendMessage(text); 
		} catch (RemoteException e) {
			e.printStackTrace();
		}
			
		
		
	}
	
	private void changeButtonsHabilitation(boolean habilitation) {
		allButtons.forEach( button -> {button.setEnabled(habilitation);});
	}
	
	/// THREAD PARALELA ESCUTANDO MUDANÇAS DO JOGADOR REMOTO
	public void listenToOtherPlayerChanges() {
		(new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				while (player.isGameOn) {
					try {
						Thread.sleep(500);
						if(player.guestLastMessage != "") {
							String currentText = chat.getText();
							chat.setText(currentText.equals("") ? player.guestLastMessage : currentText + '\n' + "[ADVERSARIO] " + player.guestLastMessage);
							player.guestLastMessage = "";
						}
						if(player.guestSystemLastMessage != "") {
							String currentText = chat.getText();
							chat.setText(currentText.equals("") ? player.guestLastMessage : currentText + '\n' + "[SISTEMA] " + player.guestSystemLastMessage);
							player.guestSystemLastMessage = "";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		}).execute();
	}
	
	/// THREAD PARALELA RODANDO O JOGO 
		public void gameLoop() {
			(new SwingWorker<Void, Void>() {
				@Override
				public Void doInBackground() {
					while (player.isGameOn) {
						try {
							if(player.guestLastPlay != "") {
								String choosedCell = player.guestLastPlay;
								player.guestLastPlay = "";
								
								int board = Character.getNumericValue(choosedCell.charAt(0));
								int line = Character.getNumericValue(choosedCell.charAt(1));
								int column = Character.getNumericValue(choosedCell.charAt(2));
								int guestNumber = Character.getNumericValue(choosedCell.charAt(3));
								
								cells[board][line][column].setText("" + guestNumber);
								
							}
							if(player.thisPlayer == player.actualPlayer) {
								changeButtonsHabilitation(true);
								Thread.sleep(2000);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			}).execute();
		}
	
	/// THREAD PARALELA QUE TENTA CONECTAR AO SERVIDOR DO OUTRO JOGADOR 
	public void listenToOtherPlayerConnecting(boolean asPlayer1) {
		(new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				boolean connectedToGuest = false;
				while(!connectedToGuest) {
					try {
						Thread.sleep(500);
						connectedToGuest = player.startConnectionToOtherPlayer();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				/// HABILITANDO CAMPOS GERAIS
				giveUpButton.setEnabled(true);
				sendMessageButton.setEnabled(true);
				textField.setEnabled(true);
				
                sendTextToChatLocally("jogador conectou");
				player.isGameOn = true;
				
				/// IF ELSE QUE SETA QUEM COMEÇA JOGANDO PRIMEIRO APÓS CONEXAO ENTRE JOGADORES
				if(asPlayer1) {
					if(firstPlayer == 1) {
						player.iAmFirst = true;
						player.actualPlayer = 1;
						sendTextToChatLocally("VOCÊ COMEÇA");
						try {
							player.guestRPCConnection.sendSystemMessage("ADVERSÁRIO COMEÇA");
							player.guestRPCConnection.setPlayerTurn(1);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}else {
						player.actualPlayer = 2;
						sendTextToChatLocally("ADVERSÁRIO COMEÇA");
						try {
							player.guestRPCConnection.setMeAsFirstPlayer();
							player.guestRPCConnection.sendSystemMessage("VOCÊ COMEÇA");
							player.guestRPCConnection.setPlayerTurn(2);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
				
				
				
				/// HABILITANDO CELULAS DO JOGO APENAS PRA QUEM COMEÇA
				if(player.thisPlayer == player.actualPlayer) {
					changeButtonsHabilitation(true);
				}
				
				listenToOtherPlayerChanges();
				gameLoop();
				return null;
			}
		}).execute();
	}
}
