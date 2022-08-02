package serverSide.sharedRegions;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import commInfra.Request;
import serverSide.entities.BarClientProxy;
import clientSide.entities.*;
import serverSide.main.Constants;

/**
 *  Interface to the Bar.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Bar and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class BarInterface
{
    /**
    *  Reference to the bar.
    */

    private final Bar bar;

    /**
   *  Instantiation of an interface to the bar.
   *
   *    @param bar reference to the bar
   */
    public BarInterface(Bar bar)
    {
        this.bar = bar;
        
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
        ////System.out.println(inMessage.getMsgType());
        switch(inMessage.getMsgType())
        {
            case MessageType.ALERTWAITER:   ////System.out.printf("Chef state= %d\n", inMessage.getChefState ());
                                            if ((inMessage.getChefState () < ChefStates.DSHPT) || (inMessage.getChefState () > ChefStates.DLVPT))
                                              throw new MessageException ("Invalid chef state 12!", inMessage);
                                            break;

            case MessageType.ENTERSTUDENT:  if ((inMessage.getStudentID () < 0) || (inMessage.getStudentID () >= Constants.N))
                                              throw new MessageException ("Invalid student id!", inMessage);
                                            else if((inMessage.getStudentState() < StudentStates.GGTRT) || (inMessage.getStudentState() > StudentStates.TKSTT ))
                                              throw new MessageException ("Invalid student state 22!", inMessage);
                                            break;
                            
            case MessageType.CALLWAITER:    if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                              throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState() != StudentStates.OGODR))
                                              throw new MessageException ("Invalid student state 23", inMessage);
                                            break;

            case MessageType.SIGNALWAITER:  if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState() != StudentStates.CHTWC))
                                                throw new MessageException ("Invalid student state 24", inMessage);
                                            break;

            case MessageType.EXITSTUDENT:   if((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                throw new MessageException ("Invalid student id", inMessage);
                                            else if((inMessage.getStudentState() < StudentStates.CHTWC) || (inMessage.getStudentState() > StudentStates.GGHOM))
                                                throw new MessageException ("Invalid student state 25", inMessage);
                                            break;

            case MessageType.LOOKWAITER:    if(inMessage.getWaiterState() != WaiterStates.APPST)
                                              throw new MessageException ("Invalid waiter state 12", inMessage);
                                            break;

            case MessageType.RETURNWAITER:  if(false)
                                              throw new MessageException ("Invalid waiter state 13", inMessage);
                                            break;

            case MessageType.COLLECTWAITER: if((inMessage.getWaiterState() < WaiterStates.APPST) || (inMessage.getWaiterState() > WaiterStates.WTFPT))
                                              throw new MessageException ("Invalid waiter state 14", inMessage);
                                            break;

            case MessageType.PREPAREWAITER: if((inMessage.getWaiterState() < WaiterStates.APPST) || (inMessage.getWaiterState() > WaiterStates.PRCBL))
                                              throw new MessageException ("Invalid waiter state 15", inMessage);
                                            break;                                                

            case MessageType.SAYGOODBYE:    ////System.out.printf("state16: %d\n",inMessage.getWaiterState());
                                            if(inMessage.getWaiterState() != WaiterStates.APPST)
                                              throw new MessageException ("Invalid waiter state 16", inMessage);
                                            break;   

            case MessageType.SHUT:          // check nothing
                                            break;
            default:                   throw new MessageException ("Invalid message type 54!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())

        { 
            case MessageType.ALERTWAITER:   ((BarClientProxy) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState ());
                                            bar.alertTheWaiter();
                                            outMessage = new Message (MessageType.WAITERALERTED,
                                                    ((BarClientProxy) Thread.currentThread ()).getChefState ());
                                            break;

            case MessageType.ENTERSTUDENT:  ////System.out.printf("message: ");
                                            ((BarClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((BarClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            int[] orderOfArrival = bar.enter();
                                            outMessage = new Message (MessageType.STUDENTENTERED,((BarClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((BarClientProxy) Thread.currentThread ()).getStudentState (), orderOfArrival);
                                                    ////System.out.printf("message: %s\n",outMessage);
                                            
                                            break;
                            
            case MessageType.CALLWAITER:    ((BarClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((BarClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            bar.callWaiter();
                                            outMessage = new Message (MessageType.WAITERCALLED, ((BarClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((BarClientProxy) Thread.currentThread ()).getStudentState ());
                                            break; 

            case MessageType.SIGNALWAITER:  ((BarClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((BarClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            bar.signalTheWaiter();
                                            outMessage = new Message (MessageType.WAITERSIGNALED,((BarClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((BarClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.EXITSTUDENT:   ((BarClientProxy) Thread.currentThread ()).setStudentID (inMessage.getStudentID ());
                                            ((BarClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
                                            bar.exit();
                                            outMessage = new Message (MessageType.STUDENTEXITED,((BarClientProxy) Thread.currentThread ()).getStudentID (),
                                                    ((BarClientProxy) Thread.currentThread ()).getStudentState ());
                                            break;

            case MessageType.LOOKWAITER:    ((BarClientProxy) Thread.currentThread ()).setWaiterState(inMessage.getWaiterState ());
                                            //GenericIO.writeString("Look around request\n");
                                            Request r = bar.lookAround();
                                            outMessage = new Message (MessageType.WAITERLOOKED,
                                                    ((BarClientProxy) Thread.currentThread ()).getWaiterState (), r);
                                            break;

            case MessageType.RETURNWAITER:  ((BarClientProxy) Thread.currentThread ()).setWaiterState(inMessage.getWaiterState ());
                                            bar.returnToBar();
                                            outMessage = new Message (MessageType.WAITERRETURNED,
                                                    ((BarClientProxy) Thread.currentThread ()).getWaiterState ());
                                            break;

            case MessageType.COLLECTWAITER: ((BarClientProxy) Thread.currentThread ()).setWaiterState(inMessage.getWaiterState ());
                                            bar.collectPortion();
                                            outMessage = new Message (MessageType.WAITERCOLLECTED,
                                                    ((BarClientProxy) Thread.currentThread ()).getWaiterState ());
                                            break;

            case MessageType.PREPAREWAITER: ((BarClientProxy) Thread.currentThread ()).setWaiterState(inMessage.getWaiterState ());
                                            bar.prepareTheBill();
                                            outMessage = new Message (MessageType.WAITERPREPARED,
                                                    ((BarClientProxy) Thread.currentThread ()).getWaiterState ());
                                            break;                                             

            case MessageType.SAYGOODBYE:  ((BarClientProxy) Thread.currentThread ()).setWaiterState(inMessage.getWaiterState ());
                                          int studentID = inMessage.getRequest().getRequestID();
                                          int numberOfStudentsInRest = bar.sayGoodbye(studentID);
                                          outMessage = new Message (MessageType.SAYGOODBYEDONE,
                                                  ((BarClientProxy) Thread.currentThread ()).getWaiterState (), numberOfStudentsInRest);
                                          break; 
                                          
            case MessageType.ENDOP:     bar.endOperation();
                                          outMessage = new Message (MessageType.EOPDONE, inMessage.getWaiterState());
                                          break;

            case MessageType.SHUT:        bar.shutdown();
                                          outMessage = new Message (MessageType.SHUTDONE); 
                                          break;
        }
        return (outMessage);
    } 
}
