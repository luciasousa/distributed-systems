CODEBASE="file:///home/"$1"/test/Restaurant/dirBar/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerBar 22154 localhost 22156
