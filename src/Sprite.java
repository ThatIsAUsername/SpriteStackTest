import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Holds a collection of related images, e.g. an animation sequence.
 */
public class Sprite
{
  private ArrayList<BufferedImage> spriteImages;

  public Sprite(BufferedImage baseSprite)
  {
    if( null == baseSprite )
    {
      System.out.println("WARNING! Sprite() given null image. Continuing with placeholder.");
      // If we are given an invalid image, just use a default that should stick out like a sore thumb.
      baseSprite = createDefaultBlankSprite(100, 100);
    }
    spriteImages = new ArrayList<BufferedImage>();
    spriteImages.add(baseSprite);
  }

  /**
   * Attempts to parse the given string as the location of a spritesheet, and create a sprite from it.
   */
  public Sprite(String filename, int spriteWidthPx, int spriteHeightPx)
  {
    BufferedImage spriteSheet = null;
    try
    {
      spriteSheet = ImageIO.read(new File(filename));
    }
    catch (IOException ioex)
    {
      System.out.println("WARNING! Exception loading resource file " + filename);
      spriteSheet = null;
    }

    spriteImages = new ArrayList<BufferedImage>();
    if( null == spriteSheet || spriteHeightPx > spriteSheet.getHeight() || spriteWidthPx > spriteSheet.getWidth() )
    {
      //If we are given an invalid image, just use a default that should stick out like a sore thumb.
      System.out.println("WARNING! Failed to create Sprite from filename. Continuing with placeholder.");
      spriteImages.add(createDefaultBlankSprite(spriteWidthPx, spriteHeightPx));
    }
    else
    {
      // Start at the beginning of the sprite sheet.
      int xOffset = 0;
      int yOffset = 0;
      int spriteNum = 0;

      // Cut the sprite-sheet into individual frames.
      while ((spriteNum + 1) * spriteWidthPx <= spriteSheet.getWidth())
      {
        BufferedImage frame = createDefaultBlankSprite(spriteWidthPx, spriteHeightPx);
        Graphics g = frame.getGraphics();
        g.drawImage(spriteSheet.getSubimage(xOffset, yOffset, spriteWidthPx, spriteHeightPx), 0, 0, spriteWidthPx, spriteHeightPx, null);
        spriteImages.add(frame);
        xOffset += spriteWidthPx;
        spriteNum++;
      }
    }
  }

  /**
   * Parses the provided sprite sheet into individual, like-sized frames, with the assumption
   * that the sprite images are arranged horizontally.
   * If the sprite sheet is taller than the provided height, the extra image space is ignored.
   * If the sprite sheet width does not divide evenly, then the final (too-short) section
   * of the image will NOT be included as a separate frame.
   * @param spriteSheet The image to parse into frames.
   * @param spriteWidthPx The width of each frame in pixels.
   * @param spriteHeightPx The height of each frame in pixels.
   */
  public Sprite(BufferedImage spriteSheet, int spriteWidthPx, int spriteHeightPx)
  {
    spriteImages = new ArrayList<BufferedImage>();

    if( null == spriteSheet || spriteHeightPx > spriteSheet.getHeight() || spriteWidthPx > spriteSheet.getWidth() )
    {
      System.out.println("WARNING! Sprite() given invalid sprite sheet. Creating placeholder image.");
      // Just make a single blank frame of the specified size.
      spriteImages.add(createDefaultBlankSprite(spriteWidthPx, spriteHeightPx));
    }
    else
    {
      // Start at the beginning of the sprite sheet.
      int xOffset = 0;
      int yOffset = 0;
      int spriteNum = 0;

      // Cut the sprite-sheet into individual frames.
      while ((spriteNum + 1) * spriteWidthPx <= spriteSheet.getWidth())
      {
        BufferedImage frame = createDefaultBlankSprite(spriteWidthPx, spriteHeightPx);
        Graphics g = frame.getGraphics();
        g.drawImage(spriteSheet.getSubimage(xOffset, yOffset, spriteWidthPx, spriteHeightPx), 0, 0, spriteWidthPx, spriteHeightPx, null);
        spriteImages.add(frame);
        xOffset += spriteWidthPx;
        spriteNum++;
      }
    }
  }

  /**
   * Sprite copy-constructor. Perform a deep-copy on each of the other sprite's frames.
   * @param other
   */
  public Sprite(Sprite other)
  {
    spriteImages = new ArrayList<BufferedImage>();

    if( null == other )
    {
      System.out.println("WARNING! Sprite() given null Sprite. Creating placeholder image.");
      // Just make a single blank frame of the specified size.
      spriteImages.add(createDefaultBlankSprite(100, 100));
    }
    else
    {
      for( int i = 0; i < other.numFrames(); ++i )
      {
        BufferedImage aFrame = other.getFrame(i);
        BufferedImage myFrame = new BufferedImage(aFrame.getWidth(), aFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        myFrame.getGraphics().drawImage(aFrame, 0, 0, null);
        spriteImages.add(myFrame);
      }
    }
  }

  public int numFrames()
  {
    return spriteImages.size();
  }

  /**
   * Adds another frame to this sprite.
   * @param sprite
   */
  public void addFrame(BufferedImage sprite)
  {
    spriteImages.add(sprite);
  }

  public BufferedImage getFrame(int index)
  {
    // Normalize the index if needed.
    for(; index >= spriteImages.size() || index < 0; index += spriteImages.size() * ((index < 0)? 1:-1));
    return spriteImages.get(index);
  }

  /**
   * For every image contained in this sprite, change each pixel with a value in oldColors to the corresponding value in newColors.
   */
  public void colorize(Color[] oldColors, Color[] newColors)
  {
    for( BufferedImage bi : spriteImages )
    {
      for( int x = 0; x < bi.getWidth(); ++x )
      {
        for( int y = 0; y < bi.getHeight(); ++y )
        {
          int colorValue = bi.getRGB(x, y);
          for( int c = 0; c < oldColors.length; ++c )
          {
            if( oldColors[c].getRGB() == colorValue )
            {
              bi.setRGB(x, y, newColors[c].getRGB());
            }
          }
        }
      }
    }
  }

  /**
   * For every image contained in this sprite, change each oldColor pixel to newColor.
   */
  public void colorize(Color oldColor, Color newColor)
  {
    for( BufferedImage bi : spriteImages )
    {
      for( int x = 0; x < bi.getWidth(); ++x )
      {
        for( int y = 0; y < bi.getHeight(); ++y )
        {
          int colorValue = bi.getRGB(x, y);
          if( oldColor.getRGB() == colorValue )
          {
            bi.setRGB(x, y, newColor.getRGB());
          }
        }
      }
    }
  }

  /**
   * Sets every pixel of the given color to transparent, but otherwise preserves the pixel's
   * color (so it could be made non-transparent again, in theory). This method relies on the
   * Sprite being composed of ARGB images, as it makes assumptions about the data representation
   * of the underlying raster.
   * @param color The Color to set transparent in this Sprite.
   */
  public void setColorTransparent(Color color)
  {
    for( BufferedImage bi : spriteImages )
    {
      int rasterSize = bi.getWidth() * bi.getHeight() * 4; // *4 for RGBA
      int[] raster = new int[rasterSize];
      bi.getRaster().getPixels(0, 0, bi.getWidth(), bi.getHeight(), raster);

      // The raster has 4 ints for each pixel; R, G, B, A. A is transparency.
      // Therefore we will hit index 3 and every 4th thereafter, and set it
      // transparent if needed.
      for( int x = 3; x < rasterSize; x+=4 )
      {
        if((color.getRed() == raster[x-3])
            && (color.getGreen() == raster[x-2])
            && (color.getBlue() == raster[x-1]))
        {
          raster[x] = 0;
        }
      }

      bi.getRaster().setPixels(0, 0, bi.getWidth(), bi.getHeight(), raster);
    }
  }

  /**
   * Creates a new blank (all black) image of the given size. This is used to generate placeholder
   * assets on the fly when we fail to load resources from disk.
   * @param w Desired width of the placeholder image.
   * @param h Desired height of the placeholder image.
   * @return A new BufferedImage of the specified size, filled in with all black.
   */
  private static BufferedImage createDefaultBlankSprite(int w, int h)
  {
    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics big = bi.getGraphics();
    big.setColor(Color.BLACK);
    big.fillRect(0, 0, w, h);
    return bi;
  }
}
