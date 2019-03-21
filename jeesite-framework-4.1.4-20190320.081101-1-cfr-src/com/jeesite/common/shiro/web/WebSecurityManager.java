/*	
 * Decompiled with CFR 0.140.	
 * 	
 * Could not load the following classes:	
 *  com.jeesite.common.collect.ListUtils	
 *  org.apache.shiro.SecurityUtils	
 *  org.apache.shiro.authc.Authenticator	
 *  org.apache.shiro.authc.pam.AuthenticationStrategy	
 *  org.apache.shiro.authc.pam.ModularRealmAuthenticator	
 *  org.apache.shiro.authz.Authorizer	
 *  org.apache.shiro.authz.ModularRealmAuthorizer	
 *  org.apache.shiro.crypto.CryptoException	
 *  org.apache.shiro.mgt.RememberMeManager	
 *  org.apache.shiro.mgt.SecurityManager	
 *  org.apache.shiro.realm.Realm	
 *  org.apache.shiro.subject.PrincipalCollection	
 *  org.apache.shiro.subject.SubjectContext	
 *  org.apache.shiro.web.mgt.DefaultWebSecurityManager	
 */	
package com.jeesite.common.shiro.web;	
	
import com.jeesite.common.collect.ListUtils;	
import com.jeesite.common.shiro.web.RememberMeManager;	
import com.jeesite.common.shiro.web.i;	
import java.util.ArrayList;	
import java.util.Collection;	
import java.util.Iterator;	
import org.apache.shiro.SecurityUtils;	
import org.apache.shiro.authc.Authenticator;	
import org.apache.shiro.authc.pam.AuthenticationStrategy;	
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;	
import org.apache.shiro.authz.Authorizer;	
import org.apache.shiro.authz.ModularRealmAuthorizer;	
import org.apache.shiro.crypto.CryptoException;	
import org.apache.shiro.mgt.SecurityManager;	
import org.apache.shiro.realm.Realm;	
import org.apache.shiro.subject.PrincipalCollection;	
import org.apache.shiro.subject.SubjectContext;	
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;	
	
public class WebSecurityManager	
extends DefaultWebSecurityManager {	
    public WebSecurityManager(Collection<Realm> realms) {	
        WebSecurityManager webSecurityManager = this;	
        super(realms);	
        webSecurityManager.initialize();	
    }	
	
    protected PrincipalCollection getRememberedIdentity(SubjectContext subjectContext) {	
        try {	
            return super.getRememberedIdentity(subjectContext);	
        }	
        catch (CryptoException cryptoException) {	
            return null;	
        }	
    }	
	
    public void setRealms(Collection<Realm> realms) {	
        WebSecurityManager webSecurityManager = this;	
        super.setRealms(realms);	
        if (webSecurityManager.getAuthorizer() instanceof ModularRealmAuthorizer) {	
            ModularRealmAuthorizer a = (ModularRealmAuthorizer)this.getAuthorizer();	
            ArrayList a2 = ListUtils.newArrayList();	
            a.setRealms((Collection)a2);	
            a2.add(realms.iterator().next());	
        }	
    }	
	
    public WebSecurityManager(Realm singleRealm) {	
        WebSecurityManager webSecurityManager = this;	
        super(singleRealm);	
        webSecurityManager.initialize();	
    }	
	
    private /* synthetic */ void initialize() {	
        WebSecurityManager webSecurityManager = this;	
        webSecurityManager.setRememberMeManager((org.apache.shiro.mgt.RememberMeManager)new RememberMeManager());	
        ((ModularRealmAuthenticator)webSecurityManager.getAuthenticator()).setAuthenticationStrategy((AuthenticationStrategy)new i(this));	
        SecurityUtils.setSecurityManager((SecurityManager)this);	
    }	
	
    public WebSecurityManager() {	
        WebSecurityManager webSecurityManager = this;	
        webSecurityManager.initialize();	
    }	
}	
	
