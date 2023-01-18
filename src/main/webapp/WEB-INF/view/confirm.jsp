<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="dto.Kadai16Account" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>下記の内容で登録します。よろしいですか？</p>
	<%
		Kadai16Account account = (Kadai16Account)session.getAttribute("input_data");
	%>
	名前：<%=account.getName() %><br>
	年齢：<%=account.getAge() %><br>
	性別：<%=account.getGender() %><br>
	電話番号：<%=account.getTel() %><br>
	メール：<%=account.getMail() %><br>
	パスワード：**************<br>
	<A href="Kadai16RegisterExecuteServlet">OK</A><br>
	<a href="Kadai16RegisterFormServlet">戻る</a>
</body>
</html>