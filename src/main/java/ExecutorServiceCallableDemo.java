import java.util.concurrent.*;

public class ExecutorServiceCallableDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CounterIncCallable callableTask = new CounterIncCallable(100000, "callable first thread");
        CounterIncCallable callableTask2 = new CounterIncCallable(15, "callable second thread");
        Future<Integer> result = executor.submit(callableTask);
        Future<Integer> result2 = executor.submit(callableTask2);
        executor.shutdown();


        System.out.println("Main thread is running");

        try {
            System.out.println(result.get());
            System.out.println(result2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

}

class CounterIncCallable implements Callable<Integer> {
    private int num = 0;
    private String id = "";

    public CounterIncCallable(int num, String id) {
        this.num = num;
        this.id = id;
    }

    public Integer call() throws Exception {
        System.out.println("thread comes in");
        System.out.println("Worker ID: " + this.id);
        long threadId = Thread.currentThread().getId();
        System.out.println("Thread # " + threadId + " is doing this task");
        int result = 0;
        for (int i = 1; i <= num; i++) {
            result += i;
        }
        return result;
    }
}
