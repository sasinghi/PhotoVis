/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import com.jwetherell.algorithms.data_structures.KdTree.XYZPoint;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComponent;
import java.lang.Math;

/**
 *
 * @author sasinghi
 */
class Image {
    BufferedImage img;
    BufferedImage original_img;
    Point location;
    XYZPoint center;
    double height;
    double width;
    float angle;
    int frame_width;
    int frame_height;
    int id;
    double original_height;
    double original_width;

    public Image() {
    }

    public Image(BufferedImage img, Point location, int height, int width, float angle, int id) {
        this.img = img;
        this.original_img = img;
        this.location = location;
        this.height = height;
        this.width = width;
        this.angle = angle;
        this.id = id;
        //this.center=new XYZPoint(((this.location.x) + (this.location.x+this.width*Math.cos(this.angle)) + (this.location.x+this.height*Math.sin(this.angle)) + (this.location.x+this.height*Math.sin(this.angle)+this.width*Math.cos(this.angle)))/4,((this.location.y)+(this.location.y+this.width*Math.sin(this.angle))+(this.location.y-this.height*Math.cos(this.angle))+(this.location.y-this.height*Math.cos(this.angle)+this.width*Math.sin(this.angle)))/4);
    }

    public Image(BufferedImage img, int height, int width, int frame_width, int frame_height, int id) {
        this.img = img;
        this.original_img = img;
        this.height = height;
        this.width = width;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.id = id;
        
        // maintained to check max shrink/enlargement
        this.original_height = height;  
        this.original_width = width;
        
        //this.center=new XYZPoint(((this.location.x) + (this.location.x+this.width*Math.cos(this.angle)) + (this.location.x+this.height*Math.sin(this.angle)) + (this.location.x+this.height*Math.sin(this.angle)+this.width*Math.cos(this.angle)))/4,((this.location.y)+(this.location.y+this.width*Math.sin(this.angle))+(this.location.y-this.height*Math.cos(this.angle))+(this.location.y-this.height*Math.cos(this.angle)+this.width*Math.sin(this.angle)))/4);
    }

    public Image(BufferedImage img, int height, int width, int id) {
        this.img = img;
        this.original_img = img;
        this.height = height;
        this.width = width;
        this.id = id;
        this.center=new XYZPoint(((this.location.x) + (this.location.x+this.width*Math.cos(this.angle)) + (this.location.x+this.height*Math.sin(this.angle)) + (this.location.x+this.height*Math.sin(this.angle)+this.width*Math.cos(this.angle)))/4,((this.location.y)+(this.location.y+this.width*Math.sin(this.angle))+(this.location.y-this.height*Math.cos(this.angle))+(this.location.y-this.height*Math.cos(this.angle)+this.width*Math.sin(this.angle)))/4);

    }

    public BufferedImage getOriginal_img() {
        return original_img;
    }

    public void setOriginal_img(BufferedImage original_img) {
        this.original_img = original_img;
    }
    
    

    public double getOriginal_height() {
        return original_height;
    }

    public void setOriginal_height(double original_height) {
        this.original_height = original_height;
    }

    public double getOriginal_width() {
        return original_width;
    }

    public void setOriginal_width(double original_width) {
        this.original_width = original_width;
    }
    

    
    public int getFrame_height() {
        return frame_height;
    }

    public void setFrame_height(int frame_height) {
        this.frame_height = frame_height;
    }

    public int getFrame_width() {
        return frame_width;
    }

    public void setFrame_width(int frame_width) {
        this.frame_width = frame_width;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void updateCenter() {
       double a = ((this.getLocation().x) + (this.getLocation().x + this.getWidth()))/2;
       double b = ((this.getLocation().y) + (this.getLocation().y + this.getHeight()))/2;
       center = new XYZPoint(a,b);
    }
    
    public XYZPoint getCenter() {
        return center;
    }
    
}
