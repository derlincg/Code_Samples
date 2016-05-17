/*
	Name: Carl Derline
	Lab name: Shell part C
	Course: CSCE 444
	Date: 4-11-16
	Language: C, Compiler version: gcc version 4.9.2
	Sources consulted:
		http://stackoverflow.com/questions/17166721/pipe-implementation-in-linux-using-c
		Karen Bullinger
	Description: This is part C of the shell lab. It has all the functionality
		of parts A and B, and can also handle I/O redirection and pipes.

*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <linux/limits.h>
void execNonPipe();
void execPipeCommand();
int execNonExecutable();

int input_fd, redirect_fd;
char *redirect_file, *input_file;
char *argv[100], *argv2[100];
int operatorCounter = 1;
int pid, status, AMPERSAND = 0, R_CARET = 0, L_CARET = 0, PIPE = 0, gate = 0;
int fd[2]; // pipe
int main()
{
	char *token, *cwd;
	char command[100];
	while(1)
	{
		int i = 0;
		char pathBuffer[PATH_MAX + 1];
		cwd = getcwd(pathBuffer, PATH_MAX + 1);
		printf("%s@shell3:%s$ ", getenv("USER"), cwd); // prompt
		fgets(command, 100, stdin);
		command[strcspn(command, "\n")] = '\0'; // strip newline character from fgets
		token = strtok(command, " "); // get first part of command
		// commence parsing
		while(token != NULL)
		{
			if(*token == '>')
			{
				R_CARET = operatorCounter;
				operatorCounter++;
				token = strtok(NULL, " ");
				redirect_file = malloc(strlen(token) + 1); // store redirect_file
				redirect_file = token;
				//token = strtok(NULL, " "); // cant include this...
			}
			else if(*token == '<')
			{
				L_CARET = operatorCounter;
				operatorCounter++;
				token = strtok(NULL, " ");
				input_file = malloc(strlen(token) + 1); // store input_file
				input_file = token;
				token = strtok(NULL, " ");
			}
			else if(*token == '|')
			{
				PIPE = operatorCounter;
				operatorCounter++;
				argv[i] = '\0'; // end argv with a null byte
				i = 0; // reset i for argv list for 2nd command
				while(*token != '<' && *token != '>') // get arguments for 2nd command
				{
					if(!gate)
					{
						token = strtok(NULL, " "); // gets argv2[0]
						gate = 1;
					}
					argv2[i] = malloc(strlen(token) + 1);
					argv2[i] = token;
					token = strtok(NULL, " ");
					i++;
					if(token == NULL)
						break;
				}
				argv2[i] = '\0'; // end argv2 with a null byte
			}
			else if(!R_CARET && !PIPE && !L_CARET) // arguments for initial command end here
			{
				argv[i] = malloc(strlen(token) + 1);
				argv[i] = token;
				token = strtok(NULL, " ");
				i++;
			}
			else
				token = strtok(NULL, " ");
		}

		// end argv with a null byte
		if(*argv[i-1] == '&')
		{
			argv[i-1] = '\0';
			AMPERSAND = 1;
		}
		else if(!PIPE)
			argv[i] = '\0';

		if(!PIPE) // any command without a pipe
			execNonPipe();
		else // commands containing a pipe
			execPipeCommand();

		// reset arrays for the next command
		memset(command, 0, strlen(command));
		memset(argv, 0, strlen(*argv));
		if(argv2[0] != NULL)
			memset(argv2, 0, strlen(*argv2)); // only reset argv2 if it is used.
		AMPERSAND = 0;
		PIPE = 0;
		R_CARET = 0;
		L_CARET = 0;
		operatorCounter = 1;
		gate = 0;
	}
	return 0;
}

/*
 * This function executes any command that does not contain a pipe. First, it checks
 * whether the command was non-executable by calling execNonExecutable(). If it is
 * executable, the rest of the code runs, checking for I/O redirections.
 */
void execNonPipe()
{
	int flag = 1;
	if((flag = execNonExecutable()) == 0) // if command is executable
	{
		pid = fork();
		if(pid == -1)
			perror("fork failed.");
		else if(pid == 0) // child
		{
			// input and output redirect are both present.
			// open and close necessary files and file descriptors
			if(R_CARET && L_CARET)
			{
				input_fd = open(input_file, O_RDONLY|S_IRUSR);
				dup2(input_fd, 0); // input comes from input_fd
				close(input_fd);
				if((redirect_fd = open(redirect_file, O_WRONLY|O_CREAT, S_IRUSR|S_IWUSR)) < 0)
					perror("failed to open redirect file");
				dup2(redirect_fd, 1); // redirect output to specified file
				close(redirect_fd);
				execvp(argv[0], argv);
			}
			else if(L_CARET) // only left caret is present.
			{
				if((input_fd = open(input_file, O_RDONLY|S_IRUSR)) < 0)
					perror("failed to open redirect file");
				dup2(input_fd, 0); // redirect input_fd as stdin
				close(input_fd);
				execvp(argv[0], argv);
				perror("execvp failed.");
			}
			else if(R_CARET) // only right caret present
			{
				if((redirect_fd = open(redirect_file, O_WRONLY|O_CREAT, S_IRUSR|S_IWUSR)) < 0)
					perror("failed to open redirect file");
				dup2(redirect_fd, 1); // redirect output to specified file
				close(redirect_fd);
				execvp(argv[0], argv);
			}
			else
				execvp(argv[0], argv);
		}
		else // parent
		{
			if(!AMPERSAND) // only wait if an ampersand is not present
				pid = wait(&status);
		}
	}
}

/*
 * 	This function executes commands containing a pipe. I separated this out from
 * 	other commands because it requires two forks rather than one.
 */
void execPipeCommand()
{
	if(pipe(fd) == -1) // pipe
	{
		fprintf(stderr, "Pipe failed.\n");
		exit(1);
	}

	pid = fork(); // fork for first command
	if(pid < 0)
		perror("fork 1 failed.\n");

	else if(pid == 0) // child 1
	{
		if(L_CARET && PIPE > L_CARET) // example: grep word < input.txt | wc
		{
			if((input_fd = open(input_file, O_RDONLY|S_IRUSR)) < 0)
				perror("failed to open redirect file");
			dup2(input_fd, STDIN_FILENO); // input redirected to input_file
			close(input_fd);
			close(STDOUT_FILENO);
			dup2(fd[1], STDOUT_FILENO); // output to the pipe
			close(fd[1]);
			close(fd[0]);
			execvp(argv[0], argv);
		}
		else
		{
			close(STDOUT_FILENO);
			dup(fd[1]); // replace stdout with pipe write
			close(fd[0]);
			close(fd[1]);
			execvp(argv[0], argv); // writes to the pipe
			perror("execvp 1 failed.\n");
		}

	}

	pid = fork(); // fork for second command
	if(pid < 0)
		perror("fork 2 failed.\n");
	else if(pid == 0)
	{
		if(R_CARET && PIPE < R_CARET) // example: cat Shell.c | head > out.txt
		{
			// data will be in pipe
			if((redirect_fd = open(redirect_file, O_WRONLY|O_CREAT, S_IRUSR|S_IWUSR)) < 0)
				perror("failed to open redirect file");
			dup2(redirect_fd, 1); // redirect output to redirect_file
			close(redirect_fd);
			close(STDIN_FILENO);
			dup2(fd[0], STDIN_FILENO); // input comes from pipe
			close(fd[1]);
			close(fd[0]);
			execvp(argv2[0], argv2);
		}
		else
		{
			close(STDIN_FILENO);
			dup(fd[0]); // replace stdin with pipe read
			close(fd[1]);
			close(fd[0]);
			execvp(argv2[0], argv2);
			perror("execvp 2 failed.\n");
		}
	}
	// parent
	close(fd[0]);
	close(fd[1]);
	if(!AMPERSAND) // only wait if an ampersand is not present
	{
		pid = wait(&status); // wait for both children
		pid = wait(&status);
	}
}

/*
 * This function is called by execNonPipe(), and simply checks if the command
 * is a non-executable command... All of which require different code to
 * run properly. Returns 1 if the command was a non-executable, 0 otherwise.
 */
int execNonExecutable()
{
	int returnValue = 0;
	if(strcmp(argv[0], "cd") == 0)
	{
		chdir(argv[1]);
		returnValue = 1;
	}
	else if(strcmp(argv[0], "rm") == 0)
	{
		remove(argv[1]);
		returnValue = 1;
	}
	else if(strcmp(argv[0], "exit") == 0)
		exit(0);
	else
		returnValue = 0;
	return returnValue;
}

