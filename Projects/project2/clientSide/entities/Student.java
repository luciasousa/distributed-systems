package clientSide.entities;
import clientSide.main.Constants;
import clientSide.stubs.*;

/**
 *    Student thread.
 *
 *      It simulates the student life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public class Student extends Thread 
{
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
    private final TableStub tableStub;

    /**
     *  Reference to the bar.
     */
    private final BarStub barStub;

    /**
     *   Instantiation of a student thread.
     *
     *     @param studentID student id
     *     @param studentState student state
     *     @param tableStub reference to tableStub
     *     @param barStub reference to the barStub
     */
    public Student(int studentID,int studentState, TableStub tableStub, BarStub barStub)
    {
        this.studentID = studentID;
        this.studentState = studentState;
        this.tableStub = tableStub;
        this.barStub = barStub;
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
        int[] orderOfArrival = barStub.enter();
        //System.out.println("student read menu");
        tableStub.readMenu();
        if (orderOfArrival[0] != studentID){
            tableStub.informCompanion();
            //System.out.printf("student %d inform companion left the talk\n",studentID);
        } 
        else
        {
            //System.out.printf("student %d prepare order\n", studentID);
            tableStub.prepareTheOrder();
            while(!tableStub.hasEverybodyChosen()) {
                //System.out.printf("student %d add up ones choice\n", studentID);
                tableStub.addUpOnesChoice();
            }
            //System.out.println("student is going to call the waiter");
            barStub.callWaiter();
            //System.out.printf("student %d describe order\n", studentID);
            tableStub.describeTheOrder();
            //System.out.printf("student %d join talk\n", studentID);
            tableStub.joinTheTalk();
        }
        
        for(int i=0; i< Constants.M; i++)
        {
            //System.out.printf("student %d start eating\n", studentID);
            tableStub.startEating();
            //System.out.printf("student %d end eating\n", studentID);
            tableStub.endEating();
            //wait for everyone to finish
            if(!tableStub.hasEverybodyFinished()){
                //System.out.printf("student %d wait for everyone to finish\n", studentID);
                tableStub.waitForEverybodyToFinish();
            }
            //System.out.printf("student %d wait for course\n", studentID);
            tableStub.waitForCourseToBeReady();
        }

        if(orderOfArrival[Constants.N-1] != studentID){
            //System.out.printf("student %d wait for payment\n", studentID);
            tableStub.waitForPayment();
        }
            
        
        if(orderOfArrival[Constants.N-1] == studentID) 
        {
            //System.out.printf("last student %d signal waiter\n", studentID);
            barStub.signalTheWaiter();
            //System.out.printf("last student %d should have arrived earlier\n", studentID);
            tableStub.shouldHaveArrivedEarlier();
            //System.out.printf("last student %d honour the bill\n", studentID);
            tableStub.honourTheBill();
        }
        //System.out.printf("student %d exit\n", studentID);
        barStub.exit();
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
}
