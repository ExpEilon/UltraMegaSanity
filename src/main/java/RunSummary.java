import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RunSummary {
    String test;
    long maxTime,totlaTime;
    String timeForLongest;
    int success,failure;
    Map<String,Integer> excSum;
    public RunSummary(String test){
        this.test = test;
        totlaTime = 0;
        maxTime = 0;
        success = 0;
        failure = 0;
        excSum = new HashMap<>();
    }
    public RunSummary update(long time,String when,org.junit.runner.Result result){
        totlaTime += time;
        if(time>maxTime) {
            maxTime = time;
            timeForLongest = when;
        }
        if(result.wasSuccessful())
            this.success++;
        else{
            String err = result.getFailures().get(0).getException().getMessage();
            if(excSum.get(err) == null)
                excSum.put(err,1);
            else
                excSum.put(err,excSum.get(err)+1);
            this.failure++;
        }
        return this;
    }
    @Override
    public String toString(){
        return "******************************************************\n"+
                "Test: " + test +" average time: " + (success==0 ? "" : totlaTime/success) + " longestDuration: " + maxTime +" ,at - " + timeForLongest + " ,success: " + success + " ,failed: " + failure + "\n" +
                excSum.entrySet().stream()
                        .map((entry) ->
                                "failed for exception: " + entry.getKey() + " for " + entry.getValue() + " times"
                        )
                        .collect(Collectors.joining("\n"));
    }
}
