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
import kth.se.lab4demo.model.Model;


public class View {
    private static View instance = null;
    private int currentActiveBtn = 0;
    private static final int GRID_SIZE = 9;
    private static final int SECTIONS_PER_ROW = 3;
    private static final int SECTION_SIZE = 3;

    private final Label[][] numberTiles; // the tiles/squares to show in the ui grid
    private final VBox numberPane;
    private Stage primaryStage;

    private View() {
        numberTiles = new Label[GRID_SIZE][GRID_SIZE];
        initNumberTiles();
        numberPane = createNumberPane();
        initView();
    }

    public int getCurrentActiveBtn(){
        return this.currentActiveBtn;
    }
    public void setCurrentActiveBtn(int currentIndex){
        this.currentActiveBtn = currentIndex;

        Scene scene = primaryStage.getScene();
        BorderPane b = (BorderPane) scene.getRoot();
        VBox vBox =  (VBox) b.getRight();

        for (int i = 0; i < 10; i++) {
            Button button = (Button) vBox.getChildren().get(i);
            if (this.currentActiveBtn == i){
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

    public static View getInstance(){
        if(instance == null)
            instance = new View();
        return instance;
    }


    private void initView(){
        this.primaryStage = new Stage();
        this.primaryStage.setTitle("Suduku");
        this.primaryStage.setOnCloseRequest(windowEvent -> {
            Model model = Model.getInstance();
            if(model.isSaved())
                primaryStage.close();
            else{
                showSavedRequest();
            }
        });

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(numberPane);

        VBox leftPanel = createLeftPanel();
        borderPane.setLeft(leftPanel);

        VBox rightPanel = createRightPanel();
        borderPane.setRight(rightPanel);

        MenuBar menuBar = createMenybar();
        borderPane.setTop(menuBar);

        Scene root = new Scene(borderPane);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    //kth.se.lab4demo.view
    public void view(int [][][] stage){

        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
               if(stage[y][x][1] == 0){
                   numberTiles[y][x].setText("");
               }else{
                   String value = String.valueOf(stage[y][x][1]);
                   numberTiles[y][x].setText(value);
               }

               if(stage[y][x][0] != 0)
                    numberTiles[y][x].setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
               else
                   numberTiles[y][x].setFont(Font.font("Monospaced", FontWeight.SEMI_BOLD, 20));
            }
        }
    }

    public void showSavedRequest(){
        Text text = new Text("Do you want to save?");
        text.setTextAlignment(TextAlignment.CENTER);
        HBox textBox = new HBox(text);


        Button exitButton = new Button("EXIT");
        exitButton.setOnAction(new SavedRequestBtnHandler(primaryStage,"EXIT"));

        Button saveButton = new Button("SAVE");
        saveButton.setOnAction(new SavedRequestBtnHandler(primaryStage,"SAVE"));

        HBox btnBox = new HBox();
        btnBox.setSpacing(30);
        btnBox.getChildren().add(exitButton);
        btnBox.getChildren().add(saveButton);
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

    private MenuBar createMenybar(){
        MenuBar menuBar = new MenuBar();

        //File
        final Menu filesMeny = new Menu("Files");

        MenuItem loadGameItem = new MenuItem("Load game");
        loadGameItem.setOnAction(new FileController("LOAD_GAME"));

        MenuItem saveGameItem = new MenuItem("Save game");
        saveGameItem.setOnAction(new FileController("SAVE_GAME"));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(new FileController("EXIT_GAME"));

        filesMeny.getItems().addAll(loadGameItem, saveGameItem, exitItem);

        //game
        final Menu gameMeny = new Menu("Game");
        Menu newGameSubItem = new Menu("New game");
        MenuItem easyGameSubItem = new MenuItem("Easy");
        MenuItem mediumGameSubItem = new MenuItem("Medium");
        MenuItem hardGameSubItem = new MenuItem("Hard");
        newGameSubItem.getItems().addAll(easyGameSubItem,mediumGameSubItem, hardGameSubItem);
        gameMeny.getItems().add(newGameSubItem);

        final Menu helpMeny = new Menu("Help");
        MenuItem ruleItem = new MenuItem("Rule");
        MenuItem howItem = new MenuItem("How to play");
        helpMeny.getItems().addAll(ruleItem,howItem);

        menuBar.getMenus().addAll(filesMeny, gameMeny,helpMeny );
        return menuBar;
    }

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

    public void viewCheckResult(boolean isCorrect) {
        String msg = "Uncorrect";
        if (isCorrect) {
            msg = "Correct";
        }


        Text text = new Text(msg);
        VBox vbox = new VBox(text);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setHeight(200);
        stage.setWidth(300);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createRightPanel (){
        VBox rightPanel = new VBox();

        rightPanel.getChildren().addAll();
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setSpacing(2);
        rightPanel.setPadding(new Insets(10));

        for (int i = 0; i < 10; i++) {
            String titel = i < 9 ? String.valueOf(i+1) : "C";
            Button btn = new Button(titel);
            rightPanel.getChildren().add(btn);

            RightBtnController controller = new RightBtnController(i);
            btn.setOnAction(controller);
        }

        return rightPanel;
    }

    // called by constructor (only)
    private void initNumberTiles() {


        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Label tile = new Label(/* add number, or "", to display */); // data from kth.se.lab4demo.model
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
}
