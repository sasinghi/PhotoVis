
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sasinghi
 */
public class DisplayPhotos  {
     public static void main (String args[]) throws IOException {    
         JFrame frame = new JFrame("Photo Viewer");
         frame.setSize(1200, 800);
         JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,100,100));
      
      //  frame.getContentPane().setLayout(new FlowLayout());

        JPanel green = new JPanel();
       
      

        JPanel yellow = new JPanel();
        yellow.setBackground(Color.YELLOW);
        yellow.setPreferredSize(new Dimension(80, 150));
        frame.getContentPane().add(yellow, BorderLayout.CENTER);
        frame.setBackground(Color.red);
        /* BufferedImage wPic = ImageIO.read(new File(filename));
         JLabel wIcon = new JLabel(new ImageIcon(wPic));
          
         wIcon.setLocation(0, 0);
         wIcon.setPreferredSize(new Dimension(200,200));
         wIcon.setSize(100, 100);
         panel.add(wIcon);*/
       
         //frame.pack();
         panel.setVisible(true);
         frame.setVisible(true);
         
         //panel.setVisible(false);
         frame.repaint();
         
         
         //wait 
         long start = new Date().getTime();
         while(new Date().getTime() - start < 1000L){}
         
         // move to new location
         System.out.println("Move");
        // wIcon.setLocation(128, 0);
         //frame.setSize(200, 200);
         frame.validate();
         frame.repaint();
         
     }
}
