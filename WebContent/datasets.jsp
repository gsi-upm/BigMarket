<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
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
        <%            
            File archivo = new File("/home/dlara/Gitted/WebContent/datasets");
    		FileReader fileReader = new FileReader(archivo);
    		BufferedReader buffReader = new BufferedReader(fileReader);
    		String linea = null;
    		
    		while((linea=buffReader.readLine()) != null){
    			out.print(linea);
    			out.print("<br>");
    		}
    		
    		if(null!=fileReader){
    			fileReader.close();
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