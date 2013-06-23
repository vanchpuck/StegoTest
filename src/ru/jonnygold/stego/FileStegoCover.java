/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.*;


/**
 *
 * @author Vanchpuck
 */
public class FileStegoCover {
    
    private Stego stego;
    
    private File file;
    
    private StegoFactory factory;
    
    public FileStegoCover(File file) throws IOException{
        initialise(file);        
    }
        
    public void setFile(File file) throws IOException{
        initialise(file);
    }
    
    public Stego getStego(){
        return this.stego;
    }
    
    public File getFile() throws IOException{
        factory.putStego(file, stego);
        return file;
    }
    
    private void initialise(File file) throws IOException{
        this.file = file;
        
        // File type determination
        String fileName = file.getName();
        String type = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
        
        // Stego initialisation
        if(type.equals("bmp") || type.equals("png") || type.equals("gif") ){
            factory = ImageStegoFactory.getInstance();
        }
        else{
            throw new IOException("Формат файла не поддерживается");
        }
        
        this.stego = factory.createStego(file);
    }
}
