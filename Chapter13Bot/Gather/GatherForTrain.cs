using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.IO;

using HeatonResearchNeural.Util;

namespace Chapter13Bot.Gather
{
    public class GatherForTrain : ScanReportable
    {
        public const bool LOG = true;
        public const int THREAD_POOL_SIZE = 20;

        /**
         * The main method processes the command line arguments and then calls
         * process method to determine the birth year.
         * 
         * @param args
         *            Holds the Yahoo key and the site to search.
         */
        public static void main(String[] args)
        {
            try
            {
                GatherForTrain when = new GatherForTrain();
                when.process();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                Console.WriteLine(e.StackTrace);
            }
        }

        private ICollection<String> trainingDataGood = new List<String>();
        private ICollection<String> trainingDataBad = new List<String>();
        private int currentTask;

        private int totalTasks;

        /**
         * This method is called to determine the birth year for a person. It
         * obtains 100 web pages that Yahoo returns for that person. Each of these
         * pages is then searched for the birth year of that person. Which ever year
         * is selected the largest number of times is selected as the birth year.
         * 
         * @param name
         *            The name of the person you are seeing the birth year for.
         * @throws IOException
         *             Thrown if a communication error occurs.
         */
        public void process()
        {

            
            ReadCSV famous = new ReadCSV("famous.csv");
            report("Building training data from list of famous people.");
            DateTime started = new DateTime();
            WaitCallback w = new WaitCallback(WorkerProc);

            List<ManualResetEvent> events = new List<ManualResetEvent>();
            this.totalTasks = 0;
            while (famous.Next())
            {
                String name = famous.Get("Person");
                int year = famous.GetInt("Year");

                CollectionWorker worker = new CollectionWorker(this, name,
                       year);

                ManualResetEvent e = new ManualResetEvent(false);
                worker.setEvent(e);
                events.Add(e);
                ThreadPool.QueueUserWorkItem(w, worker);
                this.totalTasks++;
            }

            WaitHandle.WaitAll(events.ToArray());

            long length = (DateTime.Now - started).Ticks;
            length /= 1000L;
            length /= 60;
            Console.WriteLine("Took " + length
                    + " minutes to collect training data from the Internet.");
            Console.WriteLine("Writing training file");
            writeTrainingFile();

        }

        public void receiveBadSentence(String sentence)
        {
            this.trainingDataBad.Add(sentence);

        }

        public void receiveGoodSentence(String sentence)
        {
            this.trainingDataGood.Add(sentence);

        }

        public void report(String str)
        {
            Console.WriteLine(str);
        }

        public void reportDone(String str)
        {
            report(this.currentTask + "/" + this.totalTasks + ":" + str);
            this.currentTask++;
        }

        private void writeTrainingFile()
        {
            TextWriter o = new StreamWriter(Config.FILENAME_GOOD_TRAINING_TEXT);

            foreach (String str in this.trainingDataGood)
            {
                o.WriteLine(str.Trim());
            }

            o.Close();

            o = new StreamWriter(Config.FILENAME_BAD_TRAINING_TEXT);

            foreach (String str in this.trainingDataBad)
            {
                o.WriteLine(str.Trim());
            }

            o.Close();
        }

        public void WorkerProc(Object obj)
        {
        }
    }
}
