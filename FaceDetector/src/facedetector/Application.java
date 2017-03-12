/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facedetector;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.google.common.collect.Lists;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JFrame;


/**
 *
 * @author oyku
 */
public class Application extends JFrame implements MouseListener {
    
    private static FaceDb db = new FaceDb();
    private static List<Rectangle> currentFaces = Lists.newArrayList();
    
    public static List<String> imagefiles = new ArrayList<String>();
    public static int index = 0;
    private boolean dialog = false;
    
    public BufferedImage input;
    public static String path = "C://Users//oyku//Desktop//deneme//";
    
    public static void main(String[] args) throws IOException {
         
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        BufferedImage tempimage = null;
        
        for (int i = 0; i < listOfFiles.length; i++) {
            String temp = listOfFiles[i].getName();
            
            if (temp.endsWith(".jpg") || temp.endsWith(".JPG") || temp.endsWith(".png") || temp.endsWith(".PNG")) {
                imagefiles.add(temp);
            }
        }
        
        
        Application gui = new Application();
        gui.setLayout(new FlowLayout());
        gui.setPreferredSize(new Dimension(900,600));
      //gui.setMaximumSize(new Dimension(1000,1000));
      //gui.setMinimumSize(new Dimension(1000,1000));

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.addMouseListener(gui);
        gui.setVisible(true);
     
        gui.pack();
    }
    
    public static void drawFaces(BufferedImage image) {
        System.out.println("Drawing Faces");
        final List<FaceDetector.PotentialFace> faces = FaceDetector.run(image, db);
        if (faces.isEmpty()) {
          return;
        }
        
        Graphics2D g2 = image.createGraphics();
        g2.setStroke(new BasicStroke(2));
        currentFaces.clear();
       
        for (FaceDetector.PotentialFace face : faces) {
            final Rectangle r = face.box;
            final Color c1 = Color.GREEN;
            
            final String msg;
            if (face.name == null || face.confidence < 60) {
                msg = "Click to tag";
            } 
            else {
                msg = String.format("%s: %f", face.name, face.confidence);
            }
            
            g2.setColor(c1);
            g2.drawRect(r.x, r.y, r.width, r.height);
            g2.drawString(msg, r.x + 5, r.y - 5);
            currentFaces.add(r);
        }
        
    }
    
    
    @Override
    public void mouseClicked(MouseEvent evt) {
        System.out.println("Mouse is clicked");
     
        if (currentFaces == null) {
            System.out.println("No face in frame");
            return;
        }
        
        dialog = true;
        System.out.println(currentFaces.size());
        for(Rectangle r : currentFaces) {
            if (r.contains(evt.getPoint())) {
                final BufferedImage clickedFace = input.getSubimage(r.x, r.y, r.width, r.height);
                final ImageIcon preview = new ImageIcon(clickedFace, "Preview");
                
                final String name = (String) JOptionPane.showInputDialog(null, "Save as?", "Tag", JOptionPane.QUESTION_MESSAGE, preview, null, null);
                
                if (name != null) {
                    System.out.println("Saving " + name + "...");
                    db.add(name, clickedFace);
                }
                
                
                break;
            }
        }
        dialog = false;

    }

    

    @Override
    public void mousePressed(MouseEvent me){
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
   @Override
    public void paint(Graphics g)  {
        super.paint(g);
        System.out.println("Painting");
        try {
            input =  ImageIO.read(new File( path + imagefiles.get(index)));
            drawFaces(input);
            g.drawImage(input, 0, 0, null);
            
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            if (index != 0 ){
                                index -= 1;
                            }
                            else{
                                index = imagefiles.size() - 1;
                            }
                            e.setKeyCode(0);
                            repaint();
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (index != imagefiles.size() - 1 ){
                                index += 1;
                            }
                            else{
                                index = 0;
                            }
                            e.setKeyCode(0);
                            repaint();
                            break;
                        default:
                            break;
                    }
                }
            });
            
        } catch (IOException ex) {
        }
        
    }
    
    private static int scale(double num, double maxNum, double maxTarget) {
        return (int) (num*maxTarget/maxNum);
    }
}
