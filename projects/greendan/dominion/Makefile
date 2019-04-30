CFLAGS= -Wall -fpic -coverage -lm -std=c99
ODIR=obj
TESTDIR=testSuite

rngs.o: rngs.h rngs.c
	gcc -c rngs.c -g  $(CFLAGS)

card.o: card.h card.c
	gcc -c card.c -g $(CFLAGS)

dominion.o: dominion.c dominion.h card.o rngs.o
	gcc -c dominion.c card.c -g  $(CFLAGS)

playdom: dominion.o playdom.c
	gcc -o playdom playdom.c -g dominion.o card.o rngs.o $(CFLAGS)
#To run playdom you need to entere: ./playdom <any integer number> like ./playdom 10*/
# testDrawCard: testDrawCard.c dominion.o rngs.o
	# gcc -o testDrawCard -g  testDrawCard.c dominion.o rngs.o $(CFLAGS)

test_main: unit_tests/test_main.c unit_tests/unit_tests.c dominion.o rngs.o
	gcc -o test_main -g unit_tests/test_main.c unit_tests/unit_tests.c dominion.o rngs.o $(CFLAGS)

# badTestDrawCard: badTestDrawCard.c dominion.o rngs.o
	# gcc -o badTestDrawCard -g  badTestDrawCard.c dominion.o rngs.o $(CFLAGS)

# testBuyCard: testDrawCard.c dominion.o rngs.o
	# gcc -o testDrawCard -g  testDrawCard.c dominion.o rngs.o $(CFLAGS)

cardtest1.o: testSuite/cardtest1.c testSuite/testSuite.h
	gcc -c testSuite/cardtest1.c -g  $(CFLAGS)

cardtest2.o: testSuite/cardtest2.c testSuite/testSuite.h
	gcc -c testSuite/cardtest2.c -g  $(CFLAGS)

cardtest3.o: testSuite/cardtest3.c testSuite/testSuite.h
	gcc -c testSuite/cardtest3.c -g  $(CFLAGS)

buildTestSuite: cardtest1.o cardtest2.o cardtest3.o dominion.o card.o rngs.o
	gcc -o runCardTest testSuite/testSuite.c cardtest1.o cardtest2.o cardtest3.o dominion.o card.o rngs.o -g $(CFLAGS)

interface.o: interface.h interface.c
	gcc -c interface.c -g  $(CFLAGS)

# runtests: testDrawCard
	# ./testDrawCard &> unittestresult.out
	# gcov dominion.c >> unittestresult.out
	# cat dominion.c.gcov >> unittestresult.out

# testMain: test_main
	# ./test_main

player: player.c interface.o
	gcc -o player player.c -g  dominion.o card.o rngs.o interface.o $(CFLAGS)

all: playdom player

clean:
	rm -f *.o runTests playdom.exe playdom player player.exe  *.gcov *.gcda *.gcno *.so *.out
