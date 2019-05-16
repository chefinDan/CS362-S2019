#ifndef TESTSUITE_H
#define TESTSUITE_H

#include "../dominion.h"
#include "../dominion_helpers.h"
#include "../rngs.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

#define DEBUG 0
#define NOISY_TEST 1
#define MAX_CYCLES 2000

// const char* allCards[] = {
// 											 "curse", "estate", "duchy", "province", "copper", "silver",
// 											 "gold", "adventurer", "council_room", "feast", "gardens",
// 											 "mine", "remodel", "smithy", "village", "baron", "great_hall",
// 											 "minion", "steward", "tribute", "ambassador", "cutpurse",
// 											 "embargo", "outpost", "salvager", "sea_hag", "treasure_map"
// 											};

int testDrawCard(struct gameState *pre);
int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G);
int testShuffle(int *kingdomCards, int randomSeed);
int testSupply();
int testCardEffect(int, struct gameState*);
int testAdventurerEffect();
int randomtestadventurer(struct gameState*);

#endif
