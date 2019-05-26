import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class ClearApplicationDataAutomation extends SeeTestBase{
    String app = "com.experitest.UICatalog";
    private static final String DOCUMENTS_MANAGER_MENU_ITEM_XPATH = "nixpath=//*[@label='Documents file manager' and @class='UIAStaticText' and @knownSuperClass='UITableViewLabel' and @y>300 and @visible='true']";


    @Test
    public void keychainItemRemovedAfterApplicationClearData(){
        installAndLaunch();
        //test for issues SA_26469_SA_27581
        client.swipeWhileNotFound("Down", 100, 1000, "NATIVE", "//*[@accessibilityLabel='Keychain information' and @class='UITableViewLabel']", 0, 1000, 5, true);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='labelForKeychainItem']", 0, "testItemLabel");
        client.click("NATIVE", "xpath=//*[@text='Add']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@accessibilityIdentifier='lastStatusInformation' and @text='No error.']", 0, 2000);

        client.applicationClearData(app);

        // assert that the item is no longer in the keychain

        client.launch("com.experitest.UICatalog", true, true);
        client.swipeWhileNotFound("Down", 100, 1000, "NATIVE", "//*[@accessibilityLabel='Keychain information' and @class='UITableViewLabel']", 0, 1000, 5, true);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='labelForKeychainItem']", 0, "testItemLabel");
        client.sleep(500);
        client.click("NATIVE", "xpath=//*[@text='Find']", 0, 1);
        client.sleep(500);
        client.waitForElement("NATIVE", "xpath=//*[@accessibilityIdentifier='lastStatusInformation' and @text='The specified item could not be found in the keychain.']", 0, 5000);
    }

    @Test
    public void applicationSettingsResetToDefaultAfterApplicationClearData() {
        installAndLaunch();
//        log.info("open UICatalog settings in Preferences");
        client.launch("scheme:uicatalog://settings", true, true);

        final String optionalOpenButtonForScheme = "xpath=//*[@XCElementType='XCUIElementTypeButton' and @id='Open']";
        if (client.waitForElement("native", optionalOpenButtonForScheme, 0, 2000) || client.isElementFound("native", "xpath=//*[@XCElementType='XCUIElementTypeStaticText' and @id='Open in “UICatalog”?']")) {
            client.click("native", optionalOpenButtonForScheme, 0, 1);
        }

//        log.info("Change 'Enabled'");
        if(client.isElementFound("NATIVE", "xpath=//*[@value='1' and @text='Enabled' and @XCElementType='XCUIElementTypeSwitch']", 0)) {
            client.click("NATIVE", "xpath=//*[@value='1' and @text='Enabled' and @XCElementType='XCUIElementTypeSwitch']", 0, 1);
        }
//        log.info("Change group name");
        client.elementSendText("NATIVE", "xpath=//*[@XCElementType='XCUIElementTypeTextField' and @name='Name']", 0, "Non-default name");

//        log.info("Closing Settings application before clearing UICatalog data, because when left open, not all settings get synchronized.");
        client.applicationClose("com.apple.Preferences");

        client.applicationClearData(app);

//        log.info("launch with 'stopIfRunning == true', because cleaning logic in instrumentation library executes once on first launch");
        client.launch(app, true, true);
//        log.info("give instrumentation time to execute cleaning logic");
        client.sleep(5000);

//        log.info("verify settings are back to default");
        client.launchWithOptions(app, "{\"relaunch\":\"true\",\"launch_args\":[\"--experilog\"]}");
        client.launch("scheme:uicatalog://settings", true, true);
        client.verifyElementFound("NATIVE", "xpath=//*[@value='1' and @text='Enabled' and @XCElementType='XCUIElementTypeSwitch']", 0);
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Default name' and @XCElementType='XCUIElementTypeTextField' and @id='Name']", 0);
    }

    enum FILE_TYPE {
        FILE, FOLDER
    }

    private boolean create(final String relativePath, FILE_TYPE type) {
        if (FILE_TYPE.FILE == type) {
            return createFile(relativePath);
        } else {
            return createFolder(relativePath);
        }
    }

    // should work on non-instrumented and instrumented
    @Test
    public void containerClearedAfterClearApplicationData() {
        installAndLaunch();
        client.swipeWhileNotFound("down", 200, 200, "NATIVE", DOCUMENTS_MANAGER_MENU_ITEM_XPATH, 0, 500, 5, true);

        final List<Pair<String, FILE_TYPE>> toCreate = ImmutableList.<Pair<String, FILE_TYPE>>builder()
                .add(Pair.of("emptyFolder", FILE_TYPE.FOLDER))
                .add(Pair.of("folderWithFile", FILE_TYPE.FOLDER))
                .add(Pair.of("folderWithFile/file1", FILE_TYPE.FILE))
                .add(Pair.of("folderWithFolder", FILE_TYPE.FOLDER))
                .add(Pair.of("folderWithFolder/nestedFolder", FILE_TYPE.FOLDER))
                .add(Pair.of("folderWithFolderWithFiles", FILE_TYPE.FOLDER))
                .add(Pair.of("folderWithFolderWithFiles/nestedFolderWithFile", FILE_TYPE.FOLDER))
                .add(Pair.of("folderWithFolderWithFiles/nestedFolderWithFile/fileInNestedFolder_1", FILE_TYPE.FILE))
                .add(Pair.of("folderWithFolderWithFiles/nestedFolderWithFile/fileInNestedFolder_2", FILE_TYPE.FILE))
                .build()
                ;
        toCreate.forEach(p ->
                create(p.getLeft(), p.getRight()));

        client.applicationClearData(app);

        client.launch(app, false, true);
        client.swipeWhileNotFound("down", 200, 200, "NATIVE", DOCUMENTS_MANAGER_MENU_ITEM_XPATH, 0, 500, 5, true);

        final String notDeleted = toCreate.stream()
                .map(p -> p.getLeft())
                .filter(path -> !exists(path))
                .collect(Collectors.joining(", "));

        assertTrue(String.format("Some files/folders were not deleted from the application container: %s", notDeleted), notDeleted.isEmpty());
    }

    private boolean exists(String relativePath) {
        final String CHECK_EXISTS_XPATH = "nixpath=//*[@id='Exists?' and @class='UIAButton']";
        client.elementSendText("NATIVE", "nixpath=//*[@placeholder='<File/folder name to be created>' and @class='UIATextField']", 0, relativePath);
        client.click("NATIVE", CHECK_EXISTS_XPATH, 0, 1);
        return client.waitForElement("NATIVE", "nixpath=//*[@text=\"" + relativePath + " doesn't exist.\"]", 0, 10000);
    }

    private boolean createFile(String relativePath) {
        final String CREATE_FILE_XPATH = "nixpath=//*[@id='File' and @class='UIAButton']";
        client.elementSendText("NATIVE", "nixpath=//*[@placeholder='<File/folder name to be created>' and @class='UIATextField']", 0, relativePath);
        client.click("NATIVE", CREATE_FILE_XPATH, 0, 1);
        return client.waitForElement("NATIVE", "nixpath=//*[@text='Created file "  + relativePath + "' and @class='UIATextField']", 0, 10000);
    }

    private boolean createFolder(String relativePath) {
        final String CREATE_FOLDER_XPATH = "nixpath=//*[@id='Folder' and @class='UIAButton']";

        client.elementSendText("NATIVE", "nixpath=//*[@placeholder='<File/folder name to be created>' and @class='UIATextField']", 0, relativePath);
        client.click("NATIVE", CREATE_FOLDER_XPATH, 0, 1);
        return client.waitForElement("NATIVE", xpathForPathCreationResult(relativePath), 0, 10000);
    }

    private String xpathForPathCreationResult(String folder1) {
        return "nixpath=//*[@text='Created folder " + folder1 + "' and @class='UIATextField']";
    }

    private void installAndLaunch(){
        client.install(PathsMap.UICatalog,true,false);
        launch();
    }

}
