package simulatedWorld;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simulatedWorld.creatures.Creature;
import simulatedWorld.creatures.RedSlime;
import simulatedWorld.creatures.Slime;

import java.util.LinkedList;

public class Main extends Application {

    public static Text edible = new Text(0, 60, "");
    public static Text drinkable = new Text(0, 75, "");
    public static Text danger = new Text(0, 90, "");
    public static Text idle = new Text(0, 105, "");
    public static Text breedable = new Text(0, 120, "");
    public static Text green = new Text(0, 135, "");
    public static Text red = new Text(0, 150, "");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        /*
        for (int i = 0; i <= 20; i++) {
            System.out.print("{");
            for (int j = 0; j <= 20; j++) {
                System.out.print(Math.sqrt((i - 10) * (i - 10) + (j - 10) * (j - 10)) + ", ");
            }
            System.out.println("},");
        }
         */


        stage.setTitle("a little world");
        Pane root = new Pane();

        Canvas canvas = new Canvas(500, 300);
        root.getChildren().add(canvas);
        PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();

        root.getChildren().addAll(edible, drinkable, danger, idle, breedable, green, red);

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);

        World w = new World(pw);
        Player p = w.getPlayer();
        scene.setOnKeyPressed((e) -> {
            switch (e.getCode().toString().charAt(0)) {
                case 'W':
                    p.setU(true);
                    break;
                case 'D':
                    p.setR(true);
                    break;
                case 'S':
                    p.setD(true);
                    break;
                case 'A':
                    p.setL(true);
                    break;
                case 'F':
                    LinkedList<Creature> crts = w.getCreaturesNear(p.getX(), p.getY(), World.tileWidth);
                    for(Creature c : crts){
                        w.dead(c);
                    }
                    break;
                case 'G':
                    if(p.spectate != null){
                        p.spectate = null;
                    }else {
                        p.spectate = w.getCreaturesNear(p.getX(), p.getY(), World.tileWidth).getFirst();
                        if (p.spectate == null) {
                            drinkable.setText("");
                            edible.setText("");
                            danger.setText("");
                            idle.setText("");
                        }
                    }
                    break;
            }
        });
        scene.setOnKeyReleased((e) -> {
            switch (e.getCode().toString().charAt(0)) {
                case 'W':
                    p.setU(false);
                    break;
                case 'D':
                    p.setR(false);
                    break;
                case 'S':
                    p.setD(false);
                    break;
                case 'A':
                    p.setL(false);
                    break;
            }
        });

        stage.show();
        //stage.setFullScreen(true);



        new AnimationTimer() {
            @Override
            public void handle(long now) {
                canvas.setHeight(scene.getHeight());
                canvas.setWidth(scene.getWidth());
                w.update((int)scene.getWidth(), (int)scene.getHeight());
                green.setText(Slime.count + "");
                red.setText(RedSlime.count + "");
            }
        }.start();
    }
}
