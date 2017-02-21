
/*
 * A class that overrides the paintcomponent method of the JPanel class
 * in order to draw polygons on the panel.
 * Polygons are taken from the display class.
 * 
 * February 5, 2014
 * By: Mark Guan
 */

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PolygonDrawerPanel extends JPanel {

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (Display.getPolygons().size() > 0) {
			for (Polygon shape : Display.getPolygons()) {
				// setting the color
				g.setColor(shape.getColor());
				// Setting a fixed radius
				radiusSetter(shape);
				// Drawing axis with radius of 5 pixels
				g.fillOval((int) (shape.getAxis().getX() - 5), (int) (shape.getAxis().getY() - 5),
						5 * 2, 5 * 2);
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(shape.getLineThickness()));
				// getting the points of the Polygon
				ArrayList<Point> points = shape.getPoints();
				// Painting lines between the points
				int i = 0;
				int j = 1;
				for (; j < shape.getNumberOfSides(); i++, j++) {
					g2.drawLine(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y);
				}
				j--;
				g.drawLine(points.get(j).x, points.get(j).y, points.get(0).x, points.get(0).y);
			}
		}
	}

	// method that sets the radius of the polygon to go to the nearest side of
	// the panel
	public void radiusSetter(Polygon shape) {
		Point axis = shape.getAxis();
		double limX;
		double limY;
		if (axis.x < getWidth() - axis.x) {
			limX = axis.x - 1;
		} else {
			limX = getWidth() - axis.x;
		}
		if (axis.y < getHeight() - axis.y) {
			limY = axis.y - 1;
		} else {
			limY = getHeight() - axis.y - 1;
		}
		if (limX > limY) {
			shape.setRadius((int) limY);
		} else {
			shape.setRadius((int) limX);
		}
	}
}
