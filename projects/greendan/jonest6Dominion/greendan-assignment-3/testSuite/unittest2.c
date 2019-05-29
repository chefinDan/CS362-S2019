#include "testSuite.h"

int testgetCost(){
	int testPass = 1;
	int invalidCard;

	// Positive test for correct cost of each kingdom card
	for(int card = curse; card <= treasure_map; card++){

		switch (card){
			case curse:
				testPass = (getCost(card) == CURSE_COST);
				break;
	    case estate:
				testPass = (getCost(card) == ESTATE_COST);
				break;
	    case duchy:
				testPass = (getCost(card) == DUCHY_COST);
				break;
	    case province:
				testPass = (getCost(card) == PROVINCE_COST);
				break;
	    case copper:
				testPass = (getCost(card) == COPPER_COST);
				break;
	    case silver:
				testPass = (getCost(card) == SILVER_COST);
				break;
	    case gold:
				testPass = (getCost(card) == GOLD_COST);
				break;
	    case adventurer:
				testPass = (getCost(card) == ADVENTURER_COST);
				break;
	    case council_room:
				testPass = (getCost(card) == COUNCIL_ROOM_COST);
				break;
	    case feast:
				testPass = (getCost(card) == FEAST_COST);
				break;
	    case gardens:
				testPass = (getCost(card) == GARDEN_COST);
				break;
	    case mine:
				testPass = (getCost(card) == MINE_COST);
				break;
	    case remodel:
				testPass = (getCost(card) == REMODEL_COST);
				break;
	    case smithy:
				testPass = (getCost(card) == SMITHY_COST);
				break;
	    case village:
				testPass = (getCost(card) == VILLAGE_COST);
				break;
	    case baron:
				testPass = (getCost(card) == BARON_COST);
				break;
	    case great_hall:
				testPass = (getCost(card) == GREAT_HALL_COST);
				break;
	    case minion:
				testPass = (getCost(card) == MINION_COST);
				break;
	    case steward:
				testPass = (getCost(card) == STEWARD_COST);
				break;
	    case tribute:
				testPass = (getCost(card) == TRIBUTE_COST);
				break;
	    case ambassador:
				testPass = (getCost(card) == AMBASSADOR_COST);
				break;
	    case cutpurse:
				testPass = (getCost(card) == CUTPURSE_COST);
				break;
	    case embargo:
				testPass = (getCost(card) == EMBARGO_COST);
				break;
	    case outpost:
				testPass = (getCost(card) == OUTPOST_COST);
				break;
	    case salvager:
				testPass = (getCost(card) == SALVAGER_COST);
				break;
	    case sea_hag:
				testPass = (getCost(card) == SEA_HAG_COST);
				break;
	    case treasure_map:
				testPass = (getCost(card) == TREASURE_MAP_COST);
				break;
		}

		if(testPass != 1){
			return -1;
		}
	}

	// Negative test for invalid cards
	for(int i = 0; i < 1000; i++){
		invalidCard = (int)(Random()*10000)%500;
		if(invalidCard < 0 || invalidCard > 26){
			testPass = (getCost(invalidCard) == -1);
			if(testPass != 1){
				return -1;
			}
		}
	}

	return 0;
}


int main(int argc, char** argv){
	struct gameState preState;

	if(testgetCost() < 0){
		fprintf (stdout, "==== unittest2 failed.\nSet UNITTEST2_DEBUG to 1 for debug info\n");
	}
	else{
		printf("%s\n", "All tests PASS");
	}

	return 0;
};
