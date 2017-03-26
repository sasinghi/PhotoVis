/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.image.BufferedImage;

/**
 *
 * @author guliz
 */
public class MosaicImage {
    BufferedImage img;
    double[] avg;
    int rank;
    
    public MosaicImage(){
    
    }
    
    public MosaicImage(BufferedImage img, double[] avg,int rank){
        this.img=img;
        this.avg=avg;
        this.rank=rank;
    }
   
    
    public BufferedImage getImage(){
            return this.img;
        }
    
    public double[] getAvg(){
            return this.avg;
        }
}
