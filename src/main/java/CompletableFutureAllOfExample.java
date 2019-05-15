import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CompletableFutureAllOfExample {
    public static void main(String[] args) {

        List<String> readyToGoContent = Arrays.asList("1", "2", "3", "4");
        List<CompletableFuture<String>> modifiedContentFutureList = readyToGoContent.stream()
                .map(webPageLink -> genContent(webPageLink))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                modifiedContentFutureList.toArray(new CompletableFuture[modifiedContentFutureList.size()])
        );

        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(x -> {
            return modifiedContentFutureList.stream()
                    .map(modifiedContent -> modifiedContent.join())
                    .collect(Collectors.toList());
        });

        CompletableFuture<Long> cnt = allPageContentsFuture.thenApply(x->{
            return x.stream().filter(y-> y.contains("3")).count();
        });

        CompletableFuture<Long> cnt2 = allPageContentsFuture.thenApply(x->{
            return x.stream().filter(y-> y.contains("Modified")).count();
        });

        try {
            System.out.println("Number of Post Processed Str having keyword '3' - " +
                    cnt.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Number of Post Processed Str having keyword 'Modified' - " +
                    cnt2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public static CompletableFuture<String> genContent(String test) {
        return CompletableFuture.supplyAsync(() -> {
            return "Modified_" + test;
        });
    }
}
