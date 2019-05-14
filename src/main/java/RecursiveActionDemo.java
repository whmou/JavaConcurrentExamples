import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RecursiveActionDemo {
    public static void main(String[] args) {
        List<BigInteger> list = new ArrayList<>();
        int SIZE = 1_000_000_000;
        int[] arr = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
//            list.add(new BigInteger(Integer.toString(i)));
        }
//        ForkJoinPool.commonPool().invoke(new ModifyList(0, list.size()-1, list));
        ModifyArray modifyArrayTask = new ModifyArray(0, SIZE, arr);
//        modifyListTask.invoke();
        ForkJoinPool pool = new ForkJoinPool(100);
        pool.invoke(modifyArrayTask);
        boolean failFlag = false;
        for (int i : arr) {
            if (i != 0) {
                System.out.println("got non 0");
                failFlag = true;
                break;
            }
        }
        if (!failFlag) {
            System.out.println("all changed to 0");
        }
    }


}


class ModifyArray extends RecursiveAction {
    private static int SEQUENTIAL_THRESHOLD = 5;
    private List<BigInteger> lst;
    int l;
    int r;
    int[] arr;

    ModifyArray(int l, int r, int[] arr) {
        this.l = l;
        this.r = r;
        this.arr = arr;
    }

    @Override
    protected void compute() {
        if (this.r - this.l < SEQUENTIAL_THRESHOLD) {
            for (int i = l; i < r; i++) {
//                System.out.println(String.format("performing modification for idx: %d - %d", l, r));
//                this.lst.set(i, new BigInteger("0"));
                arr[i] = 0;
            }
        } else {
            ModifyArray leftTask = new ModifyArray(l, (l + r) / 2, arr);
            ModifyArray rightTask = new ModifyArray((l + r) / 2, r, arr);
//            leftTask.fork();
//            rightTask.fork();
            invokeAll(leftTask, rightTask);
        }
    }

}