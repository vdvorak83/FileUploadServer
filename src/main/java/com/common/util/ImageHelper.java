package com.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Created by Kirill Stoianov on 19/09/17.
 */
public class ImageHelper {

    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 100;

    // TODO: 20/09/17 https://stackoverflow.com/questions/991349/resize-image-while-keeping-aspect-ratio-in-java


    public static BufferedImage resizeImage(String path) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(path));
        return resizeImage(originalImage);
    }


    // TODO: 20/09/17 add size params
    public static BufferedImage resizeImage(BufferedImage originalImage){
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }
}
