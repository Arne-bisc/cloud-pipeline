package com.epam.pipeline.autotests;

import com.epam.pipeline.autotests.ao.GlobalSearchAO;
import com.epam.pipeline.autotests.ao.LibraryFolderAO;
import com.epam.pipeline.autotests.ao.NavigationHomeAO;
import com.epam.pipeline.autotests.ao.NavigationMenuAO;
import com.epam.pipeline.autotests.mixins.Navigation;
import com.epam.pipeline.autotests.utils.C;
import com.epam.pipeline.autotests.utils.TestCase;
import com.epam.pipeline.autotests.utils.Utils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.hasText;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.epam.pipeline.autotests.ao.Primitive.*;
import static com.epam.pipeline.autotests.ao.Profile.advancedTab;
import static com.epam.pipeline.autotests.utils.Utils.sleep;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class GlobalSearchTest extends AbstractBfxPipelineTest implements Navigation {

    private final String folder = "globalSearchFolder" + Utils.randomSuffix();
    private final String pipeline = "globalSearchPipeline" + Utils.randomSuffix();
    private final String configuration = "globalSearchConf" + Utils.randomSuffix();
    private final String innerFolder1 = "globalSearchInnerFolder1" + Utils.randomSuffix();
    private final String innerFolder2 = "globalSearchInnerFolder2" + Utils.randomSuffix();
    private final String customConfigurationProfile = "custom-profile";
    private final String customDisk = "22";
    private final String defaultInstanceType = C.DEFAULT_INSTANCE;
    private final String defaultRegistry = C.DEFAULT_REGISTRY;
    private final String defaultGroup = C.DEFAULT_GROUP;
    private final String testingTool = C.TESTING_TOOL_NAME;

    private final String title = "testIssue";
    private final String description = "testIssueDescription";

    @BeforeClass
    @TestCase(value = {"EPMCMBIBPC-2653"})
    public void prepareForSearch() {
        navigationMenu()
                .library()
                .createFolder(folder)
                .cd(folder)
                .createPipeline(pipeline)
                .createFolder(innerFolder1)
                .createConfiguration(configuration)
                .configurationWithin(configuration, configuration ->
                        configuration.expandTabs(advancedTab)
                                .setValue(DISK, customDisk)
                                .selectValue(INSTANCE_TYPE, defaultInstanceType)
                                .setValue(NAME, customConfigurationProfile)
                                .selectDockerImage(dockerImage ->
                                        dockerImage
                                                .selectRegistry(defaultRegistry)
                                                .selectGroup(defaultGroup)
                                                .selectTool(testingTool)
                                                .click(OK)
                                )
                                .click(SAVE)
                );
        navigationMenu()
                .library()
                .cd(folder)
                .cd(innerFolder1)
                .createFolder(innerFolder2);
        home()
                .globalSearch()
                .ensureVisible(FOLDERS, PIPELINES, RUNS, TOOLS, DATA, ISSUES, SEARCH, QUESTION_MARK)
                .ensureAll(enabled, FOLDERS, PIPELINES, RUNS, TOOLS, DATA, ISSUES)
                .close();
        home()
                .ensure(NavigationHomeAO.panel("Active runs"), visible);
        sleep(2, SECONDS);
        search()
                .hover(QUESTION_MARK)
                .sleep(1, SECONDS)
                .messageShouldAppear("The query string supports the following special characters:")
                .close();

        sleep(2, MINUTES);
    }

    @Test
    @TestCase(value = {"EPMCMBIBPC-2657"})
    public void searchResultCancel() {
        search()
                .search(folder)
                .enter()
                .sleep(3, SECONDS)
                .ensure(FOLDERS, enabled)
                .ensureAll(GlobalSearchAO.disable, PIPELINES, RUNS, TOOLS, DATA, ISSUES)
                .hover(SEARCH_RESULT)
                .close()
                .ensureAll(enabled, FOLDERS, PIPELINES, RUNS, TOOLS, DATA, ISSUES)
                .ensure(SEARCH, empty)
                .close();
    }

    @Test
    @TestCase(value = {"EPMCMBIBPC-2670"})
    public void issueSearch() {
        library()
                .clickOnFolder(folder)
                .showIssues()
                .clickNewIssue()
                .addNewIssue(title, description);
        home().sleep(6, MINUTES);
        search()
                .search(title)
                .enter()
                .sleep(2, SECONDS)
                .hover(SEARCH_RESULT)
                .openSearchResultItem(title)
                .ensure(TITLE, hasText(title))
                .ensure(DESCRIPTION, matchText("Opened .* by You"))
                .ensure(HIGHLIGHTS, hasText("Found in name"))
                .ensure(PREVIEW, hasText(description))
                .close()
                .close();
        search()
                .click(ISSUES)
                .search(description)
                .enter()
                .hover(SEARCH_RESULT)
                .openSearchResultItem(title)
                .ensure(TITLE, hasText(title))
                .ensure(DESCRIPTION, matchText("Opened .* by You"))
                .ensure(HIGHLIGHTS, hasText("Found in text"))
                .ensure(PREVIEW, hasText(description))
                .close()
                .close();
    }

    @Test(dependsOnMethods = {"issueSearch"})
    @TestCase(value = {"EPMCMBIBPC-2671"})
    public void identicalNamesSearch() {
        library()
                .cd(folder)
                .createFolder(title);
        home().sleep(5, MINUTES);
        search()
                .ensureAll(enabled, FOLDERS, PIPELINES, RUNS, TOOLS, DATA, ISSUES)
                .search(title)
                .enter()
                .ensureAll(GlobalSearchAO.disable, PIPELINES, RUNS, TOOLS, DATA)
                .ensureAll(enabled, FOLDERS, ISSUES)
                .ensure(FOLDERS, hasText("1 FOLDER"))
                .ensure(ISSUES, hasText("1 ISSUE"))
                .validateSearchResults(2, title)
                .click(FOLDERS)
                .ensureAll(enabled, FOLDERS, ISSUES)
                .ensure(FOLDERS, GlobalSearchAO.selected)
                .validateSearchResults(1, title)
                .click(FOLDERS)
                .ensureAll(enabled, FOLDERS, ISSUES)
                .validateSearchResults(2, title)
                .close()
                .click(ISSUES)
                .ensureAll(enabled, FOLDERS, PIPELINES, RUNS, TOOLS, DATA, ISSUES)
                .ensure(ISSUES, GlobalSearchAO.selected)
                .search(title)
                .enter()
                .ensureAll(GlobalSearchAO.disable, FOLDERS, PIPELINES, RUNS, TOOLS, DATA)
                .ensure(ISSUES, enabled)
                .ensure(ISSUES, hasText("1 ISSUE"))
                .ensure(ISSUES, GlobalSearchAO.selected)
                .validateSearchResults(1, title)
                .click(ISSUES)
                .ensureAll(GlobalSearchAO.disable, PIPELINES, RUNS, TOOLS, DATA)
                .ensureAll(enabled, FOLDERS, ISSUES)
                .validateSearchResults(2, title)
                .close()
                .close();
    }

    @Test
    @TestCase(value = {"EPMCMBIBPC-2654"})
    public void folderSearch() {
        search()
                .search(innerFolder1)
                .sleep(3, SECONDS)
                .validateSearchResults(1, innerFolder1)
                .hover(SEARCH_RESULT)
                .openSearchResultItem(innerFolder1)
                .ensure(TITLE, hasText(innerFolder1))
                .ensure(HIGHLIGHTS, hasText("Found in name"))
                .ensure(PREVIEW, hasText(innerFolder2))
                .parent()
                .moveToSearchResultItem(innerFolder1, () -> new LibraryFolderAO(innerFolder1))
                .ensureAll(visible, SETTINGS, UPLOAD_METADATA);
    }

    @Test
    @TestCase(value = {"EPMCMBIBPC-2655"})
    public void folderSearchByEnterKey() {
        search()
                .search(innerFolder1)
                .enter()
                .hover(SEARCH_RESULT)
                .openSearchResultItem(innerFolder1)
                .ensure(TITLE, hasText(innerFolder1))
                .ensure(HIGHLIGHTS, hasText("Found in name"))
                .ensure(PREVIEW, hasText(innerFolder2))
                .parent()
                .moveToSearchResultItem(innerFolder1, () -> new LibraryFolderAO(innerFolder1))
                .ensureAll(visible, SETTINGS, UPLOAD_METADATA);
    }

    @Test
    @TestCase(value = {"EPMCMBIBPC-2656"})
    public void negativeFolderSearch() {
        search()
                .click(PIPELINES)
                .search(innerFolder1)
                .enter()
                .validateSearchResults(0, "")
                .close()
                .search(innerFolder1.toLowerCase())
                .enter()
                .validateSearchResults(0, "")
                .close()
                .search(innerFolder1.substring(0, innerFolder1.length()/2 - 1))
                .enter()
                .validateSearchResults(0, "")
                .close()
                .close();
    }

    @Test(priority = 100)
    @TestCase(value = {"EPMCMBIBPC-2676"})
    public void searchAfterDeleting() {
        library()
                .removeNotEmptyFolder(folder)
                .sleep(5, MINUTES);
        home();
        search()
                .search(pipeline)
                .enter()
                .validateSearchResults(0, "")
                .search(configuration)
                .enter()
                .validateSearchResults(0, "")
                .search(innerFolder1)
                .enter()
                .validateSearchResults(0, "")
                .search(title)
                .enter()
                .validateSearchResults(0, "")
                .close()
                .close();
    }

    private GlobalSearchAO search() {
        return new NavigationMenuAO().search();
    }
}
