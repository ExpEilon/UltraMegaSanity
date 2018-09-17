/**
 * Created by eyal.neumann on 2/19/2018.
 */
//package <set your test package>;

import com.experitest.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MultiGestureTest extends BaseTest{

    @Test
    public void testMultiGestureTest() throws Exception {
        /*******************************
         *
         *
         *  The app "Test MultiTouch needs to be downloaded first from App Store.
         *
         *
         ********************************/
        int waitTime = 1000;
        String deviceInfo = client.getDevicesInformation();
        String deviceScreenSize = deviceInfo.split("screensize=\"")[1].split("\"")[0];
        int screenX = Integer.parseInt(deviceScreenSize.split("x")[0]);
        int screenY = Integer.parseInt(deviceScreenSize.split("x")[1]);
        client.launch("com.ferretking.testtouch", true, false);
        client.startStepsGroup("MultiGestureTest");
        client.startMultiGesture("MultiGestureTest");
        client.multiTouchDownCoordinate(screenX/4, screenY/7, 0);
        client.multiWait(waitTime, 0);
        client.multiTouchMoveCoordinate(screenX/4, screenY/2, 0);
        client.multiWait(waitTime, 0);
        client.multiTouchUp(0);
        client.multiTouchDownCoordinate(3*screenX/4, screenY/2, 1);
        client.multiWait(waitTime, 1);
        client.multiTouchMoveCoordinate(screenX/10, screenY/2, 1);
        client.multiWait(waitTime, 1);
        client.multiTouchUp(1);
        client.multiTouchDownCoordinate(screenX/5, screenY/7, 2);
        client.multiWait(waitTime, 2);
        client.multiTouchMoveCoordinate(screenX/5, screenY/4, 2);
        client.multiWait(waitTime, 2);
        client.multiTouchUp(2);
        client.performMultiGesture();
        client.stopStepsGroup();
    }
}
