package src;

import com.jwetherell.algorithms.data_structures.KdTree;
import edu.wlu.cs.levy.CG.KDTree;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.coobird.thumbnailator.Thumbnails;

public class PhotoViewer extends JFrame implements ActionListener{
    
final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    static Map<Integer, Image> labelImageMap;
    static ArrayList<Image> images = new ArrayList<>();
    //frame dimensions
    static double FRAME_WIDTH;
    static double FRAME_HEIGHT;
    // Nearest neighbors parameters. range along x and y direction is 10. 
    final static int xrad = 1000;
    final static int yrad = 1000;
    // Minimum scale is h/2 * w/2
    // Maximum scale is h*2 * w*2
    final static double MIN = 2.0;
    final static double MAX = 0.5;
    final static double SCALE = 1;
    private static int IMAGE_TRIAL_COUNT = 0;
    private static int PACKING_TRIAL_COUNT = 0;

    private static ArrayList<JButton> labels;
    private int FOCUS = 0;
    
    Container pane;
    private static CreateGUI frame;
    
    public PhotoViewer() {
        labels = new ArrayList<>();
    }
    
    private  BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
        BufferedImage resizedImg = srcImg;
        try {
            resizedImg = Thumbnails.of(srcImg).size(w, h).asBufferedImage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resizedImg;
    }

    public  void addComponentAt(Component component, Point location, Container pane) {
    }

    public  void addComponentsToPane(final CreateGUI gui, ArrayList<Image> images1) throws IOException {
        pane = (Container) gui.getContentPane().getComponent(1);
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        FRAME_WIDTH=pane.getWidth();
        FRAME_HEIGHT=pane.getHeight();
        JButton label;
        Dimension dimension;
        Random random = new Random();
        for (src.Image image : images) {
            dimension = checkBoundingDimensions(image.getOriginal_height(), image.getOriginal_width());
            if ((int) dimension.getWidth() != image.getWidth() || (int) dimension.getHeight() != image.getHeight()) {
                // Scale image to new dimension 
                image.setImg(getScaledImage(image.img, (int) dimension.getWidth(), (int) dimension.getHeight()));
                image.setHeight((int) dimension.getHeight());
                image.setWidth((int) dimension.getWidth());

            }
            // Choose a random point inside frame
//            int x = random.nextInt((int) FRAME_WIDTH);
//            int y = random.nextInt((int) FRAME_HEIGHT);
//            image.setLocation(new Point(x, y));
//            BufferedImage shrunkImg=null;
//            while(!insideFrame(image)){
//                // Try shrinking image once
//                shrunkImg = getScaledImage(image.getImg(), (int)(image.getOriginal_width()/1.9), (int)(image.getOriginal_height()/1.9));
//                if(insideFrame(image.getLocation(),shrunkImg)){
//                    image.setImg(shrunkImg);
//                    image.setWidth(shrunkImg.getWidth());
//                    image.setHeight(shrunkImg.getHeight());
//                }else{
//                    // try a new location
//                    x = random.nextInt((int) FRAME_WIDTH);
//                    y = random.nextInt((int) FRAME_HEIGHT);
//                    image.setLocation(new Point(x, y));
//                }
//            }
            

            // TESTING
            if (image.getId() == 0) {
                // set first image and center 
                image.setLocation(new Point(0,0));
                
            }
            if (image.getId() == 1) {
                // second image does not overlap 
                image.setLocation(new Point((int) (0+256-90), (int) (0+230)));
                
            }
            if(image.getId() == 2){
                image.setLocation(new Point((int) ((FRAME_WIDTH /2)-256-50), (int) ((FRAME_HEIGHT / 2)+10)));
            }
            // END TESTING

            // Add pair in labelImage Map
            labelImageMap.put(pane.getComponentCount(), image);

            label = new JButton(new ImageIcon(image.getImg()));

            label.setOpaque(false);
            label.setContentAreaFilled(false);
            //label.setBorderPainted(false);
            label.setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());
            label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            label.setName(""+image.getId());
            

            labels.add(label);
            
            labels.get(image.getId()).addActionListener(this);
            labels.get(image.getId()).setActionCommand(label.getName());
            
            pane.add(labels.get(image.getId()));
            pane.getComponent(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());

//            while(pane.getComponent(image.getId()).getLocation().x != image.getLocation().x && pane.getComponent(image.getId()).getLocation().y != image.getLocation().y && pane.getComponent(image.getId()).getSize()!= new Dimension(image.getWidth(), image.getHeight())){
//                System.out.println("Bounds mismatch for image"+ image.getId());
//                System.out.println("Image Location"+ image.getLocation());
//                System.out.println("Component Location"+ pane.getComponent(image.getId()).getLocation());
//                System.out.print("sIZES:");
//                System.out.println(pane.getComponent(image.getId()).getSize()!= new Dimension(image.getWidth(), image.getHeight()));
//                pane.getComponent(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());  
//                //wait
//                long start = new Date().getTime();
//                while (new Date().getTime() - start < 1000L) {
//                }
//            }

            pane.getComponent(image.getId()).repaint();
            
            

            //wait
            long start = new Date().getTime();
            while (new Date().getTime() - start < 1000L) {
            }
        }

        
        Container feature_panel = (Container) gui.getContentPane().getComponent(0);
        
        ActionListener face_recognition = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               FaceRecognition();
            }};   
            
        ((JButton) feature_panel.getComponent(2)).addActionListener(face_recognition);
            
        ActionListener color_group = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color_Grouping();
            }};   
        
        ((JButton) feature_panel.getComponent(3)).addActionListener(color_group);
        
        ActionListener timeline = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    TimeLine();
            }};   
        
        ((JButton) feature_panel.getComponent(5)).addActionListener(timeline);
        
        ActionListener geotag = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    GeoTag();
            }}; 
        
        ((JButton) feature_panel.getComponent(6)).addActionListener(geotag);
        
        ActionListener photomosaic = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    PhotoMosaic();
            }};
        
        ((JButton) feature_panel.getComponent(7)).addActionListener(photomosaic);
        
        //Check for overlaps and try resolving them
        ResolveOverlaps(gui, images);
    }

           
    public void createAndShowGUI(ArrayList<Image> images) throws IOException {
        
        
            frame=new CreateGUI();
        
//        //Create and set up the window.
//        JFrame frame = new JFrame("PhoJoy");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         //Use no layout manager
//        frame.getContentPane().setLayout(null);
//        frame.getContentPane().setPreferredSize(new Dimension(1200, 780));
//        frame.pack();
//        FRAME_WIDTH = 1200;
//        FRAME_HEIGHT = 780;

       
        
//        frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener() {
//
//            @Override
//            public void ancestorResized(HierarchyEvent e) {
//                // System.out.println("Resized:" + e.getChanged().getSize());
//                FRAME_WIDTH = e.getChanged().getSize().getWidth();
//                FRAME_HEIGHT = e.getChanged().getSize().getHeight();
//                Container container = (Container) e.getChanged();
//                for (Component component : container.getComponents()) {
//                    component.setLocation((int) (FRAME_WIDTH / 1200) * component.getX(), (int) (FRAME_HEIGHT / 780) * component.getY());
//                }
//                // WRITE A GLOBAL MOVE RESIZE AND CALL HERE ---TODO
//                e.getChanged().revalidate();
//                e.getChanged().repaint();
//            }
//
//            @Override
//            public void ancestorMoved(HierarchyEvent e) {
//                //System.out.println(e);
//            }
//        });

        //Display the window.
     
        frame.setVisible(true);
        
        //wait
        long start = new Date().getTime();
        while (new Date().getTime() - start < 1000L) {
        }
       
       addComponentsToPane(frame,images);


    }

    public static void main(String[] args) throws IOException {
        PhotoViewer pv = new PhotoViewer();
        
        // Add images in an ArrayList
        pv.images = pv.readImages();
        // instantiate label image map 
        pv.labelImageMap = new HashMap<>();
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        pv.createAndShowGUI(pv.images);


    }

    private static ArrayList<Image> readImages() {
       String filename;
        ArrayList<Image> image = new ArrayList<>();
        //***********EXAMPLE1****************//
        //***********EXAMPLE1****************//
        int j = 0;
        for (int i = 8; i < 11; i++) {
            try {
                filename = "images/small/image" + i + ".png";
                BufferedImage img = ImageIO.read(new File(filename));
                image.add(new Image(img, img.getHeight(), img.getWidth(), (int) FRAME_WIDTH, (int) FRAME_HEIGHT, j));
                j++;
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
                while (height > FRAME_HEIGHT && height > 0) {
                    height -= 10;
                }
            }
            if (width > FRAME_WIDTH) {
                while (width > FRAME_WIDTH && width > 0) {
                    width -= 10;
                }
            }
            return new Dimension(width, height);
        }
    }

 
    private static ArrayList<Integer> overlappedImages(src.Image image, Container pane, int exclude) {
        ArrayList<Integer> adjacency = new ArrayList<>();

        if (pane.getComponentCount() <= 1) {
            // pane has no components yet. This is the first. So, no overlap.
            return adjacency;
        } else {
            // System.out.println(exclude + "is excluded");
            
            Image compareImg;
            
            Rectangle imageRec = pane.getComponent(image.getId()).getBounds();
            
            Rectangle compareImgRec;

            //this function finds 9 nearest neighbours of image
            ArrayList<Integer> list = Neighbours(image, exclude);
            System.out.println("Image " + image.getId() + " has " + list.size() + " neighbors.");
            for (int i = 0; i < list.size(); i++) {
                int key = list.get(i);
                if (key != exclude) {
                    compareImg = labelImageMap.get(key);
                    
                    compareImgRec = pane.getComponent(compareImg.getId()).getBounds();

                    // Check if the two bounding rectangles intersect. Rotation not considered for now. 
                    // Checks - if the two images intersect, if new image completely overlaps another image & vice versa
                    if (imageRec.intersects(compareImgRec) || imageRec.contains(compareImgRec) || compareImgRec.contains(imageRec)) {
                        adjacency.add(key);
                    }
                }
            }
            System.out.println("Image" + image.getId() + " has " + adjacency.size() + " overlaps.");
            return adjacency;
        }
    }

    private  void MoveOverlappingImages(Container pane, src.Image image, ArrayList<Integer> adjacency) {
        Image compareImg;
        Rectangle imageRec = pane.getComponent(image.getId()).getBounds();
        Rectangle compareImgRec;
        // holds movement vector
        double move_x = 0;
        double move_y = 0;
        double magnitude = 0;
        double direction = 0;

        Line2D line = null;
       
        Point2D[] intersections = null;
        Point2D intersectionPoint = null;

        for (int key = 0; key < adjacency.size(); key++) {
            compareImg = labelImageMap.get(adjacency.get(key));
            compareImgRec = pane.getComponent(compareImg.getId()).getBounds();
            Rectangle intersection = imageRec.intersection(compareImgRec);
            if (!intersection.isEmpty() && intersection.getWidth() > 0 && intersection.getHeight() > 0) {

                // If image center is inside the intersection rectangle, move by a magnitude of their distance from intersection center to image center
                if (intersection.contains(new Point((int) imageRec.getCenterX(), (int) imageRec.getCenterY()))) {
                    if(compareImgRec.contains(imageRec)){
                        // image is contained inside Compare image
                        magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), compareImgRec.getCenterX(), compareImgRec.getCenterY());
                        // from compare image center to image center. Outwards.
                        direction = Math.atan2((compareImgRec.getCenterY() - imageRec.getCenterY()) , (imageRec.getCenterX() - compareImgRec.getCenterX()));
                    }else{
                        magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), intersection.getCenterX(), intersection.getCenterY());
                        direction = Math.atan2((intersection.getCenterY() - imageRec.getCenterY()) , (imageRec.getCenterX() - intersection.getCenterX()));
                    }
                } else {
                    // line joining centers
                    line = new Line2D.Double(intersection.getCenterX(), intersection.getCenterY(), imageRec.getCenterX(), imageRec.getCenterY());
                    // point of intersection of line with intersection rec
                    intersections = getIntersectionPoint(line, intersection);
                    
                    // To get the intersection point that lies between imageRec.center and intersection.center.
                    for (int i = 0; i < intersections.length; i++) {
                        if (intersections[i] != null) {
                            intersectionPoint=intersections[i];
                        }
                    }
                    // Calculating vector magnitude
                    //magnitude = distance(intersection.getCenterX(), intersection.getCenterY(), intersectionPoint.getX(), intersectionPoint.getY());
                    magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), intersectionPoint.getX(), intersectionPoint.getY());
                    direction = Math.atan2((intersection.getCenterY() - imageRec.getCenterY()) , (imageRec.getCenterX() - intersection.getCenterX()));
                }
            } else {
                // completely overlapped case -- DOESN'T WORK. TODO.
                magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), compareImgRec.getCenterX(), compareImgRec.getCenterY());
                direction = Math.atan2((compareImgRec.getCenterY() - imageRec.getCenterY()) , (imageRec.getCenterX() - compareImgRec.getCenterX()));
            }
            
            // direction is always from compare image center to image center
            //direction = Math.atan2((compareImgRec.getCenterY() - imageRec.getCenterY()) , (imageRec.getCenterX() - compareImgRec.getCenterX()));
                   
            
            // Resolving vector to x and y directions
            
            // handling special cases
            double angleNPI = direction/Math.PI;
            double angleNPIBy2 = (direction*2)/Math.PI;
            if(angleNPI == Math.floor(angleNPI) && !Double.isInfinite(angleNPI)){
                // angle is of form n*pi. Sine is 0. Cosine is 1.
                move_x += magnitude* Math.cos(direction);
                move_y -= 0; // y grows downwards
            }else if(angleNPIBy2 == Math.floor(angleNPIBy2) && !Double.isInfinite(angleNPIBy2)){
                // angle is of form n*(pi/2). Sine is 1. Cosine is 0.
                move_x += 0;
                move_y -= magnitude* Math.sin(direction); // y grows downwards
            }else{
                move_x += magnitude * Math.cos(direction);
                move_y -= magnitude * Math.sin(direction); // y grows downwards
            } 
        }

        // New location in the direction of resultant vector. 
        Point oldLocation = image.getLocation();

        // New location in the direction of resultant vector. 
        Point newLocation = new Point((int) (image.getLocation().x + move_x), (int) (image.getLocation().y + move_y));
        BufferedImage shrunkImg = image.img;
            
        // Check if new location is in frame
        if (!insideFrame(newLocation) ) {
            // Shrink image and try resolving overlaps again.
            while (image.getOriginal_height() / image.getHeight() <= MIN && image.getOriginal_width() / image.getWidth() <= MIN && insideFrame(image)) {
                shrunkImg = getScaledImage(image.img, (int) (image.getWidth()/1.9), (int) (image.getHeight()/1.9));
                image.setImg(shrunkImg);
                image.setHeight(shrunkImg.getHeight());
                image.setWidth(shrunkImg.getWidth());
            }
            labels.get(image.getId()).setIcon(new ImageIcon(shrunkImg));
            labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());
            pane.revalidate();
            pane.repaint();
            MoveOverlappingImages(pane, image, adjacency);
        } else if (insideFrame(newLocation) && !insideFrame(newLocation, image)) {
            // At new location but shrinked image
            image.setLocation(newLocation);
            System.out.println("Here in new location");
            while (!insideFrame(image) && image.getOriginal_height() / image.getHeight() <= MIN && image.getOriginal_width() / image.getWidth() <= MIN) {
                shrunkImg = getScaledImage(image.img, (int) (image.getWidth()/1.9), (int) (image.getHeight()/1.9));
                System.out.println("Shrinked in new location");
                image.setImg(shrunkImg);
                image.setHeight(shrunkImg.getHeight());
                image.setWidth(shrunkImg.getWidth());
            } 
            labels.get(image.getId()).setIcon(new ImageIcon(shrunkImg));
            labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());
            pane.revalidate();
            pane.repaint();
            MoveOverlappingImages(pane, image, adjacency);
        }else{
            // At new location with same size 
            image.setLocation(newLocation);
        }
        
        // TESTING -- SCALING AND UPDATING
//        BufferedImage shrunkImg = getScaledImage(image.img, (int) (image.getWidth()/1.1), (int) (image.getHeight()/1.1));
//        System.out.println("Shrinked in new location");
//        image.setImg(shrunkImg);
//        image.setHeight(shrunkImg.getHeight());
//        image.setWidth(shrunkImg.getWidth());
//        labels.get(image.getId()).setIcon(new ImageIcon(shrunkImg));
//        labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, image.getWidth(), image.getHeight());
//
//        pane.revalidate();
//        pane.repaint();

        // update image in the labelImageMap
        labelImageMap.put(image.getId(), image);      

        // animate movement to new location
        animateMovement(pane, image, oldLocation, image.getLocation(),adjacency);

    }


    //this function detect k nearest neighbours of the image
    private static ArrayList<Integer> Neighbours(src.Image image, int exclude) {



        java.util.List<Integer> neighborlist = new ArrayList<>();
        java.util.List<Integer> overlaplist = new ArrayList<>();
        java.util.List<Integer> list = new ArrayList<>();
        try {
            //KdTree neighbourTree=new KdTree();
            // 2 dimensional kd tree - initialized everytime. 
            KDTree<Integer> neighbourTree = new KDTree<>(2);
            for (Integer key : labelImageMap.keySet()) {
                if (key != exclude) {
                    //neighbourTree.add(labelImageMap.get(key).getCenter(),key);
                    double[] center = {labelImageMap.get(key).getCenter().getX(), labelImageMap.get(key).getCenter().getY()};
                    if (neighbourTree.search(center) != null) {
                        // This image list should always be empty. -- CHECK CORRECTNESS
                        overlaplist.add(key);
                    } else {
                        neighbourTree.insert(center, key);
                    }
                }
            }

            // get objects in range of current image boundary
            double[] lo = {image.getCenter().getX() - xrad, image.getCenter().getY() - yrad};
            double[] hi = {image.getCenter().getX() + xrad, image.getCenter().getY() + yrad};
            neighborlist = neighbourTree.range(lo, hi);
        } catch (Exception e) {
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
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
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
            
            // check if point of intersection is within line "segment" bounds
            if( xi <= Math.max(x1,x2) && xi >= Math.min(x1,x2) && xi <= Math.max(x3,x4) && xi >= Math.min(x3,x4) && yi >= Math.min(y1,y2) && yi >= Math.min(y3,y4) && yi <= Math.max(y1,y2) && yi <= Math.max(y3,y4) ){
                p = new Point2D.Double(xi, yi);
            }
        }
        return p;
    }

     private static boolean insideFrame(Point newLocation) {
        return newLocation.x < FRAME_WIDTH && newLocation.y < FRAME_HEIGHT && newLocation.x >= 0 && newLocation.y >= 0;
    }

    private static boolean insideFrame(Point newLocation, Image image) {
        return (newLocation.x + image.getWidth()) < FRAME_WIDTH && (newLocation.y + image.getHeight()) < FRAME_HEIGHT;
    }
    
    private static boolean insideFrame(Point newLocation, BufferedImage image) {
        return (newLocation.x + image.getWidth()) < FRAME_WIDTH && (newLocation.y + image.getHeight()) < FRAME_HEIGHT;
    }

    private static boolean insideFrame(src.Image image) {
        return image.getLocation().x + image.getWidth() < FRAME_WIDTH && image.getLocation().y + image.getHeight() < FRAME_HEIGHT;
    }


    private  void ResolveOverlaps(CreateGUI gui, ArrayList<src.Image> images) {
        pane = (Container) gui.getContentPane().getComponent(1);
        ArrayList<Integer> containOverlaps = getAllOverlappingImages(pane, images);
        Random r = new Random();
        ArrayList<Integer> adjacency;
        int limit = containOverlaps.size();
        PACKING_TRIAL_COUNT = 0;
        while (containOverlaps.size() > 0 && PACKING_TRIAL_COUNT <= limit) {
            // choose a random image containing overlaps
            // i = r.nextInt(containOverlaps.size());
            //Image image = labelImageMap.get(containOverlaps.get(i));

            //TESTING
            Image image = labelImageMap.get(1);
            // END TESTING
            IMAGE_TRIAL_COUNT = 0;
            adjacency = overlappedImages(image, pane, image.getId());
            while (adjacency.size() > 0 && IMAGE_TRIAL_COUNT <= 5) {
                // Move the image to escape current overlaps
                MoveOverlappingImages(pane, image, adjacency);
                adjacency = overlappedImages(image, pane, image.getId());
                IMAGE_TRIAL_COUNT++;
                System.out.println("Trying again...."+ IMAGE_TRIAL_COUNT);
            }
            containOverlaps = getAllOverlappingImages(pane, images);
            PACKING_TRIAL_COUNT++;
        }
        if (containOverlaps.size() > 0) {
            // After 5 tries, overlaps still exist. Change positions of all images. 
            try {
                // Remove all components

                    pane.removeAll();
                    pane.revalidate();
                    pane.repaint();
                    //wait
                    long start = new Date().getTime();
                    while (new Date().getTime() - start < 1000L) {}
               // Add images in new positions
                addComponentsToPane(gui, images);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Packed.");
        }
    }

    private static ArrayList<Integer> getAllOverlappingImages(Container pane, ArrayList<src.Image> images) {
         ArrayList<Integer> allOverlappingImages = new ArrayList<>();
        for (Image image : images) {
            if (overlappedImages(image, pane, image.getId()).size() > 0) {
                allOverlappingImages.add(image.getId());
            }
        }
        return allOverlappingImages;
    }
    private  void animateMovement(Container pane, src.Image image, Point oldLocation, Point newLocation, ArrayList<Integer> adjacency) {
        // (x,y) = (1-t)*(x1,y1) + t*(x2,y2)
        double t = 0;
        Point location = new Point();
        ArrayList<Integer> currentOverlaps = new ArrayList<>();
        if (!oldLocation.equals(newLocation)) {
            while (t <= 1) {
                t += 0.2;
                location.x = (int) ((1 - t) * oldLocation.x + t * newLocation.x);
                location.y = (int) ((1 - t) * oldLocation.y + t * newLocation.y);
                image.setLocation(location);
                pane.getComponent(image.getId()).setBounds(location.x, location.y, image.getWidth(), image.getHeight());
                pane.getComponent(image.getId()).repaint();
                currentOverlaps = overlappedImages(image, pane, image.getId());
                if(currentOverlaps.size()<=0){
                    // No overlaps
                    break;
                }
                if(currentOverlaps.size() > adjacency.size() && IMAGE_TRIAL_COUNT<=5){
                    // new overlaps being created, recalculate movement vector
                    MoveOverlappingImages(pane, image, currentOverlaps);
                    IMAGE_TRIAL_COUNT++;
                }
                
                //wait
                long start = new Date().getTime();
                while (new Date().getTime() - start < 1000L) {
                }
            }
        }
    }
    
    private static void FaceRecognition() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void Color_Grouping() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void TimeLine() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void GeoTag() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void PhotoMosaic() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Here in action performed");
        String image_id = ae.getActionCommand();
        System.out.println("Image: "+image_id +" was in focus");
        int imageId = Integer.parseInt(image_id);
        System.out.println("Image: "+imageId +" was in focus");
        BufferedImage scaledImg = getScaledImage(images.get(imageId).img, images.get(imageId).getOriginal_width()*2,images.get(imageId).getOriginal_height()*2) ;
        images.get(imageId).setImg(scaledImg);
        images.get(imageId).setWidth(scaledImg.getWidth());
        images.get(imageId).setHeight(scaledImg.getHeight());
        labelImageMap.put(imageId, images.get(imageId));
        labels.get(imageId).setIcon(new ImageIcon(images.get(imageId).img));
        labels.get(imageId).setBounds(images.get(imageId).getLocation().x,images.get(imageId).getLocation().y, images.get(imageId).getWidth(), images.get(imageId).getHeight());
        pane.revalidate();
        pane.repaint();
        //wait
        long start = new Date().getTime();
        while (new Date().getTime() - start < 1000L) {
        }
        ResolveOverlaps(frame, images); // PROBLEM-- TODO
        
    }
}
