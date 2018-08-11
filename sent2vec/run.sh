#!/bin/sh
clear
echo "=========== Tokenizing the corpora methods .... ============"
/usr/local/bin/python3.6-32 wikiTokenize.py corporaMethods > corporaMethodsTokenized
echo "=========== Compiling the library (make) .... =============="
make
echo "=========== Sentence similarity for corpora .... ==========="
./fasttext nnSent wiki_bigrams.bin corporaMethodsTokenized 2
echo "=========== DONE! ==============="
