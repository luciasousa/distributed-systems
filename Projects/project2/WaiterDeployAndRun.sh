echo "Transfering data to the waiter node."
sshpass -p "qwerty" ssh sd105@l040101-ws02.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws02.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirWaiter.zip sd105@l040101-ws02.ua.pt:test/Restaurant
echo "Decompressing data sent to the waiter node."
sshpass -p "qwerty" ssh sd105@l040101-ws02.ua.pt 'cd test/Restaurant ; unzip -uq dirWaiter.zip'
echo "Executing program at the waiter node."
sshpass -p "qwerty" ssh sd105@l040101-ws02.ua.pt 'cd test/Restaurant/dirWaiter ; java clientSide.main.ClientWaiter l040101-ws07.ua.pt 22153 l040101-ws06.ua.pt 22152 l040101-ws04.ua.pt 22151 l040101-ws05.ua.pt 22154'