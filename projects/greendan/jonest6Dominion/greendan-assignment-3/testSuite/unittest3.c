#include "testSuite.h"

// ========================================================================== //
// === This unit test check for the following:
// === A. Positive tests
// ===  1. The person who played council_room gains 4 cards to their hand.
// ===  2. The person who played the council_room earns an extra buy.
// ===  3. Every other player has one extra card in their hand.
// ===  4. The supply cards are unchanged
// ===  5. Skipping the buy phase, victory points have not changed.
// ===  6. Player 1 deck has 4 less cards
// ===  7. Other players' deck's have 1 less card

// ========================================================================== //
// ===                          testCouncil_roomEffect                    === //
// ===                                                                    === //
// ========================================================================== //

int testCouncil_roomEffect(){

  struct gameState* G = malloc(sizeof(struct gameState));
  memset(G, '\0', sizeof(struct gameState));

  int cardHandPos,
    handCntBefore,
    handCntAfter,
    p2HandCntBefore,
    p2HandCntAfter,
    p3HandCntBefore,
    p3HandCntAfter,
    totalSupplyCntBefore,
    totalSupplyCntAfter,
    p1DeckCnt,
    p2DeckCnt,
    p3DeckCnt,
    p1Score, p2Score, p3Score,
    playCardResult,
    p1BuysBefore,
    p1BuysAfter,
    treasure_cnt_before,
    treasure_cnt_after,
    totalSupplyCnt;

  const int p1GainCards = 4;
  const int p1GainBuys = 1;
  const int otherPlGainCards = 1;
  int numPlayers = 3;
	int p1 = 0;
	int p2 = 1;
  int p3 = 2;
	int testPass = 1;
  int randomSeed = ((int)(Random()*MAX_HAND))%1000;

	int kingdomCards[10] = {
    adventurer,
    village,
    feast,
    gardens,
    mine,
    remodel,
    smithy,
    council_room,
    cutpurse,
    great_hall
  };

	initializeGame(
    numPlayers,
    kingdomCards,
    randomSeed,
    G
  );

  // Put council_room in p1 hand
	gainCard(
    council_room,
    G,
    TO_HAND,
    p1
  );

  // Set initial handcount, cardHandPos, numBuys and score
  handCntBefore = G->handCount[p1];
  cardHandPos = handCntBefore -1;
  p1DeckCnt = G->deckCount[p1];
  p2DeckCnt = G->deckCount[p2];
  p3DeckCnt = G->deckCount[p3];
  p1BuysBefore = G->numBuys;
  p2HandCntBefore = G->handCount[p2];
  p3HandCntBefore = G->handCount[p3];

  p1Score = scoreFor(p1, G);
  p2Score = scoreFor(p2, G);
  p3Score = scoreFor(p3, G);

  // Count all cards in supply
  totalSupplyCntBefore = 0;
  for(int card = curse; card <= treasure_map; card++){
    totalSupplyCntBefore += supplyCount(card, G);
  }

  // Check that council_room is in p1 hand, then play the card.
  if(handCard(cardHandPos, G) == council_room){
		playCardResult = playCard(cardHandPos, -1, -1, -1, G);

    if(playCardResult < 0){
			fprintf(stdout, "*** unittest3 failed at function playCard\n");
			testPass = 0;
		}
	}
	else{
		fprintf(stdout, "*** unittest3 failed at handCard(), council_room not in hand\n");
		testPass = 0;
	}

  /****** Test A.1 ******/
  // playCard() discards the played card, so the hand count will be 1 less.
  if(G->handCount[p1] != handCntBefore + p1GainCards -1){
    fprintf(stdout, "*** unittest3 failed @ A.1, player did nnot gain correct number of cards\n");
    testPass = 0;
  }

  /****** Test A.2 ******/
  // This will fail due to bug introduced in assignment 2
  if(G->numBuys != p1BuysBefore + p1GainBuys){
    fprintf(stdout, "*** unittest3 failed @ A.2, player did not gain correct number of buys\n");
    testPass = 0;
  }

  /****** Test A.3 ******/
  p2HandCntAfter = G->handCount[p2];
  p3HandCntAfter = G->handCount[p3];
  if(p2HandCntAfter - p2HandCntBefore != otherPlGainCards ||
     p2HandCntAfter - p2HandCntBefore != otherPlGainCards){
      fprintf(stdout, "*** unittest3 failed @ A.3, other players did nnot gain correct number of cards\n");
      testPass = 0;
  }

  /****** Test A.4 ******/
  totalSupplyCntAfter = 0;
  for(int card = curse; card <= treasure_map; card++){
    totalSupplyCntAfter += supplyCount(card, G);
  }
  if(totalSupplyCntAfter != totalSupplyCntBefore){
    fprintf(stdout, "*** unittest3 failed @ A.4, supply card count was al1tered\n");
    testPass = 0;
  }
  if(UNITTEST3_DEBUG){
    printf("totalSupplyCntAfter: %d\n", totalSupplyCntAfter);
    printf("totalSupplyCntBefore: %d\n", totalSupplyCntBefore);
  }

  /****** Test A.5 ******/
  if(scoreFor(p1, G) != p1Score ||
     scoreFor(p2, G) != p2Score ||
     scoreFor(p3, G) != p3Score
  )
  {
    fprintf(stdout, "*** unittest3 failed @ A.5, player(s) score was altered\n");
    testPass = 0;
  }

  /****** Test A.6 ******/
  if(G->deckCount[p1] != (p1DeckCnt - p1GainCards)){
    fprintf(stdout, "*** unittest3 failed @ A.6, player1 deck was incorrectly incremented\n");
    testPass = 0;
  }

  /****** Test A.7 ******/
  if(G->deckCount[p2] != (p2DeckCnt - otherPlGainCards) ||
     G->deckCount[p3] != (p3DeckCnt - otherPlGainCards)
  )
  {
    fprintf(stdout, "*** unittest3 failed @ A.7, other players decks were incorrectly incremented\n");
    testPass = 0;
  }

  if(testPass != 1){
    free(G);
    return -1;
  }
  free(G);
  return 0;
}

int main(int argc, char** argv){

	if(testCouncil_roomEffect() < 0){
		fprintf (stdout, "==== unittest3 failed.\nSet UNITTEST3_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
