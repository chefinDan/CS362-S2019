#include "testSuite.h"

int testCutpurseCard(){
	struct gameState G;
	int numPlayers = 2;
	int player = 0;
	int handCntBefore, handCntAfter, deckCntBefore, deckCntAfter;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;
	int treasure_cnt_before, treasure_cnt_after, tmp_card;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, cutpurse, great_hall};

	initializeGame(numPlayers, kingdomCards, randomSeed, &G);

	gainCard(cutpurse, &G, TO_DECK, player);








}
