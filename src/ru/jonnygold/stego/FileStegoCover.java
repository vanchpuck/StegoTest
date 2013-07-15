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
public class FileStegoCover extends StegoCover{

    private Stego stego;

    private File file;

    private StegoFactory factory;

    public FileStegoCover(File file) throws IOException{
        initialise(file); 
    }

    public void setFile(File file) throws IOException{
        initialise(file);
    }

    protected Stego getStego(){
        return this.stego;
    }

    /**
    * Puts steganograms into file, which associated with this FileStegoCover object
    * @throws IOException 
    */
    public void putIn() throws IOException{
        factory.putStego(file, stego);
    }

    private void initialise(File file) throws IOException{
        this.file = file;

        factory = ImageStegoFactory.getInstance();

        this.stego = factory.createStego(file);
    }
}