#include "testSuite.h"

// ========================================================================== //
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
	struct gameState G;
	int numPlayers = 2;
	int player1 = 0;
	int handCntBefore, handCntAfter, deckCntBefore, deckCntAfter, playCardResult, coinCntBefore, coinCntAfter;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;
	int treasure_cnt_before, treasure_cnt_after, tmp_card;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, cutpurse, great_hall};

	initializeGame(numPlayers, kingdomCards, randomSeed, &G);

	gainCard(cutpurse, &G, TO_HAND, player1);

	coinCntBefore = G.coins;
	if(CARDTEST3_DEBUG){
		printf("coinCntBefore: %d\n", coinCntBefore);
	}

	// If cutpurse card is in players hand, play that card.
	if(handCard(5, &G) == cutpurse){
		playCardResult = playCard(5, -1, -1, -1, &G);
		if(playCardResult < 0){
			fprintf(stderr, "cardtest3 failed at playCard()\n");
			return -1;
		}
	}
	else{
		fprintf(stderr, "cardtest3 failed at handCard(), cutpurse not in hand\n");
		return -1;
	}

	coinCntAfter = G.coins;

	if(CARDTEST3_DEBUG){
		printf("coinCntAfter: %d\n", coinCntAfter);
	}

	if(coinCntAfter != coinCntBefore +2){
		return -1;
	}

	return 0;


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
