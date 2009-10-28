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

list slides = ["slide1","slide2","slide3","slide4"];
integer index;

newSlide()
{
    string texture = llList2String(slides,index);
    llSetTexture(texture,1);
    index++;
    if(index>=llGetListLength(slides) )
        index = 0;
}

default
{
    state_entry()
    {
        llSetTimerEvent(30);
        index = 0;
        newSlide();
    }
    
    touch_start(integer num)
    {
        index = 0;
        newSlide();
        llSay(0,"Starting slide show over");
    }

    timer()
    {
        newSlide();
    }
}
