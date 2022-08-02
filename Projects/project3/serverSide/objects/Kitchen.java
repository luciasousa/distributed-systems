package serverSide.objects;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;

/**
 *    Kitchen
 *
 *    It is responsible for the the synchronization of the Chef and Waiter
 *    is implemented as an implicit monitor.
 *    There is one internal synchronization points: 
 *    a single blocking point for the Chef, where he waits for the Waiter to signal
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class Kitchen implements KitchenInterface{
/**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
   *  Reference to chef thread.
   */
    private Thread chef;

    /**
   *  Reference to waiter thread.
   */
    private Thread waiter;

    /**
     *  State of the waiter.
     */
    private int waiterState;

    /**
     *  State of the chef.
     */
    private int chefState;

    /**
     *   Counter of the number of courses to deliver.
     */
    private int numberOfCoursesToDeliver;

    /**
     *   Counter of the number of portions to deliver.
     */
    private int numberOfPortionsToDeliver;

    /**
     *   Counter of the current course number
     */
    private int nCourses;

    /**
     *   Counter of the current portion number
     */
    private int nPortions;

    /**
     *   Reference to the general repository
     */
    private final GeneralReposInterface reposInterface;

    /**
     *   Boolean flag that indicates if is the first course.
     */
    private boolean firstCourse;

    /**
     *   Boolean flag that indicates if note is available.
     */
    private boolean isNoteAvailable = false;

    /**
     *   Boolean flag that indicates if preperation was started.
     */
    private boolean preparationStarted;

    /**
     *   Boolean flag that indicates if portion was collected.
     */
    private boolean portionCollected;

    /**
     *  Kitchen instantiation.
     *
     *    @param repos reference to the General Information Repository
     */
    public Kitchen(GeneralReposInterface reposInterface)
    {
        firstCourse = true;
        this.reposInterface = reposInterface;
        nEntities=0;
        chef = null;
        chefState = -1;
        waiter = null;
        waiterState = -1;
        nCourses = 0;
        nPortions = 0;
    }

    /**
     *    Operation set first course
     *
     *    Called by the chef to set first course
     *    @param b boolean value setting the first course
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setFirstCourse(boolean b) throws RemoteException { firstCourse = b; }

    /**
     *    Operation get first course
     *
     *    Called by the chef to get first course
     *    @return true if is the first course
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     *    
     */
    public boolean getFirstCourse() throws RemoteException { return firstCourse; }

    /**
     *    Operation watch the news
     *
     *    Called by the chef to watch the news
     *    waits until note is available
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void watchTheNews() throws RemoteException
    {
        chef = Thread.currentThread();
        chefState = ChefStates.WAFOR;
        reposInterface.setChefState(chefState);
        //System.out.println("chef watches the news");
        while(!isNoteAvailable)
        {
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("chef has note");
    }
    
    /**
     *    Operation hand note to the chef
     *
     *    Called by the waiter to hand note to the chef
     *    signals chef that note is available
     *    waiter waits until chef starts preparation
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void handTheNoteToChef() throws RemoteException
    {
        //System.out.println("waiter hands the note to chef");
        waiter = Thread.currentThread();
        waiterState = WaiterStates.PCODR;
        reposInterface.setWaiterState(waiterState);
        
        this.numberOfCoursesToDeliver = Constants.M;
        this.numberOfPortionsToDeliver = Constants.N;
        isNoteAvailable = true;
        notifyAll();
        while(!preparationStarted)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *    Operation chef wait for collection
     *
     *    Called by the waiter for chef to wait for collection of the portion
     *    @param chefState chef state
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void chefWaitForCollection(int chefState) throws RemoteException
    {
        //System.out.println("chef waits for waiter to collect portion");
        while(!portionCollected)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        portionCollected = false;
    }

    /**
     *    Operation portion has been collected
     *
     *    Called by the waiter to inform that portion was collected
     *    signals chef that portion was collected
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void portionHasBeenCollected(int waiterState) throws RemoteException
    {
        portionCollected = true;
        notifyAll();
    }

    /**
     *    Operation start preparation
     *
     *    Called by the chef to start preparation
     *    signals waiter that preperation was started
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */    
    public synchronized void startPreparation() throws RemoteException
    {
        chef = Thread.currentThread();
        chefState = ChefStates.PRPCS;
        nCourses = 1;
        nPortions = 0;
        reposInterface.setStatePortionsCourses(chefState, nPortions, nCourses);
        
        numberOfCoursesToDeliver--;
        //System.out.printf("chef starts preparation\n");
        preparationStarted = true;
        notifyAll();
    }

    /**
     *    Operation proceedToPresentation
     *
     *    Called by the chef to proceed to presentation
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void proceedToPresentation() throws RemoteException
    {
        chef = Thread.currentThread();
        chefState = ChefStates.DSHPT;
        nPortions += 1;
        reposInterface.setStatePortions(chefState, nPortions);
        
        if(numberOfPortionsToDeliver!=0) numberOfPortionsToDeliver--;
        else numberOfPortionsToDeliver=Constants.N-1;
        //System.out.printf("chef proceeds to presentation, course %d, portion %d\n",numberOfCoursesToDeliver,numberOfPortionsToDeliver);
    }

    /**
     *    Operation have all portions been delivered
     *
     *    Called by the chef to check if all portions were delivered
     *    
     *    @return true if the portions have been fully delivered otherwise false
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized boolean haveAllPortionsBeenDelivered() throws RemoteException
    {
        if(numberOfPortionsToDeliver == 0) return true; else return false;
    }

    /**
     *    Operation has the order been completed
     *
     *    Called by the chef to check if the order was completed
     *    
     *    @return true if the order has been fully completed otherwise false
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized boolean hasTheOrderBeenCompleted() throws RemoteException
    {
        if(numberOfCoursesToDeliver == 0) return true; else return false;
    }

    /**
     *    Operation have next portion ready
     *
     *    Called by the chef to have next portion ready
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void haveNextPortionReady() throws RemoteException
    {
        chef = Thread.currentThread();
        chefState = ChefStates.DSHPT;
        nPortions += 1;
        reposInterface.setStatePortions(chefState, nPortions);
        numberOfPortionsToDeliver--;
        //System.out.printf("chef have next portion ready course %d, portion %d\n",numberOfCoursesToDeliver,numberOfPortionsToDeliver);
    }

    /**
     *    Operation continue preparation
     *
     *    Called by the chef to continue preparation
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void continuePreparation() throws RemoteException
    {
        chef = Thread.currentThread();
        chefState = ChefStates.PRPCS;
        nCourses += 1;
        nPortions = 0;
        reposInterface.setStatePortionsCourses(chefState, nPortions, nCourses);
        
        numberOfCoursesToDeliver--;
        numberOfPortionsToDeliver = Constants.N;
        //System.out.printf("chef continues preparation course %d, portion %d\n",numberOfCoursesToDeliver,numberOfPortionsToDeliver);
    }

    /**
     *    Operation clean up
     *
     *    Called by the chef to clean up
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public synchronized void cleanUp() throws RemoteException
    {
        //System.out.println("chef cleans up");
        chef = Thread.currentThread();
        chefState = ChefStates.CLSSV;
        reposInterface.setChefState(chefState);
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
        if (nEntities >= Constants.EK)
            ServerKitchen.shutdown();
        notifyAll ();                                        // the kitchen may now terminate
    }
}