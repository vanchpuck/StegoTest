/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import ru.jonnygold.wavelet.*;
import ru.jonnygold.wavelet.filters.HaarFilter;

/**
 *
 * @author Vanchpuck
 */
class HaarSignalWriter implements SignalWriter{

    public static final int ERR_VAL = 5;
    
    RateFilter rFltr;
    
    public HaarSignalWriter(){
        rFltr = new RateFilter(new WaveletTransformer(HaarFilter.getInstance(), SimpleTransform.getInstance()), 3);
    }
    
    public HaarSignalWriter(TransformLogic alg){
        rFltr = new RateFilter(new WaveletTransformer(HaarFilter.getInstance(), alg), 3);
    }
    
    @Override
    public boolean writeSecret(Signal signal, int secret) {
//        if(signal.getData()[0] < 2 && signal.getData()[1] < 2) return null;
        WaveletData1D wData = rFltr.getDirectTransform(signal, TransformDirection.ROW_TRANSFORM);
        
//        System.out.println("Коэффициент: "+wData.getWavelet().getData()[0]);
//        int rate = (int) Math.round(wData.getWavelet().getData()[0]);
        //System.out.println("Коэффициент: "+rate);
        
        if(wData.getWavelet().getData()[0]==0){
            if(recordingCheck(wData, secret)){
                wData.getWavelet().getData()[0] = secret;
                //System.out.println("Данные записаны: "+wData.getWavelet().getData()[0]);
                signal.setSignal(rFltr.getInverseTransform(wData), 0, 0);
                return true;//rFltr.getInverseTransform(wData);
            }
            else{
//                System.out.println("Проверка НЕ пройдена");
                // Coefficients normalisation 
                if(wData.getScaled().getData()[0] < 2){
                    wData.getScaled().getData()[0] = 2;
                }
//                else if(wData.getScaled().getData()[0] > 253){
//                    wData.getScaled().getData()[0] = 253;
//                }
//                System.out.println(wData.getScaled().getData()[0]);
                wData.getWavelet().getData()[0] = ERR_VAL;
                signal.setSignal(rFltr.getInverseTransform(wData), 0, 0);
                return false;
            }
        }        
        return false;
    }

    private boolean recordingCheck(WaveletData1D wData, int value){
        wData.getWavelet().getData()[0] = value;
//        System.out.println(wData.getWavelet().getData()[0]+" = "+value);
        
        Signal restored = rFltr.getInverseTransform(wData);
        restored.getData()[0] = Math.abs(restored.getData()[0]);
        restored.getData()[1] = Math.abs(restored.getData()[1]);
//        if(restored.getData()[0] < 2 && restored.getData()[1] < 2) return false;
//        if(restored.getData()[1] < 3) return false;
//        System.out.println(restored.getData()[0]+" "+restored.getData()[1]);
        
        rFltr.setBorder(0);
        
        WaveletData1D wData1 = rFltr.getDirectTransform(restored, TransformDirection.ROW_TRANSFORM);
        rFltr.setBorder(3);
        
        if(Math.round(wData1.getWavelet().getData()[0]) == value) {
            //System.out.println("Проверка прошла успешно");
            return true;
        }
        else {
//            System.out.println("Проверка не пройдена");
            return false;
        }
    }
    
//    @Override
    public boolean availabilityCheck(Signal segment) {
        boolean tmp = writeSecret(segment, (byte)1);
        if(!tmp) {
            return false;
        }
        tmp = writeSecret(segment, (byte)0);
        if(!tmp) {
            return false;
        }
        return true;
    }
    
}
