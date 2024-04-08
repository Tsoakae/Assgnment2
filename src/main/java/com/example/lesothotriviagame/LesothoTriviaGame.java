package com.example.lesothotriviagame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LesothoTriviaGame extends Application {

    private int currentQuestionIndex = 0;
    private int score = 0;
    private Timeline timer;
    private int timeRemaining = 15;

    private String[][] questions = {
            {"What is the capital city of Lesotho?", "Maseru", "Leribe", "Mokhotlong", "Quthing", "/videos/Maseru.mp4"},
            {"What is the traditional hat of Lesotho called?", "Mokorotlo", "Bokgeleke", "Khokhovula", "Umkhovu", "/Images/Mokorotlo.jpg"},
            {"Who is the founder of Lesotho?", "Moshoeshoe I", "Moshoeshoe II", "Sekhonyana II", "Letsie III", "/Images/Moshoeshoe I.jpg"},
            {"Which mountain range covers much of Lesotho?", "Drakensberg Mountains", "Atlas Mountains", "Andes Mountains", "Himalayas", "/videos/Drakensberg Mountains.mp4"}
    };

    private String[] correctAnswers = {
            "Maseru",
            "Mokorotlo",
            "Moshoeshoe I",
            "Drakensberg Mountains"
    };

    private Label questionNumberLabel;
    private Label scoreLabel;
    private Label questionLabel;
    private HBox bottomPane;
    private Label timerLabel;
    private StackPane imagePlaceholder;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lesotho Trivia Game");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.LIGHTBLUE); // Set background color

        HBox topPane = new HBox();
        topPane.setPadding(new Insets(10));
        topPane.setSpacing(20);
        questionNumberLabel = new Label();
        scoreLabel = new Label();
        timerLabel = new Label();
        // Set font and color for labels
        questionNumberLabel.setFont(Font.font("Arial", 18));
        scoreLabel.setFont(Font.font("Arial", 18));
        timerLabel.setFont(Font.font("Arial", 18));
        questionNumberLabel.setTextFill(Color.WHITE);
        scoreLabel.setTextFill(Color.WHITE);
        timerLabel.setTextFill(Color.WHITE);
        topPane.getChildren().addAll(questionNumberLabel, scoreLabel, timerLabel);
        topPane.setStyle("-fx-background-color: #333333;"); // Set background color
        root.setTop(topPane);

        GridPane centerPane = new GridPane();
        centerPane.setPadding(new Insets(10));
        centerPane.setVgap(20);
        centerPane.setHgap(20);
        questionLabel = new Label();
        // Set font and color for question label
        questionLabel.setFont(Font.font("Arial", 20));
        questionLabel.setTextFill(Color.BLACK);
        centerPane.add(questionLabel, 0, 0);
        imagePlaceholder = new StackPane();
        imagePlaceholder.setPrefSize(400, 300);
        centerPane.add(imagePlaceholder, 0, 1);
        centerPane.setAlignment(Pos.CENTER); // Center content vertically and horizontally
        root.setCenter(centerPane);

        bottomPane = new HBox(); // Use HBox for horizontal layout
        bottomPane.setPadding(new Insets(10));
        bottomPane.setSpacing(10);
        bottomPane.setAlignment(Pos.CENTER); // Center content horizontally
        root.setBottom(bottomPane);

        displayQuestion();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayQuestion() {
        startTimer();

        if (currentQuestionIndex >= questions.length) {
            endGame();
            return;
        }

        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1));
        scoreLabel.setText("Score: " + score);
        questionLabel.setText(questions[currentQuestionIndex][0]);

        loadMedia(questions[currentQuestionIndex][5]); // Load media for the current question

        bottomPane.getChildren().clear(); // Clear previous buttons
        for (int i = 0; i < 4; i++) {
            Button optionButton = new Button(questions[currentQuestionIndex][i + 1]);
            optionButton.setOnAction(e -> handleAnswer(optionButton.getText()));
            optionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;"); // Set button style
            bottomPane.getChildren().add(optionButton);
        }
    }
    private void loadMedia(String mediaPath) {
        if (mediaPath.endsWith(".mp4")) {
            // Load video clip
            Media video = new Media(getClass().getResource(mediaPath).toExternalForm());
            MediaPlayer player = new MediaPlayer(video);
            MediaView mediaView = new MediaView(player);

            // Set width and height to match images
            mediaView.setFitWidth(600);
            mediaView.setFitHeight(500);

            imagePlaceholder.getChildren().clear();
            imagePlaceholder.getChildren().add(mediaView);
            player.play();
        } else if (mediaPath.endsWith(".jpg") || mediaPath.endsWith(".png")) {
            // Load image
            javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResourceAsStream(mediaPath));
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
            double maxWidth = 600; // Set the desired width
            double maxHeight = 500; // Set the desired height
            double scaleFactor = Math.min(maxWidth / image.getWidth(), maxHeight / image.getHeight());
            imageView.setFitWidth(image.getWidth() * scaleFactor);
            imageView.setFitHeight(image.getHeight() * scaleFactor);
            imagePlaceholder.getChildren().clear();
            imagePlaceholder.getChildren().add(imageView);
        }
    }


    private void handleAnswer(String selectedAnswer) {
        stopTimer();

        // Check if currentQuestionIndex is within bounds
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.length) {
            if (currentQuestionIndex < questions.length) {
                String correctAnswer = correctAnswers[currentQuestionIndex];
                if (selectedAnswer.equals(correctAnswer)) {
                    score++;
                } else {
                    System.out.println("OPPS! INCORRECT");
                }
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    displayQuestion();
                } else {
                    endGame();
                }
            }
        }
    }
    private void endGame() {
        stopTimer();
        String scoreText = score + "/" + questions.length;
        if (score == questions.length) {
            // Create and configure an alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!🎉");
            alert.setHeaderText("You got all answers correct!");
            alert.setContentText("Well done! You are a Lesotho trivia master! 🎉");

            // Show the alert dialog
            alert.showAndWait();
        } else {
            // Create a label to display the final score
            Label finalScoreLabel = new Label("Game Over. Your final score is: " + scoreText);
            finalScoreLabel.setFont(Font.font("Arial", 18));
            finalScoreLabel.setTextFill(Color.BLACK);

            // Set position for final score label
            BorderPane.setAlignment(finalScoreLabel, Pos.CENTER);
            BorderPane.setMargin(finalScoreLabel, new Insets(10));

            // Add final score label to the root pane
            ((BorderPane) bottomPane.getParent()).setCenter(finalScoreLabel);
        }
        scoreLabel.setText("Score: " + scoreText); // Update score label
    }

    private void startTimer() {
        int duration = (currentQuestionIndex == 0 || currentQuestionIndex == questions.length - 1) ? 5 : 15;
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            if (timeRemaining >= 0) {
                timerLabel.setText("Time Remaining: " + timeRemaining + "s");
            } else {
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    displayQuestion();
                } else {
                    endGame();
                }
            }
        }));
        timer.setCycleCount(duration); // Set the cycle count based on the duration
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
        timeRemaining = 15;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
