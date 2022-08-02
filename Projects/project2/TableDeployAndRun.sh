echo "Transfering data to the table node."
sshpass -p "qwerty" ssh sd105@l040101-ws04.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws04.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirTable.zip sd105@l040101-ws04.ua.pt:test/Restaurant
echo "Decompressing data sent to the table node."
sshpass -p "qwerty" ssh sd105@l040101-ws04.ua.pt 'cd test/Restaurant ; unzip -uq dirTable.zip'
echo "Executing program at the table node."
sshpass -p "qwerty" ssh sd105@l040101-ws04.ua.pt 'cd test/Restaurant/dirTable ; java serverSide.main.ServerTable 22151 l040101-ws07.ua.pt 22153'
