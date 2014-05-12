<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/index.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
#tableBet{
float:left;
padding-right: 5%;
}

#tableClose{

}

</style>

<title>Big Market</title>
</head>
<div id="BM">BigMarket</div><br>
<div id="nav">
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/index.html"><button type="button">Home</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/parameters.jsp"><button type="button">Parameters</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/results.jsp"><button type="button">Results</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/show.jsp"><button type="button">Show</button></a>
	
</div>
<body>
	<%
	
	
	Double[] close = (Double[]) request.getAttribute("close");
	

	
	%>
	<div id="tableBet">
	<table width="20%" cellspacing="5" cellpadding="0">
  		<tr>
    		<th>Betweeness</th>
  		</tr>
  		<tr>
  			<th>Node 0</th>
  			<th>11.0</th>
  		</tr>
  		<tr>
  			<th>Node 1</th>
  			<th>2.0</th>
  		</tr>
  		<tr>
  			<th>Others</th>
  			<th>0.0</th>
  		</tr>
  	</table>
  	</div>
  	
  	<div id="tableClose">
  	<table width="20%" cellspacing="5" cellpadding="0">
  		<tr>
    		<th>Closeness</th>
  		</tr>
  		<tr>
  			<th>1.5</th>
  			<th>0 1 3 5 6 7 8 10 11</th>
  		</tr>
  		<tr>
  			<th>1.0</th>
  			<th>2 4 9 12</th>
  		</tr>
  		<tr>
  			<th>0.0</th>
  			<th>Others</th>
  		</tr>
  	</table>
  	</div>
  
	
</body>
</html>