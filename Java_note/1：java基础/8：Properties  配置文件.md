### Propertise 类：

**表示了一个持久的属性集,Properties 可保存在流中或从流中加载。属性列表中每个键及其对应值都是一个字符串**
**此类继承了hashTable类，实现map接口**

构造方法：
	Properties() 
         	创建一个无默认值的空属性列表

  方法
	 String getProperty(String key) 
        	用指定的键在此属性列表中搜索属性
	 Object setProperty(String key, String value) 
          	调用 Hashtable 的方法 put

 void load(InputStream inStream) 
      	从输入流中读取属性列表（键和元素对） 
 void store(OutputStream out, String comments) 
            以适合使用 load(InputStream) 方法加载到 Properties 表中的格式，将此 Properties 表中的属性列表
       （键和元素对）写入输出流
 void list(PrintWriter out) 
            将属性列表输出到指定的输出流



###  . properties 配置文件：

###### 格式为文本文件，文件的内容的格式是“键=值”的格式，文本注释信息可以用#来注释，可以使用EL表达式拿值

### 加载配置文件方式（两种）：

##### 1:ResourceBundle 类

​    static ResourceBundle getBundle(String baseName) 
​           使用指定的基本名,调用者的类加载器获取资源包
​    String getString(String key) 
​           从此资源包中获取给定键的字符串
​    Set<String> keySet() 
​           返回此 ResourceBundle 及其父包中包含的所有键的 Set

```java
例：
	ResourceBundle  rb = ResourceBundle.getBundle("jdbc");     // 在resource文件夹下，对应classpath
	String str = rb.getString("jdbc.username");   jbdc.username = root;
```



##### 2:从流中加载.properties 文件

​	        fis = new FileInputStream(new File("D:\\Ecplise workspace\\Test\\src\\druid.properties"));
		BufferedInputStream bs=new BufferedInputStream(fis);
		Properties pro=new Properties();
		pro.load(bs);
		System.out.println(pro.getProperty("url"));




