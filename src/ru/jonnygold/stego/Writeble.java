/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;

/**
 *
 * @author Vanchpuck
 */
public interface Writeble {
    
    public void write(Secret data) throws IOException;
    
//    public void write(byte[] data, int off, int len) throws IOException;
    
//    public int getCapacity();
    
}
