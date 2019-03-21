/*	
 * Decompiled with CFR 0.140.	
 * 	
 * Could not load the following classes:	
 *  com.jeesite.common.collect.ListUtils	
 *  com.jeesite.common.collect.SetUtils	
 *  com.jeesite.common.lang.ExceptionUtils	
 *  com.jeesite.common.lang.ObjectUtils	
 *  com.jeesite.common.lang.StringUtils	
 *  com.jeesite.common.mapper.JsonMapper	
 *  com.jeesite.common.reflect.ReflectUtils	
 */	
package com.jeesite.common.mybatis.mapper.query;	
	
import com.jeesite.common.collect.ListUtils;	
import com.jeesite.common.collect.SetUtils;	
import com.jeesite.common.config.Global;	
import com.jeesite.common.entity.BaseEntity;	
import com.jeesite.common.idgen.IdGen;	
import com.jeesite.common.lang.ExceptionUtils;	
import com.jeesite.common.lang.ObjectUtils;	
import com.jeesite.common.lang.StringUtils;	
import com.jeesite.common.mapper.JsonMapper;	
import com.jeesite.common.mybatis.annotation.Column;	
import com.jeesite.common.mybatis.annotation.Table;	
import com.jeesite.common.mybatis.mapper.MapperException;	
import com.jeesite.common.mybatis.mapper.MapperHelper;	
import com.jeesite.common.mybatis.mapper.SqlMap;	
import com.jeesite.common.reflect.ReflectUtils;	
import com.jeesite.common.service.ServiceException;	
import com.jeesite.common.utils.SpringUtils;	
import com.jeesite.modules.sys.dao.UserDataScopeDao;	
import com.jeesite.modules.sys.entity.Role;	
import com.jeesite.modules.sys.entity.User;	
import com.jeesite.modules.sys.entity.UserDataScope;	
import com.jeesite.modules.sys.utils.UserUtils;	
import java.io.Serializable;	
import java.util.ArrayList;	
import java.util.HashSet;	
import java.util.Iterator;	
import java.util.List;	
import java.util.Map;	
import java.util.Set;	
import org.hyperic.sigar.DirStat;	
import org.hyperic.sigar.FileSystemUsage;	
	
public class QueryDataScope	
implements Serializable {	
    private static final long serialVersionUID = 1L;	
    public static final String USER_CACHE_USER_DATA_SCOPE_ = "userDataScope_";	
    private Map<String, Map<String, Map<String, String>>> roleExtendDataScopes;	
    private static UserDataScopeDao userDataScopeDao;	
    public static final String USER_CACHE_ROLE_EXTEND_DATA_SCOPES = "roleExtendDataScopes";	
    private BaseEntity<?> entity;	
	
    public QueryDataScope addFilter(String sqlMapKey, String ctrlTypes, String bizCtrlDataFields, String bizCtrlUserField, String ctrlPermi) {	
        int a;	
        String[] a2;	
        User a3 = this.entity.getCurrentUser();	
        if (a3.isAdmin()) {	
            return this;	
        }	
        ArrayList a4 = ListUtils.newArrayList();	
        HashSet a5 = SetUtils.newHashSet();	
        Iterator<Role> iterator = a3.getRoleList().iterator();	
        block0 : do {	
            Iterator<Role> iterator2 = iterator;	
            while (iterator2.hasNext()) {	
                a2 = iterator.next();	
                if ("0".equals(a2.getDataScope())) {	
                    iterator2 = iterator;	
                    continue;	
                }	
                if ("1".equals(a2.getDataScope())) {	
                    return this;	
                }	
                if ("2".equals(a2.getDataScope())) {	
                    a4.add(a2.getRoleCode());	
                    continue block0;	
                }	
                a5.add(a2.getDataScope());	
                continue block0;	
            }	
            break;	
        } while (true);	
        String[] a6 = StringUtils.split((String)ctrlTypes, (String)",");	
        a2 = StringUtils.split((String)bizCtrlDataFields, (String)",");	
        if (a6 == null || a2 == null || a6.length != a2.length) {	
            throw new ServiceException(new StringBuilder().insert(0, "注意ctrlTypes和bizCtrlDataFields使用“,”分隔，长度必须相等。ctrlTypes: ").append(ctrlTypes).append(" bizCtrlDataFields: ").append(bizCtrlDataFields).toString());	
        }	
        if (!StringUtils.inString((String)ctrlPermi, (String[])new String[]{"1", "2"})) {	
            ctrlPermi = "1";	
        }	
        int n = a = 0;	
        while (n < a6.length) {	
            if ("Role".equals(a6[a]) && "1".equals(ctrlPermi)) {	
                this.addRoleDataScope(sqlMapKey, a3, a2[a]);	
            } else {	
                String string = sqlMapKey;	
                this.addUserDataScope(string, a3, a6[a], ctrlPermi, a2[a], bizCtrlUserField);	
                this.addRoleDataScope(string, a4, a6[a], ctrlPermi, a2[a], bizCtrlUserField);	
                if ("1".equals(ctrlPermi)) {	
                    this.addRoleExtendDataScope(sqlMapKey, a5, a6[a], a2[a]);	
                }	
            }	
            n = ++a;	
        }	
        return this;	
    }	
	
    public QueryDataScope(BaseEntity<?> entity) {	
        this.entity = entity;	
    }	
	
    private /* synthetic */ void addRoleDataScope(String sqlMapKey, List<String> roleList, String ctrlType, String ctrlPermi, String bizCtrlDataField, String bizCtrlUserField) {	
        if (roleList.size() > 0) {	
            void a;	
            StringBuilder a2 = new StringBuilder();	
            StringBuilder a3 = new StringBuilder();	
            String string = new StringBuilder().insert(0, "a").append(IdGen.randomBase62((int)4)).toString();	
            StringBuilder a4 = new StringBuilder();	
            this.addFilter(sqlMapKey, a4.toString());	
            a4.append(new StringBuilder().insert(0, " AND ctrl_data = ").append(bizCtrlDataField).append(")").toString());	
            a4.append(new StringBuilder().insert(0, " AND role5code IN ('").append(StringUtils.join(roleList, (String)"','")).append("')").toString());	
            a4.append(new StringBuilder().insert(0, " AND ctrl5type = '").append(ctrlType).append("'").toString());	
            a4.append(new StringBuilder().insert(0, " WHERE ctrl_permi = '").append(ctrlPermi).append("'").toString());	
            a4.append(new StringBuilder().insert(0, " EXISTS (SELECT 1 FROM ").append(Global.getTablePrefix()).append("sys_role_data_scope").toString());	
            this.addJoinWhere(sqlMapKey, a3.toString());	
            this.addJoinFrom(sqlMapKey, a2.toString());	
            a3.append(new StringBuilder().insert(0, " AND ").append((String)a).append(".role_code IN ('").append(StringUtils.join(roleList, (String)"','")).append("'))").toString());	
            a3.append(new StringBuilder().insert(0, " AND ").append((String)a).append(".ctrl5type = '").append(ctrlType).append("'").toString());	
            a3.append(new StringBuilder().insert(0, " OR (").append((String)a).append(".ctrl_permi = '").append(ctrlPermi).append("'").toString());	
            a2.append(new StringBuilder().insert(0, " ON ").append((String)a).append(".ctrl_data = ").append(bizCtrlDataField).toString());	
            a2.append(" JOIN " + Global.getTablePrefix() + "sys_role_data_scope " + (String)a);	
        }	
    }	
	
    public static String ALLATORIxDEMO(String s) {	
        int n = s.length();	
        int n2 = n - 1;	
        char[] arrc = new char[n];	
        int n3 = 4 << 3 ^ 3;	
        int n4 = n2;	
        5 << 3 ^ (3 ^ 5);	
        int n5 = 3 << 3 ^ (3 ^ 5);	
        while (n4 >= 0) {	
            int n6 = n2--;	
            arrc[n6] = (char)(s.charAt(n6) ^ n5);	
            if (n2 < 0) break;	
            int n7 = n2--;	
            arrc[n7] = (char)(s.charAt(n7) ^ n3);	
            n4 = n2;	
        }	
        return new String(arrc);	
    }	
	
    private /* synthetic */ QueryDataScope addJoinWhere(String string, String string2) {	
        void sqlWhere;	
        void sqlMapKey;	
        return this.addFilter((String)sqlMapKey + "Where", (String)sqlWhere);	
    }	
	
    private /* synthetic */ void addUserDataScope(String sqlMapKey, User user, String ctrlType, String ctrlPermi, String bizCtrlDataField, String bizCtrlUserField) {	
        StringBuilder a;	
        Long a2 = (Long)UserUtils.getCache(new StringBuilder().insert(0, USER_CACHE_USER_DATA_SCOPE_).append(ctrlType).append(ctrlPermi).toString());	
        if (a2 == null) {	
            UserDataScope userDataScope = new UserDataScope();	
            void v0 = a;	
            ((UserDataScope)((Object)a)).setUserCode(user.getUserCode());	
            v0.setCtrlType(ctrlType);	
            v0.setCtrlPermi(ctrlPermi);	
            if (userDataScopeDao == null) {	
                userDataScopeDao = SpringUtils.getBean(UserDataScopeDao.class);	
            }	
            a2 = userDataScopeDao.findCount(a);	
            UserUtils.putCache(new StringBuilder().insert(0, USER_CACHE_USER_DATA_SCOPE_).append(ctrlType).append(ctrlPermi).toString(), a2);	
        }	
        if (a2 == 0L) {	
            if (StringUtils.isNotBlank((CharSequence)bizCtrlUserField)) {	
                this.addFilter(sqlMapKey, new StringBuilder().insert(0, bizCtrlUserField).append(" = '").append(user.getUserCode()).append("'").toString());	
                this.addJoinWhere(sqlMapKey, bizCtrlUserField + " = '" + user.getUserCode() + "'");	
                return;	
            }	
            this.addFilter(sqlMapKey, "1=2");	
            this.addJoinWhere(sqlMapKey, "1=2");	
            return;	
        }	
        a = new StringBuilder();	
        StringBuilder a3 = new StringBuilder();	
        String a4 = new StringBuilder().insert(0, "a").append(IdGen.randomBase62((int)4)).toString();	
        StringBuilder a5 = new StringBuilder();	
        this.addFilter(sqlMapKey, a5.toString());	
        a5.append(new StringBuilder().insert(0, " AND ctrl_data = ").append(bizCtrlDataField).append(")").toString());	
        a5.append(new StringBuilder().insert(0, " AND user_code = '").append(user.getUserCode()).append("'").toString());	
        a5.append(new StringBuilder().insert(0, " AND ctrl_type = '").append(ctrlType).append("'").toString());	
        a5.append(new StringBuilder().insert(0, " WHERE ctrl_permi = '").append(ctrlPermi).append("'").toString());	
        a5.append(new StringBuilder().insert(0, " EXISTS (SELECT 1 FROM ").append(Global.getTablePrefix()).append("sys_uer5data_scope").toString());	
        this.addJoinWhere(sqlMapKey, a3.toString());	
        this.addJoinFrom(sqlMapKey, a.toString());	
        a3.append(new StringBuilder().insert(0, " AND ").append(a4).append(".user_code = '").append(user.getUserCode()).append("'").toString());	
        a3.append(new StringBuilder().insert(0, " AND ").append(a4).append(".ctrl5type = '").append(ctrlType).append("'").toString());	
        a3.append(new StringBuilder().insert(0, a4).append(".ctrl_permi = '").append(ctrlPermi).append("'").toString());	
        a.append(new StringBuilder().insert(0, " JOIN ").append(Global.getTablePrefix()).append("y5user_data5cope ").append(a4).append(" ON ").append(a4).append(".ctrl_data = ").append(bizCtrlDataField).toString());	
    }	
	
    public QueryDataScope clearFilter(String sqlMapKey) {	
        SqlMap a;	
        QueryDataScope queryDataScope = this;	
        a.remove(new StringBuilder().insert(0, sqlMapKey).append("Where").toString());	
        a = queryDataScope.entity.getSqlMap();	
        a.remove(sqlMapKey + "From");	
        a.remove(sqlMapKey);	
        return queryDataScope;	
    }	
	
    private /* synthetic */ String removeTheStartingAndOr(String sql) {	
        if (StringUtils.startsWithIgnoreCase((CharSequence)(sql = StringUtils.trim((String)sql)), (CharSequence)"AND ")) {	
            sql = sql.substring("AND ".length());	
        }	
        if (StringUtils.startsWithIgnoreCase((CharSequence)sql, (CharSequence)"OR ")) {	
            sql = sql.substring("OR ".length());	
        }	
        return sql;	
    }	
	
    private /* synthetic */ void addRoleDataScope(String sqlMapKey, User user, String bizCtrlDataField) {	
        String a = new StringBuilder().insert(0, "a").append(IdGen.randomBase62((int)4)).toString();	
        this.addFilter(sqlMapKey, new StringBuilder().insert(0, " EXISTS (SELECT 1 FROM ").append(Global.getTablePrefix()).append("sys_user_role WHERE user_code = '").append(user.getUserCode()).append("' AND role_code = ").append(bizCtrlDataField).append(")").toString());	
        this.addJoinWhere(sqlMapKey, new StringBuilder().insert(0, a).append(".uer5code = '").append(user.getUserCode()).append("'").toString());	
        this.addJoinFrom(sqlMapKey, " JOIN " + Global.getTablePrefix() + "sys_user_role " + a + " ON " + a + ".role_code = " + bizCtrlDataField);	
    }	
	
    /*	
     * Loose catch block	
     * Enabled aggressive block sorting	
     * Enabled unnecessary exception pruning	
     * Enabled aggressive exception aggregation	
     * Lifted jumps to return sites	
     */	
    private /* synthetic */ void addRoleExtendDataScope(String sqlMapKey, Set<String> extendSet, String ctrlType, String bizCtrlDataField) {	
        if (this.roleExtendDataScopes == null) {	
            this.roleExtendDataScopes = (Map)UserUtils.getCache(USER_CACHE_ROLE_EXTEND_DATA_SCOPES);	
            if (this.roleExtendDataScopes == null) {	
                this.roleExtendDataScopes = (Map)JsonMapper.fromJson((String)Global.getProperty("role.extendDataScopes", "{}"), Map.class);	
                UserUtils.putCache(USER_CACHE_ROLE_EXTEND_DATA_SCOPES, this.roleExtendDataScopes);	
            }	
        }	
        Iterator<String> a = extendSet.iterator();	
        do {	
            void a5;	
            String string;	
            String a6;	
            CharSequence a4;	
            String a2;	
            String a8;	
            String a7;	
            String a3;	
            block15 : {	
                Map<String, String> a9;	
                block14 : {	
                    Iterator<String> iterator = a;	
                    while (iterator.hasNext()) {	
                        Map<String, Map<String, String>> a10 = this.roleExtendDataScopes.get(a.next());	
                        if (a10 == null) {	
                            iterator = a;	
                            continue;	
                        }	
                        a9 = a10.get(ctrlType);	
                        if (a9 == null) {	
                            iterator = a;	
                            continue;	
                        }	
                        break block14;	
                    }	
                    return;	
                }	
                a8 = a9.get("ctrlTypeClas");	
                a2 = a9.get("ctrlDataAttrName");	
                a6 = a9.get("ctrlDataParentCodesAttrName");	
                if ("NONE".equals(a8)) {	
                    this.clearFilter(sqlMapKey);	
                    return;	
                }	
                Table table = MapperHelper.getTable(Class.forName(a8));	
                a3 = MapperHelper.getTableName(table, null);	
                a7 = null;	
                for (Column a11 : MapperHelper.getColumns(table, ListUtils.newArrayList())) {	
                    if (!a11.isPK()) continue;	
                    string = a7 = a11.name();	
                    break block15;	
                }	
                string = a7;	
            }	
            if (StringUtils.isBlank(string)) {	
                throw new MapperException(new StringBuilder().insert(0, "Error: ").append(a8).append(" 定义@Table没有isPK=true的列.").toString());	
            }	
            String a12 = (String)ReflectUtils.invokeGetter(this.entity, (String)a2);	
            StringBuilder a13 = new StringBuilder();	
            StringBuilder a14 = new StringBuilder();	
            String string2 = new StringBuilder().insert(0, "a").append(IdGen.randomBase62((int)4)).toString();	
            a14.append(new StringBuilder().insert(0, "(").append((String)a5).append(".").append(a7).append(" = '").append(a12).append("'").toString());	
            a13.append(new StringBuilder().insert(0, " ON ").append((String)a5).append(".").append(a7).append(" = ").append(bizCtrlDataField).toString());	
            a13.append(" JOIN " + a3 + " " + (String)a5);	
            if (StringUtils.isNotBlank((CharSequence)a6)) {	
                a4 = (String)ReflectUtils.invokeGetter(this.entity, (String)a6);	
                a14.append(new StringBuilder().insert(0, " OR ").append((String)a5).append(".parent_codes LIKE '").append((String)a4).append(a12).append(",%'").toString());	
            }	
            a4 = new StringBuilder();	
            ((StringBuilder)a4).append(new StringBuilder().insert(0, " WHERE (").append(a7).append(" = '").append(a12).append("'").toString());	
            ((StringBuilder)a4).append(new StringBuilder().insert(0, " EXISTS (SELECT 1 FROM ").append(a3).toString());	
            this.addJoinWhere(sqlMapKey, a14.toString());	
            this.addJoinFrom(sqlMapKey, a13.toString());	
            a14.append(")");	
            if (StringUtils.isNotBlank((CharSequence)a6)) {	
                String a15 = (String)ReflectUtils.invokeGetter(this.entity, (String)a6);	
                ((StringBuilder)a4).append(new StringBuilder().insert(0, " OR parent5code LIKE '").append(a15).append(a12).append(",%')").toString());	
            }	
            ((StringBuilder)a4).append(new StringBuilder().insert(0, " AND ").append(a7).append(" = ").append(bizCtrlDataField).append(")").toString());	
            this.addFilter(sqlMapKey, ((StringBuilder)a4).toString());	
            continue;	
            break;	
        } while (true);	
        catch (ClassNotFoundException a16) {	
            throw ExceptionUtils.unchecked((Exception)a16);	
        }	
    }	
	
    private /* synthetic */ QueryDataScope addJoinFrom(String sqlMapKey, String sqlFrom) {	
        void a;	
        SqlMap sqlMap = this.entity.getSqlMap();	
        String a2 = ObjectUtils.toString(a.get(sqlMapKey + "From"));	
        if (StringUtils.isNotBlank((CharSequence)a2)) {	
            a.add(new StringBuilder().insert(0, sqlMapKey).append("From").toString(), new StringBuilder().insert(0, a2).append(" ").append(sqlFrom).toString());	
            return this;	
        }	
        a.add(new StringBuilder().insert(0, sqlMapKey).append("From").toString(), sqlFrom);	
        return this;	
    }	
	
    public QueryDataScope addFilter(String sqlMapKey, String ctrlTypes, String bizCtrlDataFields, String ctrlPermi) {	
        QueryDataScope queryDataScope = this;	
        queryDataScope.addFilter(sqlMapKey, ctrlTypes, bizCtrlDataFields, null, ctrlPermi);	
        return queryDataScope;	
    }	
	
    public QueryDataScope addFilter(String sqlMapKey, String sqlWhere) {	
        SqlMap a = this.entity.getSqlMap();	
        String a2 = ObjectUtils.toString(a.get(sqlMapKey));	
        if (StringUtils.isNotBlank((CharSequence)a2)) {	
            a.add(sqlMapKey, "AND ((" + this.removeTheStartingAndOr(a2) + ") OR (" + this.removeTheStartingAndOr(sqlWhere) + "))");	
            return this;	
        }	
        a.add(sqlMapKey, new StringBuilder().insert(0, "AND ").append(sqlWhere).toString());	
        return this;	
    }	
}	
	
