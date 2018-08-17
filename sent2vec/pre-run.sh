cd src/
echo "Executing setup.py for sent2vec/src"
python setup.py build_ext
echo "Executing pip install for sent2vec/src"
sudo -H pip install .
echo "completed!!"
cd ..
