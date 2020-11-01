### Watcher

------

##### 01：添加watcher面板

![image-20200909154707657](/Users/likang/Code/Git/Middleware/Tool/photos/watcher-panel.png)

##### 02：Header面板

- 展示dashboard的标题，提供整体操作dashboard的按钮，提供查看个人信息和配置个人首选项的按钮
- 选取时间段数据、复制链接可以分享；

##### 03：watcher配置介绍

![watcher-graph](/Users/likang/Code/Git/Middleware/Tool/photos/watcher-graph.png)

- Graph里面的选项有：
  - **General**（常规选择）
    - Title：仪表板上的面板标题
    - Span：列在面板中的宽度
    - Height：面板内容高度(以像素为单位)
    - Drilldown / detail link：添加动态面板的链接，可以链接到其他仪表板或URL；
  - **Metrics**（指标）
    - 使用指标搜索，添加指标；
  - Axes（坐标轴）
    -  用于坐标轴和网格的显示方式，包括单位，比例，标签等；
    - Left Y和 Right Y可以定制左右Y轴；
  - **Legend**（图例）
    - Total:返回所有度量查询值的总和
    - Current:返回度量查询的最后一个值
    - Min:返回最小的度量查询值
    - Max:返回最大的度量查询值
    -  Avg:返回所有度量查询的平均值
  -  Display（显示样式）
    - 图表模式(Chart Options)
      - Bar:一个条形图显示值
      - Lines:显示线图值
      - Points:显示点值
    - 选择模式（Mode Options）
      - Fill:系列的颜色填充，0是没有；
      - Line Width:线的宽度；
    - Staircase:楼梯状显示
      - 如果有多个选择项,它们可以作为一个群体显示；
    - 叠加和空值（Stacking & Null value）
      - Stack：每个系列是叠在另一个之上
      - Null value：空值
      - 如果你启用了堆栈可以选择应该显示鼠标悬停功能。

##### 04：报警规则

![watcher-callPolice](/Users/likang/Code/Git/Middleware/Tool/photos/watcher-callPolice.png)

- 基本信息

  - 主要是对报警模板订级别，报警名称的说明；

- 报警规则

  ![报警规则](/Users/likang/Code/Git/Middleware/Tool/photos/报警规则.png)

  - x时间段内值都小于y就报警：5分钟之内值都小于10就报警
    - 5m<10;
  - x时间段内出现y次小于z就报警：2分钟之内出现1次大于10就报警
    - 1%2m>10;
  - 可以为不同的星期设置不同的报警规则；

##### 05：Function

1. combine（合并）
   - averageSeries(*seriesLists)：对seriesLists指定的所有metric取平均值；
   - isNonNull(seriesList)：用来看那些指标有数据（度量哪些指标还仍然在打数/alive）
   - maxSeries(*seriesLists)：在seriesLists指定的所有metric中，取最大值(每个点取最大值)
   - minSeries(*seriesLists)：在seriesLists指定的所有metric中，取最小值(每个点取最小值)
2. Transform（转换）
   - absolute(seriesList)：用abs函数取绝对值
   - derivative(seriesList)：用来算相邻两个点的差值，可以理解成变化率（斜率）；
   - integral(seriesList)：一直累加
   - timeShift(seriesList, timeShift,)：同比环比
3. Calculate （计算）
   - asPercent(seriesList, total=None)：百分比
4. Special（其他）
   - alias(seriesList, newName)：别名
   - aliasByMetric(seriesList)：取最后一级节点作为别名

##### 06：术语：

- datapoint：value+timestamp就可以理解成一个datapoint. 如果没有值，则为None(null)；
- function：对已存在的一个或多个指标(series)，用来做变换、聚合或其他运算；
- metric：同series、metric series.，用来标识指标名称，用元素点(.)分隔；
- target：一个target可以是一个单独的metric，也可以是一个或多个metric经过function运算之后的表达式；