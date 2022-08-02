package clientSide.main;
import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;

/**
 *    Client side of the Assignment 2 - Waiter.
 *    Static solution Attempt (number of threads controlled by global constants - ExecConst)
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ClientWaiter {
    /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - name of the platform where is located the general repository server
   *        args[1] - port number for listening to service requests
   *        args[2] - name of the platform where is located the kitchen server
   *        args[3] - port number for listening to service requests
   *        args[4] - name of the platform where is located the table server
   *        args[5] - port number for listening to service requests
   * 		args[6] - name of the platform where is located the bar server
   *        args[7] - port number for listening to service requests	
   */
    public static void main(String[] args) {
        Waiter waiter;
        KitchenStub kitchenStub;
        TableStub tableStub;
        BarStub barStub;
        GeneralReposStub generalRepos;
        String kitchenServerName = "";
        String tableServerName = "";
        String barServerName = "";
        String generalRepoServerName = "";
        int kitchenServerPort = -1;
        int tableServerPort = -1;
        int barServerPort = -1;
        int generalRepoServerPort = -1;
        
        if (args.length != 8)
        { GenericIO.writelnString ("Wrong number of parameters!");
          System.exit (1);
        }
        generalRepoServerName = args[0];
	     try
	     { generalRepoServerPort = Integer.parseInt (args[1]);
	     }
	     catch (NumberFormatException e)
	     { GenericIO.writelnString ("args[1] is not a number!");
	       System.exit (1);
	     }
	     if ((generalRepoServerPort < 4000) || (generalRepoServerPort >= 65536))
	        { GenericIO.writelnString ("args[1] is not a valid port number!");
	          System.exit (1);
	        }
	     kitchenServerName = args[2];
	     try
	     { kitchenServerPort = Integer.parseInt (args[3]);
	     }
	     catch (NumberFormatException e)
	     { GenericIO.writelnString ("args[3] is not a number!");
	       System.exit (1);
	     }
	     if ((kitchenServerPort < 4000) || (kitchenServerPort >= 65536))
	        { GenericIO.writelnString ("args[3] is not a valid port number!");
	          System.exit (1);
	        }
	     tableServerName = args[4];
	     try
	     { tableServerPort = Integer.parseInt (args[5]);
	     }
	     catch (NumberFormatException e)
	     { GenericIO.writelnString ("args[5] is not a number!");
	       System.exit (1);
	     }
	     if ((tableServerPort < 4000) || (tableServerPort >= 65536))
	        { GenericIO.writelnString ("args[5] is not a valid port number!");
	          System.exit (1);
	        }
	     barServerName = args[6];
	     try
	     { barServerPort = Integer.parseInt (args[7]);
	     }
	     catch (NumberFormatException e)
	     { GenericIO.writelnString ("args[7] is not a number!");
	       System.exit (1);
	     }
	     if ((barServerPort < 4000) || (barServerPort >= 65536))
	        { GenericIO.writelnString ("args[7] is not a valid port number!");
	          System.exit (1);
	        }

        
        kitchenStub = new KitchenStub(kitchenServerName, kitchenServerPort);
        tableStub = new TableStub(tableServerName, tableServerPort);
        barStub = new BarStub(barServerName, barServerPort);
        generalRepos = new GeneralReposStub(generalRepoServerName,generalRepoServerPort);
        waiter = new Waiter(WaiterStates.APPST, barStub, kitchenStub, tableStub);

        /*start thread*/
        waiter.start();

        /* wait for the end */
        try {
            waiter.join();	
        } catch (InterruptedException e) {}
        System.out.println("The Waiter just terminated");
		System.out.println("End of Simulation");
		tableStub.shutdown ();
        barStub.shutdown();
		kitchenStub.shutdown();
        generalRepos.shutdown ();
	}
}
