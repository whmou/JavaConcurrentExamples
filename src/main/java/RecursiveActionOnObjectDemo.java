import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RecursiveActionOnObjectDemo {
    public static void main(String[] args) {
        List<BigInteger> list = new ArrayList<>();
        int SIZE = 1_000_000;
        for (int i = 0; i < SIZE; i++) {
            list.add(new BigInteger(Integer.toString(i)));
        }
//        ForkJoinPool.commonPool().invoke(new ModifyList(0, list.size()-1, list));
        ModifyList modifyListTask = new ModifyList(0, SIZE, list);
//        modifyListTask.invoke();
        ForkJoinPool pool = new ForkJoinPool(100);
        pool.invoke(modifyListTask);
        boolean failFlag = false;
        for (BigInteger i : list) {
            if (i.intValue() != 0) {
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


class ModifyList extends RecursiveAction {
    private static int SEQUENTIAL_THRESHOLD = 5;
    private List<BigInteger> lst;
    int l;
    int r;

    ModifyList(int l, int r, List lst) {
        this.l = l;
        this.r = r;
        this.lst = lst;
    }

    @Override
    protected void compute() {
        if (this.r - this.l < SEQUENTIAL_THRESHOLD) {
            for (int i = l; i < r; i++) {
//                System.out.println(String.format("performing modification for idx: %d - %d", l, r));
                this.lst.set(i, new BigInteger("0"));
            }
        } else {
            ModifyList leftTask = new ModifyList(l, (l + r) / 2, lst);
            ModifyList rightTask = new ModifyList((l + r) / 2, r, lst);
//            leftTask.fork();
//            rightTask.fork();
            invokeAll(leftTask, rightTask);
        }
    }

}