package src;

import com.jwetherell.algorithms.data_structures.KdTree;
import edu.wlu.cs.levy.CG.KDTree;
import java.awt.*;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import net.coobird.thumbnailator.Thumbnails;

public class PhotoViewer {

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    static Map<Integer, Image> labelImageMap;
    static ArrayList<Image> images = new ArrayList<>();
    //frame dimensions
    static double FRAME_WIDTH = 1200;
    static double FRAME_HEIGHT = 780;
    // Nearest neighbors parameters. range along x and y direction is 10. 
    final static int xrad = 100;
    final static int yrad = 100;
    // Minimum scale is h/2 * w/2
    // Maximum scale is h*2 * w*2
    final static double MIN = 2.0;
    final static double MAX = 0.5;
    final static double SCALE = 1;

    private static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
        BufferedImage resizedImg = srcImg;
        try {
            resizedImg = Thumbnails.of(srcImg).size(w, h).asBufferedImage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resizedImg;
    }

    public static void addComponentAt(Component component, Point location, Container pane) {
    }

    public static void addComponentsToPane(final Container pane, ArrayList<Image> images1) throws IOException {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        JLabel label;
        Dimension dimension;
        for (src.Image image : images) {
            dimension = checkBoundingDimensions(image.getHeight(), image.getWidth());
            if ((int) dimension.getWidth() != image.getWidth() || (int) dimension.getHeight() != image.getHeight()) {
                // Scale image to new dimension 
                image.setImg(getScaledImage(image.img, (int) dimension.getWidth(), (int) dimension.getHeight()));
                image.setHeight((int) dimension.getHeight());
                image.setWidth((int) dimension.getWidth());
            }
            // By default first position is (0,0) 
            image.setLocation(new Point(0, 0));
            
            // setting image id to its order on pane
            image.setId(pane.getComponentCount());
            
            // Add pair in labelImage Map
            labelImageMap.put(pane.getComponentCount(), image);

            label = new JLabel(new ImageIcon(image.getImg()));

            label.setBounds(0, 0, image.getWidth(), image.getHeight());
            label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

            pane.add(label);
            pane.getParent().revalidate();
            


            // Check if overlaps exist
            //Map<Integer,Image> adjacency = overlapsAnother(image,pane,pane.getComponentCount()-1);
            ArrayList<Integer> adjacency = overlappedImages(image, pane, pane.getComponentCount() - 1);
            if (adjacency.size() > 0) {
                System.out.println("Image "+ image.getId()+" has "+ adjacency.size()+" overlaps.");
                // Move the overlapping images 
                MoveOverlappingImages(pane, image, adjacency);
            }else {
                // enlarge image till MAX scale 
                BufferedImage enlargedImg;
                while (image.getOriginal_height()/image.getHeight() >= MAX && image.getOriginal_width()/image.getWidth() >= MAX && insideFrame(image)){
                     enlargedImg = getScaledImage(image.img, (int) image.getWidth()*2, (int) image.getHeight()*2);
                     image.setImg(enlargedImg);
                     image.setHeight(enlargedImg.getHeight());
                     image.setWidth(enlargedImg.getWidth());
                }
                
                pane.getComponent(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());
                pane.getParent().revalidate();
                pane.getParent().repaint();
            }

            //Check and resize all panel components

            //detects mouse clicks longer than 2 seconds for user interaction
            pane.addMouseListener(new MouseAdapter() {

                Date pressedTime;
                long timeClicked;

                @Override
                public void mousePressed(MouseEvent e) {
                    pressedTime = new Date();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    timeClicked = new Date().getTime() - pressedTime.getTime();
                    if (timeClicked >= 2000) {
                        MouseDetect(e, pane);
                    }
                }
            });

        }
    }

    private static void createAndShowGUI(ArrayList<Image> images) throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("PhoJoy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Use no layout manager
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setPreferredSize(new Dimension(1200, 780));
        frame.pack();
        FRAME_WIDTH = 1200;
        FRAME_HEIGHT = 780;



        frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener() {

            @Override
            public void ancestorResized(HierarchyEvent e) {
                // System.out.println("Resized:" + e.getChanged().getSize());
                FRAME_WIDTH = e.getChanged().getSize().getWidth();
                FRAME_HEIGHT = e.getChanged().getSize().getHeight();
                Container container = (Container) e.getChanged();
                for (Component component : container.getComponents()) {
                    component.setLocation((int) (FRAME_WIDTH / 1200) * component.getX(), (int) (FRAME_HEIGHT / 780) * component.getY());
                }
                // WRITE A GLOBAL MOVE RESIZE AND CALL HERE ---TODO
                e.getChanged().revalidate();
                e.getChanged().repaint();
            }

            @Override
            public void ancestorMoved(HierarchyEvent e) {
                //System.out.println(e);
            }
        });

        //Display the window.
        frame.setVisible(true);
        
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane(), images);

        

        // Wait 
//        long start = new Date().getTime();
//        while(new Date().getTime() - start < 1000L){}
        //-------------TEST
        // Change position of first image
        // System.out.println(frame.getContentPane().getComponents().length);

        // Change component location
        //frame.getContentPane().getComponent(0).setBounds((int)FRAME_WIDTH/20, (int)FRAME_HEIGHT/20, 300, 300);

        //frame.revalidate();
        //frame.repaint();
        //------------REMOVE


    }

    public static void main(String[] args) throws IOException {
        // Add images in an ArrayList
        images = readImages();
        // instantiate label image map 
        labelImageMap = new HashMap<>();
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    createAndShowGUI(images);

                } catch (IOException ex) {
                }
            }
        });
    }

    private static ArrayList<Image> readImages() {
        String filename;
        ArrayList<Image> image = new ArrayList<>();
        //***********EXAMPLE1****************//
        //***********EXAMPLE1****************//
        for(int i=8;i<10;i++){
            try {
                filename = "images/small/image"+i+".png";
                BufferedImage img = ImageIO.read(new File(filename));
                image.add(new Image(img, img.getHeight(), img.getWidth(), (int) FRAME_WIDTH, (int) FRAME_HEIGHT,i));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
         }
        //***********END EXAMPLE 1****************//
        //***********EXAMPLE 2****************//
//        for(int i=8;i<17;i++){
//            try {
//                //if(i<3)
//                  //  filename = "images/image"+0+".jpg";
//                //else
//                    filename = "images/small/image"+i+".png";
//                BufferedImage img = ImageIO.read(new File(filename));        
//                image.add(new Image(img,img.getHeight(),img.getWidth(),(int)FRAME_WIDTH,(int)FRAME_HEIGHT,i));
//                
//            } catch (IOException ex) {
//            } 
//        }
        //***********END EXAMPLE 2****************//
        
        //*********Example 3 - scaling and bounding ************//
//        try {
//                filename = "images/image4.jpg";
//                BufferedImage img = ImageIO.read(new File(filename));
//                image.add(new Image(img, img.getHeight(), img.getWidth(), (int) FRAME_WIDTH, (int) FRAME_HEIGHT,0));
//                
//                } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        // ********End Example 3*****************//
        return image;
    }

    private static Dimension checkBoundingDimensions(int height, int width) {
        // Checks if image larger than bounding frame
        if (height <= FRAME_HEIGHT && width <= FRAME_WIDTH) {
            // Image fits, return unchanged Dimensions
            return new Dimension(width, height);
        } else {
            if (height > FRAME_HEIGHT) {
                while (height > FRAME_HEIGHT) {
                    height -= 10;
                }
            }
            if (width > FRAME_WIDTH) {
                while (width > FRAME_WIDTH) {
                    width -= 10;
                }
            }
            return new Dimension(width, height);
        }
    }

//    private static Map<Integer,Image> overlapsAnother(src.Image image, Container pane, int exclude) {
//        Map<Integer,Image> adjacency = new HashMap<>();
//        if(pane.getComponentCount()<=1){
//            // pane has no components yet. This is the first. So, no overlap.
//            System.out.println("If....");
//            return adjacency;
//        }
//        else{
//            // Check overlap using nearest neighbors  -- TODO
//            // Avoid positions with overlap OR move the rest to create space for the new
//            System.out.println(exclude + "is excluded");
//            Image compareImg = new Image();
//            Rectangle imageRec = new Rectangle(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight()); // Deafault location taken as (0,0)
//            Rectangle compareImgRec;
//            for(int key : labelImageMap.keySet()){
//                if(key!= exclude){
//                    compareImg = labelImageMap.get(key);
//                    //Check if image and compareImg have an intersection
//                    compareImgRec = new Rectangle(pane.getComponent(key).getBounds().getLocation().x, pane.getComponent(key).getBounds().getLocation().y, compareImg.width, compareImg.height);
//                    if(imageRec.intersects(compareImgRec)){
//                        adjacency.put(key,labelImageMap.get(key));
//                    }
//                }
//            }
//            System.out.println(labelImageMap.size()+"...size of map");
//            System.out.println(adjacency.size()+"...overlapping images");
//            return adjacency;   
//        }
//        
//    }
//    
    private static ArrayList<Integer>  overlappedImages(src.Image image, Container pane, int exclude) {
        ArrayList<Integer> adjacency = new ArrayList<>();

        if (pane.getComponentCount() <= 1) {
            // pane has no components yet. This is the first. So, no overlap.
            return adjacency;
        } else {
            // System.out.println(exclude + "is excluded");
            //Lines are created to obtain the bounds of the image
            Image compareImg;
            Line2D[] imageLine = DrawPath(image); // Considering rotation
            Rectangle imageRec = new Rectangle(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());  
            Line2D[] compareImgLine;
            Rectangle compareImgRec;

            //this function finds 9 nearest neighbours of image
            ArrayList<Integer> list = Neighbours(image, exclude);
            System.out.println("Image "+ image.getId()+" has "+ list.size()+" neighbors.");
            for (int i = 0; i < list.size(); i++) {
                int key = list.get(i);
                if (key != exclude) 
                {
                    compareImg = labelImageMap.get(key);
                    compareImgLine = DrawPath(compareImg);
                    compareImgRec = new Rectangle(compareImg.getLocation().x, compareImg.getLocation().y, compareImg.width, compareImg.height);
//                    //This controls the lines of images so that it can understand intersections of rotated images
//                    if (IsIntersecting(imageLine, compareImgLine)) {
//                        adjacency.put(key, labelImageMap.get(key));
//                    } 
                    // Check if the two bounding rectangles intersect. Rotation not considered for now. 
                    // Checks - if the two images intersect, if new image completely overlaps another image & vice versa
                    if (imageRec.intersects(compareImgRec) || imageRec.contains(compareImgRec) || compareImgRec.contains(imageRec)) {
                        adjacency.add(key);
                    }
                }
            }
            return adjacency;
        }
    }

    private static void MoveOverlappingImages(Container pane, src.Image image, ArrayList<Integer> adjacency ) {
        Image compareImg;
        Rectangle imageRec = new Rectangle(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight()); 
        Rectangle compareImgRec;
        // holds movement vector
        double move_x = 0;
        double move_y = 0;
        double magnitude = 0;
        double direction = 0;
        
        Line2D line = null;
        
        Point2D[] intersections = null;
        Point2D intersectionPoint = null;
        
        for(int key =0;key<adjacency.size();key++){
            compareImg = labelImageMap.get(key);
            compareImgRec = new Rectangle(compareImg.getLocation().x, compareImg.getLocation().y, compareImg.width, compareImg.height);
            Rectangle intersection = imageRec.intersection(compareImgRec);
            if (!intersection.isEmpty()){
                
                // line joining centers
                line = new Line2D.Double(intersection.getCenterX(), intersection.getCenterY(), image.getCenter().getX(), image.getCenter().getY());
        
                // point of intersection of line with intersection rec
                intersections = getIntersectionPoint(line, intersection);
                
                for(int i =0 ; i< intersections.length;i++){
                    if(intersections[i] != null){
                        intersectionPoint = intersections[i];
                    }
                }
                // Calculating vector magnitude
                magnitude = distance(image.getCenter().getX(),image.getCenter().getY(), intersectionPoint.getX(), intersectionPoint.getY());
                direction = Math.atan((intersection.getCenterY()-image.getCenter().getY()) / (image.getCenter().getX()- intersection.getCenterX()));
            } else {
                // completely overlapped case 
                magnitude = distance(image.getCenter().getX(),image.getCenter().getY(), compareImg.getCenter().getX(), compareImg.getCenter().getY());
                direction = Math.atan((compareImg.getCenter().getY()-image.getCenter().getY()) / (image.getCenter().getX()- compareImg.getCenter().getX()));
            }
            // Resolving vector to x and y directions
            move_x += magnitude* Math.sin(direction);
            move_y += magnitude* Math.cos(direction);
        }
        
        // New location in the direction of resultant vector. 
        Point newLocation = new Point((int) (image.getLocation().x + move_x), (int)(image.getLocation().y + move_y));
        
        // Check if new location is in frame
        if(!insideFrame(newLocation)){
            // Shrink the image until no adjacencies/overlapps and to min shrink limit
            BufferedImage shrunkImg;
             while (image.getOriginal_height()/image.getHeight() <= MIN && image.getOriginal_width()/image.getWidth() <= MIN){
                shrunkImg = getScaledImage(image.img, (int) image.getWidth()/2, (int) image.getHeight()/2);
                image.setImg(shrunkImg);
                image.setHeight(shrunkImg.getHeight());
                image.setWidth(shrunkImg.getWidth());
            }
        }else{
            // Check Bounding dimensions and move image
            Dimension dimension = checkBoundingDimensions(newLocation.y+image.getHeight(), newLocation.x + image.getWidth());
            if ((int) dimension.getWidth() != (newLocation.y+image.getWidth()) || (int) dimension.getHeight() != (newLocation.y+image.getHeight()) ) {
                // Scale image to new dimension 
                image.setImg(getScaledImage(image.img, (int) dimension.getWidth(), (int) dimension.getHeight()));
                image.setHeight((int) dimension.getHeight());
                image.setWidth((int) dimension.getWidth());
            }
            image.setLocation(newLocation);
        }
        
        pane.getComponent(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());
        pane.getParent().revalidate();
        pane.getParent().repaint();
        
        // Repeat till this image overlaps with none others
        adjacency = overlappedImages(image, pane, image.getId());
        while (adjacency.size() > 0) {
            System.out.println("Image "+ image.getId()+" has "+ adjacency.size()+" overlaps.");
            // Move the overlapping images 
            MoveOverlappingImages(pane, image, adjacency);
        }

     }
                
    //this function draws lines bounding the image
    private static Line2D[] DrawPath(Image image) {
        Line2D line1 = new Line2D.Double(image.getLocation().x, image.getLocation().y, image.getLocation().x + image.getWidth() * Math.cos(image.getAngle()), image.getLocation().y + image.getWidth() * Math.sin(image.getAngle()));
        Line2D line2 = new Line2D.Double(image.getLocation().x, image.getLocation().y, image.getLocation().x + image.getHeight() * Math.sin(image.getAngle()), image.getLocation().y - image.getHeight() * Math.cos(image.getAngle()));
        Line2D line3 = new Line2D.Double(image.getLocation().x + image.getWidth() * Math.cos(image.getAngle()), image.getLocation().y + image.getWidth() * Math.sin(image.getAngle()), image.getLocation().x + image.getHeight() * Math.sin(image.getAngle()) + image.getWidth() * Math.cos(image.getAngle()), image.getLocation().y - image.getHeight() * Math.cos(image.getAngle()) + image.getWidth() * Math.sin(image.getAngle()));
        Line2D line4 = new Line2D.Double(image.getLocation().x + image.getHeight() * Math.sin(image.getAngle()), image.getLocation().y - image.getHeight() * Math.cos(image.getAngle()), image.getLocation().x + image.getHeight() * Math.sin(image.getAngle()) + image.getWidth() * Math.cos(image.getAngle()), image.getLocation().y - image.getHeight() * Math.cos(image.getAngle()) + image.getWidth() * Math.sin(image.getAngle()));
        Line2D[] lines = new Line2D[]{line1, line2, line3, line4};
        return lines;

    }

    //this function determines if bounding lines of two images are intersecting
    private static boolean IsIntersecting(Line2D[] first, Line2D[] second) {
        for (Line2D line1 : first) {
            for (Line2D line2 : second) {
                if (line1.intersectsLine(line2)) {
                    return true;
                }
            }
        }

        return false;
    }

    //this function enlarges the clicked image, need to be improved!!
    private static void MouseDetect(MouseEvent e, Container pane) {
        int x = e.getX();
        int y = e.getY();
        Image clickedimage;
        Rectangle imageloop;
        for (int key : labelImageMap.keySet()) {

            clickedimage = labelImageMap.get(key);
            imageloop = new Rectangle(pane.getComponent(key).getBounds().getLocation().x, pane.getComponent(key).getBounds().getLocation().y, clickedimage.width, clickedimage.height);
            if (imageloop.contains(new Point(x, y))) {
                System.out.println("There is a photo there");
                clickedimage.setImg(getScaledImage(clickedimage.getImg(), clickedimage.getHeight()*3/2, clickedimage.getWidth()*3/2));
                clickedimage.setHeight(clickedimage.getHeight() * 3 / 2);
                clickedimage.setWidth(clickedimage.getWidth() * 3 / 2);
                pane.getComponent(key).setBounds(clickedimage.getLocation().x, clickedimage.getLocation().y, clickedimage.getWidth(), clickedimage.getHeight());
                pane.getParent().revalidate();
                pane.getParent().repaint();
                ArrayList<Integer>  adjacency = overlappedImages(clickedimage, pane, pane.getComponentCount() - 1);
                MoveOverlappingImages(pane, clickedimage, adjacency);
                break;
            } else {
                System.out.println("There are no photos there");
            }
        }
    }

    //this function detect k nearest neighbours of the image
    private static ArrayList<Integer> Neighbours(src.Image image, int exclude) {

//        KdTree neighbourTree = new KdTree();
//
//        for (Integer key : labelImageMap.keySet()) {
//            neighbourTree.add(labelImageMap.get(key).getCenter(), key);
//        }
//
//        ArrayList<Integer> list = neighbourTree.nearestNeighbourSearch(k, image.getCenter());
//
//        return list;

         java.util.List<Integer> neighborlist = new ArrayList<>();         
         java.util.List<Integer> overlaplist = new ArrayList<>();
         java.util.List<Integer> list = new ArrayList<>();
        try {
            //KdTree neighbourTree=new KdTree();
            // 2 dimensional kd tree - initialized everytime. 
            KDTree<Integer> neighbourTree = new KDTree<>(2); 
            for(Integer key : labelImageMap.keySet()){
                if(key!=exclude){
                    //neighbourTree.add(labelImageMap.get(key).getCenter(),key);
                    double[] center = {labelImageMap.get(key).getCenter().getX(), labelImageMap.get(key).getCenter().getY()};
                    if(neighbourTree.search(center)!=null){
                        // This image list should always be empty. -- CHECK CORRECTNESS
                        overlaplist.add(key);
                    }else{
                    neighbourTree.insert(center,key); 
                    }
               }
         }
                
                // get objects in range of current image boundary
                double [] lo = {image.getCenter().getX()-xrad,image.getCenter().getY()-yrad};
                double [] hi = {image.getCenter().getX()+xrad,image.getCenter().getY()+yrad};
                neighborlist = neighbourTree.range(lo, hi);
        }
        catch (Exception e) {
	    System.err.println(e);
	}

        list.addAll(neighborlist);
        list.addAll(overlaplist);
       return (ArrayList<Integer>) list;

    }

    private static void getLargestImage(ArrayList<src.Image> images) {
        int maxH = 3000;
        Dimension minDim = new Dimension(3000, 3000);
        Dimension iDim = new Dimension();
        System.out.println(images.size());
        for (Image i : images) {
            System.out.println(i.getWidth() + "*" + i.getHeight());
            //iDim = new Dimension(i.getWidth(), i.getHeight());
//            if( (iDim.getHeight()*iDim.getWidth()) > (maxDim.getHeight()*maxDim.getWidth())){
//                maxDim = iDim;
//            }
//            if( (iDim.getHeight()*iDim.getWidth()) < (minDim.getHeight()*minDim.getWidth())){
//                minDim = iDim;
//            }
            if (maxH > i.getWidth()) {
                maxH = i.getWidth();
            }
        }

        System.out.println("================sizes( H* W)=============");
        System.out.println(maxH);
        //System.out.println(minDim.getHeight() + "*" + maxDim.getWidth());

    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1-x2), 2)+Math.pow((y1-y2), 2));
    }
   
    public static Point2D[] getIntersectionPoint(Line2D line, Rectangle2D rectangle) {

            Point2D[] p = new Point2D[4];

            // Top line
            p[0] = getIntersectionPoint(line,
                            new Line2D.Double(
                            rectangle.getX(),
                            rectangle.getY(),
                            rectangle.getX() + rectangle.getWidth(),
                            rectangle.getY()));
            // Bottom line
            p[1] = getIntersectionPoint(line,
                            new Line2D.Double(
                            rectangle.getX(),
                            rectangle.getY() + rectangle.getHeight(),
                            rectangle.getX() + rectangle.getWidth(),
                            rectangle.getY() + rectangle.getHeight()));
            // Left side...
            p[2] = getIntersectionPoint(line,
                            new Line2D.Double(
                            rectangle.getX(),
                            rectangle.getY(),
                            rectangle.getX(),
                            rectangle.getY() + rectangle.getHeight()));
            // Right side
            p[3] = getIntersectionPoint(line,
                            new Line2D.Double(
                            rectangle.getX() + rectangle.getWidth(),
                            rectangle.getY(),
                            rectangle.getX() + rectangle.getWidth(),
                            rectangle.getY() + rectangle.getHeight()));

            return p;

        }

     public static Point2D getIntersectionPoint(Line2D lineA, Line2D lineB) {

            double x1 = lineA.getX1();
            double y1 = lineA.getY1();
            double x2 = lineA.getX2();
            double y2 = lineA.getY2();

            double x3 = lineB.getX1();
            double y3 = lineB.getY1();
            double x4 = lineB.getX2();
            double y4 = lineB.getY2();

            Point2D p = null;

            double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
            if (d != 0) {
                double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
                double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

                p = new Point2D.Double(xi, yi);

            }
            return p;
        }

    private static boolean insideFrame(Point newLocation) {
        return newLocation.x < FRAME_WIDTH && newLocation.y < FRAME_HEIGHT && newLocation.x >=0 && newLocation.y >=0 ; 
    }

    private static boolean insideFrame(src.Image image) {
        return image.getLocation().x+image.getWidth() < FRAME_WIDTH && image.getLocation().y+image.getHeight() < FRAME_HEIGHT ; 
    }
    
}