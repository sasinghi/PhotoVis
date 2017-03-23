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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.coobird.thumbnailator.Thumbnails;

public class PhotoViewer extends JPanel implements ActionListener {

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    static Map<Integer, Image> labelImageMap;
    static Map<Integer, Image> timelineLabelImageMap;
    static ArrayList<Image> images = new ArrayList<>();
    //frame dimensions
    static double FRAME_WIDTH;
    static double FRAME_HEIGHT;
    // Nearest neighbors parameters. range along x and y direction is 10. 
    final static int xrad = 200;
    final static int yrad = 200;
    // Minimum scale is h/2 * w/2
    // Maximum scale is h*2 * w*2
    final static double MIN = 50.0;
    final static double MAX = 9.5;
    final static double SCALE = 1;
    private static int IMAGE_TRIAL_COUNT = 0;
    private static int PACKING_TRIAL_COUNT = 0;
    private static int IMAGE_TRIAL_COUNT_1 = 0;
    private static int ENLARGE_COUNT = 2;
    public static boolean colorGroupClicked = false;
    public static boolean firstColorClicked = false;
    public static boolean secondColorClicked = false;
    private static ArrayList<JButton> labels;
    private static ArrayList<JButton> timelineLabels;
    private static Date TIME_BEGIN;
    private int FOCUS = 0;
    // TimeLine
    private static ArrayList<Integer> times;
    static HashMap<Integer, ArrayList<Image>> timeImageMap;
    static HashMap<Integer, Integer> timeBoundaryMap;
    private static boolean INTERRUPT = false;
    // UI
    private static PhoJoy frame;
    

    public PhotoViewer() {
        labels = new ArrayList<>();
        timelineLabels = new ArrayList<>();
        setFocusable(true);

    }

    @SuppressWarnings("CallToThreadDumpStack")
    private BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
        BufferedImage resizedImg = srcImg;
        try {
            resizedImg = Thumbnails.of(srcImg).size(w, h).asBufferedImage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resizedImg;
    }

    public void addComponentsToPane(final PhoJoy gui, ArrayList<Image> images, Boolean timeline, Boolean fromResolve) throws IOException {
        JTabbedPane tabPane = (JTabbedPane) gui.getContentPane().getComponent(0);
        //if(!timeline)
        JPanel pane = (JPanel) tabPane.getComponentAt(0);
        addComponentsToPane(pane, images, timeline, fromResolve);
    }

    private void addComponentsToPane(JPanel pane, ArrayList<src.Image> images, Boolean timeline, Boolean fromResolve) {
        //pane = (Container) gui.getContentPane().getComponent(1);
        //pane = gui.getContentPane();
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        double MIN_S;
        if (timeline) {
            MIN_S = (MIN / 10) - 4;
        } else {
            MIN_S = MIN;
        }


        FRAME_WIDTH = pane.getPreferredSize().width;
        FRAME_HEIGHT = pane.getPreferredSize().height;
        JButton label;
        Dimension dimension;
        Random random = new Random();

        if (images.size() >= 15 && !(timeline || fromResolve)) {
            //scale all images down  mAKE 1/10th
            BufferedImage img = null;
            for (Image image : images) {
                img = getScaledImage(image.getImg(), (int) (image.getWidth() / 40), (int) (image.getHeight() / 40));
                image.setImg(img);
                image.setHeight(img.getHeight());
                image.setWidth(img.getWidth());
            }
        }


        for (src.Image image : images) {
            dimension = checkBoundingDimensions((int) image.getOriginal_height(), (int) image.getOriginal_width());
            if ((int) dimension.getWidth() < image.getWidth() || (int) dimension.getHeight() < image.getHeight()) {
                // Scale image to new dimension 
                image.setImg(getScaledImage(image.img, (int) dimension.getWidth(), (int) dimension.getHeight()));
                image.setHeight((int) dimension.getHeight());
                image.setWidth((int) dimension.getWidth());

            }
            // Choose a random point inside frame
            int x = random.nextInt((int) FRAME_WIDTH);
            int y = random.nextInt((int) FRAME_HEIGHT);
            image.setLocation(new Point(x, y));

            if (!insideFrame(image)) {

                BufferedImage shrunkImg = null;
                double scaleDown = 1.2;
                // Try shrinking image once
                shrunkImg = getScaledImage(image.getOriginal_img(), (int) (image.getWidth() / scaleDown), (int) (image.getHeight() / scaleDown));
                image.setImg(shrunkImg);
                image.setHeight(image.getImg().getHeight());
                image.setWidth(image.getImg().getWidth());
                while (!insideFrame(image) && (image.getOriginal_width() / image.getWidth()) <= MIN_S && (image.getOriginal_height() / image.getHeight()) <= MIN_S) {
                    // Choose another random point inside frame
                    x = random.nextInt((int) FRAME_WIDTH);
                    y = random.nextInt((int) FRAME_HEIGHT);
                    image.setLocation(new Point(x, y));
                    if (insideFrame(image)) {
                        break;
                    } else {
                        // Try with even shrunk image in a new location
                        scaleDown += 0.2;
                        shrunkImg = getScaledImage(image.getOriginal_img(), (int) (image.getWidth() / scaleDown), (int) (image.getHeight() / scaleDown));
                        image.setImg(shrunkImg);
                        image.setHeight(image.getImg().getHeight());
                        image.setWidth(image.getImg().getWidth());
                    }
                }
                while (!insideFrame(image)) {
                    // Still not inside frame but MIN shrink limit reached. Choose new point. 
                    x = random.nextInt((int) FRAME_WIDTH);
                    y = random.nextInt((int) FRAME_HEIGHT);
                    image.setLocation(new Point(x, y));
                }
            }


//            // TESTING
//            if (image.getId() == 0) {
//                // set first image and center 
//                image.setLocation(new Point(40,40));
//                
//            }
//            if (image.getId() == 1) {
//                // second image does not overlap 
//                image.setLocation(new Point((int) (20), (int) (40)));
//                
//            }
//            if(image.getId() == 2){
//                image.setLocation(new Point((int) ((FRAME_WIDTH /2)-256-50), (int) ((FRAME_HEIGHT / 2)+10)));
//            }
//            // END TESTING


            image.setHeight(image.getImg().getHeight());
            image.setWidth(image.getImg().getWidth());
            image.updateCenter();

            if (timeline) {
                timelineLabelImageMap.put(pane.getComponentCount(), image);
            } else {
                // Add pair in labelImage Map
                labelImageMap.put(pane.getComponentCount(), image);
            }


            label = new JButton(new ImageIcon(image.getImg()));

            label.setOpaque(false);
            label.setContentAreaFilled(false);
            //label.setBorderPainted(false);
            label.setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            label.setName("" + image.getId());

            if (!timeline) {
                labels.add(label);
                labels.get(image.getId()).setActionCommand(label.getName());
                pane.add(labels.get(image.getId()));
            } else {
                timelineLabels.add(label);
                pane.add(timelineLabels.get(image.getId()));
            }


            //labels.get(image.getId()).addActionListener(this);
//            
//            labels.get(image.getId()).addMouseListener(new java.awt.event.MouseAdapter(){ 
//                public void mouseEntered(java.awt.event.MouseEvent evt) {
//                    System.out.println("Entered");
//                    if(evt.getSource().equals(JButton.class)){
//                        JButton button = (JButton) evt.getSource();
//                        button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 100));
//                        pane.validate();
//                        pane.repaint();
//                    }     
//                }
//
//                public void mouseExited(java.awt.event.MouseEvent evt) {
//                    System.out.println("Exited");
//                    if(evt.getSource().equals(JButton.class)){
//                        JButton button = (JButton) evt.getSource();
//                        button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
//                        pane.validate();
//                        pane.repaint();
//                    } 
//                }
//            });


            pane.getComponent(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            pane.getComponent(image.getId()).repaint();
            long start = new Date().getTime();
            while (new Date().getTime() - start < 1L) {
            }

            // Try to move image if any overlaps. 
            IMAGE_TRIAL_COUNT_1 = 0;
            double scaleDown = 1.2;
            ArrayList<Integer> adjacency = overlappedImages(image, pane, image.getId(), timeline);
            while (adjacency.size() > 0 && IMAGE_TRIAL_COUNT_1 <= (images.size() / 5)) {
                if (IMAGE_TRIAL_COUNT_1 > (images.size() / 20) && (image.getOriginal_height() / image.getHeight()) <= MIN_S && (image.getOriginal_width() / image.getWidth()) <= MIN_S && insideFrame(image)) {
                    // Tried enough times. Now shrink and try
                    animateMovement(pane, image, image.getHeight(), image.getWidth(), image.getHeight() / scaleDown, image.getWidth() / scaleDown, timeline);
                    scaleDown += 0.2;
                }
                // Move the image to escape current overlaps
                MoveOverlappingImages(pane, image, adjacency, timeline);
                if (timeline) {
                    adjacency = overlappedImages(timelineLabelImageMap.get(image.getId()), pane, image.getId(), timeline);
                } else {
                    adjacency = overlappedImages(labelImageMap.get(image.getId()), pane, image.getId(), timeline);
                }
                IMAGE_TRIAL_COUNT_1++;
            }

        }

        // For overlaps that couldn't be resolved while placing photos
        ResolveOverlaps(pane, images, timeline);



    }

    public void createAndShowGUI(ArrayList<Image> images) throws IOException {


        //frame=new CreateGUI();

        frame = new PhoJoy();





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



        // TO-DO - Make below three run in parallel. Zoom, shrink interaction

        // Default Browsing
        addComponentsToPane(frame, images, false, false);

        final JTabbedPane tabPane = (JTabbedPane) frame.getContentPane().getComponent(0);

        tabPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane panel = (JTabbedPane) e.getSource();
                    if (panel.getSelectedIndex() == 3) {
                        JPanel pane = (JPanel) tabPane.getComponentAt(3);

                        final JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File("images/"));
                        int result = fileChooser.showOpenDialog(pane);
                        File selectedFile = null;
                        if (result == JFileChooser.APPROVE_OPTION) {
                            selectedFile = fileChooser.getSelectedFile();
                        }
                        BufferedImage img = null;

                        File file = new File(selectedFile.getAbsolutePath());
                        try {
                            img = PhotoMosaic(file);

                        } catch (IOException ex) {
                            Logger.getLogger(PhotoViewer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        InteractivePanel mosaic = new InteractivePanel(img);

                        mosaic.setBounds(0, 0, pane.getWidth(), pane.getHeight());
                        pane.add(mosaic);
                        frame.revalidate();
                        frame.repaint();
                    }
                    if (panel.getSelectedIndex() == 1) {
                       

                    }
                    if(panel.getSelectedIndex() == 2){
                        
                    }
                }
            }
        });

        JPanel pane = (JPanel) tabPane.getComponentAt(1);
        System.out.println("Here");
        JPanel longPanel = new JPanel(null);
        longPanel.setPreferredSize(new Dimension(frame.getContentPane().getSize().width * 2, pane.getPreferredSize().height-15));
        //longPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));

        int prev = 0;
        JPanel timePeriod = new JPanel();
        JLabel year = new JLabel("Year");
        //       timePeriod.setBackground(Color.yellow);
        //       timePeriod.setPreferredSize(new Dimension(200, longPanel.getPreferredSize().height-17));
        //       timePeriod.setBounds(10, 10, 200, longPanel.getPreferredSize().height-17);
        //       longPanel.add(timePeriod);
        //       longPanel.revalidate();
        //       longPanel.repaint();

        JScrollPane scroll = new JScrollPane(longPanel);
        scroll.setSize(tabPane.getComponentAt(1).getSize());
        scroll.setVisible(true);
        pane.add(scroll);



        FRAME_WIDTH = longPanel.getPreferredSize().width; // EXAMPLE
        try {
            TimeLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < times.size(); i++) {
            timePeriod = new JPanel(null);
            year = new JLabel("" + times.get(i));
            int width = (int) (timeBoundaryMap.get(times.get(i)));
            //int width = 300;
            timePeriod.setPreferredSize(new Dimension(width, longPanel.getPreferredSize().height -17));
            if (i % 2 == 0) {
                timePeriod.setBackground(Color.LIGHT_GRAY);
            } else {
                timePeriod.setBackground(Color.white);
            }

            timePeriod.setBounds(prev, 30, width, longPanel.getPreferredSize().height-17);
            timePeriod.setVisible(true);
            year.setBounds(prev, 5, width, 30);
            longPanel.add(year);
            longPanel.add(timePeriod);
            timelineLabels = new ArrayList<>();
            //PhotoViewer.images = readImages();
            timelineLabelImageMap = new HashMap<>();
            addComponentsToPane(timePeriod, timeImageMap.get(times.get(i)), true, false);
            enlargeWherePossible(timePeriod, timeImageMap.get(times.get(i)), true);
            ResolveOverlaps(timePeriod, timeImageMap.get(times.get(i)), true);
            longPanel.revalidate();
            longPanel.repaint();
            //timePeriod.setBounds(prev, 10, width, pane.getMinimumSize().height);
            prev += width;

            frame.revalidate();
            frame.repaint();
        }
        
        // Geo Tags
        JPanel geopane = (JPanel) tabPane.getComponentAt(2);
        final GeoTags sample = new GeoTags();
        sample.setSize(geopane.getSize());
        geopane.add(sample);
        frame.revalidate();
        frame.repaint();
       



    }

    public static void main(String[] args) throws IOException {
        PhotoViewer pv = new PhotoViewer();
        // Add images in an ArrayList
        PhotoViewer.images = PhotoViewer.readImages();
        // instantiate label image map 
        PhotoViewer.labelImageMap = new HashMap<>();
        PhotoViewer.timelineLabelImageMap = new HashMap<>();
        TIME_BEGIN = new Date();
        System.out.println("Begin:" + System.currentTimeMillis());
        pv.createAndShowGUI(PhotoViewer.images);

//        //Create and set up the window.
//        frame = new JFrame("PhoJoy");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         //Use no layout manager
//        frame.getContentPane().setLayout(null);
//        frame.add(pv);
//        frame.getContentPane().setPreferredSize(new Dimension(1200, 780));
//        frame.pack();
//        FRAME_WIDTH = 1200;
//        FRAME_HEIGHT = 780;
    }

    public static ArrayList<Image> readImages() {
        String filename;
        BufferedImage img;
        ArrayList<Image> image = new ArrayList<>();
        //***********EXAMPLE1****************//
        //***********EXAMPLE1****************//
//        int j = 0;
//        for (int i = 8; i < 10; i++) {
//            try {
//                filename = "images/small/image" + i + ".png";
//                BufferedImage img = ImageIO.read(new File(filename));
//                image.add(new Image(img, img.getHeight(), img.getWidth(), (int) FRAME_WIDTH, (int) FRAME_HEIGHT, j));
//                j++;
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
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



        for (int i = 0; i < 100; i++) {
            try {
                filename = "images/dataset/IMG" + i + ".png";
                img = ImageIO.read(new File(filename));
                image.add(new Image(img, img.getHeight(), img.getWidth(), (int) FRAME_WIDTH, (int) FRAME_HEIGHT, i));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return image;
    }

    private static Dimension checkBoundingDimensions(int height, int width) {
        // Checks if image larger than bounding frame
        if (height < (FRAME_HEIGHT / 2) && width < (FRAME_WIDTH / 2)) {
            // Image fits, return unchanged Dimensions
            return new Dimension(width, height);
        } else {
            if (height > (FRAME_HEIGHT / 2) ) {
                while (height > (FRAME_HEIGHT / 2) && height > 10) {
                    height -= 10;
                }
            }
            if (width > (FRAME_WIDTH / 2) ) {
                while (width > (FRAME_WIDTH / 2) && width > 10) {
                    width -= 10;
                }
            }
            return new Dimension(width, height);
        }
    }

    private static ArrayList<Integer> overlappedImages(src.Image image, Container pane, int exclude, Boolean timeline) {
        ArrayList<Integer> adjacency = new ArrayList<>();

        if (pane.getComponentCount() <= 1) {
            // pane has no components yet. This is the first. So, no overlap.
            return adjacency;
        } else {
            Image compareImg;

            Rectangle imageRec;
            if (image.getId() < 0) {
                imageRec = new Rectangle(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            } else {
                imageRec = pane.getComponent(image.getId()).getBounds();
            }

            Rectangle compareImgRec;

            //this function finds 9 nearest neighbours of image
            ArrayList<Integer> list = Neighbours(image, exclude, timeline);
            //System.out.println("Image " + image.getId() + " has " + list.size() + " neighbors.");
            for (int i = 0; i < list.size(); i++) {
                int key = list.get(i);
                if (key != exclude) {
                    if (timeline) {
                        compareImg = timelineLabelImageMap.get(key);
                    } else {
                        compareImg = labelImageMap.get(key);
                    }


                    compareImgRec = pane.getComponent(compareImg.getId()).getBounds();

                    // Check if the two bounding rectangles intersect. Rotation not considered for now. 
                    // Checks - if the two images intersect, if new image completely overlaps another image & vice versa
                    if (imageRec.intersects(compareImgRec) || imageRec.contains(compareImgRec) || compareImgRec.contains(imageRec)) {
                        adjacency.add(key);
                    }
                }
            }
            // System.out.println("Image" + image.getId() + " has " + adjacency.size() + " overlaps.");
            return adjacency;
        }
    }

    private void MoveOverlappingImages(Container pane, src.Image image, ArrayList<Integer> adjacency, Boolean timeline) {
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

        double MIN_S;
        if (timeline) {
            MIN_S = (MIN / 10) - 4;
        } else {
            MIN_S = MIN;
        }

        if (INTERRUPT) {
            return;
        }

        for (int key = 0; key < adjacency.size(); key++) {
            if (timeline) {
                compareImg = timelineLabelImageMap.get(adjacency.get(key));
            } else {
                compareImg = labelImageMap.get(adjacency.get(key));
            }

            compareImgRec = pane.getComponent(compareImg.getId()).getBounds();
            Rectangle intersection = imageRec.intersection(compareImgRec);
            if (INTERRUPT) {
                return;
            }
            if (!intersection.isEmpty() && intersection.getWidth() > 0 && intersection.getHeight() > 0) {
                if (INTERRUPT) {
                    return;
                }

                // If image center is inside the intersection rectangle
                if (intersection.contains(new Point((int) imageRec.getCenterX(), (int) imageRec.getCenterY()))) {
                    // Move by disttance between centeres of recs of the two images. 
                    magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), compareImgRec.getCenterX(), compareImgRec.getCenterY());
                    // Move along intersection center to image center direction.
                    direction = Math.atan2((intersection.getCenterY() - imageRec.getCenterY()), (imageRec.getCenterX() - intersection.getCenterX()));
                } else {
                    if (INTERRUPT) {
                        return;
                    }
//                    // line joining intersection center and imageRec center
//                    line = new Line2D.Double(intersection.getCenterX(), intersection.getCenterY(), imageRec.getCenterX(), imageRec.getCenterY());
//                    // point of intersection of line with intersection rec
//                    intersections = getIntersectionPoint(line, intersection);
//                    
//                    // To get the intersection point that lies between imageRec.center and intersection.center.
//                    for (int i = 0; i < intersections.length; i++) {
//                        if (intersections[i] != null) {
//                            intersectionPoint=intersections[i];
//                        }
//                    }
                    // Calculating vector magnitude
                    magnitude = distance(intersection.getCenterX(), intersection.getCenterY(), imageRec.getCenterX(), imageRec.getCenterY());
                    //magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), intersectionPoint.getX(), intersectionPoint.getY());
                    direction = Math.atan2((intersection.getCenterY() - imageRec.getCenterY()), (imageRec.getCenterX() - intersection.getCenterX()));
                }
            } else {

                if (INTERRUPT) {
                    return;
                }

                // completely overlapped case 
                magnitude = distance(imageRec.getCenterX(), imageRec.getCenterY(), compareImgRec.getCenterX(), compareImgRec.getCenterY());
                direction = Math.atan2((compareImgRec.getCenterY() - imageRec.getCenterY()), (imageRec.getCenterX() - compareImgRec.getCenterX()));
            }
            // Resolving vector to x and y directions

            // handling special cases
            double angleNPI = direction / Math.PI;
            double angleNPIBy2 = (direction * 2) / Math.PI;
            if (angleNPI == Math.floor(angleNPI) && !Double.isInfinite(angleNPI)) {
                // angle is of form n*pi. Sine is 0. Cosine is 1.
                move_x += magnitude * Math.cos(direction);
                move_y -= 0; // y grows downwards
            } else if (angleNPIBy2 == Math.floor(angleNPIBy2) && !Double.isInfinite(angleNPIBy2)) {
                // angle is of form n*(pi/2). Sine is 1. Cosine is 0.
                move_x += 0;
                move_y -= magnitude * Math.sin(direction); // y grows downwards
            } else {
                move_x += magnitude * Math.cos(direction);
                move_y -= magnitude * Math.sin(direction); // y grows downwards
            }
        }

        // New location in the direction of resultant vector. 
        Point oldLocation = image.getLocation();

        // New location in the direction of resultant vector. 
        Point newLocation = new Point((int) (image.getLocation().x + move_x), (int) (image.getLocation().y + move_y));
        double scaleDown = 1.2;
        ArrayList<Integer> currentOverlaps = overlappedImages(image, pane, image.getId(), timeline);
        // Check if new location is in frame
        if (!insideFrame(newLocation)) {
            // Shrink image in current location-- ONLY ONCE and check if new Location this time is inside frame
            while ((currentOverlaps.size() >= adjacency.size()) && (image.getOriginal_height() / image.getHeight()) <= MIN_S && (image.getOriginal_width() / image.getWidth()) <= MIN_S && insideFrame(image)) {
                animateMovement(pane, image, image.getHeight(), image.getWidth(), image.getHeight() / scaleDown, image.getWidth() / scaleDown, adjacency, true, timeline);
                if (timeline) {
                    currentOverlaps = overlappedImages(timelineLabelImageMap.get(image.getId()), pane, image.getId(), timeline);
                } else {
                    currentOverlaps = overlappedImages(labelImageMap.get(image.getId()), pane, image.getId(), timeline);
                }

                scaleDown += 0.2;
            }
            if (timeline) {
                timelineLabelImageMap.put(image.getId(), image);
                timelineLabels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            } else {
                labelImageMap.put(image.getId(), image);
                labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            }
            pane.revalidate();
            pane.repaint();
        } else if (insideFrame(newLocation) && !insideFrame(newLocation, image)) {
            // At new location but shrinked image
            image.setLocation(newLocation);
            scaleDown = 1.2;
            while ((image.getOriginal_height() / image.getHeight()) <= MIN_S && (image.getOriginal_width() / image.getWidth()) <= MIN_S) {
                if (!timeline && insideFrame(labelImageMap.get(image.getId()))) {
                    break;
                }
                if (timeline && insideFrame(timelineLabelImageMap.get(image.getId()))) {
                    break;
                }
                animateMovement(pane, image, image.getHeight(), image.getWidth(), image.getHeight() / scaleDown, image.getWidth() / scaleDown, timeline);
                scaleDown += 0.2;
            }
            if (timeline) {
                timelineLabelImageMap.put(image.getId(), image);
                timelineLabels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            } else {
                labelImageMap.put(image.getId(), image);
                labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
            }
            pane.revalidate();
            pane.repaint();
        } else {
            // At new location with same size 
            image.setLocation(newLocation);
        }

        // update center
        image.updateCenter();
        // update image in the labelImageMap
        if (timeline) {
            timelineLabelImageMap.put(image.getId(), image);
        } else {
            labelImageMap.put(image.getId(), image);
        }

        if (INTERRUPT) {
            return;
        }

        // animate movement to new location
        animateMovement(pane, image, oldLocation, image.getLocation(), adjacency, timeline);

    }

    //this function detect k nearest neighbours of the image
    private static ArrayList<Integer> Neighbours(src.Image image, int exclude, Boolean timeline) {



        java.util.List<Integer> neighborlist = new ArrayList<>();
        java.util.List<Integer> overlaplist = new ArrayList<>();
        java.util.List<Integer> list = new ArrayList<>();
        Set<Integer> imageSet;
        double[] center = new double[2];
        try {
            //KdTree neighbourTree=new KdTree();
            // 2 dimensional kd tree - initialized everytime. 
            KDTree<Integer> neighbourTree = new KDTree<>(2);
            if (timeline) {
                imageSet = timelineLabelImageMap.keySet();
            } else {
                imageSet = labelImageMap.keySet();
            }

            for (Integer key : imageSet) {
                if (key != exclude) {
                    if (timeline) {
                        center[0] = timelineLabelImageMap.get(key).getCenter().getX();
                        center[1] = timelineLabelImageMap.get(key).getCenter().getY();
                    } else {
                        center[0] = labelImageMap.get(key).getCenter().getX();
                        center[1] = labelImageMap.get(key).getCenter().getY();
                    }
                    //neighbourTree.add(labelImageMap.get(key).getCenter(),key);

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
            if (xi <= Math.max(x1, x2) && xi >= Math.min(x1, x2) && xi <= Math.max(x3, x4) && xi >= Math.min(x3, x4) && yi >= Math.min(y1, y2) && yi >= Math.min(y3, y4) && yi <= Math.max(y1, y2) && yi <= Math.max(y3, y4)) {
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
        return (image.getLocation().x + image.getWidth()) < FRAME_WIDTH && (image.getLocation().y + image.getHeight()) < FRAME_HEIGHT;
    }

    private void ResolveOverlaps(JPanel pane, ArrayList<src.Image> images, Boolean timeline) {

        //pane = (Container) gui.getContentPane().getComponent(1);
        //pane=gui.getContentPane();

        double MIN_S;
        if (timeline) {
            MIN_S = (MIN / 10) - 4;
        } else {
            MIN_S = MIN;
        }

        ArrayList<Integer> containOverlaps = getAllOverlappingImages(pane, images, timeline);
        Random r = new Random();
        ArrayList<Integer> adjacency;
        int limit = labelImageMap.size() / 5;
        int i = 0;
        PACKING_TRIAL_COUNT = 0;
        src.Image image;
        while (containOverlaps.size() > 0 && PACKING_TRIAL_COUNT <= limit) {
            if (INTERRUPT) {
                break;
            }
            // choose a random image containing overlaps
            i = r.nextInt(containOverlaps.size());
            if (timeline) {
                image = timelineLabelImageMap.get(containOverlaps.get(i));
            } else {
                image = labelImageMap.get(containOverlaps.get(i));
            }


            //TESTING
            //Image image = labelImageMap.get(1);
            // END TESTING
            IMAGE_TRIAL_COUNT = 0;
            double scaleDown = 1.2;
            adjacency = overlappedImages(image, pane, image.getId(), timeline);
            while (adjacency.size() > 0 && IMAGE_TRIAL_COUNT <= (limit / 2)) {
                if (INTERRUPT) {
                    break;
                }
                if (IMAGE_TRIAL_COUNT > (limit / 8) && (image.getOriginal_height() / image.getHeight()) <= MIN_S && (image.getOriginal_width() / image.getWidth()) <= MIN_S && insideFrame(image)) {
                    // Tried enough times. Now shrink and try
                    animateMovement(pane, image, image.getHeight(), image.getWidth(), image.getHeight() / scaleDown, image.getWidth() / scaleDown, timeline);
                    scaleDown += 0.2;
                }
                // Move the image to escape current overlaps
                MoveOverlappingImages(pane, image, adjacency, timeline);
                if (timeline) {
                    adjacency = overlappedImages(timelineLabelImageMap.get(image.getId()), pane, image.getId(), timeline);
                } else {
                    adjacency = overlappedImages(labelImageMap.get(image.getId()), pane, image.getId(), timeline);
                }

                IMAGE_TRIAL_COUNT++;
                System.out.println("Trying again...." + IMAGE_TRIAL_COUNT);
            }
            containOverlaps = getAllOverlappingImages(pane, images, timeline);
            PACKING_TRIAL_COUNT++;
        }
        if (INTERRUPT) {
            return;
        }
        if (containOverlaps.size() > 0) {
            if (INTERRUPT) {
                return;
            }
            // After 5 tries, overlaps still exist. Change positions of all images. 
            try {
                // Remove all components
                System.out.println(containOverlaps.size() + " UNRESOLVED OVERLAPS");
                pane.removeAll();
                pane.revalidate();
                pane.repaint();

                timelineLabels = new ArrayList<>();
                labels = new ArrayList<>();
                PhotoViewer.images = readImages();
                labelImageMap = new HashMap<>();
                timelineLabelImageMap = new HashMap<>();
                //wait
                long start = new Date().getTime();
                while (new Date().getTime() - start < 1L) {
                }

                // Add images in new positions
                addComponentsToPane(pane, images, timeline, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            if (INTERRUPT) {
                return;
            }
            System.out.println("Packed." + System.currentTimeMillis());
//            while(ENLARGE_COUNT>0 && !timeline){
//                   enlargeWherePossible(pane, images,timeline);
//                   ResolveOverlaps(pane, images, timeline);
//                   ENLARGE_COUNT--;
//                }
//           
        }
    }

    private static ArrayList<Integer> getAllOverlappingImages(Container pane, ArrayList<src.Image> images, Boolean timeline) {
        ArrayList<Integer> allOverlappingImages = new ArrayList<>();
        for (Image image : images) {
            if (overlappedImages(image, pane, image.getId(), timeline).size() > 0) {
                allOverlappingImages.add(image.getId());
            }
        }
        return allOverlappingImages;
    }

    private void animateMovement(Container pane, src.Image image, Point oldLocation, Point newLocation, ArrayList<Integer> adjacency, Boolean timeline) {
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
                image.updateCenter();
                if (timeline) {
                    timelineLabelImageMap.put(image.getId(), image);
                    timelineLabels.get(image.getId()).setBounds(location.x, location.y, (int) image.getWidth(), (int) image.getHeight());
                } else {
                    labelImageMap.put(image.getId(), image);
                    labels.get(image.getId()).setBounds(location.x, location.y, (int) image.getWidth(), (int) image.getHeight());
                }
//                pane.getComponent(image.getId()).setBounds(location.x, location.y, (int)image.getWidth(), (int)image.getHeight());
//                pane.getComponent(image.getId()).repaint();
                pane.revalidate();
                pane.repaint();
                long start = new Date().getTime();
                while (new Date().getTime() - start < 1L) {
                }
                currentOverlaps = overlappedImages(image, pane, image.getId(), timeline);
                if (currentOverlaps.size() <= 0) {
                    // No overlaps
                    break;
                }
//                if(currentOverlaps.size() > adjacency.size() && IMAGE_TRIAL_COUNT<=(images.size()/10) ){
//                    // new overlaps being created, recalculate movement vector
//                    MoveOverlappingImages(pane, image, currentOverlaps);
//                    IMAGE_TRIAL_COUNT++;
//                }


            }
        }
    }

    private void animateMovement(Container pane, src.Image image, double oldHeight, double oldWidth, double newHeight, double newWidth, Boolean timeline) {
        // (x,y) = (1-t)*(x1,y1) + t*(x2,y2)
        double t = 0;
        BufferedImage img;
        if (newWidth != oldWidth && newHeight != oldHeight) {
            while (t < 1) {
                t += 0.6;
                img = getScaledImage(image.getOriginal_img(), (int) (((1 - t) * oldWidth) + (t * newWidth)), (int) (((1 - t) * oldHeight) + (t * newHeight)));
                image.setImg(img);
                image.setHeight(img.getHeight());
                image.setWidth(img.getWidth());
                image.updateCenter();
                if (timeline) {
                    timelineLabelImageMap.put(image.getId(), image);
                    timelineLabels.get(image.getId()).setIcon(new ImageIcon(img));
                    timelineLabels.get(image.getId()).setLocation(image.getLocation());
                    timelineLabels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
                } else {
                    labelImageMap.put(image.getId(), image);
                    labels.get(image.getId()).setIcon(new ImageIcon(img));
                    labels.get(image.getId()).setLocation(image.getLocation());
                    labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) image.getWidth(), (int) image.getHeight());
                }
                pane.revalidate();
                pane.repaint();
                long start = new Date().getTime();
                while (new Date().getTime() - start < 1L) {
                }
            }
        }
    }

    private void animateMovement(Container pane, src.Image image, double oldHeight, double oldWidth, double newHeight, double newWidth, ArrayList<Integer> adjacency, Boolean check, Boolean timeline) {
        // (x,y) = (1-t)*(x1,y1) + t*(x2,y2)
        double t = 0;
        BufferedImage img;
        ArrayList<Integer> currentOverlaps = new ArrayList<>();
        //if (newWidth != oldWidth && newHeight!=oldHeight) {
        while (t < 1) {
            t += 0.6;
            img = getScaledImage(image.getOriginal_img(), (int) (((1 - t) * oldWidth) + (t * newWidth)), (int) (((1 - t) * oldHeight) + (t * newHeight)));
            image.setImg(img);
            image.setHeight(img.getHeight());
            image.setWidth(img.getWidth());
            image.updateCenter();
            if (timeline) {
                timelineLabelImageMap.put(image.getId(), image);
                timelineLabels.get(image.getId()).setIcon(new ImageIcon(img));
                timelineLabels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) Math.floor(image.getWidth()), (int) Math.floor(image.getHeight()));
            } else {
                labelImageMap.put(image.getId(), image);
                labels.get(image.getId()).setIcon(new ImageIcon(img));
                labels.get(image.getId()).setBounds(image.getLocation().x, image.getLocation().y, (int) Math.floor(image.getWidth()), (int) Math.floor(image.getHeight()));
            }
            pane.revalidate();
            pane.repaint();
            long start = new Date().getTime();
            while (new Date().getTime() - start < 1L) {
            }
            currentOverlaps = overlappedImages(image, pane, image.getId(), timeline);
            if (currentOverlaps.size() <= 0 && check) {
                // No overlaps while shrinking down ONLY
                break;
            }

            //  }
        }
    }

    private static void FaceRecognition() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void Color_Grouping() {
        if (!colorGroupClicked) {
            colorGroupClicked = true;
        } else {
            firstColorClicked = false;
            secondColorClicked = false;
            colorGroupClicked = false;
        }
    }

    private static void TimeLine() throws IOException {
        timeImageMap = new HashMap<>();
        timeBoundaryMap = new HashMap<>();
        // Populate Map
        times = new ArrayList<>();
        
        for (int i = 0; i < 17; i++) {
            int j=0;
            ArrayList<Image> imgs = new ArrayList<>();
            String filename = "images/small/example1.png";
            BufferedImage img = ImageIO.read(new File(filename));
            int k = 0;
            while (k <= i) {
                imgs.add(new Image(img, img.getHeight(), img.getWidth(), (int) FRAME_WIDTH, (int) FRAME_HEIGHT, j));
                j++;
                k++;
            }
            times.add(2000 + i);
            timeImageMap.put(2000 + i, imgs);
        }

        int total = 0;
        for (int k = 0; k < times.size(); k++) {
            total += timeImageMap.get(times.get(k)).size();
        }
        int length = 0;
        System.out.println("TOTAL:" + total);
        for (int i = 0; i < times.size(); i++) {
            length = (int) (FRAME_WIDTH * timeImageMap.get(times.get(i)).size() / total);
            timeBoundaryMap.put(times.get(i), length);
        }

    }

    private static void GeoTag() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static BufferedImage PhotoMosaic(File file) throws IOException {

        PhotoMosaic mosaic = new PhotoMosaic(file);
        return mosaic.output;

    }

    private void onePhotoforColorGroup(src.Image testimg) {
        int[] testavg = ColorSimilarity.averageColor(testimg.getImg());
        double[] testavgd = ColorSimilarity.convertCIEvalues(testavg);
        for (src.Image image : images) {
            if (testimg.getId() != image.getId()) {
                int[] imageavg = ColorSimilarity.averageColor(image.getImg());
                double[] imageavgd = ColorSimilarity.convertCIEvalues(imageavg);

                double diff = ColorSimilarity.findDifference(testavgd, imageavgd);
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String image_id = ae.getActionCommand();
        int imageId = Integer.parseInt(image_id);

        JTabbedPane tabPane = (JTabbedPane) frame.getContentPane().getComponent(0);
        JPanel pane = (JPanel) tabPane.getComponentAt(0);

        if (!colorGroupClicked) {
            System.out.println("Here in action performed");
            System.out.println("Image: " + image_id + " was in focus");
            Boolean timeline = false;
            animateMovement(pane, labelImageMap.get(imageId), labelImageMap.get(imageId).getHeight(), labelImageMap.get(imageId).getWidth(), labelImageMap.get(imageId).getHeight() * 2, labelImageMap.get(imageId).getWidth() * 2, timeline);
            labels.get(imageId).setIcon(new ImageIcon(labelImageMap.get(imageId).getImg()));
            labels.get(imageId).setBounds(labelImageMap.get(imageId).getLocation().x, labelImageMap.get(imageId).getLocation().y, (int) (labelImageMap.get(imageId).getWidth()), (int) (labelImageMap.get(imageId).getHeight()));
            pane.revalidate();
            pane.repaint();
            //wait
            long start = new Date().getTime();
            while (new Date().getTime() - start < 10L) {
            }
            // ResolveOverlaps(frame, images); // PROBLEM-- TODO
        } else if (colorGroupClicked && !firstColorClicked) {
            firstColorClicked = true;
            onePhotoforColorGroup(labelImageMap.get(imageId));
            System.out.println("1st photo clicked");
        } else if (colorGroupClicked && firstColorClicked) {
            secondColorClicked = true;
            System.out.println("2nd photo clicked");
        }




    }

//    @Override
//    public void actionPerformed(ActionEvent ae) {
//        try {
//            INTERRUPT = true;
//            System.out.println("Here in action performed");
//            String image_id = ae.getActionCommand();
//            System.out.println("Image: "+image_id +" was in focus");
//            int imageId = Integer.parseInt(image_id);
//            System.out.println("Image: "+imageId +" was in focus");
//            this.animateMovement(this.pane, PhotoViewer.labelImageMap.get(imageId), PhotoViewer.labelImageMap.get(imageId).getHeight(), PhotoViewer.labelImageMap.get(imageId).getWidth(), PhotoViewer.labelImageMap.get(imageId).getHeight()*2, labelImageMap.get(imageId).getWidth()*2);
//            labels.get(imageId).setIcon(new ImageIcon(labelImageMap.get(imageId).getImg()));
//            labels.get(imageId).setBounds(labelImageMap.get(imageId).getLocation().x,labelImageMap.get(imageId).getLocation().y, (int)(labelImageMap.get(imageId).getWidth()), (int)(labelImageMap.get(imageId).getHeight()));
//            this.pane.getComponent(imageId).revalidate();
//            this.pane.getComponent(imageId).repaint();
//            
//            this.pane.revalidate();
//            this.pane.repaint();
//            
//            Thread.sleep(100);
//            INTERRUPT = false;
//            ResolveOverlaps(PhotoViewer.frame, PhotoViewer.images); // PROBLEM-- TODO
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        
//    }
    private void enlargeWherePossible(JPanel pane, ArrayList<src.Image> images, boolean timeline) {
        // Once packed, enlarge images.
        FRAME_WIDTH = pane.getPreferredSize().width;
        FRAME_HEIGHT = pane.getPreferredSize().height;
        ArrayList<Integer> containOverlaps = getAllOverlappingImages(pane, images, timeline);
        Image temp = new Image();
        double scale = 1.2;
        ArrayList<Integer> adjacency;
        if (containOverlaps.size() <= 0) {
            for (Image image : images) {
                scale = 1;
                temp = new Image();
                temp.setImg(getScaledImage(image.getOriginal_img(), (int) (image.getWidth() * scale), (int) (image.getHeight() * scale)));
                temp.setId(-1);
                temp.setHeight(temp.getImg().getHeight());
                temp.setWidth(temp.getImg().getWidth());
                temp.setLocation(image.getLocation());
                temp.updateCenter();
                adjacency = overlappedImages(temp, pane, image.getId(), timeline);
                while (adjacency.size() <= 0 && scale <= 2) {
                    if (!timeline && !insideFrame(labelImageMap.get(image.getId()))) {
                        break;
                    }
                    if (timeline && !insideFrame(timelineLabelImageMap.get(image.getId()))) {
                        break;
                    }
                    //image.setImg(temp.getImg());
                    //image.setHeight(temp.getImg().getHeight());
                    //image.setWidth(temp.getImg().getWidth());
                    animateMovement(pane, image, image.getHeight(), image.getWidth(), (image.getHeight() * scale), (image.getWidth() * scale), adjacency, false, timeline);
                    if (timeline) {
                        adjacency = overlappedImages(timelineLabelImageMap.get(image.getId()), pane, image.getId(), timeline);
                    } else {
                        adjacency = overlappedImages(labelImageMap.get(image.getId()), pane, image.getId(), timeline);
                    }
                    scale += 0.2;
                }
                if (adjacency.size() > 0) {
                    // scale back down 
                    animateMovement(pane, image, image.getHeight(), image.getWidth(), Math.floor(image.getHeight() / (scale - 0.2)), Math.floor(image.getWidth() / (scale - 0.2)), adjacency, false, timeline);
                }
            }
            //containOverlaps = getAllOverlappingImages(pane, images,timeline);
        }

        System.out.println("Enlarged where possible." + System.currentTimeMillis());

    }
}
