package lab_2;

import java.util.*;

class SharedResults{
    private Map<Long,Boolean> map = new HashMap<>();
    public synchronized void addResult(long Task,boolean result){
        map.put(Task,result);
    }

    public synchronized Map<Long,Boolean> getResults(){
        return new HashMap<>(map);
    }
}
class SharedResourses{
    private Queue<Long> Tasks = new LinkedList<>();
    public synchronized void addTask(long number){
        Tasks.add(number);
        notify();
    }
    public synchronized Long getTask() throws InterruptedException{
        while (Tasks.isEmpty()){
            wait();
        }return Tasks.poll();
    }
}
class PrimeChech implements Runnable{
    private SharedResourses sharedResourses;
    private SharedResults sharedResults;
    public PrimeChech(SharedResourses sharedResourses,SharedResults sharedResults){
        this.sharedResourses = sharedResourses;
        this.sharedResults = sharedResults;
    }
    private boolean isPrime(Long task){
        if(task <= 1)
            return false;
        for(int i=2;i*i<=task;i++){
            if(task%i == 0){
                return false;
            }
        }return true;
    }
    @Override
    public void run(){
        boolean log = false;
        try {
            while(true){
                Long task = sharedResourses.getTask();
                boolean result = isPrime(task);
                if(log)
                    System.out.println(Thread.currentThread().getName() + " processing task: " + task + " result: " + result);
                sharedResults.addResult(task,result);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


public class Main {
    public static void main(String[] args) {
        int numberOfThreads = 5;
        Scanner scanner = new Scanner(System.in);
        SharedResourses sharedResource = new SharedResourses();
        SharedResults sharedResults = new SharedResults();

        // Tworzenie wątków
        for (int i = 0; i < numberOfThreads; i++) {
            PrimeChech workerThread = new PrimeChech(sharedResource,sharedResults);
            Thread thread = new Thread(workerThread);
            thread.start();
        }

        // Dodawanie zadań do współdzielonego zasobu
        for (long i = 1; i <= 100; i++) {
            sharedResource.addTask(i);
        }

        // Obsluga uzytkownika
        while(true){
            System.out.println("Enter number (or exit to close):");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                long numberToCheck = Long.parseLong(input);
                sharedResource.addTask(numberToCheck);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        Thread.currentThread().interrupt();
        System.out.println("Closing application...");
        Map<Long,Boolean> map = sharedResults.getResults();
        for (Map.Entry<Long, Boolean> entry : map.entrySet()) {
            System.out.println("Num: " + entry.getKey() + ", isPrime: " + entry.getValue());
        }
    }
}