key avataronsittarget;

default
{
    state_entry()
    {
        llSetTextureAnim(ANIM_ON | ROTATE | LOOP | SMOOTH, ALL_SIDES, 0, 0, 0, 100, 1);
        llSitTarget(<0,0,1>,<0,0,0,1>);
        llSetSitText("Camp");
        llSetTimerEvent(3);
    }

    changed(integer change)
    {
        if(change & CHANGED_LINK) 
        {
            avataronsittarget = llAvatarOnSitTarget();
            if( avataronsittarget != NULL_KEY )
            {
                if ((llGetPermissions() & PERMISSION_TRIGGER_ANIMATION) && llGetPermissionsKey() == avataronsittarget) 
                {
                    llStopAnimation("sit");
                    llStartAnimation("dance1");
                } 
                else 
                {
                    llRequestPermissions(avataronsittarget, PERMISSION_TRIGGER_ANIMATION);
                }
            }
        }
    }
    
    timer()
    {
        if ((llGetPermissions() & PERMISSION_TRIGGER_ANIMATION) && llGetPermissionsKey() == avataronsittarget) 
        {
            llStartAnimation("dance1");
        }
    }

    run_time_permissions(integer perm)
    {
        if(perm)
        {
            llStopAnimation("sit");
            llStartAnimation("stand");
        }
    }


}