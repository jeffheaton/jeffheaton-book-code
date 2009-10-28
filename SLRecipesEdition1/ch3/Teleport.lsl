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

vector target=<190, 197, 64>;

vector offset;

default
{    
    moving_end()
    {
        offset = (target- llGetPos()) * (ZERO_ROTATION / llGetRot());
        llSitTarget(offset, ZERO_ROTATION); 
    }

    state_entry()
    {
        llSetText("Teleport pad",<0,0,0>,1.0);
        offset = (target- llGetPos()) * (ZERO_ROTATION / llGetRot());
        llSetSitText("Teleport");
        llSitTarget(offset, ZERO_ROTATION);      
    }

    changed(integer change) 
    { 
        if (change & CHANGED_LINK) 
        { 
            llSleep(0.5); 
            if (llAvatarOnSitTarget() != NULL_KEY) 
            { 
                llUnSit(llAvatarOnSitTarget()); 
            }
        }
    }
    
    touch_start(integer i)
    {
        llSay(0, "Please right-click and select Teleport");
    }
}