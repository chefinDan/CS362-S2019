#ifndef TESTSUITE_H
#define TESTSUITE_H

#include "../dominion.h"
#include "../dominion_helpers.h"
#include "../card.h"
#include "../rngs.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

#define DEBUG 0
#define NOISY_TEST 1
#define MAX_CYCLES 1000

int testDrawCard(struct gameState *pre);
int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G);
int testShuffle(int *kingdomCards, int randomSeed);
int testSupply();
int testCardEffect(int, struct gameState*);
int testAdventurerEffect();
int randomtestadventurer(struct gameState*);
int randomtestcard1(struct gameState*);
int buildKingdomCards(int*, int);
int printData(struct gameState* preState, struct gameState* postState, unsigned player);

#endif
