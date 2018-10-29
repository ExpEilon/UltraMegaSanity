import org.junit.Assert;
import org.junit.Test;

public class JavaScriptTest extends BaseTest {
    @Test
    public void hybridRunJavascript3(){
        client.launch("safari:http://www.wikipedia.org",true,true);
        Assert.assertTrue(client.waitForElement("Web", "xpath=//*[@alt='Wikipedia']", 0, 10000));
        Assert.assertEquals("{\"resultType\":\"\",\"message\":\"\",\"consoleLog\":\"\",\"succeed\":true}", client.hybridRunJavascript("", 0, "function a(){ return '' }; result = a;")); //
        Assert.assertEquals("",client.hybridRunJavascript("", 0, "result = null;")); //''
        Assert.assertEquals("{\"name\":\"asaf\",\"age\":\"30\",\"role\":\"qa\"}",client.hybridRunJavascript("", 0, "result = {name: 'asaf', age: '30', 'role': 'qa'};"));
        Assert.assertEquals("[item 1, item 2, item 3, {type=item, name=item 4}]",client.hybridRunJavascript("", 0, "var data2 = [ 'item 1', 'item 2', 'item 3' , { type: 'item', name: 'item 4' } ]; result = data2;")); //

        client.hybridRunJavascript("", 0, "result = document.getElementsByTagName('body')[0].innerHTML;");
        try{
            client.hybridRunJavascript("", 0, "a = 1; delete a; result = a;");
        }
        catch(Exception e){}

        try{
            client.hybridRunJavascript("", 0, "result = 1; throw 'exception';");
        }
        catch(Exception e){
            return;
        }
        Assert.fail();
    }
}
