using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter13Bot
{
    public interface ScanReportable
    {
        void receiveBadSentence(String sentence);

        void receiveGoodSentence(String sentence);
    }
}
