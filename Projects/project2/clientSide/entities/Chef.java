package clientSide.entities;
import clientSide.stubs.*;


/**
 *    Chef thread.
 *
 *      It simulates the chef life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public class Chef extends Thread 
{
    /**
     *  Chef state.
     */
    private int chefState;

    /**
     *  Reference to the kitchen.
     */
    private KitchenStub kitchenStub;

    /**
     *  Reference to the bar.
     */
    private BarStub barStub;

     /**
     *   Instantiation of a Chef thread.
     *
     *     @param chefState state of the chef
     *     @param kitchenStub reference to the kitchenStub
     *     @param barStub reference to the barStub
     */
    public Chef(int chefState, KitchenStub kitchenStub, BarStub barStub)
    {
        this.chefState = chefState;
        this.kitchenStub = kitchenStub;
        this.barStub = barStub;
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
        kitchenStub.watchTheNews();
        //System.out.println("start preparation");
        kitchenStub.startPreparation();
        do 
        {
            if(!kitchenStub.getFirstCourse()) kitchenStub.continuePreparation(); else kitchenStub.setFirstCourse(false);
            //System.out.println("proceed to presentation");
            kitchenStub.proceedToPresentation();
            //System.out.println("alert waiter");
            barStub.alertTheWaiter();

            while(!kitchenStub.haveAllPortionsBeenDelivered()) {
                //System.out.println("have next portion ready");
                kitchenStub.haveNextPortionReady();
                //System.out.println("alert waiter");
                barStub.alertTheWaiter();
            }

        } while(!kitchenStub.hasTheOrderBeenCompleted());
        //System.out.println("clean up");
        kitchenStub.cleanUp();
    }
}
