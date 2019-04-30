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
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;
	int treasure_cnt_before, treasure_cnt_after, tmp_card;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, baron, great_hall};


	// === 	Test for drawing correct number of treasure cards 							=== //
	// === under normal circumstances.																			=== //
	initializeGame(numPlayers, kingdomCards, randomSeed, &G);

	// put 20 cards in the players deck, randomly picking from the 10 kingdom
	// cards and specifically putting silvers in the deck.
	for(int i = 0; i < 10; i++){
		gainCard(kingdomCards[ ((int)(Random()*MAX_HAND))%10 ], &G, TO_DECK, player);
		gainCard(silver, &G, TO_DECK, player);
	}
	// put adventurer in the players hand
	gainCard(adventurer, &G, TO_HAND, player);

	// count treasure cards in hand before playing adventurer card
	treasure_cnt_before = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		tmp_card = G.hand[player][i];
		if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
			treasure_cnt_before++;
		}
	}

	// Play the adventurer card
	if(handCard(5, &G) == adventurer){
		playCard(5, 0, 0, 0, &G);
	}

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
	if(treasure_cnt_after != treasure_cnt_before + 2) {
		return -1;
	}

	// === 	Test for drawing correct number of treasure cards 							=== //
	// === 	when there are no treasure cards in deck, thus requiring a			=== //
	// ===	reshuffle.																											=== //

	initializeGame(numPlayers, kingdomCards, randomSeed, &G);

	// replace all coppers in deck with estates
	for(int i = 0; i < G.deckCount[player]; i++){
		if(G.deck[player][i] = copper){
			G.deck[player][i] = estate;
		}
	}

	// put adventurer in the players hand
	gainCard(adventurer, &G, TO_HAND, player);

	// put 2 copper in discard
	gainCard(copper, &G, TO_DISCARD, player);
	gainCard(copper, &G, TO_DISCARD, player);

	// count treasure cards in hand before playing adventurer card
	treasure_cnt_before = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		tmp_card = G.hand[player][i];
		if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
			treasure_cnt_before++;
		}
	}

	// Play the adventurer card
	if(handCard(5, &G) == adventurer){
		playCard(5, 0, 0, 0, &G);
	}

	treasure_cnt_after = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		tmp_card = G.hand[player][i];
		if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
			treasure_cnt_after++;
		}
	}

	if(treasure_cnt_after != treasure_cnt_before + 2) {
		return -1;
	}


	return 0;
}
