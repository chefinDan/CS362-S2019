#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<time.h>

#define STR_MAX 6

// =========================================================== //
// ===                    inputChar()                      === //
// === This function returns a char in the ascii range of  === //
// === 32-127. This includes all printable ascii characters. = //
// =========================================================== //
char inputChar()
{
    return rand()%127 + 32;
}


// =========================================================== //
// ===                  inputString                        === //
// === This function returns a random string of 6 chars    === //
// === that is witin the testable domain of testme().      === //
// === No other characters are returned because testme()   === //
// === does not have code to handle any chars other than   === //
// === r,e,s,e,t and '\0'.                                 === //
// =========================================================== //
char *inputString()
{
    int i;
    static char randStr[STR_MAX];

    char letters[STR_MAX] = {'r','e','s','e','t','\0'};

    for(i = 0; i < STR_MAX; i++){
      randStr[i] = letters[rand()%STR_MAX];
    }

    return randStr;
}


void testme()
{
  int tcCount = 0;
  char *s;
  char c;
  int state = 0;
  while (1)
  {
    tcCount++;
    c = inputChar();
    s = inputString();
    printf("Iteration %d: c = %c, s = %s, state = %d\n", tcCount, c, s, state);

    if (c == '[' && state == 0) state = 1;
    if (c == '(' && state == 1) state = 2;
    if (c == '{' && state == 2) state = 3;
    if (c == ' '&& state == 3) state = 4;
    if (c == 'a' && state == 4) state = 5;
    if (c == 'x' && state == 5) state = 6;
    if (c == '}' && state == 6) state = 7;
    if (c == ')' && state == 7) state = 8;
    if (c == ']' && state == 8) state = 9;
    // if(state == 9) return;
    if (s[0] == 'r' && s[1] == 'e'
       && s[2] == 's' && s[3] == 'e'
       && s[4] == 't' && s[5] == '\0'
       && state == 9)
    {
      printf("error ");
      exit(200);
    }
  }
}


int main(int argc, char *argv[])
{
    srand(time(NULL));
    testme();
    return 0;
}
