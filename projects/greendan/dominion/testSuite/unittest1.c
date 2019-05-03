#include "testSuite.h"

// ========================================================================== //
// === 												testDrawCard()															=== //
// ===	This function takes a player number and a gameState struct as			=== //
// === 	params. It draws a card and confirms asserts that the handCount 	=== //
// ===	properties of the gamestate struct are correctly 									=== //
// ===	incremented/decrimented.																					=== //
// ========================================================================== //

int testDrawCard(struct gameState *pre) {
  int player, drawResult;
  struct gameState post;

  int testPass = 1;
  memset(&post, '\0', sizeof(struct gameState));

	// Run testDrawCard() MAX_CYCLES times
	for (int i = 0; i < MAX_CYCLES; i++) {
		player = floor(Random() * 2); //randomly choose player <0 or 1>
		pre->deckCount[player] = floor(Random() * MAX_DECK); // give player a large deck <0-500>
		pre->discardCount[player] = floor(Random() * MAX_DECK); // give player a large discard pile <0-500>
		pre->handCount[player] = floor(Random() * MAX_HAND); // give player a large hand <0-500>

		//copy gameState post to mem location of local gameState pre
	  assert( memcpy(&post, pre, sizeof(struct gameState)) );

		if(UNITTEST1_DEBUG){
			printf ("drawCard PRE: player: %d, HC: %d, DeC: %d, DiC: %d\n",
			player, pre->handCount[player], pre->deckCount[player], pre->discardCount[player]);
		}

		drawResult = drawCard (player, &post);
		if(drawResult != 0){
			testPass = 0;
		}

		if(UNITTEST1_DEBUG)	{
			printf ("drawCard POST: player %d, HC: %d, DeC: %d, DiC %d\n",
			player, post.handCount[player], post.deckCount[player], post.discardCount[player]);
		}

		// check that player that drew card has one more card than before
		if(UNITTEST1_DEBUG){
			printf ("drawCard PRE: player: %d, HC: %d\n", player, pre->handCount[player]);
			printf ("drawCard POST: player %d, HC: %d\n", player, post.handCount[player]);
		}
		if(post.handCount[player] != (pre->handCount[player] + 1)){
			fprintf(stderr, "*** unittest1 failed at handCount incriment after drawCard()\n");
			testPass = 0;
		}
	}

	if(testPass == 1){
		return 0;
	}
	return -1;

}

int main(int argc, char** argv){
	struct gameState preState;

	if(testDrawCard(&preState) < 0){
		fprintf (stdout, "==== unittest1 failed.\nSet UNITTEST1_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
