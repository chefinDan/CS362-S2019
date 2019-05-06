#include "testSuite.h"

// ========================================================================== //
// ===                        testSmithyCard                              === //
// === This unit test checks for correct functioning of smithyEffect(). 	=== //
// === It checks for the correct number of cards after playing the card   === //
// === as well as the correct number of cards in the deck and discard 		=== //
// === pile.																															=== //
// ========================================================================== //

/* NOTE: This test fails due to the bug I introduced in the previous assignment.
	This bug causes the Smithy card to draw 4 cards from the players deck instead
	of three. The bug has not been fixed.
*/

int testSmithyCard(){
  int handCntBefore,
      handCntAfter,
      deckCntBefore,
      deckCntAfter,
      playCardResult,
      V_cardCntBefore,
      V_cardCntAfter,
      treasure_cnt_before,
      treasure_cnt_after,
      tmp_card;

	struct gameState G;
	int testPass = 1;
	int numPlayers = 2;
	int player = 0;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;

	int kingdomCards[10] = { adventurer,
                           council_room,
                           feast,
                           gardens,
                           mine,
                           remodel,
                           smithy,
                           village,
                           baron,
                           great_hall
  };

	initializeGame(numPlayers,
                 kingdomCards,
                 randomSeed,
                 &G
  );

	// put smithy card in players hand, now the 6th card, position 5
	gainCard(smithy,
           &G,
           TO_HAND,
           player
  );

	handCntBefore = numHandCards(&G);
	deckCntBefore = G.deckCount[player];
	V_cardCntBefore = G.supplyCount[estate] +
                    G.supplyCount[duchy] +
                    G.supplyCount[province];

	if(handCard(5, &G) == smithy){
		playCardResult = playCard(5, 0, 0, 0, &G);
		if(playCardResult < 0){
			fprintf(stdout, "cardtest2 failed at playCard()\n");
			testPass = 0;
		}
	}
	else{
		fprintf(stdout, "cardtest2 failed at handCard(), smithy not in hand\n");
		testPass = 0;
	}

	handCntAfter = numHandCards(&G);
	deckCntAfter = G.deckCount[player];
	V_cardCntAfter = G.supplyCount[estate] +
                   G.supplyCount[duchy] +
                   G.supplyCount[province];

	//Smithy adds 3 cards to hand, and discards smithy card. Leaving the player with net plus 2 cards.
	if(CARDTEST2_DEBUG){
		printf("== inside testSmithyCard(): handCntBefore: %d\n", handCntBefore);
		printf("== inside testSmithyCard(): handCntAfter: %d\n", handCntAfter);
		printf("== inside testSmithyCard(): expected handCntAfter: %d\n", handCntBefore +2);
	}
	if(handCntAfter != handCntBefore +2){
		fprintf(stdout, "*** cardtest2 failed at handCnt, incorrect num of cards in players hand\n");
		testPass = 0;
	}

	if(CARDTEST2_DEBUG){
		printf("== inside testSmithyCard():deckCntBefore: %d\n", deckCntBefore);
		printf("== inside testSmithyCard():deckCntAfter: %d\n", deckCntAfter);
		printf("== inside testSmithyCard(): expected deckCntAfter: %d\n", deckCntBefore -3);
	}
	if(deckCntBefore != deckCntAfter +3){
		fprintf(stdout, "*** cardtest2 failed at deckCnt, incorrect num of cards in players deck\n");
		testPass = 0;
	}

	if(CARDTEST2_DEBUG){
		printf("== inside testSmithyCard():V_cardCntBefore: %d\n", V_cardCntBefore);
		printf("== inside testSmithyCard():V_cardCntAfter: %d\n", V_cardCntAfter);
	}
	if(V_cardCntBefore != V_cardCntAfter){
		fprintf(stdout, "*** cardtest2 failied at V_cardCnt, number of victory cards in supply has chnaged.\n");
		testPass = 0;
	}

	if(testPass == 1){
		return 0;
	}
	return -1;
}

int main(int argc, char** argv){

	if(testSmithyCard() < 0){
		fprintf (stdout, "==== testSmithyCard() failed.\nSet CARDTEST2_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
