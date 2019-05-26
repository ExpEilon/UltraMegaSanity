import org.junit.Test;

public class CounterCommands extends SeeTestBase {
    String instrumentedApp ="com.experitest.ExperiBank";
    String nonInstrumentedApp ="com.apple.Maps";
    double res;

    @Test
    public void getInstrumented() throws Exception {
        install();
        counter("cpu",instrumentedApp);
        counter("memory",instrumentedApp);
//        counter("battery",instrumentedApp); // isn't supported
    }

    @Test
    public void getNonInstrumented() throws Exception {
        install();
        counter("cpu",nonInstrumentedApp);
        counter("memory",nonInstrumentedApp);
//        counter("battery",nonInstrumentedApp); // isn't supported
    }

    public void counter(String counter,String app) throws Exception {
        client.launch(app, true, true);
        res = Double.parseDouble(client.getCounter(counter).replace(",",""));
        res += Double.parseDouble(client.getCounter(counter).replace(",","")); // in case first one doesn't work
        if (res<=0) {
            throw new Exception("Counter of " + counter +" didn't work");
        }
    }
    public void install(){
        if(ConfigManager.checkIfSetTrue("installFromPath"))
            client.install(PathsMap.EriBank,false,false);
        else client.install((isGrid ? "cloud:":"")+instrumentedApp ,true,false);
    }
}
