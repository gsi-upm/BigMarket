<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/index.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
	
	List<Double> bet = (List<Double>) request.getAttribute("bet");
	List<Integer> betNodes = (List<Integer>) request.getAttribute("betnodes");
	
	for(int i = 0; i<bet.size(); i++){
		out.print(bet.get(i));
		out.print(betNodes.get(i));
	}
	
	%>
</body>
</html>