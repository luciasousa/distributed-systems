package interfaces;
import java.rmi.*;

/**
 *   Operational interface of a remote object of type Table.
 *
 *     It provides the functionality to access the Table.
 */
public interface TableInterface extends Remote{
    /**
     *    Operation take a seat
     *
     *    Called by the student to take a seat at the table
     *    @param studentID student id
     *    @param studentState student state
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void takeASeat(int studentID, int studentState) throws RemoteException;

    /**
     *    Operation salute the client
     *
     *    Called by the waiter to salute the client
     * 
     *    @param studentID the ID of the student to be saluted by the waiter
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void saluteTheClient(int studentID) throws RemoteException;

    /**
     *    Operation read menu
     *
     *    Called by the student to read the menu, wakes up waiter to signal that he has read the menu
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void readMenu(int studentID) throws RemoteException;

    /**
     *    Operation prepare the order
     *
     *    Called by the first student to arrive to prepare the order
     *    waits until gets the choices from companions
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void prepareTheOrder(int studentID) throws RemoteException;

    /**
     *    Operation inform Companion
     *
     *    Called by the student (that was not the first to arrive) to inform companion
     *    Signal first student that it has been informed and waits for the course to be ready
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void informCompanion(int studentID) throws RemoteException;

    /**
     *    Operation add up ones choice
     *
     *    Called by the first student to arrive to add up the companion's choice to the order
     *    waits until gets the choices from companions
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void addUpOnesChoice(int studentID) throws RemoteException;
    
    /**
     *    Operation wait for pad
     *
     *    Called by the student when students calls the waiter
     *    waits until waiter has the pad
     *    @param studentID student id
     *    @param studentState student state
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void waitForPad(int studentID, int studentState) throws RemoteException;

    /**
     *    Operation get the pad
     *
     *    Called by the waiter to get the pad
     *    signal student that he got the pad and waits until student describes the order
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void getThePad() throws RemoteException;

    /**
     *    Operation describe the order
     *
     *    Called by the first student to arrive to describe the order
     *    signals waiter that the order was described
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void describeTheOrder() throws RemoteException;

    /**
     *    Operation has everybody chosen
     *
     *    Called by the first student to arrive to check if every companion has chosen
     *    @return boolean
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public boolean hasEverybodyChosen() throws RemoteException;

    /**
     *    Operation join the talk
     *
     *    Called by the first student to arrive to join the talk
     *    waits until course is ready
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void joinTheTalk(int studentID) throws RemoteException;

    /**
     *    Operation deliver portion
     *
     *    Called by the waiter to deliver portion
     *    signals students that portion was delivered
     *    waits for all students to eat the course
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void deliverPortion() throws RemoteException;

    /**
     *    Operation have all clients been served
     *
     *    Called by the waiter to check if all clients have been served
     *    @return true if all clients have been served false otherwise
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public boolean haveAllClientsBeenServed() throws RemoteException;

    /**
     *    Operation start eating
     *
     *    Called by the student to start eating
     *    waits a random time
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void startEating(int studentID) throws RemoteException;

    /**
     *    Operation end eating
     *
     *    Called by the student to end eating
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void endEating(int studentID) throws RemoteException;

    /**
     *    Operation has everybody finished
     *
     *    Called by the student to check if everybody has finished
     *    signals waiter that everybody finished eating
     *    waits for next course to be ready
     * 
     *    @return true if everybody has finished false otherwise
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public boolean hasEverybodyFinished() throws RemoteException;

    /**
     *    Operation wait For Everybody To Finish
     *
     *    Called by the student to wait for the rest of the students to end eating.
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void waitForEverybodyToFinish() throws RemoteException;

    /**
     *    Operation wait For Course To Be Ready
     *
     *    Called by the student to wait for next course.
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void waitForCourseToBeReady(int studentID) throws RemoteException;

    /**
     *    Operation wait for payment
     *
     *    Called by the students(except for the last one) to wait for payment
     *    students wait for last student to pay the bill
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void waitForPayment(int studentID) throws RemoteException;

    /**
     *    Operation present the bill
     *
     *    Called by the waiter to present the bill
     *    signals last student that bill is ready
     *    waits for student to pay
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void presentTheBill() throws RemoteException;

    /**
     *    Operation should have arrived earlier
     *
     *    Called by the last student to arrive to pay the bill
     *    student waits for the bill to be ready
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void shouldHaveArrivedEarlier(int studentID) throws RemoteException;

    /**
     *    Operation honour the bill
     *
     *    Called by the last student to arrive to honour the bill
     *    signals the rest of the students that he/she has paid the bill
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void honourTheBill() throws RemoteException;

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */

    public void shutdown () throws RemoteException;
}
