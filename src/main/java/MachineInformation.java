import org.jutils.jprocesses.JProcesses;

import java.lang.management.ManagementFactory;
import java.util.stream.Collectors;

public class MachineInformation {
    public static void main(String[] args) {
//        System.out.println(JProcesses.getProcessList().stream().map(p -> p.getCommand()).collect(Collectors.joining("\n")));
        int totalCPU = 0;
        for (int i = 1; i <= 100; i++) {
            totalCPU += JProcesses.getProcessList().stream().filter(p -> p.getCommand().contains("headless_lib")).map(p -> p.getCpuUsage()).collect(Collectors.summingInt(j -> Integer.parseInt(j)));
            System.out.println(totalCPU/i);
        }
//                .forEach(process -> System.out.println(processDetails(process)));
        /* Total number of processors or cores available to the JVM */
//        System.out.println("Available processors (cores): " +
//                Runtime.getRuntime().availableProcessors());
//        Runtime.getRuntime().
        /* Total amount of free memory available to the JVM */
//        System.out.println("Free memory (bytes): " +
//                Runtime.getRuntime().freeMemory());
//
//        /* This will return Long.MAX_VALUE if there is no preset limit */
//        long maxMemory = Runtime.getRuntime().maxMemory();
//        /* Maximum amount of memory the JVM will attempt to use */
//        System.out.println("Maximum memory (bytes): " +
//                (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
//
//        /* Total memory currently available to the JVM */
//        System.out.println("Total memory available to JVM (bytes): " +
//                Runtime.getRuntime().totalMemory());

        /* Get a list of all filesystem roots on this system */
//        File[] roots = File.listRoots();
//
//        /* For each filesystem root, print some info */
//        for (File root : roots) {
//            System.out.println("File system root: " + root.getAbsolutePath());
//            System.out.println("Total space (bytes): " + root.getTotalSpace());
//            System.out.println("Free space (bytes): " + root.getFreeSpace());
//            System.out.println("Usable space (bytes): " + root.getUsableSpace());
//        }
    }

}
