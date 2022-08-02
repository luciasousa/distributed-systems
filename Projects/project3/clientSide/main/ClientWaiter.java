package clientSide.main;
import java.rmi.registry.*;
import java.rmi.*;
import clientSide.entities.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *    Client side of the Restaurant (waiter).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class ClientWaiter
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - name of the platform where is located the RMI registering service
   *        args[1] - port number where the registering service is listening to service requests
   */

   public static void main (String [] args)
   {
        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests


        /* getting problem runtime parameters */

        if (args.length != 2)
            { GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
            }
        rmiRegHostName = args[0];
        try
        { rmiRegPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
            { GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit (1);
            }

        /* problem initialization */

        String nameEntryGeneralRepos = "GeneralRepository";               // public name of the general repository object
        GeneralReposInterface reposInterface = null;                      // remote reference to the general repository object
        String nameEntryKitchen = "Kitchen";                              // public name of the kitchen object
        KitchenInterface kitchenInterface = null;                         // remote reference to the kitchen object
        String nameEntryTable = "Table";                                  // public name of the table object
        TableInterface tableInterface = null;                             // remote reference to the table object
        String nameEntryBar = "Bar";                                      // public name of the bar object
        BarInterface barInterface = null;                                 // remote reference to the bar object
        Registry registry = null;                                         // remote reference for registration in the RMI registry service
        Waiter waiter;                                                    // waiter thread

        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { reposInterface = (GeneralReposInterface) registry.lookup (nameEntryGeneralRepos);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("GeneralRepos lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("GeneralRepos not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { kitchenInterface = (KitchenInterface) registry.lookup (nameEntryKitchen);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Kitchen lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Kitchen not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { tableInterface = (TableInterface) registry.lookup (nameEntryTable);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Table lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Table not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { barInterface = (BarInterface) registry.lookup (nameEntryBar);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Bar lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Bar not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        waiter = new Waiter (WaiterStates.APPST,barInterface, kitchenInterface, tableInterface);
        System.out.println("Start of the Simulation");
        /* start of the simulation */
        waiter.start ();

        /* waiting for the end of the simulation */
        try
        { waiter.join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("The waiter has terminated.");

        try
        { kitchenInterface.shutdown ();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Waiter generator remote exception on Kitchen shutdown: " + e.getMessage ());
            System.exit (1);
        }
        try
        { tableInterface.shutdown ();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Waiter generator remote exception on Table shutdown: " + e.getMessage ());
            System.exit (1);
        }
        try
        { barInterface.shutdown ();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Waiter generator remote exception on Bar shutdown: " + e.getMessage ());
            System.exit (1);
        }
        try
        { reposInterface.shutdown ();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Waiter generator remote exception on GeneralRepos shutdown: " + e.getMessage ());
            System.exit (1);
        }
        System.out.println("End of the Simulation");
    }
}
