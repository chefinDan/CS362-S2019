#include "testSuite.h"


// ========================================================================== //
// ===				main driver for testsuite functions 												=== //
// ========================================================================== //
int main(int argc, char** argv){

	int i, n, r, numPlayers, drawResult, player, deckCount, discardCount, handCount;
	struct gameState G;
	// enum CARD;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, baron, great_hall};

	testCardEffect(adventurer, &G);

	testDrawCard(&G);



	printf ("ALL TESTS COMPLETE\n");

	exit(0);
};


// ========================================================================== //
// === 												testCardEffect()														=== //
// ===	This function takes a card to test and a gameState struct as			=== //
// === 	params. Using a switch statement It calls the corresponding test 	=== //
// ===	function for the card param. 																			=== //
// ========================================================================== //

int testCardEffect(int card, struct gameState *G){
	switch(card) {
		case curse:
			break;
    case estate:
      break;
    case duchy:
      break;
    case province:
      break;
    case copper:
      break;
    case silver:
      break;
    case gold:
      break;
		case adventurer:
			printf("%s\n", "= running: testAdventurerEffect() =");
			if(testAdventurerEffect() < 0){
				printf ("%s\n", "***** testAdventurerEffect() failed.");
				return 0;
			}

		case council_room:
      break;
    case feast:
      break;
    case gardens:
      break;
    case mine:
      break;
    case remodel:
      break;
    case smithy:
      break;
    case village:
      break;
    case baron:
      break;
    case great_hall:
      break;
    case minion:
      break;
    case steward:
      break;
    case tribute:
      break;
    case ambassador:
      break;
    case cutpurse:
      break;
    case embargo:
      break;
    case outpost:
      break;
    case salvager:
      break;
    case sea_hag:
      break;
    case treasure_map:
      break;
		default:
			return 0;

	};

}


// ========================================================================== //
// === 												testDrawCard()															=== //
// ===	This function takes a player number and a gameState struct as			=== //
// === 	params. It draws a card and confirms asserts that the handCount 	=== //
// ===	properties of the gamestate struct are correctly 									=== //
// ===	incremented/decrimented.																					=== //
// ========================================================================== //

int testDrawCard(struct gameState *pre) {
  struct gameState post;
	int r, player, drawResult;

	printf("%s\n", "= running: testDrawCard() =");
	// Run testDrawCard() MAX_CYCLES times
	for (int i = 0; i < MAX_CYCLES; i++) {
		player = floor(Random() * 2); //randomly choose player <0 or 1>
		pre->deckCount[player] = floor(Random() * MAX_DECK); // give player a large deck <0-500>
		pre->discardCount[player] = floor(Random() * MAX_DECK); // give player a large discard pile <0-500>
		pre->handCount[player] = floor(Random() * MAX_HAND); // give player a large hand <0-500>

		//copy gameState post to mem location of local gameState pre
	  assert( memcpy(&post, pre, sizeof(struct gameState)) );

		if(DEBUG){
			printf ("drawCard PRE: player: %d, HC: %d, DeC: %d, DiC: %d\n",
			player, pre->handCount[player], pre->deckCount[player], pre->discardCount[player]);
		}

		r = drawCard (player, &post);
		if(r != 0)
			return -1;

		if(DEBUG)	{
			printf ("drawCard POST: player %d, HC: %d, DeC: %d, DiC %d\n",
			player, post.handCount[player], post.deckCount[player], post.discardCount[player]);
		}

		if(post.handCount[player] != (pre->handCount[player] + 1)){
			printf("%s\n", "==== handCount incriment failed");
			printf ("drawCard PRE: player: %d, HC: %d\n", player, pre->handCount[player]);
			printf ("drawCard POST: player %d, HC: %d\n", player, post.handCount[player]);
			return -1;
		}
	}

	return 0;
}



// ========================================================================== //
// === 												testAdventurerEffect()											=== //
// ===	This function tests the adventureCardEffect function. It returns -1 = //
// ===	if the test failed, else it returns 0. 														=== //																					=== //
// ========================================================================== //

int testAdventurerEffect(){
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




int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G) {

  // struct gameState G;

  int i;

  int start = -1;

  // int k[10] = {adventurer, gardens, embargo, village, minion, mine, cutpurse,
	       // sea_hag, tribute, smithy};

  // memset(&G, 'z', sizeof(struct gameState));

  initializeGame(numPlayers, kingdomCards, randomSeed, G);


  // printf ("Rough guide to locations in structure:\n");
  // printf ("0: numPlayers\n");
  // printf ("%ld: supplyCount[0]\n", ((long)&(G->supplyCount[0]))-((long)&G));
  // printf ("%ld: embargoTokens[0]\n", ((long)&(G->embargoTokens[0]))-((long)&G));
  // printf ("%ld: hand[0][0]\n", ((long)&(G->hand[0][0]))-(long)(&G));
  // printf ("%ld: deck[0][0]\n", ((long)&(G->deck[0][0]))-(long)(&G));
  // printf ("%ld: discard[0][0]\n", ((long)&(G->discard[0][0]))-(long)(&G));
  // printf ("%ld: playerCards[0]\n", ((long)&(G->playedCards[0]))-(long)(&G));

  // for (i = 0; i < sizeof(struct gameState); i++) {
  //   if (((char*)&G)[i] == 'z') {
  //     if (start == -1) {
	// start = i;
  //     }
  //   } else{
  //     if (start != -1) {
	// if (start == (i-1)) {
	//   printf ("Byte %d uninitialized.\n", start);
	// } else {
	//   printf ("Bytes %d-%d uninitialized.\n", start, i-1);
	// }
	// start = -1;
  //     }
  //   }
  // }
	//
  // if (start != -1) {
  //   if (start == (i-1)) {
  //     printf ("Byte %d uninitialized.\n", start);
  //   } else {
  //     printf ("Bytes %d-%d uninitialized.\n", start, i-1);
  //   }
  // }

  return 0;
}



int testShuffle(int *kingdomCards, int randomSeed) {
  struct gameState G;
  struct gameState G2;

  // Initialize G.
	initializeGame(2, kingdomCards, randomSeed, &G);

  memcpy (&G2, &G, sizeof(struct gameState));

  int ret = shuffle(0,&G);

  if (G.deckCount[0] > 0) {
    assert (ret != -1);

    qsort ((void*)(G.deck[0]), G.deckCount[0], sizeof(int), compare);
    qsort ((void*)(G2.deck[0]), G2.deckCount[0], sizeof(int), compare);
  } else
    assert (ret == -1);

  assert(memcmp(&G, &G2, sizeof(struct gameState)) == 0);

};




int testSupply() {

  int r;

  int k[10] = {adventurer, council_room, feast, gardens, mine,
	       remodel, smithy, village, baron, great_hall};

  struct gameState G;

  r = initializeGame(4, k, 1, &G);

  printf ("initializeGame(4, k, 1, &G) = %d\n", r);
  assert(r == 0);

  r = supplyCount(adventurer, &G);
  printf ("supplyCount(adventurer, &G) = %d\n", r);
  assert(r == 10);

  return 0;
};
