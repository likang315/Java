### JS-SDK

------

##### 01：监听朋友圈分享事件

- 基于微信内的网页开发工具包，通过使用微信JS-SDK，网页开发者可借助微信高效地使用拍照、选图、语音、位置等手机系统的能力，同时可以直接使用微信分享、扫一扫、卡券、支付等微信特有的能力，为微信用户提供更优质的网页体验。
- 一个抽奖活动，抽奖次数有限，若次数使用完便不能再次参与抽奖，但是用户分享到朋友圈之后，可增加一次抽奖机会，这样便达到了宣传的效果。

##### 02：实现步骤

1. ###### 绑定域名

   - 在公众号管理页面，**设置JS接口安全域名**，表示该域名下的所有页面，都拥有使用JSSDK的权限；

2. ###### 引入JS文件

   - 在需要调用JS接口的页面引入如下JS文件，（支持https）：http://res.wx.qq.com/open/js/jweixin-1.6.0.js；

3. ###### 通过config接口注入权限验证配置

   1. 所有需要使用JS-SDK的页面必须先注入配置信息，否则将无法调用；

      ```js
      wx.config({
        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: '', // 必填，公众号的唯一标识
        timestamp: , // 必填，生成签名的自定义时间戳
        nonceStr: '', // 必填，生成签名的自定义随机串
        signature: '',// 必填，签名
        jsApiList: ['updateTimelineShareData', 'openLocation'] // 必填，需要使用的JS接口列表
      });
      ```

   2. 生成签名

      - jsapi_ticket是公众号用于调用微信JS接口的临时票据；

      - 拿到的access_token后：采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）：https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi

        ```java
        //获取JSSDK的接口地址
        public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        
        /**
        * 获取JSSDK的jsapi_ticket
        */
        public static void getJsapi_ticket(){
          //发起请求到指定的接口
          String result = HttpUtil.get(GET_TICKET_URL.replace(
            "ACCESS_TOKEN", getAccessToken()));
          System.out.println(result);
        }
        ```

      - **签名规则如下**：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分） 。对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1。这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。

      - 验证签名是否正确工具：https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=jsapisign

        ```java
        /**
        * 计算jssdk-config的签名
        * @param jsapi_ticket
        * @param timestamp
        * @param noncestr
        * @param url
        * @return
        */
        public static String getSignature(String jsapi_ticket,Long timestamp,String noncestr,String url ){
          // 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）
          Map<String,Object> map = new TreeMap<>();
          map.put("jsapi_ticket",jsapi_ticket);
          map.put("timestamp",timestamp);
          map.put("noncestr",noncestr);
          map.put("url",url);
          // 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1
          StringBuilder sb = new StringBuilder();
          Set<String> set = map.keySet();
          for (String key : set) {
            sb.append(key + "=" + map.get(key)).append("&");
          }
          // 去掉最后一个&符号
          String temp = sb.substring(0,sb.length()-1);
          // 使用sha1加密
          String signature = SecurityUtil.SHA1(temp);
          return signature;
        }
        ```

   3. config权限验证成功后会**执行ready方法，相反失败会执行error方法**，所有接口调用都必须在config接口获得结果之后，因为config是一个异步操作；

      ```js
      wx.ready(function(){
        // 自定义分享到朋友圈内容接口
        wx.updateTimelineShareData({
          title: '抽奖活动', // 分享时的标题
          link: 'http://huihui.mynatapp.cc/gift.html', // 分享时的链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
          imgUrl: 'http://www.wolfcode.cn/img/wolfcode/logo.png', // 分享时显示的图标
          // 用户确认分享后执行的回调函数
          success: function () {
            // 给用户添加1次抽奖机会
            playnum = 1;
            $('.playnum').html(playnum);
          },
          // 用户取消分享后执行的回调函数
          cancel: function () {
            alert("取消分享");
          }
        });
      });
      
      wx.error(function(res){
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
      });
      ```

   ##### 03：获取用户地理位置，打开微信内置地图
   
   1. 微信公众号底部【校区位置】菜单，打开应用页面，该页面中调用微信JSSDK打开微信内置地图，并设置目标地点，此时可看到用户当前位置与目标地点位置的距离，还可通过右下角绿色按钮，调用百度地图等第三方软件，点击后会自动设置用户的位置作为起点，目标地点作为终点，自动查询方案路线。
   
   2. 引入JS文件，配置config接口权限验证
   
   3. 通过ready接口处理成功验证
   
      ```js
      wx.ready(function(){
      	wx.openLocation({
      		latitude:23.132006, // 纬度，浮点数，范围为90 ~ -90
      		longitude:113.377785, // 经度，浮点数，范围为180 ~ -180。
      		name: 'qunar', // 位置名
      		address: '北京海淀', // 地址详情说明
      		scale: 25, // 地图缩放级别,整形值,范围从1~28。默认为最大
      		infoUrl: 'http://www.qunar.cn/' // 在查看位置界面底部显示的超链接,可点击跳转
      	});
      });
      ```