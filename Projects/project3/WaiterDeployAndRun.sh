echo "Transfering data to the waiter node."
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password scp dirWaiter.zip sd105@l040101-ws06.ua.pt:test/Restaurant
echo "Decompressing data sent to the waiter node."
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'cd test/Restaurant ; unzip -uq dirWaiter.zip'
echo "Executing program at the waiter node."
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'cd test/Restaurant/dirWaiter ; ./waiter_com_d.sh'