#include "testSuite.h"

// ========================================================================== //
// === This unit test checks for correct functioning of smithyEffect(). 	=== //
// === It checks for the correct number of cards after playing the card   === //
// === as well as the correct number of cards in the deck and discard 		=== //
// === pile.																															=== //
// ========================================================================== //

int testSmithyCard(){
	struct gameState G;
	int numPlayers = 2;
	int player = 0;
	int handCntBefore, handCntAfter, deckCntBefore, deckCntAfter;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;
	int treasure_cnt_before, treasure_cnt_after, tmp_card;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, village, baron, great_hall};

	initializeGame(numPlayers, kingdomCards, randomSeed, &G);

	// put smithy card in players hand, now the 6th card, position 5
	gainCard(smithy, &G, TO_HAND, player);

	handCntBefore = numHandCards(&G);

	if(handCard(5, &G) == smithy){
		playCard(5, 0, 0, 0, &G);
	}
	else{
		fprintf(stderr, "cardtest2 failure\n");
		fprintf(stderr, "smithy not in hand\n");
		return -1;
	}

	handCntAfter = numHandCards(&G);

	if(CARDTEST2_DEBUG){
		printf("== inside testSmithyCard(): handCntBefore: %d\n", handCntBefore);
		printf("== inside testSmithyCard(): handCntAfter: %d\n", handCntAfter);
		printf("== inside testSmithyCard(): expected handCntAfter: %d\n", handCntBefore +2);
	}

	// Smithy adds 3 cards to hand, and discards smithy card. Leaving the player with net plus 2 cards.
	if(handCntAfter != handCntBefore +2){
		return -1;
	}



	return 0;
}

// struct gameState {
//   int numPlayers; //number of players
//   int supplyCount[treasure_map+1];  //this is the amount of a specific type of card given a specific number.
//   int embargoTokens[treasure_map+1];
//   int outpostPlayed;
//   int outpostTurn;
//   int whoseTurn;
//   int phase;
//   int numActions; /* Starts at 1 each turn */
//   int coins; /* Use as you see fit! */
//   int numBuys; /* Starts at 1 each turn */
//   int hand[MAX_PLAYERS][MAX_HAND];
//   int handCount[MAX_PLAYERS];
//   int deck[MAX_PLAYERS][MAX_DECK];
//   int deckCount[MAX_PLAYERS];
//   int discard[MAX_PLAYERS][MAX_DECK];
//   int discardCount[MAX_PLAYERS];
//   int playedCards[MAX_DECK];
//   int playedCardCount;
// };

// enum CARD
//   {curse = 0,
//    estate,
//    duchy,
//    province,
//
//    copper,
//    silver,
//    gold,
//
//    adventurer,
//    /* If no/only 1 treasure found, stop when full deck seen */
//    council_room,
//    feast, /* choice1 is supply # of card gained) */
//    gardens,
//    mine, /* choice1 is hand# of money to trash, choice2 is supply# of
// 	    money to put in hand */
//    remodel, /* choice1 is hand# of card to remodel, choice2 is supply# */
//    smithy,
//    village,
//
//    baron, /* choice1: boolean for discard of estate */
//    /* Discard is always of first (lowest index) estate */
//    great_hall,
//    minion, /* choice1:  1 = +2 coin, 2 = redraw */
//    steward, /* choice1: 1 = +2 card, 2 = +2 coin, 3 = trash 2 (choice2,3) */
//    tribute,
//
//    ambassador, /* choice1 = hand#, choice2 = number to return to supply */
//    cutpurse,
//    embargo, /* choice1 = supply# */
//    outpost,
//    salvager, /* choice1 = hand# to trash */
//    sea_hag,
//    treasure_map
//   };
