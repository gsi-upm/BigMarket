<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "simulation.Simulation" %>
<%@ page import= "simulation.Launcher" %> 
<%@ page import= "simulation.util.Neo4JManageTool" %> 
<%@ page import= "simulation.util.Constants" %>
<%@ page import="java.util.SortedMap" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <script src="js/bootstrap.js"></script>
    <script src="http://code.jquery.com/jquery.js"></script>
    <title>BigMarket</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/actions.css" rel="stylesheet">

  </head>

  <body>
  <%
      Double[] bet = (Double[])request.getAttribute("bet");
      int[] betnodes = (int[])request.getAttribute("betnodes");
      SortedMap<Integer, Double> close = (SortedMap<Integer, Double>)request.getAttribute("close");
      
    %>
    <div class="container">

      <div class="masthead">
        <img src="img/logo2.png" height="70px">
        <img src="img/logo_gsi_final.svg" class="pull-right" width=6% float="right">
        <nav>
          <ul class="nav nav-justified">
            <li><a href="index.html">About BigMarket</a></li>
            <li><a href="setup.html">Set up Simulation</a></li>
            <li><a href="running.jsp">Running</a></li>
            <li class="active"><a href="actions.html">Actions</a></li>
            <li><a href="contact.html">Contact</a></li>
          </ul>
        </nav>
      </div>


      <div class="row">
        <div class="col-lg-4" id="col-left">
          <div class="btn-group">
            <button type="button" class="btn btn-primary">SNA type</button>
            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only">Dropdown menu</span>
            </button>
            <ul class="dropdown-menu" role="menu">
              <li>Betweeness</li>
              <li>Closeness</li>
              <li>Grado Entrada</a></li>
              <li>Grado Salida</li>
            </ul>
          </div>




          <table class="table table-striped" id="scroll">
              <tr>
                <th>Node</th>
                <th>Meassure</th>
              </tr>
              <%
                int i=0;
                for(i=0;i<betnodes.length;i++){
                out.print("<tr>");
                out.print("<td>"+betnodes[i]+"</td>");
                out.print("<td>"+bet[i]+"</td>");
                out.print("</tr>");
                }
              %>
          </table>
        </div>
        <div class="col-lg-4" id="col-right">
          <h4>See the graph of the network</h4>
          <p><input type="button" class="btn btn-primary" value="See network" id="seeNetworkButton" onclick="seeNetwork()"></p>
          <h4>Export the graph in a .gexf file</h4>
          <a class="btn btn-lg btn-primary" width="15px" href="grafoInicial.gexf" download="grafoInicial.gexf" role="button">Download graph</a>
        </div>
      </div>

      <!-- Site footer -->
      <footer class="footer">
        <p>&copy;Grupo de Sistemas Inteligentes GSI-UPM </p>
      </footer>

    </div>
    <script src="js/comportamiento.botones.actions.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    
    
  </body>
</html>