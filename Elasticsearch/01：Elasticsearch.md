###  Elasticsearch【开箱即用】

------

​	是一个实时分布式搜索和分析引擎，用于**全文搜索、结构化搜索、分析**以及将这三者混合使用；

##### 01：介绍

​	Elasticsearch也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，但是它的目的是通过简单的`RESTful API`来隐藏Lucene的复杂性，从而让全文搜索变得简单。

##### 02：安装ES

###### 版本

- Elasticsearch **7.0 开始，内置了 Java 环境**，所以说，安装 7.0+ 版本会方便很多；

###### 安装流程：

1. 解压

   ```sh
   tar -zxvf elasticsearch-7.1.0.tar.gz
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

   1. ```sh
      # 前台启动
      ./bin/elasticsearch
      ```

4. 看到started，表示启动成功，curl 127.0.0.1:9200?pretty  看是否启动成功；

##### 03：Kibana

- Kibana是一个旨在与Elasticsearch一起使用的开源分析和可视化平台。您可以使用Kibana搜索，查看和与Elasticsearch索引中存储的数据进行交互。

- 基于浏览器的界面使您能够快速创建和共享动态仪表板，以实时显示对Elasticsearch查询的更改。

  - 下载适用于您平台Kibana 4二进制软件包。
  - 解压缩`.zip`或`tar.gz`存档文件。
  - 安装Kibana插件（可选）。
  - 从安装目录运行Kibana：`bin/kibana`，Kibana现在在5601端口上运行。

- **Sense** 是一个 [Kibana](https://www.elastic.co/guide/en/kibana/4.6/index.html) 应用 它提供交互式的控制台**，通过浏览器直接向 Elasticsearch 提交请求**

  - 安装与运行 Sense：

    1. 在 Kibana 目录下运行下面的命令，下载并安装 Sense app：

       ```sh
       ./bin/kibana plugin --install elastic/sense
       ```

    2. 启动 ./bin/kibana
    3. 在你的浏览器中打开 Sense: `http://localhost:5601/app/sense`；

##### 04：ES 交互

- **Java API**

  如果你正在使用 Java，在代码中你**可以使用 Elasticsearch 内置的两个客户端**：

  - **节点客户端（Node client）**
    - 节点客户端作为一个非数据节点加入到本地集群中。换句话说，它本身不保存任何数据，但是它知道数据在集群中的哪个节点中，并且可以把请求转发到正确的节点。
  - **传输客户端（Transport client）**
    - 轻量级的传输客户端可以将请求发送到远程集群。它本身不加入集群，但是它可以将请求转发到集群中的一个节点上。
  - 两个 Java 客户端都是通过 *9300* 端口并使用 Elasticsearch 的原生传输协议和集群交互。集群中的节点通过端口 9300 彼此通信。如果这个端口没有打开，节点将无法形成一个集群。

- **RESTful API with JSON over HTTP**

  - 其他语言可以使用 RESTful API 通过端口 *9200* 和 Elasticsearch 进行通信；

  - 一个 Elasticsearch 请求和任何 HTTP 请求一样由若干相同的部件组成：

    ```js
    curl -X<VERB> '<PROTOCOL>://<HOST>:<PORT>/<PATH>?<QUERY_STRING>' -d '<BODY>'
    ```

    被 `< >` 标记的部件：

    | `VERB`         | 适当的 HTTP *方法* : `GET`、 `POST`、 `PUT`、 `HEAD` 或者 `DELETE`。 |
    | -------------- | ------------------------------------------------------------ |
    | `PROTOCOL`     | `http` 或者 `https`（如果你在 Elasticsearch 前面有一个 `https` 代理） |
    | HOST           | **ES 集群中任意节点的主机名**，或者用 `localhost` 代表本地机器上的节点。 |
    | `PORT`         | 运行 Elasticsearch HTTP 服务的端口号，默认是 `9200` 。       |
    | `PATH`         | **API 的终端路径**（例如 `_count` 将返回集群中文档数量）。Path 可能包含多个组件，例如：`_cluster/stats` 和 `_nodes/stats/jvm` 。 |
    | `QUERY_STRING` | **任意可选的查询字符串参数** (例如 `?pretty` 将格式化地输出 JSON 返回值，使其更容易阅读) |
    | `BODY`         | **一个 JSON 格式的请求体** (如果请求需要的话)                |

  - ```sh
    curl -XGET 'http://localhost:9200/_count?pretty' -d '
    {
        "query": {
            "match_all": {}
        }
    }
    '
    ```

  - 返回数据

    - Elasticsearch 返回一个 HTTP 状态码一个 JSON 格式的返回值

    - ```json
      {
          "count" : 0,
          "_shards" : {
              "total" : 5,
              "successful" : 5,
              "failed" : 0
          }
      }
      ```

##### 05：集群和节点

- **节点(node)：**是一个运行着的Elasticsearch实例；【非机器】
- **集群(cluster)：**是**一组具有相同`cluster.name`的节点集合**，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群
  - 注意：修改其默认值，防止新启动的节点加入相同网络中的另一个同名的集群中；
  - 通过修改`config/`目录下的`elasticsearch.yml`文件，然后重启ELasticsearch；

##### 06：面向文档

​	是**面向文档(document oriented)**的，意味着它可以存储整个对象或**JSON文档(document)**。然而它不仅仅是存储，还会 **索引(index)** 每个文档的内容使之可以被搜索。在Elasticsearch中，你可以对文档（而非成行成列的数据）进行**索引、搜索、排序、过滤**【非关系型数据库：不用再将复杂的对象，拆分成行列形式存储】；

##### 7：索引

​	Elasticsearch集群可以包含多个**索引(indices)**（数据库），每一个索引可以包含多个**类型(types)**（表），每一个类型包含多个**文档(documents)**（行），然后每个文档包含多个**字段(Fields)**（列）。

- **索引 这个词在 Elasticsearch 语境中包含多重意思**
- **「索引一个文档」（动词）**表示把一个文档存储到**索引（名词）**里，以便它可以被检索或者查询，如果文档已经存在，新的文档将覆盖旧的文档;
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

1. ###### 检索文档

   - 执行HTTP GET请求，需要指出文档的“地址”——**索引、类型和ID【文档ID】**既可

   - ```JSON
     # 指定文档ID
     GET /megacorp/employee/1
     # 查询字符串搜索，像传递URL参数一样去传递查询语句
     GET /megacorp/employee/_search?q=name:Smith
     
     # 返回结果包含了文档的一些元数据，以及 _source 属性，内容是雇员的原始 JSON 文档
     {
       "_index" :   "megacorp",
       "_type" :    "employee",
       "_id" :      "1",
       "_version" : 1,
       "found" :    true,
       "_source" :  {
           "first_name" :  "John",
           "last_name" :   "Smith",
           "age" :         25,
           "about" :       "I love to go rock climbing",
           "interests":  [ "sports", "music" ]
       }
     }
     ```

2. ###### 轻量搜索【Query-String】

   - 第一个尝试的几乎是最简单的搜索了;
   
   - 返回结果包括了所有三个文档，放在数组 `hits` 中。一个搜索默认返回十条结果。
   
   - ```json
     # 搜索全体员工
     GET /megacorp/employee/_search
     {
       "took":      6,
       "timed_out": false,
       "_shards": { ... },
       "hits": {
                     "total":      3,
                     "max_score":  1,
                     "hits": [
                       {
                         "_index":         "megacorp",
                         "_type":          "employee",
                         "_id":            "3",
                         "_score":         1,
                         "_source": {
                           "first_name":  "Douglas",
                           "last_name":   "Fir",
                           "age":         35,
                           "about":       "I like to build cabinets",
                           "interests": [ "forestry" ]
                         }
                       },{
     
                       } 
                     ]
                   }
     }
     ```
   
3. ###### 使用查询表达式【使用DSL语句查询（Domain Specific Language特定领域语言）】

   - 不再使用查询字符串作为参数，而是使用JSON请求体代替；

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

4. ###### 全文搜索

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

5. ###### 短语搜索

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

6. ###### 高亮搜索

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

7. ###### 聚合（aggregations）

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





