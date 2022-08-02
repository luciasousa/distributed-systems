package clientSide.main;
import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;

/**
 *    Client side of the Assignment 2 - Chef.
 *    Static solution Attempt (number of threads controlled by global constants - ExecConst)
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ClientChef {
    /**
	 *  Main method.
	 *
	 *    @param args runtime arguments
	 *        args[0] - name of the platform where is located the general repository server
	 *        args[1] - port number for listening to service requests
	 *        args[2] - name of the platform where is located the kitchen server
	 *        args[3] - port number for listening to service requests
	 *        args[4] - name of the platform where is located the bar server
	 *        args[5] - port number for listening to service requests
	 */
    public static void main(String[] args) {
        Chef chef;
        KitchenStub kitchenStub;
        BarStub barStub;
        GeneralReposStub generalReposStub;
        String kitchenServerName = "";
        String barServerName = "";
        String generalRepoServerName = "";
        int kitchenServerPort = -1;
        int barServerPort = -1;
        int generalRepoServerPort = -1;
        
        if (args.length != 6)
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
	     barServerName = args[4];
	     try
	     { barServerPort = Integer.parseInt (args[5]);
	     }
	     catch (NumberFormatException e)
	     { GenericIO.writelnString ("args[5] is not a number!");
	       System.exit (1);
	     }
	     if ((barServerPort < 4000) || (barServerPort >= 65536))
	        { GenericIO.writelnString ("args[5] is not a valid port number!");
	          System.exit (1);
	        }

        
        kitchenStub = new KitchenStub(kitchenServerName, kitchenServerPort);
        barStub = new BarStub(barServerName, barServerPort);
        generalReposStub = new GeneralReposStub(generalRepoServerName,generalRepoServerPort);
        chef = new Chef(ChefStates.WAFOR, kitchenStub, barStub);

        /*start thread*/
        chef.start();

        /* wait for the end */
        try{ 
            chef.join();
        }
        catch (InterruptedException e) {}
        System.out.println("The Chef just terminated");
        System.out.println("End of the Simulation");
		kitchenStub.shutdown ();
        barStub.shutdown();
        generalReposStub.shutdown ();
	}
}
