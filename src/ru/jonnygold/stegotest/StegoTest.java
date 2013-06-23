/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stegotest;

import com.sun.image.codec.jpeg.JPEGEncodeParam;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import ru.jonnygold.stego.*;
import ru.jonnygold.wavelet.*;

/**
 *
 * @author Vanchpuck
 */
public class StegoTest {

    String fileName;
    byte[] fileData;
    
    public StegoTest() throws IOException, NoSuchAlgorithmException, Exception{
        
        File dir = new File("test");
        for(File f : dir.listFiles()){
            File secretFile = f;

            // Считываем картинку
            File in = new File("test.bmp");

            FileStegoCover cover = new FileStegoCover(in);

            StegoWriter sw = new StegoWriter(cover.getStego());  
            System.out.println("capacity: "+sw.getCapacity());
            getFileContent(secretFile);        
            sw.write(new Secret(Secret.Type.FILE, fileName.getBytes(), fileData));
            in = cover.getFile();


            /**************/
            FileStegoCover coverRead = new FileStegoCover(in);
            StegoReader sr = new StegoReader(coverRead.getStego());
            Secret s = sr.read();

            System.out.println("attachment: "+new String(s.getAttachment()));
            String fName = "restored//"+new String(s.getAttachment());

            FileOutputStream fos = new FileOutputStream(new File(fName));

            fos.write(s.getData());

            fos.flush();
            fos.close();
        }
        /**************/
        
//        BufferedImage img = ImageIO.read(in);
//                
//        Signal signal = ImageProcessor.getSignal(img, 0);
//        
//        // Создаём стего
////        Stego stego = StegoFactory_1.createStego(signal);
//        StegoFactory sFactory = new ImageStegoFactory();
//        Stego stego = sFactory.createStego(in);
        
//        /*** Пишем в стего ***/
//        StegoWriter sw = new StegoWriter(stego);  
//        System.out.println("Capacity: "+sw.getCapacity());
//        StringBuilder sb = new StringBuilder(25160);
//        for(int i=0;i<4;i++){
//            sb.append("q");
//        }
//        System.out.println("Start writing...");
////        sw.write(new Secret(Secret.Type.TEXT, "Мама мыла раму!!!".getBytes()));
//        sw.write(new Secret(Secret.Type.TEXT, sb.toString().getBytes()));
//     
//        BufferedImage img_1 = img;
//        ImageProcessor.setBand(img_1, signal, 0);
//        write(in, img_1);
////        ImageIO.write(img, "jpg", new File("test111.jpg"));
//        
//        
//        
//        /*** Читаем стего ***/
//        // Создаём копию стего (как быдто мы его из файла достали)
//        Stego stg = StegoFactory_1.createStego(stego.getSignal(0));
////        Stego stg = sFactory.createStego(new File("out.png"));
//        
//        StegoReader sr = new StegoReader(stg);        
//        byte[] bb = sr.read().getData();
//        String out = new String(bb);        
//        System.out.println(out);

    }
    
    public void getFileContent(File f) throws FileNotFoundException, IOException{
        fileName = f.getName();
        System.out.println("fName: "+fileName);
        FileInputStream is = new FileInputStream(f);
        fileData = new byte[is.available()];
        is.read(fileData, 0, is.available());
        is.close();
    }
    
    public void write(File in, RenderedImage inImg) throws FileNotFoundException, IOException{
        
        ImageReader ir = ImageIO.getImageReadersByFormatName("png").next();
        ImageWriter iw = ImageIO.getImageWritersByFormatName("png").next();
        
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        ImageOutputStream ios = ImageIO.createImageOutputStream(new File("out.png"));
        
        ir.setInput(iis);
        iw.setOutput(ios);
        
//        ir.getImageMetadata(0);
        
        ImageWriteParam wp = iw.getDefaultWriteParam();
//        wp.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
//        wp.setCompressionQuality(1);
        
        
//        IIOImage img = new IIOImage(inImg, null, null);
        IIOImage img = new IIOImage(inImg, null, ir.getImageMetadata(0));
        
        
        
        iw.write(null, img, wp);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, Exception {
        new StegoTest();
    }
}
