#ifndef TESTSUITE_H
#define TESTSUITE_H

#include "../dominion.h"
#include "../dominion_helpers.h"
#include "../rngs.h"
#include "../card.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <errno.h> /* errno */

#define CARDTEST1_DEBUG 0
#define CARDTEST2_DEBUG 0
#define CARDTEST3_DEBUG 0
#define CARDTEST4_DEBUG 0

#define NOISY_TEST 1
#define MAX_CYCLES 2000
#define TO_DISCARD 0
#define TO_DECK 1
#define TO_HAND 2


int testDrawCard(struct gameState *pre);
int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G);
int testShuffle(int *kingdomCards, int randomSeed);
int testSupply();
int testCardEffect(int);
int testAdventurerCard();
int testSmithyCard();
int testCutpurseCard();

#endif
