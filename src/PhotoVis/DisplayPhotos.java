
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
         frame.setSize(1200, 800);
         
      //  frame.getContentPane().setLayout(new FlowLayout());

        JPanel green = new JPanel();
       
      

        JPanel yellow = new JPanel();
        yellow.setBackground(Color.YELLOW);
        yellow.setPreferredSize(new Dimension(800, 150));
        frame.getContentPane().add(yellow, BorderLayout.CENTER);
        frame.setBackground(Color.red);
        JLabel wIcon = new JLabel("Hi");
         wIcon.setBounds(0, 0, 200, 200);
         wIcon.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
         wIcon.setLocation(0, 0);
         yellow.add(wIcon);
       
         frame.pack();
         yellow.setVisible(true);
         frame.setVisible(true);
         
//         //panel.setVisible(false);
//         frame.repaint();
         
         
         //wait 
         
         for(int i =0;i<5;i++){
                long start = new Date().getTime();
                while(new Date().getTime() - start < 1000L){}
                yellow.getComponent(0).setBounds(10*i, 10*i, 100-10*i, 100-10*i);
                yellow.getComponent(0).setLocation(10*i, 10*i);
                
                
            }
         
//         // move to new location
//         System.out.println("Move");
//        // wIcon.setLocation(128, 0);
//         //frame.setSize(200, 200);
//         frame.validate();
//         frame.repaint();
         
     }
}
