#include "testSuite.h"


// ========================================================================== //
// ***                        printData()                                     //
// Description: Prints useful debug information from struct gameState         //
// Param: struct gameState* <pre> - The gameState before a card was played    //
//         struct gameState* <post> - The gameState after a card was played   //
//         unsigned player - The player whose data will be printed            //
// ========================================================================== //
int printData(struct gameState* preState, struct gameState* postState, unsigned player) {
   unsigned treasureCnt;

   // count all treasure
   treasureCnt = fullDeckCount(player, copper, preState) +
                  fullDeckCount(player, silver, preState) +
                  fullDeckCount(player, gold, preState);

   printf("preState->handCount[%u]: %u\n", player, preState->handCount[player]);
   printf("postState.handCount[%u]: %u\n", player, postState->handCount[player]);
   printf("preState->deckCount[%u]: %u\n", player, preState->deckCount[player]);
   printf("postState->deckCount[%u]: %u\n", player, postState->deckCount[player]);
   printf("preState treasureCnt: %u\n", treasureCnt);

   return 0;
}


// ========================================================================== //
// ***                  buildKingdomCards()                               *** //
// Description: Makes a deck of random cards for testing purposes. Will       //
// always inlcude int <required> and and all victory cards/treasure cards     //
// Params: int* <cards> - The array that random cards will be added to.       //
//         int <required> - The card enum that must be included in the deck   //
// ========================================================================== //
int buildKingdomCards(int* cards, int required) {
   unsigned it, n, act_min, act_max;
   act_min = 7;
   act_max = 26;
   // Generate 10 random kingdomCards, only choosing from action cards
   for(it = 0; it < 10; ++it){
      cards[it] = (int)floor(Random() * 26) %(act_max+1-act_min) + act_min;
   }

   // Check for required card
   for(it = 0, n = 0; it < 10; ++it){
      if(cards[it] == required){
         n = 1; break;
      }
   }

   // if required not already added, add it now.
   if(!n){ cards[9] = required; }

   // add victory point cards to cards array
   for(it = 10, n = estate; it < 13; ++it, ++n){
      cards[it] = n;
   }

   // add treasure cards
   for(it = 13, n = copper; it < 16; ++it, ++n){
      cards[it] = n;
   }

   // if building the kingdomCards failed, then stop everything
   for(it = 0; it < 16; ++it){
      if(cards[it] < curse || cards[it] > treasure_map){
         printf("%s\n", "buildKingdomCards failed");
         printf("cards[%u]: %d\n", it, cards[it]);
         return 1;
      }
   }

   return 0;
}


int randomtestadventurer(struct gameState* preState){
   unsigned i, k, n, player, card;
   int it, cards[16];
   struct gameState postState;

   SelectStream(2);
   PutSeed(3);

   for(n = 0; n < 1000; n++){
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
