<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "simulation.Simulation" %>
<%@ page import= "simulation.Launcher" %> 
<%@ page import= "simulation.util.Neo4JManageTool" %> 

<!DOCTYPE html>
<html>
<head>
	<script>
	function clickROS(){
		document.actions.bPress.value="ros";
	}
	
	function clickRun(){
		document.actions.bPress.value="run";
	}
	
	function clickPause(){
		document.actions.bPress.value="pause";
	}
	
	function clickStop(){
		document.actions.bPress.value="stop";
	}
	
	</script>
	<meta charset=UTF-8">	
	<link rel="stylesheet" type="text/css" href="css/running.css">	
	<title>BigMarket</title>
</head>
<body>
	<%
	Integer broadUsers = (Integer)request.getAttribute("broadUsers");
	Integer aqUsers = (Integer)request.getAttribute("aqUsers");
	Integer oddUsers = (Integer)request.getAttribute("oddUsers");
	Integer oddTweets = (Integer)request.getAttribute("oddTweets");
	Integer acqTweets = (Integer) request.getAttribute("acqTweets");
	Integer broadTweets = (Integer) request.getAttribute("broadTweets");
	Long steps = (Long)request.getAttribute("steps");
	Simulation sim = (Simulation)request.getAttribute("sim");
	
	%>
	
		
	<div id="BM">BigMarket</div><br>
	<div id="nav">
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/index.html"><button type="button">Home</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/parameters.jsp"><button type="button">Parameters</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/results.jsp"><button type="button">Results</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/show.jsp"><button type="button">Show</button></a>
	
	</div>
	<div id="steps">
	Time step: <%=steps%>
	</div>

	<div id="table">
	<table width="70%" cellspacing="5" cellpadding="0">
  		<tr>
    		<th></th>
    		<th><ins>Numbers of users</ins></th>
    		<th><ins>Tweets Generated</ins></th>
  		</tr>
  		<tr>
   		 	<th>Broadcaster</th>
    		<th><%=broadUsers%></th>
    		<th><%=broadTweets %></th>
  		</tr>
  		<tr>
  			<th>Aquaintances</th>
  			<th><%=aqUsers%></th>
  			<th><%=acqTweets %></th>
  		</tr>
  		<tr>
  			<th>Odd Users</th>
  			<th><%=oddUsers%></th>
  			<th><%=oddTweets %></th>
  		</tr>

	</table> 
	</div>

	<div id="buttons">
		<form action="BigMarketServlet" method="POST" name="actions">
			<input id="b1" type="submit" value="Run one step" onclick="clickROS()">
			<input id="b2" type="submit" value="Run" onclick="clickRun()">
			<input id="b3" type="submit" value="Pause" onclick="clickPause()">
			<input id="b4" type="submit" value="Stop" onclick="clickStop()">
			<input name="bPress" type="hidden" value="default">	
	
						
		</form>
	</div>
	
	<div id="footer">
	<hr>
		<ul>
  			<li>Daniel Lara Diezma Grupo de Sistemas Inteligentes.</li>
  			<li>Emilio Serrano Fernández Grupo de Sistemas Inteligentes.</li>
  			<li>Carlos Ángel Iglesias Fernández Grupo de Sistemas Inteligentes.</li>
		</ul> 		
	</div>
</body>
</html>