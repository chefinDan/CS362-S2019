#ifndef CARD_H
#define CARD_H

#include "dominion.h"
#include "dominion_helpers.h"

int adventurerEffect(struct gameState *state, int currentPlayer);
int council_roomEffect(struct gameState *state, int currentPlayer, int handPos);
int smithyEffect(int handPos, int currentPlayer, struct gameState *state);
int villageEffect(int currentPlayer, struct gameState *state, int handPos);
int stewardEffect(int currentPlayer, struct gameState *state, int choice1, int choice2, int choice3, int handPos);


#endif
