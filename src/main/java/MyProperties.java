import java.util.HashMap;

public class MyProperties {

    static Connection runOn = Connection.STA;
        static String deviecsSN = "45a8ec778b1e6eb400e828cb989be9934fc03a8b";//SN separated by ',' or blank for all iOS devices.
//    static Class[] tests = {EriBankTest.class,SafariLongRun.class,JavaScriptTest.class,DeviceProperty.class,DeviceActions.class,RunNativeAPICallTest.class,PressTheDotTest.class,
//            CounterCommands.class,SetAuthTest.class,EriBankAppium.class,JSAppiumTest.class};//for new iOS OS
    static Class[] tests = {MonitorTest.class};//for general run
//    static Class[] tests = {SetLocationTest.class,JSAppiumTest.class};//Appium sanity
    static boolean allNighter = false;
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
        CloudMac ("http://192.168.2.29",9000,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek5qUTVNREl4TmpJMU9RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NTE4NTAyMTYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.uxBvakkHShYFaZMD3UgNKTZ6RhHALecCcWYgdAWqE8Q",
                "admin","Qwer1234",true),
        CloudWindows ("https://eilongrodsky-pc",443,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek16SXhNekkzTmpZek9RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDg1NzMyNzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.XsuzHTOHBmFawHabJYTOnr5fbjA17HhzQurSjzBBEnw",
                "admin","Experitest2012",true),
        MasterCloud ("http://mastercloud",443,"eyJ4cC51IjoxMSwieHAucCI6MiwieHAubSI6Ik1UVXdPVFUwTXpnMk5UVXhPUSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzEyODc5MjAsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.fm3frbnvYhEAtG4XtklXNPMALv_MAy2rVTgrYNy0-Y0",
                "khaleda","Experitest2012",true),
        QACloud ("https://qacloud.experitest.com",443,"eyJ4cC51IjoyMTgxLCJ4cC5wIjoyLCJ4cC5tIjoiTUEiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjQ2NzU2NDMxMDEsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.4ctoGGxmnD7-s1bbdi7HOUrsyPrVNgwx8VgLKZDn26I",
                    "khaleda","Experitest2012",true),
        ReleaseCloud ("https://releasecloud",443,"eyJ4cC51IjoyMTgxLCJ4cC5wIjoyLCJ4cC5tIjoiTUEiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjQ2NjY4NTkxMDcsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.jfDf6eJNY1RcjA6bSqM_KvXxppNtR8KDgoku1eulcXg",
                "khaleda","Experitest2012",true),
        qawin2016 ("https://qa-win2016.experitest.com",443,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek5ETXhOalV5TkRRMk1RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDk2NzY1MjUsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.BYiHtK5_0_AvX8cdzZBGXEE6jVuMLWBcxN3MAYCGIMQ",
                "admin","Experitest2012",true),
        navotCloud ("http://192.168.2.13",80,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek1UUXdNemM1T0RRMU53IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDY3NjM3OTgsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.uUzgJmI5vjF39iAtnwab4Wc-L41FQhjouo2D55OYCg4",
                "admin","Experitest2012",true),
        DorCloud ("http://192.168.2.41",80,"eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek1Ua3hNekE0TXpZM01nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDczNjcwMzcsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.6fgI5Rd1IzbzGAi_3wx6d2NMF4QO4cuKLXBsiClnpYs",
                "admin","Dd123456",true),
        ASE ("http://localhost",4723,"","","",false),
        MACASE ("http://192.168.2.197",4723,"","","",false),
        STA ("localhost",8889,"","","",false),
        MACSTA ("192.168.2.197",8889,"","","",false);

        public final String ip,AK,username,password;
        public int port;
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
}
