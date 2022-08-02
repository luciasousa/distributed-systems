CODEBASE="file:///home/"$1"/test/Restaurant/dirKitchen/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerKitchen 22152 localhost 22156
