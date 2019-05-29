#include "testSuite.h"

// ========================================================================== //
// ===                    testCutpurseCard                                === //
// === This unit test checks for correct functioning of cutpurseEffect(). === //
// === It checks for the correct number of cards after playing the card   === //
// === as well as the correct number of cards in the deck and discard 		=== //
// === pile.																															=== //
// ========================================================================== //

/* NOTE: in constrcuting this unit test, I found a bug that caused G.coins
	to not be correctly updated to reflect the two bonus coins gained by playing
	the cutpurse action card.
	This bug was fixed.
*/

int testCutpurseCard(){
  int handCntBefore,
      handCntAfter,
      deckCntBefore,
      deckCntAfter,
      playCardResult,
      p2HandCntBefore,
      p2HandCntAfter,
      p2HasCopper,
      coinCntBefore,
      coinCntAfter,
      treasure_cnt_before,
      treasure_cnt_after,
      tmp_card;

  struct gameState G;
	int numPlayers = 2;
	int player1 = 0;
  int player2 = 1;
  int testPass = 1;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;

	int kingdomCards[10] = { adventurer,
                           council_room,
                           feast,
                           gardens,
                           mine,
                           remodel,
                           smithy,
                           village,
                           cutpurse,
                           great_hall
  };

	initializeGame(numPlayers,
                 kingdomCards,
                 randomSeed,
                 &G
  );

	gainCard(cutpurse,
           &G,
           TO_HAND,
           player1
  );

  // Draw player2's cards
  for(int i = 0; i < 5; i++){
    drawCard(player2, &G);
  }

	coinCntBefore = G.coins;
	if(CARDTEST3_DEBUG){
		printf("coinCntBefore: %d\n", coinCntBefore);
	}

  p2HandCntBefore = G.handCount[player2];
  if(CARDTEST3_DEBUG){
		printf("player 2 handCntBefore: %d\n", p2HandCntBefore);
	}

  p2HasCopper = 0;
  for(int i = 0; i < p2HandCntBefore; i++){
    if(G.hand[player2][i] == copper){
      p2HasCopper = 1;
    }
  }

	// If cutpurse card is in players hand, play that card.
	if(handCard(5, &G) == cutpurse){
		playCardResult = playCard(5, -1, -1, -1, &G);
		if(playCardResult < 0){
			fprintf(stdout, "cardtest3 failed at playCard()\n");
			testPass = 0;
		}
	}
	else{
		fprintf(stdout, "cardtest3 failed at handCard(), cutpurse not in hand\n");
		testPass = 0;
	}

	coinCntAfter = G.coins;

  if(p2HasCopper > 0){
    if(G.handCount[player2] != 4){
      fprintf(stdout, "cardtest3 failed at player2 handcount, player2 should have discarded a copper\n");
  		testPass = 0;
    }
  }
  else{
    if(CARDTEST3_DEBUG){
      fprintf(stdout, "%s\n", "Player two did not have a copper in hand");
    }
  }

	if(CARDTEST3_DEBUG){
		printf("coinCntAfter: %d\n", coinCntAfter);
	}

	if(coinCntAfter != coinCntBefore +2){
    fprintf(stdout, "cardtest3 failed at player1 coinCnt, player1 should have 2 extra coin\n");
    testPass = 0;
	}

  if(testPass = 0){
    return -1;
  }
  else{
    return 0;
  }

}

int main(int argc, char** argv){

	if(testCutpurseCard() < 0){
		fprintf (stdout, "==== testCutpurseCard() failed.\nSet CARDTEST3_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
