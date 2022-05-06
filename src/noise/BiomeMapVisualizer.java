package noise;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Makes a colorful window displaying 3 layers of noise combined together as red green and blue values
 * for each pixel and rounded off to a set number of sections. Each distinct color or section can be
 * labeled as a distinct biome. This can serve as the base to further biome generation specialized to
 * each individual color region. There is also an option to move through the z axis to get a cool
 * morphing animation of changing color which can be the base to some really cool art.
 *
 * @author Benjamin Vick
 */
public class BiomeMapVisualizer extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;
    private PixelWriter pw;
    private final OpenSimplexNoise noise = new OpenSimplexNoise();

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        // if you want a smooth animation you will want these two values low
        scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setTitle("Biome Map");

        Canvas canvas = new Canvas(root.getWidth(), root.getHeight());
        pw = canvas.getGraphicsContext2D().getPixelWriter();
        root.getChildren().add(canvas);

        generate(0);

        AnimationTimer at = new AnimationTimer() {
            double offset = 0; // starting z position for the map
            @Override
            public void handle(long now) {
                generate(offset);
                offset += 0.03;// move the z position a little bit every time the map is completed
            }
        };
        at.start(); // shows a continuous animation of the map moving through the z axis.

        stage.show();
    }

    private void generate(double offset) {
        // The rate at which each new octave changes in effect to the total value.
        // You want this < 1 and > 0 for a biome map since you want finer details to have a smaller effect.
        double roughness = 0.35f;

        // Scales all your x y and z coordinates smaller or larger.
        double scale = 0.01f;

        // The distance between the 3 red green and blue layers in the noise. If this value is too small
        // the noise layers will be too similar though it can be useful to make rivers or lakes.
        double gap = 200;

        // The number of levels of noise mushed together to get the final image. Each layer is zoomed out twice
        // as far giving quicker and more fine details.
        int octaves = 4;

        // sections controls how many sections each color is split into
        // this is the number of sections per color, in reality there will be sections^3 individual colors
        // Sections currently have even width though because of the smooth nature of noise if you want
        // equal sized sections and equal frequency you will need to squish the values inward so the extremes
        // become as common as the average values. You can use this to your advantage to make rarer biomes or
        // code it out by squishing the values closer with a more complex function.
        double sections = 5;
        --sections; // This is for 0 based indexing.

        for (int y = 0; y < scene.getHeight(); y++) {
            for (int x = 0; x < scene.getWidth(); x++) {

                // scale values from -1 through 1 to 0 through 1
                double red = octavedNoise(octaves, roughness, x * scale, y * scale, offset) + 1;
                double green = octavedNoise(octaves, roughness, x * scale, y * scale, offset + gap) + 1;
                double blue = octavedNoise(octaves, roughness, x * scale, y * scale, offset + gap * 2) + 1;
                red /= 2;
                green /= 2;
                blue /= 2;

                /*
                // Multiply out the values of each color so that there are as many whole numbers in the range as
                // sections then round them to those values and divide back down to the 0-1 value range.
                red = Math.round(red * (sections)) / sections;
                green = Math.round(green * (sections)) / sections;
                blue = Math.round(blue * (sections)) / sections;
                //*/

                pw.setColor(x, y, new Color(red, blue, green, 1));
            }
        }
    }

    /**
     * Takes all the octaves of a point and combines them together based on the roughness.
     * Octaves are simply levels or layers of noise used on top of the primary value to
     * add extra variation and finer details. Roughness multiplies the weight of each
     * successive octave. If you choose 0.5 for instance the first octave will have a
     * weight of 1 and the second will be 0.5 next will be 0.25 then 0.125 followed by
     * 0.0625 and so on. Each octave grows in scale by a factor of 2 as well so that the
     * details are smaller and more fine on higher octaves. The end result will be scaled
     * to a number between -1 and 1.
     *
     * @param octaves Number layers of noise to combine into this values calculation.
     * @param roughness Rate at which the effect of each octave changes.
     * @param x Position on the x axis to get the value of.
     * @param y Position on the y axis to get the value of.
     * @param z Position on the z axis to get the value of.
     * @return The combined value of all the octaves at the given position.
     */
    private double octavedNoise(int octaves, double roughness, double x, double y, double z) {
        double val = 0;
        double max = 0;
        double octaveStrength = 1;
        double scale = 1;
        for (int i = 1; i <= octaves; i++) {
            val += noise.eval(scale * x, scale * y, scale * z) * octaveStrength;
            scale *= 2;
            max += octaveStrength;
            octaveStrength *= roughness;
        }
        return val / max;
    }
}

