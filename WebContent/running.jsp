<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "simulation.Simulation" %>
<%@ page import= "simulation.Launcher" %> 
<%@ page import= "simulation.util.Neo4JManageTool" %> 
<%@ page import= "simulation.util.Constants" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>BigMarket</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/running.css" rel="stylesheet">

  </head>

  <body>
    <%
      Integer broadUsers = (Integer)request.getAttribute("broadUsers");
      Integer acqUsers = (Integer)request.getAttribute("aqUsers");
      Integer oddUsers = (Integer)request.getAttribute("oddUsers");
      Integer oddTweets = (Integer)request.getAttribute("oddTweets");
      Integer acqTweets = (Integer) request.getAttribute("acqTweets");
      Integer broadTweets = (Integer) request.getAttribute("broadTweets");
      Long steps = (Long)request.getAttribute("steps");
      Simulation sim = (Simulation)request.getAttribute("sim");
    %>
    <div class="container">

      <div class="masthead">
        <img src="img/logo2.png" height="70px">
        <img src="img/logo_gsi_final.svg" class="pull-right" width=6% float="right">
        <nav>
          <ul class="nav nav-justified">
            <li><a href="index.html">About BigMarket</a></li>
            <li><a href="setup.html">Set up Simulation</a></li>
            <li class="active"><a href="running.jsp">Running</a></li>
            <li><a href="actions.html">Actions</a></li>
            <li><a href="contact.html">Contact</a></li>
          </ul>
        </nav>
      </div>

      <!-- Jumbotron -->
      <div class="jumbotron">
        <table class="table table-striped" id="tableA">
          <tr>
            <th>Time step</th>
            <td><%=steps%></td>
          </tr>
      </div>

      <div class="row">
        <table class="table table-striped" id="tableB">
          <tr>
            <th>User type</th>
            <th>Number of users</th>
            <th>Number of tweets writed</th>
          </tr>
          <tr>
            <th>Broadcaster</td>
            <td><%=broadUsers%></td>
            <td><%=broadTweets%></td>
          </tr>
          <tr>
            <th>Acquaintances</td>
            <td><%=acqUsers%></td>
            <td><%=acqTweets%></td>
          </tr>
          <tr>
            <th>Odd users</td>
            <td><%=oddUsers%></td>
            <td><%=oddTweets%></td>
          </tr>
        </table>
      </div>

      <div class="action-buttons">
        <form action="BigMarketServlet" method="POST" name="running">
          <input type="hidden" name="formName" value="runningForm"/>
          <input type="submit" class="btn btn-primary" value="Run one step" id="runOneStepButton" onCLick="clickROS()">
          <input type="submit" class="btn btn-primary" value="Run" id="runButton" onClick="clickRun()">
          <input type="submit" class="btn btn-primary" value="Pause" id="pauseButton" onClick="clickPause()">
          <input type="submit" class="btn btn-primary" value="Stop" id="StopButton" onClick="clickStop()">
          <input name="actionSelected" type="hidden" value="default" id="actionSelected"> 
        </form>
      </div>
      <!-- Site footer -->
      <footer class="footer">
        <p>&copy;Grupo de Sistemas Inteligentes GSI-UPM </p>
      </footer>

    </div>
    <script src="js/comportamiento.botones.running.js"></script>
  </body>
</html>