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
    - Modify run.sh bash script. If you want more precision (though it will be slower) then use wiki model, otherwise use toronto model
2. composition:
    - modify config.properties to your local folders
    - modify task-def-script to reflect abstract services
    - modify task-exec-script to reflect concrete services
  
## Execution
- There are two modes of execution:
    1. Batch: it reads both astract and concrete services from task-def-script and task-exec-script files respectively
    2. Step-by-step: it waits for user to type each entry on the console
- The execution mode can be changed on class CompositionLauncher, when InputController is instantiated
- First, execute bash script 'run.sh' under sent2vec, wait few minutes (depending on the size of the pre-trained model), and then run the Java project (though, if you run Java first, it will wait until sent2vec has established the connection)
- Once the sent2vec server is running, the pre-trained model is loaded only once, so you can stop and re-start the Java client as many times as you want without stopping the server (though sometimes the server is blocked, so you have to stopped)

## Playing with it
- QoS features (e.g., Connectivity, Battery Consumption, etc.) are static for now, but you can write a decay function for battery consumption and a random function for connectivity in CompositionController.addPhoneStatusToWM, just to see how different services are grounded
- Modify both task-def-script and task-exec-script files, and see what happens. However, try the provided example first
- Modify the thresholds on ServiceExecutor class. See explanation on method getServiceMethod
- Try both execution modes (batch, step-by-step)
- Create your own services following the provided ones as templates (using annotations and so on)
- Add synonyms in SemanticController (it will be helpful when retrieving data from working memory)



 
