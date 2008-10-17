using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace Encog.Util.DownSample
{
    class SimpleIntensityDownsample : IDownSample
    {
        Bitmap Image
        {
            get
            {
                return this.Image;
            }
        }

        int[] PixelMap
        {
            get
            {
                return this.pixelMap;
            }
        }

        double RatioX
        {
            get
            {
                return this.ratioX;
            }
        }

        double RatioY
        {
            get
            {
                return this.ratioY;
            }
        }

        int ImageHeight
        {
            get
            {
                return this.imageHeight;
            }
        }

        int ImageWidth
        {
            get
            {
                return this.imageWidth;
            }
        }

        int DownSampleLeft
        {
            get
            {
                return this.downSampleLeft;
            }
        }

        int DownSampleRight
        {
            get
            {
                return this.downSampleRight;
            }
        }

        int DownSampleTop
        {
            get
            {
                return this.downSampleTop;
            }
        }

        int DownSampleBottom
        {
            get
            {
                return this.downSampleBottom;
            }
        }



        private Image image;
        private int[] pixelMap;
        private double ratioX;
        private double ratioY;
        private int imageHeight;
        private int imageWidth;
        private int downSampleLeft;
        private int downSampleRight;
        private int downSampleTop;
        private int downSampleBottom;

        public SimpleIntensityDownsample(Image image)
        {
            processImage(image);
        }

        public void ProcessImage(Bitmap image)
        {
            this.image = image;
            ImageSize size = new ImageSize(image);
            this.imageHeight = size.getHeight();
            this.imageWidth = size.getWidth();
            this.downSampleLeft = 0;
            this.downSampleTop = 0;
            this.downSampleRight = this.imageWidth;
            this.downSampleBottom = this.imageHeight;
        }

        /// <summary>
        /// Called to downsample the image and store it in the down sample component.
        /// </summary>
        /// <param name="height">The height of the image.</param>
        /// <param name="width">The width of the image</param>
        /// <returns></returns>
        public double[] DownSample(int height, int width)
        {

            double[] result = new double[height * width];

            PixelGrabber grabber = new PixelGrabber(this.image, 0, 0,
                   this.imageWidth, this.imageWidth, true);

            try
            {
                grabber.grabPixels();
            }
            catch (InterruptedException e)
            {
                throw new EncogError(e);
            }

            this.pixelMap = (int[])grabber.getPixels();

            // now downsample

            this.ratioX = (double)(this.downSampleRight - this.downSampleLeft)
                    / (double)width;
            this.ratioY = (double)(this.downSampleBottom - this.downSampleTop)
                    / (double)height;

            int index = 0;
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
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
        private double DownSampleRegion(int x, int y)
        {
            int startX = (int)(this.downSampleLeft + (x * this.ratioX));
            int startY = (int)(this.downSampleTop + (y * this.ratioY));
            int endX = (int)(startX + this.ratioX);
            int endY = (int)(startY + this.ratioY);

            int redTotal = 0;
            int greenTotal = 0;
            int blueTotal = 0;

            int total = 0;

            for (int yy = startY; yy <= endY; yy++)
            {
                for (int xx = startX; xx <= endX; xx++)
                {
                    int loc = xx + (yy * this.imageWidth);
                    int pixel = this.pixelMap[loc];
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;

                    redTotal += red;
                    greenTotal += green;
                    blueTotal += blue;
                    total++;

                    if (this.pixelMap[loc] != -1)
                    {
                        return 1.0;
                    }
                }
            }

            return (redTotal + greenTotal + blueTotal) / (total * 3);
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
        public void FindBounds()
        {
            // top line
            for (int y = 0; y < this.imageHeight; y++)
            {
                if (!hLineClear(y))
                {
                    this.downSampleTop = y;
                    break;
                }

            }
            // bottom line
            for (int y = this.imageHeight - 1; y >= 0; y--)
            {
                if (!hLineClear(y))
                {
                    this.downSampleBottom = y;
                    break;
                }
            }
            // left line
            for (int x = 0; x < this.imageWidth; x++)
            {
                if (!vLineClear(x))
                {
                    this.downSampleLeft = x;
                    break;
                }
            }

            // right line
            for (int x = this.imageWidth - 1; x >= 0; x--)
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
        private bool HLineClear(int y)
        {
            for (int i = 0; i < this.imageWidth; i++)
            {
                if (this.pixelMap[(y * this.imageWidth) + i] != -1)
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
        private bool VLineClear(int x)
        {
            for (int i = 0; i < this.imageHeight; i++)
            {
                if (this.pixelMap[(i * this.imageWidth) + x] != -1)
                {
                    return false;
                }
            }
            return true;
        }
    }
}
