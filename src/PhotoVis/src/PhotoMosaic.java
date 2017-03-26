/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.floor;
import static java.lang.Math.min;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.coobird.thumbnailator.Thumbnails;


/**
 *
 * @author guliz
 */
public class PhotoMosaic {
        static int size = 5;
        static int final_width;
        static int final_height;
        static int frame_width=1250;
        static int frame_height=600;
        static ArrayList<MosaicImage> images = new ArrayList<>();
        static BufferedImage output;
        static File target;
       
    public static void initiate(File input, String path) throws IOException {
        target = input;
        System.out.println("PhotoMosaic is initiated");
        File directory;
        directory = new File(path);
        

        int j=0;
        for (File file : directory.listFiles())
            {
            try{ 
                // could also use a FileNameFilter
                if(file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg"))
                {
                    BufferedImage myimage = ImageIO.read(file);
                    BufferedImage unitImg = Thumbnails.of(myimage).forceSize(1, 1).asBufferedImage();
                    double[] avg = convertCIEvalues(averageColor(unitImg));

                    BufferedImage tileImg = Thumbnails.of(myimage).forceSize(size,size).asBufferedImage();
                    images.add(new MosaicImage(tileImg,avg,j));
                    j++;
                }
            }
             catch (IOException ex) {
                ex.printStackTrace();
            }
                
            }
        CalculateDimensions();
        ArrayList<BufferedImage> parts = getImagesFromInput();
        ArrayList<BufferedImage> results = new ArrayList<>();
        
        for(int k=0;k<parts.size();k++){
            BufferedImage bestFit = getBestFit(parts.get(k));
            results.add(bestFit);
        }
        
        BufferedImage final_result = createOutput(results);
        
        //ImageIO.write(final_result, "png", new File("output.png"));
        //output=Thumbnails.of(final_result).size(final_width, final_height).asBufferedImage();
        output=final_result;
        System.out.println("PhotoMosaic is created");
    }
        public static void CalculateDimensions() throws IOException{
            BufferedImage input = ImageIO.read(target);
          //  System.out.println("input.getHeight() " + input.getHeight() + " input.getWidth()" + input.getWidth() );
            if(input.getHeight()<frame_height && input.getWidth()<frame_width){
                final_height = input.getHeight();
                final_width = input.getWidth();
            } else if (input.getHeight()<frame_height && input.getWidth()>frame_width){
                final_width = frame_width;
                final_height = input.getHeight()*final_width/input.getWidth();
            } else if (input.getHeight()>frame_height && input.getWidth()<frame_width){
                final_height = frame_height;
                final_width = input.getWidth()*final_height/input.getWidth();
            } else if (input.getHeight()>frame_height && input.getWidth()>frame_width){
                if((input.getWidth()/frame_width)>(input.getHeight()/frame_height)){
                    final_width = frame_width;
                    final_height = input.getHeight()*final_width/input.getWidth();
                }else{
                    final_height = frame_height;
                    final_width = input.getWidth()*final_height/input.getHeight();
                }
                
            } 
          //  System.out.println("final_height " + final_height + " final_width" + final_width );

        
        }
        public static BufferedImage createOutput(ArrayList<BufferedImage> results) throws IOException{
            
           // BufferedImage input = ImageIO.read(target);
            int tilePerLine = final_width/size;
            int tilePerColumn = final_height/size;
            //int x=input.getWidth()/tilePerLine;
            //int y=input.getHeight()/tilePerColumn;
            //System.out.println(x + " x and y  "+ y);
            BufferedImage photo = new BufferedImage(final_width, final_height, BufferedImage.TYPE_3BYTE_BGR); 
            
            for(int k=0;k<results.size();k++){
                        int locX=size*(k%tilePerLine);
                        int locY=(int) (size*floor(k/tilePerLine));
                        
			BufferedImage imagePart = photo.getSubimage(locX,locY,size,size);
			imagePart.setData(results.get(k).getData());
		}
            
        
            return photo;
        }
    
    
        public static ArrayList<BufferedImage> getImagesFromInput() throws IOException{
            ArrayList<BufferedImage> tiles = new ArrayList<>();
            BufferedImage input=ImageIO.read(target);
            BufferedImage shrunkImage = Thumbnails.of(input).forceSize(final_width, final_height).asBufferedImage();
            int totalHeight = shrunkImage.getHeight();
            int totalWidth = shrunkImage.getWidth();
            int x=0;
            int y=0;
            int w=size;
            int h=size;
            while(y+h <= totalHeight){
			while(x+w <= totalWidth){
				BufferedImage inputPart = shrunkImage.getSubimage(x, y, w, h);
				tiles.add(inputPart);
				x+=w;
			}
			x=0;
			y+= h;
		}
            return tiles;
        }
        
        public static BufferedImage getBestFit(BufferedImage part) throws IOException{
            BufferedImage bestFit=null;
            int bestFitScore = 10000000;
            
            for(int i=0;i<images.size();i++){
                    int score = getScore(part, images.get(i));
			if (score < bestFitScore){
				bestFitScore = score;
				bestFit = images.get(i).getImage();
			}	
		}
            return bestFit;
        }
        
        public static int getScore(BufferedImage a,MosaicImage b) throws IOException{
            BufferedImage unitImg = Thumbnails.of(a).forceSize(1, 1).asBufferedImage();
            double[] avg = convertCIEvalues(averageColor(unitImg));
            int score = findDifference(avg,b.getAvg());
            return score;
        }
       
         public static int[] averageColor(BufferedImage bi) {
            int sumr = 0, sumg = 0, sumb = 0;
            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    Color pixel = new Color(bi.getRGB(x, y));
                    sumr += pixel.getRed();
                    sumg += pixel.getGreen();
                    sumb += pixel.getBlue();
                }
            }
            int num = bi.getWidth()*bi.getHeight();
            
            int[] avg = {sumr / num, sumg / num, sumb / num};
            
            return avg;
    }
         
    public static double[] convertCIEvalues(int[] color ){
        
        double var_R = ( (double)color[0] / 255 ) ;       //R from 0 to 255
        double var_G = ( (double)color[1] / 255 ) ;       //G from 0 to 255
        double var_B = ( (double)color[2] / 255 ) ;       //B from 0 to 255

        if ( var_R > 0.04045 ){ 
                var_R = Math.pow(( ( var_R + 0.055 ) / 1.055 ),  2.4);
        }
        else     {              var_R = var_R / 12.92;}
        if ( var_G > 0.04045 ) {var_G = Math.pow(( ( var_G + 0.055 ) / 1.055 ), 2.4);}
        else                   {var_G = var_G / 12.92;}
        if ( var_B > 0.04045 ) {var_B = Math.pow(( ( var_B + 0.055 ) / 1.055 ) , 2.4);}
        else                 {  var_B = var_B / 12.92;}

        var_R = var_R * 100;
        var_G = var_G * 100;
        var_B = var_B * 100;

        //Observer. = 2°, Illuminant = D65
        double X = var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805;
        double Y = var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722;
        double Z = var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505;
    
        
        double var_X = X / 95.047  ;       //ref_X =  95.047   Observer= 2°, Illuminant= D65
        double var_Y = Y / 100.00    ;      //ref_Y = 100.000
        double var_Z = Z / 108.883    ;      //ref_Z = 108.883
  

        if ( var_X > 0.008856 ){ var_X = Math.pow(var_X, ( (double)1/3 ));}
        else  {                  var_X = ( 7.787 * var_X ) + ( (double)16 / 116 );}
        if ( var_Y > 0.008856 ){ var_Y = Math.pow(var_Y, ( (double)1/3 ));}
        else       {             var_Y = ( 7.787 * var_Y ) + ( (double)16 / 116 );}
        if ( var_Z > 0.008856 ) { var_Z =Math.pow(var_Z , ( (double)1/3) );}
        else            {        var_Z = ( 7.787 * var_Z ) + ((double)16 / 116 );}

        
        double[] CIE = {0,0,0};
        
        CIE[0] = ( 116 * var_Y ) - 16;
        CIE[1] = 500 * ( var_X - var_Y );
        CIE[2] = 200 * ( var_Y - var_Z );
        
        
        return CIE;
          
    }
    
    
    public static int findDifference(double[] first, double[] second){
        double CIE_L1 = first[0];
        double CIE_a1 = first[1];
        double CIE_b1 = first[2];

        double CIE_L2 = second[0];
        double CIE_a2 = second[1];
        double CIE_b2 = second[2];
        double weight = 1;
            
        double xC1 = Math.sqrt( Math.pow( CIE_a1 , 2 ) + Math.pow( CIE_b1 , 2 ) );
        double xC2 = Math.sqrt( Math.pow( CIE_a2 , 2 ) + Math.pow( CIE_b2 , 2 ) );
        double xDL = CIE_L2 - CIE_L1;
        double xDC = xC2 - xC1;
        double xDH;
        double xDE = Math.sqrt( ( ( CIE_L1 - CIE_L2 ) * ( CIE_L1 - CIE_L2 ) )
                  + ( ( CIE_a1 - CIE_a2 ) * ( CIE_a1 - CIE_a2 ) )
                  + ( ( CIE_b1 - CIE_b2 ) * ( CIE_b1 - CIE_b2 ) ) );
        if ( Math.sqrt( xDE ) > ( Math.sqrt( Math.abs( xDL ) ) + Math.sqrt( Math.abs( xDC ) ) ) ) {
           xDH = Math.sqrt( ( xDE * xDE ) - ( xDL * xDL ) - ( xDC * xDC ) );
        }
        else {
           xDH = 0;
        }
        double xSC = 1 + ( 0.045 * xC1 );
        double xSH = 1 + ( 0.015 * xC1 );
        xDC /=  xSC;
        xDH /=  xSH;
        int diff = (int) Math.sqrt( Math.pow(xDL , 2) + Math.pow(xDC , 2 )+ Math.pow(xDH , 2) );
        return diff;
    }

    PhotoMosaic(File file, String path) throws IOException {
            
            
            initiate(file,path);
        }
    
    
         
    }
        

        
    
 
   
    

