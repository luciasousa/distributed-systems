package commInfra;

/**
 *   Type of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType
{
  /**
   *  Initialization of the logging file name and the number of iterations (service request).
   */
    public static final int SETNFIC = 1;

  /**
   *  Logging file was initialized (reply).
   */
    public static final int NFICDONE = 2;

  /**
   *  Chef wait for an order (request).
   */
  public static final int WAFOR = 3;

  /**
   *  Order done (reply).
   */
  public static final int ORDERDONE = 4;

  /**
   *  Chef prepares course (request).
   */
  public static final int PRPCS = 5;

  /**
   *  Course prepared (reply).
   */
  public static final int CSPREP = 6;

  /**
   *  Get first course (request).
   */
  public static final int GETFIRSTCOURSE = 7;

  /**
   *  First course (reply).
   */
  public static final int FIRSTCOURSE = 8;

  /**
   *  Proceed to presentation (request).
   */
  public static final int PRCPRES = 9;

  /**
   *  Proceed to presentation (reply).
   */
  public static final int PRESDONE = 10;

  /**
   *  Have all portions been delivered (request).
   */
  public static final int PORDELIV = 11;

  /**
   *  Have all portions been delivered (reply).
   */
  public static final int PORDELIVDONE = 12;

  /**
   *  Have next portion ready (request).
   */
  public static final int POREADY = 13;

  /**
   *  Have next portion ready (reply).
   */
  public static final int POREADYDONE = 14;

  /**
   *  Set first course (request).
   */
  public static final int SETFIRSTCS = 15;

  /**
   *  Set first course (reply).
   */
  public static final int SETFIRSTCSDONE = 16;

  /**
   *  Has the order been completed (request).
   */
  public static final int ORDERCOMPREQ = 17;

  /**
   *  Order been complete (reply).
   */
  public static final int ORDERCOMP = 18;

  /**
   *  Clean up (request).
   */
  public static final int CLEANREQ = 19;

  /**
   *  Clean up (reply).
   */
  public static final int CLEAN = 20;

  /**
   *  Hand note to the chef (request).
   */
  public static final int NOTEREQ = 21;

  /**
   *  Hand note to the chef (reply).
   */
  public static final int NOTE = 22;

  /**
   *  Read menu (request).
   */
  public static final int READMENUREQ = 23;

  /**
   *  Read menu (reply).
   */
  public static final int READMENU = 24;

  /**
   *  Inform companion (request).
   */
  public static final int INFCOMPREQ = 25;

  /**
   *  Inform companion (reply).
   */
  public static final int INFCOMP = 26;

  /**
   *   Prepare the order (request).
   */
  public static final int PREPORDERREQ = 27;

  /**
   *   Prepare the order (reply).
   */
  public static final int PREPORDER = 28;

  /**
   *   Has everybody chosen (request).
   */
  public static final int EVBDCHOSENREQ = 29;

  /**
   *   Has everybody chosen (reply).
   */
  public static final int EVBDCHOSEN = 30;

  /**
   *   Add up ones choice (request).
   */
  public static final int ADDUPCHCREQ = 31;

  /**
   *   Add up ones choice (reply).
   */
  public static final int ADDUPCHC = 32;

  /**
   *   Describe the order (request).
   */
  public static final int DESCORDERREQ = 33;

  /**
   *   Describe the order (reply).
   */
  public static final int DESCORDER = 34;

  /**
   *   Join the talk (request).
   */
  public static final int JOINTALKREQ = 35;

  /**
   *   Join the talk (reply).
   */
  public static final int JOINTALK = 36;

  /**
   *   Start eating (request).
   */
  public static final int STARTEATREQ = 37;

  /**
   *   Start eating (reply).
   */
  public static final int STARTEAT = 38;

  /**
   *   End eating (request).
   */
  public static final int ENDEATREQ = 39;

  /**
   *   End eating (reply).
   */
  public static final int ENDEAT = 40;

  /**
   *   Wait for everybody to finish (request).
   */
  public static final int EVBDFINISHREQ = 41;

  /**
   *   Wait for everybody to finish (reply).
   */
  public static final int EVBDFINISH = 42;

  /**
   *   Wait for course to be ready (request).
   */
  public static final int CSREADYREQ = 43;

  /**
   *   Wait for course to be ready (reply).
   */
  public static final int CSREADY = 44;

  /**
   *   Wait for payment (request).
   */
  public static final int WTPAYREQ = 45;

  /**
   *   Wait for payment (reply).
   */
  public static final int WTPAY = 46;

  /**
   *   Should have arrived earlier (request).
   */
  public static final int SHARRIVEDEARLIERREQ = 47;

  /**
   *   Should have arrived earlier (reply).
   */
  public static final int SHARRIVEDEARLIER = 48;

  /**
   *   Honour the bill (request).
   */
  public static final int HONOURBILLREQ = 49;

  /**
   *   Honour the bill (reply).
   */
  public static final int HONOURBILL = 50;

  /**
   *   Salute the client (request).
   */
  public static final int SALUTECLIENTREQ = 51;

  /**
   *   Salute the client (reply).
   */
  public static final int SALUTECLIENT = 52;

  /**
   *   Get the pad (request).
   */
  public static final int GETPADREQ = 53;

  /**
   *   Get the pad (reply).
   */
  public static final int GETPAD = 54;

  /**
   *   Have all clients been served (request).
   */
  public static final int HVCLIENTSBEENSRVREQ = 55;

  /**
   *   Have all clients been served (reply).
   */
  public static final int HVCLIENTSBEENSRV = 56;

  /**
   *   Deliver portion (request).
   */
  public static final int DELVPTREQ = 57;

  /**
   *   Deliver portion (reply).
   */
  public static final int DELVPT = 58;

  /**
   *   Present the bill (request).
   */
  public static final int PRESBILLREQ = 59;

  /**
   *   Present the bill (reply).
   */
  public static final int PRESBILL = 60;

  /**
   *   Alert the waiter (request).
   */
  public static final int ALERTWAITER = 61;

  /**
   *   Alert the waiter (reply).
   */
  public static final int WAITERALERTED = 62;

  /**
   *   Student enters the restaurant (request).
   */
  public static final int ENTERSTUDENT = 63;

  /**
   *   Student enters the restaurant (reply).
   */
  public static final int STUDENTENTERED = 64;

  /**
   *   Call the waiter (request).
   */
  public static final int CALLWAITER = 65;

  /**
   *   Call the waiter (reply).
   */
  public static final int WAITERCALLED = 66;

  /**
   *   Signal the waiter (request).
   */
  public static final int SIGNALWAITER = 67;

  /**
   *   Signal the waiter (reply).
   */
  public static final int WAITERSIGNALED = 68;

  /**
   *   Student exits the restaurant (request).
   */
  public static final int EXITSTUDENT = 69;

  /**
   *   Student exits the restaurant (reply).
   */
  public static final int STUDENTEXITED = 70;

  /**
   *   Waiter looks around (request).
   */
  public static final int LOOKWAITER = 71;

  /**
   *   Waiter looks around (reply).
   */
  public static final int WAITERLOOKED = 72;

  /**
   *   Waiter returns to bar (request).
   */
  public static final int RETURNWAITER = 73;
  
  /**
   *   Waiter returns to bar (reply).
   */
  public static final int WAITERRETURNED = 74;

  /**
   *   Waiter collects the portion (request).
   */
  public static final int COLLECTWAITER = 75;

  /**
   *   Waiter collects the portion (reply).
   */
  public static final int WAITERCOLLECTED = 76;

  /**
   *   Waiter prepares the bill (request).
   */
  public static final int PREPAREWAITER = 77;

  /**
   *   Waiter prepares the bill (reply).
   */
  public static final int WAITERPREPARED = 78;

  /**
   *   Waiter says goodbye to student (request).
   */
  public static final int SAYGOODBYE = 79;

  /**
   *   Waiter says goodbye to student (reply).
   */
  public static final int SAYGOODBYEDONE = 80;


  /**
   *  Server shutdown (service request).
   */
  public static final int SHUT = 81;

  /**
   *  Server was shutdown (reply).
   */
   public static final int SHUTDONE = 82;

  /**
   *  Set chef state (service request).
   */
   public static final int SETCHEFSTATE = 83;

  /**
   *  Set waiter state (service request).
   */
   public static final int SETWAITERSTATE = 84;

  /**
   *  Set student state (service request).
   */
  public static final int SETSTUDENTSTATE = 85;


  /**
   *  Set chef, waiter and student states (service request).
   */
  public static final int SETSTATES = 86;

  /**
   *  Setting acknowledged (reply).
   */

  public static final int SACK = 87;

  /**
   *  Chef wait (request).
   */
  public static final int CHEFWAIT = 88;

  /**
   *  Chef wait (reply).
   */
  public static final int CHEFWAITDONE = 89;

  /**
   *  Portion collect (request).
   */
  public static final int PORTIONCOLLECT = 90;

  /**
   *  Portion collect (reply).
   */
  public static final int PORTIONCOLLECTDONE = 91;

  /**
   *  Take a seat (request).
   */
  public static final int TAKESEAT = 92;

  /**
   *  Take a seat (reply).
   */
  public static final int TAKESEATDONE = 93;

  /**
   *  Wait for pad (request).
   */
  public static final int WAITPAD = 94;

  /**
   *  Wait for pad (reply).
   */
  public static final int WAITPADDONE = 95;

  /**
   *  End operation (request).
   */
  public static final int ENDOP = 96;

  /**
   *  End operation (reply).
   */
  public static final int EOPDONE = 97;

  /**
   *  Wait everybody to finish (request).
   */
  public static final int WAITEVBDFINISHREQ = 98;

  /**
   *  Wait everybody to finish (reply).
   */
  public static final int WAITEVBDFINISH = 99;

  /**
   *  Sets the number of portions (request).
   */
  public static final int SETNUMBERPORTIONS = 100;

   /**
   *  Sets the number of courses (request).
   */
  public static final int SETNUMBERCOURSES = 101;

  /**
   *  Sets the seat number (request).
  */
  public static final int SETSEAT = 102;

  /**
   *  Sets the number of courses and portions(request).
   */
  public static final int SETCOURSESPORTIONS = 103;

  /**
   *  Chef continues preparation (request).
  */
  public static final int CONTINUEPREP = 104;

  /**
   *  Chef continues preparation (reply).
  */
  public static final int CONTINUEPREPDONE = 105;

  /**
   *  Set chef state, course and portion
  */
  public static final int SETSTATECOURSEPORT = 106;

  /**
   *  Set student state and seat when leaving the restaurant
  */
  public static final int SETSTATELEAVE = 107;

  /**
   *  Set chef state and portion
  */
  public static final int SETSTATEPORTION = 108;

}
