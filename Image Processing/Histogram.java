package ce326.hw2;

import java.io.FileWriter;
import java.io.IOException;

public class Histogram {
    double[] histogram;
    
    public Histogram(YUVImage img){
        int i,j;
        histogram = new double[256];////////////////////
        
        for(i=0; i < histogram.length; i++){
            histogram[i]=0;
        }
        for(i=0; i < img.image.length; i++){
            for(j=0; j < img.image[0].length; j++){
                histogram[img.image[i][j].getY()]++;
            }
        }
    }
    
    @Override
    public String toString(){
        String diagram="";
        int i;
        double temp;
        
        for(i=0; i<histogram.length; i++){
            diagram = diagram + i;
            temp = histogram[i];
            
            while(temp >= 1000){
                diagram = diagram + "#";
                temp = temp - 1000;
            }
            while(temp >= 100){
                diagram = diagram + "$";
                temp = temp - 100;
            }
            while(temp != 0){
                diagram = diagram + "*";
                temp--;
            }
            diagram = diagram + "\n";
        }
        return  diagram;
    }
    
    public void toFile(java.io.File file){
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(toString());
        }
        catch(IOException ex){
            System.out.println("Problem with write");  
        }     
    }
    
    public void equalize(){
        int i;
        double sum=0;
        
        for(i=0; i<256; i++){//////////////////////////
            sum = sum + histogram[i];
        }
        
        for(i=0; i<256; i++){
            histogram[i] = histogram[i]/sum;
        }
        
        for(i=1; i<256; i++){
            histogram[i] = histogram[i] + histogram[i-1];
        }
        
        for(i=0; i<256; i++){
            histogram[i] = (int) (histogram[i]*235);
        }
    }
    
    public short getEqualizedLuminocity(int luminocity){
        
        for(int i=0; i<256; i++){
            if(i==luminocity) {
                return (short)(histogram[i]);
            }
        }
        
        return 0;
    }
}
