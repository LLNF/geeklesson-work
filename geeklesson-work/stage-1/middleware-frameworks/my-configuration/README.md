# geekbang-stage1-work week2 BY LLNF

## 作业

- 在 my-configuration 基础上，实现 ServletRequest 请求参

  数的 **ConfigSource（MicroProfile Config）**，提供参考：

  Apache Commons Configuration 中的

  **org.apache.commons.configuration.web.ServletRequestConfiguration**。

## 部署

- 无法启动，只分析代码

## 作业详情 

### org.geektimes.configuration.microprofile.config.source.servlet.ServletRequestConfigSource——实现核心代码（个人）

  [ServletRequestConfigSource.java](https://github.com/LLNF/geeklesson-work/blob/master/geeklesson-work/stage-1/middleware-frameworks/my-configuration/src/main/java/org/geektimes/configuration/microprofile/config/source/servlet/ServletRequestConfigSource.java)

  ```java
public class ServletRequestConfigSource extends MapBasedConfigSource {

    private ServletRequest request;

    //检查配置属性解析的时候是否需要分隔符解析，默认false
    private boolean delimiterParsingDisabled;

    //默认配置分隔符
    private char listDelimiter;

    public ServletRequestConfigSource(ServletRequest request,boolean delimiterParsingDisabled,char listDelimiter) {
        super(format("ServletRequest[name:%s] init serverName", request.getServerName()), 500);
        this.request = request;
        this.delimiterParsingDisabled = Objects.isNull(delimiterParsingDisabled) ? false : delimiterParsingDisabled;
        this.listDelimiter = Objects.isNull(listDelimiter) ? ',' : listDelimiter;
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
                //handleDelimiters()进行具体的分隔符操作并返回一个或多个对象
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


  ```

### 步骤一：初始化参数

初始化参数：配置的属性值是否需要分隔符解析以及分隔符配置，未配置则赋默认值

  ```java
    public ServletRequestConfigSource(ServletRequest request,boolean delimiterParsingDisabled,char listDelimiter) {
        super(format("ServletRequest[name:%s] init serverName", request.getServerName()), 500);
        this.request = request;
        this.delimiterParsingDisabled = Objects.isNull(delimiterParsingDisabled) ? false : delimiterParsingDisabled;
        this.listDelimiter = Objects.isNull(listDelimiter) ? ',' : listDelimiter;
    }
  ```

### 步骤二 ：初始化request配置数据

重写抽象方法，以Map<String,String>方式装配好数据org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource#prepareConfigData

```java
    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName,request.getParameter(parameterName));
        }
    }
```

### 步骤三 ：实现 getProperty() 和 getKeys() 方法

```java
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
                //handleDelimiters()进行具体的分隔符操作并返回一个或多个对象
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

```

## 作业问题记录

​		在子类中调用父类的成员变量的时候，如果用Map，我是可以获取到我put的值的，但是改成String，int等类型的时候，就无法获取 

**原因：String int是不可变的**

