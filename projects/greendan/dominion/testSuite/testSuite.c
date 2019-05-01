// #include "testSuite.h"
//
//
// // ========================================================================== //
// // ===				main driver for testsuite functions 												=== //
// // ========================================================================== //
// int main(int argc, char** argv){
//
// 	int i, n, r, numPlayers, drawResult, player, deckCount, discardCount, handCount;
// 	struct gameState G;
// 	// enum CARD;
//
// 	int kingdomCards[10] = {adventurer, council_room, feast, gardens, mine,
// 				 remodel, smithy, village, baron, great_hall};
//
// 	testCardEffect(adventurer);
// 	testCardEffect(smithy);
// 	testCardEffect(cutpurse);
// 	testCardEffect(sea_hag);
//
// 	testDrawCard(&G);
//
//
//
// 	printf ("ALL TESTS COMPLETE\n");
//
// 	exit(0);
// };
//
//
// // ========================================================================== //
// // === 												testCardEffect()														=== //
// // ===	This function takes a card to test and a gameState struct as			=== //
// // === 	params. Using a switch statement It calls the corresponding test 	=== //
// // ===	function for the card param. 																			=== //
// // ========================================================================== //
//
// int testCardEffect(int card){
// 	switch(card) {
// 		case curse:
// 			break;
//     case estate:
//       break;
//     case duchy:
//       break;
//     case province:
//       break;
//     case copper:
//       break;
//     case silver:
//       break;
//     case gold:
//       break;
// 		case adventurer:
// 			printf("%s\n", "= running: testAdventurerCard() =");
// 			if(testAdventurerCard() < 0){
// 				fprintf (stderr, "==== testAdventurerCard() failed.\nSet CARDTEST1_DEBUG to 1 for debug info\n");
// 				return 0;
// 			}
// 			break;
//
// 		case council_room:
//       break;
//     case feast:
//       break;
//     case gardens:
//       break;
//     case mine:
//       break;
//     case remodel:
//       break;
//     case smithy:
// 			printf("%s\n", "= running: testSmithyCard() =");
// 			if( testSmithyCard() < 0 ){
// 				fprintf(stderr, "==== testSmithyCard failed\nSet CARDTEST2_DEBUG to 1 for debug info\n");
// 				return 0;
// 			}
//       break;
//
//     case village:
//       break;
//     case baron:
//       break;
//     case great_hall:
//       break;
//     case minion:
//       break;
//     case steward:
//       break;
//     case tribute:
//       break;
//     case ambassador:
//       break;
//     case cutpurse:
// 			printf("%s\n", "= running: testCutpurseCard() ");
// 			if( testCutpurseCard() < 0 ){
// 				fprintf(stderr, "==== testCutpurseCard failed\nSet CARDTEST3_DEBUG to 1 for debug info\n");
// 				return 0;
// 			}
//       break;
//
//     case embargo:
//       break;
//     case outpost:
//       break;
//     case salvager:
//       break;
//     case sea_hag:
// 			printf("%s\n", "= running: testSeahagCard() ");
// 			if(testSeahagCard() < 0){
// 				fprintf(stderr, "==== testSeahagCard failed\nSet CARDTEST4_DEBUG to 1 for debug info\n");
// 			}
//       break;
//
//     case treasure_map:
//       break;
// 		default:
// 			return 0;
//
// 	};
//
// }
//
//
//
//
//
// int testInit(int numPlayers, int *kingdomCards, int randomSeed, struct gameState* G) {
//
//   // struct gameState G;
//
//   int i;
//
//   int start = -1;
//
//   // int k[10] = {adventurer, gardens, embargo, village, minion, mine, cutpurse,
// 	       // sea_hag, tribute, smithy};
//
//   // memset(&G, 'z', sizeof(struct gameState));
//
//   initializeGame(numPlayers, kingdomCards, randomSeed, G);
//
//
//   // printf ("Rough guide to locations in structure:\n");
//   // printf ("0: numPlayers\n");
//   // printf ("%ld: supplyCount[0]\n", ((long)&(G->supplyCount[0]))-((long)&G));
//   // printf ("%ld: embargoTokens[0]\n", ((long)&(G->embargoTokens[0]))-((long)&G));
//   // printf ("%ld: hand[0][0]\n", ((long)&(G->hand[0][0]))-(long)(&G));
//   // printf ("%ld: deck[0][0]\n", ((long)&(G->deck[0][0]))-(long)(&G));
//   // printf ("%ld: discard[0][0]\n", ((long)&(G->discard[0][0]))-(long)(&G));
//   // printf ("%ld: playerCards[0]\n", ((long)&(G->playedCards[0]))-(long)(&G));
//
//   // for (i = 0; i < sizeof(struct gameState); i++) {
//   //   if (((char*)&G)[i] == 'z') {
//   //     if (start == -1) {
// 	// start = i;
//   //     }
//   //   } else{
//   //     if (start != -1) {
// 	// if (start == (i-1)) {
// 	//   printf ("Byte %d uninitialized.\n", start);
// 	// } else {
// 	//   printf ("Bytes %d-%d uninitialized.\n", start, i-1);
// 	// }
// 	// start = -1;
//   //     }
//   //   }
//   // }
// 	//
//   // if (start != -1) {
//   //   if (start == (i-1)) {
//   //     printf ("Byte %d uninitialized.\n", start);
//   //   } else {
//   //     printf ("Bytes %d-%d uninitialized.\n", start, i-1);
//   //   }
//   // }
//
//   return 0;
// }
//
//
//
// int testShuffle(int *kingdomCards, int randomSeed) {
//   struct gameState G;
//   struct gameState G2;
//
//   // Initialize G.
// 	initializeGame(2, kingdomCards, randomSeed, &G);
//
//   memcpy (&G2, &G, sizeof(struct gameState));
//
//   int ret = shuffle(0,&G);
//
//   if (G.deckCount[0] > 0) {
//     assert (ret != -1);
//
//     qsort ((void*)(G.deck[0]), G.deckCount[0], sizeof(int), compare);
//     qsort ((void*)(G2.deck[0]), G2.deckCount[0], sizeof(int), compare);
//   } else
//     assert (ret == -1);
//
//   assert(memcmp(&G, &G2, sizeof(struct gameState)) == 0);
//
// };
//
//
//
//
// int testSupply() {
//
//   int r;
//
//   int k[10] = {adventurer, council_room, feast, gardens, mine,
// 	       remodel, smithy, village, baron, great_hall};
//
//   struct gameState G;
//
//   r = initializeGame(4, k, 1, &G);
//
//   printf ("initializeGame(4, k, 1, &G) = %d\n", r);
//   assert(r == 0);
//
//   r = supplyCount(adventurer, &G);
//   printf ("supplyCount(adventurer, &G) = %d\n", r);
//   assert(r == 10);
//
//   return 0;
// };
