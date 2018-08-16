# <OPTIONAL_NAME> Natural-Language-based Service Composition

This project is composed of two sub-projects:
1. sent2vec: sentence embeddings based on a unsupervised version of FastText, and an extension of word2vec (CBOW) to sentences. (C++ code + Python wrappers)
2. composition: Java project that defines (further portable to Android):
    - Annotated abstract services (APIS) 
    - Annotated concrete services
    - Compositional Mechanisms (only pipeline for now)
    - Name Entity Recognition (based on Stanford NER Libraries) - it recognizes places, names, organizations, numbers, dates, and money
    - ZMQ communication with sent2vec server
    - easy-rule engine and MVEL rules for composition
    - POJOs

## Setup
1. sent2vec: follow the instructions on the README file under the sent2vec folder, and:
    - Download Cython. If using mac, pip install Cython
    - install module globally:
	      - /usr/local/bin/python2.7 setup.py build_ext
	      - sudo -H pip install .
    - Download pre-trained models from https://github.com/epfml/sent2vec, more specifically, wiki bigrams (16GB) and toronto unigrams (2GB). Put them under the folder sent2vec
    - Modify run.sh bash script. If you want more precision (though it will be slower) then use wiki model, otherwise use toronto model which is faster
2. composition:
    - modify config.properties to your local folders
    - modify task-def-script to reflect the high level description of a task or plan (e.g., plan a trip to a place on a range of dates)
    - modify task-exec-script to reflect a contextualization of the high-level description (e.g., plan a trip to Boston from August 29 to September 11)
  
## Execution
- There are two modes of execution:
    1. Batch: it reads both high-level and contextualized descriptions from task-def-script and task-exec-script files respectively
    2. Step-by-step: it waits for user to type each entry on the console rather than readin them from the files
- The execution mode can be changed on class CompositionLauncher, when InputController is instantiated
- First, execute bash script 'run.sh' under sent2vec, wait few minutes (depending on the size of the pre-trained model), and then run the Java project (though, if you run Java first, it will wait until sent2vec has established the connection)
- Once the sent2vec server is running, the pre-trained model is loaded only once, so you can stop and re-start the Java client as many times as you want without stopping the server (though sometimes the server gets blocked, so you have to stop it)

## Playing with it
- QoS features (e.g., Connectivity, Battery Consumption, etc.) are static for now, but you can write a decay function for battery consumption and a random function for connectivity in CompositionController.addPhoneStatusToWM, just to see how different services are grounded
- Modify both task-def-script and task-exec-script files, and see what happens. However, try the provided example first
- Modify the thresholds on ServiceExecutor class. See explanation on method getServiceMethod
- Try both execution modes (batch, step-by-step)
- Create your own services following the provided ones as templates (using annotations and so on)
- Add synonyms in SemanticController (it will be helpful when retrieving data from working memory)

## TO-DO
- There are specific TODO tags in the Java code that you can reference, however, the current state can allow us do some experiments and present it as a demo.
- Port the Java code to Android
- Replace SemanticController by a proper semantic relatedness/synonym mechanism (e.g., Word2Vec, WordNet, etc.)
- Train our own models? why not...
- Not sure if a cost fucntion based on similarity and QoS features should be included, or just keep using the similarity thresholds and delta
- Currently composition is linear (pipeline style) but we need to introduce control structures (e.g., loops, conditionals, etc.). It would be interesting to convert natural language commands into rules using MVEL rules (or maybe LIA/Sugilite)
- Mechanism for mapping entity extraction with parameters is pretty basic, we should improve it
- Running multiple times the same experiment produces different similarities (sent2vec) so sometimes the service grounding cannot be performed since similarities are really low.... not sure why this happens (is randomly and rarely)
- etc.


 
