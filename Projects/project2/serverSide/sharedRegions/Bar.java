package serverSide.sharedRegions;
import commInfra.*;
import serverSide.entities.BarClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import clientSide.stubs.GeneralReposStub;
import clientSide.stubs.KitchenStub;
import clientSide.stubs.TableStub;

/**
 *    Bar
 *
 *    It is responsible for the the synchronization of the Students and Waiter
 *    is implemented as an implicit monitor.
 *    
 *    There are internal synchronization points: 
 *    blocking point for the Waiter, where he waits for the Student to signal
 *    blocking point for the Waiter, where he waits for the Chef to signal
 */
public class Bar {

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *   Counter of the number of pending service requests.
     */
    private int numberOfPendingServiceRequests = 0;

    private BarClientProxy waiterProxy;
    private BarClientProxy[] studentProxy;
    private BarClientProxy chefProxy;

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
    private TableStub tableStub;

    /**
     *   Reference to the kitchen.
     */
    private KitchenStub kitchenStub;

    /**
     *   Reference to the general repository.
     */
    private final GeneralReposStub reposStub;

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
    public Bar(GeneralReposStub reposStub, TableStub tableStub, KitchenStub kitchenStub)
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
        this.reposStub = reposStub;
        this.tableStub = tableStub;
        this.kitchenStub = kitchenStub;
        studentProxy = new BarClientProxy[Constants.N];
        for(int i = 0; i < Constants.N; i++){
            studentProxy[i] = null;
        }
        waiterProxy = null;
        chefProxy = null;
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
     * 
     */
    public synchronized Request lookAround() 
    {
        waiterProxy = (BarClientProxy) Thread.currentThread();
        if(waiterProxy.getWaiterState() != WaiterStates.APPST) {
            waiterProxy.setWaiterState(WaiterStates.APPST);
            int state = waiterProxy.getWaiterState();
            reposStub.setWaiterState(state);
        }
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
     *    @return array of the students IDs in order of arrival
     * 
     */
    public int[] enter() 
    {
        int studentID;
        int studentState;
        synchronized(this)
        {
            studentID = (((BarClientProxy) Thread.currentThread()).getStudentID());
            studentProxy[studentID] = (BarClientProxy) Thread.currentThread();
            studentProxy[studentID].setStudentState(StudentStates.TKSTT);
            reposStub.setSeatOrder(studentID);
            studentState = (((BarClientProxy) Thread.currentThread()).getStudentState());
            reposStub.setStudentState(studentID, studentState);
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
        tableStub.takeASeat(studentID, studentState);
        return studentsArrival;
    }

    /**
     *    Operation return to bar
     *
     *    Called by the waiter to return to the bar
     * 
     */
    public synchronized void returnToBar() 
    {
        //System.out.println("waiter is returning to bar");
        waiterProxy = (BarClientProxy) Thread.currentThread();
        waiterProxy.setWaiterState(WaiterStates.APPST);
        int state = ((BarClientProxy) Thread.currentThread()).getWaiterState();
        reposStub.setWaiterState(state);
    }

    /**
     *    Operation call waiter
     *
     *    Called by the student to describe the order
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    while students are waiting in the table for waiter to get the pad
     * 
     */
    public void callWaiter() 
    {
        //System.out.println("waiter was called");
        int studentID;
        int studentState;
        synchronized(this) 
        {
            studentID = ((BarClientProxy)Thread.currentThread()).getStudentID();
            studentState = ((BarClientProxy)Thread.currentThread()).getStudentState();
            Request r = new Request(studentID, 'o');
            numberOfPendingServiceRequests += 1;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
        }
        tableStub.waitForPad(studentID, studentState);
    }

    /**
     *    Operation alert the waiter
     *
     *    Called by the chef for waiter collect the portions
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    while chef is waiting in the kitchen for the collection
     * 
     */
    public void alertTheWaiter() 
    {
        int chefState;
        synchronized(this) 
        {
            //chef's ID is equal to the number of students
            chefProxy = (BarClientProxy) Thread.currentThread();
            chefProxy.setChefState(ChefStates.DLVPT);
            chefState = ((BarClientProxy) Thread.currentThread()).getChefState();
            reposStub.setChefState(chefState);
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
        kitchenStub.chefWaitForCollection(chefState);
    }

    /**
     *    Operation collect portion
     *
     *    Called by the waiter to collect portion
     *    and informing chef in the kitchen that portion was collected
     * 
     */
    public void collectPortion() 
    {
        //System.out.println("waiter is collecting portion");
        int state;
        synchronized(this)
        {
            waiterProxy = (BarClientProxy) Thread.currentThread();
            waiterProxy.setWaiterState(WaiterStates.WTFPT);
            state = ((BarClientProxy) Thread.currentThread()).getWaiterState();
            reposStub.setWaiterState(state);
        }
        kitchenStub.portionHasBeenCollected(state);
    }

    /**
     *    Operation signal the waiter
     *
     *    Called by the last student to signal the waiter after everybody eaten and is time to pay
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    
     */
    public void signalTheWaiter() 
    {
        synchronized(this) 
        {
            //System.out.println("waiter has been signaled");
            int studentID = ((BarClientProxy)Thread.currentThread()).getStudentID();
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
     * 
     */
    public synchronized void prepareTheBill() 
    {
        waiterProxy = (BarClientProxy) Thread.currentThread();
        waiterProxy.setWaiterState(WaiterStates.PRCBL);
        int state = ((BarClientProxy) Thread.currentThread()).getWaiterState();
        reposStub.setWaiterState(state);
        //System.out.println("waiter preparing the bill");
    }

    /**
     *    Operation exit
     *
     *    Called by the student to exit the restaurant
     *    puts a request for the waiter
     *    signals waiter waiting in lookaround
     *    waits for waiter to say goodbye 
     * 
     */
    public void exit() 
    {
        int studentID;
        synchronized(this)
        {
            studentID = ((BarClientProxy) Thread.currentThread()).getStudentID();
            //say goodbye
            Request r = new Request(studentID, 'g');
            numberOfPendingServiceRequests += 1;
            try {
                pendingServiceRequests.write(r);
            } catch (MemException e) {
                e.printStackTrace();
            }
            notifyAll();
            studentProxy[studentID] = (BarClientProxy) Thread.currentThread();
            studentProxy[studentID].setStudentState(StudentStates.GGHOM);
            int state = ((BarClientProxy) Thread.currentThread()).getStudentState();
            reposStub.setStudentStateAndLeave(studentID, state);
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
     *    
     */
    public synchronized int sayGoodbye(int studentID) 
    {
        //System.out.printf("saying goodbye to student %d\n", studentID);
        clientsGoodbye[studentID] = true;
        notifyAll();
        numberOfStudentsInRestaurant--;
        return numberOfStudentsInRestaurant;
    }

    /**
     *  Operation end of work.
     *
     *   New operation.
     *
    */

    public synchronized void endOperation ()
    {
        while (nEntities == 0)
        { /* the waiter waits for the termination of the students and chef */
            try
            { wait ();
            }
            catch (InterruptedException e) {}
        }
            waiterProxy.interrupt ();
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */

    public synchronized void shutdown ()
    {
        nEntities += 1;
        if (nEntities >= Constants.EB)
            ServerBar.waitConnection = false;
        notifyAll ();                                        // the bar may now terminate
    }
}
