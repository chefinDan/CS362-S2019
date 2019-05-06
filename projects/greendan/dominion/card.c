
#include "card.h"

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int adventurerEffect(struct gameState *state, int currentPlayer){

	int drawntreasure, cardDrawn, tmpCnt;
	int temphand[MAX_HAND];

	drawntreasure = tmpCnt = 0;

	while(drawntreasure<2){ //card effect is complete once 2 treasure has been drawn

		//if the deck is empty we need to shuffle, discard, and add to deck
		if (state->deckCount[currentPlayer] <1){
			shuffle(currentPlayer, state);
		}

		drawCard(currentPlayer, state);
		cardDrawn = state->hand[currentPlayer][state->handCount[currentPlayer]-1]; //top card of hand is most recently drawn card.

		if(DEBUG)
			printf("cardDrawn: %d\n", cardDrawn);

		if(cardDrawn == copper || cardDrawn == silver || cardDrawn == gold)
			drawntreasure++;
		else{
			temphand[tmpCnt]=cardDrawn;
			state->handCount[currentPlayer]--; //this should just remove the top card (the most recently drawn one).
			tmpCnt++;
		}

	}

	while(tmpCnt - 1 >= 0){
		 state->discard[currentPlayer][state->discardCount[currentPlayer]++] = temphand[tmpCnt-1]; // discard all cards in play that have been drawn
		 tmpCnt = tmpCnt-1;
	}
	return 0;
}


int council_roomEffect(struct gameState *state, int currentPlayer, int handPos){
	int i;
	//+4 Cards
	for (i = 0; i < 4; i++){
		drawCard(currentPlayer, state);
	}

	//+1 Buy       *******BUG********
	// state->numBuys++;

	//Each other player draws a card
	for (i = 0; i < state->numPlayers; i++){
		if ( i != currentPlayer ){
			drawCard(i, state);
		}
	}

	//put played card in played card pile
	discardCard(handPos, currentPlayer, state, 0);
	return 0;

}


int smithyEffect(int handPos, int currentPlayer, struct gameState *state){
	int i;
	//+3 Cards
	for (i = 0; i < 4; i++) {
		 drawCard(currentPlayer, state);
	}

	//discard card from hand
	discardCard(handPos, currentPlayer, state, 0);
	return 0;
}


int villageEffect(int currentPlayer, struct gameState *state, int handPos){
	//+1 Card
	drawCard(currentPlayer, state);

	//+2 Actions        ******* BUG ***********
	state->numActions = state->numActions + 1;

	//discard played card from hand
	discardCard(handPos, currentPlayer, state, 0);
	return 0;
}



int stewardEffect(int currentPlayer, struct gameState* state, int choice1, int choice2, int choice3, int handPos){
	if (choice1 == 1){
		//+2 cards
		drawCard(currentPlayer, state);
		drawCard(currentPlayer, state);
	}
	else if (choice1 == 2){
		//+2 coins
		state->coins = state->coins + 2;
	}
	else{
		//trash 2 cards in hand
		discardCard(choice2, currentPlayer, state, 1);
		// discardCard(choice3, currentPlayer, state, 1);
		discardCard(handPos, currentPlayer, state, 1);
	}

	//discard card from hand
	// discardCard(handPos, currentPlayer, state, 0);
	discardCard(choice3, currentPlayer, state, 1);
	return 0;
}

int cutpurseEffect(int* bonus, int currentPlayer, struct gameState* state, int handPos){
  int i, j, k;

  *bonus = 2;
  updateCoins(currentPlayer, state, *bonus);

  for (i = 0; i < state->numPlayers; i++){
		if (i != currentPlayer){
    	for (j = 0; j < state->handCount[i]; j++){
  			if (state->hand[i][j] == copper){
      		discardCard(j, i, state, 0);
      		break;
    		}
  			if (j == state->handCount[i]){
      		for(k = 0; k < state->handCount[i]; k++){
	  				if (DEBUG)
	    				printf("Player %d reveals card number %d\n", i, state->hand[i][k]);
					}
      		break;
    		}
			}
  	}
	}

  //discard played card from hand
  discardCard(handPos, currentPlayer, state, 0);
}


int testSeaHagEffect(struct gameState* state, int currentPlayer){
  int i;

  for (i = 0; i < state->numPlayers; i++){
    if (i != currentPlayer){
      state->discard[i][state->discardCount[i]] = state->deck[i][state->deckCount[i]];
      state->deckCount[i]--;
      state->discardCount[i]++;
      state->deck[i][state->deckCount[i]] = curse;//Top card now a curse
      state->deckCount[i]++;
    }
  }

  return 0;
}
