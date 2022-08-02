package clientSide.entities;
import java.rmi.*;
import commInfra.Request;
import interfaces.*;

/**
 *    Waiter thread.
 *
 *      It simulates the waiter life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
public class Waiter extends Thread{
    /**
     *  Waiter State.
     */
    private int waiterState;

    /**
     *  Reference to the bar.
     */
    private BarInterface barInterface;

    /**
     *  Reference to the kitchen.
     */
    private KitchenInterface kitchenInterface;

    /**
     *  Reference to the table.
     */
    private TableInterface tableInterface;

    /**
     *   Instantiation of a Waiter thread.
     *
     *     @param waiterState waiter state
     *     @param barInterface reference to the bar Interface
     *     @param kitchenInterface reference to the kitchen Interface
     *     @param tableInterface reference to the table Interface
     */
    public Waiter(int waiterState, BarInterface barInterface, KitchenInterface kitchenInterface, TableInterface tableInterface)
    {
        this.waiterState = waiterState;
        this.barInterface = barInterface;
        this.kitchenInterface = kitchenInterface;
        this.tableInterface = tableInterface;
    }

    /**
     *   Set waiter state.
     *
     *     @param state waiter state
     */
    public void setWaiterState(int state)
    {
        this.waiterState = state;
    }

    /**
     *   Get waiter state.
     *
     *     @return waiter state
     */
    public int getWaiterState()
    {
        return this.waiterState;
    }

    /**
     *   Life cycle of the waiter.
     *   
     *   Starts at the state appraising situation (waiting for the arrival of the students)
     *   Ends when all the N students exit the restuarant
     */
    public void run() 
    {
        while(true)
        {
            Request r = lookAround();
            switch(r.getRequestType()) 
            {
                case 'c': //client arriving
                    //System.out.println("client arriving - 'c'");
                    //System.out.printf("salute client %d\n",r.getRequestID());
                    saluteTheClient(r.getRequestID());
                    //System.out.println("return to bar");
                    returnToBar();
                    break;
                
                case 'o': //order ready to be collected
                    //System.out.println("order ready to be collected - 'o'");
                    //System.out.println("get the pad");
                    getThePad();
                    //System.out.println("hand note to chef");
                    handTheNoteToChef();
                    //System.out.println("return to bar");
                    returnToBar();
                    break;
                
                case 'p': //portion ready to be collected
                    //System.out.println("portion ready to be collected - 'p'");
                    if(!haveAllClientsBeenServed())
                    {
                        //System.out.println("collect portion");
                        collectPortion();
                        //System.out.println("deliver portion");
                        deliverPortion();
                        //System.out.println("return to bar");
                        returnToBar();
                    }
                    break;

                case 'b': //bill presentation
                    //System.out.println("bill presentation - 'b'");
                    //System.out.println("prepare bill");
                    prepareTheBill();
                    //System.out.println("present bill");
                    presentTheBill();
                    //System.out.println("return to bar");
                    returnToBar();
                    break;
                    
                case 'g': //say goodbye to students
                    //System.out.println("say goodbye to students - 'g'");
                    int numberOfStudentsInRestaurant = sayGoodbye(r.getRequestID());
                    if(numberOfStudentsInRestaurant == 0) return;
            }
        }
    }
    /**
     *    Operation look around
     *
     *    Called by the waiter to look around
     *    waits until has requests
     *    @return the request read from the queue
     * 
     */
    public Request lookAround(){
        Request r = null;
        try {
            r = barInterface.lookAround();
            //System.out.printf("Request id: %d\n Request type: %c\n", r.getRequestID(), r.getRequestType());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println("look around was null");
        return r;
    }

    /**
     *    Operation salute the client
     *
     *    Called by the waiter to salute the client
     * 
     *    @param studentID the ID of the student to be saluted by the waiter
     */
    public void saluteTheClient(int studentID){
        try {
            tableInterface.saluteTheClient(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation return to bar
     *
     *    Called by the waiter to return to the bar
     * 
     */
    public void returnToBar(){
        try {
            barInterface.returnToBar();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation get the pad
     *
     *    Called by the waiter to get the pad
     *    signal student that he got the pad and waits until student describes the order
     * 
     */
    public void getThePad(){
        try {
            tableInterface.getThePad();
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
    public void handTheNoteToChef(){
        try {
            kitchenInterface.handTheNoteToChef();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation have all clients been served
     *
     *    Called by the waiter to check if all clients have been served
     *    @return true if all clients have been served false otherwise
     */
    public boolean haveAllClientsBeenServed(){
        try {
            return tableInterface.haveAllClientsBeenServed();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *    Operation collect portion
     *
     *    Called by the waiter to collect portion
     *    and informing chef in the kitchen that portion was collected
     * 
     */
    public void collectPortion(){
        try {
            barInterface.collectPortion();
        } catch (RemoteException e) {
            e.printStackTrace();
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
    public void deliverPortion(){
        try {
            tableInterface.deliverPortion();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Operation prepare the bill
     *
     *    Called by the waiter to prepare the bill
     * 
     */
    public void prepareTheBill(){
        try {
            barInterface.prepareTheBill();
        } catch (RemoteException e) {
            e.printStackTrace();
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
    public void presentTheBill(){
        try {
            tableInterface.presentTheBill();
        } catch (RemoteException e) {
            e.printStackTrace();
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
    public int sayGoodbye(int studentID){
        try {
            return barInterface.sayGoodbye(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
