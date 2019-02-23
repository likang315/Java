## Json：JavaScript Object Notation(JavaScript 对象表示法)

是存储和交换文本信息的语法，类似 XML

Json 文件的文件类型是 ".json"
Json 文本的 MIME 类型是 "application/json"

######  特点：

1:JSON 可通过 JavaScript 进行解析
2:JSON 数据可使用 AJAX 进行传输
4:能够使用内建的 JavaScript eval() 方法进行解析
5:使用数组

对于 AJAX 应用程序来说，JSON 比 XML 更快更易使用

######   使用 XML

  	读取 XML 文档
    	使用 XML DOM 来循环遍历文档
    	读取值并存储在变量中

######   使用 Json

读取 Json 字符串
用 eval() 处理 Json 字符串

##### 1:Json 对象表示

[{key：value},{key：value}] 
key：可以是任意数据类型，value:可以是Number,boolean,String,Array,null,还可以是嵌入对象

##### 2:Json 对象

?	使用 对象.key 访问
?	delete 关键字来删除 Json 对象的属性，直接赋值可以修改

##### 3：JSON.parse(text,reviver)    将字符串数据转换为 JavaScript 对象  

    text:    必需， 一个有效的 JSON 字符串
    reviver: 可选，一个转换结果的函数,将为对象的每个成员调用此函数

##### 4：解析函数

?	JSON 不允许包含函数，但你可以将函数作为字符串存储，之后再将字符串转换为函数

  var text = '{ "name":"Runoob", "alexa":"function () {return 10000;}", "site":"www.runoob.com"}';
  var obj = JSON.parse(text);
  obj.alexa = eval("(" + obj.alexa + ")");
  document.getElementById("demo").innerHTML = obj.name + " Alexa 排名：" + obj.alexa();

##### 5：eval() 

JavaScript的函数，用于将 JSON 文本转换为 JavaScript 对象,使用的是 JavaScript 编译器，可解析 JSON 文本，
然后生成 JavaScript 对象，必须把文本包围在括号中，这样才能避免语法错误

##### 6：JSONObject

实例化此对象，就是JSon对象，然后使用put(key,value)方法，toJsonString（）方法直接将Json 转换为String

```java
 String json = "{'first': 'one','next': 'two'}";
        public String[] convert(String json)
    {
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


