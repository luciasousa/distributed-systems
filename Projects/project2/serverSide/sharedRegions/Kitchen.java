package serverSide.sharedRegions;
import commInfra.*;
import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.GeneralReposStub;

/**
 *    Kitchen
 *
 *    It is responsible for the the synchronization of the Chef and Waiter
 *    is implemented as an implicit monitor.
 *    
 *    There is one internal synchronization points: 
 *    a single blocking point for the Chef, where he waits for the Waiter to signal
 */

public class Kitchen 
{
    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
   *  Reference to chef thread.
   */
    private KitchenClientProxy chefProxy;

    /**
   *  Reference to waiter thread.
   */
    private KitchenClientProxy waiterProxy;

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
    private final GeneralReposStub reposStub;

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
     *    @param reposStub reference to the General Information Repository
     */
    public Kitchen(GeneralReposStub reposStub)
    {
        firstCourse = true;
        this.reposStub = reposStub;
        nEntities=0;
        chefProxy = null;
        waiterProxy = null;
        nCourses = 0;
        nPortions = 0;
    }

    /**
     *    Operation set first course
     *
     *    Called by the chef to set first course
     *    @param b boolean value setting the first course
     *    
     */
    public void setFirstCourse(boolean b) { firstCourse = b; }

    /**
     *    Operation get first course
     *
     *    Called by the chef to get first course
     *    @return boolean value for the first course
     *    
     */
    public boolean getFirstCourse() { return firstCourse; }

    /**
     *    Operation watch the news
     *
     *    Called by the chef to watch the news
     *    waits until note is available
     *    
     */
    public synchronized void watchTheNews() 
    {
        chefProxy = (KitchenClientProxy) Thread.currentThread();
        chefProxy.setChefState(ChefStates.WAFOR);
        int state = chefProxy.getChefState();
        reposStub.setChefState(state);
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
     *    
     */
    public synchronized void handTheNoteToChef() 
    {
        //System.out.println("waiter hands the note to chef");
        waiterProxy = (KitchenClientProxy) Thread.currentThread();
        waiterProxy.setWaiterState(WaiterStates.PCODR);
        int state = waiterProxy.getWaiterState();
        reposStub.setWaiterState(state);
        
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
     *    
     */
    public synchronized void chefWaitForCollection() 
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
     *    
     */
    public synchronized void portionHasBeenCollected()
    {
        portionCollected = true;
        notifyAll();
    }

    /**
     *    Operation start preparation
     *
     *    Called by the chef to start preparation
     *    signals waiter that preperation was started
     *    
     */    
    public synchronized void startPreparation() 
    {
        chefProxy = (KitchenClientProxy) Thread.currentThread();
        chefProxy.setChefState(ChefStates.PRPCS);
        int state = chefProxy.getChefState();
        nCourses = 1;
        nPortions = 0;
        reposStub.setStatePortionsCourses(state, nPortions, nCourses);
        
        numberOfCoursesToDeliver--;
        //System.out.printf("chef starts preparation\n");
        preparationStarted = true;
        notifyAll();
    }

    /**
     *    Operation proceedToPresentation
     *
     *    Called by the chef to proceed to presentation
     *    
     */
    public synchronized void proceedToPresentation() 
    {
        chefProxy = (KitchenClientProxy) Thread.currentThread();
        chefProxy.setChefState(ChefStates.DSHPT);
        int state = chefProxy.getChefState();
        nPortions += 1;
        reposStub.setStatePortions(state, nPortions);
        
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
     */
    public synchronized boolean haveAllPortionsBeenDelivered() 
    {
        if(numberOfPortionsToDeliver == 0) return true; else return false;
    }

    /**
     *    Operation has the order been completed
     *
     *    Called by the chef to check if the order was completed
     *    
     *    @return true if the order has been fully completed otherwise false
     */
    public synchronized boolean hasTheOrderBeenCompleted() 
    {
        if(numberOfCoursesToDeliver == 0) return true; else return false;
    }

    /**
     *    Operation have next portion ready
     *
     *    Called by the chef to have next portion ready
     *    
     */
    public synchronized void haveNextPortionReady() 
    {
        chefProxy = (KitchenClientProxy) Thread.currentThread();
        chefProxy.setChefState(ChefStates.DSHPT);
        int state = chefProxy.getChefState();
        nPortions += 1;
        reposStub.setStatePortions(state, nPortions);
        numberOfPortionsToDeliver--;
        //System.out.printf("chef have next portion ready course %d, portion %d\n",numberOfCoursesToDeliver,numberOfPortionsToDeliver);
    }

    /**
     *    Operation continue preparation
     *
     *    Called by the chef to continue preparation
     *    
     */
    public synchronized void continuePreparation() 
    {
        chefProxy = (KitchenClientProxy) Thread.currentThread();
        chefProxy.setChefState(ChefStates.PRPCS);
        int state = chefProxy.getChefState();
        nCourses += 1;
        nPortions = 0;
        reposStub.setStatePortionsCourses(state, nPortions, nCourses);
        
        numberOfCoursesToDeliver--;
        numberOfPortionsToDeliver = Constants.N;
        //System.out.printf("chef continues preparation course %d, portion %d\n",numberOfCoursesToDeliver,numberOfPortionsToDeliver);
    }

    /**
     *    Operation clean up
     *
     *    Called by the chef to clean up
     *    
     */
    public synchronized void cleanUp() 
    {
        //System.out.println("chef cleans up");
        chefProxy = (KitchenClientProxy) Thread.currentThread();
        chefProxy.setChefState(ChefStates.CLSSV);
        int state = chefProxy.getChefState();
        reposStub.setChefState(state);
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
        chefProxy.interrupt ();
   }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */

    public synchronized void shutdown ()
    {
        nEntities += 1;
        if (nEntities >= Constants.EK)
            ServerKitchen.waitConnection = false;
        notifyAll ();                                        // the kitchen may now terminate
    }
}