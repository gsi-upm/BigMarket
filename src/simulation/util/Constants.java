/**
*
*
* This file is part of BigMarket.
*
* BigMarket has been developed by members of the research Group on
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes),
* acknowledged group by the Technical University of Madrid [UPM]
* (Universidad Politécnica de Madrid)
*
* Authors:
* Daniel Lara
* Carlos A. Iglesias
* Emilio Serrano
*
* Contact:
* http://www.gsi.dit.upm.es/;
*
*
*
* BigMarket is free software:
* you can redistribute it and/or modify it under the terms of the GNU
* General Public License as published by the Free Software Foundation,
* either version 3 of the License, or (at your option) any later version.
*
*
* BigMarket is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
*/


package simulation.util;

public class Constants {
	
	//Tipos usuarios
	public static final String USER_TYPE_BROADCASTER = "Broadcaster";
	public static final String USER_TYPE_ACQUAINTANCES = "Acquaintances";
	public static final String USER_TYPE_ODDUSERS = "Odd users";
	
	//Acciones simulacion
	public static final String RUN_ONE_STEP = "ros";
	public static final String RUN = "run";
	public static final String STOP = "stop";
	public static final String PAUSE = "pause";
	
	//Rutas guardado de grafos
	public static final String GRAPH_PATH = "C:/Users/dlaradie/git/BigMarket/WebContent/grafoInicial.gexf";
	public static final String GRAPH1_PATH = "C:/Users/dlaradie/git/BigMarket/WebContent/grafoPaso2.gexf";
	public static final String GRAPH_PRUEBA_PATH = "C:/Users/dlaradie/git/BigMarket/WebContent/grafoFinal.gexf";
	
	//Otras rutas
	public static final String JSON_GRAPH_PATH = "NetworkGraph.json";
	//C:\Users\dlaradie\PFC\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\BigMarket
	
	
	//Nombre de html y jsp
	public static final String INDEX_PAGE = "index.html";
	public static final String SETUP_PAGE = "setup.html";
	public static final String RUNNING_PAGE = "running.jsp";
	public static final String ACTIONS_PAGE = "actions.html";
	public static final String CONTACT_PAGE = "contact.html";
	
	//Otras constantes
	public static final String BUTTON_PRESSED = "bPress";
	public static final String FORM_NAME = "formName";
	
	public static final String STEPS = "steps";
	public static final String SIM = "sim";
	public static final String NODES = "nodes";
	public static final String BETWEENNESS = "betweenness";
	public static final String CLOSENESS = "closeness";
	
	//Acciones en el jsp de results
	//public static final String ACTION_SELECTED = "action";
	public static final String SEE_RESULTS = "results";
	public static final String SEE_NETWORK = "see";
	public static final String SAVE = "save";
	
	//Names formulario setup.html
	public static final String SETUP_FORM_NAME = "setupForm";
	public static final String RADIO_BUTTONS_NAME = "networks";
	public static final String RANDOM_SELECTED = "random";
	public static final String LOAD_SELECTED = "load";
	public static final String NUMBER_OF_NODES = "numNodes";
	public static final String RANDOM_NETWORK_NAME = "nameRandom";
	public static final String DATASET_IDENTIFIER = "datasetIdentifier";
	public static final String NEW_LOAD_NETWORK_NAME = "newLoadName";
	
	//Names formulario running.jsp
	public static final String RUNNING_FORM_NAME = "runningForm";
	public static final String ACTION_SELECTED = "actionSelected";
	

}
