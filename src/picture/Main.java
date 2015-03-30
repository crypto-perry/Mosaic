package picture;

public class Main
{

  public static void main(String[] args)
  {
// picture to create mosaic onto
    Picture mos = Utils.loadPicture(args[0]);
// size of one tile
    int tS = Integer.parseInt(args[1]);
// destination string
    String dest = args[2];
// bricks to create mosaic with and find minimum size of brick
    Picture[] bricks = new Picture[args.length - 3];
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < bricks.length; i++)
    {
      bricks[i] = Utils.loadPicture(args[i + 3]);
      if (bricks[i].getWidth() < min)
        min = bricks[i].getWidth();
      if (bricks[i].getHeight() < min)
        min = bricks[i].getHeight();
    }
// average color of the bricks and make all bricks squares of size min
    Color[] avgs = new Color[bricks.length];
    for (int i = 0; i < bricks.length; i++)
    {
      bricks[i] = cropSquare(min, bricks[i]);
      avgs[i] = computeavg(bricks[i]);
    }
    
// number of tiles in width
    int nrW = mos.getWidth() / tS;
// number of tiles in height
    int nrH = mos.getHeight() / tS;
// trimmed width of new picture
    int trimW = tS * nrW;
// trimmed height of new picture
    int trimH = tS * nrH;

// dividing mosaic in tiles and making matrix with average of every tile
    int r = 0, g = 0, b = 0, p = -1, j = -1;
    Color[][] mavgs = new Color[nrW + 1][nrH + 1];
    for (int x = 0; x < trimW; x += tS)
    {
      p++;
      j = -1;
      for (int y = 0; y < trimH; y += tS)
      {
        j++;
        r = 0;
        g = 0;
        b = 0;
        for (int x1 = 0; x1 < tS; x1++)
        {
          for (int y1 = 0; y1 < tS; y1++)
          {
            r += mos.getPixel(x1 + x, y1 + y).getRed();
            g += mos.getPixel(x1 + x, y1 + y).getGreen();
            b += mos.getPixel(x1 + x, y1 + y).getBlue();
          }
        }
        r /= (tS * tS);
        g /= (tS * tS);
        b /= (tS * tS);
        mavgs[p][j] = new Color(r, g, b);
      }
    }
    
// create new picture and construct it
    p = -1;
    j = -1;
    Picture out = Utils.createPicture(nrW * min, nrH * min);
    for (int x = 0; x < nrW * min; x += min)
    {
      p++;
      j = -1;
      for (int y = 0; y < nrH * min; y += min)
      {
        j++;
        Picture fit = findbrick(bricks, avgs, mavgs[p][j]);
        for (int x1 = 0; x1 < min; x1++)
        {
          for (int y1 = 0; y1 < min; y1++)
          {
            out.setPixel(x1 + x, y1 + y, fit.getPixel(x1, y1));
          }
        }
      }
    }
    Utils.savePicture(out, dest);
  }

// function for cropping a picture to a square picture
  private static Picture cropSquare(int S, Picture pic)
  {
    Picture crop = Utils.createPicture(S, S);
    for (int x = 0; x < S; x++)
    {
      for (int y = 0; y < S; y++)
      {
        crop.setPixel(x, y, pic.getPixel(x, y));
      }
    }
    return crop;
  }

// computes the average of a color
  private static Color computeavg(Picture pic)
  {
    Color col = new Color(0, 0, 0);
    int r = 0, g = 0, b = 0;
    int h = pic.getHeight();
    int w = pic.getWidth();
    for (int y = 0; y < h; y++)
      for (int x = 0; x < w; x++)
      {
        col = pic.getPixel(x, y);
        r += col.getRed();
        g += col.getGreen();
        b += col.getBlue();
      }
    return new Color(r / (h * w), g / (h * w), b / (h * w));
  }

// finding the best brick for a certain color
  private static Picture findbrick(Picture[] bricks, Color[] avgs, Color col)
  {
    int dif = Integer.MAX_VALUE, best = 0;
    for (int i = 0; i < avgs.length; i++)
      if (getDiff(avgs[i], col) < dif)
      {
        dif = getDiff(avgs[i], col);
        best = i;
      }
    return bricks[best];
  }

// helper function for findbrick
  private static int getDiff(Color col1, Color col2)
  {
    return Math.abs(col1.getRed() - col2.getRed())
        + Math.abs(col1.getGreen() - col2.getGreen())
        + Math.abs(col1.getBlue() - col2.getBlue());
  }

}
