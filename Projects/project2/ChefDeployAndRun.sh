echo "Transfering data to the chef node."
sshpass -p "qwerty" ssh sd105@l040101-ws03.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws03.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirChef.zip sd105@l040101-ws03.ua.pt:test/Restaurant
echo "Decompressing data sent to the chef node."
sshpass -p "qwerty" ssh sd105@l040101-ws03.ua.pt 'cd test/Restaurant ; unzip -uq dirChef.zip'
echo "Executing program at the chef node."
sshpass -p "qwerty" ssh sd105@l040101-ws03.ua.pt 'cd test/Restaurant/dirChef ; java clientSide.main.ClientChef l040101-ws07.ua.pt 22153 l040101-ws06.ua.pt 22152 l040101-ws05.ua.pt 22154'
