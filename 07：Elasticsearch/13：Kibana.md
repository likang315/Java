### Kibana 

------

[TOC]

------

##### 01：概述

- 以使用 Kibana 来搜索，查看存储在 Elasticsearch 索引中的数据并与之交互。你可以很容易实现高级的数据分析和可视化，以图表的形式展现出来；
- 侧边栏：
  - Discover：用于和Elasticsearch交互和展示搜索结果；
  - Visualize：用于图表的制作，图表分了很多类型，比如饼图、曲线图、线状图等。
  - Dashboard：整合了多个图表的面板，方便查看
  - TimeLion：Timelion是Kibana时间序列的可视化工具。时间序列可视化是可视化的，以时间顺序分析数据。
  - Dev Tools：主要用于操作es中的数据，可以对es中的数据和索引进行增删改查等操作；
  - Management：负责管理kibana，如对kibana索引的增删改等操作。
  - 

##### 02：创建索引

- Management -> Kibana - >  index Patterns -> create Index pattern;

##### 03：搜索数据

1. 选择时间，搜索框内数据Luceue 查询语法；
2. Add  filter 过滤一部分索引；
3. ![Kibana](/Users/likang/Code/Git/Middleware/Elasticsearch/photos/Kibana.jpg)

##### 04：Lucece 查询语法

- 字段:空格+值
- 通配符：使用* ，匹配一个或多个字符，模糊查询；
- ~ : 在一个单词后面加上该符号可以启用模糊搜获；
- 范围查询：
  - page: [2 TO 8] 
  - 搜索第2到第8页，包含两端点 page: [2 TO 8];
- 优先级查询：
  - 如果单词的匹配度很高，一个文档中或者一个字段中可以匹配多次，那么可以提升该词的相关度。使用符号`^`提高相关度；
  - Content: seq^2
- 逻辑操作：
  - `AND`：逻辑与，也可以用`&&`代替
  - `OR`：逻辑或，也可以使用`||`代替
  - `NOT`：逻辑非，也可以使用`!`代替
  - +：必须包含
  - -：不能包含
- 括号分组：
  - 可以使用小括号对子句进行分组，构造更复杂的查询逻辑；
  - Content: seq OR (11 AND 22)