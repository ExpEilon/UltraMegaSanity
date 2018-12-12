import java.util.HashMap;
import java.util.Map;

public class MyProperties {

    static Connection runOn = Connection.CloudWindows;
    static String deviecsSN = "00008027-000319C21498002E";//SN separated by ',' or blank for all iOS devices.
    static Class[] tests = {EriBankTest.class,SafariLongRun.class,JavaScriptTest.class,DeviceProperty.class,DeviceActions.class,RunNativeAPICallTest.class,PressTheDotTest.class,
            CounterCommands.class,SetAuthTest.class,EriBankAppium.class,JSAppiumTest.class,PerformanceTest.class};//for new iOS OS
//    static Class[] tests = {EriBankTest.class,PressTheDotTest.class,SafariSameTab.class,SafariLongRun.class,DeviceActions.class,DeviceProperty.class,MonitorTest.class};//for general run
//    static Class[] tests = {SafariLongRun.class};//Appium sanity
    static int numOfRuns = 1000;
    static boolean installFromPath = false;
    static boolean instrumented = false;
    static boolean makeReporter = false;
    static boolean saveClientLogToFile = false;
    static boolean createContainer = false; //Works only on grid
    static boolean videoRecording = false; //Works only with reporter
    static int getClientLogsLevel = 0; // 0-> no logs, 1 -> normal level, 2 -> high level (extended with client extra logs)
    static int maxDevices = 10;
    static boolean collectSupportData = false;
    static int supportDataInterval = 5; //in minutes
    static long lastReboot = 0;

    enum Connection{
//        CloudWindows ("https://eilongrodsky-pc",443,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek16SXhNekkzTmpZek9RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDg1NzMyNzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.XsuzHTOHBmFawHabJYTOnr5fbjA17HhzQurSjzBBEnw",
//                "admin","Experitest2012",true),
        CloudWindows ("http://192.168.2.22",9000,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVME5ERXdNVEUzTXpneE1nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NTk0NjExNzQsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.jYD-urvjQ6cqlBL03EwyfYFWV4E8R2QMkCoqGJ1neA8",
                "admin","Experitest2012",true),
        CloudMac ("http://192.168.2.29",9000,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVME1ESXlOVE00TWpnMk9BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NTU1ODUzODMsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.REsnj_ETp0ajEpl40eMhQvI7vU-O0We87LKBqAWiFM0",
                "admin","Qwer1234",true),
        STA ("localhost",8889,"","","",false),
        ASE ("http://localhost",4723,"","","",false),
        MasterCloud ("https://mastercloud.experitest.com",443,"eyJ4cC51IjoxMSwieHAucCI6MiwieHAubSI6Ik1UVXdPVFUwTXpnMk5UVXhPUSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzEyODc5MjAsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.fm3frbnvYhEAtG4XtklXNPMALv_MAy2rVTgrYNy0-Y0",
                "khaleda","Experitest2012",true),
        QACloud ("https://qacloud.experitest.com",443,"eyJ4cC51IjoyMTgxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVME5EQTNPVGMzTmpJek5RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjQ2OTc2ODUyNDMsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.zzC2QOe1HHFp92Aniq2FlEK6Upcy6CHtoEDCYylmGW0",
                    "khaleda","Experitest2012",true),
        ReleaseCloud ("https://releasecloud",443,"eyJ4cC51IjoyMTgxLCJ4cC5wIjoyLCJ4cC5tIjoiTUEiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjQ2NjY4NTkxMDcsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.jfDf6eJNY1RcjA6bSqM_KvXxppNtR8KDgoku1eulcXg",
                "khaleda","Experitest2012",true),
        qawin2016 ("https://qa-win2016.experitest.com",443,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek5ETXhOalV5TkRRMk1RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDk2NzY1MjUsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.BYiHtK5_0_AvX8cdzZBGXEE6jVuMLWBcxN3MAYCGIMQ",
                "admin","Experitest2012",true),
        navotCloud ("http://192.168.2.13",80,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek1UUXdNemM1T0RRMU53IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDY3NjM3OTgsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.uUzgJmI5vjF39iAtnwab4Wc-L41FQhjouo2D55OYCg4",
                "admin","Experitest2012",true),
        DorCloud ("http://192.168.2.41",80,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek1Ua3hNekE0TXpZM01nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDczNjcwMzcsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.6fgI5Rd1IzbzGAi_3wx6d2NMF4QO4cuKLXBsiClnpYs",
                "admin","Dd123456",true),
        Costumer ("https://pearsonlab.cognizant.com",443,"eyJ4cC51IjozLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVME1URTRPVGN4T0RBek5BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NTY1NDk3MTgsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.hHOTaDgEZVKsvToYeeTCbineuaxdG4-T8XJAt6WQ9ZY",
                "admin","Dd123456",true),
        MACASE ("http://192.168.2.197",4723,"","","",false),
        MACSTA ("192.168.2.197",8889,"","","",false);

        public final String ip,AK,username,password;
        public final int port;
        public final boolean isGrid;

        Connection (String ip,int port,String AK, String username,String password,boolean isGrid){
            this.ip = ip;
            this.port = port;
            this.AK = AK;
            this.username = username;
            this.password = password;
            this.isGrid = isGrid;
        }
        String getURL(){
            return ip+":"+port;
        }
    }

    static final HashMap<Class, Integer> testsRunTimes = new HashMap<Class, Integer>() {{
        put(EriBankTest.class,100);
        put(SafariLongRun.class,70);
        put(JavaScriptTest.class,30);
        put(DeviceProperty.class,10);
        put(DeviceActions.class,40);
        put(RunNativeAPICallTest.class,50);
        put(PressTheDotTest.class,100);
        put(CounterCommands.class,50);
        put(SetAuthTest.class,330);
        put(EriBankAppium.class,1000);
        put(JSAppiumTest.class,1000);
        put(PerformanceTime.class,300);
        put(MonitorTest.class,90);
        put(SafariMenu.class,1000);
        put(SafariSameTab.class,60);

    }};
}
