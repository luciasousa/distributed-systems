CODEBASE="file:///home/"$1"/test/Restaurant/dirGeneralRepos/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGeneralRepos 22153 localhost 22156
