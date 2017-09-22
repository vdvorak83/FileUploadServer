package com.common.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by Kirill Stoianov on 19/09/17.
 */
public class ImageHelper {


    public static final int SMALL_WIDTH = 400;
    public static final int SMALL_HEIGHT = 300;
    public static final int MEDIUM_WIDTH = 900;
    public static final int MEDIUM_HEIGHT = 600;
    public static final int BIG_WIDTH = 1024;
    public static final int BIG_HEIGHT = 800;


    public static BufferedImage resizeImage(String path,int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(path));
        return resizeImage(originalImage,width,height);
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage resizedImage = new BufferedImage(width,height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }


    /**
     * Resize with save aspect ratio.
     *
     * @param inputImage
     * @param resultWidth
     * @param resultHeight
     * @return
     */
//    public static BufferedImage resizeWithAspectRatio(BufferedImage inputImage, int resultWidth, int resultHeight, boolean resizeIfSmaller) {
//        // first get the width and the height of the image
//        int originWidth = inputImage.getWidth();
//        int originHeight = inputImage.getHeight();
//
//        // let us check if we have to scale the image
//        if (!resizeIfSmaller && (originWidth <= resultWidth && originHeight <= resultHeight)) {
//            // we don't have to scale the image, just return the origin
//            return inputImage;
//        }
//
//        // Scale in respect to width or height?
//        Scalr.Mode scaleMode = Scalr.Mode.AUTOMATIC;
//
//        // find out which side is the shortest
//        int maxSize = 0;
//        if (originHeight > originWidth) {
//            // scale to width
//            scaleMode = Scalr.Mode.FIT_TO_WIDTH;
//            maxSize = resultWidth;
//        } else if (originWidth >= originHeight) {
//            scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
//            maxSize = resultHeight;
//        }
//
//        // Scale the image to given size
//        BufferedImage outputImage = Scalr.resize(inputImage, Scalr.Method.QUALITY, scaleMode, maxSize);
//
//        // okay, now let us check that both sides are fitting to our result scaling
//        if (scaleMode.equals(Scalr.Mode.FIT_TO_WIDTH) && outputImage.getHeight() > resultHeight) {
//            // the height is too large, resize again
//            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, resultHeight);
//        } else if (scaleMode.equals(Scalr.Mode.FIT_TO_HEIGHT) && outputImage.getWidth() > resultWidth) {
//            // the width is too large, resize again
//            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, resultWidth);
//        }
//
//
//        // now we have an image that is definitely equal or smaller to the given size
//        // Now let us check, which side needs black lines
//        int paddingSize = 0;
//        if (outputImage.getWidth() != resultWidth) {
//            // we need padding on the width axis
//            paddingSize = (resultWidth - outputImage.getWidth()) / 2;
//        } else if (outputImage.getHeight() != resultHeight) {
//            // we need padding on the height axis
//            paddingSize = (resultHeight - outputImage.getHeight()) / 2;
//        }
//
//        // we need padding?
//        if (paddingSize > 0) {
//            // add the padding to the image
//            outputImage = Scalr.pad(outputImage, paddingSize, new Color(0, 0, 0, 0));
//
//            // now we have to crop the image because the padding was added to all sides
//            int x = 0, y = 0, width = 0, height = 0;
//            if (outputImage.getWidth() > resultWidth) {
//                // set the correct range
//                x = paddingSize;
//                y = 0;
//                width = outputImage.getWidth() - (2 * paddingSize);
//                height = outputImage.getHeight();
//            } else if (outputImage.getHeight() > resultHeight) {
//                // set the correct range
//                x = 0;
//                y = paddingSize;
//                width = outputImage.getWidth();
//                height = outputImage.getHeight() - (2 * paddingSize);
//            }
//
//            // Crop the image
//            if (width > 0 && height > 0) {
//                outputImage = Scalr.crop(outputImage, x, y, width, height);
//            }
//        }
//
//        // flush both images
//        inputImage.flush();
//        outputImage.flush();
//
//        // return the final image
//        return outputImage;
//    }
    public static BufferedImage resizeWithAspectRatio(BufferedImage inputImage, int resultWidth, int resultHeight, boolean resizeIfSmaller) {
       // first get the width and the height of the image
        int originWidth = inputImage.getWidth();
        int originHeight = inputImage.getHeight();

//         let us check if we have to scale the image
        if (!resizeIfSmaller && (originWidth <= resultWidth && originHeight <= resultHeight)) {
            // we don't have to scale the image, just return the origin
            return inputImage;
        }

        // Scale in respect to width or height?
        Scalr.Mode scaleMode = Scalr.Mode.AUTOMATIC;

        // find out which side is the shortest
        int maxSize = 0;
        if (originHeight > originWidth) {
            // scale to width
            scaleMode = Scalr.Mode.FIT_TO_WIDTH;
            maxSize = resultWidth;
        } else if (originWidth >= originHeight) {
            scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
            maxSize = resultHeight;
        }

        // Scale the image to given size
        BufferedImage outputImage = Scalr.resize(inputImage, Scalr.Method.QUALITY, scaleMode, maxSize);

        // okay, now let us check that both sides are fitting to our result scaling
        if (scaleMode.equals(Scalr.Mode.FIT_TO_WIDTH) && outputImage.getHeight() > resultHeight) {
            // the height is too large, resize again
            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, resultHeight);
        } else if (scaleMode.equals(Scalr.Mode.FIT_TO_HEIGHT) && outputImage.getWidth() > resultWidth) {
            // the width is too large, resize again
            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, resultWidth);
        }

        // now we have an image that is definitely equal or smaller to the given size
        // Now let us check, which side needs black lines
        int paddingSize = 0;
        if (outputImage.getWidth() != resultWidth) {
            // we need padding on the width axis
            paddingSize = (resultWidth - outputImage.getWidth()) / 2;
        } else if (outputImage.getHeight() != resultHeight) {
            // we need padding on the height axis
            paddingSize = (resultHeight - outputImage.getHeight()) / 2;
        }

        // we need padding?
        if (paddingSize > 0) {
            // add the padding to the image
            outputImage = Scalr.pad(outputImage, paddingSize);

            // now we have to crop the image because the padding was added to all sides
            int x = 0, y = 0, width = 0, height = 0;
            if (outputImage.getWidth() > resultWidth) {
                // set the correct range
                x = paddingSize;
                y = 0;
                width = outputImage.getWidth() - (2 * paddingSize);
                height = outputImage.getHeight();
            } else if (outputImage.getHeight() > resultHeight) {
                // set the correct range
                x = 0;
                y = paddingSize;
                width = outputImage.getWidth();
                height = outputImage.getHeight() - (2 * paddingSize);
            }

            // Crop the image
            if (width > 0 && height > 0) {
                outputImage = Scalr.crop(outputImage, x, y, width, height);
            }
        }

        // flush both images
        inputImage.flush();
        outputImage.flush();

        // return the final image
        return outputImage;
    }

    public static String getFileExtension(String name) {
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return null;
        }
    }

}
