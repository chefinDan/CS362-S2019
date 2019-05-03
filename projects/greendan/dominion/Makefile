CFLAGS= -fpic -coverage -lm -std=c99
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

# test_main: unit_tests/test_main.c unit_tests/unit_tests.c dominion.o rngs.o
# 	gcc -o test_main -g unit_tests/test_main.c unit_tests/unit_tests.c dominion.o rngs.o $(CFLAGS)

# badTestDrawCard: badTestDrawCard.c dominion.o rngs.o
	# gcc -o badTestDrawCard -g  badTestDrawCard.c dominion.o rngs.o $(CFLAGS)

# testBuyCard: testDrawCard.c dominion.o rngs.o
	# gcc -o testDrawCard -g  testDrawCard.c dominion.o rngs.o $(CFLAGS)

# cardtest1.o: testSuite/cardtest1.c testSuite/testSuite.h
# 	gcc -c testSuite/cardtest1.c -g  $(CFLAGS)
#
# cardtest2.o: testSuite/cardtest2.c testSuite/testSuite.h
# 	gcc -c testSuite/cardtest2.c -g  $(CFLAGS)
#
# cardtest3.o: testSuite/cardtest3.c testSuite/testSuite.h
# 	gcc -c testSuite/cardtest3.c -g  $(CFLAGS)
#
# cardtest4.o: testSuite/cardtest4.c testSuite/testSuite.h
# 	gcc -c testSuite/cardtest4.c -g  $(CFLAGS)

# runTestSuite: cardtest1.o cardtest2.o cardtest3.o cardtest4.o dominion.o card.o rngs.o
	# gcc -o runCardTest testSuite/testSuite.c cardtest1.o cardtest2.o cardtest3.o cardtest4.o dominion.o card.o rngs.o -g $(CFLAGS)

runTestSuite: testSuite/* dominion.o card.o rngs.o
	echo "Result for running card tests:" > alltests.log
	echo "cardtest1.c:" >> alltests.log
	gcc -o cardtest1.out testSuite/cardtest1.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest1.out >> alltests.log
	gcov -b dominion.c >> alltests.log

	echo "cardtest2.c:" >> alltests.log
	gcc -o cardtest2.out testSuite/cardtest2.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest2.out >> alltests.log 2>&1
	gcov -b dominion.c >> alltests.log

	echo "cardtest3.c:" >> alltests.log
	gcc -o cardtest3.out testSuite/cardtest3.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest3.out >> alltests.log 2>&1
	gcov -b dominion.c >> alltests.log;

	echo "cardtest4.c:" >> alltests.log
	gcc -o cardtest4.out testSuite/cardtest4.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest4.out >> alltests.log 2>&1
	gcov -b dominion.c >> alltests.log

	echo "\n\nResult for running unit tests:" >> alltests.log
	echo "unittest1.c:" >> alltests.log
	gcc -o unittest1.out testSuite/unittest1.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest1.out >> alltests.log
	gcov -b dominion.c >> alltests.log

	echo "unittest2.c:" >> alltests.log
	gcc -o unittest2.out testSuite/unittest2.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest2.out >> alltests.log
	gcov -b dominion.c >> alltests.log

	echo "unittest3.c:" >> alltests.log
	gcc -o unittest3.out testSuite/unittest3.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest3.out >> alltests.log
	gcov -b dominion.c >> alltests.log

	echo "unittest4.c:" >> alltests.log
	gcc -o unittest4.out testSuite/unittest4.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest4.out >> alltests.log
	gcov -b dominion.c >> alltests.log



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
	rm -f *.o runTests playdom.exe playdom player player.exe  *.gcov *.gcda *.gcno *.so *.out *.log
