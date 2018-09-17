import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandsPerformance {
    private Map<String,TimeHolder> sum;

    public CommandsPerformance(){
        sum = new HashMap<>();
    }

    public void addData(String op,long totalTime){
        if(sum.get(op) == null)
            sum.put(op,new TimeHolder(totalTime));
        else sum.get(op).inc(totalTime);
    }

    @Override
    public String toString(){
        return sum.entrySet().stream().map(s -> "For operation " + s + s.getValue().toString()).collect(Collectors.joining("\n"));
    }

    private class TimeHolder{
        private long totalTime;
        private int times;

        public TimeHolder(long totalTime){
            this.totalTime = totalTime;
            this.times = 1;
        }
        public void inc(long totalTime){
            this.totalTime+= totalTime;
            times++;
        }
        @Override
        public String toString(){
            return ", average: " + totalTime/times + " milis";
        }
    }
}
