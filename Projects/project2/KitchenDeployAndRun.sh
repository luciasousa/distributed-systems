echo "Transfering data to the kitchen node."
sshpass -p "qwerty" ssh sd105@l040101-ws06.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws06.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirKitchen.zip sd105@l040101-ws06.ua.pt:test/Restaurant
echo "Decompressing data sent to the kitchen node."
sshpass -p "qwerty" ssh sd105@l040101-ws06.ua.pt 'cd test/Restaurant ; unzip -uq dirKitchen.zip'
echo "Executing program at the kitchen node."
sshpass -p "qwerty" ssh sd105@l040101-ws06.ua.pt 'cd test/Restaurant/dirKitchen ; java serverSide.main.ServerKitchen 22152 l040101-ws07.ua.pt 22153'
