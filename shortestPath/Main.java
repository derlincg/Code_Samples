/*
  Name: Carl Derline
  Assignment: lab 2
  Title: Shortest Path
  Course: CSCE 371
  Semester: Fall 2015
  Instructor: George Hauser
  Date: 11/13/15
  Sources consulted: textbook, Karen Bullinger
  Program description: This is the main method of my program. It reads a file called
  	"pairs.txt", then it iterates through the file and calculates the
  	convex hull and shortest path from A to B from the data given in the file.
  Known Problems: In one case, points on the convex hull print out of order (see known
  	bugs section in the report)
  Creativity: none
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/**
 * This class is the runnable part of the program. It reads the input file "pairs.txt"
 * and performs quickHull() and shortestPath() on all of the data given in the file.
 */
public class Main {

	/**
	 * Main. This method creates a new ShortestPath object, then reads the file
	 * the user specifies. For each problem contained in the file, this method
	 * computes the Convex hull of the original data set using the quickhull
	 * algorithm, and then computes the shortest path between the given A and B
	 * points.
	 * 
	 * @param args. NA, No command line arguments are given.
	 */
	public static void main(String[] args) {
		ShortestPath sp = new ShortestPath();
		File in;
		int nProbs, np, count = 1;
		double xA, yA, xB, yB;
		Point a, b, currentP;
		ArrayList<Point> n = new ArrayList<Point>();
		ArrayList<Point> temp = new ArrayList<Point>();
		Scanner reader;
		in = new File("pairs.txt");
		System.out.println();
		
		try {
			reader = new Scanner(in);
			nProbs = reader.nextInt(); // loop counter: first number in file
			for (int i = 0; i < nProbs; i++) {
				xA = reader.nextDouble();
				yA = reader.nextDouble();
				xB = reader.nextDouble();
				yB = reader.nextDouble();
				a = new Point(xA, yA); // create points A and B for ShortestPath
				b = new Point(xB, yB);
				np = reader.nextInt();
				// get all points for the current problem
				for (int j = 0; j < np; j++) {
					currentP = new Point(reader.nextDouble(), reader.nextDouble());
					n.add(currentP);
				}
				Collections.sort(n, Point.compareX);
				Collections.sort(n, Point.compareY);
				System.out.println("Problem #" + count);
				System.out.println("Convex Hull:");
				// Compute and print the convex hull of the original data.
				temp = sp.preQuickHull(n);
				for (int j = 0; j < temp.size(); j++)
					System.out.println(temp.get(j));
				// Compute and print the shortest path from A to B.
				System.out.println(sp.shortestPath(temp, a, b));
				count++;
				n.clear();
				temp.clear();
			}
		} catch (FileNotFoundException e) {
			System.out.println("unable to find file '" + in + "'");
		}
	}

}
