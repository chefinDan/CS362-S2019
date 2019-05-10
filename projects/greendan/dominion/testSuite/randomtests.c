#include "testSuite.h"

int randomTestDrawCard(){

    SelectStream(2);
    PutSeed(3);
    int player, i, n, drawResult;
    double rand;
    struct gameState G;
    for(n = 0; n < 10000; n++){
      for(i = 0; i < sizeof(struct gameState); i++){
        ((char*)&G)[i] = floor(Random() *256);
      }
      player = floor(Random() * 2);
      G.deckCount[player] = floor(Random() * MAX_DECK);
      G.discardCount[player] = floor(Random() * MAX_DECK);
      G.handCount[player] = floor(Random() * MAX_HAND);
      drawResult = drawCard(player, &G);
      if(drawResult != 0){
        printf("%s\n", "drawCard failed");
        return 1;
      }
      // G.deckCount[player] = 0;
      // drawResult = drawCard(player, &G);
      // if(drawResult != 0){
      //   printf("%s\n", "drawCard failed");
      //   return 1;
      // }
    }
}
int main(int argc, char* argv[]){
  randomTestDrawCard();
  return 0;
}
