package serverSide.objects;
import java.rmi.*;
import java.util.Objects;
import clientSide.entities.*;
import genclass.*;
import interfaces.*;
import serverSide.main.Constants;
import serverSide.main.ServerGeneralRepos;

/**
 *  General Repository of information.
 *
 *    It is responsible to keep the visible internal state of the problem and to provide means for it
 *    to be printed in the logging file.
 *    It is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    There are no internal synchronization points.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class GeneralRepos implements GeneralReposInterface{
    /**
     *  Number of iterations of the student life cycle.
     */
    private int nIter;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *  Name of the logging file.
     */
    private String logFileName;

    /**
     *  State of the chef.
     */
    private int chefState;

    /**
     *  State of the waiter.
     */
    private int waiterState;

    /**
     *  States of the students.
     */
    private int[] studentState = new int[Constants.N];

    /**
     *  Counter with the number of course.
     */
    private int numberOfCourse;

    /**
     *  Counter with the number of portion.
     */
    private int numberOfPortion;    

    /**
     *  Array with the order that students sat down.
     */
    private int[] seatOrder;

    /**
     *  Seat number.
     */
    private int seatNumber = 0;


    /**
     *   Instantiation of a general repository object.
     *
     *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public GeneralRepos() throws RemoteException
    {
        this.logFileName = "logger";
        chefState = ChefStates.WAFOR;
        waiterState = WaiterStates.APPST;
        for(int i = 0; i < Constants.N; i++)
        {
            studentState[i] = StudentStates.GGTRT;
        }

        numberOfCourse = 0;
        numberOfPortion = 0;
        seatOrder = new int[Constants.N];

        reportInitialStatus();
    }

    /**
     *  Write the header and the initial states to the logging file.
     *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     *  
     */
    public void reportInitialStatus() throws RemoteException
    {
        TextFile log = new TextFile();
        if (!log.openForWriting (".", logFileName))
        {
            GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }
        log.writelnString ("\t\t\t\t\t\t\t\t\t\t\t\tThe Restaurant - Description of the internal state\n");
        log.writelnString (" Chef   Waiter  Stu0  Stu1    Stu2   Stu3   Stu4   Stu5  Stu6    NCourse   NPortion                         Table       ");
        log.writelnString ("State   State  State  State  State  State  State  State  State                        Seat0\t Seat1\tSeat2\tSeat3\tSeat4\tSeat5\tSeat6");
        if (!log.close())
        {
            GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
        reportStatus();
    }

    /**
    *   Set chef state.
    *
    *     @param state chef state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public synchronized void setChefState (int state) throws RemoteException
    {
        if(chefState!=state){
            chefState = state;
            reportStatus();
            //System.out.printf("Set Chef: %d; %d\n", numberOfCourse, numberOfPortion);
        }
   }

   /**
    *   Set waiter state.
    *
    *     @param state waiter state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public synchronized void setWaiterState (int state) throws RemoteException
    {
        if(waiterState!=state){
            waiterState = state;
            reportStatus();
            //System.out.printf("Set Waiter: %d; %d\n", numberOfCourse, numberOfPortion);
        }
    }

   /**
    *   Set student state.
    *
    *     @param state student state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public synchronized void setStudentState (int studentID, int state) throws RemoteException
    {
        if(studentState[studentID]!=state)
        {
            studentState[studentID] = state;
            reportStatus();
            //System.out.printf("Set Student: %d; %d\n", numberOfCourse, numberOfPortion);
        }
    }

    /**
    *   Set student, chef and waiter states.
    *
    *     @param cState chef state
    *     @param wState waiter state
    *     @param sID student id
    *     @param sState student state
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */

    public synchronized void setChefWaiterStudentState (int cState, int wState, int sID, int sState) throws RemoteException
    {
        if(studentState[sID]!=sState || waiterState!=wState || chefState!=cState)
        {
            studentState[sID] = sState;
            waiterState = wState;
            chefState = cState;
            //System.out.printf("Set All: %d; %d\n", numberOfCourse, numberOfPortion);
            reportStatus();
        }
    }

   /**
    *   Update Counter numberOfPortion
    *
    *     @param nPortions integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public synchronized void setNumberOfPortions(int nPortions) throws RemoteException
    {
        numberOfPortion = nPortions;
    }

    /**
    *   Update Counter numberOfCourse
    *
    *     @param nCourses integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public synchronized void setNumberOfCourses(int nCourses) throws RemoteException
    {
        numberOfCourse = nCourses;
    }

    /**
    *   Update Counter numberOfPortions and numberOfCourses
    *
    *     @param nPortions integer
    *     @param nCourses integer
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */

    public synchronized void setNumberOfPortionsAndCourses(int nPortions, int nCourses) throws RemoteException
    {
        numberOfPortion = nPortions;
        numberOfCourse = nCourses;
    }

    /**
    *   Update chef state, numberOfPortions and numberOfCourses
    *
    *     @param state chef state
    *     @param nPortions portion number
    *     @param nCourses course number
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */

    public synchronized void setStatePortionsCourses(int state, int nPortions, int nCourses) throws RemoteException
    {
        chefState = state;
        numberOfPortion = nPortions;
        numberOfCourse = nCourses;
        reportStatus();
        //System.out.printf("Set Chef port course: %d; %d\n", numberOfCourse, numberOfPortion);
    }

    /**
    *   Update chef state and numberOfPortions
    *
    *     @param state chef state
    *     @param nPortions portion number
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */

    public synchronized void setStatePortions(int state, int nPortions) throws RemoteException
    {
        chefState = state;
        numberOfPortion = nPortions;
        reportStatus();
        //System.out.printf("Set chef port: %d; %d\n", numberOfCourse, numberOfPortion);
    }

    /**
    *   Update student state and seat
    *
    *     @param state student state
    *     @param id student id
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */

    public synchronized void setStudentStateAndLeave(int id, int state) throws RemoteException
    {
        studentState[id] = state;
        for(int i = 0; i < Constants.N; i++)
        {
            if(id == seatOrder[i]) seatOrder[i] = -1;
        }
        //System.out.printf("Set student leave: %d; %d\n", numberOfCourse, numberOfPortion);
        reportStatus();
    }

    /**
    *   Update Array seatOrder
    *
    *     @param id student id
    *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */

    public synchronized void setSeatOrder(int id) throws RemoteException
    {
        seatOrder[seatNumber] = id;
        seatNumber += 1;
    }   

    /**
     *   Operation initialization of simulation.
     *
     *   New operation.
     *
     *     @param logFileName name of the logging file
     *     @param nIter number of iterations of the student life cycle
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */

    public synchronized void initSimul (String logFileName, int nIter) throws RemoteException
    {
        if (!Objects.equals (logFileName, ""))
            this.logFileName = logFileName;
        this.nIter = nIter;
        reportInitialStatus();
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
        if (nEntities >= Constants.EGR)
            ServerGeneralRepos.shutdown();
    }


    /**
     *  Write a state line at the end of the logging file.
     *
     *  The current state of entities is organized in a line to be printed.
     *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    public void reportStatus() throws RemoteException
    {
        TextFile log = new TextFile ();                      // instantiation of a text file handler

        String lineStatus = "";                              // state line to be printed
        String seats = "";

        if (!log.openForAppending (".", logFileName))
        { 
            GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
            System.exit (1);
        }

        switch (chefState)
        {
            case 0: lineStatus += "WAFOR  ";
                                                break;
            case 1: lineStatus += "PRPCS  ";
                                                break;
            case 2: lineStatus += "DSHPT  ";
                                                break;
            case 3: lineStatus += "DLVPT  ";
                                                break;
            case 4: lineStatus += "CLSSV  ";
                                                break;
        }
   
        switch (waiterState)
        {
            case 0: lineStatus += " APPST ";
                                                break;
            case 1: lineStatus += " PRSMN ";
                                                break;
            case 2: lineStatus += " TKODR ";
                                                break;
            case 3: lineStatus += " PCODR ";
                                                break;      
            case 4: lineStatus += " WTFPT ";
                                                break;
            case 5: lineStatus += " PRCBL ";
                                                break;
            case 6: lineStatus += " RECPM ";
                                                break;                              
        }
   
        for (int i = 0; i < Constants.N; i++)
            switch (studentState[i])
            {
                case 0:  lineStatus += " GGTRT ";
                                                break;
                case 1:  lineStatus += " TKSTT ";
                                                break;
                case 2:  lineStatus += " SELCS ";
                                                break;
                case 3:  lineStatus += " OGODR ";
                                                break;
                case 4:  lineStatus += " CHTWC ";
                                                break;
                case 5:  lineStatus += " EJYML ";
                                                break;
                case 6:  lineStatus += " PYTBL ";
                                                break;
                case 7:  lineStatus += " GGHOM ";
                                                break;
            }
       
        lineStatus += " "+String.format("%7d", numberOfCourse)+"  "+String.format("%7d", numberOfPortion);

        for(int i = 0; i < seatNumber; i++)
        {
            if(seatOrder[i] != -1)
                seats += String.format("\t%4d ", seatOrder[i]);
            else
                seats += String.format("\t");
        }

        lineStatus += seats;
        log.writelnString (lineStatus);
        if (!log.close ())
        { 
            GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
    }

    /**
    *   Write to the logging file if operation of opening for appending the file or operation of closing the file failed.
    *
    *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public void printSumUp() throws RemoteException
    {
        TextFile log = new TextFile ();  

        if (!log.openForAppending (".", logFileName))
        { 
            GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
            System.exit(1);
        }
        
        log.writelnString("\n\n\nLegend: \n" +
        "Chef State - state of the chef: WAFOR PRPCS DSHPT DLVPT CLSSV\n" +
        "Waiter State - state of the waiter: APPST PRSMN TKODR PCODR WTFPT PRCBL RECPM\n" +
        "Stu# State - state of the student #: GGTRT TKSTT SELCS OGODR CHTWC EJYML PYTBL GGHOM\n" +
        "NCourse - number of the course: 0 upto M\n" +
        "NPortion - number of the portion of a course: 0 upto N\n" +
        "Table Seat# - id of the student sat at that chair\n");

        if (!log.close ())
        { 
            GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
    }
}