package com.example.blog.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    private static final int PREF_WIDTH = 900;
    private static final int PREF_HEIGHT = 300;
    private static final String EXTENSION = "JPG";

    public static byte[] createImageForPost(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        BufferedImage scaledImage = scaleImage(image, PREF_WIDTH, PREF_HEIGHT);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, EXTENSION, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        double aspectRatio = (double) image.getWidth() / (double) image.getHeight();
        double targetRatio = (double) newWidth / (double) newHeight;

        if(targetRatio < aspectRatio) {
            newHeight   = (int) (newWidth / aspectRatio);
        } else {
            newWidth    = (int) (newHeight * aspectRatio);
        }

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
        return newImage;
    }
}
