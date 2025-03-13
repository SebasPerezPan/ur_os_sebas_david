/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class SJF_P extends Scheduler {

    // DAVID NECESITAMOS IMPLEMENTAR EN AMBOS UN VERIFICADOR DE QUE LA CPU ESTÁ LLENA XD
    SJF_P(OS os) {
        super(os);
    }

    @Override
    public void newProcess(boolean cpuEmpty) {// When a NEW process enters the queue, process in CPU, if any, is extracted to compete with the rest
        if (!cpuEmpty && os.getProcessInCPU() != null) {
            System.out.println("********** NEWPROCESS_SEVAD  ***********");
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
        } // Así es que se interrumpe. Cuando haya un nuevo proceso implemente paramos, extraemos el proceso de la cpu.

    } // Luego forzamos la evaluación.

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {// When a process return from IO and enters the queue, process in CPU, if any, is extracted to compete with the rest                
        if (!cpuEmpty && os.getProcessInCPU() != null) {
            System.out.println("********** IORETURNING_SEVAD  ***********");
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null); // Así es que se interrumpe. Cuando haya un nuevo proceso implemente paramos, extraemos el proceso de la cpu.
        }
    } // Luego forzamos la evaluación.

    @Override
    public void getNext(boolean cpuEmpty) {
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
            }
            addContextSwitch();
        }
    }
}
