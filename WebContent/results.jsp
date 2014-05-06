<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function clickSee(){
		document.finalForm.action.value="see";
	}

	function clickExport(){
		document.finalForm.action.value="export";
	}
	
	function clickResults(){
		document.finalForm.action.value="results";
	}
	
	function clickSave(){
		document.finalForm.action.value="save";
	}
	
</script>
<link rel="stylesheet" type="text/css" href="css/index.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BigMarket</title>
</head>
<body>
<div id="BM">BigMarket</div><br>

<div id="nav">
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/index.html"><button type="button">Home</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/parameters.jsp"><button type="button">Parameters</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/results.jsp"><button type="button">Results</button></a>
	<a href="http://simplez.gsi.dit.upm.es:8080/BigMarket/show.jsp"><button type="button">Show</button></a>
	
</div>
<div id="results">
Select which result would you want to see
</div>

<form name="finalForm" action="BigMarketServlet" method="POST">
<input type="checkbox" name="resu" value="betweenness">Betweenness<br>
<input type="checkbox" name="resu" value="closeness">Closeness<br> 
<input type="submit" name="results" value="See results" onclick="clickResults()">
<input type="submit" name="see" value="See network" onclick="clickSee()">
<input type="submit" name="export" value="Export network" onclick="clickExport()">
<input type="submit" name="save" value="Save in DB" onclick="clickSave()">
<input type="hidden" name="action" value="default">

</form>

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