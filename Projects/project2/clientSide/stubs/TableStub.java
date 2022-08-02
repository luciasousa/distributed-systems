package clientSide.stubs;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Table.
 *
 *    It instantiates a remote reference to the table.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class TableStub {
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
    public TableStub (String hostName, int port)
    {
       serverHostName = hostName;
       serverPortNumb = port;
    }

    /**
   *  Operation read menu.
   *
   *  It is called by the student to read the menu.
   *
   */
    public void readMenu() {
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
        outMessage = new Message (MessageType.READMENUREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        ////System.out.printf("in mes st state: %d\n",inMessage.getStudentState ());

        if (inMessage.getMsgType () != MessageType.READMENU)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 31!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.TKSTT) || (inMessage.getStudentState () > StudentStates.SELCS))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 5!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation inform companion.
   *
   *  It is called by a student to inform the first student about the order.
   *
   */
    public void informCompanion() {
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
        outMessage = new Message (MessageType.INFCOMPREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.INFCOMP)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 32!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.SELCS) || (inMessage.getStudentState () > StudentStates.CHTWC))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 6!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation prepare the order.
   *
   *  It is called by the first student to prepare the order.
   *
   */
    public void prepareTheOrder() {
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
        outMessage = new Message (MessageType.PREPORDERREQ, ((Student) Thread.currentThread()).getStudentID(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.PREPORDER)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 33!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.SELCS) || (inMessage.getStudentState () > StudentStates.OGODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 7!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation has everybody chosen.
   *
   *  It is called by the first student to check if everybody chose.
   *
   *    @return true, if everybody chose -
   *            false, otherwise
   */
    public boolean hasEverybodyChosen() {
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
        outMessage = new Message (MessageType.EVBDCHOSENREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.EVBDCHOSEN)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 34!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.OGODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 8!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
        return inMessage.getBoolVal();
    }

    /**
   *  Operation add up ones choice.
   *
   *  It is called by the frist student to add up the choices.
   *
   */
    public void addUpOnesChoice() {
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
        outMessage = new Message (MessageType.ADDUPCHCREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.ADDUPCHC)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 35!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.OGODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 9!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation describe the order.
   *
   *  It is called by the first student to describe the order to the waiter.
   *
   */
    public void describeTheOrder() {
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
        outMessage = new Message (MessageType.DESCORDERREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.DESCORDER)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 36!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.OGODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 10!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation join the talk.
   *
   *  It is called by the student to join the talk.
   *
   */
    public void joinTheTalk() {
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
        outMessage = new Message (MessageType.JOINTALKREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.JOINTALK)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 37!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.OGODR) || (inMessage.getStudentState () > StudentStates.CHTWC))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 11!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation start eating.
   *
   *  It is called by the student to start eating.
   */
    public void startEating() {
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
        outMessage = new Message (MessageType.STARTEATREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        ////System.out.printf("student start eating state: %d, id: %d\n", inMessage.getStudentState(), inMessage.getStudentID());
        if (inMessage.getMsgType () != MessageType.STARTEAT)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 38!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.CHTWC) || (inMessage.getStudentState () > StudentStates.EJYML))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 12!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    } 
    
    /**
   *  Operation end eating.
   *
   *  It is called by the student to finish eating.
   *
   */
    public void endEating() {
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
        outMessage = new Message (MessageType.ENDEATREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        ////System.out.printf("student end eating state: %d, id: %d\n", inMessage.getStudentState(), inMessage.getStudentID());
        if (inMessage.getMsgType () != MessageType.ENDEAT)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 39!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.CHTWC) || (inMessage.getStudentState () > StudentStates.EJYML))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 13!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    } 

    /**
   *  Operation has everybody finished.
   *
   *  It is called by the student to check if everybody finished.
   *
   *    @return true, if everybody finish -
   *            false, otherwise
   */
    public boolean hasEverybodyFinished() {
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
        outMessage = new Message (MessageType.EVBDFINISHREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.EVBDFINISH)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 40!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.CHTWC))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 14!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
        return inMessage.getBoolVal();
    }

    /**
   *  Operation wait for everybody to finish.
   *
   *  It is called by the student to wait until everybody finished.
   *
   */
    public void waitForEverybodyToFinish() {
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
        outMessage = new Message (MessageType.WAITEVBDFINISHREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITEVBDFINISH)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 41!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.CHTWC))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 15!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation wait for course to be ready.
   *
   *  It is called by the student to wait for course to be ready.
   *
   */
    public void waitForCourseToBeReady() {
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
        outMessage = new Message (MessageType.CSREADYREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.CSREADY)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 42!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.CHTWC))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 16!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation wait for payment.
   *
   *  It is called by the student to wait for the last student to pay.
   *
   */
    public void waitForPayment() {
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
        outMessage = new Message (MessageType.WTPAYREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WTPAY)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 43!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.CHTWC))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 17!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation should have arrived earlier.
   *
   *  It is called by the last student to pay the bill.
   *
   */
    public void shouldHaveArrivedEarlier() {
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
        outMessage = new Message (MessageType.SHARRIVEDEARLIERREQ,((Student) Thread.currentThread()).getStudentID(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.SHARRIVEDEARLIER)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 44!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () < StudentStates.CHTWC) || (inMessage.getStudentState () > StudentStates.PYTBL))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 18!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation has honour the bill.
   *
   *  It is called by the last student to honour the bill.
   *
   */
    public void honourTheBill() {
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
        outMessage = new Message (MessageType.HONOURBILLREQ, ((Student) Thread.currentThread()).getStudentID(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.HONOURBILL)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 45!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.PYTBL))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 19!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
        com.close ();
    }

    /**
   *  Operation salute the client.
   *
   *  It is called by the waiter to salute the client that arrived to the restaurant.
   *
   *    @param req request
   */
    public void saluteTheClient(Request req) {
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
        outMessage = new Message (MessageType.SALUTECLIENTREQ, ((Waiter) Thread.currentThread()).getWaiterState(), req);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.SALUTECLIENT)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 46!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () < WaiterStates.APPST) || (inMessage.getWaiterState () > WaiterStates.PRSMN))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 7!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *  Operation get the pad.
   *
   *  It is called by the waiter to get the pad.
   *
   */
    public void getThePad() {
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
        outMessage = new Message (MessageType.GETPADREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.GETPAD)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 47!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () < WaiterStates.APPST) || (inMessage.getWaiterState () > WaiterStates.TKODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 8!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *  Operation have all clients been served.
   *
   *  It is called by the waiter to check if all clients have been served.
   *
   *    @return true, if everybody was served -
   *            false, otherwise
   */
    public boolean haveAllClientsBeenServed() {
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
        outMessage = new Message (MessageType.HVCLIENTSBEENSRVREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.HVCLIENTSBEENSRV)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 48!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () != WaiterStates.APPST))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 9!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    
        return (inMessage.getBoolVal());
    }

    /**
   *  Operation deliver portion.
   *
   *  It is called by the waiter to deliver portion to the table.
   *
   */
    public void deliverPortion() {
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
        outMessage = new Message (MessageType.DELVPTREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.DELVPT)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 49!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () != WaiterStates.WTFPT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 10!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
        com.close ();
    }

    /**
   *  Operation present the bill.
   *
   *  It is called by the waiter to present the bill.
   *
   */
    public void presentTheBill() {
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
        outMessage = new Message (MessageType.PRESBILLREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.PRESBILL)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 50!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getWaiterState () < WaiterStates.PRCBL) || (inMessage.getWaiterState () > WaiterStates.RECPM))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state 11!");
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
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 51!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
   *  Operation take a seat.
   *
   *  It is called by the student to take a seat at the table.
   *
   *    @param studentID student id
   *    @param studentState student state
   */
    public void takeASeat(int studentID, int studentState) 
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
        outMessage = new Message (MessageType.TAKESEAT,studentID, studentState);
        ////System.out.printf("state: %d \n", studentState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.TAKESEATDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 52!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.TKSTT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 20!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        //((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
    }

    /**
   *  Operation wait for pad.
   *
   *  It is called by the student to wait for waiter to get the pad.
   *
   *    @param studentID student id
   *    @param studentState student state
   */
    public void waitForPad(int studentID, int studentState) 
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
        outMessage = new Message (MessageType.WAITPAD, studentID,studentState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType () != MessageType.WAITPADDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type 53!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getStudentState () != StudentStates.OGODR))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state 21!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        //((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
    }

    
}
