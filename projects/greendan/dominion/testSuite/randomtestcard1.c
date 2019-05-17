#include "testSuite.h"


int randomtestsmithy(struct gameState* preState){
   unsigned i, k, n, player;
   int it, card, cards[16];
   struct gameState postState;

   SelectStream(2);
   PutSeed(3);

   for(n = 0; n < MAX_CYCLES; n++){
      for(i = 0; i < sizeof(struct gameState); i++)
         ((char*)preState)[i] = floor(Random() *256);

      if( buildKingdomCards(cards, smithy) > 0 )
         return 1;

      player = floor(Random() * 3);
      preState->deckCount[player] = floor(Random() * MAX_DECK);
      preState->handCount[player] = floor(Random() * MAX_HAND);
      preState->discardCount[player] = floor(Random() * MAX_DECK);
      preState->playedCardCount = floor(Random() * MAX_HAND);

      // put random cards in players deck
      for(it = 0; it < preState->deckCount[player]; it++){
         preState->deck[player][it] = cards[(int)floor(Random() * 16)];
      }

      // put random cards in players hand
      for(it = 0; it < preState->handCount[player]; ++it){
         preState->hand[player][it] = cards[(int)floor(Random() * 16)];
      }

      // put random cards in players discard
      for(it = 0; it < preState->discardCount[player]; ++it){
         preState->discard[player][it] = cards[(int)floor(Random() * 16)];
      }

      // save all preState data
      postState = *preState;

      for(it = 0; it < preState->handCount[player]; ++it){
         if(preState->hand[player][it] == smithy){
            card = it;
            break;
         }
      }

      smithyEffect(card, player, &postState);
   }
   return 0;
}
