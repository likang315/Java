## Json：JavaScript Object Notation (JS 对象表示法)

是存储和交换文本信息的语法，类似 XML

Json 文件的文件类型是 ".json" Json 文本的 MIME 类型是 "application/json"

###### 特点：  

​	1:JSON 可通过 JavaScript 进行解析 

​	2:JSON 数据可使用 AJAX 进行传输 

​	3:能够使用内建的 JavaScript eval() 方法进行解析 

​	4:使用数组

对于 AJAX 应用程序来说，JSON 比 XML 更快更易使用

###### 使用 XML

```
  读取 XML 文档
	使用 XML DOM 来循环遍历文档
	读取值并存储在变量中
```

###### 使用 Json

读取 Json 字符串 用 eval() 处理 Json 字符串



### 1：Json 对象表示

 key：可以是任意数据类型，value :可以是 Number,boolean,String,Array,null,还可以是嵌入对象

```json
[{key：value},{key：value}]

{ "name":"菜鸟教程" , "url":"www.runoob.com" }
//可以是一个对象的两个属性
```

```json
{
    "sites": [
                { "name":"菜鸟教程" , "url":"www.runoob.com" }, 
                { "name":"google" , "url":"www.google.com" }, 
                { "name":"微博" , "url":"www.weibo.com" }
              ]
}
//此Json对象 site 包含三个数组，每个数组都是一个对象
```

```json
Obj = {
    "name":"runoob",
    "alexa":10000,
    "sites": {
        "site1":"www.runoob.com",
        "site2":"m.runoob.com",
        "site3":"c.runoob.com"
    }
}
//josn对象嵌套Json对象，访问
x = Obj.sites.site1;
// 或者
x = Obj.sites["site1"];
```



### 2：Json 对象

使用 对象名.key 访问 delete 关键字来删除 Json 对象的属性，直接赋值可以修改

JSON 对象在大括号（{}）中书写，对象可以包含多个键/值对（属性）

### 3：Json 语法规则

- 数据在键/值对中
- 数据由逗号分隔
- 大括号保存对象，{ }
- 中括号保存数组，[ ]

### 4：Json 访问对象值

```json
var myObj, x;
myObj = { "name":"runoob", "alexa":10000, "site":null };
x = myObj.name;
```



##### 修改值

你可以使用点号(.)来修改 JSON 对象的值：

myObj.sites.site1 = "www.google.com";

##### 删除对象属性

使用 **delete** 关键字来删除 JSON 对象的属性：

delete Obj.sites.site1;

delete Obj.sites["site1"]



### 3：Json解析

- ##### JSON.parse(text,reviver) 将字符串数据转换为 JavaScript 对象

- JSON 不能存储 Date 对象。

  如果你需要存储 Date 对象，需要将其转换为字符串

```
text:    必需， 一个有效的 JSON 字符串
reviver: 可选，一个转换结果的函数,将为对象的每个成员调用此函数
```

```javascript
var obj = JSON.parse(text, function (key, value) {
    if (key == "initDate") {
        return new Date(value);
    } else {
        return value;
}});
//启用 JSON.parse 的第二个参数 reviver，一个转换结果的函数，对象的每个成员调用此函数
```

- 使用Ajax 解析 Json

```javascript
<script>
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
          myObj = JSON.parse(this.responseText);
          document.getElementById("demo").innerHTML = myObj.name;
      }
  };
  xmlhttp.open("GET", "/try/ajax/json_demo.txt", true);
  xmlhttp.send();

</script>
```



##### 5：eval()

JavaScript的函数，用于将 JSON 文本转换为 JavaScript 对象,使用的是 JavaScript 编译器，可解析 JSON 文本， 然后生成 JavaScript 对象，必须把**文本包围在括号中**，这样才能避免语法错误

```javascript
var obj = eval ("(" + txt + ")");

document.getElementById("name").innerHTML=obj.sites[0].name 
document.getElementById("url").innerHTML=obj.sites[0].url 
```



### 6：JSONObject 使用

实例化此对象，就是JSon对象，然后使用put(key,value)方法，toJsonString（）方法直接将Json 转换为String

```java
 String json = "{'first': 'one','next': 'two'}";
 public String[] convert(String json) {
        String[] str=new String[20];
        int i=0;
        try {
            //先转为JsonObject对象
            JSONObject jsonObject = JSONObject.parseObject(json);
            Set<String> set=jsonObject.keySet();
            Iterator ite= set.iterator();
            while (ite.hasNext()){
                str[i++]=(String) jsonObject.get(ite.next());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
 }
```



### 7：alibaba 的 fastJson

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.54</version>
</dependency>
```



### 8：Google 的 Gson

```；；xml
<!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>
```

###### 1：Gson 的茶创建方式

​	Gson  gson = new Gson();

###### 2：JavaBean 和 Json 字符串相互转换

- public String toJson(Object obj )：将Java Bean 转换为 Json 字符串
- public T fromJson（String jsonStr，T class）：将Json字符串转化为JavaBean对象

###### 3：JsonElement 为其父类

​	JsonObject 为其子类

###### 4：Json 字符串的序列化和反序列化：Java Bean转换为Json 字符串

​	使用TypeAdapter 对象转换

```java
Gson gson = new GsonBuilder().create();
TypeAdapter<Person> typeAdapter = gson.getAdapter(Person.class);

String json = "{\"name\":\"栗霖\",\"age\":\"18\",\"hobby\":\"我很是很喜欢FromSoftWare的。大爱宫崎英高，赞美太阳\"}";
PersonJson p = new PersonJson("栗霖",18,"混系列忠实粉丝");

System.out.println(typeAdapter.toJson(p));
try {
  System.out.println(typeAdapter.fromJson(json));
}catch (Exception e) {
  e.printStackTrace();
}
```

