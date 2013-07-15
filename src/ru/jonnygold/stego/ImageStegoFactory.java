/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import ru.jonnygold.wavelet.Signal;
import ru.jonnygold.wavelet.SignalFactory;
//import sun.security.jca.GetInstance;

/**
 *
 * @author Vanchpuck
 */
class ImageStegoFactory implements StegoFactory{

    private static ImageStegoFactory factory;

    private ImageStegoFactory(){}

    public static ImageStegoFactory getInstance(){
        if(factory == null){
            return new ImageStegoFactory();
        }
        return factory;
    }

    @Override
    public Stego createStego(File file) throws IOException {

        // BufferedImage bi = ImageIO.read(file);
        // List<Signal> sl = new ArrayList<Signal>();
        // sl.addAll(getSignalList(bi));

            // File type determination
        String type = getFileExt(file);

            if(!type.equalsIgnoreCase("bmp") && !type.equalsIgnoreCase("png") && !type.equalsIgnoreCase("jpg") ){
                    throw new IOException("Формат файла не поддерживается");
            }

        // Initialize List to hold amount of Signals
        List<Signal> signalList = new ArrayList<Signal>();


        // Prepare reader
        // ImageReader reader = ImageIO.getImageReadersByFormatName(getFileExt(file)).next(); 
        // ImageInputStream inStream = ImageIO.createImageInputStream(file); 
        // reader.setInput(inStream);

        // Read all images from file
        /*** НА ДАННОМ ЭТАПЕ ПИШЕМ ТОЛЬКО В ПЕРВОЕ ИЗОБРАЖЕНИЕ ***/
        BufferedImage image = ImageIO.read(file);

        int imgType = image.getType();
        if(
                    imgType != BufferedImage.TYPE_3BYTE_BGR
            &&	imgType != BufferedImage.TYPE_4BYTE_ABGR
            &&	imgType != BufferedImage.TYPE_4BYTE_ABGR_PRE
            &&	imgType != BufferedImage.TYPE_BYTE_GRAY
        ){
            throw new IOException("Inappropriate image type. Only 24, 32 bit images and grayscale images are desired.");
        }

        signalList.addAll(getSignalList(image));

        // Create and return stego
        return new Stego(signalList);
    }

    public void putStego(File file, Stego stego) throws IOException{

        // Image container retrieving
        BufferedImage image = ImageIO.read(file);

        // Convert JPG into PNG (JPG in not supported yet)
        String fileExt = getFileExt(file);
        if(getFileExt(file).equalsIgnoreCase("JPG")) {
            fileExt = "PNG"; 
        }


        putSignal(image, stego.getSignalList());

        // if(getFileExt(file).equals("JPG")) {
        // 	file.renameTo(new File(getFileName(file)+".png"));
        // }

        ImageIO.write(image, fileExt, file);


        if(getFileExt(file).equals("JPG")) {
            file.renameTo(new File(getFileName(file)+".png"));
        }

    }

    private List<Signal> getSignalList(BufferedImage image){
        /***
        * ДОБАВИТЬ ПРОВЕРКУ ТИПА ИЗОБРАЖЕНИЯ!!!
        */
        // Initialize List for Signals storing
        List<Signal> signalList = new ArrayList<Signal>();

        int width = image.getWidth();
        int height = image.getHeight();

        Raster raster = image.getRaster();


        // Get signal from color band component and add into the List of Signals
        double[] data = new double[width * height]; 
        for(int i=0; i<raster.getNumBands(); i++){
            //System.out.println(" Get color band "+i);
            data = new double[width * height];
            raster.getSamples(0, 0, width, height, i, data);

            signalList.add(SignalFactory.createSignal2D(data, height, width));
        }

        return signalList;
    }

    private void putSignal(BufferedImage image, List<Signal> signalList){

        WritableRaster raster = image.getRaster();

        if(raster.getNumBands() != signalList.size()){
            throw new IllegalStateException("Неподходящее изображение");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        //System.out.println("signalList.size(): "+signalList.size());
        double[] data;
        for(int i=0; i<signalList.size(); i++){
            /*** ДОБАВИТЬ ПРОВЕРКУ СООТВЕТСТВИЯ РАЗМЕРОВ ***/
            data = signalList.get(i).getData();
            normalize(data);
            raster.setSamples(0, 0, width, height, i, data);
        }
    }

    private void normalize(double[] data){
        for(int i=0; i<data.length; i++){
            if(data[i] < 0) data[i] = 0;//Math.abs(data[i]);
            if(data[i] > 255) data[i] = 255;
        }
    }

    private String getFileExt(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).toUpperCase();
    }

    private String getFileName(File file){
        String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
