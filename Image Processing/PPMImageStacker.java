package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class PPMImageStacker{
    List<java.io.File> fileList;
    RGBImage stackImgRgb;
    PPMImage staImage;
    
    public PPMImageStacker(java.io.File dir){
        if(!dir.exists()){
            System.out.println("[ERROR] Directory " +dir+ " does not exist!");
        }
        if(!dir.isDirectory()){
            System.out.println("[ERROR] " +dir+ " is not a directory!");
        }
        
        fileList = new ArrayList<>();
        File[] files = dir.listFiles();
        for(int i=0; i<files.length; i++){
            fileList.add(files[i]);
        }
    }
    
    public void stack() throws FileNotFoundException, UnsupportedFileFormatException{
        PPMImage temp = null;
        boolean firstTime=true;
        int[][] imagesSum = null;
        
        try{
            int i,j;
            Iterator<java.io.File> it = fileList.iterator();
            while(it.hasNext()){
                
                temp = new PPMImage(it.next());
                
                if(firstTime){
                    imagesSum = new int[temp.ppm.length][temp.ppm[0].length];
                    stackImgRgb = new RGBImage();
                    stackImgRgb.image = new RGBPixel[temp.ppm.length][temp.ppm[0].length/3];
                    for(i=0; i<temp.ppm.length; i++){
                        for(j=0; j<temp.ppm[0].length/3; j++){
                            stackImgRgb.image[i][j] = new RGBPixel((short)0,(short) 0,(short) 0);
                        }
                    }
                    firstTime = false;
                }
                
                for(i=0; i<temp.ppm.length; i++){
                    for(j=0; j<temp.ppm[0].length; j++){
                        imagesSum[i][j] += temp.ppm[i][j];
                    }
                }
            }
            
            int counter;
            for(i=0; i<temp.ppm.length; i++){
                counter=0;
                for(j=0; j<temp.ppm[0].length; j++){
                    if(j%3==0){
                        stackImgRgb.image[i][counter].setRGB((short)(imagesSum[i][j]/fileList.size()),(short)(imagesSum[i][j+1]/fileList.size()),(short) (imagesSum[i][j+2]/fileList.size()));
                        counter++;
                    }
                }
            }
        }
        catch(IOException ex){
            System.out.println("An IOEXCEPTION is caught!");
        }
    }
    
    public PPMImage getStackedImage(){
        return new PPMImage(stackImgRgb);
    }
}
