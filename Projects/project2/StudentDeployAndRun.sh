echo "Transfering data to the student node."
sshpass -p "qwerty" ssh sd105@l040101-ws08.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws08.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirStudent.zip sd105@l040101-ws08.ua.pt:test/Restaurant
echo "Decompressing data sent to the student node."
sshpass -p "qwerty" ssh sd105@l040101-ws08.ua.pt 'cd test/Restaurant ; unzip -uq dirStudent.zip'
echo "Executing program at the student node."
sshpass -p "qwerty" ssh sd105@l040101-ws08.ua.pt 'cd test/Restaurant/dirStudent ; java clientSide.main.ClientStudent l040101-ws07.ua.pt 22153 l040101-ws04.ua.pt 22151 l040101-ws05.ua.pt 22154'