package ce326.hw2;

public class YUVPixel {
    private int pixel;
    
    public YUVPixel(short Y, short U, short V){
        pixel=0;    
        setY(Y);
        setU(U);
        setV(V);
    }
    
    public YUVPixel(YUVPixel pix){
        pixel = pix.pixel;
    }
    
    public YUVPixel(RGBPixel pix){
        short Y=(short)(((66*pix.getRed()+129*pix.getGreen()+25*pix.getBlue()+128)>>8)+16);
        short U=(short)(((-38*pix.getRed()-74*pix.getGreen()+112*pix.getBlue()+128)>>8)+128);
        short V=(short)(((112*pix.getRed()-94*pix.getGreen()-18*pix.getBlue()+128)>>8)+128);
        
        setY(Y);
        setU(U);
        setV(V);
    }
    
    public short getY(){
        int mask = 0x00FF0000, tempPixel;
        short Y;
        
        tempPixel = (pixel & mask);
        Y = (short)(tempPixel>>16);
        return Y;
    }
    
    public short getU(){
        int mask = 0x0000FF00, tempPixel;
        short U;
        
        tempPixel = (pixel & mask);
        U = (short)(tempPixel>>8);
        return U;
    }
    
    public short getV(){
        int mask = 0x000000FF;
        short V;
        
        V = (short)(pixel & mask);
        return V;
    }
    
    public final void setY(short Y){
        int mask = 0xFF00FFFF, temp;
        
        temp = Y;
        temp = temp<<16;
        pixel = (pixel & mask);
        pixel = (pixel | temp);
    }
    
    public final void setU(short U){
        int mask = 0xFFFF00FF, temp;
        
        temp = U;
        temp = temp<<8;
        pixel = (pixel & mask);
        pixel = (pixel | temp);
    }
    
    public final void setV(short V){
        int mask = 0xFFFFFF00, temp;
        
        temp = V;
        pixel = (pixel & mask);
        pixel = (pixel | temp);
    }
}
