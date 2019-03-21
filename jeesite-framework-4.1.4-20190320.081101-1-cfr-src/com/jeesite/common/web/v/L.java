/*	
 * Decompiled with CFR 0.140.	
 */	
package com.jeesite.common.web.v;	
	
import com.fasterxml.jackson.databind.ObjectMapper;	
import com.jeesite.common.lang.StringUtils;	
import com.jeesite.common.mapper.JsonMapper;	
import com.jeesite.common.web.http.ServletUtils;	
import com.jeesite.modules.job.d.i;	
import java.io.IOException;	
import java.lang.reflect.Type;	
import java.nio.charset.Charset;	
import java.util.Arrays;	
import java.util.List;	
import org.hyperic.sigar.test.GetPass;	
import org.springframework.http.HttpOutputMessage;	
import org.springframework.http.MediaType;	
import org.springframework.http.converter.HttpMessageNotWritableException;	
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;	
import org.springframework.http.converter.json.MappingJacksonValue;	
	
public class l	
extends MappingJackson2HttpMessageConverter {	
    @Override	
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {	
        String a = ServletUtils.getRequest().getParameter("__callback");	
        if (StringUtils.isNotBlank(a)) {	
            void a2;	
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(object);	
            void v0 = a2;	
            v0.setJsonpFunction(a);	
            super.writeInternal(v0, type, outputMessage);	
            return;	
        }	
        super.writeInternal(object, type, outputMessage);	
    }	
	
    public l() {	
        l l2 = this;	
        l2.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", DEFAULT_CHARSET), new MediaType("application", "*+json", DEFAULT_CHARSET)));	
        this.setObjectMapper(JsonMapper.getInstance());	
    }	
}	
	
