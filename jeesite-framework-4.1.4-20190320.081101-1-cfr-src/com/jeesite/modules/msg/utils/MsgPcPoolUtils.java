/*	
 * Decompiled with CFR 0.140.	
 * 	
 * Could not load the following classes:	
 *  com.jeesite.common.collect.ListUtils	
 */	
package com.jeesite.modules.msg.utils;	
	
import com.jeesite.common.cache.CacheUtils;	
import com.jeesite.common.collect.ListUtils;	
import com.jeesite.modules.msg.entity.MsgPush;	
import java.util.ArrayList;	
import java.util.List;	
	
public class MsgPcPoolUtils {	
    private static final String MSG_PC_POOL_CACHE = "msgPcPoolCache";	
	
    public static void removeCache(String key) {	
        CacheUtils.remove(MSG_PC_POOL_CACHE, key);	
    }	
	
    public static void putPool(String userCode, MsgPush msgPush) {	
        ArrayList a = MsgPcPoolUtils.getPool(userCode);	
        if (a == null) {	
            a = ListUtils.newArrayList();	
        }	
        MsgPcPoolUtils.putCache(userCode, a);	
        a.add(msgPush);	
    }	
	
    public static <V> V getCache(String key, V defaultValue) {	
        V a = MsgPcPoolUtils.getCache(key);	
        if (a != null) {	
            return a;	
        }	
        return defaultValue;	
    }	
	
    public static <V> V getCache(String key) {	
        return CacheUtils.get(MSG_PC_POOL_CACHE, key);	
    }	
	
    public static void putPool(String userCode, List<MsgPush> msgPush) {	
        MsgPcPoolUtils.putCache(userCode, msgPush);	
    }	
	
    public static <V> void putCache(String key, V value) {	
        CacheUtils.put(MSG_PC_POOL_CACHE, key, value);	
    }	
	
    public static List<MsgPush> getPool(String userCode) {	
        List a = (List)MsgPcPoolUtils.getCache(userCode);	
        if (a == null) {	
            a = ListUtils.newArrayList();	
        }	
        return a;	
    }	
}	
	
