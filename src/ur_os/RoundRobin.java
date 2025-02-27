/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler{

    int q;
    int cont;
    boolean singlequeue;
    
    RoundRobin(OS os){
        super(os);
        q = 2;
        cont=0;
        singlequeue = true;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean singlequeue){
        this(os);
        this.q = q;
        this.singlequeue = singlequeue;
    }
    

    
    void resetCounter(){
        cont=0;
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if(!processes.isEmpty() && cpuEmpty)
        {   
            resetCounter();
            Process p = processes.get(0);
            processes.remove();
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            addContextSwitch();
        }
    }
    public void getNext_other(boolean cpuEmpty) {
        if(cont==q){
            resetCounter();
            if (!cpuEmpty && os.getProcessInCPU() != null) {
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
                getNext(true);
            }
            } else{
                cont = cont+1;
            }
        if (!processes.isEmpty() && cpuEmpty) {
            Process min_BTR_process = null;
            int min_BTR = Integer.MAX_VALUE;
            for (Process p : processes) {
                if (p.isCurrentBurstCPU()) {
                    int temp = p.getRemainingTimeInCurrentBurst();
                    if (temp < min_BTR) {
                        min_BTR = temp;
                        min_BTR_process = p;
                    } else if (temp == min_BTR) {
                        min_BTR_process = tieBreaker(min_BTR_process, p);
                    }
                }
            }
            if (min_BTR_process != null) {
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, min_BTR_process);
                processes.remove(min_BTR_process);
                addContextSwitch();
            }
        }

        
    }
    
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
