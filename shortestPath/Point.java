/*
  Name: Carl Derline
  Assignment: lab 2
  Title: Shortest Path
  Course: CSCE 371
  Semester: Fall 2015
  Instructor: George Hauser
  Date: 11/13/15
  Sources consulted: textbook, Karen Bullinger
  Program description: This is the point class. It allows me to create points
  	with a given x and y coordinate, sort points by their x or y coordinates,
  	and provides a toString() method that prints points in readable format.
  Known Problems: In one case, points on the convex hull print out of order (see known
  	bugs section in the report)
  Creativity: none
 */

import java.text.DecimalFormat;
import java.util.Comparator;
/**
 * This is the point class. This class represents a point, which has an x and y coordinate.
 * The basic operations for these points are also included, such as getters and setters,
 * methods to sort by either the x or y coordinate, and a toString method to print points
 * in a readable format.
 */
public class Point {
	
	public Double x, y;
	DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * Constructor. Initializes the x and y coordinates of the point
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Setter for the x coordinate
	 * @param newX the new x coordinate
	 */
	public void setX(double newX)
	{
		x = newX;
	}
	
	/**
	 * Getter for the x coordinate
	 * @return the x coordinate
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Setter for the y coordinate
	 * @param newX the new y coordinate
	 */
	public void setY(double newY)
	{
		y = newY;
	}
	
	/**
	 * Getter for the y coordinate
	 * @return the y coordinate
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Sorts points in increasing order by their x coordinates.
	 */
	static Comparator<Point> compareX = new Comparator<Point>()
	{
		public int compare(Point p1, Point p2)
		{
			return p1.x.compareTo(p2.x);
		}
	};
	
	/**
	 * Sorts points by their y coordinates. Meant to be used after
	 * sorting the points by their x values. This creates a tie breaker
	 * for when the x coordinates are equal.
	 */
	static Comparator<Point> compareY = new Comparator<Point>()
	{
		public int compare(Point p1, Point p2)
		{
			if(p1.x.compareTo(p2.x) == 0)
				return p1.y.compareTo(p2.y);
			else
				return 0;
		}
	};
	
	/**
	 * toString. This method formats each point in an easy to read format.
	 */
	public String toString()
	{
		return "(" + df.format(this.x) + ", " + df.format(this.y) + ")";
	}

}
