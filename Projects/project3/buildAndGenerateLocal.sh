rm -rf dir*/*/
echo "Compiling source code."
javac -cp genclass.jar */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  RMI registry"
rm -rf dirRMIRegistry/interfaces dirRMIRegistry/commInfra
mkdir -p dirRMIRegistry/interfaces dirRMIRegistry/commInfra
cp interfaces/*.class dirRMIRegistry/interfaces
cp commInfra/*.class dirRMIRegistry/commInfra
echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces dirRegistry/commInfra
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces dirRegistry/commInfra
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces
cp commInfra/*.class dirRegistry/commInfra
echo "  General Repository of Information"
rm -rf dirGeneralRepos/serverSide dirGeneralRepos/clientSide dirGeneralRepos/interfaces dirGeneralRepos/commInfra
mkdir -p dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/objects dirGeneralRepos/interfaces \
         dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerGeneralRepos.class dirGeneralRepos/serverSide/main
cp serverSide/objects/GeneralRepos.class dirGeneralRepos/serverSide/objects
cp interfaces/Register.class interfaces/GeneralReposInterface.class dirGeneralRepos/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/ChefStates.class clientSide/entities/StudentStates.class dirGeneralRepos/clientSide/entities
cp commInfra/*.class dirGeneralRepos/commInfra
echo "  Bar"
rm -rf dirBar/serverSide dirBar/clientSide dirBar/interfaces dirBar/commInfra
mkdir -p dirBar/serverSide dirBar/serverSide/main dirBar/serverSide/objects dirBar/interfaces \
         dirBar/clientSide dirBar/clientSide/entities dirBar/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerBar.class dirBar/serverSide/main
cp serverSide/objects/Bar.class serverSide/objects/Table.class serverSide/objects/Kitchen.class dirBar/serverSide/objects
cp interfaces/*.class dirBar/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class clientSide/entities/ChefStates.class dirBar/clientSide/entities
cp commInfra/*.class dirBar/commInfra
echo "  Table"
rm -rf dirTable/serverSide dirTable/clientSide dirTable/interfaces dirTable/commInfra
mkdir -p dirTable/serverSide dirTable/serverSide/main dirTable/serverSide/objects dirTable/interfaces \
         dirTable/clientSide dirTable/clientSide/entities dirTable/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerTable.class dirTable/serverSide/main
cp serverSide/objects/Table.class dirTable/serverSide/objects
cp interfaces/*.class dirTable/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class dirTable/clientSide/entities
cp commInfra/*.class dirTable/commInfra
echo "  Kitchen"
rm -rf dirKitchen/serverSide dirKitchen/clientSide dirKitchen/interfaces dirKitchen/commInfra
mkdir -p dirKitchen/serverSide dirKitchen/serverSide/main dirKitchen/serverSide/objects dirKitchen/interfaces \
         dirKitchen/clientSide dirKitchen/clientSide/entities dirKitchen/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerKitchen.class dirKitchen/serverSide/main
cp serverSide/objects/Kitchen.class dirKitchen/serverSide/objects
cp interfaces/*.class dirKitchen/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/ChefStates.class dirKitchen/clientSide/entities
cp commInfra/*.class dirKitchen/commInfra
echo "  Chef"
rm -rf dirChef/serverSide dirChef/clientSide dirChef/interfaces dirChef/commInfra
mkdir -p dirChef/serverSide dirChef/serverSide/main dirChef/clientSide dirChef/clientSide/main dirChef/clientSide/entities \
         dirChef/interfaces dirChef/commInfra
cp serverSide/main/Constants.class dirChef/serverSide/main
cp clientSide/main/ClientChef.class dirChef/clientSide/main
cp clientSide/entities/Chef.class clientSide/entities/ChefStates.class dirChef/clientSide/entities
cp interfaces/*.class dirChef/interfaces
cp commInfra/*.class dirChef/commInfra
echo "  Waiter"
rm -rf dirWaiter/serverSide dirWaiter/clientSide dirWaiter/interfaces dirWaiter/commInfra
mkdir -p dirWaiter/serverSide dirWaiter/serverSide/main dirWaiter/clientSide dirWaiter/clientSide/main dirWaiter/clientSide/entities \
         dirWaiter/interfaces dirWaiter/commInfra
cp serverSide/main/Constants.class dirWaiter/serverSide/main
cp clientSide/main/ClientWaiter.class dirWaiter/clientSide/main
cp clientSide/entities/Waiter.class clientSide/entities/WaiterStates.class dirWaiter/clientSide/entities
cp interfaces/*.class dirWaiter/interfaces
cp commInfra/*.class dirWaiter/commInfra
echo "  Student"
rm -rf dirStudent/serverSide dirStudent/clientSide dirStudent/interfaces dirStudent/commInfra
mkdir -p dirStudent/serverSide dirStudent/serverSide/main dirStudent/clientSide dirStudent/clientSide/main dirStudent/clientSide/entities \
         dirStudent/interfaces dirStudent/commInfra
cp serverSide/main/Constants.class dirStudent/serverSide/main
cp clientSide/main/ClientStudent.class dirStudent/clientSide/main
cp clientSide/entities/Student.class clientSide/entities/StudentStates.class dirStudent/clientSide/entities
cp interfaces/*.class dirStudent/interfaces
cp commInfra/*.class dirStudent/commInfra
echo "Compressing execution environments."
echo "  Genclass"
rm -f genclass.zip
zip -rq genclass.zip genclass
echo "  RMI registry"
rm -f  dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry
echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry
echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "  Bar"
rm -f  dirBar.zip
zip -rq dirBar.zip dirBar
echo "  Kitchen"
rm -f  dirKitchen.zip
zip -rq dirKitchen.zip dirKitchen
echo "  Table"
rm -f  dirTable.zip
zip -rq dirTable.zip dirTable
echo "  Chef"
rm -f  dirChef.zip
zip -rq dirChef.zip dirChef
echo "  Waiter"
rm -f  dirWaiter.zip
zip -rq dirWaiter.zip dirWaiter
echo "  Student"
rm -f  dirStudent.zip
zip -rq dirStudent.zip dirStudent
echo "Deploying and decompressing execution environments."
mkdir -p ~/test/Restaurant
rm -rf ~/test/Restaurant/*
cp genclass.zip ~/test/Restaurant
cp dirRegistry.zip ~/test/Restaurant
cp dirGeneralRepos.zip ~/test/Restaurant
cp dirBar.zip ~/test/Restaurant
cp dirKitchen.zip ~/test/Restaurant
cp dirTable.zip ~/test/Restaurant
cp dirChef.zip ~/test/Restaurant
cp dirWaiter.zip ~/test/Restaurant
cp dirStudent.zip ~/test/Restaurant
cp dirRMIRegistry.zip ~/test/Restaurant
cd ~/test/Restaurant
unzip -q genclass.zip
unzip -q dirRegistry.zip
unzip -q dirGeneralRepos.zip
unzip -q dirBar.zip
unzip -q dirKitchen.zip
unzip -q dirTable.zip
unzip -q dirWaiter.zip
unzip -q dirChef.zip
unzip -q dirStudent.zip
unzip -q dirRMIRegistry.zip
cp genclass.zip ~/test/Restaurant/dirBar
cp genclass.zip ~/test/Restaurant/dirKitchen
cp genclass.zip ~/test/Restaurant/dirTable
cp genclass.zip ~/test/Restaurant/dirWaiter
cp genclass.zip ~/test/Restaurant/dirStudent
cp genclass.zip ~/test/Restaurant/dirChef
cp genclass.zip ~/test/Restaurant/dirGeneralRepos
cp genclass.zip ~/test/Restaurant/dirRegistry
cp genclass.zip ~/test/Restaurant/dirRMIRegistry
cd dirBar
unzip -q genclass.zip
cd ../dirKitchen
unzip -q genclass.zip
cd ../dirTable
unzip -q genclass.zip
cd ../dirChef
unzip -q genclass.zip
cd ../dirWaiter
unzip -q genclass.zip
cd ../dirStudent
unzip -q genclass.zip
cd ../dirGeneralRepos
unzip -q genclass.zip
cd ../dirRegistry
unzip -q genclass.zip
cd ../dirRMIRegistry
unzip -q genclass.zip
