import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
public class WordCounterThread implements Runnable {
    private final Queue<String> stringSynchronousQueue;
    private final ConcurrentMap<String, Long> concurrentMap;
    private final String name;
    public WordCounterThread(Queue<String> stringSynchronousQueue,
                             ConcurrentMap concurrentMap, String name) {
        this.stringSynchronousQueue = stringSynchronousQueue;
        this.concurrentMap = concurrentMap;
        this.name = name;
    }
    @Override
    public void run() {
        String line = stringSynchronousQueue.poll();
        int lineNumbers = 0;
        while (line != null) {
            System.out.println(line);
            line = line.replaceAll("[^a-zA-Z ]", "");
            Map<String, Long> map = Arrays.stream(line.split(" ")).filter(str -> str.length() >
                    1).map(String::toLowerCase).collect(Collectors.groupingBy(Function.identity
                    (), Collectors.counting()));
            map.entrySet().forEach(
                    entry -> {
                        if
                        (concurrentMap.containsKey(entry.getKey())) {
                            concurrentMap.put(entry.getKey(), concurrentMap.get(entry.getKey()) +
                                    entry.getValue());
                        } else {
                            concurrentMap.put(entry.getKey(),entry.getValue());
                        }
                    }
            );
            lineNumbers++;
            line = stringSynchronousQueue.poll();
        }
        System.out.println(this.name + ": " + lineNumbers);
    }
}