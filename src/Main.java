import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.SynchronousQueue;
import java.util.Map.Entry;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        if(args.length != 2){
            System.out.println("Nieprawidlowa liczba argumentow");
            return;
        }
        int arg0;
        try{
            arg0 = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException err){
            System.out.println("Nieprawidłowy format parametru");
            return;
        }
        if(arg0 < 1){
            System.out.println("Nie moze byc mniej niz 1 watek");
            return;
        }
        if(!args[1].contains(".txt")){
            System.out.println("Nieprawidlowa nazwa pliku");
            return;
        }
        Queue<String> stringSynchronousQueue = new
                ConcurrentLinkedQueue<>();
        File f = new File(args[1]);
        Scanner tekst;
        try{
            tekst = new Scanner(f);
        }
        catch(FileNotFoundException err){
            System.out.println("Blad otwarcia pliku");
            return;
        }
        String line;
        while(tekst.hasNextLine()){
            line = tekst.nextLine();
            stringSynchronousQueue.add(line);
        }
        tekst.close();
        ConcurrentMap<String, Long> concurrentMap = new
                ConcurrentHashMap();
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < arg0; i++){
            threads.add(new Thread(new
                    WordCounterThread(stringSynchronousQueue,concurrentMap,"Watek"+i)));
        }
        threads.forEach(Thread::start);
        for (Thread thread : threads){
            thread.join();
        }
//System.out.println(concurrentMap);
        List <Entry<String, Long>> list = new
                ArrayList<>(concurrentMap.entrySet());
        Collections.sort(list, (l1, l2) ->
                l1.getKey().compareTo(l2.getKey()));
        for(int i = 0; i < list.size() ; i++){
            System.out.println(list.get(i));
        }
    }
}