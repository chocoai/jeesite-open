/*	
 * Decompiled with CFR 0.139.	
 * 	
 * Could not load the following classes:	
 *  com.jeesite.common.lang.ObjectUtils	
 *  org.slf4j.Logger	
 *  org.springframework.beans.factory.annotation.Autowired	
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty	
 *  org.springframework.stereotype.Service	
 */	
package com.jeesite.modules.msg.task.impl;	
	
import com.jeesite.common.beetl.e.F;	
import com.jeesite.common.config.Global;	
import com.jeesite.common.entity.Page;	
import com.jeesite.common.lang.ObjectUtils;	
import com.jeesite.common.mybatis.mapper.provider.UpdateSqlProvider;	
import com.jeesite.common.service.BaseService;	
import com.jeesite.modules.msg.entity.MsgPush;	
import com.jeesite.modules.msg.entity.content.BaseMsgContent;	
import com.jeesite.modules.msg.service.MsgPushService;	
import com.jeesite.modules.msg.task.MsgPushTask;	
import com.jeesite.modules.msg.task.impl.E;	
import com.jeesite.modules.msg.utils.MsgPushUtils;	
import java.util.Date;	
import java.util.Iterator;	
import java.util.List;	
import org.hyperic.sigar.ProcTime;	
import org.slf4j.Logger;	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;	
import org.springframework.stereotype.Service;	
	
@Service	
@ConditionalOnProperty(name={"msg.enabled"}, havingValue="true", matchIfMissing=true)	
public class MsgLocalMergePushTask	
extends BaseService	
implements MsgPushTask {	
    @Autowired	
    private MsgPushService msgPushService;	
	
    public void setMsgPushService(MsgPushService msgPushService) {	
        this.msgPushService = msgPushService;	
    }	
	
    @Override	
    public boolean executeMsgPush(MsgPush msgPush) {	
        void msgPush2;	
        void v0 = msgPush2;	
        void v1 = msgPush2;	
        void v2 = msgPush2;	
        v1.setPushDate(new Date());	
        v1.setPushStatus("1");	
        v0.addPushReturnContent("合并推送成功！");	
        v0.setReadDate(new Date());	
        v0.setReadStatus("1");	
        this.msgPushService.updateMsgPush((MsgPush)msgPush2);	
        return true;	
    }	
	
    @Override	
    public void execute() {	
        if (!ObjectUtils.toBoolean(F.ALLATORIxDEMO().get("fnMsg")).booleanValue() && !Global.isTestProfileActive()) {	
            return;	
        }	
        MsgPush a2 = null;	
        Iterator<MsgPush> iterator = this.msgPushService.findListByMergePush(new MsgPush()).iterator();	
        block0 : do {	
            Iterator<MsgPush> iterator2 = iterator;	
            while (iterator2.hasNext()) {	
                void a3;	
                MsgPush a4 = iterator.next();	
                if (ObjectUtils.toLong((Object)a4.getMergePushCount()) <= 0L) {	
                    iterator2 = iterator;	
                    continue;	
                }	
                E e2 = new E(this, a4);	
                void v1 = a3;	
                v1.setTitle("合并消息提醒");	
                void v2 = a3;	
                v1.setContent("您有 " + a4.getMergePushCount() + " 条新消息，请注意查收。");	
                MsgPushUtils.push((BaseMsgContent)v1, null, null, a4.getReceiveUserCode());	
                do {	
                    void a5;	
                    MsgPush msgPush = new MsgPush();	
                    void v3 = a5;	
                    void v4 = a5;	
                    v4.setMsgType(a4.getMsgType());	
                    v4.setReceiveUserCode(a4.getReceiveUserCode());	
                    v3.setIsMergePush("1");	
                    v3.setPushStatus("0");	
                    v3.setPage(new Page(1, 30, -1L));	
                    List<MsgPush> a6 = this.msgPushService.findPage((MsgPush)a5).getList();	
                    if (a6.size() == 0) continue block0;	
                    int a7 = 0;	
                    int n = a6.size() - 1;	
                    while (n >= 0) {	
                        int a8;	
                        a2 = a6.get(a8);	
                        if (!this.executeMsgPush(a2)) {	
                            ++a7;	
                        }	
                        n = --a8;	
                    }	
                    if (a6.size() == a7) {	
                        this.logger.warn("没有一条消息能够发送成功，请检查你的消息服务配置。");	
                        continue block0;	
                    }	
                    if (a7 <= 0) continue;	
                    this.logger.warn(new StringBuilder().insert(0, "你有“").append(a7).append("”条消息没有发送成功！").toString());	
                } while (true);	
            }	
            break;	
        } while (true);	
    }	
}	
	
