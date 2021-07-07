# geekbang-stage1-work week1 BY LLNF
## 作业
- 1.实现自定义标签 2.编写自定义标签 tld 文件 3.部署到 Servlet 应用
## 部署
- tomcat8.5 启动
## 作业详情 
 ### tld标签文件
   [shopizer-tags.tld](https://github.com/LLNF/geeklesson-work/edit/master/geeklesson-work/shopizer/sm-shop/src/main/webapp/WEB-INF/shopizer-tags.tld)
   ```tld
     <tag>
        <name>common-response-headers-2</name>
        <tag-class>com.salesmanager.shop.tags.CommonResponseHeadersTagTwo</tag-class>
        <body-content>scriptless</body-content>
        <attribute>
            <name>cacheControl</name>
            <required>false</required>
            <rtexprvalue>false</rtexprvalue>
            <type>java.lang.String</type>
        </attribute>
        <attribute>
            <name>pragma</name>
            <required>false</required>
            <rtexprvalue>false</rtexprvalue>
            <type>java.lang.String</type>
        </attribute>
        <attribute>
            <name>expires</name>
            <required>false</required>
            <rtexprvalue>false</rtexprvalue>
            <type>java.lang.Long</type>
        </attribute>
    </tag>
   ```
### jsp文件标签
  [catalogLayout.jsp](https://github.com/LLNF/geeklesson-work/edit/master/geeklesson-work/shopizer/sm-shop/src/main/webapp/pages/shop/templates/december/catalogLayout.jsp)
  ```jsp
  <%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
  <sm:common-response-headers-2 cacheControl="no-cache" pragma="no-cache" expires="-1"/>
  ```
### java代码
  [CommonResponseHeadersTagTwo](https://github.com/LLNF/geeklesson-work/edit/master/geeklesson-work/shopizer/sm-shop/src/main/java/com/salesmanager/shop/tags/CommonResponseHeadersTagTwo.java)
  ```java
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
  ```
## 访问方式
- http://localhost:8080/
- 访问请求检查请求返回头信息
```
  responseHeader:{
   Cache-Control:no-cache,
   Pragma:no-cache
   Expires:-1
   }
```
