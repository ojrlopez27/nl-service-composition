/**
 * Copyright (c) 2016-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#include "fasttext.h"

#include <math.h>

#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <thread>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
#include <stdio.h>

#include <sys/stat.h>
#include <unistd.h>
#include <time.h>
#include <stdio.h>

struct tm *fileTime;
struct stat attrib;


namespace fasttext {

// FastText::MyTest() : quant_(false) {}


void FastText::testFile() {  

  // modified by Oscar:
  std::string inputFilename = "io-files/inputText";
  std::vector<std::string> insentences;
  std::vector<std::string> output;
  std::string sentence1;
  std::ifstream in3(inputFilename);
  std::ifstream in4(inputFilename, std::ifstream::in);
  int64_t m = 0;
  while (in3.peek() != EOF) {
    std::getline(in3, sentence1);
    insentences.push_back(sentence1);
    m++;
  }
  std::cout << "Number of sentences in the input file is " << m << "." << std::endl ;

  // std::cerr << "Query sentence? " << std::endl;
  int i = 0;
  while (in4.peek() != EOF) {  
    std::cout << "If user request is: [" << insentences[i] << "] then the most similar descriptions are: " << std::endl ;

    std::cout<< "Waiting for next sentence... " << '\n';
    in4.close();
    bool result = fileChanged();
    in4.open (inputFilename, std::ifstream::in);

    // std::cout<< "1. position: " << in4.tellg();
    // bool result = fileChanged();
    // std::cout<< "2. position: " << in4.tellg();
    // in4.seekg (0, in4.beg);
    // std::cout<< "3. position: " << in4.tellg();
    // std::cerr << "Query sentence? " << std::endl;
  }
}

bool FastText::fileChanged() {
  stat("io-files/inputText", &attrib);
  fileTime = gmtime(&(attrib.st_mtime));
  int f1_hour = fileTime->tm_hour;
  int f1_min = fileTime->tm_min;
  int f1_sec = fileTime->tm_sec;

  while(true){
    stat("io-files/inputText", &attrib);
    fileTime = gmtime(&(attrib.st_mtime));
    int f2_hour = fileTime->tm_hour;
    int f2_min = fileTime->tm_min;
    int f2_sec = fileTime->tm_sec;

    if(f1_hour == f2_hour && f1_min == f2_min && f1_sec == f2_sec){
      // do nothing
      std::cout<<"are equal" << '\n';
    }else{
      std::cout<<"are different: " << f1_hour << ":" << f1_min << ":" << f1_sec << "; " << f2_hour << ":" << f2_min << ":" << f2_sec << '\n';
      return true;
    }
    usleep(1000000);
  }
}

}
