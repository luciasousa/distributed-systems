xterm  -T "RMI registry" -hold -e " cd ~/test/Restaurant/dirRMIRegistry; ./set_rmiregistry_alt.sh 22156" &
sleep 5
xterm  -T "Registry" -hold -e "cd ~/test/Restaurant/dirRegistry; ./registry_com_alt.sh rsa" &
sleep 3
xterm  -T "General Repository" -hold -e "cd ~/test/Restaurant/dirGeneralRepos; ./repos_com_alt.sh rsa" &
sleep 2
xterm  -T "Kitchen" -hold -e "cd ~/test/Restaurant/dirKitchen; ./kitchen_com_alt.sh rsa" &
xterm  -T "Table" -hold -e "cd ~/test/Restaurant/dirTable; ./table_com_alt.sh rsa" &
sleep 2
xterm  -T "Bar" -hold -e "cd ~/test/Restaurant/dirBar; ./bar_com_alt.sh rsa" &
sleep 2
xterm  -T "Chef" -hold -e "cd ~/test/Restaurant/dirChef; ./chef_com_alt.sh rsa" &
xterm  -T "Waiter" -hold -e "cd ~/test/Restaurant/dirWaiter; ./waiter_com_alt.sh rsa" &
xterm  -T "Student" -hold -e "cd ~/test/Restaurant/dirStudent; ./student_com_alt.sh rsa" &
