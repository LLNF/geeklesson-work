/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.configuration.microprofile.config.source.servlet;

import org.apache.commons.configuration.PropertyConverter;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletRequest;
import java.util.*;

import static java.lang.String.format;

/**
 * {@link ServletRequest} {@link ConfigSource}
 *
 * @author LLNF
 * @since 1.0.0
 */
public class ServletRequestConfigSource extends MapBasedConfigSource {

    private ServletRequest request;

    //检查配置属性解析的时候是否需要分隔符解析，默认false
    private boolean delimiterParsingDisabled = false;

    //默认配置分隔符
    private char listDelimiter = ',';

    public ServletRequestConfigSource(ServletRequest request,boolean delimiterParsingDisabled,char listDelimiter) {
        super(format("ServletRequest[name:%s] init serverName", request.getServerName()), 500);
        this.request = request;
        this.delimiterParsingDisabled = delimiterParsingDisabled;
        this.listDelimiter = listDelimiter;
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName,request.getParameter(parameterName));
        }
    }
    public Object getProperty(String key){
        Map<String,String> values =  super.getProperties();

        //values为空直接返回
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }

        //values为1判断value后直接返回
        else if (values.size() == 1){
            return handleDelimiters(values.get(key));
        }
        else{
            // 确保所有 value 元素都不包含分隔符
            List<Object> result = new ArrayList<Object>(values.size());
            for(Map.Entry entry:values.entrySet()){
                Object val = handleDelimiters(entry.getValue());
                if (val instanceof Collection) {
                    result.addAll((Collection<?>) val);
                }
                else {
                    result.add(val);
                }
            }
            return result;
        }
    }

    public Iterator<String> getKeys()
    {
        // 获取所有的键值对参数
        Map<String,?> values =  getConfigData();
        return values.keySet().iterator();
    }

    protected Object handleDelimiters(Object value)
    {
        if (!isDelimiterParsingDisabled() && value instanceof String){
            List<String> list = PropertyConverter.split((String) value,
                    getListDelimiter());
            value = list.size() > 1 ? list : list.get(0);
        }
        return value;
    }

    private char getListDelimiter() {
        return listDelimiter;
    }

    private boolean isDelimiterParsingDisabled() {
        return delimiterParsingDisabled;
    }

}
