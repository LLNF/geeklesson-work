package com.salesmanager.shop.tags;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Objects;

/**
 * Common Response Headers Tag extension
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class CommonResponseHeadersTagTwo extends SimpleTagSupport {



    private String cacheControl = null;
    private String pragma = null;
    private Long expires = null;


    @Override
    public void doTag() throws JspException, IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        if(StringUtils.isNotBlank(cacheControl)){
            response.setHeader("Cache-Control", cacheControl);
        }else{
            response.setHeader("Cache-Control", "");
        }
        if(StringUtils.isNotBlank(pragma)){
            response.setHeader("pragma", pragma);
        }else{
            response.setHeader("pragma", "");
        }
        if(!Objects.isNull(expires)){
            response.setHeader("Expires", String.valueOf(expires));
        }else{
            response.setHeader("Expires", "");
        }
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getPragma() {
        return pragma;
    }

    public void setPragma(String pragma) {
        this.pragma = pragma;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }
}