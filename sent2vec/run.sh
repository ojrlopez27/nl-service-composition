#!/bin/sh
clear
echo "=========== Tokenizing the corpora methods .... ============"
#python wikiTokenize.py io-files/corporaMethods > io-files/corporaMethodsTokenized
echo "=========== Compiling the library (make) .... =============="
make
echo "=========== Sentence similarity for corpora .... ==========="
./fasttext nnSent twitter_bigrams.bin io-files/alexa/iterations/10/alexa5000.txt
#./fasttext nnSent torontobooks_unigrams.bin io-files/corporaMethodsTokenized 2
echo "=========== DONE! ==============="
