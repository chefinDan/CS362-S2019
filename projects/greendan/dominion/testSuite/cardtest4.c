#include "testSuite.h"


// Card Description
// ========================================================================== //
// ===	The seahag card causes all other players to discard the top card 	=== //
// === of their deck and replace it with a curse card. This results in		=== //
// === every other player drawing a curse card on their next turn.        === //
// ========================================================================== //


// Test Description
// ========================================================================== //
// ===	This test will check for the correct number of cards in the				=== //
// === players discard pile, and check for a curse card on the top of     === //
// === their deck.																												=== //
// ========================================================================== //

int testSeahagCard(){

	struct gameState G;
	int numPlayers = 2;
	int player1 = 0;
	int player2 = 1;
	int testPass = 1;
	int handCntBefore, handCntAfter, deckCntBefore, deckCntAfter, playCardResult, deckCntBefore_p2, deckCntAfter_p2,
			foundCurse;
	int randomSeed = ((int)(Random()*MAX_HAND))%1000;
	int treasure_cnt_before, treasure_cnt_after, tmp_card;

	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
				 remodel, smithy, sea_hag, cutpurse, great_hall};

	initializeGame(numPlayers, kingdomCards, randomSeed, &G);

	gainCard(sea_hag, &G, TO_HAND, player1);

	if(handCard(5, &G) == sea_hag){
		playCardResult = playCard(5, -1, -1, -1, &G);
		if(playCardResult < 0){
			fprintf(stderr, "*** cardtest4 failed at function playCard\n");
			testPass = 0;
		}
	}
	else{
		fprintf(stderr, "*** cardtest4 failed at handCard(), sea_hag not in hand\n");
		testPass = 0;
	}

	endTurn(&G);

	foundCurse = 0;
	for(int i = 0; i < numHandCards(&G); i++){
		if(handCard(i, &G) == curse){
			foundCurse = 1;
			break;
		}
	}

	if(foundCurse == 0){
		fprintf(stderr, "*** cardtest4 failed at finding curse card in other players hand\n");
		testPass = 0;
	}

	if(testPass == 1){
		return 0;
	}
	return -1;


}

int main(int argc, char** argv){

	if(testSeahagCard() < 0){
		fprintf (stdout, "==== testSeahagCard() failed.\nSet CARDTEST4_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};

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
