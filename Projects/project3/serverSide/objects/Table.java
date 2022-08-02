package serverSide.objects;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;

/**
 *    Table
 *
 *    It is responsible for the the synchronization of the Students and Waiter
 *    is implemented as an implicit monitor.
 *    There is one internal synchronization points: 
 *    a single blocking point for the Student, where he waits for the Waiter to signal
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class Table implements TableInterface{
 /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
   *  Reference to student threads.
   */
    private Thread[] student;

    /**
   *  Reference to waiter thread.
   */
    private Thread waiter;

    /**
     *  State of the waiter.
     */
    private int waiterState;

    /**
     *  State of the student.
     */
    private int [] studentState;

    /**
     *   Counter of the number of portions delivered.
     */
    private int numberOfPortionsDelivered;

    /**
     *   Counter of the number of courses delivered.
     */
    private int numberOfCoursesDelivered;

    /**
     *   Counter of the number of portions eaten.
     */
    private int numberOfPortionsEaten;

    /**
     *   Counter of the number of students requests.
     */
    private int numberOfStudentsRequests = 1;

    /**
     *   Reference to the general repository.
     */
    private final GeneralReposInterface reposInterface;

    /**
     *   Array of booleans that indicates if student read the menu.
     */
    private boolean[] menuRead;

    /**
     *   Boolean flag that indicates if was informed.
     */
    private boolean wasInformed = false;

    /**
     *   Boolean flag that indicates if waiter has the pad.
     */
    private boolean waiterHasThePad = false;

    /**
     *   Boolean flag that indicates if student has paid.
     */
    private boolean studentHasPaid = false;

    /**
     *   Array of booleans that indicates if clients were saluted.
     */
    private boolean[] clientsSaluted;

    /**
     *   Boolean flag that indicates if ordr was described.
     */
    private boolean orderDescribed;

    /**
     *   Array of booleans that indicates if course is ready.
     */
    private boolean []courseReady;

    /**
     *   Boolean flag that indicates if bill is ready.
     */
    private boolean billReady = false;

    /**
     *   Boolean flag that indicates if student has end eating.
     */
    private boolean hasEndedEating;

    /**
     *   Boolean flag that indicates if courses are complete.
     */
    private boolean coursesCompleted;

    /**
     *  Table instantiation.
     *
     *    @param repos reference to the General Information Repository
     */
    public Table(GeneralReposInterface reposInterface)
    {
        this.reposInterface = reposInterface;
        student = new Thread[Constants.N];
        studentState = new int[Constants.N];
        clientsSaluted = new boolean[Constants.N];
        courseReady = new boolean[Constants.M];
        menuRead = new boolean[Constants.N];
        nEntities=0;
        for(int i = 0; i< Constants.N;i++){
            student[i] = null;
            studentState[i] = -1;
        }
        
        waiter = null;
        waiterState = -1;
    }

    /**
     *    Operation take a seat
     *
     *    Called by the student to take a seat at the table
     *    @param studentID student id
     *    @param studentState student state
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void takeASeat(int studentID, int studentState) throws RemoteException
    {
        student[studentID] = Thread.currentThread();
        //System.out.printf("student %d take a seat\n", studentID);
        while(!clientsSaluted[studentID]) 
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    Operation salute the client
     *
     *    Called by the waiter to salute the client
     * 
     *    @param studentID the ID of the student to be saluted by the waiter
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void saluteTheClient(int studentID) throws RemoteException
    {
        waiter = Thread.currentThread();
        waiterState = WaiterStates.PRSMN;
        reposInterface.setWaiterState(waiterState);
        
        //System.out.printf("waiter salute the client %d", studentID);
        clientsSaluted[studentID] = true;
        notifyAll();
        while(!menuRead[studentID])
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    Operation read menu
     *
     *    Called by the student to read the menu, wakes up waiter to signal that he has read the menu
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void readMenu(int studentID) throws RemoteException
    {
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.SELCS;
        reposInterface.setStudentState(studentID, studentState[studentID]);
        
        //System.out.printf("student %d read menu\n", studentID);
        menuRead[studentID] = true;
        //signal waiter that menu was read
        notifyAll();
    }

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
    public synchronized void prepareTheOrder(int studentID) throws RemoteException
    {
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.OGODR;
        reposInterface.setStudentState(studentID, studentState[studentID]);
        
        //System.out.printf("student %d prepare the order\n", studentID);
        while (!wasInformed) 
        {    
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
    public synchronized void informCompanion(int studentID) throws RemoteException
    {
        wasInformed = false;
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.CHTWC;
        reposInterface.setStudentState(studentID, studentState[studentID]);
        
        //System.out.printf("student %d inform companion\n", studentID);
        wasInformed = true;
        numberOfStudentsRequests += 1;
        notifyAll();
        while(!courseReady[numberOfCoursesDelivered]) 
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
    public synchronized void addUpOnesChoice(int studentID) throws RemoteException
    {
        //System.out.printf("student %d has been informed\n", studentID);
        while(!wasInformed)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
    }
    
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
    public synchronized void waitForPad(int studentID, int studentState) throws RemoteException
    {     
        while(!waiterHasThePad) 
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("waiter has the pad");
    }

    /**
     *    Operation get the pad
     *
     *    Called by the waiter to get the pad
     *    signal student that he got the pad and waits until student describes the order
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized void getThePad() throws RemoteException
    { 
        waiter = Thread.currentThread();
        waiterState = WaiterStates.TKODR;
        reposInterface.setWaiterState(waiterState);
        
        //System.out.println("waiter get the pad");
        waiterHasThePad = true;
        notifyAll(); 
        while(!orderDescribed)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    Operation describe the order
     *
     *    Called by the first student to arrive to describe the order
     *    signals waiter that the order was described
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized void describeTheOrder() throws RemoteException
    {
        //System.out.println("order is described");
        orderDescribed = true;
        notifyAll();
    }

    /**
     *    Operation has everybody chosen
     *
     *    Called by the first student to arrive to check if every companion has chosen
     *    @return true if everybody has chosen false otherwise
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized boolean hasEverybodyChosen() throws RemoteException
    {
        //System.out.printf("pedidos dos estudantes = %d\n", numberOfStudentsRequests);
        if (numberOfStudentsRequests == Constants.N) return true; else return false;
    }

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
    public synchronized void joinTheTalk(int studentID) throws RemoteException
    {
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.CHTWC;
        reposInterface.setStudentState(studentID, studentState[studentID]);
        
        //System.out.println("first student has joined the talk");
        while(!courseReady[numberOfCoursesDelivered])
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
    public synchronized void deliverPortion() throws RemoteException
    {
        numberOfPortionsEaten = 0;
        numberOfPortionsDelivered += 1;
        hasEndedEating = false;
        //System.out.printf("waiter is delivering the portion %d\n", numberOfPortionsDelivered);
        if(numberOfPortionsDelivered == Constants.N)
        {
            //System.out.printf("course nº %d finished\n", numberOfCoursesDelivered);
            courseReady[numberOfCoursesDelivered] = true;
            notifyAll();
            if(numberOfCoursesDelivered == Constants.M - 1) coursesCompleted = true;
            while(!hasEndedEating)
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            numberOfPortionsDelivered = 0;
            if(numberOfCoursesDelivered < Constants.M - 1) numberOfCoursesDelivered += 1;
        }
    }

    /**
     *    Operation have all clients been served
     *
     *    Called by the waiter to check if all clients have been served
     *    @return true if all clients have been served
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public boolean haveAllClientsBeenServed() throws RemoteException
    {
        if(numberOfPortionsDelivered == Constants.N) return true; else return false;
    }

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
    public synchronized void startEating(int studentID) throws RemoteException
    {
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.EJYML;
        reposInterface.setStudentState(studentID, studentState[studentID]);

        //System.out.printf("student %d has started eating, course: %d\n", studentID, numberOfCoursesDelivered);
        try {
            wait((long) (1 + 500 * Math.random ()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation end eating
     *
     *    Called by the student to end eating
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized void endEating(int studentID) throws RemoteException
    {
        numberOfPortionsEaten += 1;
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.CHTWC;
        reposInterface.setStudentState(studentID, studentState[studentID]);
        
        //System.out.printf("student %d has finished eating\n",studentID);
    }

    /**
     *    Operation has everybody finished
     *
     *    Called by the student to check if everybody has finished
     *    signals waiter that everybody finished eating
     *    waits for next course to be ready
     * 
     *    @return true if everybody finished false otherwise
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized boolean hasEverybodyFinished() throws RemoteException
    {
        if(numberOfPortionsEaten == Constants.N) 
        {
            hasEndedEating = true;
            notifyAll();
            courseReady[numberOfCoursesDelivered] = false;
            return true; 
        } else return false;
    }

    /**
     *    Operation wait For Everybody To Finish
     *
     *    Called by the student to wait for the rest of the students to end eating.
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void waitForEverybodyToFinish() throws RemoteException
    {
        
        while(!hasEndedEating)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    Operation wait For Course To Be Ready
     *
     *    Called by the student to wait for next course.
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void waitForCourseToBeReady(int studentID) throws RemoteException
    {
        while(!(courseReady[numberOfCoursesDelivered] || coursesCompleted))
        {
            try {
                //System.out.printf("student %d waiting for course ...\n", studentID);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
    public synchronized void waitForPayment(int studentID) throws RemoteException
    {
        //System.out.printf("Student %d is waiting for payment\n", studentID);
        while(!studentHasPaid)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
    public synchronized void presentTheBill() throws RemoteException
    {
        waiter = Thread.currentThread();
        waiterState = WaiterStates.RECPM;
        reposInterface.setWaiterState(waiterState);
        //}
        
        //System.out.println("presenting the bill");
        billReady = true;
        notifyAll();
        while(!studentHasPaid)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
    public synchronized void shouldHaveArrivedEarlier(int studentID) throws RemoteException
    {
        student[studentID] = Thread.currentThread();
        studentState[studentID] = StudentStates.PYTBL;
        reposInterface.setStudentState(studentID, studentState[studentID]);

        //System.out.printf("student %d should have arrived earlier, pay the bill\n",studentID);
        while(!billReady)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    Operation honour the bill
     *
     *    Called by the last student to arrive to honour the bill
     *    signals the rest of the students that he/she has paid the bill
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized void honourTheBill() throws RemoteException
    {
        //System.out.println("bill has been honoured");
        studentHasPaid = true;
        notifyAll();
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */

    public synchronized void shutdown () throws RemoteException
    {
        nEntities += 1;
        if (nEntities >= Constants.ET)
            ServerTable.shutdown();
        notifyAll ();                                        // the table may now terminate
    }
}
