/*
  Name: Carl Derline
  Assignment: lab 2
  Title: Shortest Path
  Course: CSCE 371
  Semester: Fall 2015
  Instructor: George Hauser
  Date: 11/13/15
  Sources consulted: textbook, Karen Bullinger
  Program description: This program calculates the shortest path from point A
  	to point B while avoiding going through the hull of the data set given. Uses
  	the quickhull algorithm to find the Convex hull of the data set.
  Known Problems: In one case, points on the convex hull print out of order (see known
  	bugs section in the report)
  Creativity: none
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
/**
 * The ShortestPath class contains all of the logic for calculating the convex hull of
 * a set of points as well as finding the shortest path between two points without going
 * through the convex hull of the original data. Also included are utility methods such as
 * totalDistance(), which calculates the total distance between the first and last points
 * of a list while going through all of the points in between, and checkPoint(), which returns
 * an integer based on the position of the third point with respect to the line made by the first
 * two passed in points.
 */
public class ShortestPath {

	final double THRESHOLD = 0.00000000001;
	DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * preQuickHull: This method takes in an ArrayList of points, and
	 * splits the points into two ArrayLists, one containing the points above
	 * the p1pn line, and the other containing the points below the p1pn line.
	 * Calls quickHull on both sub-lists.
	 * @param a an ArrayList of points
	 * @return result, the ArrayList containing the points that are part of the
	 * convex hull
	 */
	public ArrayList<Point> preQuickHull(ArrayList<Point> a)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();
		// base case. if the size of the ArrayList of points is less than 3,
		// all points are on the hull.
		if(a.size() < 3)
			return a;
		// create points p1 and pn, which will be the first and last elements
		// of the sorted list.
		Point p1 = a.get(0);
		Point pn = a.get(a.size()-1);
		// add points to upper or lower lists depending on the return value of checkPoint().
		for(int i = 1; i < a.size()-1; i++)
		{
			if(checkPoint(p1, pn, a.get(i)) == 1)
				upper.add(a.get(i));
			else if(checkPoint(p1, pn, a.get(i)) == -1)
				lower.add(a.get(i));
		}
		// call quickhull on each subArray, and append the returned lists to result.
		result.addAll(quickHull(upper, p1, pn));
		result.addAll(quickHull(lower, pn, p1));
		return result;
	}
	
	/**
	 * quickHull. This method is the implementation of the quick hull algorithm.
	 * @param a The ArrayList of points
	 * @param p1 the leftmost point
	 * @param pn the rightmost point
	 * @return recursive call on the sub-lists of points. 
	 */
	public ArrayList<Point> quickHull(ArrayList<Point> a, Point p1, Point pn)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		ArrayList<Point> s1 = new ArrayList<Point>();
		ArrayList<Point> s2 = new ArrayList<Point>();
		// base case. if the list is empty, p1 is the only point on the hull
		// in this iteration.
		if(a.isEmpty())
		{
			result.add(p1);
			return result;
		}

		// find point farthest away from the p1_pn line
		Point pMax = maxDistance(p1, pn, a);
		// put points to the left of the line p1_pMax into list s1.
		for(int i = 0; i < a.size(); i++)
		{
			if(checkPoint(p1, pMax, a.get(i)) == 1)
				s1.add(a.get(i));
		}
		// put points to the left of the line pMax_pn into list s2.
		for(int i = 0; i < a.size(); i++)
		{
			if(checkPoint(pMax, pn, a.get(i)) == 1)
				s2.add(a.get(i));
		}
		// avoids adding duplicates if p1 or pn happen to equal pMax
		if(!pn.equals(pMax))
			result.addAll(quickHull(s1, p1, pMax));
		if(!p1.equals(pMax))
			result.addAll(quickHull(s2, pMax, pn));
		return result;
	}
	
	/**
	 * shortestPath. This method calculates the shortest path from point A to
	 * point B.
	 * @param n the ArrayList containing the points of the Convex hull from the
	 * original set of points in the problem.
	 * @param a the starting point of the path.
	 * @param b the ending point of the path.
	 * @return a String containing each point in the shortest path from A to B,
	 * followed by the total distance from point A to B.
	 */
	public String shortestPath(ArrayList<Point> n, Point a, Point b)
	{
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();
		ArrayList<Point> resultA = new ArrayList<Point>();
		ArrayList<Point> resultB = new ArrayList<Point>();
		ArrayList<Point> result = new ArrayList<Point>();
		n.add(b);
		n.add(0, a);
		
		// p1 and pn are the points on the hull that are closest to
		// a and b, respectively.
		Point p1 = minDistance(n, a);
		Point pn = minDistance(n, b);
		double distA, distB, finalDist;
		String resultStr = "";
		// store points in either upper or lower, depending if the point is above
		// the p1_pn line or below the p1_pn line.
		for(int i = 0; i < n.size(); i++)
		{
			if(checkPoint(p1, pn, n.get(i)) == 1)
				upper.add(n.get(i));
			else if(checkPoint(p1, pn, n.get(i)) == -1)
				lower.add(n.get(i));
		}
		// resultA and resultB represent the two halves of the new hull containing points
		// a and b.
		resultA.addAll(quickHull(upper, p1, pn));
		resultB.addAll(quickHull(lower, pn, p1));
		
		// sort the points in resultB because the points get added to resultB in reverse order.
		// sort because I want to report the shortest path from left to right.
		Collections.sort(resultB, Point.compareX);
		Collections.sort(resultB, Point.compareY);
		// add b to resultA, and a at position 0 to resultB. They will initially be missing from
		// their respective lists after quickHull().
		resultA.add(b);
		resultB.add(0, a);

		// calculate total distance from a to b of both lists.
		distA = totalDistance(resultA);
		distB = totalDistance(resultB);

		// compare distances. report the one with the smaller total distance
		if(distA < distB)
		{
			result = resultA;
			finalDist = distA;
		}
		else if(distB < distA)
		{
			result = resultB;
			finalDist = distB;
		}
		else
		{
			// tie breaker. if the distances are the same, report the one with less points
			if((resultA.size() < resultB.size()) || (resultA.size() == resultB.size()))
			{
				result = resultA;
				finalDist = distA;
			}
			else
			{
				result = resultB;
				finalDist = distB;
			}
		}
		// construct the output String
		for(int i = 0; i < result.size(); i++)
		{
			if(i == 0)
				resultStr += "A: " + result.get(i).toString() + "\n";
			else if(i == result.size()-1)
				resultStr += "B: " + result.get(i).toString() + "\n";
			else
				resultStr += result.get(i).toString() + "\n";
		}
		return "PATH: \n" + resultStr + "Distance: " + df.format(finalDist) + "\n";
	}
	
	/**
	 * checkPoint. This method checks to see if point p3 is to the left or to
	 * the right of the line made by point p1 and p2.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @return 1 if p3 is on the left of the line, -1, if p3 is on the right of
	 * the line, and 0 if it is considered on the line according to our threshold.
	 */
	public int checkPoint(Point p1, Point p2, Point p3)
	{
		// equation given in the book
		double result = (p1.getX() * p2.getY()) + (p3.getX() * p1.getY()) + 
				(p2.getX() * p3.getY()) - (p3.getX() * p2.getY()) - 
				(p2.getX() * p1.getY()) - (p1.getX() * p3.getY());
		if(result > THRESHOLD)
			return 1;
		else if(result < THRESHOLD)
			return -1;
		else
			return 0;
	}
	
	/**
	 * maxDistance. This method looks for the point that is the maximum distance away from
	 * the line made by p1 and p2. This uses the same formula as checkPoint().
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 ArrayList of points. Check each of their distances to the line.
	 * @return the point that is the maximum distance away from the line made by 
	 * p1 and p2.
	 */
	public Point maxDistance(Point p1, Point p2, ArrayList<Point> p3)
	{
		double currPoint, max = 0;
		int index = -1;
		for(int i = 0; i < p3.size(); i++)
		{
			currPoint = Math.abs(p1.getX() * p2.getY() + p3.get(i).getX() * p1.getY() + 
					p2.getX() * p3.get(i).getY() - p3.get(i).getX() * p2.getY() - 
					p2.getX() * p1.getY() - p1.getX() * p3.get(i).getY());
			if(currPoint >= max)
			{
				// update max
				max = currPoint;
				index = i;
			}
		}
		return p3.get(index);
	}
	
	/**
	 * minDistance. Finds the point in the ArrayList a that is the closest
	 * to point p2.
	 * @param a ArrayList of points to search through.
	 * @param p2 the end point for the distance.
	 * @return the point in the ArrayList that is closest to p2.
	 */
	public Point minDistance(ArrayList<Point> a, Point p2)
	{
		Point p1;
		double min = Double.MAX_VALUE, x, y;
		int curr = 0;
		for(int i = 0; i < a.size(); i++)
		{
			// compute distance
			p1 = a.get(i);
			x = p2.getX() - p1.getX();
			x *= x;
			y = p2.getY() - p1.getY();
			y *= y;
			x += y;
			x = Math.sqrt(x);
			if(x < min)
			{
				// update min
				min = x;
				curr = i;
			}
		}
		return a.get(curr);
	}
	
	/**
	 * totalDistance. This method computes the cumulative distance from the first
	 * point to the end point in the ArrayList A.
	 * @param a The ArrayList of points
	 * @return The distance from the first element to the last element, going
	 * through each of the elements in between.
	 */
	public double totalDistance(ArrayList<Point> a)
	{
		double total = 0.0;
		double x, y;
		Point p1, p2;
		for(int i = 0; i < a.size()-1; i++)
		{
			// compute distance
			p1 = a.get(i);
			p2 = a.get(i+1);
			x = p2.getX() - p1.getX();
			x *= x;
			y = p2.getY() - p1.getY();
			y *= y;
			x += y;
			x = Math.sqrt(x);
			// append to total distance
			total += x;
		}
		return total;
	}
	
	
	/**
	 * distance: Calculates the distance between two points
	 * @param p1 point 1
	 * @param p2 point 2
	 * @return the distance between the two points
	 */
	public double distance(Point p1, Point p2)
	{
		// compute distance
		double x, y;
		x = p2.getX() - p1.getX();
		x *= x;
		y = p2.getY() - p1.getY();
		y *= y;
		x += y;
		x = Math.sqrt(x);
		return x;
	}
}
