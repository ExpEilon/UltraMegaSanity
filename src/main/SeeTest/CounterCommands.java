import org.junit.Test;

public class CounterCommands extends BaseTest{
    String instrumentedApp ="com.experitest.ExperiBank";
    String nonInstrumentedApp ="com.apple.Maps";
    double res;

    @Test
    public void getInstrumented() throws Exception {
        client.install(instrumentedApp ,true,false);
        counter("cpu",instrumentedApp);
        counter("memory",instrumentedApp);
        counter("battery",instrumentedApp); // wait till SA-25366 solved for non-instrumented
    }

    @Test
    public void getNonInstrumented() throws Exception {
        counter("cpu",nonInstrumentedApp);
        counter("memory",nonInstrumentedApp);
//        counter("battery",nonInstrumentedApp); // wait till SA-25366 solved for non-instrumented
    }

    public void counter(String counter,String app) throws Exception {
        client.launch(app, true, true);
        res = Double.parseDouble(client.getCounter(counter).replace(",",""));
        res += Double.parseDouble(client.getCounter(counter).replace(",","")); // in case first one doesn't work
        if (res<=0) {
            throw new Exception("Counter of " + counter +" didn't work");
        }
    }
}
