#include "testSuite.h"

int main(){
  struct gameState preState = {0};
  int result;

   printf("%s\n\n", "=== Running randomtestadventurer().... ");
   result = randomtestadventurer(&preState);
   if(result != 0){
      printf("%s\n", "randomtestadventurer failed");
   }
   memset(&preState, 0, sizeof(struct gameState));


   printf("%s\n\n", "=== Running randomtestcard1().... ");
   result = randomtestcard1(&preState);
   
   if(result != 0){
      printf("%s\n", "randomtestsmithy failed");
   }



  return 0;
}
