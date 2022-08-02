package serverSide.main;
import java.net.SocketTimeoutException;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import serverSide.entities.*;
import serverSide.sharedRegions.*;

/**
 *    Server side of the Bar.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ServerBar {
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
     *      args[3] - name of the platform where is located the server for the table
     *      args[4] - port number where the server for the table is listening to service requests
     *      args[5] - name of the platform where is located the server for the kicthen
     *      args[6] - port number where the server for the kitchen is listening to service requests
     */

    public static void main (String[] args)
    {
        /* service is established */

        ServerCom serverCom, sconi;                                        // communication channels
        int portNumb = -1;                                          // port nunber for listening to service requests

        String reposServerName;                                        // name of the platform where is located the server for the general repository
        int reposPortNumb = -1;                                        // port nunber where the server for the general repository is listening to service requests

        String tableServerName;                                        // name of the platform where is located the server for the general repository
        int tablePortNumb = -1;

        String kitchenServerName;                                        // name of the platform where is located the server for the general repository
        int kitchenPortNumb = -1;

        if (args.length != 7)
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

        tableServerName = args[3];
        try
            { tablePortNumb = Integer.parseInt (args[4]);
            }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[4] is not a number!");
            System.exit (1);
        }
        if ((tablePortNumb < 4000) || (tablePortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[4] is not a valid port number!");
            System.exit (1);
        }

        kitchenServerName = args[5];
        try
            { kitchenPortNumb = Integer.parseInt (args[6]);
            }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[6] is not a number!");
            System.exit (1);
        }
        if ((kitchenPortNumb < 4000) || (kitchenPortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[6] is not a valid port number!");
            System.exit (1);
        }

        serverCom = new ServerCom (portNumb);
        serverCom.start ();                
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        GeneralReposStub generalReposStub = new GeneralReposStub(reposServerName, reposPortNumb);
        TableStub tableStub = new TableStub(tableServerName, tablePortNumb);
        KitchenStub kitchenStub = new KitchenStub(kitchenServerName, kitchenPortNumb);
        Bar bar = new Bar(generalReposStub, tableStub, kitchenStub);
        BarInterface barInterface = new BarInterface(bar);
        
        /* service request processing */
                                        // service provider agent
        waitConnection = true;
        while (waitConnection)
        { 
        try {
            sconi = serverCom.accept ();                                     // enter listening procedure
            BarClientProxy barClientProxy = new BarClientProxy(sconi, barInterface);            // start a service provider agent to address
            barClientProxy.start ();      
        } 
        catch(SocketTimeoutException ste) {}
        }
        serverCom.end();
        
        GenericIO.writelnString ("Service is closed!");
    }
}
