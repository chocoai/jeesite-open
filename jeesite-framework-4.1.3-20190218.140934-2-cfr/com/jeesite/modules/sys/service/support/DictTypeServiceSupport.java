/*	
 * Decompiled with CFR 0.139.	
 * 	
 * Could not load the following classes:	
 *  com.jeesite.common.lang.StringUtils	
 *  org.springframework.beans.factory.annotation.Autowired	
 *  org.springframework.transaction.annotation.Transactional	
 */	
package com.jeesite.modules.sys.service.support;	
	
import com.jeesite.common.dao.QueryDao;	
import com.jeesite.common.entity.DataEntity;	
import com.jeesite.common.entity.Page;	
import com.jeesite.common.lang.StringUtils;	
import com.jeesite.common.service.CrudService;	
import com.jeesite.modules.sys.dao.DictTypeDao;	
import com.jeesite.modules.sys.entity.DictType;	
import com.jeesite.modules.sys.service.DictDataService;	
import com.jeesite.modules.sys.service.DictTypeService;	
import com.jeesite.modules.sys.utils.DictUtils;	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.transaction.annotation.Transactional;	
	
@Transactional(readOnly=true)	
public class DictTypeServiceSupport	
extends CrudService<DictTypeDao, DictType>	
implements DictTypeService {	
    @Autowired	
    private DictDataService dictDataService;	
	
    @Transactional(readOnly=false)	
    @Override	
    public void delete(DictType dictType) {	
        DictTypeServiceSupport dictTypeServiceSupport = this;	
        super.delete(dictType);	
        dictTypeServiceSupport.dictDataService.deleteByDictType(dictType.getDictType());	
        DictUtils.clearDictCache();	
    }	
	
    @Transactional(readOnly=false)	
    @Override	
    public void save(DictType dictType, DictType old) {	
        DictType dictType2 = dictType;	
        super.save(dictType2);	
        if (!StringUtils.equals((CharSequence)dictType2.getDictType(), (CharSequence)old.getDictType())) {	
            this.dictDataService.updateDictTypeByDictType(dictType.getDictType(), old.getDictType());	
        }	
        DictUtils.clearDictCache();	
    }	
	
    @Transactional(readOnly=false)	
    @Override	
    public void updateStatus(DictType dictType) {	
        super.updateStatus(dictType);	
        DictUtils.clearDictCache();	
    }	
	
    @Override	
    public DictType get(DictType dictType) {	
        if (dictType == null) {	
            return null;	
        }	
        if (StringUtils.isNotBlank((CharSequence)dictType.getDictType())) {	
            DictType a2 = new DictType();	
            a2.setDictType(dictType.getDictType());	
            return ((DictTypeDao)this.dao).getByEntity(a2);	
        }	
        return super.get(dictType);	
    }	
	
    @Override	
    public Page<DictType> findPage(DictType dictType) {	
        return super.findPage(dictType);	
    }	
}	
	
