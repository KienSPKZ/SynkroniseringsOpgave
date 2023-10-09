

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {

	public static final int size = 20; 
	public static final int scene_height = size * 20 + 100;
	public static final int scene_width = size * 20 + 200;
	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down,fire_up,fire_down,fire_left,fire_right;
	public static Player me;
	public static List<Player> players = new ArrayList<Player>();
	private static Label[][] fields;
	private TextArea scoreList;
	private String name;
	public static final List<String> playerSpawns = new ArrayList<>();
	
	private  String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};

	private BufferedReader inFromServer;
	private DataOutputStream outToServer;

	
	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Write your name:");
			name = sc.nextLine();
			sc.close();
			playerSpawns.add("9 4");
			playerSpawns.add("14 15");
			playerSpawns.add("6 18");
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	
			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();
			
			GridPane boardGrid = new GridPane();

			image_wall  = new Image(getClass().getResourceAsStream("Image/wall4.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);
			fire_up		= new Image(getClass().getResourceAsStream("Image/fireUp.png"),size,size,false,false);
			fire_down	= new Image(getClass().getResourceAsStream("Image/fireDown.png"),size,size,false,false);
			fire_left	= new Image(getClass().getResourceAsStream("Image/fireLeft.png"),size,size,false,false);
			fire_right	= new Image(getClass().getResourceAsStream("Image/fireRight.png"),size,size,false,false);

			fields = new Label[20][20];
			for (int j=0; j<20; j++) {
				for (int i=0; i<20; i++) {
					switch (board[j].charAt(i)) {
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':					
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					default: throw new Exception("Illegal field value: "+board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);
			
			
			grid.add(mazeLabel,  0, 0); 
			grid.add(scoreLabel, 1, 0); 
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);
						
			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP:
					try {
						outToServer.writeBytes( "move " + me.name + " " + 0 + " " + -1 + " " + "up" + '\n');
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case DOWN:
					try {
						outToServer.writeBytes("move " + me.name + " " + 0 + " " + 1 + " " + "down" + '\n');
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case LEFT:
					try {
						outToServer.writeBytes("move " + me.name + " " + -1 + " " + 0 + " " + "left" + '\n');
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case RIGHT:
					try {
						outToServer.writeBytes("move " + me.name + " " + 1 + " " + 0 + " " + "right" + '\n');
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case SHIFT:
					try {
						outToServer.writeBytes("shoot " + me.name + '\n');
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				default: break;
				}
			});
			
            // Setting up standard players

			Random rand = new Random();
			String[] playerPos = playerSpawns.get(rand.nextInt(playerSpawns.size())).split(" ");
			me = new Player(name,Integer.parseInt(playerPos[0]),Integer.parseInt(playerPos[1]),"up");
			players.add(me);
			fields[Integer.parseInt(playerPos[0])][Integer.parseInt(playerPos[1])].setGraphic(new ImageView(hero_up));

			scoreList.setText(getScoreList());

			//Connection

			Socket connectionToSocket =  new Socket("localhost", 1234);
			inFromServer = new BufferedReader(new InputStreamReader(connectionToSocket.getInputStream()));
			outToServer = new DataOutputStream(connectionToSocket.getOutputStream());

			ClientInputThread cit = new ClientInputThread(inFromServer, this);
			cit.start();
			sendStringForMePlayerInfo();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void runPlayerMoved(String name, int delta_x, int delta_y, String direction) {
		int index = getIndexFromListByName(name);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				playerMoved(index, delta_x, delta_y, direction);
			}
		});
	}

	private void playerMoved(int index, int delta_x, int delta_y, String direction) {
		players.get(index).direction = direction;
		int x = players.get(index).getXpos(),y = players.get(index).getYpos();

		if (board[y+delta_y].charAt(x+delta_x)=='w') {
			players.get(index).addPoints(-1);
		}
		else {
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
				players.get(index).addPoints(10);
				p.addPoints(-10);
			} else {
				players.get(index).addPoints(1);

				fields[x][y].setGraphic(new ImageView(image_floor));
				x+=delta_x;
				y+=delta_y;

				if (direction.equals("right")) {
					fields[x][y].setGraphic(new ImageView(hero_right));
				};
				if (direction.equals("left")) {
					fields[x][y].setGraphic(new ImageView(hero_left));
				};
				if (direction.equals("up")) {
					fields[x][y].setGraphic(new ImageView(hero_up));
				};
				if (direction.equals("down")) {
					fields[x][y].setGraphic(new ImageView(hero_down));
				};

				players.get(index).setXpos(x);
				players.get(index).setYpos(y);
			}
		}
		scoreList.setText(getScoreList());
	}

	private int getIndexFromListByName(String name) {
		int index = 0;
		for (Player p : players) {
			if (p.name.equals(name)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public void addNewPlayer(String name, int xPos, int yPos, String direction) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!me.name.equals(name) && !containsName(name)) {
					Player newPlayer = new Player(name, xPos, yPos, direction);
					players.add(newPlayer);
					if (direction.equals("right")) {
						fields[xPos][yPos].setGraphic(new ImageView(hero_right));
					}
					if (direction.equals("left")) {
						fields[xPos][yPos].setGraphic(new ImageView(hero_left));
					}
					if (direction.equals("up")) {
						fields[xPos][yPos].setGraphic(new ImageView(hero_up));
					}
					if (direction.equals("down")) {
						fields[xPos][yPos].setGraphic(new ImageView(hero_down));
					}
					try {
						sendStringForMePlayerInfo();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
	}

	public void sendStringForMePlayerInfo() throws IOException {
		outToServer.writeBytes("newPlayer " + me.name + " " + me.xpos + " " + me.ypos + " " + me.direction + '\n');
	}

	public void shootFromPlayerPublic(String name) {
		int index = getIndexFromListByName(name);
		shootFromPLayer(players.get(index).direction, players.get(index).xpos, players.get(index).ypos);
	}

	private void shootFromPLayer(String direction, int xPos, int yPos) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				int x_d = xPos;
				int y_d = yPos;
				if (direction.equals("up")) {
					y_d -= 1;

				} else if (direction.equals("down")) {
					y_d += 1;

				} else if (direction.equals("right")) {
					x_d += 1;

				} else if (direction.equals("left")) {
					x_d -= 1;

				}
				int x_r = x_d;
				int y_r = y_d;
				int fieldsChanged = 0;
				while (board[y_d].charAt(x_d) != 'w') {
					Player p = getPlayerAt(x_d, y_d);
					if (p != null) {
						me.addPoints(20);
						p.addPoints(-20);
						break;
					}
					if (direction.equals("up")) {
						fields[x_d][y_d].setGraphic(new ImageView(fire_up));
						y_d--;

					} else if (direction.equals("down")) {
						fields[x_d][y_d].setGraphic(new ImageView(fire_down));
						y_d++;

					} else if (direction.equals("right")) {
						fields[x_d][y_d].setGraphic(new ImageView(fire_right));
						x_d++;

					} else if (direction.equals("left")) {
						fields[x_d][y_d].setGraphic(new ImageView(fire_left));
						x_d--;
					}
					fieldsChanged++;
				}
				ExecutorService executor = Executors.newSingleThreadExecutor();
				int finalFieldsChanged = fieldsChanged;
				executor.submit(() -> {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					removeShot(x_r, y_r, direction, finalFieldsChanged);
				});
			}
		});
	}

	private void removeShot(int x_r, int y_r, String direction, int fieldsChanged) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				int xPos_r = x_r;
				int yPos_r = y_r;
				for (int i = fieldsChanged; i > 0; i--) {
					fields[xPos_r][yPos_r].setGraphic(new ImageView(image_floor));
					if (direction.equals("up")) {
						yPos_r--;

					} else if (direction.equals("down")) {
						yPos_r++;

					} else if (direction.equals("right")) {
						xPos_r++;

					} else if (direction.equals("left")) {
						xPos_r--;
					}
				}
			}
		});
	}

	private boolean containsName(String name) {
		for (Player pl: players) {
			if (pl.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}


}

