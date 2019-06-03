#ifndef _TESTSUITE_H
#define _TESTSUITE_H

#include "../../dominion.h"
#include "../../dominion_helpers.h"
#include "../../rngs.h"
// #include "../card.h"
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

#define UNITTEST1_DEBUG 0
#define UNITTEST2_DEBUG 0
#define UNITTEST3_DEBUG 0
#define UNITTEST4_DEBUG 0

#define NOISY_TEST 1
#define MAX_CYCLES 1000
#define TO_DISCARD 0
#define TO_DECK 1
#define TO_HAND 2
#define COPPER_VALUE 1
#define SILVER_VALUE 2
#define GOLD_VALUE 3

#define COPPER_COST 0
#define SILVER_COST 3
#define GOLD_COST 6
#define ESTATE_COST 2
#define DUCHY_COST 5
#define PROVINCE_COST 8
#define CURSE_COST 0
#define ADVENTURER_COST 6
#define COUNCIL_ROOM_COST 5
#define FEAST_COST 4
#define GARDEN_COST 4
#define MINE_COST 5
#define MONEYLENDER_COST 4
#define REMODEL_COST 4
#define SMITHY_COST 4
#define VILLAGE_COST 3
#define WOODCUTTER_COST 3
#define BARON_COST 4
#define GREAT_HALL_COST 3
#define MINION_COST 5
#define SHANTY_TOWN_COST 3
#define STEWARD_COST 3
#define TRIBUTE_COST 5
#define WISHING_WELL_COST 3
#define AMBASSADOR_COST 3
#define CUTPURSE_COST 4
#define EMBARGO_COST 2
#define OUTPOST_COST 5
#define SALVAGER_COST 4
#define SEA_HAG_COST 4
#define TREASURE_MAP_COST 4
#define ONETHOUSAND 1000


int countHandCoins(int player, struct gameState *game);

int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G);
int testShuffle(int *kingdomCards, int randomSeed);
int testSupply();
int testCardEffect(int);
int testAdventurerCard();
int testSmithyCard();
int testCutpurseCard();
int testSeahagCard();
int testDrawCard(struct gameState *pre);
int testgetCost();

#endif
