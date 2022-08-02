CODEBASE="file:///home/"$1"/test/Restaurant/dirWaiter/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientWaiter localhost 22156
