#include "testSuite.h"
// const char* allCards[] = {
// 											 "curse", "estate", "duchy", "province", "copper", "silver",
// 											 "gold", "adventurer", "council_room", "feast", "gardens",
// 											 "mine", "remodel", "smithy", "village", "baron", "great_hall",
// 											 "minion", "steward", "tribute", "ambassador", "cutpurse",
// 											 "embargo", "outpost", "salvager", "sea_hag", "treasure_map"
// 											};

int main(){
  struct gameState preState;
  int numPlayers, randSeed, result;
  int cards[] = {
    curse,
    feast,
    great_hall,
    cutpurse,
    treasure_map,
    ambassador,
    adventurer,
    council_room,
    salvager,
    mine
  };

  printf("%s\n\n", "=== Running randomtestadventurer().... ");
  result = randomtestadventurer(&preState);
  if(result != 0){
    printf("%s\n", "test failed");
  }

  return 0;
}
