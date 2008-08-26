using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace Chapter12OCR
{
    public class DownSample
    {
        private Bitmap image;
        private int downSampleTop;
        private int downSampleBottom;
        private int downSampleLeft;
        private int downSampleRight;
        private double ratioX;
        private double ratioY;

        public DownSample(Bitmap image)
        {
            this.image = image;
        }

        public bool[] downSample(int downSampleWidth, int downSampleHeight)
        {
            int size = downSampleWidth * downSampleHeight;
            bool[] result = new bool[size];

            findBounds();

            // now downsample

			this.ratioX = (double) (this.downSampleRight - this.downSampleLeft)
                    / (double)downSampleWidth;
			this.ratioY = (double) (this.downSampleBottom - this.downSampleTop)
                    / (double)downSampleHeight;

            int index = 0;
            for (int y = 0; y < downSampleHeight; y++)
            {
                for (int x = 0; x < downSampleWidth; x++)
                {
					result[index++] = downSampleRegion(x, y);
				}
			}




            return result;
        }


        	/**
	 * Called to downsample a quadrant of the image.
	 * 
	 * @param x
	 *            The x coordinate of the resulting downsample.
	 * @param y
	 *            The y coordinate of the resulting downsample.
	 * @return Returns true if there were ANY pixels in the specified quadrant.
	 */
	protected bool downSampleRegion( int x,  int y) {
		 int startX = (int) (this.downSampleLeft + (x * this.ratioX));
		 int startY = (int) (this.downSampleTop + (y * this.ratioY));
		 int endX = (int) (startX + this.ratioX);
		 int endY = (int) (startY + this.ratioY);

		for (int yy = startY; yy <= endY; yy++) {
			for (int xx = startX; xx <= endX; xx++) {

                Color pixel = this.image.GetPixel(xx, yy);
				if (isBlack(pixel)) {
					return true;
				}
			}
		}

		return false;
	}



        /**
 * This method is called to automatically crop the image so that whitespace
 * is removed.
 * 
 * @param w
 *            The width of the image.
 * @param h
 *            The height of the image
 */
        protected void findBounds()
        {
            int h = image.Height;
            int w = image.Width;

            // top line
            for (int y = 0; y < h; y++)
            {
                if (!hLineClear(y))
                {
                    this.downSampleTop = y;
                    break;
                }

            }
            // bottom line
            for (int y = h - 1; y >= 0; y--)
            {
                if (!hLineClear(y))
                {
                    this.downSampleBottom = y;
                    break;
                }
            }
            // left line
            for (int x = 0; x < w; x++)
            {
                if (!vLineClear(x))
                {
                    this.downSampleLeft = x;
                    break;
                }
            }

            // right line
            for (int x = w - 1; x >= 0; x--)
            {
                if (!vLineClear(x))
                {
                    this.downSampleRight = x;
                    break;
                }
            }
        }

        /**
 * This method is called internally to see if there are any pixels in the
 * given scan line. This method is used to perform autocropping.
 * 
 * @param y
 *            The horizontal line to scan.
 * @return True if there were any pixels in this horizontal line.
 */
        protected bool hLineClear(int y)
        {
            int w = this.image.Width;
            for (int i = 0; i < w; i++)
            {
                Color pixel = this.image.GetPixel(i, y);
                if (isBlack(pixel))
                {
                    return false;
                }
            }
            return true;
        }

        /**
         * This method is called to determine ....
         * 
         * @param x
         *            The vertical line to scan.
         * @return True if there are any pixels in the specified vertical line.
         */
        protected bool vLineClear(int x)
        {
            int w = this.image.Width;
            int h = this.image.Height;
            for (int i = 0; i < h; i++)
            {
                Color pixel = this.image.GetPixel(x, i);
                if (isBlack(pixel))
                {
                    return false;
                }
            }
            return true;
        }

        protected bool isBlack(Color pixel)
        {
            return(pixel.R != 255 || pixel.G != 255 || pixel.B != 255);
        }

    }
}
