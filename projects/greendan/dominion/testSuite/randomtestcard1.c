#include "testSuite.h"


int randomtestcard1(struct gameState* pre){
   unsigned i, j, test_cycle, player;
   int it, card, cards[16], testNum, test_fail;
   struct gameState post;
   static struct Test tests[10];

   SelectStream(2);
   PutSeed(3);
   test_fail = 0;

   // run randomly generated tests MAX_CYCLES times
   for(test_cycle = 0; test_cycle < MAX_CYCLES; test_cycle++){
      for(i = 0; i < sizeof(struct gameState); i++)
         ((char*)pre)[i] = floor(Random() *256);

      if( buildKingdomCards(cards, smithy) > 0 )
         return 1;

      player = floor(Random() * 3);
      pre->deckCount[player] = floor(Random() * MAX_DECK);
      pre->handCount[player] = floor(Random() * MAX_HAND);
      pre->discardCount[player] = floor(Random() * MAX_DECK);
      pre->playedCardCount = floor(Random() * MAX_HAND);

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

      // find smithy card in players hand
      for(it = 0; it < pre->handCount[player]; ++it){
         if(pre->hand[player][it] == smithy){
            card = it;
            break;
         }
      }

      // play smithy card by calling smithyEffect
      smithyEffect(card, player, &post);

      // Check all relevant gameState data
      testNum = 0;
      check(&tests[testNum], "testHandCount", post.handCount[player], pre->handCount[player] + 3);  ++testNum;
      check(&tests[testNum], "testDeckCount", post.handCount[player], pre->handCount[player] - 3); ++testNum;
      check(&tests[testNum], "testDiscardCount", post.discardCount[player], pre->discardCount[player] + 1); ++testNum;
      check(&tests[testNum], "testScore", scoreFor(player, &post), scoreFor(player, pre)); ++testNum;

      // check test results for failures, if any check failed then print results and data
      for(it = 0; it < testNum; ++it){
         if(tests[it].result == fail && test_fail == 0){
            printf("%s", "!-- randomtestcard1 failed ---! \n");
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
   
   printf("%s", "\n\n");
   return 0;
}
