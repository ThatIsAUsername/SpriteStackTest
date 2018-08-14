import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;


public class SpriteStackTest extends JFrame implements KeyListener, MouseInputListener, ActionListener
{
  private static final long serialVersionUID = -6735195822619656602L;

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
  public void mouseMoved(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
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
    gg.drawImage(mySprite.getFrame(0), 100, 100, null);
    
    g.drawImage(buffer, 0, 0, null);
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
