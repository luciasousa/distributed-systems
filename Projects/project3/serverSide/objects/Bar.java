package serverSide.objects;
import commInfra.MemException;
import commInfra.MemFIFO;
import commInfra.Request;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;

/**
 *    Bar
 *
 *    It is responsible for the the synchronization of the Students and Waiter
 *    is implemented as an implicit monitor.
 *    There are internal synchronization points: 
 *    blocking point for the Waiter, where he waits for the Student to signal
 *    blocking point for the Waiter, where he waits for the Chef to signal
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class Bar implements BarInterface{
     /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *   Counter of the number of pending service requests.
     */
    private int numberOfPendingServiceRequests = 0;

    /**
     *  Reference to waiter thread.
     */
    private Thread waiter;

    /**
     *  Reference to students threads.
     */
    private Thread[] student;

    /**
     *  Reference to chef thread.
     */
    private Thread chef;

    /**
     *  State of the waiter.
     */
    private int waiterState;

    /**
     *  State of the chef.
     */
    private int chefState;

    /**
     *  State of the student.
     */
    private int [] studentState;

    /**
     *   FIFO with the pending service requests.
     */
    private MemFIFO<Request> pendingServiceRequests;

    /**
     *   FIFO with the IDs of the students in order of arrival.
     */
    private MemFIFO<Integer> arrivalQueue;

    /**
     *   Counter of the number of students in restaurant.
     */
    private int numberOfStudentsInRestaurant;

    /**
     *   Reference to the table.
     */
    private TableInterface tableInterface;

    /**
     *   Reference to the kitchen.
     */
    private KitchenInterface kitchenInterface;

    /**
     *   Reference to the general repository.
     */
    private final GeneralReposInterface reposInterface;

    /**
     *   Array with the IDs of the students in order of arrival.
     */
    private int[] studentsArrival;

    /**
     *   Array of booleans that indicates if waiter said goodbye to student.
     */
    private boolean[] clientsGoodbye;

    /**
     *  Table instantiation.
     *
     *    @param reposStub reference to the General Information Repository Stub
     */
    public Bar(GeneralReposInterface reposInterface, TableInterface tableInterface, KitchenInterface kitchenInterface)
    {
        try {
            pendingServiceRequests = new MemFIFO(new Request[Constants.N+1]);
        } catch (MemException e) {
            pendingServiceRequests = null;
            e.printStackTrace();
        }
        try {
            arrivalQueue = new MemFIFO(new Integer[Constants.N]);
        } catch (MemException e) {
            arrivalQueue = null;
            e.printStackTrace();
        }
        this.reposInterface = reposInterface;
        this.tableInterface = tableInterface;
        this.kitchenInterface = kitchenInterface;
        student = new Thread[Constants.N];
        studentState = new int [Constants.N];
        for(int i = 0; i < Constants.N; i++){
            student[i] = null;
            studentState[i] = -1;
        }
        waiter = null;
        waiterState = -1;
        chef = null;
        chefState = -1;
        studentsArrival = new int [Constants.N];
        clientsGoodbye = new boolean[Constants.N];
        nEntities=0;
    }

    /**
     *    Operation look around
     *
     *    Called by the waiter to look around
     *    waits until has requests
     *    @return the request read from the queue
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized Request lookAround() throws RemoteException
    {
        waiter = Thread.currentThread();
        waiterState = WaiterStates.APPST;
        reposInterface.setWaiterState(waiterState);
        
        //System.out.println("waiter looking");
        while(numberOfPendingServiceRequests == 0) {
            try {
                wait();
            } catch (Exception e) {
                //System.out.println("Thread interrupted");
            }
        }
        Request request = null;
        try {
            request = pendingServiceRequests.read();
        } catch (MemException e) {
            e.printStackTrace();
        }
        //System.out.print(request.getRequestID());
        //System.out.println(" - " + request.getRequestType());
        numberOfPendingServiceRequests--;
        return request;
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
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public int[] enter(int studentID) throws RemoteException
    {
        synchronized(this)
        {
            student[studentID] = Thread.currentThread();
            studentState[studentID] = StudentStates.TKSTT;
            reposInterface.setSeatOrder(studentID);
            reposInterface.setStudentState(studentID, studentState[studentID]);
            //System.out.printf("student %d enters\n", studentID);
            try {
                arrivalQueue.write(studentID);
            } catch (MemException e1) {
                e1.printStackTrace();
            }
            studentsArrival[numberOfStudentsInRestaurant] = studentID;
            //System.out.printf("studentsArrival[%d] = %d\n", numberOfStudentsInRestaurant, studentsArrival[numberOfStudentsInRestaurant]);
            numberOfStudentsInRestaurant++;
            Request r = new Request(studentID, 'c');
            numberOfPendingServiceRequests++;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
        }
        tableInterface.takeASeat(studentID, studentState[studentID]);
        return studentsArrival;
    }

    /**
     *    Operation return to bar
     *
     *    Called by the waiter to return to the bar
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized void returnToBar() throws RemoteException
    {
        //System.out.println("waiter is returning to bar");
        waiter = Thread.currentThread();
        waiterState = WaiterStates.APPST;
        reposInterface.setWaiterState(waiterState);
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
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void callWaiter(int studentID, int studentState) throws RemoteException
    {
        //System.out.println("waiter was called");
        synchronized(this) 
        {
            Request r = new Request(studentID, 'o');
            numberOfPendingServiceRequests += 1;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
        }
        tableInterface.waitForPad(studentID, studentState);
    }

    /**
     *    Operation alert the waiter
     *
     *    Called by the chef for waiter collect the portions
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    while chef is waiting in the kitchen for the collection
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void alertTheWaiter() throws RemoteException
    {
        synchronized(this) 
        {
            //chef's ID is equal to the number of students
            chef = Thread.currentThread();
            chefState = ChefStates.DLVPT;
            reposInterface.setChefState(chefState);
            Request r = new Request(Constants.N, 'p');
            numberOfPendingServiceRequests += 1;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
            //System.out.println("chef alerts the waiter");
        }
        kitchenInterface.chefWaitForCollection(chefState);
    }

    /**
     *    Operation collect portion
     *
     *    Called by the waiter to collect portion
     *    and informing chef in the kitchen that portion was collected
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void collectPortion() throws RemoteException
    {
        //System.out.println("waiter is collecting portion");
        synchronized(this)
        {
            waiter = Thread.currentThread();
            waiterState = WaiterStates.WTFPT;
            reposInterface.setWaiterState(waiterState);
        }
        kitchenInterface.portionHasBeenCollected(waiterState);
    }
    

    /**
     *    Operation signal the waiter
     *
     *    Called by the last student to signal the waiter after everybody eaten and is time to pay
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     *    
     */
    public void signalTheWaiter(int studentID) throws RemoteException
    {
        synchronized(this) 
        {
            //System.out.println("waiter has been signaled");
            //bill presentation
            Request r = new Request(studentID, 'b');
            numberOfPendingServiceRequests += 1;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
        }
    }

    /**
     *    Operation prepare the bill
     *
     *    Called by the waiter to prepare the bill
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public synchronized void prepareTheBill() throws RemoteException
    {
        waiter = Thread.currentThread();
        waiterState = WaiterStates.PRCBL;
        reposInterface.setWaiterState(waiterState);
        //System.out.println("waiter preparing the bill");
    }

    /**
     *    Operation exit
     *
     *    Called by the student to exit the restaurant
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    waits for waiter to say goodbye 
     *    @param studentID student id
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void exit(int studentID) throws RemoteException
    {
        synchronized(this)
        {
            //say goodbye
            Request r = new Request(studentID, 'g');
            numberOfPendingServiceRequests += 1;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
            student[studentID] = Thread.currentThread();
            studentState[studentID] = StudentStates.GGHOM;
            reposInterface.setStudentStateAndLeave(studentID, studentState[studentID]);
            while(!clientsGoodbye[studentID])
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.printf("student %d going home\n",studentID);
        }        
    }

    /**
     *    Operation say goodbye
     *
     *    Called by the waiter to say goodbye to the student
     *    signals student that waiter said goodbye
     * 
     *    @param studentID student id
     *    @return number of students in restuarant
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     *    
     */
    public synchronized int sayGoodbye(int studentID) throws RemoteException
    {
        //System.out.printf("saying goodbye to student %d\n", studentID);
        clientsGoodbye[studentID] = true;
        notifyAll();
        numberOfStudentsInRestaurant--;
        return numberOfStudentsInRestaurant;
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void shutdown () throws RemoteException
    {
        nEntities += 1;
        if (nEntities >= Constants.EB)
            ServerBar.shutdown();
        notifyAll ();                                        // the bar may now terminate
    }

}
