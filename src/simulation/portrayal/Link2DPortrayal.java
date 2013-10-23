package simulation.portrayal;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.network.EdgeDrawInfo2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;


/**
 * 
 * This class represents the graphical representation of a link in the 2D
 * display.
 * 
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public class Link2DPortrayal extends SimpleEdgePortrayal2D{

	private static final long serialVersionUID = 3056164119756999594L;
	
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

		EdgeDrawInfo2D ei = (EdgeDrawInfo2D) info;

		final int startX = (int) ei.draw.x;
		final int startY = (int) ei.draw.y;
		final int endX = (int) ei.secondPoint.x;
		final int endY = (int) ei.secondPoint.y;

		graphics.setColor(Color.BLUE);
		graphics.drawLine(startX, startY, endX, endY);
	}

}
