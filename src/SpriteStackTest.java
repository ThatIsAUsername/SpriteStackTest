import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;


public class SpriteStackTest extends JFrame implements KeyListener, MouseInputListener, ActionListener
{
  private static final long serialVersionUID = -6735195822619656602L;

  private static final double RADTODEG = 57.29578;
  private static final double DEGTORAD = 1/RADTODEG;

  private int mouseX = -1;
  private int mouseY = -1;

  Sprite mySprite = null;
  Sprite mySpriteShadow = null;
  Dimension myDimension = new Dimension(500, 500);

  private int scaleFactor = 2;

  public SpriteStackTest()
  {
    // Load the sprite, and make the background transparent.
    mySprite = new Sprite("res/layerTank.png", 64, 64);
    //mySprite.colorize(Color.WHITE, new Color(0, 200, 200, 0));
    mySprite.setColorTransparent(Color.WHITE);
    
    mySpriteShadow = makeShadowSprite(mySprite);

//    JPanel windowPane = new JPanel();
//    windowPane.setBackground(new Color(46, 196, 24));
//    windowPane.setPreferredSize(new Dimension(500, 500));
//    add(windowPane);
    setPreferredSize(myDimension);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);
    addMouseMotionListener(this);
    addMouseListener(this);
    pack();
    setVisible(true);

    // Draw the screen at (ideally) 60fps.
    javax.swing.Timer repaintTimer;
    repaintTimer = new javax.swing.Timer(16, this);
    repaintTimer.start();

  }

  public static void main(String[] args)
  {
    new SpriteStackTest();
  }

  @Override
  public void mouseDragged(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseMoved(MouseEvent event)
  {
    mouseX = event.getX();
    mouseY = event.getY();
  }

  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    // Update stuff if needed, then repaint.
    repaint();
  }

  /**
   * Paints to the canvas
   */
  @Override
  public void paint(Graphics g){

    // Create an image for double-buffering.
    BufferedImage buffer = new BufferedImage(myDimension.width, myDimension.height, BufferedImage.TYPE_INT_ARGB);
    Graphics gg = buffer.getGraphics();

    // Draw the background.
    //g.setColor(new Color(46, 196, 24));
    gg.setColor(new Color(66,134,0));
    gg.fillRect(0, 0, myDimension.width, myDimension.height);
    gg.fillRect(100, 100, 100, 50);

    // Draw the sprite
    int x = 250;
    int y = 250;
    
    // Find the angle to the mouse's location.
    double angleRad = getAngle(x, y, mouseX, mouseY) * DEGTORAD;
    
    // Draw the unit shadow
    for( int i = 0; i < mySpriteShadow.numFrames(); ++i )
    {
      BufferedImage image = mySpriteShadow.getFrame(i);

      int w = image.getWidth();
      int h = image.getHeight();
      AffineTransform scaleTransform = new AffineTransform();
      // last-in-first-applied: rotate, scale
      scaleTransform.scale(scaleFactor, scaleFactor);
      scaleTransform.rotate(angleRad, w / 2, h / 2);
      AffineTransformOp scaleOp = new AffineTransformOp(
              scaleTransform, AffineTransformOp.TYPE_BILINEAR);
      BufferedImage rotatedImg = scaleOp.filter(image, null);

      gg.drawImage(rotatedImg, x-(w*scaleFactor/2), y-(h*scaleFactor/2), null);
      x-=1*scaleFactor;
      //y+=1*scaleFactor;
    }

    // Reset the center.
    x = 250;
    y = 250;

    // Draw the unit sprite
    for( int i = 0; i < mySprite.numFrames(); ++i )
    {
      BufferedImage image = mySprite.getFrame(i);

      int w = image.getWidth();
      int h = image.getHeight();
      AffineTransform scaleTransform = new AffineTransform();
      // last-in-first-applied: rotate, scale
      scaleTransform.scale(scaleFactor, scaleFactor);
      scaleTransform.rotate(angleRad, w / 2, h / 2);
      AffineTransformOp scaleOp = new AffineTransformOp(
              scaleTransform, AffineTransformOp.TYPE_BILINEAR);
      BufferedImage rotatedImg = scaleOp.filter(image, null);

      gg.drawImage(rotatedImg, x-(w*scaleFactor/2), y-(h*scaleFactor/2), null);
      y-=1*scaleFactor;
    }
    
    g.drawImage(buffer, 0, 0, null);
  }
  
  /** Return the angle from x1,y1 to x2,y2 in degrees */
  private double getAngle(int x1, int y1, int x2, int y2) {
    double angle = (float) Math.toDegrees(Math.atan2(y2-y1, x2-x1));

    if(angle < 0){
        angle += 360;
    }

    return angle;
  }
  
  /**
   * Make a new Sprite such that every non-transparent sprite is all black,
   * and semi-transparent, so that it can be used as a shadow.
   * @param original - The sprite for which to create the shadow Sprite.
   * @return a new Sprite that can serve as a shadow for original.
   */
  private Sprite makeShadowSprite(Sprite original)
  {
    ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
    for( int i = 0; i < original.numFrames(); ++i)
    {
      BufferedImage orig = original.getFrame(i);
      BufferedImage bi = new BufferedImage(orig.getWidth(), orig.getHeight(), BufferedImage.TYPE_INT_ARGB);
      bi.getGraphics().drawImage(orig, 0, 0, null);
      int rasterSize = bi.getWidth() * bi.getHeight() * 4; // *4 for RGBA
      int[] raster = new int[rasterSize];
      bi.getRaster().getPixels(0, 0, bi.getWidth(), bi.getHeight(), raster);

      // The raster has 4 ints for each pixel; R, G, B, A. A is transparency.
      // Make any non-transparent pixel shadowy.
      for( int x = 3; x < rasterSize; x+=4 )
      {
        if(raster[x] != 0)
        {
          raster[x-3] = 0;
          raster[x-2] = 0;
          raster[x-1] = 0;
          raster[x] = 125;
        }
      }

      bi.getRaster().setPixels(0, 0, bi.getWidth(), bi.getHeight(), raster);
      images.add(bi);
    }
    Sprite shadow = new Sprite(images.get(0));
    for( int i = 1; i < images.size(); ++i)
    {
      shadow.addFrame(images.get(i));
    }
    return shadow;
  }

  
  
  @Override
  public void keyPressed(KeyEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyReleased(KeyEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyTyped(KeyEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseClicked(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }
}
