<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "simulation.Simulation" %>
<%@ page import= "simulation.Launcher" %> 
<%@ page import= "simulation.util.Neo4JManageTool" %>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Simulation parameters</title>
</head>
<body>
	<% 	
	int numberOfNodes = Integer.parseInt(request.getParameter("initialNodes"));
	String wannaGUI = request.getParameter("wannaGUI");
	String datasets = request.getParameter("datasetUsed");
	Simulation sim = new Simulation(System.currentTimeMillis());
	sim.setNumberOfNodes(numberOfNodes);

	if(datasets.equals("Yes")){
		sim.setFlag(0);
	}else{
		sim.setFlag(1);
	}
	
	Launcher launcher = new Launcher();
	launcher.launchWithGUI(sim);
	out.write("Steps: " + sim.schedule.getSteps());
	
	int numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
	System.out.println("NUMERO FINAL DE USERS " + sim.getUsers().size());
	Neo4JManageTool neoDB = new Neo4JManageTool(sim);
	neoDB.launchDatabaseTool();
	System.out.println("Program finished");
	%>
	

</body>
</html>