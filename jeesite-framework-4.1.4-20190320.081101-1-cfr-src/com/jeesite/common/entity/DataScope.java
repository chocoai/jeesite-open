/*	
 * Decompiled with CFR 0.140.	
 */	
package com.jeesite.common.entity;	
	
import com.jeesite.common.entity.DataEntity;	
import com.jeesite.common.mybatis.mapper.SqlMap;	
import com.jeesite.common.mybatis.mapper.query.QueryType;	
import com.jeesite.common.mybatis.mapper.query.QueryWhere;	
import com.jeesite.common.shiro.d.D;	
import org.hyperic.sigar.ProcState;	
	
public class DataScope<T extends DataEntity<?>>	
extends DataEntity<T> {	
    public static final String CTRL_PERMI_HAVE = "1";	
    private static final long serialVersionUID = 1L;	
    private String ctrlType;	
    private String ctrlData;	
    public static final String CTRL_PERMI_MANAGE = "2";	
    private String ctrlPermi = "1";	
	
    public void setCtrlType(String ctrlType) {	
        this.ctrlType = ctrlType;	
    }	
	
    public String getCtrlData() {	
        return this.ctrlData;	
    }	
	
    public String getCtrlPermi() {	
        return this.ctrlPermi;	
    }	
	
    public void setCtrlPermi(String ctrlPermi) {	
        this.ctrlPermi = ctrlPermi;	
    }	
	
    public String getCtrlData_in() {	
        return (String)this.sqlMap.getWhere().getValue("ctrl_data", QueryType.IN);	
    }	
	
    public void setCtrlData(String ctrlData) {	
        this.ctrlData = ctrlData;	
    }	
	
    public String getCtrlType() {	
        return this.ctrlType;	
    }	
	
    public void setCtrlData_in(String[] ctrlDatas) {	
        this.sqlMap.getWhere().and("ctrl_data", QueryType.IN, ctrlDatas);	
    }	
}	
	
