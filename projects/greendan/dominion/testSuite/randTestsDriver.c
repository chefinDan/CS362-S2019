#include "testSuite.h"

int main(){
  struct gameState preState;
  int result;

  printf("%s\n\n", "=== Running randomtestadventurer().... ");
  result = randomtestadventurer(&preState);
  if(result != 0){
    printf("%s\n", "test failed");
  }

  return 0;
}
