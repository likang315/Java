### Jackson

------

[TOC]

------

##### 01：概述

- 用来序列化和反序列化 json 的 Java 的开源框架， Jackson 解析大的 json 文件速度比较快；Jackson 运行时占用内存比较低，性能比较好；Jackson 有灵活的 API，可以很容易进行扩展和定制。

##### 02：Jackson 的组成

- **jackson-core：**核心包，提供基于”流模式”解析的相关 API，它包括 JsonPaser 和 JsonGenerator。 Jackson 内部实现正是通过高性能的流模式 API 的 JsonGenerator 和 JsonParser 来生成和解析 json。
- **jackson-annotations：**注解包，提供标准注解功能；
- **jackson-databind ：**数据绑定包， 提供基于”对象绑定” 解析的相关 API （ ObjectMapper ） 和”树模型” 解析的相关 API （JsonNode）；这些API依赖基于”流模式”解析的 API。

##### 03：导入Jar

```xml
<!--jackson-->
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-annotations</artifactId>
</dependency>

<dependency>
  <groupId>com.fasterxml.jackson.module</groupId>
  <artifactId>jackson-module-afterburner</artifactId>
</dependency>

<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-guava</artifactId>
</dependency>

<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-yaml</artifactId>
</dependency>
```

##### 04：ObjectMapper 的使用

- ObjectMapper 通过writeValue 系列方法，将对象序列化jsonString；
- ObjectMapper 通过 readValue 系列方法，将 json 反序列化为 java 对象；

###### 配置

- 需要设置 ObjectMapper 的相关配置信息。这些配置信息应用 java 对象的所有属性上；
- DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES：反序列化时忽略Json中存在但对象中不存在的属性，设为False；
- .defaultDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS))
  - 不设置，默认时间序列化成时间戳；
-  DeserializationFeature，SerializationFeature 和 Include 有关配置的类；

##### 05：Jackson 注解

- 根据实际需要，灵活的调整它的序列化、反序列化默认方式；

- | 注解               | 用法                                                         |
  | :----------------- | :----------------------------------------------------------- |
  | **@JsonProperty**  | 用于属性，序列化时把属性的名称转换为另外一个名称；反序列化时，可以解决Json串中的属性和对象属性不一致问题；                                                               示例： @JsonProperty(“stu_id”) private Date stuId; |
  | **@JsonFormat**    | 用于属性或者方法，把属性的格式序列化时转换成指定的格式。示例： @JsonFormat(timezone = “GMT+8”, pattern = “yyyy-MM-dd HH:mm”) public Date getBirthDate() |
  | **@JsonIgnore**    | 过滤掉需要序列化的属性，用在Getter方法上；                   |
  | @JsonPropertyOrder | 用于类， 指定属性在序列化时 json 中的顺序 ， 示例： @JsonPropertyOrder({ “birth_Date”, “name” }) public class Person |
  | @JsonCreator       | 用于构造方法，和 @JsonProperty 配合使用，适用有参数的构造方法。 示例： @JsonCreator public Person(@JsonProperty(“name”)String name) {…} |
  | @JsonAnySetter     | 用于属性或者方法，设置未反序列化的属性名和值作为键值存储到 map 中 @JsonAnySetter public void set(String key, Object value) { map.put(key, value); } |
  | @JsonAnyGetter     | 用于方法 ，获取所有未序列化的属性 public Map<string, object> any() { return map; } |

##### 06：Jcakson 的高阶使用

###### 格式处理

- 对于日期类型 java.util.Calendar,java.util.GregorianCalendar,java.sql.Date,java.util.Date,java.sql.Timestamp，若不指定格式， 在 json 文件中将序列化 为 **long 类型的数据**。显然这种默认格式，可读性差，转换格式是必要的。
- 对于日期类型为 java.time.LocalDate，还需要添加代码 mapper.registerModule(new JavaTimeModule());

###### 泛型反序列化

- 对于 Collection 类型 ，可以调用 constructCollectionType 方法来序列化，也可以构造 TypeReference 来序列化。

  ```java
  CollectionType javaType = mapper.getTypeFactory()
    .constructCollectionType(List.class, Person.class);
  // 第二参数是 map 的 key 的类型，第三参数是 map 的 value 的类型
   MapType javaType =
      mapper.getTypeFactory().constructMapType(
     	Map.class, String.class, Person.class);
  
   List<Person> personList = mapper.readValue(jsonInString, javaType);
   List<Person> personList = mapper.readValue(jsonInString, new
      TypeReference<List<Person>>(){});
  ```

###### 属性可视化【废弃】

- 默认的属性可视化的规则如下：
  1. 若该属性修饰符是 public，该属性可序列化和反序列化。
  2. 若属性的修饰符不是 public，但是它的 getter 方法和 setter 方法是 public，该属性可序列化和反序列化。因为 getter 方法用于序列化， 而 setter 方法用于反序列化。
  3. 若属性**只有 public 的 setter 方法，而无 public 的 getter 方 法，该属性只能用于反序列化**。
- 若想更改默认的属性可视化的规则，需要调用 ObjectMapper 的方法 setVisibility
  - mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

###### 属性过滤

- 将 Java 对象序列化为 json 时 ，有些属性需要过滤掉，不显示在 json 中 ， Jackson 有多种实现方法。
- 注解方式， 可以用 **@JsonIgnore** 过滤单个属性或用 **@JsonIgnoreProperties** 过滤多个属性;
  - 用在Getter方法，或者类上；

###### 自定义序列化和反序列化的类

- 自定义序列化类。

  - 自定义的序列化类需要直接或间接**继承 StdSerializer 或 JsonSerializer，同时需要利用 JsonGenerator 生成 json，重写方法 serialize；**

- ```java
  public class CustomSerializer extends StdSerializer<Person> {
    @Override
    public void serialize(Person person, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
      jgen.writeStartObject();
      jgen.writeNumberField("age", person.getAge());
      jgen.writeStringField("name", person.getName());
      jgen.writeEndObject();
    }
  }
  ```

- 自定义反序列化类。

  - 自定义的反序列化类需要直接或间接**继承 StdDeserializer 或 JsonDeserializer，同时需要利用 JsonParser 读取 json，重写方法 deserialize**；

    ```java
    public class CustomDeserializer extends StdDeserializer<Person> {
      @Override
      public Person deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Person person = new Person();
        int age = (Integer) ((IntNode) node.get("age")).numberValue();
        String name = node.get("name").asText();
        // add process...
        person.setAge(age);
        person.setName(name);
        return person;
      }
    }
    ```

- 定义好自定义序列化类和自定义反序列化类，若想在程序中调用它们，还**需要注册到 ObjectMapper 的 Module**;

  - ```java
     mapper.registerModule(module);
    ```

##### 07：树模型

- Jackson 也提供了树模型(tree model)来生成和解析 json。

- 若想修改或访问 json 部分属性，树模型是不错的选择。树模型由 JsonNode 节点组成。程序中常常使用 ObjectNode，ObjectNode 继承于 JsonNode；

- JsonNode是不可变的，一般用于读取。ObjectNode可变，一般用于创建Json对象图;

- ```java
  bjectMapper mapper = new ObjectMapper();
  // 构建 ObjectNode
  ObjectNode personNode = mapper.createObjectNode();
  // 更改属性
  personNode.put("name", "Tom");
  personNode.put("age", 40);
  ObjectNode addressNode = mapper.createObjectNode();
  addressNode.put("zip", "000000");
  addressNode.put("street", "Road NanJing");
  // 设置子节点
  personNode.set("address", addressNode);
  // 通过 path 查找节点
  JsonNode searchNode = personNode.path("street")；
  ```

##### 08：Tool

```java
/**
 * Json 工具类
 *
 * @author kangkang.li@qunar.com
 * @date 2021-01-24 18:39
 */
@Slf4j
@UtilityClass
public class JsonUtils {

    @Getter
    private final ObjectMapper om;

    private final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    static {
        om = JsonMapper.builder()
                .enable(JsonParser.Feature.ALLOW_COMMENTS)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                // 反序列化时忽略Json中存在但对象中不存在的属性
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 忽略为 null 的属性
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .defaultDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS))
                // @JsonFormat将用到这个时区
                .defaultTimeZone(TimeZone.getDefault())
                .addModule(new AfterburnerModule())
                .addModule(new GuavaModule())
                // 增加自定义序列化类
                .addModule(new JavaTimeModule())
                .build();
    }

    /**
     * 反序列化
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(String jsonStr, Class<T> clazz) {
        if (Objects.isNull(jsonStr) || Objects.isNull(clazz)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(jsonStr, clazz));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonStr = {}, class = {}", jsonStr, clazz, e);
            return Optional.empty();
        }
    }

    /**
     * 将异常情况封装成RunTimeException抛出
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T readValueUnchecked(String jsonStr, Class<T> clazz) {
        return om.readValue(jsonStr, clazz);
    }

    /**
     * 反序列化复杂类型
     *
     * @param jsonStr
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(String jsonStr, TypeReference<T> typeReference) {
        if (Objects.isNull(jsonStr) || Objects.isNull(typeReference)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(jsonStr, typeReference));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonStr = {}, typeReference = {}", jsonStr, typeReference.getType(), e);
            return Optional.empty();
        }
    }

    /**
     * 将异常情况封装成RunTimeException抛出
     *
     * @param jsonStr
     * @param typeReference
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T readValueUnchecked(String jsonStr, TypeReference<T> typeReference) {
        return om.readValue(jsonStr, typeReference);
    }

    /**
     * 从树模型中反序列化对象
     *
     * @param jsonNode
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(JsonNode jsonNode, Class<T> clazz) {
        if (Objects.isNull(jsonNode) || Objects.isNull(clazz)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.treeToValue(jsonNode, clazz));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonNode = {}, class = {}", jsonNode, clazz, e);
            return Optional.empty();
        }
    }

    /**
     * 从树模型中反序列化对象
     *
     * @param jsonNode
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> Optional<T> readValue(JsonNode jsonNode, TypeReference<T> typeReference) {
        if (Objects.isNull(jsonNode) || Objects.isNull(typeReference)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(om.treeAsTokens(jsonNode), typeReference));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonNode = {}, typeReference = {}", jsonNode, typeReference.getType(), e);
            return Optional.empty();
        }
    }

    /**
     * 序列化对象
     *
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) {
        try {
            return Optional.of(om.writeValueAsString(obj)).orElse("");
        } catch (IOException e) {
            log.error("序列化对象到JSON字符串失败: object = {}", obj, e);
            return "";
        }
    }

    /**
     * 从Json串中获取树模型，处理大Json
     *
     * @param jsonStr
     * @return
     */
    public Optional<JsonNode> readTree(String jsonStr) {
        if (Objects.isNull(jsonStr)) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readTree(jsonStr));
        } catch (IOException e) {
            log.error("JSON字符串反序列化失败: jsonStr = {}", jsonStr, e);
            return Optional.empty();
        }
    }
}
```

