package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.UpdateExpandedEntryRequestEvent;

/**
 * An UI Part to show the specified expanded entry.
 */
public class ExpandedEntryPanel extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(ExpandedEntryPanel.class);
    private static final String FXML = "ExpandedEntryPanel.fxml";

    private final StringProperty displayed = new SimpleStringProperty("");

    @FXML
    private TextArea expandedEntryText;

    public ExpandedEntryPanel() {
        super(FXML);
        expandedEntryText.textProperty().bind(displayed);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleUpdateExpandedEntryRequestEvent(UpdateExpandedEntryRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> displayed.setValue(event.getUpdatedEntry().getDescription().toString()));

    }

}
