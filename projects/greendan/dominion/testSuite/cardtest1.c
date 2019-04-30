#include "testSuite.h"
// ========================================================================== //
// === 												testAdventurerEffect()											=== //
// ===	This function tests the adventureCardEffect function. It returns -1 = //
// ===	if the test failed, else it returns 0. 														=== //																					=== //
// ========================================================================== //

int testAdventurerCard(){
	struct gameState G;
	int numPlayers = 2;
	int player = 0;
	int toDeck = 1;
	int treasure_cnt_before, treasure_cnt_after, tmp_card;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, baron, great_hall};


	initializeGame(numPlayers, kingdomCards, 3, &G);

	// put 20 cards in the players deck, randomly picking from the 10 kingdom
	// cards and specifically putting silvers in the deck.
	for(int i = 0; i < 10; i++){
		gainCard(kingdomCards[ ((int)(Random()*MAX_HAND))%10 ], &G, toDeck, player);
		gainCard(silver, &G, toDeck, player);
	}

	// count treasure cards in hand before playing adventurer card
	treasure_cnt_before = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		tmp_card = G.hand[player][i];
		if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
			treasure_cnt_before++;
		}
	}

	adventurerEffect(&G, player);

	// count treasure cards in hand after playing adventurer card
	treasure_cnt_after = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		tmp_card = G.hand[player][i];
		if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
			treasure_cnt_after++;
		}
	}

	// the treasure in hand should be the amount before playing the adventurer card
	// plus two. If not, return -1.
	if(treasure_cnt_after == treasure_cnt_before + 2) {
		return 0;
	}
	return -1;
}
