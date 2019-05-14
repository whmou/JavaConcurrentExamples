import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RaceConditionInRecursiveActionDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(10);
//        int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int limit = 3000;
        int [] data = new int[limit];     // 10000 sorted integers
        for (int v = 0; v < limit; v++) { // loop from 0 to 999
            data[v] = v + 1; // Set ten elements per value of the outer loop
        }

        int target = 414565908;
        for(int i=0; i< 50; i++) {
            Square app = new Square(data, 0, data.length);
            pool.invoke(app);
            System.out.println(app.result);
            if (app.result != target){
                System.out.println(String.format("race condition happened, %d != %d", app.result, target));
                break;
            }
            Square.result =0;
        }
    }
}


class Square extends RecursiveAction {
    final int LIMIT = 3;
    //keep static
    static int result;
    int start, end;
    int[] data;


    Square(int[] data, int start, int end) {
        this.start = start;
        this.end = end;
        this.data = data;
    }

    @Override
    protected void compute() {
        if ((end - start) < LIMIT) {
            for (int i = start; i < end; i++) {
                result += data[i] * data[i];
                System.out.println("Thread # " + Thread.currentThread().getId() + " is doing this task");
            }
        } else {
            int mid = (start + end) / 2;
            Square left = new Square(data, start, mid);
            Square right = new Square(data, mid, end);
            left.fork();
            right.fork();
            left.join();
            right.join();
        }
    }
}