package tasque;

import java.io.InputStream;
import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controls the main Tasque window.
 */
public class MainWindow extends AnchorPane {
    private static final Duration EXIT_DELAY = Duration.seconds(1.0);

    private final Image userImage = loadImage("/images/user.png");
    private final Image tasqueImage = loadImage("/images/tasque.png");

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Tasque tasque;

    /**
     * Creates the controller for the main Tasque window.
     */
    public MainWindow() {
    }

    @FXML
    private void initialize() {
        this.scrollPane.vvalueProperty().bind(this.dialogContainer.heightProperty());
    }

    /**
     * Supplies the Tasque backend used to process user commands.
     *
     * @param tasque Tasque backend for this window.
     */
    public void setTasque(Tasque tasque) {
        this.tasque = tasque;
        this.dialogContainer.getChildren().add(
                DialogBox.getTasqueDialog(tasque.getWelcomeMessage(), this.tasqueImage));
        Platform.runLater(this.userInput::requestFocus);
    }

    @FXML
    private void handleUserInput() {
        String input = this.userInput.getText();
        String response = this.tasque.getResponse(input);
        this.dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, this.userImage),
                DialogBox.getTasqueDialog(response, this.tasqueImage));
        this.userInput.clear();

        if (input.equals("bye")) {
            exitAfterDelay();
        }
    }

    private void exitAfterDelay() {
        this.userInput.setDisable(true);
        this.sendButton.setDisable(true);
        PauseTransition exitDelay = new PauseTransition(EXIT_DELAY);
        exitDelay.setOnFinished(event -> Platform.exit());
        exitDelay.play();
    }

    private static Image loadImage(String resourcePath) {
        InputStream imageStream = Objects.requireNonNull(
                MainWindow.class.getResourceAsStream(resourcePath),
                "Missing image resource: " + resourcePath);
        return new Image(imageStream);
    }
}
