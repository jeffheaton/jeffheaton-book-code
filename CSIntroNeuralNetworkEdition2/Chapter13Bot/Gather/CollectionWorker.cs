/// Introduction to Neural Networks with Java, 2nd Edition
/// Copyright 2008 by Heaton Research, Inc. 
/// http://www.heatonresearch.com/books/java-neural-2/
/// 
/// ISBN-10: 1604390093
/// ISBN-13: 978-1604390094
/// 
/// This class is released under the:
/// GNU Lesser General Public License (LGPL)
/// http://www.gnu.org/copyleft/lesser.html
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

using System.Threading;
using System.Net;

namespace Chapter13Bot.Gather
{
    public class CollectionWorker
    {
        private GatherForTrain bot;

        /// <summary>
        /// The search object to use.
        /// </summary>
        private YahooSearch search;

        private String name;
        private int year;

        public CollectionWorker(GatherForTrain bot, String name,
                int year)
        {
            this.bot = bot;
            this.name = name;
            this.year = year;
            this.search = new YahooSearch();
        }

        public void Call()
        {
                ScanPerson(this.name, this.year);
                this.bot.ReportDone(this.name + ", done scanning.");


        }


        private void ScanPerson(String name, int year)
        {
            ICollection<Uri> c = this.search.Search(name);
            int i = 0;

            foreach (Uri u in c)
            {
                try
                {
                    i++;
                    Console.WriteLine(name + "," + "Scanning URL " + i + "/" + c.Count);
                    Text.CheckURL(this.bot, u, year);
                }
                catch (Exception e)
                {
                    // ignore anything that might go wrong on these websites
                }                
            }
        }
    }
}
