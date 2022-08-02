echo "Transfering data to the general repository node."
sshpass -p "qwerty" ssh sd105@l040101-ws07.ua.pt 'mkdir -p test/Restaurant'
sshpass -p "qwerty" ssh sd105@l040101-ws07.ua.pt 'rm -rf test/Restaurant/*'
sshpass -p "qwerty" scp dirGeneralRepos.zip sd105@l040101-ws07.ua.pt:test/Restaurant
echo "Decompressing data sent to the general repository node."
sshpass -p "qwerty" ssh sd105@l040101-ws07.ua.pt 'cd test/Restaurant ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the server general repository."
sshpass -p "qwerty" ssh sd105@l040101-ws07.ua.pt 'cd test/Restaurant/dirGeneralRepos ; java serverSide.main.ServerGeneralRepos 22153'
echo "Server shutdown."
sshpass -p "qwerty" ssh sd105@l040101-ws07.ua.pt 'cd test/Restaurant/dirGeneralRepos ;'