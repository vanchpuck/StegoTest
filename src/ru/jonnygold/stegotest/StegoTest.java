/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stegotest;


import java.io.*;
import java.security.NoSuchAlgorithmException;
import ru.jonnygold.stego.*;

/**
 *
 * @author Vanchpuck
 */
public class StegoTest {

    String fileName;
    byte[] fileData;

    public StegoTest() throws IOException, NoSuchAlgorithmException, Exception{

        /*
        * В корне создаём папки test и restored.
        * В папку test кидаем файлик, который хотим запрятать.
        * В корень кидаем файл, в который будем прятать.
        * Запускаем программу.
        * При выполнении программа записывает содержимое файла в стего и затем счтывает в папку restored.
        */

        File dir = new File("test");

        // Пройдёмся по содержимому папки и обработаем все файлы
        for(File f : dir.listFiles()){
            File secretFile = f;

            /**** ПИШЕМ В СТЕГО ****/

            // Считываем картинку
            File in = new File("test.bmp");

            // Создаём стего
            FileStegoCover cover = new FileStegoCover(in);

            // Создаём писателя с шифрованием 
            Writeble sw = new CryptoStegoWriter(new StegoWriter(cover.getStego())); 

            // Вместимость стего в байтах
            System.out.println("CAPACITY: "+sw.getCapacity());

            // Тут просто получаем содержимое файла.
            getFileContent(secretFile); 

            // Пишем в стего (В данном случае пишем файл, но тип можно изменить, например - на текст)
            sw.write(new Secret(Secret.Type.FILE, fileName.getBytes(), fileData));
            
            // Получаем файл с записанной в него стеганограммой
            in = cover.getFile();


            /**** ЧИТАЕМ ИЗ СТЕГО ****/
            
            // Создаём стего из файла
            FileStegoCover coverRead = new FileStegoCover(in);

            // Создаём крипто-читателя 
            Readeble sr = new CryptoStegoReader(new StegoReader(coverRead.getStego()));

            // Считываем сообщение
            Secret s = sr.read();

            // getAttachment возавращает имя файла (в случае передачи текстового сообщения поле ничего не содержит)
            String fName = "restored//"+new String(s.getAttachment());

            // Создаём новый файл
            File res = new File(fName); 
            res.createNewFile();

            // Поток для записи в файл
            FileOutputStream fos = new FileOutputStream(res);

            // Получаем данные (getData()) и пишем их в поток
            fos.write(s.getData());
            fos.flush();
            fos.close();
        }

    }

    public void getFileContent(File f) throws FileNotFoundException, IOException{
        fileName = f.getName();
        System.out.println("fName: "+fileName);
        FileInputStream is = new FileInputStream(f);
        fileData = new byte[is.available()];
        is.read(fileData, 0, is.available());
        is.close();
    }

    // public void write(File in, RenderedImage inImg) throws FileNotFoundException, IOException{
    // 
    // ImageReader ir = ImageIO.getImageReadersByFormatName("png").next();
    // ImageWriter iw = ImageIO.getImageWritersByFormatName("png").next();
    // 
    // ImageInputStream iis = ImageIO.createImageInputStream(in);
    // ImageOutputStream ios = ImageIO.createImageOutputStream(new File("out.png"));
    // 
    // ir.setInput(iis);
    // iw.setOutput(ios);
    // 
    //// ir.getImageMetadata(0);
    // 
    // ImageWriteParam wp = iw.getDefaultWriteParam();
    //// wp.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
    //// wp.setCompressionQuality(1);
    // 
    // 
    //// IIOImage img = new IIOImage(inImg, null, null);
    // IIOImage img = new IIOImage(inImg, null, ir.getImageMetadata(0));
    // 
    // 
    // 
    // iw.write(null, img, wp);
    // 
    // }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, Exception {
        System.out.println("Main");
        new StegoTest();
    }
}