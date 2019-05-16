#include "testSuite.h"


int randomtestadventurer(struct gameState* preState){
   unsigned i, k, n, player, card;
   int it, cards[16];
   struct gameState postState;

   SelectStream(2);
   PutSeed(3);

   for(n = 0; n < MAX_CYCLES; n++){
      for(i = 0; i < sizeof(struct gameState); i++)
         ((char*)preState)[i] = floor(Random() *256);

      if( buildKingdomCards(cards, adventurer) > 0 )
         return 1;

      player = floor(Random() * 3);
      preState->deckCount[player] = floor(Random() * MAX_DECK);
      preState->handCount[player] = floor(Random() * MAX_HAND);
      preState->discardCount[player] = floor(Random() * MAX_DECK);

      // put random cards in players deck
      for(it = 0; it < preState->deckCount[player]; it++){
         preState->deck[player][it] = cards[(int)floor(Random() * 16)];
      }

      // put random cards in players hand
      for(it = 0; it < preState->handCount[player]; ++it){
         preState->hand[player][it] = cards[(int)floor(Random() * 16)];
         // printf("cards[rand]: %d\n", preState->hand[player][it]);
      }

      // put random cards in players discard
      for(it = 0; it < preState->discardCount[player]; ++it){
         preState->discard[player][it] = cards[(int)floor(Random() * 16)];
         // printf("cards[rand]: %d\n", preState->hand[player][it]);
      }

      // save all preState data
      postState = *preState;

      // call adventurerEffect
      adventurerEffect(&postState, player);

      // handCount is not correctly incremented
      if(postState.handCount[player] != preState->handCount[player] + 2){
         printData(preState, &postState, player);
         return 1;
      }

      // total treasure count has changed
      if(
         fullDeckCount(player, copper, &postState) +
         fullDeckCount(player, silver, &postState) +
         fullDeckCount(player, gold, &postState) !=
         fullDeckCount(player, copper, preState) +
         fullDeckCount(player, silver, preState) +
         fullDeckCount(player, gold, preState)
      ){
         printData(preState, &postState, player);
         return 1;
      }

      // count treasure cards in preState Hand
      for(it = 0, i = 0; it < preState->handCount[player]; ++it){
         card = preState->hand[player][it];
         if(card == copper || card == silver || card == gold)
            i++;
      }
      // count treasure cards in postState Hand
      for(it = 0, k = 0; it < postState.handCount[player]; ++it){
         card = postState.hand[player][it];
         if(card == copper || card == silver || card == gold)
            k++;
      }

      // Check that the player does not have +2 treasure cards in hand
      if(k != (i + 2)){
         printData(preState, &postState, player);
         return 1;
      }
  }

  return 0;
}
