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

cardtests: testSuite/* dominion.o card.o rngs.o
	gcc -o _cardtest1 testSuite/cardtest1.c dominion.o card.o rngs.o $(CFLAGS)
	gcc -o _cardtest2 testSuite/cardtest2.c dominion.o card.o rngs.o $(CFLAGS)
	gcc -o _cardtest3 testSuite/cardtest3.c dominion.o card.o rngs.o $(CFLAGS)
	gcc -o _cardtest4 testSuite/cardtest4.c dominion.o card.o rngs.o $(CFLAGS)
	echo "\ncardtest1.c:" >> cardTest_results.out
	./_cardtest1 >> cardTest_results.out
	echo "\n\ncardtest2.c:" >> cardTest_results.out
	./_cardtest2 >> cardTest_results.out
	echo "\n\ncardtest3.c:" >> cardTest_results.out
	./_cardtest3 >> cardTest_results.out
	echo "\n\ncardtest4.c:" >> cardTest_results.out
	./_cardtest4 >> cardTest_results.out
	echo "\n\n gcov results" >> cardTest_results.out
	gcov -bf card.c >> cardTest_results.out

unittests: testSuite/* dominion.o card.o rngs.o
	gcc -o _unittest1 testSuite/unittest1.c dominion.o card.o rngs.o $(CFLAGS)
	gcc -o _unittest2 testSuite/unittest2.c dominion.o card.o rngs.o $(CFLAGS)
	gcc -o _unittest3 testSuite/unittest3.c dominion.o card.o rngs.o $(CFLAGS)
	gcc -o _unittest4 testSuite/unittest4.c dominion.o card.o rngs.o $(CFLAGS)
	echo "\nunittest1.c:" >> unitTest_results.out
	./_unittest1 >> unitTest_results.out
	echo "\n\nunittest2.c:" >> unitTest_results.out
	./_unittest2 >> unitTest_results.out
	echo "\n\nunittest3.c:" >> unitTest_results.out
	./_unittest3 >> unitTest_results.out
	echo "\n\nunittest4.c:" >> unitTest_results.out
	./_unittest4 >> unitTest_results.out
	echo "\n\n gcov results" >> unitTest_results.out
	gcov -bf dominion.c >> unitTest_results.out
	gcov -bf card.c >> unitTest_results.out


unittestresults: testSuite/* dominion.o card.o rngs.o
	echo "Result for running card tests:" > unittestresults.out
	echo "cardtest1.c:" >> unittestresults.out
	gcc -o cardtest1.out testSuite/cardtest1.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest1.out >> unittestresults.out
	gcov -b dominion.c >> unittestresults.out

	echo "cardtest2.c:" >> unittestresults.out
	gcc -o cardtest2.out testSuite/cardtest2.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest2.out >> unittestresults.out 2>&1
	gcov -b dominion.c >> unittestresults.out

	echo "cardtest3.c:" >> unittestresults.out
	gcc -o cardtest3.out testSuite/cardtest3.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest3.out >> unittestresults.out 2>&1
	gcov -b dominion.c >> unittestresults.out;

	echo "cardtest4.c:" >> unittestresults.out
	gcc -o cardtest4.out testSuite/cardtest4.c dominion.o card.o rngs.o $(CFLAGS)
	./cardtest4.out >> unittestresults.out 2>&1
	gcov -b dominion.c >> unittestresults.out

	echo "\n\nResult for running unit tests:" >> unittestresults.out
	echo "unittest1.c:" >> unittestresults.out
	gcc -o unittest1.out testSuite/unittest1.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest1.out >> unittestresults.out
	gcov -b dominion.c >> unittestresults.out

	echo "unittest2.c:" >> unittestresults.out
	gcc -o unittest2.out testSuite/unittest2.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest2.out >> unittestresults.out
	gcov -b dominion.c >> unittestresults.out

	echo "unittest3.c:" >> unittestresults.out
	gcc -o unittest3.out testSuite/unittest3.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest3.out >> unittestresults.out
	gcov -b dominion.c >> unittestresults.out

	echo "unittest4.c:" >> unittestresults.out
	gcc -o unittest4.out testSuite/unittest4.c dominion.o card.o rngs.o $(CFLAGS)
	./unittest4.out >> unittestresults.out
	gcov -b dominion.c >> unittestresults.out

runRandomTest: testSuite/* dominion.o card.o rngs.o
	gcc -o rand.out testSuite/randomtests.c dominion.o card.o rngs.o $(CFLAGS)
	./rand.out
	gcov -bf dominion.c >> randtests.log

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
	rm -f *.o runTests playdom.exe playdom player player.exe  *.gcov *.gcda *.gcno *.so *.out *.log _*
