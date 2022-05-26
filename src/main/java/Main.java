import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = getUrl();
        if (url == null) return;

        nu.pattern.OpenCV.loadShared();
        Model m = new Model();
        BufferedImage orig = ImageIO.read(url);
        BufferedImage blurred = m.gaussian(orig);
        m.sharp(blurred, 5);
        m.sharpLib1(blurred);
        m.sharpLib2(blurred);
    }

    private static URL getUrl() {
        return Main.class.getResource("img.jpg");
    }
}
