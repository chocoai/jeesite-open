/*	
 * Decompiled with CFR 0.140.	
 * 	
 * Could not load the following classes:	
 *  org.apache.shiro.realm.AuthorizingRealm	
 */	
package com.jeesite.common.shiro.x;	
	
import com.jeesite.common.shiro.session.SessionDAO;	
import org.apache.shiro.realm.AuthorizingRealm;	
	
public abstract class i	
extends AuthorizingRealm {	
    protected SessionDAO sessionDAO;	
	
    public void setSessionDAO(SessionDAO sessionDAO) {	
        this.sessionDAO = sessionDAO;	
    }	
}	
	
