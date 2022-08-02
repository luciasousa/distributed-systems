package clientSide.entities;
import java.rmi.*;
import interfaces.*;
import serverSide.main.Constants;

/**
 *    Student thread.
 *
 *      It simulates the student life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
public class Student extends Thread{
    /**
     *  Student identification.
     */
    private int studentID;

    /**
     *  Student State.
     */
    private int studentState;

    /**
     *  Reference to the table.
     */
    private final TableInterface tableInterface;

    /**
     *  Reference to the bar.
     */
    private final BarInterface barInterface;

    /**
     *   Instantiation of a student thread.
     *
     *     @param studentID student id
     *     @param studentState student state
     *     @param tableInterface reference to table Interface
     *     @param barInterface reference to the bar Interface
     */
    public Student(int studentID,int studentState, TableInterface tableInterface, BarInterface barInterface)
    {
        this.studentID = studentID;
        this.studentState = studentState;
        this.tableInterface = tableInterface;
        this.barInterface = barInterface;
    }

    /**
     *   Set student id.
     *
     *     @param id student id
     */
    public void setStudentID(int id){
        studentID = id;
    }

    /**
     *   Get student id.
     *  
     *   @return stduent id
     */
    public int getStudentID(){
        return studentID;
    }

    /**
     *   Set student state.
     *
     *     @param state student state
     */
    public void setStudentState(int state){
        studentState = state;
    }

    /**
     *   Get student state.
     *
     *   @return student state
     */
    public int getStudentState(){
        return studentState;
    }

    /**
     *   Life cycle of the student.
     *   
     *   Starts at the state going to the restaurant 
     *   Ends when the student exits the restaurant
     * 
     */
    public void run() 
    {
        //System.out.println("student walk a bit");
        walkABit();
        //System.out.println("student enter");
        int[] orderOfArrival = enter(studentID);
        //System.out.println("student read menu");
        readMenu(studentID);
        if (orderOfArrival[0] != studentID){
            informCompanion(studentID);
            //System.out.printf("student %d inform companion left the talk\n",studentID);
        } 
        else
        {
            //System.out.printf("student %d prepare order\n", studentID);
            prepareTheOrder(studentID);
            while(!hasEverybodyChosen()) {
                //System.out.printf("student %d add up ones choice\n", studentID);
                addUpOnesChoice(studentID);
            }
            //System.out.println("student is going to call the waiter");
            callWaiter(studentID, studentState);
            //System.out.printf("student %d describe order\n", studentID);
            describeTheOrder();
            //System.out.printf("student %d join talk\n", studentID);
            joinTheTalk(studentID);
        }
        
        for(int i=0; i< Constants.M; i++)
        {
            //System.out.printf("student %d start eating\n", studentID);
            startEating(studentID);
            //System.out.printf("student %d end eating\n", studentID);
            endEating(studentID);
            //wait for everyone to finish
            if(!hasEverybodyFinished()){
                //System.out.printf("student %d wait for everyone to finish\n", studentID);
                waitForEverybodyToFinish();
            }
            //System.out.printf("student %d wait for course\n", studentID);
            waitForCourseToBeReady(studentID);
        }

        if(orderOfArrival[Constants.N-1] != studentID){
            //System.out.printf("student %d wait for payment\n", studentID);
            waitForPayment(studentID);
        }
            
        
        if(orderOfArrival[Constants.N-1] == studentID) 
        {
            //System.out.printf("last student %d signal waiter\n", studentID);
            signalTheWaiter(studentID);
            //System.out.printf("last student %d should have arrived earlier\n", studentID);
            shouldHaveArrivedEarlier(studentID);
            //System.out.printf("last student %d honour the bill\n", studentID);
            honourTheBill();
        }
        exit(studentID);
        //System.out.printf("student %d exit\n", studentID);
    }

    /**
     *  Living normal life.
     *
     *  Internal operation.
     */
    private void walkABit() {
        try { 
            Thread.sleep ((long) (1 + 40 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }

    /**
     *    Operation enter
     *
     *    Called by the student to enter in the restaurant
     *    puts a request for the waiter 
     *    signals waiter 
     *    while students are waiting to take a seat at the table
     *    @param studentID student id
     *    @return array of the students IDs in order of arrival
     * 
     */
    public int[] enter(int studentID)
    {
        try {
            return barInterface.enter(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *    Operation read menu
     *
     *    Called by the student to read the menu, wakes up waiter to signal that he has read the menu
     *    @param studentID student id
     */
    public void readMenu(int studentID){
        try {
            tableInterface.readMenu(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation inform Companion
     *
     *    Called by the student (that was not the first to arrive) to inform companion
     *    Signal first student that it has been informed and waits for the course to be ready
     *    @param studentID student id
     */
    public void informCompanion(int studentID){
        try {
            tableInterface.informCompanion(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation prepare the order
     *
     *    Called by the first student to arrive to prepare the order
     *    waits until gets the choices from companions
     *    @param studentID student id
     */
    public void prepareTheOrder(int studentID){
        try {
            tableInterface.prepareTheOrder(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation has everybody chosen
     *
     *    Called by the first student to arrive to check if every companion has chosen
     *    @return true if everybody has chosen
     * 
     */
    public boolean hasEverybodyChosen(){
        try {
            return tableInterface.hasEverybodyChosen();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

     /**
     *    Operation add up ones choice
     *
     *    Called by the first student to arrive to add up the companion's choice to the order
     *    waits until gets the choices from companions
     *    @param studentID student id
     */
    public void addUpOnesChoice(int studentID){
        try {
            tableInterface.addUpOnesChoice(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation call waiter
     *
     *    Called by the student to describe the order
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    while students are waiting in the table for waiter to get the pad
     *    @param studentID student id
     *    @param studentState student state
     */
    public void callWaiter(int studentID, int studentState){
        try {
            barInterface.callWaiter(studentID, studentState);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation describe the order
     *
     *    Called by the first student to arrive to describe the order
     *    signals waiter that the order was described
     * 
     */
    public void describeTheOrder(){
        try {
            tableInterface.describeTheOrder();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

     /**
     *    Operation join the talk
     *
     *    Called by the first student to arrive to join the talk
     *    waits until course is ready  
     *    @param studentID student id
     */
    public void joinTheTalk(int studentID){
        try {
            tableInterface.joinTheTalk(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation start eating
     *
     *    Called by the student to start eating
     *    waits a random time
     *    @param studentID student id
     */
    public void startEating(int studentID){
        try {
            tableInterface.startEating(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation end eating
     *
     *    Called by the student to end eating
     *    @param studentID student id
     */
    public void endEating(int studentID){
        try {
            tableInterface.endEating(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation has everybody finished
     *
     *    Called by the student to check if everybody has finished
     *    signals waiter that everybody finished eating
     *    waits for next course to be ready
     * 
     *    @return true if everybody has finished false otherwise
     */
    public boolean hasEverybodyFinished(){
        try {
            return tableInterface.hasEverybodyFinished();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *    Operation wait For Everybody To Finish
     *
     *    Called by the student to wait for the rest of the students to end eating.
     */
    public void waitForEverybodyToFinish(){
        try {
            tableInterface.waitForEverybodyToFinish();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation wait For Course To Be Ready
     *
     *    Called by the student to wait for next course.
     *    @param studentID student id
     */
    public void waitForCourseToBeReady(int studentID){
        try {
            tableInterface.waitForCourseToBeReady(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation wait for payment
     *
     *    Called by the students(except for the last one) to wait for payment
     *    students wait for last student to pay the bill
     *    @param studentID student id
     */
    public void waitForPayment(int studentID){
        try {
            tableInterface.waitForPayment(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation signal the waiter
     *
     *    Called by the last student to signal the waiter after everybody eaten and is time to pay
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    @param studentID student id
     */
    public void signalTheWaiter(int studentID){
        try {
            barInterface.signalTheWaiter(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation should have arrived earlier
     *
     *    Called by the last student to arrive to pay the bill
     *    student waits for the bill to be ready
     *    @param studentID student id
     */
    public void shouldHaveArrivedEarlier(int studentID)
    {
        try {
            tableInterface.shouldHaveArrivedEarlier(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation honour the bill
     *
     *    Called by the last student to arrive to honour the bill
     *    signals the rest of the students that he/she has paid the bill
     * 
     */
    public void honourTheBill(){
        try {
            tableInterface.honourTheBill();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation exit
     *
     *    Called by the student to exit the restaurant
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    waits for waiter to say goodbye 
     *    @param studentID student id
     * 
     */
    public void exit(int studentID){
        try {
            barInterface.exit(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}