
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
import javax.swing.BorderFactory;
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
         frame.getContentPane().setLayout(null);
         frame.getContentPane().setPreferredSize(new Dimension(1200, 780));
         //frame.getContentPane().setBackground(Color.red);        

        frame.pack();
        frame.setVisible(true);
        
        
        JLabel wIcon = new JLabel(new ImageIcon(ImageIO.read(new File("images/small/image8.png"))));
         wIcon.setBounds(0, 0, 200, 200);
         wIcon.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
         wIcon.setLocation(0, 0);
         frame.getContentPane().add(wIcon);
       
         
         
//         //panel.setVisible(false);
//         frame.repaint();
         
         
         //wait 
         
         for(int i =0;i<5;i++){
                long start = new Date().getTime();
                while(new Date().getTime() - start < 1000L){}
                 frame.getContentPane().getComponent(0).setBounds(10*i, 10*i, 100-10*i, 100-10*i);
                 frame.getContentPane().getComponent(0).setLocation(10*i, 10*i);
                 frame.getContentPane().getComponent(0).repaint();  
            }
         
//         // move to new location
//         System.out.println("Move");
//        // wIcon.setLocation(128, 0);
//         //frame.setSize(200, 200);
//         frame.validate();
//         frame.repaint();
         
     }
}
