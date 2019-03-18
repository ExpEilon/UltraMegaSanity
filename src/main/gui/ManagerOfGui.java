import java.lang.reflect.Array;
import java.util.ArrayList;

public class ManagerOfGui {

    private static ManagerOfGui ourInstance = new ManagerOfGui();

    public static ManagerOfGui getInstance() {
        return ourInstance;
    }

    private ArrayList<Thread> threads = new ArrayList<>();

    private TheFatherPanel theFatherPanel;

    private ManagerOfGui() {}

    public void setTheFatherPanel(TheFatherPanel theFatherPanel){
        this.theFatherPanel = theFatherPanel;
    }

    public TheFatherPanel getTheFatherPanel(){return theFatherPanel;}

    public void addThread(Thread t){
        threads.add(t);
    }

    public void terminateAll(){
        threads.stream().forEach(t -> ((MyThread)t).terminate());
    }
}
