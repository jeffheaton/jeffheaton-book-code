// From the book:
//
// Scripting Recipes for Second Life
// by Jeff Heaton (Encog Dod in SL)
// ISBN: 160439000X
// Copyright 2007 by Heaton Research, Inc.
//
// This script may be freely copied and modified so long as this header
// remains unmodified.
//
// For more information about this book visit the following web site:
//
// http://www.heatonresearch.com/articles/series/22/

integer CHANNEL = 42; // dialog channel

default
{
    state_entry()
    {
        llSetText("Touch to Call Elevator",<0,0,0>,1.0);
    }

    touch_start(integer total_number)
    {
                
        llRegionSay(42, "Floor 1");
    }
}
