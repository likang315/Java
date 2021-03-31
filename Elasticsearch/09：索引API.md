### 索引API

------

##### 06：映射

- **对索引字段的定义，包括数据类型、存储属性、分析器、词向量等**；

- ```json
  # 创建索引
  put index
  {
  	"指定映射中的字段或属性
      # 指定ying'sh
      "propesties": {
    		# name 的字段类型 为text
        "name": {
          "type": "text"
        },
        "age": {
          "type": "integer"
        },
  			"sex": {
          "type": "boolean",
        }
  			"value": {
          "type"："scaled_float",
          "scaling_factor": 100
        }
  			# create_time字段为日期（date）类型，并指定日期格式
        "create_time": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd"
        }
      }
    }
  }
  ```

###### 数据类型

1. 数字类型

   - 长整型（long）、整型（integer）、短整型（ short ） 、字节（ byte ） 、双精度浮点型（ double ） 、浮点型（ float ） 、半精度浮点型（ half_float ） 、可变浮点型（scaled_float）。

2. 布尔类型

   - JSON格式的true和false，但也可以接受解释为真或假的字符串，false、"false"、true、"true"

3. 数组类型

   - 在ES中，**没有专用的数组类型**。默认情况下，任何字段都可以包含零个或多个值，但是数组中的**所有值都必须具有相同的数据类型**；

4. 日期类型

   - JSON 格式规范中没有对日期数据类型进行定义，因此日期格式可以是Java中任意的日期类型；
   - 在Elasticsearch内部，日期转换为UTC（如果指定了时区），并存储为毫秒数时间戳，检索时转换成定义的格式；
   - 可以使**用分隔符来指定多种格式**。每种格式将依次尝试，直到找到匹配的格式。第一种格式将用于将毫秒转换回字符串

5. 关键字

   - 关键字（keyword）这种类型的特点是，**不再分词，直接作为一个Term索引**，检索时也只能用精确值检索；

6. 文本

   - 文本（text）这种类型会进行**解析、分词，索引的是解析后的Term**，检索时根据Term就可以检索到，一般适用于长文本的全文检索；

7. 地理位置类型

   - 地理位置（geo）是用于存储经纬度的字段类型；

   - geo类型支持多种形式

     ```json
     {
       "location": {
         "lat": 41.12,
         "lon": 23.2
       }, 
       "location": [
         41.12,
         23.2
        ],
       "location": "41.12, 23.2"
     }
     ```

###### 属性设置

- 映射属性（参数）决定了字段（Field）的存储、索引、搜索、分析等方面的功能和特；

- index 属性

  - 是否对字段值进行索引，它接受true或false，并默认为true；

- store 属性

  - 默认情况下，字段值被索引以使其可搜索，但**它们不会被存储**。如果设为true，会把字段的值单独存一份倒排索引；

- **分词器 属性**

  - 一般情况下**配置于Text类型的字段中**。表示索引和检索时采用的分析方法；

  - ```json
    {
      "settings": {
        "analysis": {
          #  自定义的分词器
          "analyzer": {
            "analyzer_name": { 
              "type": "custom",
              "tokenizer": "standard",
          		# token filter
              "filter": [
                "lowercase",
                "asciifolding"
              ]
            }
          }
        }
      },
      "mappings": {
        "properties": {
          "desc": {
            "type": "text",
            "analyzer": "analyzer_name" 
          }
        }
      }
    }
    ```

  - character filter：接收原字符流，通过添加、删除或者替换操作改变原字符流。

  - tokenizer：将一整段文本拆分成一个个的词，在一个分词器中,`有且只有一个`tokenizeer；

  - token filters：将切分的单词添加、删除或者改变，可以有零个或者多个token filters。例如将所有英文单词小写；

- ###### doc_values

  - 索引的存储结构采用的是一种叫倒排索引的数据结构。
  - 倒排索引允许查询，**在唯一排序的Term列表中查找搜索Term，并且从中可以立即访问包含该Term的文档列表**。
  - doc_values：是磁盘上的数据结构，**在文档索引时构建，这使得这种数据访问模式成为可能**。它们存储的值与_source相同，**以面向列的方式存储**，这对于排序和聚合更有效；
  - 默认情况下，所有支持doc_values的字段都启用了这个功能，如果**确定不需要对某字段进行排序或聚合，或从脚本访问字段值，则可以禁用此功能以节省磁盘空间**；
  - 