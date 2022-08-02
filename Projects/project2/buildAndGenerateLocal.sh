echo "Compiling source code."
javac -cp genclass.jar -target 8 */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions \
         dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerGeneralRepos.class dirGeneralRepos/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class dirGeneralRepos/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/GeneralRepos.class dirGeneralRepos/serverSide/sharedRegions
cp clientSide/entities/ChefStates.class clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class dirGeneralRepos/clientSide/entities
cp commInfra/*.class dirGeneralRepos/commInfra
echo "  Kitchen"
rm -rf dirKitchen
mkdir -p dirKitchen dirKitchen/serverSide dirKitchen/serverSide/main dirKitchen/serverSide/entities dirKitchen/serverSide/sharedRegions \
         dirKitchen/clientSide dirKitchen/clientSide/entities dirKitchen/clientSide/stubs dirKitchen/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerKitchen.class dirKitchen/serverSide/main
cp serverSide/entities/*.class dirKitchen/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/KitchenInterface.class serverSide/sharedRegions/Kitchen.class dirKitchen/serverSide/sharedRegions
cp clientSide/entities/ChefStates.class clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class clientSide/entities/ChefCloning.class clientSide/entities/WaiterCloning.class clientSide/entities/StudentCloning.class \
   dirKitchen/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class dirKitchen/clientSide/stubs
cp commInfra/*.class dirKitchen/commInfra
echo "  Bar"
rm -rf dirBar
mkdir -p dirBar dirBar/serverSide dirBar/serverSide/main dirBar/serverSide/entities dirBar/serverSide/sharedRegions \
         dirBar/clientSide dirBar/clientSide/entities dirBar/clientSide/stubs dirBar/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerBar.class dirBar/serverSide/main
cp serverSide/entities/*.class dirBar/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/BarInterface.class serverSide/sharedRegions/Bar.class serverSide/sharedRegions/Table.class serverSide/sharedRegions/Kitchen.class dirBar/serverSide/sharedRegions
cp clientSide/entities/*.class dirBar/clientSide/entities
cp clientSide/stubs/*.class dirBar/clientSide/stubs
cp commInfra/*.class dirBar/commInfra
echo "  Table"
rm -rf dirTable
mkdir -p dirTable dirTable/serverSide dirTable/serverSide/main dirTable/serverSide/entities dirTable/serverSide/sharedRegions \
         dirTable/clientSide dirTable/clientSide/entities dirTable/clientSide/stubs dirTable/commInfra
cp serverSide/main/Constants.class serverSide/main/ServerTable.class dirTable/serverSide/main
cp serverSide/entities/*.class dirTable/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/TableInterface.class serverSide/sharedRegions/Table.class dirTable/serverSide/sharedRegions
cp clientSide/entities/ChefStates.class clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class clientSide/entities/ChefCloning.class clientSide/entities/WaiterCloning.class clientSide/entities/StudentCloning.class \
   dirTable/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class dirTable/clientSide/stubs
cp commInfra/*.class dirTable/commInfra
echo "  Chef"
rm -rf dirChef
mkdir -p dirChef dirChef/serverSide dirChef/serverSide/main dirChef/clientSide dirChef/clientSide/main dirChef/clientSide/entities \
         dirChef/clientSide/stubs dirChef/commInfra
cp serverSide/main/Constants.class dirChef/serverSide/main
cp clientSide/main/ClientChef.class dirChef/clientSide/main
cp clientSide/entities/Chef.class clientSide/entities/ChefStates.class dirChef/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/KitchenStub.class clientSide/stubs/BarStub.class dirChef/clientSide/stubs
cp commInfra/*.class dirChef/commInfra
echo "  Waiter"
rm -rf dirWaiter
mkdir -p dirWaiter dirWaiter/serverSide dirWaiter/serverSide/main dirWaiter/clientSide dirWaiter/clientSide/main dirWaiter/clientSide/entities \
         dirWaiter/clientSide/stubs dirWaiter/commInfra
cp serverSide/main/Constants.class dirWaiter/serverSide/main
cp clientSide/main/ClientWaiter.class dirWaiter/clientSide/main
cp clientSide/entities/Waiter.class clientSide/entities/WaiterStates.class dirWaiter/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/BarStub.class clientSide/stubs/KitchenStub.class clientSide/stubs/TableStub.class dirWaiter/clientSide/stubs
cp commInfra/*.class dirWaiter/commInfra
echo "  Student"
rm -rf dirStudent
mkdir -p dirStudent dirStudent/serverSide dirStudent/serverSide/main dirStudent/clientSide dirStudent/clientSide/main dirStudent/clientSide/entities \
         dirStudent/clientSide/stubs dirStudent/commInfra
cp serverSide/main/Constants.class dirStudent/serverSide/main
cp clientSide/main/ClientStudent.class dirStudent/clientSide/main
cp clientSide/entities/Student.class clientSide/entities/StudentStates.class dirStudent/clientSide/entities
cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/TableStub.class clientSide/stubs/BarStub.class dirStudent/clientSide/stubs
cp commInfra/*.class dirStudent/commInfra
echo "Compressing execution environments."
echo " Genclass"
rm -f genclass.zip
zip -rq genclass.zip genclass
echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "  Kitchen"
rm -f  dirKitchen.zip
zip -rq dirKitchen.zip dirKitchen
echo "  Bar"
rm -f  dirBar.zip
zip -rq dirBar.zip dirBar
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
cp dirGeneralRepos.zip ~/test/Restaurant
cp dirKitchen.zip ~/test/Restaurant
cp dirBar.zip ~/test/Restaurant
cp dirTable.zip ~/test/Restaurant
cp dirChef.zip ~/test/Restaurant
cp dirWaiter.zip ~/test/Restaurant
cp dirStudent.zip ~/test/Restaurant
cp genclass.zip ~/test/Restaurant
cd ~/test/Restaurant
unzip -q dirGeneralRepos.zip
unzip -q genclass.zip
unzip -q dirKitchen.zip
unzip -q dirBar.zip
unzip -q dirTable.zip
unzip -q dirChef.zip
unzip -q dirWaiter.zip
unzip -q dirStudent.zip
unzip -q dirKitchen.zip
cp genclass.zip ~/test/Restaurant/dirBar
cp genclass.zip ~/test/Restaurant/dirKitchen
cp genclass.zip ~/test/Restaurant/dirTable
cp genclass.zip ~/test/Restaurant/dirWaiter
cp genclass.zip ~/test/Restaurant/dirStudent
cp genclass.zip ~/test/Restaurant/dirChef
cp genclass.zip ~/test/Restaurant/dirGeneralRepos
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