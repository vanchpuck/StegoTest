/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import com.sun.org.apache.bcel.internal.generic.IREM;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import ru.jonnygold.wavelet.Signal;
import ru.jonnygold.wavelet.filters.WaveletFilter;

/**
 *
 * @author Vanchpuck
 */
public class StegoFactory_1 {
    
//    private WaveletFilter filter;
//    
//    public StegoFactory_1(WaveletFilter filter){
//        this.filter = filter;
//    }
    
    public static Stego createStego(File file){
        
        String fileName = file.getName();
        String type = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
        
        if(type == "png" || type == "bmp" || type == "gif"){
            
        }
        else{
            
        }
        
        return null;
    }
    
    public static Stego createStego(BufferedImage image) throws IOException{
        
        
        
        ImageReader ir = ImageIO.getImageReadersByFormatName("gif").next();
        ImageWriter iw = ImageIO.getImageWritersByFormatName("gif").next();
        
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        ImageOutputStream ios = ImageIO.createImageOutputStream(new File("out1.gif"));
        
        ir.setInput(iis);
        iw.setOutput(ios);
        
        ir.getImageMetadata(0);
        ir.r
        
        ImageWriteParam wp = iw.getDefaultWriteParam();
//        wp.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
//        wp.setCompressionQuality(1);
        
        
//        IIOImage img = new IIOImage(inImg, null, null);
        IIOImage img = new IIOImage(inImg, null, ir.getImageMetadata(0));
        img.
        
        
        iw.write(null, img, wp);
    }
    
    public static Stego createStego(Signal signal){
        List<Signal> list = new ArrayList<Signal>();
        list.add(signal);
        return new Stego(list);
    }
    
}
