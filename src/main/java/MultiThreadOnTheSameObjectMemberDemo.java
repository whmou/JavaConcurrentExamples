import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadOnTheSameObjectMemberDemo {
    public static void main(String[] args) {
        int TARGET = 1_000;
        SampleObject so = new SampleObject();
        ModifyMember modifyMemberTask = new ModifyMember(so);
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < TARGET; i++) {
            es.execute(modifyMemberTask);
        }
        es.shutdown();
        try {
            es.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if (so.getResult() != TARGET) {
            System.out.println(String.format("race condition, %d != %d", so.getResult(), TARGET));
        } else {
            System.out.println("NO race condition");
        }
    }
}


class ModifyMember implements Runnable {
    SampleObject so;

    ModifyMember(SampleObject so) {
        this.so = so;
    }

    @Override
    public void run() {
//        System.out.println("Thread # " + Thread.currentThread().getId() + " is doing this task");
//        synchronized(so) {
        this.so.resultAddOne();
//        }

    }
}

class SampleObject {
    volatile int result = 0;

    public int getResult() {
        return this.result;
    }

    public void resultAddOne() {
        this.result += 1;

    }
}