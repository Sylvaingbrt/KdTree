package kdtree;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main 
{
	public static void main(String[] args)
    {
        System.out.println("Entrer le nom de l'image à charger :");
        String filename = new Scanner(System.in).nextLine();
        
        try{
            File pathToFile = new File(filename);
            BufferedImage img = ImageIO.read(pathToFile);

            int imgHeight = img.getHeight();
            int imgWidth  = img.getWidth();
            BufferedImage res_img = new BufferedImage(imgWidth, imgHeight, img.getType());
            BufferedImage id_img = new BufferedImage(imgWidth, imgHeight, img.getType());
            
/////////////////////////////////////////////////////////////////
//The quantization
/////////////////////////////////////////////////////////////////
            ArrayList<RefColor> Image = new ArrayList<RefColor>(); 
            ArrayList<Point2i> Pos = new ArrayList<Point2i>(); 
            ArrayList<RefColor> palette = new ArrayList<RefColor>();
            int id = 0;
            for (int y = 0; y < imgHeight; y++) {
                for (int x = 0; x < imgWidth; x++) {
                    int Color = img.getRGB(x,y);
                    int R = (Color >> 16) & 0xff; 
                    int G = (Color >> 8) & 0xff; 
                    int B = Color & 0xff;
                    Image.add(new RefColor(R, G, B, id));
                    Pos.add(new Point2i(x,y));
                    id += 1;
                }
            }
            KdTree<RefColor> treeOfImage = new KdTree<RefColor>(3, Image, 4);
            treeOfImage.getPointsFromLeaf(palette);
            KdTree<RefColor> paletteTree = new KdTree<RefColor>(3, palette, 4);
            for(RefColor p: Image){
            	Point2i pos = Pos.get(p.getId());
            	int x = pos.get(0), y = pos.get(1);
            	RefColor voisin = (RefColor) paletteTree.getNN(p);
                int resR = voisin.get(0), resG = voisin.get(1), resB = voisin.get(2);
                int cRes = 0xff000000 | (resR << 16) 
                					  | (resG << 8)
                                      | resB;
                res_img.setRGB(x,y,cRes);
                }
/////////////////////////////////////////////////////////////////

            ImageIO.write(id_img, "jpg", new File("ResId.jpg"));
            ImageIO.write(res_img, "jpg", new File("ResColor.jpg"));
/////////////////////////////////////////////////////////////////
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
