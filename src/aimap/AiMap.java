package aimap;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import javafx.animation.Animation;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author aboshady
 */
public class AiMap extends Application {

    ToggleGroup aGroup, gGroup;
    RadioButton aR[], gR[];
    ComboBox from, to;
    TextArea text;
    TitledPane gPane, aPane, cPane;
    Button search, load, exit;
    FileChooser fileChooser;
    ImageView imgMap;
    StackPane centerPane;
    Pane marioPane;
    VBox gBox, aBox, rightBox, cBox;
    BorderPane root;
    Map map;
    City city;
    String last1, last2;
    int g1, g2;
    SequentialTransition animation;
    String[] answer;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initializeComponents();
        // setActions();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.setTitle("AI Project");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> showExitScreen());

    }

    public void initializeComponents() throws MalformedURLException, Exception {
        imgMap = new ImageView(new Image("images/map.png"));
        imgMap.setFitWidth(1650); //was 1500
        imgMap.setPreserveRatio(true);
        aGroup = new ToggleGroup(); //algorithms
        gGroup = new ToggleGroup();// governorates
        RadioButton[] aR = new RadioButton[4], gR = new RadioButton[3];//
        ComboBox from = new ComboBox(), to = new ComboBox();
        from.setStyle("-fx-pref-width: 120;");
        to.setStyle("-fx-pref-width: 100;");
        from.setPromptText("Source: ");
        to.setPromptText("Goal: ");
        text = new TextArea();
        text.setEditable(false);
        text.setPrefSize(50, 100);
        text.setStyle("-fx-font-size:20;-fx-font-weight:bold;");

        search = new Button("Seach");
        search.setStyle("-fx-color:rgb(1,91,181);-fx-font-weight:bold;");
        search.setPrefSize(80, 20);
        search.setDisable(true);
        exit = new Button("Exit");
        //file browser
        load = new Button("Load data");
        load.setStyle("-fx-color:rgb(183, 47, 47);-fx-font-weight:bold;");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select data file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        for (int i = 0; i < 4; i++) {
            aR[i] = new RadioButton();
            aR[i].setToggleGroup(aGroup);
        }
        for (int i = 0; i < 3; i++) {
            gR[i] = new RadioButton();
            gR[i].setToggleGroup(gGroup);
        }
        aR[0].setText("BFS");
        aR[1].setText("DFS");
        aR[2].setText("A star");
        aR[3].setText("Best First Search");

        gR[0].setText("El-menoufia");
        gR[1].setText("El-gharbia");
        gR[2].setText("El-qalubia");
        aR[0].setUserData("BFS");
        aR[1].setUserData("DFS");
        aR[2].setUserData("A_STAR");
        aR[3].setUserData("BEST_FIRST_SEARCH");
        aR[0].setSelected(true);
        gR[0].setUserData(1);
        gR[1].setUserData(0);
        gR[2].setUserData(2);
        aBox = new VBox(20, aR);// to contain algorithms
        gBox = new VBox(20, gR);//// governorates
        cBox = new VBox(20, new HBox(10, new Label("From"), from), new HBox(26, new Label("To"), to));
        gPane = new TitledPane("GOVERNORATES", gBox);
        cPane = new TitledPane("CITY", cBox);
        aPane = new TitledPane("Algorithm", aBox);
        gPane.setExpanded(false);
        cPane.setExpanded(false);
        aPane.setExpanded(false);
        rightBox = new VBox(20, gPane, cPane, aPane, text, new HBox(20, search, load));
        rightBox.setPadding(new Insets(10, 10, 10, 10));
        marioPane = new Pane();
        centerPane = new StackPane();
        centerPane.getChildren().addAll(imgMap, marioPane);
        root = new BorderPane(null, null, rightBox, null, centerPane);
        root.setStyle("-fx-background-image: url(images/background.png);"
                + "-fx-background-repeat: no-repeat;");
        

        animation = new SequentialTransition();
        //setActions
        gGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (gGroup.getSelectedToggle() != null) {
                    city = new City((Integer) gGroup.getSelectedToggle().getUserData());

                    if (from.getValue() != null) {
                        last1 = from.getValue().toString();
                    }
                    if (to.getValue() != null) {
                        last2 = to.getValue().toString();
                    }
                    from.getItems().clear();
                    to.getItems().clear();
                    if (last1 != null) {
                        from.setPromptText(last1);
                        from.setValue(last1);

                    }

                    if (last2 != null) {
                        to.setPromptText(last2);

                        to.setValue(last2);

                    }

                    from.getItems().addAll(city.getCity());
                    to.getItems().addAll(city.getCity());

                }

            }
        }
        );
        search.setOnAction(e -> {

            if (animation.getStatus() == Animation.Status.RUNNING) {
                animation.stop();
            }
            if (from.getValue() == null || to.getValue() == null) {
                text.appendText("Invalid from or to\n");
                return;
            }
            String[] cities = map.getCitiesNames();
            int fromIndex = Arrays.asList(cities).indexOf(from.getValue().toString());
            int toIndex = Arrays.asList(cities).indexOf(to.getValue().toString());

            System.out.println(toIndex + " " + fromIndex + " " + g1 + " " + g2);
            double[] points = map.search(fromIndex, toIndex, Map.Type.valueOf(aGroup.getSelectedToggle().getUserData().toString()));
            if (points == null) {
                return;
            }
            answer = map.getAnswer();
            setAnimation(animation, points);
            text.appendText(answer[0] + '\n');
            if (points.length > 2) {
                animation.play();
            }

        });

        load.setOnAction((ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(null);
            try {
                map = new Map(file);
            } catch (Exception ex) {
                text.appendText("File error!\n");

                return;
            }
            search.setDisable(false);
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showExitScreen() {
        Text text = new Text(25, 245, "THANKS"), text2 = new Text(" Ahmed Ali\t Ahmed Ibraheim");
        text.setFont(new Font(Font.getFontNames().get(3), 200));
        text2.setStyle("fx-background-color:white;-fx-fill:rgb(1,91,181);-fx-font-size:30;"
                + "-fx-font-weight:bold;");
        text.setStyle("-fx-stroke:white;-fx-stroke-width:8;-fx-fill:rgb(0, 176, 226);");

        Pane pane = new Pane(text);
        pane.setStyle("-fx-background-color:rgb(1,91,181);"); //0 176 226
        BorderPane b = new BorderPane(pane, null, null, text2, null);
        BorderPane.setAlignment(text2, Pos.CENTER);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5)));
        timeline.play();
        Stage s2 = new Stage(StageStyle.UNDECORATED);
        s2.setScene(new Scene(b, 600, 380));
        s2.show();
        timeline.setOnFinished(e2 -> s2.close());
    }
    //---

    public void setAnimation(SequentialTransition animation, double[] points) {
        ImageView imgGif = new ImageView(new Image("images/man.gif"));
        imgGif.setFitHeight(240);
        imgGif.setFitWidth(240);
        Color color = Color.rgb(183, 47, 47);
        marioPane.getChildren().clear(); //not here
        text.clear();
        int totalAnimationTime = points.length / 2 * 800; // millis
        //double[]points = new double[]{0,0, 900, 100,400,500,0,500,989.0, 219.0,200,200,0,500,778.0, 575.0} ; //parameter
        animation.getChildren().clear();
        int numPaths = points.length / 2 - 1;
        if (numPaths == 0) {
            return;
        }
        int time = totalAnimationTime / numPaths;
        for (int i = 2; i < points.length - 1; i += 2) {
            MoveTo moveTo = new MoveTo(points[i - 2], points[i - 1]);
            LineTo lineTo = new LineTo(points[i], points[i + 1]);
            Path path = new Path(moveTo, lineTo);
            PathTransition pathTransition = new PathTransition(Duration.millis(time), path, imgGif);
            if (i == 2) {
                double[] arr = getImageStatus(points[0], points[1], points[2], points[3]);
                imgGif.setScaleX(arr[0]);
                imgGif.setRotate(180 * arr[1] / Math.PI);
            }
            int p = i / 2;
            pathTransition.setOnFinished(e -> {
                text.appendText(answer[p] + "\n");//print the city reached
                Line l = new Line(points[p * 2 - 2], points[p * 2 - 1], points[p * 2], points[p * 2 + 1]);
                l.setStrokeWidth(4);
                l.setStroke(color);
                marioPane.getChildren().add(0, l);
                Circle c = new Circle(points[p * 2], points[p * 2 + 1], 7, color);
                marioPane.getChildren().add(0, c);
                if (p != points.length / 2 - 1) {
                    double[] arr = getImageStatus(points[2 * p], points[2 * p + 1], points[2 * p + 2], points[2 * p + 3]);
                    imgGif.setScaleX(arr[0]);
                    imgGif.setRotate(180 * arr[1] / Math.PI);
                }
            });

            animation.getChildren().add(pathTransition);
        }
        animation.setOnFinished(e2 -> marioPane.getChildren().remove(imgGif));
        if (!marioPane.getChildren().contains((ImageView) imgGif)) {
            marioPane.getChildren().add(imgGif);
        }

    }

    //GIF rotation 
    double[] getImageStatus(double... p) {
        double arr[] = new double[2];
        arr[0] = (p[0] - p[2] > 0) ? -1 : 1;
        double tan = (p[3] - p[1]) / (p[2] - p[0]);
        arr[1] = (tan < 0) ? -Math.atan(-tan) : Math.atan(tan);
        return arr;
    }

}

class City {

    String[] Elmnofia;
    String[] Elqaluobya;
    String[] Elgharbya;
    int Governorate;

    City(int Governorate) {
        this.Governorate = Governorate;

    }

    public String[] getCity() {
        if (Governorate == 1) {
            String[] Elmnofia = {"Tala",
                "Berkt el-sab3",
                "El-shohada",
                "Shebin el-kom",
                "Quisna",
                "Menouf",
                "Sirs allyan",
                "El-bagor",
                "Ashmon",
                "El-sadat"};
            return Elmnofia;
        } else if (Governorate == 2) {
            String[] Elqaluobya = {
                "Kafr shokr",
                "Banha",
                "Tokh",
                " El-kanater",
                "Qalub",
                "Shebin el-kanater",
                "El-khanka",
                "Shoubra"};
            return Elqaluobya;
        } else {
            String[] Elgharbya = {
                "Basion",
                "Ktor",
                "El-mahlla",
                "Samanod",
                "Kafr el-ziat",
                "Tanta",
                "Santa",
                "Zefta"};
            return Elgharbya;

        }

    }
}
