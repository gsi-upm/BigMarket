<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="org.apache.commons.httpclient.methods.GetMethod"%>
<%@page import="org.apache.commons.httpclient.Header"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonParser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    	<link rel="stylesheet" type="text/css" href="css/index.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BigTweet</title>
    </head>
    <body>
    <div id="BM">BigMarket</div><br>
    <div id="nav">
	<a href="http://localhost:8080/BigMarket/index.html"><button type="button">Home</button></a>
	<a href="http://localhost:8080/BigMarket/parameters.jsp"><button type="button">Parameters</button></a>
	<a href="http://localhost:8080/BigMarket/results.jsp"><button type="button">Results</button></a>
	<a href="http://localhost:8080/BigMarket/show.jsp"><button type="button">Show</button></a>
	
	</div>
        <%            
        try{
			String res = "";
			String nodePointUrl = "http://localhost:7474/db/data/labels/";
			HttpClient client = new HttpClient();
			GetMethod mGet = new GetMethod(nodePointUrl);
	


			Header mtHeader = new Header();
			mtHeader.setName("accept");
			mtHeader.setValue("application/json");
			mGet.addRequestHeader(mtHeader);

			client.executeMethod(mGet);
			res = mGet.getResponseBodyAsString();		
											
			JsonArray root = (JsonArray) new JsonParser().parse(res);
			
			for(JsonElement j : root){
				out.write(j.toString().substring(1, j.toString().length()-1));
				out.write("<br>");
			}

		}catch(Exception e){
			System.out.println("Exception in creating node in neo4j : " + e);
		}	
        %>
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