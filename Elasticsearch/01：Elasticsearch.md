###  Elasticsearch

------

​	是一个实时分布式搜索和分析引擎，用于全文搜索、结构化搜索、分析以及将这三者混合使用；

##### 1：介绍

​	Elasticsearch也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，但是它的目的是通过简单的`RESTful API`来隐藏Lucene的复杂性，从而让全文搜索变得简单。

##### 2：安装ES

###### 版本

- Elasticsearch 7.0 开始，内置了 Java 环境，所以说，安装 7.0+ 版本会方便很多；

###### 安装流程：

1. 解压

   ```
   tar -zxvf elasticsearch-7.1.0-darwin-x86_64.tar.gz
   ```

2. | 目录    | 配置文件         | 描述                                                     |
   | ------- | ---------------- | -------------------------------------------------------- |
   | bin     |                  | 放置脚本文件，如启动脚本 elasticsearch, 插件安装脚本等。 |
   | config  | elasticserch.yml | elasticsearch 配置文件，如集群配置、jvm 配置等。         |
   | jdk     |                  | java 运行环境                                            |
   | data    | path.data        | 数据持久化文件                                           |
   | lib     |                  | 依赖的相关类库                                           |
   | logs    | path.log         | 日志文件                                                 |
   | modules |                  | 包含的所有 ES 模块                                       |
   | plugins |                  | 包含的所有已安装的插件                                   |

3. 运行ES

   1. ```xml
      # 前台启动
      ./bin/elasticsearch
      ```

4. 看到started，表示启动成功，curl 127.0.0.1:9200  看是否启动成功；

##### 3：安装Marvel插件

- Marvel是ES的管理和监控插件 ，可在ES目录下运行命令来下载和安装；

- ```
  ./bin/plugin -i elasticsearch/marvel/latest
  ```

- 若本地安装了Marvel，可以在浏览器中通过以下地址访问它
  
  - http://localhost:9200/_plugin/marvel/

##### 4：集群和节点

- **节点(node)：**是一个运行着的Elasticsearch实例；【非机器】
- **集群(cluster)：**是一组具有相同`cluster.name`的节点集合，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群
  - 注意：修改其默认值，防止新启动的节点加入相同网络中的另一个同名的集群中；
  - 通过修改`config/`目录下的`elasticsearch.yml`文件，然后重启ELasticsearch；

##### 5：API

- **节点客户端：**以无数据节点(none data node)身份加入集群，换言之，它自己不存储任何数据，但是它知道数据在集群中的具体位置，并且能够直接转发请求到对应的节点上。
- **传输客户端：**传输客户端能够发送请求到远程集群。它自己不加入集群，只是简单转发请求给集群中的节点。

两个Java客户端都通过**9300端口**与集群交互，使用Elasticsearch传输协议(Elasticsearch Transport Protocol)。集群中的节点之间也通过9300端口进行通信。

- **HTTP请求**：

- ```sql
  curl -X<VERB> '<PROTOCOL>://<HOST>:<PORT>/<PATH>?<QUERY_STRING>' -d '<BODY>'
  # 示例
  curl -XGET 'http://localhost:9200/_count?pretty' -d '
  {
      "query": {
          "match_all": {}
      }
  }
  '
  ```

##### 6：面向文档

​	是**面向文档(document oriented)**的，意味着它可以存储整个对象或**JSON文档(document)**。然而它不仅仅是存储，还会**索引(index)**每个文档的内容使之可以被搜索。在Elasticsearch中，你可以对文档（而非成行成列的数据）进行索引、搜索、排序、过滤【非关系型数据库：不用再将复杂的对象，拆分成行列形式存储】；

##### 7：索引

​	Elasticsearch集群可以包含多个**索引(indices)**（数据库），每一个索引可以包含多个**类型(types)**（表），每一个类型包含多个**文档(documents)**（行），然后每个文档包含多个**字段(Fields)**（列）。

- **「索引一个文档」**表示把一个文档存储到**索引（名词）**里，以便它可以被检索或者查询,如果文档已经存在，新的文档将覆盖旧的文档;
- 倒排索引：默认情况下，文档中的所有字段都会被**索引**（拥有一个倒排索引），都可以被搜索到；

###### 示例

```SQL
PUT /megacorp/employee/1
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}

# megacorp	索引名
# employee	类型名
# 1	这个员工的ID [文档]
```

##### 8：搜索

1. ###### 使用GET请求

   - 执行HTTP GET请求，需要指出文档的“地址”——**索引、类型和ID【文档ID】**既可

   - ```SQL
     # 指定文档ID
     GET /megacorp/employee/1
     # 搜索全体员工
     GET /megacorp/employee/_search
     # 查询字符串搜索，像传递URL参数一样去传递查询语句
     GET /megacorp/employee/_search?q=name:Smith
     ```

2. ###### 使用DSL语句查询（Domain Specific Language特定领域语言）

   - 不再使用查询字符串作为参数吗，而是使用JSON请求体代替；
   - **match语句**
   
   - ```sql
     GET /megacorp/employee/_search
     {
         "query" : {
             "match" : {
                 "last_name" : "Smith"
             }
         }
     }
     ```
   
   - **过滤器(filter)**
   
     ```sql
     GET /megacorp/employee/_search
     {
         "query" : {
             "filtered" : {
                 "filter" : {
                     "range" : {
                         "age" : { "gt" : 30 }
                     }
                 },
                 "query" : {
                     "match" : {
                         "name" : "smith"
                     }
                 }
             }
         }
     }
     ```
   
3. ###### 全文搜索

   - 在各种文本字段中进行全文搜索，并且返回相关性最大的结果集；
   - "_score"：相关度分数；

   - ```sql
      GET /megacorp/employee/_search
      {
          "query" : {
              "match" : {
                  "about" : "rock climbing"
              }
          }
      }
      
      {
         ...
         "hits": {
            "total":      2,
            "max_score":  0.16273327,
            "hits": [
               {
                  ...
                  "_score":         0.16273327,
                  "_source": {
                     "first_name":  "John",
                     "last_name":   "Smith",
                     "age":         25,
                     "about":       "I love to go rock climbing",
                     "interests": [ "sports", "music" ]
                  }
               },
               {
                  ...
                  "_score":         0.016878016,
                  "_source": {
                     "first_name":  "Jane",
                     "last_name":   "Smith",
                     "age":         32,
                     "about":       "I like to collect rock albums",
                     "interests": [ "music" ]
                  }
               }
            ]
         }
      }
      ```

4. ###### 短语搜索

   - 想要确切的匹配若干个单词或者**短语(phrases)**，不想要全文搜索；

      ```sql
      GET /megacorp/employee/_search
      {
          "query" : {
              "match_phrase" : {
                  "about" : "rock climbing"
              }
          }
      }
      ```

5. ###### 高亮搜索

   - 有些应用喜欢从每个搜索结果中**高亮(highlight)**匹配到的关键字，这样用户可以知道为什么这些**文档和查询相匹配**；

     ```sql
     GET /megacorp/employee/_search
     {
         "query" : {
             "match_phrase" : {
                 "about" : "rock climbing"
             }
         },
         "highlight": {
             "fields" : {
                 "about" : {}
             }
         }
     }
     
     {
        ...
        "hits": {
           "total":      1,
           "max_score":  0.23013961,
           "hits": [
              {
                 ...
                 "_score":         0.23013961,
                 "_source": {
                    "first_name":  "John",
                    "last_name":   "Smith",
                    "age":         25,
                    "about":       "I love to go rock climbing",
                    "interests": [ "sports", "music" ]
                 },
                 "highlight": {
                    "about": [
                       "I love to go <em>rock</em> <em>climbing</em>" <1>
                    ]
                 }
              }
           ]
        }
     }
     ```

6. ###### 聚合（aggregations）

   - 所有职员中最大兴趣点是什么

   - ```sql
     GET /megacorp/employee/_search
     {
       "aggs": {
         "all_interests": {
           "terms": { "field": "interests" }
         }
       }
     }
     ```





