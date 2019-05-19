#ifndef TESTSUITE_H
#define TESTSUITE_H

#include "../dominion.h"
#include "../dominion_helpers.h"
#include "../card.h"
#include "../rngs.h"
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

#define DEBUG 0
#define NOISY_TEST 1
#define MAX_CYCLES 1000
#define STR_MAX 64

enum RESULT {
   fail,
   pass,
   null,
};

struct Test {
   char testFor[STR_MAX];
   enum RESULT result;
};

int testDrawCard(struct gameState *pre);
int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G);
int testShuffle(int *kingdomCards, int randomSeed);
int testSupply();
int testCardEffect(int, struct gameState*);
int testAdventurerEffect();
int randomtestadventurer(struct gameState*);
int randomtestcard1(struct gameState*);
int randomtestcard2(struct gameState*);
int buildKingdomCards(int*, int);
int printData(unsigned test_cycle, struct gameState* preState, struct gameState* postState, unsigned player);
void check(struct Test*, const char*, int, int);

enum RESULT my_assertEqual(int, int);
#endif
