### KindEditor：

### 是一套开源的在线HTML编辑器，主要用于让用户在网站上获得所见即所得编辑效果，开发人员可以用 KindEditor ，把传统的多行文本输入框(textarea)替换为可视化的文本输入框，使用js 编写

URL：http://kindeditor.net

```javascript
使用方法：（Jsp）
1：先部署KindEditor文件到---Web-Content

2：<textarea id="editor_id" name="content" style="width:700px;height:300px;">
</textarea>

3：<script charset="utf-8" src="/editor/kindeditor.js"></script>

<script charset="utf-8" src="/editor/lang/zh-CN.js"></script>
<script>
KindEditor.ready(function(K) {
window.editor = K.create('#editor_id');
});
</script>
```



##### 4：取得HTML的内容

?	html = $('#editor_id').val(); // jQuery
   设置HTML内容
?	editor.html('HTML内容');


注意： K.create函数的第二个参数，可以对编辑器进行配置
	
1：cssPath
	指定编辑器iframe document的CSS文件，用于设置可视化区域的样式
2：uploadJson
	指定上传文件的服务器端程序
3：allowFileManager
	true时显示浏览远程服务器按钮
4：fileManagerJson
	指定浏览远程图片的服务器端程序

```js
例：KindEditor.ready(function(K) {
                window.editor = K.create('#${mis.field}_id',{			
                uploadJson : 'admin/uploadpic/imgupload',
    		allowFileManager : false

	       });
});
```



