/// Introduction to Neural Networks with C#, 2nd Edition
/// Copyright 2008 by Heaton Research, Inc. 
/// http://www.heatonresearch.com/book/programming-neural-networks-cs-2.html
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

namespace Chapter13Bot
{
    /// <summary>
    /// Receives the results from a URL scan.
    /// </summary>
    public interface ScanReportable
    {
        void ReceiveBadSentence(String sentence);

        void ReceiveGoodSentence(String sentence);
    }
}
