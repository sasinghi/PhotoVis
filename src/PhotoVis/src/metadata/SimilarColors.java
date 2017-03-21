/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metadata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author oyku
 */
public class SimilarColors {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BufferedImage myimage = ImageIO.read(new File("C:\\Users\\oyku\\Desktop\\Geotagged Images\\Example Las Vegas.png\\"));
     
        int[] avgfirst = averageColor(myimage);
        double[] avgfirst1 = convertCIEvalues(avgfirst);
        
        File infilepath = new File("C:\\Users\\oyku\\Desktop\\Geotagged Images\\");
        File[] listOfFiles = infilepath.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            String temp = listOfFiles[i].getName();
            
           if (temp.endsWith("png") && !temp.equals("Example Las Vegas.png")) {
                
                BufferedImage testimage = ImageIO.read(new File("C:\\Users\\oyku\\Desktop\\Geotagged Images\\" + temp));
                
                int[] avgsecond = averageColor(testimage);
                double[] avgsecond1 = convertCIEvalues(avgsecond);
                double diff = findDifference(avgfirst1, avgsecond1);
                System.out.println(temp + "     "    + diff);
                
                System.out.println("--------------------------------------------- ");
            }
            
        }
        
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
    
    
    public static double findDifference(double[] first, double[] second){
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
        double diff = Math.sqrt( Math.pow(xDL , 2) + Math.pow(xDC , 2 )+ Math.pow(xDH , 2) );
        return diff;
    }
    
    
    /*
    public static int findDifference2(double[] first, double[] second)
    {
        int rmean = ( (int)first[0] + (int)second[0]) / 2;
        int r = (int)first[0] - (int)second[0];
        int g = (int)first[1]- (int)second[1];
        int b = (int)first[2]- (int)second[2];
      
       
        return (int) Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)));
    }
    */
    
    
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
    
    
}
