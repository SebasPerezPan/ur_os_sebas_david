/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 *
 * @author prestamour
 */
public class MFQ extends Scheduler{

    int currentScheduler;
    
    private ArrayList<Scheduler> schedulers;
    //This may be a suggestion... you may use the current sschedulers to create the Multilevel Feedback Queue, or you may go with a more tradicional way
    //based on implementing all the queues in this class... it is your choice. Change all you need in this class.
    
    MFQ(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList();
    }
    
    MFQ(OS os, Scheduler... s){ //Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(s.length > 0)
            currentScheduler = 0;
    }
        
    @Override
    public void addProcess(Process p){

        if (p.getState() == ProcessState.NEW || p.getState() == ProcessState.IO) {
            p.setCurrentScheduler(0);
        } else if (p.getState() == ProcessState.CPU) {
            int newLevel = Math.min(p.getCurrentScheduler() + 1, schedulers.size() - 1);
            p.setCurrentScheduler(newLevel);
        }
        schedulers.get(p.getCurrentScheduler()).addProcess(p);
        // schedulers.get(currentScheduler).addProcess(p);
    }
    
    void defineCurrentScheduler(){

         OptionalInt index = IntStream.range(0, schedulers.size())
            .filter(i -> !schedulers.get(i).isEmpty())
            .findFirst();
        currentScheduler = index.orElse(-1);

        // int i = 0;
        // while (i < schedulers.size() && schedulers.get(i).isEmpty()) {
        //     i++;
        // }
        // currentScheduler = (i < schedulers.size()) ? i : -1;

        //This methos is siggested to help you find the scheduler that should be the next in line to provide processes... perhaps the one with process in the queue?
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //Suggestion: now that you know on which scheduler a process is, you need to keep advancing that scheduler. If it a preemptive one, you need to notice the changes
        //that it may have caused and verify if the change is coherent with the priority policy for the queues.
        if (cpuEmpty) {
            defineCurrentScheduler();
        }
        if (currentScheduler != -1) {
            addContextSwitch();
            schedulers.get(currentScheduler).getNext(cpuEmpty);
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
