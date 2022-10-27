/**
 *View object refect model class,
 * When model change it will call the cooresponding functions to update view;
 */


package kth.se.lab4demo.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kth.se.lab4demo.controll.*;


public class View {
    private static View instance = null;
    private static final int GRID_SIZE = 9;
    private static final int SECTIONS_PER_ROW = 3;
    private static final int SECTION_SIZE = 3;
    private final Label[][] numberTiles; // the tiles/squares to show in the ui grid
    private final VBox numberPane;
    private Stage primaryStage;

    /**
     * Private contructor initialise view all the interactions
     */
    private View() {
        numberTiles = new Label[GRID_SIZE][GRID_SIZE];
        initNumberTiles();
        numberPane = createNumberPane();
        initView();
    }

    /**
     * create and return one single instance of View
     * @return return the same instance of view
     */
    public static View getInstance(){
        if(instance == null)
            instance = new View();
        return instance;
    }

    /**
     * the call of corresponding functions to init view
     */
    private void initView(){
        this.primaryStage = new Stage();
        this.primaryStage.setTitle("Suduku");
        this.primaryStage.setOnCloseRequest(new WindowCloseRequestHandler());

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(numberPane);

        VBox leftPanel = createLeftPanel();
        borderPane.setLeft(leftPanel);

        VBox rightPanel = createRightPanel();
        borderPane.setRight(rightPanel);

        MenuBar menuBar = createMenyBar();
        borderPane.setTop(menuBar);

        Scene root = new Scene(borderPane);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    /**
     * A main function to update the view
     * @param stage 3D array, stage[][][0] original, stage[][][1] currentstage
     */
    public void updateView(int [][][] stage){
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                //update the number tiles
                if(stage[y][x][1] == 0){
                    numberTiles[y][x].setText("");
                }else{
                    String value = String.valueOf(stage[y][x][1]);
                    numberTiles[y][x].setText(value);
                }

                //The tile that can not be changed, will be represent as bolder font
                if(stage[y][x][0] != 0)
                    numberTiles[y][x].setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
                else
                    numberTiles[y][x].setFont(Font.font("Monospaced", FontWeight.SEMI_BOLD, 20));
            }
        }
    }

    /**
     *Take an index to show the current active button and change the color to blue
     * Take the index of the current active button and style it blue to indicate that it's active
     * @param currentIndex index of the active button
     *
     */
    public void updateActiveBtn(int currentIndex){
        //get instance of the VBox that contain all the button to the right
        Scene scene = primaryStage.getScene();
        BorderPane b = (BorderPane) scene.getRoot();
        VBox vBox =  (VBox) b.getRight();

        //loop throw all the buttons and compare its index to the currentindex if it equals make it blue else make it white
        for (int i = 0; i < 10; i++) {
            Button button = (Button) vBox.getChildren().get(i);
            if (currentIndex == i){
                button.setStyle("-fx-background-color: #0000ff;");
                button.setTextFill(Paint.valueOf("ffffff"));
                System.out.println(i);
            }
            else{
                button.setStyle(null);
                button.setTextFill(Paint.valueOf("000000"));
            }
        }
    }

    /**
     * prompt user to save game.
     * @param nextAction the next action after saved.
     */
    public void showSavedRequestStage(String nextAction){
        Text text = new Text("Current work is not saved yet\nDo you want to save before continues?");
        text.setTextAlignment(TextAlignment.CENTER);
        HBox textBox = new HBox(text);

        Button nextButton = new Button(nextAction);
        nextButton.setOnAction(new SavedRequestBtnHandler(nextAction.toUpperCase()));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(new SavedRequestBtnHandler("SAVE"));

        HBox btnBox = new HBox();
        btnBox.setSpacing(30);
        btnBox.getChildren().addAll(saveButton ,nextButton);
        btnBox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(textBox, btnBox);
        vbox.setSpacing(30);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);

        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * show a message if currentstage is correct or not
     * @param isCorrect true if correct, false if not correct
     */
    public void showResultStage(boolean isCorrect) {
        String msg = "Uncorrect";
        if (isCorrect) {
            msg = "Correct";
        }
        Text text = new Text(msg);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);
        if(isCorrect){
            Button newGameBtn = new Button("New Game");
            newGameBtn.setOnAction(new ResultBtnController("NEW_GAME"));
            hbox.getChildren().add(newGameBtn);
            Button exitBtn = new Button("Exit Game");
            exitBtn.setOnAction(new ResultBtnController("EXIT_GAME"));
            hbox.getChildren().add(exitBtn);
        }else{
            Button clearBtn = new Button("Clear All");
            clearBtn.setOnAction(new ResultBtnController("CLEAR_ALL"));
            hbox.getChildren().add(clearBtn);
            Button continueBtn = new Button("Continue");
            continueBtn.setOnAction(new ResultBtnController("CONTINUE"));
            hbox.getChildren().add(continueBtn);
        }

        vbox.getChildren().addAll(text,hbox);

        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * creates menubar
     * @return menubar
     */
    private MenuBar createMenyBar(){
        MenuBar menuBar = new MenuBar();

        //Menubar -> Menu -> MenuItem -> MenuItem
        //file
        MenuItem loadGameItem = new MenuItem("Load game");
        MenuItem saveGameItem = new MenuItem("Save game");
        MenuItem exitItem = new MenuItem("Exit");

        saveGameItem.setOnAction(new FileController("SAVE_GAME"));
        loadGameItem.setOnAction(new FileController("LOAD_GAME"));
        exitItem.setOnAction(new FileController("EXIT_GAME"));

        Menu filesMeny = new Menu("Files");
        filesMeny.getItems().addAll(loadGameItem, saveGameItem, exitItem);

        //game
        MenuItem easyGameSubItem = new MenuItem("Easy");
        MenuItem mediumGameSubItem = new MenuItem("Medium");
        MenuItem hardGameSubItem = new MenuItem("Hard");

        mediumGameSubItem.setOnAction(new NewGameBtnController("NEW_MEDIUM_GAME"));
        easyGameSubItem.setOnAction(new NewGameBtnController("NEW_EASY_GAME"));
        hardGameSubItem.setOnAction(new NewGameBtnController("NEW_HARD_GAME"));

        Menu newGameSubItem = new Menu("New game");
        newGameSubItem.getItems().addAll(easyGameSubItem,mediumGameSubItem, hardGameSubItem);

        Menu gameMeny = new Menu("Game");
        gameMeny.getItems().add(newGameSubItem);

        //help
        Menu helpMeny = new Menu("Help");
        MenuItem clearAllItem = new MenuItem("Clear all");
        clearAllItem.setOnAction(new HelpController("CLEAR_ALL"));

        MenuItem ruleItem = new MenuItem("Rule");
        ruleItem.setOnAction(new HelpController("RULE"));

        helpMeny.getItems().addAll(ruleItem,clearAllItem);

        menuBar.getMenus().addAll(filesMeny, gameMeny,helpMeny );
        return menuBar;
    }

    /**
     * create lefthandside panel including buttons
     * @return Vbox with buttons inside
     */
    private VBox createLeftPanel (){
        VBox leftPanel = new VBox();

        Button checkBtn = new Button("Check");
        Button hintBtn = new Button("hint");
        checkBtn.setOnAction(new LeftBtnController("Check"));
        leftPanel.getChildren().addAll(checkBtn, hintBtn);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setSpacing(15);
        leftPanel.setPadding(new Insets(10));
        hintBtn.setOnAction(new LeftBtnController("Hint"));

        return leftPanel;
    }

    /**
     * creates righthandside panel including buttons 1-C
     * @return vBox with buttons
     */
    private VBox createRightPanel (){
        VBox rightPanel = new VBox();

        rightPanel.getChildren().addAll();
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setSpacing(2);
        rightPanel.setPadding(new Insets(10));

        //create all buttons to the right side 1-C
        for (int i = 0; i < 10; i++) {
            String titel = i < 9 ? String.valueOf(i+1) : "C";
            Button btn = new Button(titel);
            rightPanel.getChildren().add(btn);

            btn.setOnAction(new RightBtnController(i));
        }

        return rightPanel;
    }

    /**
     *initials all number tiles as empty
     */
    private void initNumberTiles() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Label tile = new Label();
                tile.setPrefWidth(32);
                tile.setPrefHeight(32);
                tile.setAlignment(Pos.CENTER);
                tile.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;"); // css style

                tile.setOnMouseClicked(new MainBoardController(col, row)); // add your custom event handler

                // add new tile to grid
                numberTiles[row][col] = tile;
            }
        }
    }

    /**
     * create number pane, include 3x3 sections each section include 3x3 labels.
     * @return VBox with all labels
     */
    private VBox createNumberPane() {
        // create the root tile pane
        TilePane root = new TilePane();
        root.setPrefColumns(SECTIONS_PER_ROW);
        root.setPrefRows(SECTIONS_PER_ROW);
        root.setStyle("-fx-border-color: black; -fx-border-width: 1.0px; -fx-background-color: white;");

        // create the 3*3 sections and add the number tiles
        for (int srow = 0; srow < SECTIONS_PER_ROW; srow++) {
            for (int scol = 0; scol < SECTIONS_PER_ROW; scol++) {
                TilePane section = new TilePane();
                section.setPrefColumns(SECTION_SIZE);
                section.setPrefRows(SECTION_SIZE);
                section.setStyle( "-fx-border-color: black; -fx-border-width: 0.5px;");

                // add number tiles to this section
                for (int row = 0; row < SECTION_SIZE; row++) {
                    for (int col = 0; col < SECTION_SIZE; col++) {
                        // calculate which tile and add
                        section.getChildren().add(
                                numberTiles[srow * SECTION_SIZE + row][scol * SECTION_SIZE + col]);
                    }
                }

                // add the section to the root tile pane
                root.getChildren().add(section);
            }
        }

        VBox rootWrap = new VBox(root);
        rootWrap.setPadding(new Insets(7,0,7,0));

        return rootWrap;
    }

    /**
     * show a popup with one button to close
     * @param s Content of the popup
     */
    public void showPopup(String s){
        Stage popupStage = new Stage();

        Text text = new Text(s);
        text.setTextAlignment(TextAlignment.CENTER);
        HBox textBox = new HBox(text);

        Button btn = new Button("Close");
        btn.setOnAction((e)-> popupStage.close());

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(textBox, btn);

        Scene scene = new Scene(vBox);
        popupStage.setAlwaysOnTop(true);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
}
