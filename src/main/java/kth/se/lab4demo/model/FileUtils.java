package kth.se.lab4demo.model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

class FileUtils{
    static private void saveModelToFile(Model model, File file) {
        try{
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(model);
            out.close();
            fileOut.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void saveGameModel(Model model){
        //create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Same Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)","*.txt"));
        Stage fileChooserStage = new Stage();
        //show the save dialog
        File file = fileChooser.showSaveDialog(fileChooserStage);

        if(file != null){
            FileUtils.saveModelToFile(model, file);
        }
    }

    static private Model loadModelFromFile(File file){
        try{
            FileInputStream fileIN = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIN);
            return (Model) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Model loadGame(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)","*.txt"));
        Stage fileChooserStage = new Stage();
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if(file != null){
            return FileUtils.loadModelFromFile(file);
        }
        return null;
    }
}