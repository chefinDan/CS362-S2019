#include "testSuite.h"


int randomtestcard2(struct gameState* pre){
   unsigned i, j, test_cycle, player;
   int it, handPos, cards[16],
       testNum, test_fail,
       choice1, choice2, choice3,
       trashedVP;
   struct gameState post;
   static struct Test tests[10];

   SelectStream(2);
   PutSeed(3);
   test_fail = trashedVP = 0;

   // run randomly generated tests MAX_CYCLES times
   for(test_cycle = 0; test_cycle < MAX_CYCLES; test_cycle++){
      for(i = 0; i < sizeof(struct gameState); i++)
         ((char*)pre)[i] = floor(Random() *256);

      if( buildKingdomCards(cards, steward) > 0 ) return 1;

      player = floor(Random() * 3);
      pre->deckCount[player] = floor(Random() * MAX_DECK);
      pre->handCount[player] = floor(Random() * MAX_HAND);
      pre->discardCount[player] = floor(Random() * MAX_DECK);
      pre->playedCardCount = floor(Random() * MAX_HAND);
      updateCoins(player, pre, 0);

      // put random cards in players deck
      for(it = 0; it < pre->deckCount[player]; it++){
         pre->deck[player][it] = cards[(int)floor(Random() * 16)];
      }

      // put random cards in players hand
      for(it = 0; it < pre->handCount[player]; ++it){
         pre->hand[player][it] = cards[(int)floor(Random() * 16)];
      }

      // put random cards in players discard
      for(it = 0; it < pre->discardCount[player]; ++it){
         pre->discard[player][it] = cards[(int)floor(Random() * 16)];
      }

      // save all pre data
      post = *pre;

      // find steward card in players hand
      for(it = 0; it < pre->handCount[player]; ++it){
         if(pre->hand[player][it] == steward){
            handPos = it;
            break;
         }
      }

      // Choose random values for each choice
      choice1 = floor(Random() * 3);
      choice2 = floor(Random() * pre->handCount[player]);
      choice3 = floor(Random() * pre->handCount[player]);

      // Choices 2 and 3 represent cards in the players hand that might be trashed.
      // While unrealistic during normal play, it is valid move to trash a victory card.
      // This section of code determines how many victory points will be lost if victory cards
      // are cosen to be trashed.
      if(choice2 == estate || choice2 == duchy || choice2 == province){
         if(choice2 == estate)   trashedVP += 1;
         if(choice2 == duchy)    trashedVP += 3;
         if(choice2 == province) trashedVP += 6;
      }
      if(choice3 == estate || choice3 == duchy || choice3 == province){
         if(choice3 == estate)   trashedVP += 1;
         if(choice3 == duchy)    trashedVP += 3;
         if(choice3 == province) trashedVP += 6;
      }

      // play steward card by calling stewardEffect
      stewardEffect(choice1, choice2, choice3, player, &post, handPos);

      // Check all relevant gameState data
      testNum = 0;
      if(choice1 == 1){
         check(&tests[testNum], "testHandCount", (pre->handCount[player] + 1), (post.handCount[player] ) ); ++testNum;
         check(&tests[testNum], "testDeckCount", ((pre->deckCount[player]) - 2), post.deckCount[player] ); ++testNum;
         check(&tests[testNum], "testDiscardCount", ((pre->discardCount[player]) + 1), post.discardCount[player] ); ++testNum;
         check(&tests[testNum], "testScore", scoreFor(player, &post), scoreFor(player, pre)); ++testNum;
      }
      else if(choice1 == 2){
         check(&tests[testNum], "testBuyCount", post.numBuys, pre->numBuys + 2);  ++testNum;
         check(&tests[testNum], "testHandCount", post.handCount[player], pre->handCount[player] - 1); ++testNum;
         check(&tests[testNum], "testDeckCount", post.deckCount[player], pre->deckCount[player]); ++testNum;
         check(&tests[testNum], "testDiscardCount", post.discardCount[player], pre->discardCount[player] + 1); ++testNum;
         check(&tests[testNum], "testScore", scoreFor(player, &post), scoreFor(player, pre)); ++testNum;
      }
      else{
         check(&tests[testNum], "testHandCount", (pre->handCount[player] -3) , post.handCount[player]); ++testNum;
         check(&tests[testNum], "testDeckCount", pre->deckCount[player], post.deckCount[player]); ++testNum;
         check(&tests[testNum], "testDiscardCount", pre->discardCount[player], post.discardCount[player]); ++testNum;
         check(&tests[testNum], "testScore", scoreFor(player, pre) - trashedVP, scoreFor(player, &post) ); ++testNum;
      }

      // check test results for failures, if any check failed then print results and data
      for(it = 0; it < testNum; ++it){
         if(tests[it].result == fail && test_fail == 0){
            printf("%s", "!-- randomtestcard2 failed ---! \n");
            for(j = 0; j < testNum; ++j){
               printf("%d. %s\n", j, tests[j].testFor);
               printf(" result: %d\n", tests[j].result);
               test_fail = 1;
            }
            if(test_fail == 1){
               printData(test_cycle, pre, &post, player);
            }
         }
      }
   }
   if(test_fail == 0){
      printf("%s", "+++ all tests passed +++ \n");
   }
   return 0;
}
