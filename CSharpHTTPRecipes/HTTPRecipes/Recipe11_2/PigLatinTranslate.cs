using System;
using System.Collections.Generic;
using System.Text;


namespace Recipe11_2
{
    /// <summary>
    /// Recipe #11.2: Using SOAP
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe uses a SOAP web service to translate a sentence
    /// into Pig Latin.  This recipe uses DotNet to access the
    /// SOAP web service.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class PigLatinTranslate
    {
        static void Main(string[] args)
        {
            Recipe11_2.com.httprecipes.www.PigLatinTranslator translator 
                = new Recipe11_2.com.httprecipes.www.PigLatinTranslator();
            Console.WriteLine( translator.translate("Hello World!") );
        }
    }
}
