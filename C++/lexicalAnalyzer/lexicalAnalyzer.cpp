/* This program is a lexical analyzer for the language tiny C. It reads a tiny C file and
 * produces a table with the lexeme and the number of times it appeared in the file. The program
 * also produces a table with the total number of a each type of lexeme (int literal for example).
 *
 * name: Carl Derline
 * date: 10-17-14
 * assignment: lab 2
 * program name: Lexical analyzer
 * bugs: none.
 * 
 * Sources consulted
 * http://www.cplusplus.com/reference/iomanip/setw/
 * http://www.cplusplus.com/reference/map/map/begin/
 * www.cplusplus.com/reference/iterator/advance/
 * http://en.cppreference.com/w/cpp/container/map/find
 * http://www.cplusplus.com/reference/map/map/
 * http://www.asciitable.com/
 */
using namespace std;
#include <iostream>
#include <string>
#include <cstring>
#include <fstream>
#include <map>
#include <iomanip>

void startsWithDigit(char);
void startsWithPeriod(char);
void startsWithQuote(char);
void id_keyword(string);
void countOccurrences(string);
void type(string);
void printTable();

fstream file;
char currentChar;
string word;
string id;
int PERIOD = 46;
int QUOTE = 34;

//counters for the number of occurrences of each id.
int c_special = 0, c_int_lit = 0, c_int_real = 0, c_keyword = 0, c_str_lit = 0, c_id = 0;
map<string, string> keywords; //stores keywords and symbols.
map<string, string> tokId; //stores everything and their id name.
map<string, int> counter; //stores everything and how many times each item appears.
map<char,int>::iterator it;
map<string, string>::iterator it2;

/*
 * @params: argc and *argv[], where argc is the number of expected
 * command-line arguments, and *argv[] is the infile and outfile names.
 * @return: returns 0 because its the main method.
 * 
 * In this method, I open the file, read the file one character at a
 * time, and process it based on what kind of character it is.
 */
int main(int argc, char *argv[]) 
{
	//initialize values of keyword map.
	keywords["if"] = "keyword";
	keywords["else"] = "keyword";
	keywords["for"] = "keyword";
	keywords["do"] = "keyword";
	keywords["while"] = "keyword";
	keywords["switch"] = "keyword";
	keywords["case"] = "keyword";
	keywords["default"] = "keyword";
	keywords["break"] = "keyword";
	keywords["return"] = "keyword";
	keywords["("] = "special";
	keywords[")"] = "special";
	keywords["["] = "special";
	keywords["]"] = "special";
	keywords["+"] = "special";
	keywords["-"] = "special";
	keywords["="] = "special";
	keywords[","] = "special";
	keywords[";"] = "special";
	keywords["{"] = "special";
	keywords["}"] = "special";
	keywords["&"] = "special";
	keywords["*"] = "special";
	keywords["/"] = "special";

	if(argc != 2)
		cout << "Please enter in the format " << argv[0] << " [infile]" << endl;
	else
	{
		file.open(argv[1], ios::in);
		while(!file.eof())
		{
			currentChar = file.get();
			//call all methods to correctly process currentc
			startsWithDigit(currentChar);
			startsWithPeriod(currentChar);
			startsWithQuote(currentChar);
			id_keyword(word);
			
			if(keywords.find(string(1, currentChar)) != keywords.end())
			{
				//store as special character
				word += currentChar;
				countOccurrences(word);
				id = "special";
				type(word);
				word.clear();
			}
		}
		file.close();
		cout << "Reading file " << argv[1] << "...done." << endl << endl;

		printTable(); //formats output.
	}
	return 0;
}

/*
 * @params: none
 * @return: nothing
 * 
 * In this method, I print the data in a table showing the required
 * statistics.
 */
void printTable()
{
	map<string,int>::iterator it = counter.begin();
	map<string,string>::iterator it2 = tokId.begin();
	
	cout << "==================" << endl;
	cout << "Symbol table" << endl;
	cout << "==================" << endl << endl;
	cout << left << setw(12) << "Lexeme" << "\t\t\t" << "| # appearances" << "\t" << "| class" << endl;
	cout << "-----------------------------------------------------------------" << endl;
	while(it != counter.end() && it2 != tokId.end())
	{
		cout << left << setw(12) << it->first << "\t\t\t";
		cout << left  << "| " << it->second << "\t\t";
		cout << left  << "| " << it2->second << endl;
		it++;
		it2++;
	}
	cout << endl;
	cout << "==================" << endl;
	cout << "Statistics" << endl;
	cout << "==================" << endl << endl;
	
	cout << left << setw(12) << "class" << "\t\t" << "| # appearances" << endl;
	cout << "------------------------------------------" << endl;
	
	it = counter.begin();
	it2 = tokId.begin();
	while(it != counter.end() && it2 != tokId.end())
	{
		if(it2->second == "special") {
			c_special += it->second;
		}
		else if(it2->second == "identifier") {
			c_id += it->second;
		}
		else if(it2->second == "keyword") {
			c_keyword += it->second;
		}
		else if(it2->second == "int_literal") {
			c_int_lit += it->second;
		}
		else if(it2->second == "real_literal") {
			c_int_real += it->second;
		}
		else {
			c_str_lit += it->second;
		}
		it++;
		it2++;	
	}
	cout << left << setw(12) << "Identifier" << "\t\t" << "| " << c_id << endl;
	cout << left << setw(12) << "Int_literal" << "\t\t" << "| " << c_int_lit << endl;
	cout << left << setw(12) << "Real_literal" << "\t\t" << "| " << c_int_real << endl;
	cout << left << setw(12) << "Str_literal" << "\t\t" << "| " << c_str_lit << endl;
	cout << left << setw(12) << "Special" << "\t\t" << "| " << c_special << endl;
	cout << left << setw(12) << "Keyword" << "\t\t" << "| " << c_keyword << endl;
    cout << endl;
}

/*
 * @params: currentc the character being processed.
 * @return: nothing.
 * 
 * This method is used when the first currentc in the string word is a 
 * digit, and adds the character to the string word, and processes the 
 * string according to what file.peek() equals. this method will either 
 * store word as an int literal or a real literal depending on the 
 * prescence of a ".".
 */
void startsWithDigit(char currentc)
{
	while(isdigit(currentc))
	{
		word += currentc;
		if(!isdigit(file.peek())) 
		{
			if(keywords.find(string(1, file.peek())) != keywords.end())
			{
				countOccurrences(word);
				id = "int_literal";
				type(word);
				word.clear();
				break;
			}
			if(isspace(file.peek())) 
			{
				//store as int literal
				countOccurrences(word);
				id = "int_literal";
				type(word);
				word.clear();
				break;
			}	
			if(file.peek() == PERIOD)
			{
				currentc = file.get();
				word += currentc;
				while(isdigit(file.peek())) 
				{
					currentc = file.get();
					word += currentc;
					//becomes real literal
				}
				//store as real literal
				countOccurrences(word);
				id = "real_literal";
				type(word);
				word.clear();
				break;
			}
			
		}	
		else 
		{
			currentc = file.get();
		}
	}
}
	
/*
 * @params: currentc the character being processed.
 * @return: nothing.
 * 
 * This method is used when the first currentc in the string word is a 
 * period, and adds the character to the string word, and processes the 
 * string according to what file.peek() equals. this method will store
 * word as a real literal because it starts with a ".".
 */
void startsWithPeriod(char currentc)
{
	while(currentc == PERIOD && word.empty())
	{
		word += currentc;
		while(isdigit(file.peek())) 
		{
			currentc = file.get();
			word += currentc;
		}
		//store as a real literal
		countOccurrences(word);
		id = "real_literal";
		type(word);
		word.clear();
		break;
	}
}

/*
 * @params: currentc the character being processed.
 * @return: nothing.
 * 
 * This method is used when the first currentc in the string word is a 
 * quote, and adds the character to the string word, and processes the 
 * string according to what file.peek() equals. this method will store
 * word as a string literal.
 */
void startsWithQuote(char currentc)
{
	while(currentc == QUOTE)
	{
		word += currentc;
		while(file.peek() != QUOTE)
		{
			currentc = file.get();
			word += currentc;
		}
		currentc = file.get();
		word += currentc;
		//store as string literal
		countOccurrences(word);
		id = "str_literal";
		type(word);
		word.clear();
		break;
	}
}

/*
 * @params: word the string being processed.
 * @return: nothing.
 * 
 * This method is used when the first currentc in the string word is 
 * alphabetic or a digit when word is not empty, and compares the word
 * with each entry in the keyword map. If it finds a match, it stores
 * word as a keyword, else it will store word as an identifier.
 */
void id_keyword(string word)
{
	while(isalpha(currentChar) || (isdigit(currentChar) && !word.empty()))
	{
		word += currentChar;
		if(keywords.find(word) != keywords.end() && !isalpha(file.peek()))
		{
			//store as keyword
			countOccurrences(word);
			id = "keyword";
			type(word);
			word.clear();
			break;
		}
		if(!isalpha(file.peek()) && !isdigit(file.peek()))
		{
			//store as identifier
			countOccurrences(word);
			id = "identifier";
			type(word);
			word.clear();
			break;
		}
		else
		{
			currentChar = file.get();
		}
	}
}

/*
 * @params: word the string being processed.
 * @return: nothing.
 * 
 * This method adds each word into the map counter, which counts the
 * occurrence of each word. if the word is already in the map, it
 * increments the value of the key.
 */
void countOccurrences(string word)
{
	if(counter.find(word) == counter.end())
	{
		counter.insert(pair<string,int>(word,1));
	}
	else
	{
		counter[word]++;
	}
}

/*
 * @params: word the string being processed.
 * @return: nothing.
 * 
 * This method searches the tokId map, which stores the id of each word.
 * if the word is not in the map, it adds the word with the appropriate
 * id set by the "startsWith" methods.
 */
void type(string word)
{
	if(tokId.find(word) == tokId.end())
	{
		tokId.insert(pair<string, string>(word, id));
	}
}



