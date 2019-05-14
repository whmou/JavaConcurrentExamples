import java.util.concurrent.*;

public class ExecutorServiceRunnableDemo {

    public static void main(String[] args) throws InterruptedException{
        System.out.println("hello world");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CounterIncRunable callableTask = new CounterIncRunable(100000, "runnable first thread");
        CounterIncRunable callableTask2 = new CounterIncRunable(15, "runnable second thread");
        executor.submit(callableTask);
        executor.submit(callableTask2);
        executor.shutdown();

        System.out.println("Main thread is Done");
    }

}

class CounterIncRunable implements Runnable {
    private int num = 0;
    private String id  = "";
    public CounterIncRunable(int num, String id){
        this.num = num;
        this.id = id;
    }

    public void run()  {
        System.out.println("thread comes in");
        System.out.println("Worker ID: " + this.id);
        long threadId = Thread.currentThread().getId();
        System.out.println("Thread # " + threadId + " is doing this task");
        int result = 0;
        for(int i=1;i<=num;i++){
            result+=i;
        }
        System.out.println(this.id + " result: " + String.valueOf(result) );
    }
}
