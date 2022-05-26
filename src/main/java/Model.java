import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Model {

    public void sharpLib2(BufferedImage img) throws IOException {
        Mat kernel = new Mat(3, 3, CvType.CV_16SC1);
        kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
        Mat sharped = new Mat();
        Imgproc.filter2D(img2Mat(img), sharped, -1, kernel);
        BufferedImage result = (BufferedImage) HighGui.toBufferedImage(sharped);
        Utils.save(result, "result/sharpLib2", "result",  "jpg");
    }

    public void sharpLib1(BufferedImage img) throws IOException {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(img2Mat(img), blurred, new Size(0, 0), 3);
        Mat weighted = blurred.clone();
        Core.addWeighted(blurred, 1.5, weighted, -0.5, 0, weighted);
        BufferedImage result = (BufferedImage) HighGui.toBufferedImage(weighted);
        Utils.save(result, "result/sharpLib1", "result",  "jpg");
    }

    public BufferedImage sharp(BufferedImage img, int repeat) throws IOException {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), TYPE_INT_RGB);
        GaussianBlur blur = new GaussianBlur();
        for (int i = 0; i < repeat; i++) {
            result = sum(img, diff(img, blur.process(img)));
            img.setData(result.getData());
            Utils.save(result, "result/sharp", "result" + i,  "jpg");
        }
        return result;
    }

    public BufferedImage gaussian(BufferedImage img) throws IOException {
        BufferedImage result = new GaussianBlur().process(img);
        Utils.save(result, "result/gaussian", "result",  "jpg");
        return result;
    }

    public static BufferedImage diff(BufferedImage imgA, BufferedImage imgB) {
        return operation(imgA, imgB, -1);
    }

    public static BufferedImage sum(BufferedImage imgA, BufferedImage imgB) {
        return operation(imgA, imgB, 1);
    }

    public static BufferedImage operation(BufferedImage imgA, BufferedImage imgB, int sign) {
        if (sign >= 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int h = imgA.getHeight();
        int w = imgA.getWidth();
        BufferedImage result = new BufferedImage(w, h, TYPE_INT_RGB);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int colorA = imgA.getRGB(j, i);
                int colorB = imgB.getRGB(j, i);
                int r = Utils.ch1(colorA) + Utils.ch1(colorB) * sign;
                int g = Utils.ch2(colorA) + Utils.ch2(colorB) * sign;
                int b = Utils.ch3(colorA) + Utils.ch3(colorB) * sign;
                if (r < 0) r = 0;
                else if (r > 255) r = 255;
                if (g < 0) g = 0;
                else if (g > 255) g = 255;
                if (b < 0) b = 0;
                else if (b > 255) b = 255;
                result.setRGB(j, i, Utils.color(r, g, b));
            }
        }
        return result;
    }

    public static Mat img2Mat(BufferedImage image) {
        image = convertTo3ByteBGRType(image);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }

    public static BufferedImage convertTo3ByteBGRType(BufferedImage image) {
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        convertedImage.getGraphics().drawImage(image, 0, 0, null);
        return convertedImage;
    }
}
