import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.*;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Dmitry on 15.11.2015.
 */
public class BTree extends Application {
    public static Field[] jsonObject;
    public static Field[] jsonObject1;
    public static Tree T;
    public static TreeView<String> treeView;
    public TextField tfA;
    public TextField tfB;
    public TextArea tfC;
    public Button A, B, C;
    Pane rootNode;
    String pathWord;
    MultipleSelectionModel<TreeItem<String>> tvSelModel;
    String path;
    public static void main(String args[]) throws Exception
    {
        T = new Tree();
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
        /*List < Item > description = Arrays.asList(new Item("dfsdfs"), new Item("dfsdfs dsfs"), new Item("dfsdfs fdgdsfg sefsd"));
        T.insert(7, new Field("www.github.com", "fsdfsd", description));
        T.insert(2, new Field("www.google.com", "fsdffdsfssd", description));
        T.insert(1, new Field("www.yan
        dex.ru", "fsdsdfsd", description));
        T.insert(17, new Field("www.habrahabr.ru", "fsddsffsd", description));*/
        Gson gson = new Gson();
        jsonObject = gson.fromJson(new FileReader("KeepNote.json"), Field[].class);
        //System.out.println(gsonPretty.toJson(jsonObject));
        for (int i = 0; i < jsonObject.length; i++) {
            T.insert(jsonObject[i].name.hashCode(), jsonObject[i]);
        }
        /*T.insert(8);
        T.insert(3);
        T.insert(4);
        T.insert(5);
        T.insert(6);
        T.insert(9);
        T.insert(11);
        T.insert(20);
        T.insert(15);
        T.insert(18);
        T.insert(25);
        T.insert(50);*/
        T.print();
        int size = 50;
        HashTable H = new HashTable(size, 5);
        //System.out.println(gsonPretty.toJson(jsonObject));
        for (int i = 0; i < jsonObject.length; i++) {
            DataItem data = new DataItem(jsonObject[i].name.hashCode(), jsonObject[i]);
            H.insert(data);
        }
        H.displayTable();
        launch(args);
    }
    public void fileWrite()
    {
        TreeItem<String> child[] = new TreeItem[jsonObject.length + 1];
        TreeItem<String> tiRoot = new TreeItem("Root");
        int iter = 0;

        for (int i = 0; i < jsonObject.length; i++) {
            child[iter] = new TreeItem<String>(jsonObject[i].name);
            tiRoot.getChildren().add(child[iter]);
            Field f = T.search(jsonObject[i].name.hashCode());
            //child[iter].getChildren().add(new TreeItem<String>(f.date));
            if(f != null) {
                for (int j = 0; j < f.description.size(); j++) {
                    TreeItem<String> s = new TreeItem<String>(f.description.get(j).string);
                    child[iter].getChildren().add(s);
                }
            }
            iter++;
        }
        treeView.setVisible(false);
        treeView = new TreeView<String>(tiRoot);
        treeView.setMinSize(400, 900);
        rootNode.getChildren().addAll(treeView);
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("KeepNote.json", false)) {
            writer.write(gsonPretty.toJson(jsonObject));
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
        tvSelModel =
                treeView.getSelectionModel();
        treeView.setMinSize(400, 800);
        tvSelModel.selectedItemProperty().addListener(
                new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> changed,
                                        TreeItem<String> oldVal, TreeItem<String> newVal) {
                        if(newVal != null)
                        {
                            path = newVal.getValue();
                            pathWord = path;
                            TreeItem<String> tmp = newVal.getParent();
                            while(tmp != null) {
                                path = tmp.getValue() + " -> " + path;
                                tmp = tmp.getParent();
                            }
                            String str = "";
                            if(T.search(newVal.getValue().hashCode()) != null)
                                str  = T.search(newVal.getValue().hashCode()).date;
                            response.setText("Selection is " + newVal.getValue() + "\nComplete path is " + path + "\n" + str);// + (T.search(newVal.getValue().hashCode())).date);
                        }
                    }
                });
        //rootNode.getChildren().addAll(treeView,response, A, B, C, tfA, tfB, tfC, response1, response2, response3);
    }

    Label response, response1, response2, response3;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Notebook");
        rootNode = new Pane();
        rootNode.setLayoutX(0);
        rootNode.setLayoutY(0);
        //rootNode.setAlignment(Pos.BASELINE_LEFT);
        Scene myScene = new Scene(rootNode, 1100, 800);
        myScene.getStylesheets().add
                (Tree.class.getResource("Styles.css").toExternalForm());
        primaryStage.setScene(myScene);
        response = new Label("No selection");
        response.setLayoutX(450);
        response.setLayoutY(700);

        A = new Button("Add note");
        B = new Button("Delete note");
        C = new Button("Exit");

        A.setLayoutX(750);
        A.setLayoutY(100);
        A.setMinWidth(180);
        A.setMinHeight(50);

        B.setLayoutX(750);
        B.setLayoutY(180);
        B.setMinWidth(180);
        B.setMinHeight(50);

        C.setLayoutX(750);
        C.setLayoutY(260);
        C.setMinWidth(180);
        C.setMinHeight(50);

        response1 = new Label("Name : ");
        response1.setLayoutX(600);
        response1.setLayoutY(400);

        response2 = new Label("Date : ");
        response2.setLayoutX(600);
        response2.setLayoutY(450);

        response3 = new Label("Description : ");
        response3.setLayoutX(600);
        response3.setLayoutY(500);

        tfA = new TextField();
        tfA.setLayoutX(750);
        tfA.setLayoutY(400);
        tfA.setPrefColumnCount(15);

        tfB = new TextField();
        tfB.setLayoutX(750);
        tfB.setLayoutY(450);
        tfB.setPrefColumnCount(15);

        tfC = new TextArea();
        tfC.setPrefRowCount(5);
        tfC.setPrefColumnCount(14);
        tfC.setLayoutX(750);
        tfC.setLayoutY(500);

        TreeItem<String> tiRoot = new TreeItem("Root");
        TreeItem<String> child[] = new TreeItem[jsonObject.length];
        int iter = 0;

        for (int i = 0; i < jsonObject.length; i++) {
            child[iter] = new TreeItem<String>(jsonObject[i].name);
            tiRoot.getChildren().add(child[iter]);
            Field f = T.search(jsonObject[i].name.hashCode());
            //child[iter].getChildren().add(new TreeItem<String>(f.date));
            for(int j = 0; j < f.description.size(); j++)
            {
                TreeItem<String> s = new TreeItem<String>(f.description.get(j).string);
                child[iter].getChildren().add(s);
            }
            iter++;
        }

        treeView = new TreeView<String>(tiRoot);

        A.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (T.search(tfA.getText().hashCode()) == null && tfA.getText().length() != 0 && tfB.getText().length() != 0 && tfC.getText().length() != 0) {
                    List<Item> description = Arrays.asList(new Item(tfC.getText()));
                    TreeItem<String> tiRoot = new TreeItem("Root");
                    Field textField = new Field(tfA.getText(), tfB.getText(), description);
                    T.insert(textField.name.hashCode(), textField);
                    TreeItem<String> child[] = new TreeItem[jsonObject.length + 1];
                    int iter = 0;
                    jsonObject1 = new Field[jsonObject.length + 1];
                    for (int i = 0; i < jsonObject.length; i++) {
                        jsonObject1[i] = jsonObject[i];
                    }
                    jsonObject1[jsonObject.length] = textField;
                    jsonObject = jsonObject1;
                    fileWrite();
                    T.print();
                }
            }
        });

        B.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(pathWord != "" && pathWord != "Root")
                {
                    T.remove(pathWord.hashCode());
                    jsonObject1 = new Field[jsonObject.length - 1];
                    String text = pathWord;
                    for (int i = 0, j = 0; i < jsonObject.length; i++, j++) {
                        if((jsonObject[i].name).compareTo(text) != 0)
                            jsonObject1[j] = jsonObject[i];
                        else
                            j--;
                    }
                    jsonObject = jsonObject1;
                }
                fileWrite();
                T.print();

            }
        });

        C.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });

        tvSelModel =
                treeView.getSelectionModel();
        treeView.setMinSize(400, 800);
        tvSelModel.selectedItemProperty().addListener(
                new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> changed,
                                TreeItem<String> oldVal, TreeItem<String> newVal) {
                            if(newVal != null)
                            {
                                path = newVal.getValue();
                                pathWord = path;
                                TreeItem<String> tmp = newVal.getParent();
                                while(tmp != null) {
                                    path = tmp.getValue() + " -> " + path;
                                    tmp = tmp.getParent();
                                }
                                String str = "";
                                if(T.search(newVal.getValue().hashCode()) != null)
                                    str  = T.search(newVal.getValue().hashCode()).date;
                                response.setText("Selection is " + newVal.getValue() + "\nComplete path is " + path + "\n" + str);// + (T.search(newVal.getValue().hashCode())).date);
                            }
                        }
                    });
        rootNode.getChildren().addAll(treeView,response, A, B, C, tfA, tfB, tfC, response1, response2, response3);

        primaryStage.show();

    }
}