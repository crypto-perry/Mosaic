package picture;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;

public class Picture
{
  private final BufferedImage image;
  private JFrame frame;

  protected Picture(BufferedImage image)
  {
    this.image = image;
  }

  protected BufferedImage getImage() {return image;}

  public int getWidth() {return image.getWidth();}

  public int getHeight() {return image.getHeight();}

  public Color getPixel(int x, int y)
  {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  public void setPixel(int x, int y, Color rgb)
  {

    image.setRGB(x, y, 0xff000000 | (((0xff & rgb.getRed()) << 16)
        | ((0xff & rgb.getGreen()) << 8) | (0xff & rgb.getBlue())));
  }

  public JLabel getJLabel()
  {
    if (image == null)
    {
      return null;
    }
    ImageIcon icon = new ImageIcon(image);
    return new JLabel(icon);
  }

  public void show()
  {
    // create the GUI for viewing the image if needed
    if (frame == null)
    {
      frame = new JFrame();
      frame.setContentPane(getJLabel());
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setTitle("Preview");
      frame.setResizable(true);
      frame.pack();
      frame.setVisible(true);
    }
    frame.repaint();
  }
}
