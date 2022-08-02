package serverSide.sharedRegions;
import serverSide.entities.BarClientProxy;
import serverSide.entities.TableClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import clientSide.stubs.GeneralReposStub;

/**
 *    Table
 *
 *    It is responsible for the the synchronization of the Students and Waiter
 *    is implemented as an implicit monitor.
 *    
 *    There is one internal synchronization points: 
 *    a single blocking point for the Student, where he waits for the Waiter to signal
 */

public class Table 
{

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
   *  Reference to student threads.
   */
    private TableClientProxy[] studentProxy;

    /**
   *  Reference to waiter thread.
   */
    private TableClientProxy waiterProxy;

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
    private final GeneralReposStub reposStub;

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
     *    @param reposStub reference to the General Information Repository
     */
    public Table(GeneralReposStub reposStub)
    {
        this.reposStub = reposStub;
        studentProxy = new TableClientProxy[Constants.N];
        clientsSaluted = new boolean[Constants.N];
        courseReady = new boolean[Constants.M];
        menuRead = new boolean[Constants.N];
        nEntities=0;
        for(int i = 0; i< Constants.N;i++){
            studentProxy[i] = null;
        }
        
        waiterProxy = null;
    }

    /**
     *    Operation take a seat
     *
     *    Called by the student to take a seat at the table
     */
    public synchronized void takeASeat() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
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
     */
    public synchronized void saluteTheClient(int studentID) 
    {
        waiterProxy = (TableClientProxy) Thread.currentThread();
        //if(waiterProxy.getWaiterState() != WaiterStates.PRSMN) {
        waiterProxy.setWaiterState(WaiterStates.PRSMN);
        int state = waiterProxy.getWaiterState();
        reposStub.setWaiterState(state);
        //}
        
        //System.out.printf("waiter salute the client %d, state: %d\n", studentID, waiterProxy.getWaiterState());
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
     * 
     */
    public synchronized void readMenu() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        studentProxy[studentID].setStudentState(StudentStates.SELCS);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        
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
     * 
     */
    public synchronized void prepareTheOrder() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        //if(studentProxy[studentID].getStudentState() != StudentStates.OGODR) {
        studentProxy[studentID].setStudentState(StudentStates.OGODR);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        //}
        
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
     * 
     */
    public synchronized void informCompanion() 
    {
        wasInformed = false;
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        studentProxy[studentID].setStudentState(StudentStates.CHTWC);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        
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
     * 
     */
    public synchronized void addUpOnesChoice() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
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
     * 
     */
    public synchronized void waitForPad() 
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
     * 
     */
    public synchronized void getThePad() 
    { 
        waiterProxy = (TableClientProxy) Thread.currentThread();
        waiterProxy.setWaiterState(WaiterStates.TKODR);
        int state = waiterProxy.getWaiterState();
        reposStub.setWaiterState(state);
        
        //System.out.printf("waiter get the pad, state: %d\n", waiterProxy.getWaiterState());
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
     * 
     */
    public synchronized void describeTheOrder() 
    {
        //System.out.println("order is described");
        orderDescribed = true;
        notifyAll();
    }

    /**
     *    Operation has everybody chosen
     *
     *    Called by the first student to arrive to check if every companion has chosen
     *    @return boolean
     * 
     */
    public synchronized boolean hasEverybodyChosen() 
    {
        //System.out.printf("pedidos dos estudantes = %d\n", numberOfStudentsRequests);
        if (numberOfStudentsRequests == Constants.N) return true; else return false;
    }

    /**
     *    Operation join the talk
     *
     *    Called by the first student to arrive to join the talk
     *    waits until course is ready
     * 
     */
    public synchronized void joinTheTalk() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        //if(studentProxy[studentID].getStudentState() != StudentStates.CHTWC) {
        studentProxy[studentID].setStudentState(StudentStates.CHTWC);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        //}
        
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
     * 
     */
    public synchronized void deliverPortion() 
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
     *    @return boolean
     */
    public boolean haveAllClientsBeenServed() 
    {
        if(numberOfPortionsDelivered == Constants.N) return true; else return false;
    }

    /**
     *    Operation start eating
     *
     *    Called by the student to start eating
     *    waits a random time
     * 
     */
    public synchronized void startEating() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        //if(studentProxy[studentID].getStudentState() != StudentStates.EJYML) {
        studentProxy[studentID].setStudentState(StudentStates.EJYML);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        //}
        
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
     * 
     */
    public synchronized void endEating() 
    {
        numberOfPortionsEaten += 1;
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        //if(studentProxy[studentID].getStudentState() != StudentStates.CHTWC) {
        studentProxy[studentID].setStudentState(StudentStates.CHTWC);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        //}
        
        //System.out.printf("student %d has finished eating\n", ((TableClientProxy) Thread.currentThread()).getStudentID());
    }

    /**
     *    Operation has everybody finished
     *
     *    Called by the student to check if everybody has finished
     *    signals waiter that everybody finished eating
     *    waits for next course to be ready
     * 
     *    @return boolean
     */
    public synchronized boolean hasEverybodyFinished() 
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
     */
    public synchronized void waitForEverybodyToFinish(){
        
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
     */
    public synchronized void waitForCourseToBeReady(){
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
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
     * 
     */
    public synchronized void waitForPayment()
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
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
     * 
     */
    public synchronized void presentTheBill() 
    {
        waiterProxy = (TableClientProxy) Thread.currentThread();
        //if(waiterProxy.getWaiterState() != WaiterStates.RECPM) {
        waiterProxy.setWaiterState(WaiterStates.RECPM);
        int state = waiterProxy.getWaiterState();
        reposStub.setWaiterState(state);
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
     * 
     */
    public synchronized void shouldHaveArrivedEarlier() 
    {
        int studentID = ((TableClientProxy) Thread.currentThread()).getStudentID();
        studentProxy[studentID] = (TableClientProxy) Thread.currentThread();
        //if(studentProxy[studentID].getStudentState() != StudentStates.PYTBL) {
        studentProxy[studentID].setStudentState(StudentStates.PYTBL);
        int state = ((TableClientProxy) Thread.currentThread()).getStudentState();
        reposStub.setStudentState(studentID, state);
        //}
        
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
     * 
     */
    public synchronized void honourTheBill() 
    {
        //System.out.println("bill has been honoured");
        studentHasPaid = true;
        notifyAll();
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
      { 
        try
        { wait ();
        }
        catch (InterruptedException e) {}
      }
      for(int i = 0; i < Constants.N; i++){
          studentProxy[i].interrupt ();
      }
   }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */

    public synchronized void shutdown ()
    {
        nEntities += 1;
        if (nEntities >= Constants.ET)
            ServerTable.waitConnection = false;
        notifyAll ();                                        // the table may now terminate
    }
}