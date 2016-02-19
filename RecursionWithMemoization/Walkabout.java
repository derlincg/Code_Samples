/*
  Name: Carl Derline
  Assignment: lab 3
  Title: Walkabout
  Course: CSCE 371
  Semester: Fall 2015
  Instructor: George Hauser
  Date: 12/8/15
  Sources consulted: textbook, Karen Bullinger
  Program description: This program reads a given file as a command line argument, and calculates
  	the number of paths from the upper left hand corner to the bottom right hand corner of each
  	grid provided in the file.
  Known Problems: none
  Creativity: none
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Walkabout 
{
	BigInteger[][] matrix;
	BigInteger[][] cache;
	
	/**
	 * main. The main function handles all of the reading of the file provided
	 * as a command line argument. The data read will be stored in a global two
	 * dimensional array of BigIntegers called matrix. The function then calls findPaths()
	 * on the matrix at the starting location of matrix[0][0].
	 * @param args the file to be read.
	 */
	public static void main(String[] args) 
	{
		Walkabout w = new Walkabout();
		File f = null;
		Scanner reader;
		
		try {
			// make sure user inputs correct number of command line args
			if(args.length != 1) {
				System.out.println("usage: java Walkabout [input File]");
				System.exit(0);
			}
			// create the file and the scanner to read the file
			f = new File(args[0]);
			reader = new Scanner(f);
			int currProb = reader.nextInt();
			String line;
			
			// continue loop while a -1 has not been encountered
			while(currProb != -1)
			{
				// declare a matrix to hold the data and a matrix for memoization
				w.matrix = new BigInteger[currProb][currProb];
				w.cache = new BigInteger[currProb][currProb];
				//store the data from the file into matrix
				for(int i = 0; i < currProb; i++) {
					line = reader.next();
					for(int j = 0; j < line.length(); j++) {
						w.matrix[i][j] = new BigInteger(line.charAt(j) + "");
					}
				}
				// initialize the cache to all zeros
				for(int i = 0; i < w.cache.length; i++) {
					for(int j = 0; j < w.cache.length; j++)
						w.cache[i][j] = new BigInteger("0");
				}
				System.out.println(w.findPaths(0, 0));
				currProb = reader.nextInt();
			}
		} catch (FileNotFoundException e) {
			// catch the case where the file cannot be found
			System.out.println("Unable to find file '" + f + "'");
		}
	}
	
	/**
	 * findPaths. This method recursively computes the number of paths from the upper
	 * left corner to the bottom right corner of the matrix. The base case is if the
	 * algorithm has reached the bottom right corner of the grid. If it has, return a 1,
	 * which represents one successful path.
	 * @param i the row index of the matrix
	 * @param j the column index of the matrix
	 * @return the number of paths from the upper left corner to the
	 * bottom right corner.
	 */
	public BigInteger findPaths(int i, int j)
	{
		// base case. if we have reached the end of the matrix, return a 1,
		// meaning a path has been found.
		if(i == matrix.length-1 && j == matrix.length-1) {
			return new BigInteger("1");
		}
		else
		{
			// if the cache contains a value, do not recompute it, and simply
			// return the value stored at cache[i][j]
			if(matrix[i][j].intValue() == 0 || cache[i][j].intValue() != 0)
				return cache[i][j];
			// if i + matrix[i][j] does not go past the boundaries of the grid, add to the cache
			// the findPaths call in the right direction
			if(i + matrix[i][j].intValue() < matrix.length && matrix[i][j].intValue() != 0) {
				cache[i][j] = cache[i][j].add(findPaths(i + matrix[i][j].intValue(), j));
			}
			// if i + matrix[i][j] does not go past the boundaries of the grid, add to the cache
			// the findPaths call in the down direction 
			if(j + matrix[i][j].intValue() < matrix.length && matrix[i][j].intValue() != 0) {
				cache[i][j] = cache[i][j].add(findPaths(i, j + matrix[i][j].intValue()));
			}
		}
		return cache[i][j];
	}

}
