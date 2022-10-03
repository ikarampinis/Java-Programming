package ce326.hw2;


public class RGBImage implements Image{
    RGBPixel[][] image;
    int colorDepth;
    public static final int MAX_COLORDEPTH = 255;
    
    public RGBImage(){
        
    }
    
    public RGBImage(int width, int height, int colordepth){
        image = new RGBPixel[height][width];
        colorDepth = colordepth;
    }
    
    public RGBImage(RGBImage copyImg){
        image = copyImg.image;
    }
    
    public RGBImage(YUVImage YUVImg){
        image = new RGBPixel[YUVImg.image.length][YUVImg.image[0].length];
        for(int i=0; i<YUVImg.image.length; i++){
            for(int j=0; j<YUVImg.image[0].length; j++){
                image[i][j] = new RGBPixel(YUVImg.image[i][j]);
            }
        }
    }
    
    
    public final int getWidth(){
       return image[0].length;
    }
    
    public final int getHeight(){
        return image.length;
    }
    
    public int getColorDepth(){
        return MAX_COLORDEPTH;
    }
    
    public RGBPixel getPixel(int row, int col){
        return image[row][col];
    }
    
    public void setPixel(int row, int col, RGBPixel pixel){
        image[row][col] = pixel;
    }
    
    @Override
    public void grayscale(){
        int i,j;
        short gray;
        
        for(i=0; i<getHeight(); i++){
            for(j=0; j<getWidth(); j++){
                gray=(short)(image[i][j].getRed()*0.3+image[i][j].getGreen()*0.59+image[i][j].getBlue()*0.11);
                image[i][j].setRGB(gray, gray, gray);
            }
        }
    }
    
    @Override
    public void doublesize(){
        int i,j;
        int newWidth = 2*getWidth();
        int newHeight = 2*getHeight();
        RGBPixel[][] temp = image;
        image = new RGBPixel[newHeight][newWidth];
        
        for (i=0; i<newHeight/2; i++){
            for(j=0; j<newWidth/2; j++){
                image[2*i][2*j] = temp[i][j];
                image[2*i+1][2*j] = temp[i][j];
                image[2*i][2*j+1] = temp[i][j];
                image[2*i+1][2*j+1] = temp[i][j];
            }
        }
    }
    
    @Override
    public void halfsize(){
        int i,j,avgRed,avgGreen, avgBlue;
        int newWidth = getWidth()/2;
        int newHeight = getHeight()/2;
        RGBPixel[][] temp = image;
        image = new RGBPixel[newHeight][newWidth];
        
        for (i=0; i<newHeight; i++){
            for(j=0; j<newWidth; j++){
                avgRed=(temp[2*i][2*j].getRed()+temp[2*i+1][2*j].getRed()+temp[2*i][2*j+1].getRed()+temp[2*i+1][2*j+1].getRed())/4;
                avgGreen=(temp[2*i][2*j].getGreen()+temp[2*i+1][2*j].getGreen()+temp[2*i][2*j+1].getGreen()+temp[2*i+1][2*j+1].getGreen())/4;
                avgBlue=(temp[2*i][2*j].getBlue()+temp[2*i+1][2*j].getBlue()+temp[2*i][2*j+1].getBlue()+temp[2*i+1][2*j+1].getBlue())/4;
                image[i][j]= new RGBPixel((short)avgRed,(short)avgGreen, (short)avgBlue);
            }
        }
    }
    
    @Override
    public void rotateClockwise(){
        int i,j;
        int newWidth = getHeight();
        int newHeight = getWidth();
        RGBPixel[][] temp = image;
        image = new RGBPixel[newHeight][newWidth];
        
        for(j=newWidth-1; j>=0; j--){
            for (i=0; i<newHeight; i++){
                image[i][j] = temp[(newWidth-1)-j][i];
            }
        }
    }
}
