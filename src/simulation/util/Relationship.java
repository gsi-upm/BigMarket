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

public class Relationship {
	
	public static final String OUT = "out";
    public static final String IN = "in";
    public static final String BOTH = "both";
    private String type;
    private String direction;
 
    public String toJsonCollection() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(" \"type\" : \"" + type + "\"");
        if(direction != null) {
            sb.append(", \"direction\" : \"\" + direction + \"\"");
        }
        sb.append(" }");
        return sb.toString();
    }
 
    public Relationship(String type, String direction) {
        setType(type);
        setDirection(direction);
    }
 
    public Relationship(String type) {
        this(type, null);
    }
 
    public void setType(String type) {
        this.type = type;
    }
 
    public void setDirection(String direction) {
    }

}
