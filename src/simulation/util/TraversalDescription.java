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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TraversalDescription {

	public static final String DEPTH_FIRST = "depth first";
    public static final String NODE = "node";
    public static final String ALL = "all";
 
    private String uniqueness = NODE;
    private int maxDepth = 1;
    private String returnFilter = ALL;
    private String order = DEPTH_FIRST;
    private List<Relationship> relationships = new ArrayList<Relationship>();
 
    public void setOrder(String order) {
        this.order = order;
    }
 
    public void setUniqueness(String uniqueness) {
        this.uniqueness = uniqueness;
    }
 
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
 
    public void setReturnFilter(String returnFilter) {
        this.returnFilter = returnFilter;
    }
 
    public void setRelationships(Relationship relationships) {
        this.relationships =  Arrays.asList(relationships);
    }
 
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(" \"order\" : \"" + order + "\"");
        sb.append(", ");
        sb.append(" \"uniqueness\" : \"" + uniqueness + "\"");
        sb.append(", ");
        if (relationships.size() > 0) {
            sb.append("\"relationships\" : [");
            for (int i = 0; i < relationships.size(); i++) {
                sb.append(relationships.get(i).toJsonCollection());
                if (i < relationships.size() - 1) { // Miss off the final comma
                    sb.append(", ");
                }
            }
            sb.append("], ");
        }
        sb.append("\"return filter\" : { ");
        sb.append("\"language\" : \"builtin\", ");
        sb.append("\"name\" : \"");
        sb.append(returnFilter);
        sb.append("\" }, ");
        sb.append("\"max depth\" : ");
        sb.append(maxDepth);
        sb.append(" }");
        return sb.toString();
    }
}
