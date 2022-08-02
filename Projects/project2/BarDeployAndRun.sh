echo "Transfering data to the bar node."
sshpass -p "qwerty" ssh sd105@l040101-ws05.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws05.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirBar.zip sd105@l040101-ws05.ua.pt:test/Restaurant
echo "Decompressing data sent to the bar node."
sshpass -p "qwerty" ssh sd105@l040101-ws05.ua.pt 'cd test/Restaurant ; unzip -uq dirBar.zip'
echo "Executing program at the bar node."
sshpass -p "qwerty" ssh sd105@l040101-ws05.ua.pt 'cd test/Restaurant/dirBar ; java serverSide.main.ServerBar 22154 l040101-ws07.ua.pt 22153 l040101-ws04.ua.pt 22151 l040101-ws06.ua.pt 22152'