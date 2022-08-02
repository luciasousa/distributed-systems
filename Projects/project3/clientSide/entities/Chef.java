package clientSide.entities;
import java.rmi.*;
import interfaces.*;

/**
 *    Chef thread.
 *
 *      It simulates the chef life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
public class Chef extends Thread{
    /**
     *  Chef state.
     */
    private int chefState;

    /**
     *  Reference to the kitchen.
     */
    private KitchenInterface kitchenInterface;

    /**
     *  Reference to the bar.
     */
    private BarInterface barInterface;

     /**
     *   Instantiation of a Chef thread.
     *
     *     @param chefState state of the chef
     *     @param kitchenInterface reference to the kitchen Interface
     *     @param barInterface reference to the bar Interface
     */
    public Chef(int chefState, KitchenInterface kitchenInterface, BarInterface barInterface)
    {
        this.chefState = chefState;
        this.kitchenInterface = kitchenInterface;
        this.barInterface = barInterface;
    }

    /**
     *   Set chef state.
     *
     *     @param state chef state
     */
    public void setChefState(int state)
    {
        chefState = state;
    }

    /**
     *   Get chef state.
     *
     *   @return chef state
     */
    public int getChefState()
    {
        return chefState;
    }

    /**
     *   Life cycle of the chef.
     *   
     *   Starts at the state whatch the news (waiting for the note)
     *   Ends when the order is complete.
     */
    public void run() 
    {
        //System.out.println("watch news");
        watchTheNews();
        //System.out.println("start preparation");
        startPreparation();
        do 
        {
            if(!getFirstCourse()) continuePreparation(); else setFirstCourse(false);
            //System.out.println("proceed to presentation");
            proceedToPresentation();
            //System.out.println("alert waiter");
            alertTheWaiter();

            while(!haveAllPortionsBeenDelivered()) {
                //System.out.println("have next portion ready");
                haveNextPortionReady();
                //System.out.println("alert waiter");
                alertTheWaiter();
            }

        } while(!hasTheOrderBeenCompleted());
        //System.out.println("clean up");
        cleanUp();
    }

    /**
     *    Operation set first course
     *
     *    Called by the chef to set first course
     *    @param b boolean value setting the first course
     *    
     */
    public void setFirstCourse(boolean b) { 
        try {
            kitchenInterface.setFirstCourse(b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation get first course
     *
     *    Called by the chef to get first course
     *    @return true if it is the first course false otherwise
     *    
     */
    public boolean getFirstCourse(){ 
        try {
            return kitchenInterface.getFirstCourse();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
     }

    /**
     *    Operation watch the news
     *
     *    Called by the chef to watch the news
     *    waits until note is available
     *    
     */
    public synchronized void watchTheNews()
    {
        try {
            kitchenInterface.watchTheNews();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        try {
            kitchenInterface.handTheNoteToChef();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation chef wait for collection
     *
     *    Called by the waiter for chef to wait for collection of the portion
     *    @param chefState chef state
     */
    public synchronized void chefWaitForCollection(int chefState)
    {
        try {
            kitchenInterface.chefWaitForCollection(chefState);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation portion has been collected
     *
     *    Called by the waiter to inform that portion was collected
     *    signals chef that portion was collected
     *     @param waiterState waiter state
     *    
     */
    public synchronized void portionHasBeenCollected(int waiterState)
    {
        try {
            kitchenInterface.portionHasBeenCollected(waiterState);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        try {
            barInterface.alertTheWaiter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        try {
            kitchenInterface.startPreparation();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation proceedToPresentation
     *
     *    Called by the chef to proceed to presentation
     *    
     */
    public synchronized void proceedToPresentation()
    {
        try {
            kitchenInterface.proceedToPresentation();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        try {
            return kitchenInterface.haveAllPortionsBeenDelivered();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
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
        try {
            return kitchenInterface.hasTheOrderBeenCompleted();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *    Operation have next portion ready
     *
     *    Called by the chef to have next portion ready
     *    
     */
    public synchronized void haveNextPortionReady()
    {
        try {
            kitchenInterface.haveNextPortionReady();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation continue preparation
     *
     *    Called by the chef to continue preparation
     *    
     */
    public synchronized void continuePreparation()
    {
        try {
            kitchenInterface.continuePreparation();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation clean up
     *
     *    Called by the chef to clean up
     *    
     */
    public synchronized void cleanUp()
    {
        try {
            kitchenInterface.cleanUp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
