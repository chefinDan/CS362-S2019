#include "testSuite.h"
// ========================================================================== //
// === 												testAdventurerEffect()											=== //
// ===	This function tests the adventureCardEffect function. It returns -1 = //
// ===	if the test failed, else it returns 0. 														=== //
// ========================================================================== //

int testAdventurerCard(){
  int treasure_cnt_before,
      treasure_cnt_after,
      tmp_card,
      playCardResult,
      deckCntBefore,
			deckCntAfter,
      V_cardCntBefore,
      V_cardCntAfter,
      hand_cnt_after,
      hand_cnt_before,
			discard_cnt_before,
      discard_cnt_after;

	struct gameState G;
	int numPlayers = 2;
	int testPass = 1;
	int player = 0;
	int toDeck = 1;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, baron, great_hall};


	// === 	Test for drawing correct number of treasure cards 							=== //
	// === under normal circumstances.																			=== //
		initializeGame(numPlayers,
                   kingdomCards,
                   randomSeed,
                   &G
    );

		// put 20 cards in the players deck, randomly picking from the 10 kingdom
		// cards and specifically putting silvers in the deck.
		for(int i = 0; i < 10; i++){
			gainCard(kingdomCards[ ((int)(Random()*MAX_HAND))%10 ],
               &G,
               TO_DECK,
               player
      );
			gainCard(silver,
               &G,
               TO_DECK,
               player
      );
		}
		// put adventurer in the players hand
		gainCard(adventurer,
             &G,
             TO_HAND,
             player
    );

		// count treasure cards in hand before playing adventurer card
		treasure_cnt_before = 0;
		for(int i = 0; i < numHandCards(&G); i++){
			tmp_card = G.hand[player][i];
			if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
				treasure_cnt_before++;
			}
		}

		deckCntBefore = G.deckCount[player];
		V_cardCntBefore = G.supplyCount[estate] +
                      G.supplyCount[duchy] +
                      G.supplyCount[province];

    hand_cnt_before = G.handCount[0];

	// Play the adventurer card
	if(handCard(5, &G) == adventurer){
		playCardResult = playCard(5, 0, 0, 0, &G);
		if(playCardResult < 0){
			fprintf(stdout, "*** cardtest1 failed at playCard()\n");
			testPass = 0;
		}
	}
	else{
		fprintf(stdout, "*** cardtest1 failed at handCard(), adventurer not in hand\n");
		testPass = 0;
	}

		// count treasure cards in hand after playing adventurer card
		treasure_cnt_after = 0;
		for(int i = 0; i < numHandCards(&G); i++){
			tmp_card = G.hand[player][i];
			if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
				treasure_cnt_after++;
			}
		}

		hand_cnt_after = G.handCount[0];
		deckCntAfter = G.deckCount[player];
		V_cardCntAfter = G.supplyCount[estate] +
                     G.supplyCount[duchy] +
                     G.supplyCount[province];

		// the treasure in hand should be the amount before playing the adventurer card
		// plus two. If not, test fails.
		if(CARDTEST1_DEBUG){
			printf("== inside testAdventurerCard(): treasure_cnt_before: %d\n", treasure_cnt_before);
			printf("== inside testAdventurerCard(): treasure_cnt_after: %d\n", treasure_cnt_after);
			printf("== inside testAdventurerCard(): expected treasure_cnt_after: %d\n", treasure_cnt_before +2);
		}
		if(treasure_cnt_after != treasure_cnt_before +2) {
			fprintf(stdout, "*** cardtest1 failed at treasure_cnt_after\n");
			testPass = 0;
		}

		// the players hand count should be the amount before playing the adventurer card
		// plus 1. This is due to the two treasure cards drawn and discarding the adventurer card.
		if(CARDTEST1_DEBUG){
			printf("== inside testAdventurerCard(): hand_cnt_before: %d\n", hand_cnt_before);
			printf("== inside testAdventurerCard(): hand_cnt_after: %d\n", hand_cnt_after);
			printf("== inside testAdventurerCard(): expected hand_cnt_after: %d\n", hand_cnt_before +2);
		}
		if(hand_cnt_after != hand_cnt_before +2) {
			fprintf(stdout, "*** cardtest1 failed at hand_cnt_after\n");
			testPass = 0;
		}

		// The vitory card count should remain unchnaged.
		if(CARDTEST1_DEBUG){
			printf("== inside testAdventurerCard(): V_cardCntBefore: %d\n", V_cardCntBefore);
			printf("== inside testAdventurerCard(): V_cardCntAfter: %d\n", V_cardCntAfter);
			printf("== inside testAdventurerCard(): expected deckCntAfter: %d\n", V_cardCntBefore);
		}
		if(V_cardCntBefore != V_cardCntAfter) {
			fprintf(stdout, "*** cardtest1 failed at V_cardCnt, victory card counts have changed.\n");
			testPass = 0;
		}

	// === 	Test for drawing correct number of treasure cards 							=== //
	// === 	when there are no treasure cards in deck, thus requiring a			=== //
	// ===	reshuffle.																											=== //

	initializeGame(numPlayers,
                 kingdomCards,
                 randomSeed,
                 &G
  );

	// replace all coppers in deck with estates
	for(int i = 0; i < G.deckCount[player]; i++){
		if(G.deck[player][i] = copper){
			G.deck[player][i] = estate;
		}
	}

	// put adventurer in the players hand
	gainCard(adventurer,
           &G,
           TO_HAND,
           player
  );

	// put 2 copper in discard
	gainCard(copper,
           &G,
           TO_DISCARD,
           player
  );
	gainCard(copper,
           &G,
           TO_DISCARD,
           player
  );

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
		playCard(5, -1, -1, -1, &G);
	}

	treasure_cnt_after = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		tmp_card = G.hand[player][i];
		if(tmp_card == copper || tmp_card == silver || tmp_card == gold){
			treasure_cnt_after++;
		}
	}

	if(treasure_cnt_after != treasure_cnt_before + 2) {
		fprintf(stdout, "*** cardtest1 failed at treasure_cnt Test 2.\n");
		testPass = 0;
	}

	if(testPass == 1){
		return 0;
	}
	return -1;

}

int main(int argc, char** argv){

	if(testAdventurerCard() < 0){
		fprintf (stdout, "==== testAdventurerCard() failed.\nSet CARDTEST1_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
