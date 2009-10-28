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

default
{
    state_entry()
    {
        llSetTextureAnim(ANIM_ON | ROTATE | LOOP | SMOOTH, ALL_SIDES, 0, 0, 0, 100, .05);
        llVolumeDetect(TRUE);
    }
    
    collision_start( integer num_detected )
    {
        llTriggerSound("splash", 1); 
    }

}
