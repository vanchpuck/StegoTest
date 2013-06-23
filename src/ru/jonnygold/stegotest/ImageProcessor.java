/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stegotest;

import java.awt.image.BufferedImage;
import ru.jonnygold.wavelet.Signal;
import ru.jonnygold.wavelet.SignalFactory;

/**
 *
 * @author Vanchpuck
 */
public class ImageProcessor {
    
    public static Signal getSignal(BufferedImage img, int band){
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        double[] data = new double[width*height];
        img.getData().getSamples(0, 0, width, height, band, data);
        
        Signal signal = SignalFactory.createSignal2D(data, height, width);
        
        return signal;
    }
    
    public static void setBand(BufferedImage img, Signal signal, int band){
        int width = img.getWidth();
        int height = img.getHeight();
        
        img.getRaster().setSamples(0, 0, width, height, band, signal.getData());
    }
    
}
