/*
 * @author Andrei Ciceu
 * CS1027
 * 03/20/2024
 * This code is for an array unique priority queue, that takes in data with a double value
 * for priority and orders the items from least to greatest based of priority
 * 
 */

public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T>{
    private T[] queue; //queue holding data
    private double[] priority; //array holding priorities
    private int count; //# items in queue
    

    public ArrayUniquePriorityQueue(){ // Constructor
        queue = (T[])(new Object[10]);
        priority = new double[10];
        count = 0;
    }

    /*
     * @param data Identifies the item
     * @param prio The priority for the data
     */
    public void add(T data, double prio){
        if (contains(data)){
            return;
        }
        
        if (count == queue.length){
            T[] tempqueue = (T[])new Object[queue.length+5];
            double[] tempprio = new double[priority.length+5];
            for (int i = 0; i<count;i++){
                tempqueue[i] = queue[i];
                tempprio[i] = priority[i];
            }
            queue = tempqueue;
            priority = tempprio;
        }

        if (count == 0){
            queue[0] = data;
            priority[0] = prio;
            count++;
            return;
        }

        int check = -1;

        for (int i = 0; i<=count; i++){
            if (prio<priority[i]){
                check = i;
                break;
            }else if (prio == priority[i]){
                check = i+1;
                break;
            }
        }
    
        if (check<0){
            queue[count] = data;
            priority[count] = prio;
            count++;
        } else {
            int j = 0;
            double[] newprio = new double[priority.length];
            T[] newqueue = (T[])new Object[queue.length];
            for (int i = 0; i<=count; i++){
                if (i == check){
                    newqueue[i] = data;
                    newprio[i] = prio;
                } else {
                    newqueue[i] = queue[j];
                    newprio[i] = priority[j];
                    j++;
                }
            }
            queue = newqueue;
            priority = newprio;
            count++;
        }
    }

    /*
     * @param data the data item used
     * @returns boolean checks if item is contained in queue
     * 
     */
    public boolean contains(T data){
        for (int i = 0; i<count; i++){
            if (queue[i].equals(data)){
                return true;
            }
        }
        return false;
    }

    /*
     * @throws CollectionException if queue is empty
     * @return T returns data of item at the front of queue
     */
    public T peek() throws CollectionException{
        if (count == 0) throw new CollectionException("PQ is empty");
        return queue[0];
    }

    /*
     * @throws CollectionException if queue is empty
     * @returns T returns data of item at front of queue after removing it
     */
    public T removeMin () throws CollectionException{
        if (count == 0) throw new CollectionException("PQ is empty");
        T data = queue[0];
        for (int i = 0; i<queue.length-1; i++){
            if (i == queue.length-1){
                queue[i] = null;
                priority[i] = 0.0;
            }else{
                queue[i] = queue[i+1];
                priority[i] = priority[i+1];
            }
        }
        count--;
        return data;
    }

    /*
     * @param T data item used to find in queue
     * @param double newPrio new priority to update
     * @throws CollectionException if item not in queue
     * Updates the priority of existing item in queue
     */
    public void updatePriority(T data, double newPrio) throws CollectionException{
        if (!contains(data)) throw new CollectionException("Item not found in PQ");

        int pos = 0;
        for (int i = 0; i<count; i++){
            if (queue[i].equals(data)){
                pos = i;
            }
        }

        for (int i = pos; i<count; i++){
            if (i == queue.length-1){
                queue[i] = null;
                priority[i] = 0.0;
            }else{
                queue[i] = queue[i+1];
                priority[i] = priority[i+1];
            }
            
        }
        count--;
        
        add(data, newPrio);
    }

    /*
     * @returns boolean checks if queue is empty
     */
    public boolean isEmpty(){
        if (count == 0){
            return true;
        }
        return false;
    }

    /*
     * @returns int returns size of queue
     */
    public int size (){
        return count;
    }

    /*
     * @returns int returns length of queue
     */
    public int getLength(){
        return queue.length;
    }

    /*
     * @returns String forms string from queue
     */
    public String toString (){
        if (isEmpty()){
            return "The PQ is empty";
        }
        String s = "";
        for (int i = 0; i<count; i++){
            if (i==count-1){
                s+= queue[i] + " [" + priority[i] + "]";
            }else{
                s+= queue[i] + " [" + priority[i] + "], ";  
            }
        }
        return s;
    }

}
