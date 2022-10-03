package ce326.hw2;

public class RGBPixel {
    private int pixel;
    
    public RGBPixel(short red, short green, short blue){
        pixel = 0;
        setRGB(red, green, blue);
    }
    
    public RGBPixel(RGBPixel pix){
        pixel = pix.pixel;
    }
    
    public RGBPixel(YUVPixel pix){
        pixel=0;
        short C = (short)(pix.getY()-16);
        short D = (short)(pix.getU()-128);
        short E = (short)(pix.getV()-128);
        
        short red = clip((298*C + 409*E+128)>>8);
        short green = clip((298*C - 100*D - 208*E +128)>>8);
        short blue = clip((298*C + 516*D + 128)>>8);
        
        setRGB(red, green, blue);
    }
    
    public final short clip(int value){
        if(value<0){
            return 0;
        }
        else if (value>255){
            return 255;
        }
        return (short)value;
    }
    
    public short getRed(){
        int mask = 0x00FF0000, tempPixel;
        short red;
        
        tempPixel = (pixel & mask);
        red = (short)(tempPixel>>16);
        return  red;
    }
    
    public short getGreen(){
        int mask = 0X0000FF00, tempPixel;
        short green;
        
        tempPixel = (pixel & mask);
        green = (short)(tempPixel>>8);
        return  green;
    }
    
    public short getBlue(){
        int mask = 0X000000FF;
        short blue;
        
        blue = (short)(pixel & mask);
        return  blue;
    }
    
    public void setRed(short red){
        int mask = 0xFF00FFFF, temp;
        
        temp = red;
        temp = temp<<16;
        pixel = (pixel & mask);
        pixel = (pixel | temp);
    }
    
    public void setGreen(short green){
        int mask = 0xFFFF00FF, temp;
        
        temp = green;
        temp = temp<<8;
        pixel = (pixel & mask);
        pixel = (pixel | temp);
    }
    
    public void setBlue(short blue){
        int mask = 0xFFFFFF00, temp;
        
        temp = blue;
        pixel = (pixel & mask);
        pixel = (pixel | temp);
    }
    
    public int getRGB(){
        return pixel;
    }
    
    public void setRGB(int value){
        short red, green, blue;
        int mask, temp;
        
        mask = 0x00FF0000;
        temp = (value & mask);
        red = (short)(temp>>16);
        setRed(red);
        
        mask = 0x0000FF00;
        temp = (value & mask);
        green = (short)(temp>>8);
        setGreen(green);
        
        mask = 0x000000FF;
        blue = (short)(value & mask);
        setBlue(blue);       
    }
    
    public final void setRGB(short red, short green,short blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    
    @Override
    public String toString(){
        return getRed()+" "+getGreen()+" "+getBlue();
    }
}
