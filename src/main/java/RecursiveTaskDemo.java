import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class RecursiveTaskDemo {

    public static void main(String[] args) {

        final int SIZE = 10_000_000;
        int[] array = randomArray(SIZE);
        EvenNumberCounterRecursiveTask mainTask = new EvenNumberCounterRecursiveTask(0, SIZE - 1, array);
        ForkJoinPool pool = new ForkJoinPool(50);
        Integer evenNumberCount = pool.invoke(mainTask);

        System.out.println("Number of even numbers: " + evenNumberCount);
        int tmp = 0;
        for (int i = 0; i < SIZE - 1; i++) {
            if (array[i] % 2 == 0) tmp++;
        }
        System.out.println("Non Multi-thread version Number of even numbers: " + tmp);
    }

    static int[] randomArray(int SIZE) {
        int[] array = new int[SIZE];
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            array[i] = random.nextInt(50);
        }

        return array;
    }
}

class EvenNumberCounterRecursiveTask extends RecursiveTask<Integer> {

    int l;
    int r;
    int[] arr;

    EvenNumberCounterRecursiveTask(int l, int r, int[] arr) {
        this.l = l;
        this.r = r;
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (this.r - this.l < 1_000) {
            int tmp = 0;
            for (int i = l; i < r; i++) {
                if ((this.arr[i] % 2) == 0) {
                    tmp++;
                }
            }
            return tmp;
        } else {
            EvenNumberCounterRecursiveTask leftTask = new EvenNumberCounterRecursiveTask(l, (l + r) / 2, arr);
            EvenNumberCounterRecursiveTask rightTask = new EvenNumberCounterRecursiveTask((l + r) / 2, r, arr);
            invokeAll(leftTask, rightTask);
            return leftTask.join() + rightTask.join();
        }
    }
}