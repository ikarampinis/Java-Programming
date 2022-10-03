package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class YUVImage {
    YUVPixel[][] image;
    
    public YUVImage(int width, int height){
        image = new YUVPixel[height][width];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                image[i][j] = new YUVPixel((short)16,(short) 128,(short) 128);
            }
        }
    }
    
    public YUVImage(YUVImage copyImg){
        image = copyImg.image;
    }
    
    public YUVImage(RGBImage RGBImg){
        image = new YUVPixel[RGBImg.getHeight()][RGBImg.getWidth()];
        for(int i=0; i<RGBImg.getHeight(); i++){
            for(int j=0; j<RGBImg.getWidth(); j++){
                image[i][j] = new YUVPixel(RGBImg.image[i][j]);
            }
        }
    }
    
    public YUVImage(java.io.File file) throws FileNotFoundException, UnsupportedFileFormatException{
        int width, height;
        short Y, U, V;
        
        try(Scanner scan = new Scanner(file)){
            
            if(scan.hasNext("YUV3")){
                scan.next();
            }
            else{
                throw new UnsupportedFileFormatException();
            }
            
            width = scan.nextInt();
            height = scan.nextInt();
            image = new YUVPixel[height][width];
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    Y = (short)scan.nextInt();
                    U = (short)scan.nextInt();
                    V = (short)scan.nextInt();
                    
                    image[i][j] = new YUVPixel(Y, U, V);
                }
            }
        }
    }
    
    @Override
    public String toString(){
        int height, width;
        
        height = image.length;
        width = image[0].length;
        
        StringBuilder formatYUV = new StringBuilder("YUV3\n"+width+" "+height+"\n");
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                formatYUV.append(image[i][j].getY()+" "+image[i][j].getU()+" "+image[i][j].getV()+"\n");
            }
        }
        return formatYUV.toString();
    }
    
    public void toFile(java.io.File file){
        if(file.exists()){
            file.delete();
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(toString());
        }
        catch(IOException ex){
            System.out.println("Problem with write");  
        }     
    }
    
    public void equalize(){
        Histogram hist = new Histogram(this);
        
        hist.equalize();
        for(int i=0; i<image.length; i++){
            for(int j=0; j<image[0].length; j++){
                image[i][j].setY(hist.getEqualizedLuminocity(image[i][j].getY()));
            }
        }
    }
}


