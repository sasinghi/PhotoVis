package src;


import java.awt.*;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Demo {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    
    //frame dimensions
    static double FRAME_WIDTH;
    static double FRAME_HEIGHT;
    
    // Minimum scale is h/2 * w/2
    // Maximum scale is h*2 * w*2
    final static double MIN = 2.0;
    final static double MAX = 0.5;
    final static double SCALE = 1;
    
     private static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public static void addComponentsToPane(Container pane) throws IOException {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	if (shouldFill) {
	//natural height, maximum width
	c.fill = GridBagConstraints.BOTH;
	}
        
        if (shouldWeightX) {
	c.weightx = 1;
        }

        String filename = "images/image"+3+".jpg";
        BufferedImage wPic = ImageIO.read(new File(filename));        
        
        //scaling height and width -- MINIMUM SCALING
        int width = (int) (wPic.getWidth()/MIN);
        int height = (int) (wPic.getHeight()/MIN);
        
        ImageIcon image1 = new ImageIcon(getScaledImage(wPic,width,height));
        JLabel label1 = new JLabel(image1);


	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 0;
        c.gridheight=1;
        c.gridwidth=1;
	pane.add(label1, c);

        filename = "images/image"+4+".jpg";
        wPic = ImageIO.read(new File(filename));        
        
        //scaling height and width -- MINIMUM SCALING
        width = (int) (wPic.getWidth()/(MIN*2));
        height = (int) (wPic.getHeight()/(MIN*2));
        
        ImageIcon image2 = new ImageIcon(getScaledImage(wPic,width,height));
        JLabel label2 = new JLabel(image2);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 1;
	c.gridy = 0;
	pane.add(label2, c);

        filename = "images/image"+1+".png";
        wPic = ImageIO.read(new File(filename));        
        
        //scaling height and width -- MINIMUM SCALING
        width = (int) (wPic.getWidth()/MIN);
        height = (int) (wPic.getHeight()/MIN);
        
        ImageIcon image3 = new ImageIcon(getScaledImage(wPic,width,height));
        JLabel label3 = new JLabel(image3);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 2;
	c.gridy = 0;
	pane.add(label3, c);
        
        filename = "images/image"+4+".jpg";
        wPic = ImageIO.read(new File(filename));        
        
        //scaling height and width -- MINIMUM SCALING
        width = (int) (wPic.getWidth()/MIN);
        height = (int) (wPic.getHeight()/MIN);
        
        ImageIcon image4 = new ImageIcon(getScaledImage(wPic,width,height));
        JLabel label4 = new JLabel(image4);
	c.fill = GridBagConstraints.BOTH;
	//c.ipady = 40;      //make this component tall
	c.weightx = 0.0;
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
        
	pane.add(label4, c);
        

	filename = "images/image"+1+".png";
        wPic = ImageIO.read(new File(filename));        
        
        //scaling height and width -- MINIMUM SCALING
        width = (int) (wPic.getWidth()/MIN);
        height = (int) (wPic.getHeight()/MIN);
        
        ImageIcon image5 = new ImageIcon(getScaledImage(wPic,width,height));
        JLabel label5 = new JLabel(image5);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 0;       //reset to default
	c.weighty = 0.0;   
	c.insets = new Insets(10,0,0,0);  //top padding
	c.gridx = 0;       //aligned with button 2
	c.gridwidth = 1;   //1 column wide
	c.gridy = 3;       //5th row
	pane.add(label5, c);
        
        filename = "images/image"+1+".png";
        wPic = ImageIO.read(new File(filename));        
        
        width = (int) (wPic.getWidth()/SCALE);
        height = (int) (wPic.getHeight()/SCALE);
        
        ImageIcon image6 = new ImageIcon(getScaledImage(wPic,width,height));
        JLabel label6 = new JLabel(image6);
	c.fill = GridBagConstraints.VERTICAL;
        c.ipadx=0;
	c.ipady = 0;       //reset to default
	c.weighty = 1.0;   //request any extra vertical space
	c.insets = new Insets(0,0,0,0);  //top padding
	c.gridx = 2;       //aligned with button 2
	c.gridheight = 2;   //2 rows wide
	c.gridy = 4;       //5th row
	pane.add(label6, c);

	
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
 
            
            @Override
            public void ancestorResized(HierarchyEvent e) {
                System.out.println("Resized:" + e.getChanged().getSize());
                FRAME_WIDTH = e.getChanged().getSize().getWidth();
                FRAME_HEIGHT = e.getChanged().getSize().getHeight(); 
            }

            @Override
            public void ancestorMoved(HierarchyEvent e) {
                //System.out.println(e);
            }
        });
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        // Wait 
        long start = new Date().getTime();
        while(new Date().getTime() - start < 10000L){}
        
        // Change position of first image
        System.out.println(frame.getContentPane().getComponents().length);
        Component component = frame.getContentPane().getComponent(0);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=0;
        
        frame.getContentPane().remove(frame.getContentPane().getComponent(0));
        
        frame.getContentPane().add(component,c);
        
        frame.revalidate();
        frame.pack();
        frame.repaint();
        
        
        
    }

    public static void main(String[] args) throws IOException {
        createAndShowGUI();
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });*/
    }
}