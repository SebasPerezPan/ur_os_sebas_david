/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class SJF_NP extends Scheduler{

    
    SJF_NP(OS os){
        super(os);
    }


//    public void getNext(boolean cpuEmpty) {
//        if (!processes.isEmpty() && cpuEmpty) {
//            // Encontrar el proceso con el menor tiempo total de ejecución
//            Process shortestJob = processes.stream().min(Comparator.comparingInt(Process::getRemainingTimeInCurrentBurst)) // Selecciona el proceso más corto
//                .orElse(null);
//
//            if (shortestJob != null) {
//                processes.remove(shortestJob); // Elimina el proceso seleccionado de la lista
//                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestJob); // Asigna el proceso a la CPU
//            }
//        }
//    }

    // Esta es la implementación del pseudo-codigo usando un proceso de iteración entre los remaining, con el metodo que pillamos xD
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty() && cpuEmpty){
            Process min_BTR_process = null;
            int min_BTR = Integer.MAX_VALUE;

            for (Process p: processes){
                if (p.isCurrentBurstCPU()){
                    int temp = p.getRemainingTimeInCurrentBurst();
                    if(temp < min_BTR) {
                        min_BTR = temp;
                        min_BTR_process = p;
                    } else if (temp == min_BTR) {
                        min_BTR_process = tieBreaker(min_BTR_process, p);

                    }
                }
            }
        if (min_BTR_process != null){
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, min_BTR_process);
            processes.remove(min_BTR_process);
            addContextSwitch();
        }}
    }
    // os.ioq.processes Es la lista.
    @Override
    public void newProcess(boolean cpuEmpty) {
    } //Non-preemtive

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive
    
}
