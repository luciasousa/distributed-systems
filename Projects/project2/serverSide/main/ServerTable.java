package serverSide.main;
import java.net.SocketTimeoutException;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import serverSide.entities.*;
import serverSide.sharedRegions.*;

/**
 *    Server side of the Table Shared Region.
 *
 *    Request serialization variant.
 *    It waits for the start of the message exchange.
 */
public class ServerTable {
    /**
     *  Flag signaling the service is active.
     */

    public static boolean waitConnection;

    /**
     *    Main method.
     *
     *    @param args runtime arguments
     *      args[0] - port number for listening to service requests
     *      args[1] - name of the platform where is located the server for the general repository
     *      args[2] - port number where the server for the general repository is listening to service requests
     */

    public static void main (String[] args)
    {
        /* service is established */

        ServerCom serverCom, sconi;                                        // communication channels
        int portNumb = -1;                                          // port nunber for listening to service requests

        String reposServerName;                                        // name of the platform where is located the server for the general repository
        int reposPortNumb = -1;                                        // port nunber where the server for the general repository is listening to service requests


        if (args.length != 3)
            { GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
            }
        try
            { portNumb = Integer.parseInt (args[0]);
            }
        catch (NumberFormatException e)
            { GenericIO.writelnString ("args[0] is not a number!");
                System.exit (1);
            }
        if ((portNumb < 4000) || (portNumb >= 65536))
            { GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
            }
        reposServerName = args[1];
        try
            { reposPortNumb = Integer.parseInt (args[2]);
            }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if ((reposPortNumb < 4000) || (reposPortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        serverCom = new ServerCom (portNumb);
        serverCom.start ();                
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        GeneralReposStub generalReposStub = new GeneralReposStub(reposServerName, reposPortNumb);
        Table table = new Table(generalReposStub);
        TableInterface tableInterface = new TableInterface(table);
        
        /* service request processing */
                                        // service provider agent
        waitConnection = true;
        while (waitConnection)
        { 
        try {
            sconi = serverCom.accept ();                                     // enter listening procedure
            TableClientProxy tableClientProxy = new TableClientProxy (sconi, tableInterface);            // start a service provider agent to address
            tableClientProxy.start ();      
        } 
        catch(SocketTimeoutException ste) {}
        }
        serverCom.end();
        GenericIO.writelnString ("Service is closed!");
    }
}
