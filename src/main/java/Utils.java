import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    public static int ch1(int color) {
        return (color & 0xff0000) >> 16;
    }

    public static int ch2(int color) {
        return (color & 0xff00) >> 8;
    }

    public static int ch3(int color) {
        return color & 0xff;
    }

    public static int color(int ch1, int ch2, int ch3) {
        return check(ch1) << 16 | check(ch2) << 8 | check(ch3);
    }

    public static int check(int color) {
        return color > 255 ? 255 : color & 0xff;
    }

    public static void save(BufferedImage bi, String path, String name, String format) throws IOException {
        Files.createDirectories(Paths.get(path).toAbsolutePath());
        ImageIO.write(bi, format, new File(path + "/" + name + "." + format));
    }
}
