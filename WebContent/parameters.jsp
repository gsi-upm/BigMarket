<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "simulation.Simulation" %>
<%@ page import= "simulation.Launcher" %> 
<%@ page import= "simulation.util.Neo4JManageTool" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Simulation parameters</title>
</head>
<body>
	<input type="button" name="StopButton" value="Stop">

	<input type="button" name="PauseButton" value="Pause">

	<input type="button" name="PlayButton" value="Play">

	<% 	int numberOfNodes = Integer.parseInt(request.getParameter("initialNodes"));
	String wannaGUI = request.getParameter("wannaGUI");
	String datasets = request.getParameter("datasetUsed");
	Simulation sim = new Simulation(System.currentTimeMillis());
	sim.setNumberOfNodes(numberOfNodes);

	if(datasets.equals("Yes")){
		sim.setFlag(0);
	}else{
		sim.setFlag(1);
	}
	

	%>
	
	<%
	
	Launcher launcher = new Launcher();
	if(wannaGUI.equals("Yes")){
		launcher.launchWithGUI(sim);
	}else{
		launcher.launchWithoutGUI(sim);
	}

	int numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
	System.out.println("NUMERO FINAL DE USERS " + sim.getUsers().size());
	Neo4JManageTool neoDB = new Neo4JManageTool(sim);
	neoDB.launchDatabaseTool();
	System.out.println("Program finished");
	%>
	

</body>
</html>