#include "testSuite.h"

// ========================================================================== //
// === This unit test check for the following:
// === A. Positive tests
// ===  1. The person who played village gains 1 card to their hand.
// ===  2. The person who played the village earns 2 extra actions.
// ===  3. Player one's buys have not changed
// ===  4. Other players hands do not change
// ===  5. The supply cards are unchanged
// ===  6. Skipping the buy phase, victory points have not changed.
// ===  7. Player 1 deck has 1 less cards
// ===  8. Other players' deck's have not changed

// ========================================================================== //
// ===                          testVillageffect                          === //
// ===                                                                    === //
// ========================================================================== //

int testVillageEffect(){
  struct gameState G;
  memset(&G, '\0', sizeof(struct gameState));

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
      p1Actions,
      treasure_cnt_before,
      treasure_cnt_after,
      totalSupplyCnt;

  const int p1GainCards = 1;
  const int p1GainBuys = 0;
  const int p1GainActions = 2;
  const int otherPlGainCards = 0;
  int numPlayers = 3;
	int p1 = 0;
	int p2 = 1;
  int p3 = 2;
	int testPass = 1;
  int randomSeed = ((int)(Random()*MAX_HAND))%1000;

	int kingdomCards[10] = {
      adventurer,
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

	initializeGame(
    numPlayers,
    kingdomCards,
    randomSeed,
    &G
  );

  // Put council_room in p1 hand
	gainCard(
    village,
    &G,
    TO_HAND,
    p1
  );

  // Set initial handcount, cardHandPos, numBuys and score
  handCntBefore = G.handCount[p1];
  cardHandPos = handCntBefore -1;
  p1DeckCnt = G.deckCount[p1];
  p2DeckCnt = G.deckCount[p2];
  p3DeckCnt = G.deckCount[p3];
  p1BuysBefore = G.numBuys;
  p2HandCntBefore = G.handCount[p2];
  p3HandCntBefore = G.handCount[p3];
  p1Actions = G.numActions;
  p1Score = scoreFor(p1, &G);
  p2Score = scoreFor(p2, &G);
  p3Score = scoreFor(p3, &G);

  // Count all cards in supply
  totalSupplyCnt = 0;
  for(int card = curse; card <= treasure_map; card++){
    totalSupplyCntBefore += supplyCount(card, &G);
  }

  // Check that village is in p1 hand, then play the card.
  if(handCard(cardHandPos, &G) == village){
		playCardResult = playCard(cardHandPos, -1, -1, -1, &G);

    if(playCardResult < 0){
			fprintf(stdout, "*** unittest4 failed at function playCard\n");
			testPass = 0;
		}
	}
	else{
		fprintf(stdout, "*** unittest4 failed at handCard(), village not in hand\n");
		testPass = 0;
	}

  /****** Test A.1 ******/
  // playCard() discards the played card, so the hand count will be 1 less.
  handCntAfter = G.handCount[p1];
  if(G.handCount[p1] != handCntBefore + p1GainCards -1){
    fprintf(stdout, "*** unittest4 failed @ A.1, player did not gain correct number of cards\n");
    testPass = 0;
  }

  /****** Test A.2 ******/
  // This fails due to a bug introduced in assignment 2
  if(G.numActions != (p1Actions + p1GainActions)){
    fprintf(stdout, "*** unittest4 failed @ A.2, player did not earn correct number of actions\n");
    testPass = 0;
  }


  /****** Test A.3 ******/
  p1BuysAfter = G.numBuys;
  if(p1BuysAfter - p1BuysBefore != p1GainBuys){
    fprintf(stdout, "*** unittest4 failed @ A.3 , player did nnot gain correct number of buys\n");
    testPass = 0;
  }

  /****** Test A.4 ******/
  p2HandCntAfter = G.handCount[p2];
  p3HandCntAfter = G.handCount[p3];
  if(p2HandCntAfter - p2HandCntBefore != otherPlGainCards ||
     p2HandCntAfter - p2HandCntBefore != otherPlGainCards){
      fprintf(stdout, "*** unittest4 failed @ A.4, other players hands have chnaged\n");
      testPass = 0;
  }

  /****** Test A.5 ******/
  totalSupplyCntAfter = 0;
  for(int card = curse; card <= treasure_map; card++){
    totalSupplyCntAfter += supplyCount(card, &G);
  }
  if(totalSupplyCntAfter != totalSupplyCntBefore){
    fprintf(stdout, "*** unittest4 failed @ A.5, supply card count was altered\n");
    testPass = 0;
  }

  /****** Test A.6 ******/
  if(scoreFor(p1, &G) != p1Score ||
     scoreFor(p2, &G) != p2Score ||
     scoreFor(p3, &G) != p3Score
  )
  {
    fprintf(stdout, "*** unittest4 failed @ A.6, player(s) score was altered\n");
    testPass = 0;
  }

  /****** Test A.7 ******/
  if(G.deckCount[p1] != (p1DeckCnt - p1GainCards)){
    fprintf(stdout, "*** unittest4 failed @ A.7, player1 deck was incorrectly incremented\n");
    testPass = 0;
  }

  /****** Test A.8 ******/
  if(G.deckCount[p2] != (p2DeckCnt - otherPlGainCards) ||
     G.deckCount[p3] != (p3DeckCnt - otherPlGainCards)
  )
  {
    fprintf(stdout, "*** unittest4 failed at A.8, other players decks were incorrectly incremented\n");
    testPass = 0;
  }

  if(testPass != 1){
    return -1;
  }
  return 0;
}

int main(int argc, char** argv){

	if(testVillageEffect() < 0){
		fprintf (stdout, "==== unittest4 failed.\nSet UNITTEST4_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
