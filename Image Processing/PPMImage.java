package ce326.hw2;

import java.io.*;
import java.util.Scanner;

public class PPMImage extends RGBImage{
    short[][] ppm;
    
    public PPMImage(){
        
    }
    
    public PPMImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException{
        super();
        
        try(Scanner scan = new Scanner(file)){
            int counter, i, j;
            
            if(scan.hasNext("P3")){
                scan.next();
            }
            else{
                throw new UnsupportedFileFormatException();
            }
            
            int Width = scan.nextInt();
            if(Width<0){
                throw new UnsupportedFileFormatException();
            }
            
            int Height = scan.nextInt();
            if(Height <0){
                throw new UnsupportedFileFormatException();
            }
            
            int colordepth = scan.nextInt();
            if(colordepth > 256 || colordepth < 0){
                throw new UnsupportedFileFormatException();
            }
            
            super.image = new RGBPixel[Height][Width];

            ppm = new short[Height][Width*3];
            for( i=0; i<Height; i++){
                counter=0;
                for( j=0; j<Width*3; j++){
                    ppm[i][j] = scan.nextShort();
                    if(j%3==2){
                        super.image[i][counter] = new RGBPixel(ppm[i][j-2], ppm[i][j-1], ppm[i][j]);
                        counter++;
                    }
                }
            }
        }
    }
    
    public PPMImage(RGBImage img){
        super(img);
        ppm = new short[super.getHeight()][super.getWidth()*3];
        for(int i=0; i<super.getHeight(); i++){
            for(int j=0; j<super.getWidth()*3; j++){
                switch (j%3) {
                    case 0:
                        ppm[i][j] = super.image[i][j/3].getRed();
                        break;
                    case 1:
                        ppm[i][j] = super.image[i][j/3].getGreen();
                        break;
                    default:
                        ppm[i][j] = super.image[i][j/3].getBlue();
                        break;
                }
            }
        }
    }
    
    public PPMImage(YUVImage img){
        super(img);
        ppm = new short[super.getHeight()][super.getWidth()*3];
        for(int i=0; i<super.getHeight(); i++){
            for(int j=0; j<super.getWidth()*3; j++){
                switch (j%3) {
                    case 0:
                        ppm[i][j] = super.image[i][j/3].getRed();
                        break;
                    case 1:
                        ppm[i][j] = super.image[i][j/3].getGreen();
                        break;
                    default:
                        ppm[i][j] = super.image[i][j/3].getBlue();
                        break;
                }
            }
        }
    }
    
    @Override
    public String toString(){
        int height = ppm.length, width = ppm[0].length;
        StringBuilder ppmForm = new StringBuilder("P3\n"+width/3+" "+height+"\n"+MAX_COLORDEPTH+"\n");
        
        for(int i=0; i<height; i++){
            for(int j=0; j<width/3; j++){
                ppmForm.append(super.getPixel(i, j)+"\n");
            }
        }
        return ppmForm.toString();
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
}
