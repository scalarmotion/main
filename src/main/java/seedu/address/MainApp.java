package seedu.address;

import static seedu.address.storage.AwarenessStorage.AWARENESS_FILEPATH;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.AddressBook;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.awareness.Awareness;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.storage.XmlAddressBookStorage;
import seedu.address.storage.XmlAwarenessStorage;
import seedu.address.storage.entry.EntryBookStorage;
import seedu.address.storage.entry.XmlEntryBookStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 3, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;


    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing ResuMaker ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        AddressBookStorage addressBookStorage = new XmlAddressBookStorage(
                // Paths.get("data", "addressbook.xml"));
                // ^ Temp, to avoid breaking system tests. TODO: Remove with other AB code.
                userPrefs.getEntryBookFilePath());
        EntryBookStorage entryBookStorage = new XmlEntryBookStorage(userPrefs.getEntryBookFilePath());
        storage = new StorageManager(addressBookStorage, entryBookStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();

    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or an empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {

        final String messageFileNotFound = "Data file not found. Will be starting with a sample %s.";
        final String messageFormatProblem = "Data file not in the correct format. Will be starting with an empty %s.";
        final String messageIoProblem = "Problem while reading from the file. Will be starting with an empty %s.";

        // need to update system tests before these can be removed
        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook initialData;

        try {
            addressBookOptional = storage.readAddressBook();
            initialData = addressBookOptional.orElseGet(() -> {
                logger.info(String.format(messageFileNotFound, "Addressbook"));
                return SampleDataUtil.getSampleAddressBook();
            }
            );
        } catch (DataConversionException e) {
            logger.warning(String.format(messageFormatProblem, "Addressbook"));
            initialData = new AddressBook();
        } catch (IOException e) {
            logger.warning(String.format(messageIoProblem, "Addressbook"));
            initialData = new AddressBook();
        }

        Optional<ReadOnlyEntryBook> entryBookOptional;
        ReadOnlyEntryBook initialDataForEntryBook;

        try {
            entryBookOptional = storage.readEntryBook();
            initialDataForEntryBook = entryBookOptional.orElseGet(() -> {
                logger.info(String.format(messageFileNotFound, "entrybook"));
                return SampleDataUtil.getSampleEntryBook();
            }
            );
        } catch (DataConversionException e) {
            logger.warning(String.format(messageFormatProblem, "entrybook"));
            initialDataForEntryBook = new EntryBook();
        } catch (IOException e) {
            logger.warning(String.format(messageIoProblem, "entrybook"));
            initialDataForEntryBook = new EntryBook();
        }

        Optional<Awareness> awarenessOptional;
        Awareness awareness;

        try {
            awarenessOptional = new XmlAwarenessStorage(AWARENESS_FILEPATH).readAwarenessData();
            awareness = awarenessOptional.orElseGet(() -> {
                logger.info(String.format(messageFileNotFound, "awareness"));
                return SampleDataUtil.getSampleAwareness();
            });

        } catch (DataConversionException e) {
            logger.warning(String.format(messageFormatProblem, "awareness"));
            awareness = new Awareness();
        } catch (IOException e) {
            logger.warning(String.format(messageIoProblem, "awareness"));
            awareness = new Awareness();
        }


        return new ModelManager(initialData, initialDataForEntryBook, userPrefs, awareness);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting ResuMaker " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping ResuMaker ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
