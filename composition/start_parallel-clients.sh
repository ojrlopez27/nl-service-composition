#!/usr/bin/env bash
echo `which parallel`
export SHELL=$(type -p bash)
connect_client()
{
    java -jar ./build/libs/edu.cmu.inmind.services.composition-1.0.jar
}
read numOfClients
echo "Testing for ${numOfClients} client..."
export -f connect_client
#seq ${numOfClients} | parallel --eta -j100% connect_client -joblog
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
#seq 100|parallel --eta -j100% connect_client
N=4
for i in {1..1000}; do
(
    connect_client
)
#&if [[$(jobs -r -p|wc -l) -gt ${N}]] ; then
#wait -n
#fi
done
wait
echo "all done"