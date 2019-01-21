#!/usr/bin/env bash
# Let's find zmq server and client socket processes on tcp:5555 ports
echo "Let's find zmq server and client socket processes on tcp:5555 ports"
sudo lsof -ti tcp:5555

# Now let's now force kill disconnected yet stuck zmq server and client socket processes on tcp:5555 port, if any:
echo "Let's now force kill disconnected yet stuck zmq server and client socket processes on tcp:5555 port, if any:" 
kill -9 $(sudo lsof -ti tcp:5555)

echo "Press any key to continue..." 
read 

# Let's clean kill run.sh processes, if any still stale processes still running in background:
echo "Let's clean kill run.sh processes, if any still stale processes still running in background:" 
kill -9 $(ps aux|grep "sh run.sh"|awk '{print $2}')

echo "Press any key to continue..." 
read 

# Let's find if any zombie processes that are running
echo "Let's find if any zombie processes that are running" 
ps aux | awk '"[Zz]" ~ $8 { printf("%s, PID = %d\n", $8, $2); }'

echo "Press any key to continue..." 
read 

echo "Note: this is not mandatory but sent2vec processes/threads are sometimes not killed from above commands in the script which could potentially turn into a Zombie process. Zombie processes are that are tracked by Operating system which have completed execution but are struck in Terminated state. So since the zombies are already dead, we cannot kill them. So find the parent of above zombie process pid by typing command: 'ps o ppid pidFromAboveCommand'. If you found there were any parent processes f zombie processes, kill them manually by using the command: 'kill -9 zombieParentPid'."

echo "Press any key to continue..." 
read 

# Let's find all Terminal application process pids, but just get one less than existing processes. And kill the pid.
echo "Let's find all Terminal application process pids, but just get one less than existing processes. And kill the pid." 
echo "Note: we are killing one less process every time: since we do not want to kill current kill/ps command we are executing now"
echo "Press any key to continue..." 
read 
kill -9 $(ps aux|grep "Terminal*"|grep -v grep|awk '{print $2}')
