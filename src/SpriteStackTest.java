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
  Dimension myDimension = new Dimension(500, 500);

  public SpriteStackTest()
  {
    // Load the sprite, and make the background transparent.
    mySprite = new Sprite("res/layerTank.png", 64, 64);
    //mySprite.colorize(Color.WHITE, new Color(0, 200, 200, 0));
    mySprite.setColorTransparent(Color.WHITE);
    
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
   * paints the double-buffered image to the canvas
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
    
    for( int i = 0; i < mySprite.numFrames(); ++i )
    {
      BufferedImage image = mySprite.getFrame(i);

      int w = image.getWidth();
      int h = image.getHeight();
      AffineTransform scaleTransform = new AffineTransform();
      // last-in-first-applied: rotate, scale
      //scaleTransform.scale(scaleX, scaleY);
      scaleTransform.rotate(angleRad, w / 2, h / 2);
      AffineTransformOp scaleOp = new AffineTransformOp(
              scaleTransform, AffineTransformOp.TYPE_BILINEAR);
      BufferedImage rotatedImg = scaleOp.filter(image, null);

      gg.drawImage(rotatedImg, x-(w/2), y-(h/2), null);
      y--;
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
