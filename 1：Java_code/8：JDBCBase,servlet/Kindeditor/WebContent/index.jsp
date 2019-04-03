<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script charset="utf-8" src="editor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="editor/lang/zh-CN.js"></script>
<script>
        KindEditor.ready(function(K) {
                window.editor = K.create('#editor_id',{
                    uploadJson : 'upload',
                    fileManagerJson : '../jsp/file_manager_json.jsp',
                    allowFileManager : false
            });
        });
</script>
</head>
<body>
<center>
  <form action="welcome.jsp" method="post">
	      标题:<input type="text" name="title"/><br/>
	      内容:<textarea id="editor_id" name="content" style="width:700px;height:300px;"></textarea><br/>
	      <input type="submit" value="提交"/>
  </form>
</center>

</body>
</html>