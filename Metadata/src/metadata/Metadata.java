/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/*  To change the metadata  -> writeMetaData(String filename, String keyword, String value)
    To read the metadata    -> readMetaData(String fileName , String key)
    To read the GPS info    -> readGPS(String filename)
*/

package metadata;

import com.sun.imageio.plugins.png.PNGMetadata;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javaxt.io.Image;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author oyku
 */
public class Metadata {

    public static void main(String[] args) throws IOException, Exception {
        
        String filename = "C:\\Users\\oyku\\Desktop\\Geotagged Images\\example8.png";
        
        
        /***************** To write keyword-value combination to metadata ***********************/
//        writeMetaData(filename, "VisTest", "Test is completed!");
        
       
        /***************** To read keyword to metadata ***********************/
        //String value = readMetaData(filename, "VisTest");
        //System.out.println(value);
        
        
        /***************** To read the GPS info of photo ***********************/
        readGPS(filename);
    }
    
    public static double[] readGPS(String filename) throws Exception{
       
        Image image = new Image(filename);
        double[] gps = null;
        if(image.getGPSCoordinate() != null){
            gps = image.getGPSCoordinate();
        
            System.out.println("Longitude:  " + gps[0]);
            System.out.println("Latitude:   " + gps[1]);
        }
        else{
            System.out.println("There is no GPS information in this photo");
        }
        
        
        return gps;
    }
    
    
    public static void writeMetaData(String filename, String keyword, String value) throws IOException, Exception{
        File inputfile = new File(filename);
        BufferedImage image = readImage(inputfile);
        IIOMetadataNode newMetadata = createMetaData(keyword, value);
        writeImage(inputfile, image, newMetadata);
    }
    
    public static IIOMetadataNode createMetaData(String key, String value) throws Exception {
      
        IIOMetadataNode textEntry = new IIOMetadataNode("tEXtEntry");
        textEntry.setAttribute("keyword", key);
        textEntry.setAttribute("value", value);

        IIOMetadataNode text = new IIOMetadataNode("tEXt");
        text.appendChild(textEntry);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
        root.appendChild(text);
         
        return root;
    }
    
    
    public static String readMetaData(String fileName , String key) throws IOException{
        
        File file = new File( fileName );
        ImageInputStream iis = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

        if (readers.hasNext()) {

            ImageReader reader = readers.next();
            reader.setInput(iis, true);
            IIOMetadata metadata = reader.getImageMetadata(0);
            PNGMetadata pngmeta = (PNGMetadata) metadata; 
            NodeList childNodes = pngmeta.getStandardTextNode().getChildNodes();
        
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                String keyword = node.getAttributes().getNamedItem("keyword").getNodeValue();
                String value = node.getAttributes().getNamedItem("value").getNodeValue();
                if(key.equals(keyword)){
                    return value;
                }
            }
        }
        
        return null;
    }
    
    
    private static void writeImage(File outputFile, BufferedImage image, IIOMetadataNode newMetadata) throws IOException
    {
        String extension = getFileExtension(outputFile);
        ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromBufferedImageType(image.getType());
         
        ImageOutputStream stream = null;
        try
        {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(extension);
            while(writers.hasNext())
            {
                ImageWriter writer = writers.next();
                ImageWriteParam writeParam = writer.getDefaultWriteParam();
                IIOMetadata imageMetadata = writer.getDefaultImageMetadata(imageType, writeParam);
                if (!imageMetadata.isStandardMetadataFormatSupported())
                {
                    continue;
                }
                if (imageMetadata.isReadOnly())
                {
                    continue;
                }
                 
                imageMetadata.mergeTree("javax_imageio_png_1.0", newMetadata);
                 
                IIOImage imageWithMetadata = new IIOImage(image, null, imageMetadata);
                 
                stream = ImageIO.createImageOutputStream(outputFile);
                writer.setOutput(stream);
                writer.write(null, imageWithMetadata, writeParam);
            }
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
    }
  
    
    private static BufferedImage readImage(File file) throws IOException
    {
        ImageInputStream stream = null;
        BufferedImage image = null;
        try
        {
            stream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (readers.hasNext())
            {
                ImageReader reader = readers.next();
                reader.setInput(stream);
                image = reader.read(0);
            }
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
 
        return image;
    }
    
    private static String getFileExtension(File file)
    {
        String fileName = file.getName();
        int lastDot = fileName.lastIndexOf('.');
        return fileName.substring(lastDot + 1);
    }
     
}
