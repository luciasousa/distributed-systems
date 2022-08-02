package clientSide.entities;

/**
 *    Student cloning.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public interface StudentCloning
{
    /**
     *   Set student id.
     *
     *     @param id student id
     */
    public void setStudentID(int id);

    /**
     *   Get student id.
     *  
     *   @return stduent id
     */
    public int getStudentID();

    /**
     *   Set student state.
     *
     *     @param state student state
     */
    public void setStudentState(int state);

    /**
     *   Get student state.
     *
     *   @return student state
     */
    public int getStudentState();
}
