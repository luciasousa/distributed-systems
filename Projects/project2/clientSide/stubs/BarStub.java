package clientSide.stubs;
import clientSide.entities.Chef;
import clientSide.entities.ChefStates;
import clientSide.entities.Student;
import clientSide.entities.StudentStates;
import clientSide.entities.Waiter;
import clientSide.entities.WaiterStates;
import clientSide.main.Constants;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Bar.
 *
 *    It instantiates a remote reference to the bar.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class BarStub {
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
    public BarStub (String hostName, int port)
    {
       serverHostName = hostName;
       serverPortNumb = port;
    }

    /**
     *  Operation alert the waiter.
     *
     *  It is called by the chef when a portion is ready to deliver.
     * 
     */
    public void alertTheWaiter() {
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
        Request r = new Request(Constants.N, 'p');
        outMessage = new Message (MessageType.ALERTWAITER,((Chef) Thread.currentThread()).getChefState(),r);
        com.writeObject (outMessage);

        inMessage = (Message) com.readObject ();
        ////System.out.printf("Chef state= %d\n",inMessage.getChefState());
        if (inMessage.getMsgType () != MessageType.WAITERALERTED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 1!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if ((inMessage.getChefState () < ChefStates.DSHPT) || (inMessage.getChefState () > ChefStates.DLVPT))
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state 12_1!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState ());
    }

    /**
   *  Operation enter.
   *
   *  It is called by a student when he enters to the restaurant.
   *
   *    @return array of students by order of arrival
   * 
   */
    public int[] enter() {
        // communication channel
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        ////System.out.println("try to establish connection");
        while (!com.open ())                                           // waits for a connection to be established
        { 
            ////System.out.println("wait to establish connection");
            try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.ENTERSTUDENT, ((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState(), 'c');
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.STUDENTENTERED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 2!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if ((inMessage.getStudentState () < StudentStates.GGTRT) || (inMessage.getStudentState () > StudentStates.TKSTT))
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 1!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();

        return inMessage.getArrStudents();
    }

    /**
   *  Operation call waiter.
   *
   *  It is called by the first student when the order is ready.
   *
   */
    public void callWaiter() {
        // communication channel
        ////System.out.println("Bar -> Call Waiter");
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message(MessageType.CALLWAITER,((Student) Thread.currentThread()).getStudentID(),((Student) Thread.currentThread()).getStudentState(), 'o');
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITERCALLED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 3!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if (inMessage.getStudentState () != StudentStates.OGODR)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 2!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation signal the waiter
   *
   *  It is called by a student when everybody finished eating.
   *
   */
    public void signalTheWaiter() {
        // communication channel
        ////System.out.println("signal the waiter bar stub");
        ClientCom com = new ClientCom (serverHostName, serverPortNumb);
        Message outMessage,        // outgoing message
        inMessage;                 // incoming message
        while (!com.open ())                                           // waits for a connection to be established
        { try
            { Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SIGNALWAITER,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState(),'b');
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITERSIGNALED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 4!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if (inMessage.getStudentState () != StudentStates.CHTWC)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 3!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation exit.
   *
   *  It is called by a student when he goes home.
   *
   */
    public void exit() {
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

        outMessage = new Message (MessageType.EXITSTUDENT,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState(),'g');
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.STUDENTEXITED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type  5!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if ((inMessage.getStudentState () < StudentStates.CHTWC || inMessage.getStudentState () > StudentStates.GGHOM))
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 4!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation look around.
   *
   *  It is called by the waiter to check if he has requests.
   *
   *    @return request
   */
    public Request lookAround() {
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

        outMessage = new Message (MessageType.LOOKWAITER, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITERLOOKED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 6!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if (inMessage.getWaiterState () != WaiterStates.APPST)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 1!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
        ////System.out.printf("Request from message: id %d type %c\n",(inMessage.getRequest()).getRequestID(),(inMessage.getRequest()).getRequestType());
        return inMessage.getRequest();
    }

    /**
   *  Operation return to bar.
   *
   *  It is called by the waiter to return to bar.
   *
   */
    public void returnToBar() {
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

        outMessage = new Message (MessageType.RETURNWAITER, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITERRETURNED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 7!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *  Operation collect portion.
   *
   *  It is called by the waiter to collect portion.
   *
   */
    public void collectPortion() {
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

        outMessage = new Message (MessageType.COLLECTWAITER, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITERCOLLECTED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 8!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if ((inMessage.getWaiterState () < WaiterStates.APPST || inMessage.getWaiterState () > WaiterStates.WTFPT))
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 2!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *  Operation prepare the bill.
   *
   *  It is called by the waiter to prepare the bill.
   *
   */
    public void prepareTheBill() {
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

        outMessage = new Message (MessageType.PREPAREWAITER, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITERPREPARED)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 9!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
            if ((inMessage.getWaiterState () < WaiterStates.APPST || inMessage.getWaiterState () > WaiterStates.PRCBL))
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 3!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *  Operation say goodbye.
   *
   *  It is called by the waiter to say goodbye to the student leaving.
   *
   *    @param req request
   *    @return number of students in restaurant
   */
    public int sayGoodbye(Request req) {
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
        outMessage = new Message (MessageType.SAYGOODBYE, ((Waiter) Thread.currentThread()).getWaiterState(), req);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.SAYGOODBYEDONE)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 10!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        if (inMessage.getWaiterState () != WaiterStates.APPST)
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 4!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        ((Waiter) Thread.currentThread ()).setWaiterState(inMessage.getWaiterState ());
        com.close ();

        return inMessage.getNumberOfStudentsInRestaurant();
    }

    /**
   *  Operation end of work.
   *
   *   New operation.
   *
   */
   public void endOperation ()
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
      outMessage = new Message (MessageType.ENDOP, ((Waiter) Thread.currentThread()).getWaiterState());
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();
      if (inMessage.getMsgType() != MessageType.EOPDONE)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      com.close ();
   }

   /**
   *   Operation server shutdown.
   *
   *   New operation.
   */
    public void shutdown() {
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
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 11!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }
}
