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

integer index;

// for loading notecard
string notecardName;
key notecardQuery;
integer notecardIndex;
list notecardList;

newSlide()
{
    string texture = llList2String(notecardList,index);
    llSetTexture(texture,1);
    index++;
    if(index>=llGetListLength(notecardList) )
        index = 0;
}

default
{
    state_entry()
    {
        if( llGetListLength(notecardList)==0 )
        {
            notecardName = "SlideControl";
            state loading;
        }
        else
        {
            llSetTimerEvent(30);
            index = 0;
            newSlide();
        }
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

state loading
{
    state_entry()
    {
        llSay(0,"Slideshow loading data...");
        notecardIndex = 0;
        notecardQuery = llGetNotecardLine(notecardName,notecardIndex++);    
    }
    
    dataserver(key query_id, string data) 
    {
        if ( notecardQuery == query_id) 
        {
            // this is a line of our notecard
            if (data == EOF) 
            {    
                llSay(0,"Slideshow loaded...");
                state default;

            } else 
            {
                notecardList += [data];
                notecardQuery = llGetNotecardLine(notecardName,notecardIndex++); 
            }
        }
    }
}
