import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FishQuizApp {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private int fishiness = 50;
    private int questionIndex = 0;

    private JProgressBar fishMeter;
    private JLabel nameLabel;
    private JLabel imageLabel; // Single label for both trout & results
    private String userName = "";

    private java.util.List<Question> questions = new ArrayList<>();

    public FishQuizApp() {
        frame = new JFrame("Fish: Find Your True Self");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Fish meter
        fishMeter = new JProgressBar(0, 100);
        fishMeter.setValue(fishiness);
        fishMeter.setStringPainted(true);
        fishMeter.setString("Fishy-o-meter");

        // Image label (trout placeholder at first)
        imageLabel = new JLabel(loadIcon("/result3.jpg", 250, 200));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(fishMeter);
        topPanel.add(imageLabel);


        frame.add(topPanel, BorderLayout.NORTH);

        // Questions
        initializeQuestions();
        Collections.shuffle(questions);

        // Panels
        mainPanel.add(createTitlePanel(), "TITLE");
        for (int i = 0; i < questions.size(); i++) {
            mainPanel.add(createQuestionPanel(questions.get(i)), "Q" + i);
        }
        mainPanel.add(createResultPanel(), "RESULT");

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "TITLE");
    }

    // Load and scale images from resources
    private ImageIcon loadIcon(String resourcePath, int width, int height) {
        java.net.URL imgURL = getClass().getResource(resourcePath);
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage();
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            System.err.println("WARNING: Could not find resource: " + resourcePath);
            return new ImageIcon();
        }
    }

    private void initializeQuestions() {
        questions.add(new Question("Do you enjoy swimming?",
                new Answer("Yes", +10), new Answer("No", -10), new Answer("Absolutely", +20)));
        questions.add(new Question("If you could pick one place to live for the rest of your life, where would you pick?",
                new Answer("Near the ocean", +15), new Answer("Inland, clearly", -10), new Answer("Bathtub", +5)));
        questions.add(new Question("Do you wish that you could breathe underwater?",
                new Answer("Obviously", +20), new Answer("No??", -15), new Answer("Maybe", +10)));
        questions.add(new Question("Do you have fond memories of being a fish?",
                new Answer("Yes", +20), new Answer("No", -15), new Answer("Possibly", +10)));
        questions.add(new Question("Does the thought of being called a 'punk bass fish' upset you?",
                new Answer("Yes, it would.", -10), new Answer("No, it wouldn't", +10), new Answer("Large or small mouth bass?", +5)));

        questions.add(new Question("Gills. Thoughts?",
                new Answer("You've piqued my interest", +10), new Answer("...what about them?", -10), new Answer("...go on...", +5)));
        questions.add(new Question("Consider the following, you acquire a new betta fish, which tank would you put it in?",
                new Answer("Clearly one with space and filtering.", +10), new Answer("A bowl with water is a bowl with water.", -10), new Answer("If I pick a good enough place, can we be roomies?", +20)));
        questions.add(new Question("How many triangles do you know? As a friend?",
                new Answer("At least 4.", +10), new Answer("I thought this was about fish.", -10), new Answer("Do triangular fish count?", +5)));
        questions.add(new Question("You come across a goldfish. A common, standard fish. What do you think of it?",
                new Answer("Classic carp body, ol' reliable", +5), new Answer("The most boring fish ever, duh.", -10), new Answer("My fishy brethren, I salute thee.", +10)));
        questions.add(new Question("What do you consider a fish to be?",
                new Answer("A fish is a creature with a wish.", +10), new Answer("The swimmy ones.", -10), new Answer("...they're fish, who cares?", +5)));
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Welcome to the Fish: Find Your True Self", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField nameField = new JTextField();
        JButton startButton = new JButton("Start Quiz");

        startButton.addActionListener(e -> {
            userName = nameField.getText().trim();
            if (userName.isEmpty()) userName = "Mysterious Fish";
            nameLabel.setText("Player: " + userName);
            resetQuizState();
            imageLabel.setIcon(loadIcon("/result3.jpg", 250, 200)); // Reset trout
            cardLayout.show(mainPanel, "Q0");
        });

        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        inputPanel.add(new JLabel("Take this 10 question quiz to find your fishiness.", SwingConstants.CENTER));
        inputPanel.add(new JLabel("Enter your name:", SwingConstants.CENTER));
        inputPanel.add(nameField);

        panel.add(title, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQuestionPanel(Question question) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel questionLabel = new JLabel(question.text, SwingConstants.CENTER);
        panel.add(questionLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        for (Answer ans : question.answers) {
            JButton btn = new JButton(ans.text);
            btn.addActionListener(e -> answerQuestion(ans.value));
            buttonPanel.add(btn);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        nameLabel = new JLabel("", SwingConstants.CENTER);
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setName("resultLabel");

        JPanel buttonPanel = new JPanel();
        JButton restartButton = new JButton("Restart Quiz");
        JButton exitButton = new JButton("Exit");

        restartButton.addActionListener(e -> {
            resetQuizState();
            imageLabel.setIcon(loadIcon("/result3.jpg", 250, 200));
            cardLayout.show(mainPanel, "TITLE");
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(resultLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void answerQuestion(int value) {
        fishiness += value;
        fishiness = Math.max(0, Math.min(100, fishiness));

        fishMeter.setValue(fishiness);
        fishMeter.setString("Fishy-o-meter: " + fishiness + "%");

        questionIndex++;
        if (questionIndex < questions.size()) {
            cardLayout.show(mainPanel, "Q" + questionIndex);
        } else {
            updateResultImage();
            showResult();
            cardLayout.show(mainPanel, "RESULT");
        }
    }

    private void updateResultImage() {
        if (fishiness >= 0 && fishiness <= 25) imageLabel.setIcon(loadIcon("/result1.jpg", 250, 200));
        else if (fishiness >= 26 && fishiness <= 50) imageLabel.setIcon(loadIcon("/result2.jpg", 250, 200));
        else if (fishiness >= 51 && fishiness <= 75) imageLabel.setIcon(loadIcon("/result3.jpg", 250, 200));
        else imageLabel.setIcon(loadIcon("/result4.png", 250, 200));
    }

    private void showResult() {
        String result;
        if (fishiness >= 0 && fishiness <= 25) result = " you're not fishy. Frankly, you're dry. REAL dry.";
        else if (fishiness >= 26 && fishiness <= 50) result = "  you're moderately fishy. You dabble in the concept of fish.";
        else if (fishiness >= 51 && fishiness <= 75) result = " you are no longer bound by the land laws.";
        else result = ", YOU ARE EXTREMELY FISH. Return to the ocean IMMEDIATELY!";

        nameLabel.setText(userName + ", " + result);
    }

    private void resetQuizState() {
        fishiness = 50;
        questionIndex = 0;
        fishMeter.setValue(fishiness);
        fishMeter.setString("Fishy-o-meter");
        Collections.shuffle(questions);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FishQuizApp::new);
    }

    static class Question {
        String text;
        Answer[] answers;
        Question(String text, Answer... answers) { this.text = text; this.answers = answers; }
    }

    static class Answer {
        String text;
        int value;
        Answer(String text, int value) { this.text = text; this.value = value; }
    }
}