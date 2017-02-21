
/*
 * The Purpose of this class is to represent a polygon which can be painted
 * in a JPanel and then rotated.
 * 
 * February 5, 2014
 * By: Mark Guan, Thomas Russell
 */

import java.awt.*;
import java.util.ArrayList;

public class Polygon {

	private int numberOfSides;
	private Color color;
	private Point axis;
	private Point initialPoint;
	private int lineThickness;
	private int radius;
	private ArrayList<Point> points = new ArrayList<Point>();
	private double thetaIncrement;
	private double theta;

	// Constructor
	// takes in the color of the Polygon sides, the number of sides, and its
	// axis that it is rotating around initial point is the point vertical from
	// the axis will calculate the rest of the points based off initial points
	// and the interior angles is not centered line thickness is defaulted to 5
	// radius is calculated in Display class
	public Polygon(Color color, int numSides, Point axis) {
		this.color = color;
		numberOfSides = numSides;
		this.axis = axis;
		initialPoint = new Point((int) axis.getX(), (int) axis.getY() + radius);
		points.add(initialPoint);
		for (int i = 0; i < numSides - 1; i++) {
			points.add(rotate(points.get(i), true, 360 / numSides));
		}
		lineThickness = 5;
		thetaIncrement= 0.01;
	}

	// constructor
	// takes in number of sides, and its axis that it is rotating around
	// will default that the color is black and delegate the work to the other
	// constructor
	public Polygon(int numSides, Point axis) {
		this(Color.BLACK, numSides, axis);
	}

	// constructor
	// takes in the color, number of sides, and assume axis is center of JPanel
	// will delegate the work to the other constructor
	public Polygon(Color color, int numSides) {
		this(color, numSides, new Point(0, 0));
	}

	// constructor
	// takes, number of sides, whether or not it will spin clockwise, and
	// assume axis is center of JPanel
	// will delegate the work to the other constructor assuming color is black

	// returns the initial point
	public Point getPoint() {
		return initialPoint;
	}

	// sets the initial point given a new point
	public void setPoint(Point newPoint) {
		this.initialPoint = newPoint;
	}

	// will rotate every point in the ArrayList points, one degree in the given
	// direction
	// It then sets the new ArrayList of rotated points to the class data
	// ArrayList points.
	public void rotateArray(boolean direction) {
		ArrayList<Point> rotatedPoints = new ArrayList<Point>();
		for (int i = 0; i < points.size(); i++) {
			rotatedPoints.add(rotate(points.get(i), direction, 1));
		}
		points = rotatedPoints;
	}

	// will rotate a point by the given degree in the given direction based off
	// the radius of the Polygon
	// To rotate, the entered point is converted to polar (by the helper
	// method), then the theta coordinate
	// is incremented. It then converts the point back into cartesian so it can
	// be used to create a Polygon. 
	// will return the new point
	public Point rotate(Point initialPoint, boolean direction, int degreeOfRotation) {
		thetaIncrement = (Math.PI * degreeOfRotation) / 180;

		convertToPolar(initialPoint);
		if (direction == true) // clockwise
		{
			theta += thetaIncrement;
			return convertToCartesian(radius, theta);
		}

		else // counter clockwise
		{
			theta -= thetaIncrement;
			return convertToCartesian(radius, theta);
		}

	}

	// will set theta given a Cartesian point
	// It takes a given point, pretends that point is the origin (by
	// subtracting the axis), then
	// calculates theta by taking the arcTan of the point. The radius stays the
	// same as the
	// radius that we calculated when constructing the polygon, so this method
	// only sets
	// theta.
	public void convertToPolar(Point point) {
		double x = point.getX();
		double y = point.getY();
		double newX = x - (int) axis.getX();
		double newY = y - (int) axis.getY();
		theta = Math.atan2(newY, newX);
		// radius = the same as this.radius
	}

	// will take a polar coordinate and return a Cartesian coordinate
	// It takes a given radius and theta, does radius * cos(theta) to get an X
	// value that has an
	// axis on the origin then adds the polygon's real axis.
	// For the y value, it does radius * sin(theta) then adds the y component
	// of the axis.
	// It returns the values as a Point object.
	public Point convertToCartesian(double radius, double theta) {
		int x = (int) Math.round(((radius * Math.cos(theta) + axis.getX())));
		int y = (int) Math.round(((radius * Math.sin(theta) + axis.getY())));
		return new Point(x, y);
	}

	// returns the number of sides
	public int getNumberOfSides() {
		return numberOfSides;
	}

	// returns the color
	public Color getColor() {
		return color;
	}

	// sets the color
	public void setColor(Color color) {
		this.color = color;
	}

	// returns the axis
	public Point getAxis() {
		return axis;
	}

	// Sets the axis - because the middle is different, all the other points in
	// the polygon need to be reset.
	public void setAxis(Point axis) {
		ArrayList<Point> newPoints = new ArrayList<Point>();
		this.axis = axis;
		initialPoint = points.get(0);
		newPoints.add(initialPoint);
		for (int i = 0; i < numberOfSides - 1; i++) {
			newPoints.add(rotate(points.get(i), true, 360 / numberOfSides));
		}
		points = newPoints;
	}

	// returns the line thickness of the Polygon
	public int getLineThickness() {
		return lineThickness;
	}

	// void, sets the line thickness of the Polygon
	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}

	// returns the radius
	public int getRadius() {
		return radius;
	}

	// sets the Radius and sets all the points
	public void setRadius(int radius) {
		this.radius = radius;
		rotateArray(true);
		rotateArray(false);
	}

	// returns the points of the Polygon
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public String toString(){
		return this.getNumberOfSides() + "-sided polygon";
	}

}
