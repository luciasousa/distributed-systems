package serverSide.sharedRegions;
import clientSide.entities.ChefStates;
import clientSide.entities.StudentStates;
import clientSide.entities.Waiter;
import clientSide.entities.WaiterStates;
import commInfra.*;
import serverSide.main.Constants;
/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralReposInterface
{
    /**
     *  Reference to the general repository.
     */
    private final GeneralRepos repos;

    /**
     *  Instantiation of an interface to the general repository.
     *
     *    @param repos reference to the general repository
     */
    public GeneralReposInterface(GeneralRepos repos)
    {
       this.repos = repos;
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
    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;                                     // mensagem de resposta

        /* validation of the incoming message */
        //System.out.printf("Message type: %d\n",inMessage.getMsgType());
        switch (inMessage.getMsgType ())
        { 
        
            case MessageType.SETNFIC: if (inMessage.getLogFName () == null)
                                        throw new MessageException ("Name of the logging file is not present!", inMessage);
                                        else if (inMessage.getNIter () <= 0)
                                            throw new MessageException ("Wrong number of iterations!", inMessage);
                                    break;

            case MessageType.SETCHEFSTATE:  if ((inMessage.getChefState() < ChefStates.WAFOR) || (inMessage.getChefState() > ChefStates.CLSSV))
                                                throw new MessageException ("Invalid chef state 13!", inMessage);
                                            break;

            case MessageType.SETWAITERSTATE:    if ((inMessage.getWaiterState() < WaiterStates.APPST) || (inMessage.getWaiterState() > WaiterStates.RECPM))
                                                    throw new MessageException ("Invalid waiter state 17!", inMessage);
                                                break;

            case MessageType.SETSTUDENTSTATE:   if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                    throw new MessageException ("Invalid student id!", inMessage);
                                                else if ((inMessage.getStudentState() < StudentStates.GGTRT) || (inMessage.getStudentState() > StudentStates.GGHOM))
                                                    throw new MessageException ("Invalid student state 26!", inMessage);
                                                break;

            case MessageType.SETSTATES: if ((inMessage.getChefState() < ChefStates.WAFOR) || (inMessage.getChefState() > ChefStates.CLSSV))
                                            throw new MessageException ("Invalid chef state 14!", inMessage);
                                        
                                        if ((inMessage.getWaiterState() < WaiterStates.APPST) || (inMessage.getWaiterState() > WaiterStates.RECPM))
                                            throw new MessageException ("Invalid waiter state 18!", inMessage);
                                        
                                        if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                            throw new MessageException ("Invalid student id!", inMessage);
                                        else if ((inMessage.getStudentState() < StudentStates.GGTRT) || (inMessage.getStudentState() > StudentStates.GGHOM))
                                            throw new MessageException ("Invalid student state 27!", inMessage);
                                        break;

            case MessageType.SETNUMBERCOURSES:    if ((inMessage.getNumberOfCourses() < 0 || inMessage.getNumberOfCourses() > Constants.M))
                                                    throw new MessageException ("Invalid course number!", inMessage);
                                                  break;

            case MessageType.SETNUMBERPORTIONS:  if ((inMessage.getNumberOfPortions() < 0 || inMessage.getNumberOfPortions() > Constants.N))
                                                    throw new MessageException ("Invalid portion number!", inMessage);
                                                break;

            case MessageType.SETSEAT:   if ((inMessage.getSeatNumber() < 0 || inMessage.getSeatNumber() > Constants.N))
                                            throw new MessageException ("Invalid seat number!", inMessage);
                                        break;

            case MessageType.SETCOURSESPORTIONS:    if ((inMessage.getNumberOfCourses() < 0 || inMessage.getNumberOfCourses() > Constants.M))
                                                        throw new MessageException ("Invalid number of courses!", inMessage);
                                                    else if((inMessage.getNumberOfPortions() < 0 || inMessage.getNumberOfPortions() > Constants.N))
                                                        throw new MessageException ("Invalid number of portions!", inMessage);
                                                    break;

            case MessageType.SETSTATECOURSEPORT:    if(inMessage.getChefState() < ChefStates.PRPCS || inMessage.getChefState() > ChefStates.DLVPT)
                                                        throw new MessageException ("Invalid chef state!", inMessage);
                                                    else if ((inMessage.getNumberOfCourses() < 0 || inMessage.getNumberOfCourses() > Constants.M))
                                                        throw new MessageException ("Invalid number of courses!", inMessage);
                                                    else if((inMessage.getNumberOfPortions() < 0 || inMessage.getNumberOfPortions() > Constants.N))
                                                        throw new MessageException ("Invalid number of portions!", inMessage);
                                                    break;

            case MessageType.SETSTATEPORTION:   if(inMessage.getChefState() < ChefStates.PRPCS || inMessage.getChefState() > ChefStates.DLVPT)
                                                    throw new MessageException ("Invalid chef state!", inMessage);
                                                else if((inMessage.getNumberOfPortions() < 0 || inMessage.getNumberOfPortions() > Constants.N))
                                                    throw new MessageException ("Invalid number of portions!", inMessage);
                                                break;

            case MessageType.SETSTATELEAVE:     if(inMessage.getStudentState() < StudentStates.CHTWC || inMessage.getStudentState() > StudentStates.GGHOM)
                                                    throw new MessageException ("Invalid student state 30!", inMessage);
                                                else if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() >= Constants.N))
                                                    throw new MessageException ("Invalid student id!", inMessage);
                                                break;

            case MessageType.SHUT:     // check nothing
                                        break;

            default:                   throw new MessageException ("Invalid message type 55!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())

        { 
            case MessageType.SETNFIC:   repos.initSimul(inMessage.getLogFName (), inMessage.getNIter ());
                                        outMessage = new Message (MessageType.NFICDONE);
                                        break;

            case MessageType.SETCHEFSTATE:  repos.setChefState (inMessage.getChefState());
                                            outMessage = new Message (MessageType.SACK);
                                            break; 

            case MessageType.SETWAITERSTATE:    repos.setWaiterState (inMessage.getWaiterState());
                                                outMessage = new Message (MessageType.SACK);
                                                break; 

            case MessageType.SETSTUDENTSTATE:   repos.setStudentState(inMessage.getStudentID(), inMessage.getStudentState());
                                                outMessage = new Message (MessageType.SACK);
                                                break; 

            case MessageType.SETSTATES: repos.setChefWaiterStudentState (inMessage.getChefState(), inMessage.getWaiterState(), inMessage.getStudentID(), inMessage.getStudentState());
                                        outMessage = new Message (MessageType.SACK);
                                        break;

            case MessageType.SETNUMBERCOURSES:  repos.setNumberOfCourses(inMessage.getNumberOfCourses());
                                                outMessage = new Message (MessageType.SACK);
                                                break;

            case MessageType.SETNUMBERPORTIONS: repos.setNumberOfPortions(inMessage.getNumberOfPortions());
                                                outMessage = new Message (MessageType.SACK);
                                                break;

            case MessageType.SETSEAT: repos.setSeatOrder(inMessage.getSeatNumber());
                                                outMessage = new Message (MessageType.SACK);
                                                break;

            case MessageType.SETCOURSESPORTIONS:    repos.setNumberOfPortionsAndCourses(inMessage.getNumberOfPortions(), inMessage.getNumberOfCourses());
                                                    outMessage = new Message (MessageType.SACK);
                                                    break;

            case MessageType.SETSTATECOURSEPORT:    repos.setStatePortionsCourses(inMessage.getChefState(), inMessage.getNumberOfPortions(), inMessage.getNumberOfCourses());
                                                    outMessage = new Message (MessageType.SACK);
                                                    break;

            case MessageType.SETSTATEPORTION:   repos.setStatePortions(inMessage.getChefState(), inMessage.getNumberOfPortions());
                                                outMessage = new Message (MessageType.SACK);
                                                break;

            case MessageType.SETSTATELEAVE:    repos.setStudentStateAndLeave(inMessage.getStudentState(), inMessage.getStudentID());
                                                    outMessage = new Message (MessageType.SACK);
                                                    break;
                        

            case MessageType.SHUT:  repos.shutdown();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break; 
        }

        return (outMessage);
    }
}
