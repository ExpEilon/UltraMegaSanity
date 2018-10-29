import org.junit.Test;

public class SafariSameTab extends BaseTest{

    @Test
    public void SafariSameTab() {
        client.hybridClearCache(true, true);
        client.launch("safari:www.google.com", true, true);
        String tabStart = client.getCurrentBrowserTabId();
        client.launch("safari:www.google.com", true, false);
        String tabEnd = client.getCurrentBrowserTabId();
        assert tabStart.equals(tabEnd);
    }
}
