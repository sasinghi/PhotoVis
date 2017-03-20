/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Preetama
 */
public class SliderTest {
    private static Date TIME_BEGIN;
     public static void main(String[] args) throws IOException {
        PhotoViewer pv = new PhotoViewer();
        
        // Add images in an ArrayList
        pv.images = pv.readImages();
        // instantiate label image map 
        pv.labelImageMap = new HashMap<>();
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        TIME_BEGIN = new Date();
        System.out.println("Begin:"+System.currentTimeMillis());
        
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
        //pv.createAndShowGUI(pv.images);
        
         JPanel left = new JPanel(null);
         left.setBackground(Color.white);
         left.setSize(300,300);
         left.setVisible(true);
         
         // TO TEST -- Change signature of addComponentsToPane(JPanel...)
         //pv.addComponentsToPane(left,pv.images);
         
         JPanel right = new JPanel(null);
         right.setBackground(Color.blue);
         right.setVisible(true);
         
         JPanel center = new JPanel(null);
         center.setBackground(Color.gray);
         center.setVisible(true);
         

         final JFrame jFrame = new JFrame();
         final PanelSlider42<JFrame> slider = new PanelSlider42<JFrame>(jFrame);
         final JPanel jPanel = slider.getBasePanel();

         slider.addComponent(left);
         slider.addComponent(right);
         slider.addComponent(center);
         
         
         jFrame.getContentPane().add(jPanel);
         jFrame.setSize(300, 300);
         jFrame.setLocationRelativeTo(null);
         jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         jFrame.setVisible(true);
        
         
         
     }
   
    
}
