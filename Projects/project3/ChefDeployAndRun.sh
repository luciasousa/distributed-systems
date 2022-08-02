echo "Transfering data to the chef node."
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password scp dirChef.zip sd105@l040101-ws05.ua.pt:test/Restaurant
echo "Decompressing data sent to the chef node."
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'cd test/Restaurant ; unzip -uq dirChef.zip'
echo "Executing program at the chef node."
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'cd test/Restaurant/dirChef ; ./chef_com_d.sh'