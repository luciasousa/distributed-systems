package serverSide.sharedRegions;
import clientSide.entities.*;
import clientSide.main.*;
import commInfra.*;
import serverSide.entities.TableClientProxy;
/**
 *  Interface to the Table.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Kitchen and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class TableInterface {
    /**
    *  Reference to the bar.
    */

    private final Table table;

    /**
   *  Instantiation of an interface to the table.
   *
   *    @param table reference to the table
   */
    public TableInterface(Table table)
    {
        this.table = table;
    }

    /**
   *  Processing of the incoming messages.
   *
   *  Validation, execution of the corresponding method and generation of the outgoing message.
   *
   *    @param inMessage service request
   *    @return service reply
   *    @throws MessageException if the incoming message is not valid
   */

    public Message processAndReply(Message inMessage) throws MessageException
    {
        Message outMessage = null;                                     // outgoing message

        /* validation of the incoming message */

        switch(inMessage.getMsgType())
        {
            case MessageType.READMENUREQ:   if ((inMessage.getStudentID () < 0) || (inMessage.getStudentID () >= Constants.N))
                                              throw new MessageException ("Invalid student id!", inMessage);
                                            else if ((inMessage.getStudentState () < StudentStates.TKSTT) || (inMessage.getStudentState () > StudentStates.SELCS))
                                              throw new MessageException ("Invalid student state 28!", inMessage);
                                            break;

            case MessageType.INFCOMPREQ:    if ((inMessage.getStudentID () < 0) || (inMessage.getStudentID () >= Constants.N))
                                              throw new MessageException ("Invalid student id!", inMessage);
                                            else if((inMessage.getStudentState() < StudentStates.SELCS) || (inMessage.getStudentState() > StudentStates.CHTWC ))
                                              throw new MessageException ("Invalid student state 29!", inMessage);
                                            break;
                            
            case MessageType.PREPORDERREQ:    if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                              throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState () < StudentStates.SELCS) || (inMessage.getStudentState () > StudentStates.OGODR))
                                              throw new MessageException ("Invalid student state 30", inMessage);
                                            break;

            case MessageType.EVBDCHOSENREQ:  if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState() != StudentStates.OGODR))
                                                throw new MessageException ("Invalid student state 31", inMessage);
                                            break;

            case MessageType.ADDUPCHCREQ:   if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState() != StudentStates.OGODR))
                                                throw new MessageException ("Invalid student state 32", inMessage);
                                            break;

            case MessageType.DESCORDERREQ:  if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState() != StudentStates.OGODR))
                                                throw new MessageException ("Invalid student state 33", inMessage);
                                            break;

            case MessageType.JOINTALKREQ:   if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState () < StudentStates.OGODR) || (inMessage.getStudentState () > StudentStates.CHTWC))
                                                throw new MessageException ("Invalid student state 34", inMessage);
                                            break;

            case MessageType.STARTEATREQ:   if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState () < StudentStates.CHTWC) || (inMessage.getStudentState () > StudentStates.EJYML))
                                                throw new MessageException ("Invalid student state 35", inMessage);
                                            break;

            case MessageType.ENDEATREQ:     if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState () < StudentStates.CHTWC) || (inMessage.getStudentState () > StudentStates.EJYML))
                                                throw new MessageException ("Invalid student state 36", inMessage);
                                            break;

            case MessageType.EVBDFINISHREQ: ////System.out.printf("state37: %d\n", inMessage.getStudentState());
                                            if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState () != StudentStates.CHTWC))
                                                throw new MessageException ("Invalid student state 37", inMessage);
                                            break;

            case MessageType.WAITEVBDFINISHREQ: if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                    throw new MessageException ("Invalid student id", inMessage);
                                                else if((inMessage.getStudentState () != StudentStates.CHTWC))
                                                    throw new MessageException ("Invalid student state 37", inMessage);
                                                break;

            case MessageType.CSREADYREQ:    ////System.out.printf("state38: %d\n", inMessage.getStudentState());
                                            if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState () != StudentStates.CHTWC))
                                                throw new MessageException ("Invalid student state 38", inMessage);
                                            break;

            case MessageType.WTPAYREQ:  if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                            throw new MessageException ("Invalid student id", inMessage);
                                        else if((inMessage.getStudentState () != StudentStates.CHTWC))
                                            throw new MessageException ("Invalid student state 39", inMessage);
                                        break;

            case MessageType.SHARRIVEDEARLIERREQ:   if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                        throw new MessageException ("Invalid student id", inMessage);
                                                    else if((inMessage.getStudentState () < StudentStates.CHTWC) || (inMessage.getStudentState () > StudentStates.PYTBL))
                                                        throw new MessageException ("Invalid student state 40", inMessage);
                                                    break;

            case MessageType.HONOURBILLREQ:     if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                    throw new MessageException ("Invalid student id", inMessage);
                                                else if((inMessage.getStudentState () != StudentStates.PYTBL))
                                                    throw new MessageException ("Invalid student state 41", inMessage);
                                                break;

            case MessageType.SALUTECLIENTREQ:   if((inMessage.getWaiterState () < WaiterStates.APPST) || (inMessage.getWaiterState () > WaiterStates.PRSMN))
                                                    throw new MessageException ("Invalid waiter state 21", inMessage);
                                                break;                                                

            case MessageType.GETPADREQ:     if((inMessage.getWaiterState () < WaiterStates.APPST) || (inMessage.getWaiterState () > WaiterStates.TKODR))
                                                throw new MessageException ("Invalid waiter state 22", inMessage);
                                            break;   

            case MessageType.HVCLIENTSBEENSRVREQ:   ////System.out.printf("Waiter state= %d \n", inMessage.getWaiterState());
                                                    if(inMessage.getWaiterState () != WaiterStates.APPST)
                                                        throw new MessageException ("Invalid waiter state 23", inMessage);
                                                    break; 

            case MessageType.DELVPTREQ:     if(inMessage.getWaiterState () != WaiterStates.WTFPT)
                                                throw new MessageException ("Invalid waiter state 24", inMessage);
                                            break; 

            case MessageType.PRESBILLREQ:   if((inMessage.getWaiterState () < WaiterStates.PRCBL) || (inMessage.getWaiterState () > WaiterStates.RECPM))
                                                throw new MessageException ("Invalid waiter state 25", inMessage);
                                            break; 


            case MessageType.TAKESEAT:    if((inMessage.getStudentState () != StudentStates.TKSTT))
                                        throw new MessageException ("Invalid student state 42", inMessage);
                                        break;  

            case MessageType.WAITPAD:    if((inMessage.getStudentState () != StudentStates.OGODR))
                                        throw new MessageException ("Invalid student state 43", inMessage);
                                        break;  

            case MessageType.SHUT:          // check nothing
                                            break;
            default:                   throw new MessageException ("Invalid message type 57!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())

        { 
            case MessageType.READMENUREQ:   ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.readMenu();
                                            ////System.out.printf("Table interface -> student id: %d\n",(inMessage.getStudentID ()));
                                            outMessage = new Message (MessageType.READMENU,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.INFCOMPREQ:    ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.informCompanion();
                                            outMessage = new Message (MessageType.INFCOMP, ((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;
                          
            case MessageType.PREPORDERREQ:  ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.prepareTheOrder();
                                            outMessage = new Message (MessageType.PREPORDER, ((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.EVBDCHOSENREQ: ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            boolean everybodyChosen = table.hasEverybodyChosen();
                                                outMessage = new Message (MessageType.EVBDCHOSEN, ((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                        ((TableClientProxy) Thread.currentThread ()).getStudentState (), everybodyChosen);
                                            break;

            case MessageType.ADDUPCHCREQ:   ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.addUpOnesChoice();
                                            outMessage = new Message (MessageType.ADDUPCHC, ((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.DESCORDERREQ:  ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.describeTheOrder();
                                            outMessage = new Message (MessageType.DESCORDER, ((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.JOINTALKREQ:   ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.joinTheTalk();
                                            outMessage = new Message (MessageType.JOINTALK, ((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.STARTEATREQ:   ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.startEating();
                                            outMessage = new Message (MessageType.STARTEAT,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.ENDEATREQ: ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                        ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                        table.endEating();
                                        outMessage = new Message (MessageType.ENDEAT,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                        break;

            case MessageType.EVBDFINISHREQ: ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            boolean everybodyFinish = table.hasEverybodyFinished();
                                            outMessage = new Message (MessageType.EVBDFINISH,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                        ((TableClientProxy) Thread.currentThread ()).getStudentState (), everybodyFinish);
                                           break;

            case MessageType.WAITEVBDFINISHREQ: ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                                ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                                table.waitForEverybodyToFinish();
                                                outMessage = new Message (MessageType.WAITEVBDFINISH,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                        ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                                break;

            case MessageType.CSREADYREQ:    ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.waitForCourseToBeReady();
                                            outMessage = new Message (MessageType.CSREADY,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.WTPAYREQ:  ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                        ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                        table.waitForPayment();
                                        outMessage = new Message (MessageType.WTPAY,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                        break;

            case MessageType.SHARRIVEDEARLIERREQ:   ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                                    ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                                    table.shouldHaveArrivedEarlier();
                                                    outMessage = new Message (MessageType.SHARRIVEDEARLIER,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                            ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                                    break;

            case MessageType.HONOURBILLREQ: ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            table.honourTheBill();
                                            outMessage = new Message (MessageType.HONOURBILL,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.SALUTECLIENTREQ:   ((TableClientProxy) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
                                                //salute the client needs student ID ???
                                                ////System.out.printf("Request id: %d \n",(inMessage.getRequest()).getRequestID());
                                                table.saluteTheClient((inMessage.getRequest()).getRequestID());
                                                outMessage = new Message (MessageType.SALUTECLIENT,
                                                        ((TableClientProxy) Thread.currentThread ()).getWaiterState ());
                                                break;                                          

            case MessageType.GETPADREQ: ((TableClientProxy) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
                                        table.getThePad();
                                        outMessage = new Message (MessageType.GETPAD,
                                                ((TableClientProxy) Thread.currentThread ()).getWaiterState ());
                                        break;              

            case MessageType.HVCLIENTSBEENSRVREQ:   ((TableClientProxy) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
                                                    boolean haveAllBeenServed = table.haveAllClientsBeenServed();
                                                        outMessage = new Message (MessageType.HVCLIENTSBEENSRV,
                                                                ((TableClientProxy) Thread.currentThread ()).getWaiterState (), haveAllBeenServed);
                                                    break;  

            case MessageType.DELVPTREQ: ((TableClientProxy) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
                                        table.deliverPortion();
                                        outMessage = new Message (MessageType.DELVPT,
                                                ((TableClientProxy) Thread.currentThread ()).getWaiterState ());
                                        break;  

            case MessageType.PRESBILLREQ:   ((TableClientProxy) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
                                            table.presentTheBill();
                                            outMessage = new Message (MessageType.PRESBILL,
                                                    ((TableClientProxy) Thread.currentThread ()).getWaiterState ());
                                            break;  

            case MessageType.TAKESEAT:      ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState(inMessage.getStudentState ());
                                            table.takeASeat();
                                            outMessage = new Message (MessageType.TAKESEATDONE,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;  
    
            case MessageType.WAITPAD:       ((TableClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((TableClientProxy) Thread.currentThread ()).setStudentState(inMessage.getStudentState ());
                                            table.waitForPad();
                                            outMessage = new Message (MessageType.WAITPADDONE,((TableClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((TableClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;  
            
            case MessageType.ENDOP:     table.endOperation();
                                            outMessage = new Message (MessageType.EOPDONE, inMessage.getStudentID(), inMessage.getStudentState());
                                            break;

            case MessageType.SHUT:        table.shutdown();
                                          outMessage = new Message (MessageType.SHUTDONE); 
                                          break;
        }
        return (outMessage);
    } 
}
