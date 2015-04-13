import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary 
{
	private static ArrayList<String> dictionary;
	public int size;
	
	/**
	 * this is the constructor for the dictionary. it stores
	 * all the words from a text document into an arraylist and
	 * loads the dictionary in boggleGame.java.
	 * @param filename the dictionary file that is being read.
	 */
	public Dictionary(String filename)
	{
		dictionary = new ArrayList<String>();
		String inputLine;
		Scanner infile = null;
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("file not found " + filename);
			e.printStackTrace();
		}
		while(infile.hasNext())
		{
			inputLine = infile.nextLine();
			dictionary.add(inputLine);
			size++;
		}
	}

	/**
	 * This method searches the dictionary. when called, this method searches
	 * for the desired word via binary search.
	 * @param target the desired word.
	 * @param first the start index of the dictionary.
	 * @param last the end index of the dictionary
	 * @return
	 */
	public static int search(Comparable<String> target, int first, int last)
	{
		if(first > last)
			return (- first - 1);
		int mid = (first+last)/2;
		int result = target.compareTo(dictionary.get(mid));
		if(result == 0)
			return mid;
		else if(result < 0)
			return search(target, first, mid-1);
		else
			return search(target, mid+1, last);
	}
}
