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

float rad = 0.0;
float radinc = 0.05;
float time_inc = .2;
float rotspeed = 3.2;

default
{    

    state_entry()
    {
        llSetTextureAnim(0, ALL_SIDES, 0, 0, 0, 0, 0);
    }
    
    link_message(integer sender_num, integer num, string str, key id)
    {
        if(str=="stop")
        {
            llSetTextureAnim(0, ALL_SIDES, 0, 0, 0, 0, 0);
        }
        if(str=="start")
        {
            llSetTextureAnim(ANIM_ON | ROTATE | LOOP | SMOOTH, ALL_SIDES, 0, 0, 0, 100, 20);
        }
    }
    
}