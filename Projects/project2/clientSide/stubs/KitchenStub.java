package clientSide.stubs;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Kitchen.
 *
 *    It instantiates a remote reference to the kitchen.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class KitchenStub {
    /**
    *  Name of the computational system where the server is located.
    */
    private String serverHostName;

    /**
    *  Number of the listening port at the computational system where the server is located.
    */
    private int serverPortNumb;

    /**
    *  Instantiation of a remote reference
    *
    *    @param hostName name of the computational system where the server is located
    *    @param port number of the listening port at the computational system where the server is located
    */
    public KitchenStub (String hostName, int port)
    {
       serverHostName = hostName;
       serverPortNumb = port;
    }

    /**
   *  Operation watch the news.
   *
   *  It is called by the chef that is watching the news waiting for the order.
   *
   */
    public void watchTheNews() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message

        while (!com.open ())       // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        
        outMessage = new Message (MessageType.WAFOR, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        //System.out.printf("out message type: %d\n",outMessage.getMsgType());
        inMessage = (Message) com.readObject ();
        //System.out.printf("in message type: %d\n",inMessage.getMsgType());
        if (inMessage.getMsgType () != MessageType.ORDERDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 18!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () != ChefStates.WAFOR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 2!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();
    }

    /**
   *  Operation start preparation.
   *
   *  It is called by the chef to start preparation.
   *
   */
    public void startPreparation() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.PRPCS, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.CSPREP)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 19!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.WAFOR) || (inMessage.getChefState () > ChefStates.PRPCS))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 3!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();
    }

    /**
   *  Operation get first course.
   *
   *  It is called by the chef to check if he is preparing the first course or not.
   *
   *    @return true, if chef is preparing the first course -
   *            false, otherwise
   */
    public boolean getFirstCourse() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.GETFIRSTCOURSE, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.FIRSTCOURSE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 20!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.WAFOR) || (inMessage.getChefState () > ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 4!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();

        return (inMessage.getBoolVal());
    }

    /**
   *  Operation proceed to presentation.
   *
   *  It is called by the chef to proceed to the presentation of the dish.
   *
   */
    public void proceedToPresentation() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.PRCPRES, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.PRESDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 21!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.PRPCS) || (inMessage.getChefState () > ChefStates.DSHPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 5!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();

    }

    /**
   *  Operation have all portions been delivered.
   *
   *  It is called by the chef to check if all portions have been delivered.
   *
   *    @return true, if all portions have been delivered -
   *            false, otherwise
   */
    public boolean haveAllPortionsBeenDelivered() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.PORDELIV, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.PORDELIVDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 22!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () != ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 6!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();

        return (inMessage.getBoolVal());
    }

    /**
   *  Operation have next portion ready.
   *
   *  It is called by the chef to start preparing the next portion.
   *
   */
    public void haveNextPortionReady() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.POREADY, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.POREADYDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 23!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.DSHPT) || (inMessage.getChefState () > ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 7!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();

    }

    /**
   *  Operation set first course
   *
   *  It is called by the chef to set the first course.
   *
   *    @param b true, if the chef is preparing the first course -
   *            false, otherwise
   */
    public void setFirstCourse(boolean b) {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETFIRSTCS, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.SETFIRSTCSDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 24!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.WAFOR) || (inMessage.getChefState () > ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 8!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();

    }

    /**
   *  Operation has the order been completed.
   *
   *  It is called by the chef to check if the order has been completed.
   *
   *    @return true, if the order is complete -
   *            false, otherwise
   */
    public boolean hasTheOrderBeenCompleted() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.ORDERCOMPREQ, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.ORDERCOMP)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 25!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () != ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 9!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();

        return (inMessage.getBoolVal());
    }

    /**
   *  Operation clean up.
   *
   *  It is called by the chef when the order is complete to clean up.
   *
   */
    public void cleanUp() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.CLEANREQ, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.CLEAN)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 26!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.DLVPT) || (inMessage.getChefState () > ChefStates.CLSSV))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 10!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();
    }

    /**
   *  Operation hand note to the chef.
   *
   *  It is called by the waiter to hand the note to the chef.
   *
   */
    public void handTheNoteToChef() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.NOTEREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.NOTE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 27!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () < WaiterStates.TKODR) || (inMessage.getWaiterState () > WaiterStates.PCODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 5!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *   Operation server shutdown.
   *
   *   New operation.
   */
    public void shutdown ()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
            { Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SHUT);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SHUTDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 28!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
   *  Operation chef wait for collection.
   *
   *  It is called by the chef to wait for waiter to collect the portion.
   * 
   *  @param chefState chef state
   *
   */
    public void chefWaitForCollection(int chefState) 
    {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.CHEFWAIT, chefState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.CHEFWAITDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 29!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () != ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 11!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
   *  Operation portion has been collected.
   *
   *  It is called by the waiter when he collect the portion.
   * 
   *  @param waiterState waiter state
   *
   */
    public void portionHasBeenCollected(int waiterState) 
    {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.PORTIONCOLLECT, waiterState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.PORTIONCOLLECTDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 30!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () < WaiterStates.APPST) || (inMessage.getWaiterState () > WaiterStates.WTFPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 6!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    public void continuePreparation() 
    {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.CONTINUEPREP, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.CONTINUEPREPDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 19!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getChefState () < ChefStates.PRPCS) || (inMessage.getChefState () > ChefStates.DLVPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 3!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
        com.close ();
    }
}
