using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;

using HeatonResearch.Spider.HTML;

namespace Chapter13Bot
{
    public class Text
    {
	/**
	 * Check the specified URL for a birth year. This will occur if one sentence
	 * is found that has the word born, and a numeric value less than 3000.
	 * 
	 * @param url
	 *            The URL to check.
	 * @throws IOException
	 *             Thrown if a communication error occurs.
	 */
	public static void checkURL( ScanReportable report,  Uri url,
			 int desiredYear)  {
		int ch;
		 StringBuilder sentence = new StringBuilder();
		String ignoreUntil = null;

                    WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML html = new ParseHTML(istream);


		do {
			ch = html.Read();
			if ((ch != -1) && (ch != 0) && (ignoreUntil == null)) {
				if (".?!".IndexOf((char)ch) != -1) {
					 String str = sentence.ToString();
					 int year = Text.extractYear(str);

					if (desiredYear == -1) {
						// looking for any year
						if (year != -1) {
							report.receiveGoodSentence(str);
						}
					} else {
						// looking for a specific year
						if (year == desiredYear) {
							report.receiveGoodSentence(str);
						} else if (year != -1) {
							report.receiveBadSentence(str);
						}
					}
					sentence.Length = 0;
				} else if (ch == ' ') {
                    string str = sentence.ToString();
					if ((sentence.Length > 0)
							&& (str[str.Length - 1] != ' ')) {
						sentence.Append(' ');
                        
					}
				} else if ((ch != '\n') && (ch != '\t') && (ch != '\r')) {
					if ((ch) < 128) {
						sentence.Append((char) ch);
					}
				}
			} else if (ch == 0) {
				// clear anything before a body tag
				if (html.Tag.Name.Equals("body",StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("br", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("li", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("p", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("h1", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("h2", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("h3", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("td", StringComparison.CurrentCultureIgnoreCase)
                        || html.Tag.Name.Equals("th", StringComparison.CurrentCultureIgnoreCase))
                {
					sentence.Length = 0;
				}
				// ignore everything between script and style tags
				if (ignoreUntil == null) {
					if (html.Tag.Name.Equals("script",StringComparison.CurrentCultureIgnoreCase)) {
						ignoreUntil = "/script";
					} else if (html.Tag.Name
							.Equals("style",StringComparison.CurrentCultureIgnoreCase)) {
						ignoreUntil = "/style";
					}
				} else {
                    if (html.Tag.Name.Equals(ignoreUntil, StringComparison.CurrentCultureIgnoreCase))
                    {
						ignoreUntil = null;
					}
				}

				// add a space after the tag
				if (sentence.Length > 0) {
                    string str = sentence.ToString();
					if (str[str.Length - 1] != ' ') {
						sentence.Append(' ');
					}
				}
			}
		} while (ch != -1);

	}

	/**
	 * Examine a sentence and see if it contains the word born and a number.
	 * 
	 * @param sentence
	 *            The sentence to search.
	 * @return The number that was found.
	 */
	public static int extractYear( String sentence) {
		int result = -1;

        string[] tok = sentence.Split(' ');
		for(int i=0;i<tok.Length;i++) {
			 String word = tok[i];

			try {
				result = int.Parse(word);

				if ((result < 1600) || (result > 2100)) {
					result = -1;
				}
			} catch ( FormatException ) {
			}
		}

		return result;
	}

    }
}
