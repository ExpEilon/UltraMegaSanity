import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerOfGui {

    private static ManagerOfGui ourInstance = new ManagerOfGui();

    public static ManagerOfGui getInstance() {
        return ourInstance;
    }

    private ArrayList<MyThread> threads = new ArrayList<>();

    private TheFatherPanel theFatherPanel;

    private EndIsComingPanel endIsComingPanel;

    private ManagerOfGui() {}

    public void setTheFatherPanel(TheFatherPanel theFatherPanel){
        this.theFatherPanel = theFatherPanel;
    }

    public TheFatherPanel getTheFatherPanel(){return theFatherPanel;}

    public void setEndIsComingPanel(EndIsComingPanel endIsComingPanel){
        this.endIsComingPanel = endIsComingPanel;
    }

    public EndIsComingPanel getEndIsComingPanel(){return endIsComingPanel;}


    public void addThread(MyThread t){
        threads.add(t);
    }

    public void terminateAll(){
        threads.stream().forEach(t -> t.interrupt());
    }
    public boolean isTerminated(){
        return threads.stream().allMatch(t -> t.isInterrupted());
    }
    public List<MyThread> getThreads(){
        return threads;
    }

    public static void letsStart(boolean restart){
        if(restart) {
            WriteSummary.restartRoot();
            WriteSummary.createRoot();
        }
        ConfigManager.getDevices().forEach(d -> {
            d.restart();
            MyThread thread = new MyThread(d);
            thread.setName(d.getSN());
            ManagerOfGui.getInstance().addThread(thread);
            thread.start();
        });
        ManagerOfGui.getInstance().setEndIsComingPanel(new EndIsComingPanel());
        ManagerOfGui.getInstance().getTheFatherPanel().theEndIsComing();
    }
}
