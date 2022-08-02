package interfaces;
import java.rmi.*;

/**
 *   Operational interface of a remote object of type GeneralRepos.
 *
 *     It provides the functionality to access the General Repository of Information.
 */
public interface GeneralReposInterface extends Remote{

    /**
     *  Write the header and the initial states to the logging file.
     *
     *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void reportInitialStatus() throws RemoteException;

    /**
    *   Set chef state.
    *
    *     @param state chef state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setChefState (int state) throws RemoteException;

   /**
    *   Set waiter state.
    *
    *     @param state waiter state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setWaiterState (int state) throws RemoteException;

   /**
    *   Set student state.
    *     @param studentID student id
    *     @param state student state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setStudentState (int studentID, int state) throws RemoteException;

    /**
    *   Set student, chef and waiter states.
    *
    *     @param cState chef state
    *     @param wState waiter state
    *     @param sID student id
    *     @param sState student state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setChefWaiterStudentState (int cState, int wState, int sID, int sState) throws RemoteException;

   /**
    *   Update Counter numberOfPortion
    *
    *     @param nPortions integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setNumberOfPortions(int nPortions) throws RemoteException;

    /**
    *   Update Counter numberOfCourse
    *
    *     @param nCourses integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setNumberOfCourses(int nCourses) throws RemoteException;

    /**
    *   Update Counter numberOfPortions and numberOfCourses
    *
    *     @param nPortions integer
    *     @param nCourses integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setNumberOfPortionsAndCourses(int nPortions, int nCourses) throws RemoteException;

    /**
    *   Update chef state, numberOfPortions and numberOfCourses
    *
    *     @param state chef state
    *     @param nPortions portion number
    *     @param nCourses course number
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setStatePortionsCourses(int state, int nPortions, int nCourses) throws RemoteException;

    /**
    *   Update chef state and numberOfPortions
    *
    *     @param state chef state
    *     @param nPortions portion number
    *     @throws RemoteException
    */
    public void setStatePortions(int state, int nPortions) throws RemoteException;

    /**
    *   Update student state and seat
    *
    *     @param state chef state
    *     @param id
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setStudentStateAndLeave(int state, int id) throws RemoteException;

    /**
    *   Update Array seatOrder
    *
    *     @param id integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void setSeatOrder(int id) throws RemoteException;

    /**
     *   Operation initialization of simulation.
     *
     *   New operation.
     *
     *     @param logFileName name of the logging file
     *     @param nIter number of iterations of the student life cycle
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void initSimul (String logFileName, int nIter) throws RemoteException;

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void shutdown () throws RemoteException;

    /**
     *  Write a state line at the end of the logging file.
     *
     *  The current state of entities is organized in a line to be printed.
     *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void reportStatus() throws RemoteException;

    /**
    *   Write to the logging file if operation of opening for appending the file or operation of closing the file failed.
    *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public void printSumUp() throws RemoteException;
}
