/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.common.web.annotation;	
	
import java.lang.annotation.Annotation;	
import java.lang.annotation.ElementType;	
import java.lang.annotation.Retention;	
import java.lang.annotation.RetentionPolicy;	
import java.lang.annotation.Target;	
	
@Target(value={ElementType.METHOD})	
@Retention(value=RetentionPolicy.RUNTIME)	
public @interface JsonField {	
    public Class<?> type();	
	
    public String[] include() default {""};	
	
    public String[] filter() default {""};	
}	
	
