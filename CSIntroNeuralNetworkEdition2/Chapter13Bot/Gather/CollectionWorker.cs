using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

using System.Threading;

namespace Chapter13Bot.Gather
{
    public class CollectionWorker
    {
        private GatherForTrain bot;

        private ManualResetEvent eventHandler;

        /*
         * The search object to use.
         */
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

        public void call()
        {
            try
            {
                scanPerson(this.name, this.year);
                this.bot.reportDone(this.name + ", done scanning.");
                if (this.eventHandler != null)
                {
                    this.eventHandler.Set();
                }
            }
            catch (Exception e)
            {
                this.bot.reportDone(this.name + ", error encountered.");
                Console.WriteLine(e);
                Console.WriteLine(e.StackTrace);
                throw e;
            }
        }

        public void setEvent(ManualResetEvent eventHandler)
        {
            this.eventHandler = eventHandler;
        }

        private void scanPerson(String name, int year)
        {
            ICollection<Uri> c = this.search.search(name);
            int i = 0;

            foreach (Uri u in c)
            {
                try
                {
                    i++;
                    Text.checkURL(this.bot, u, year);
                }
                catch (IOException)
                {

                }
            }
        }
    }
}
