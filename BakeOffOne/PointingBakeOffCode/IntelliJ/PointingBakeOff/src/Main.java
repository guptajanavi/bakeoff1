import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;

// Main class that extends PApplet, allowing use of Processing library functionalities
public class Main extends PApplet {
    // Fields for setting up the experiment parameters
    int margin = 200; // Margin from the window edges
    final int padding = 50; // Padding between buttons
    final int buttonSize = 40; // Size of each button
    ArrayList<Integer> trials = new ArrayList<Integer>(); // List to hold trial order
    int trialNum = 0; // Current trial number
    int lineStartIndex = 0; // Index for the start of the line (unused in this code)
    int startTime = 0; // Start time of the trials
    int finishTime = 0; // Finish time of the trials
    int hits = 0; // Number of successful hits
    int misses = 0; // Number of misses
    Robot robot; // Robot for automated tasks (not used in this version)
    int numRepeats = 1; // Number of times each button is repeated

    // Main method to launch the Processing sketch
    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        size(700, 700); // Set the size of the window
    }

    @Override
    public void setup() {
        noStroke(); // Disable drawing outlines for shapes
        textFont(createFont("Arial", 16)); // Set the font for text
        textAlign(CENTER); // Align text to the center
        frameRate(60); // Set the frame rate
        ellipseMode(CENTER); // Set ellipse drawing mode

        try {
            robot = new Robot(); // Initialize Robot for automated tasks
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Initialize the trial order and shuffle it
        for (int i = 0; i < 16; i++) {
            for (int k = 0; k < numRepeats; k++) {
                trials.add(i);
            }
        }
        Collections.shuffle(trials);
        System.out.println("trial order: " + trials);

        surface.setLocation(0, 0); // Set the window location
    }

    @Override
    public void draw() {
        background(0); // Clear the screen with a black background

        // Check if all trials are complete
        if (trialNum >= trials.size()) {
            displayResults(); // Display results if all trials are complete
            return;
        }

        // Display the current trial number
        fill(255); // Set fill color to white
        text((trialNum + 1) + " of " + trials.size(), 40, 20);

        // Draw a line between the previous and current targets, if applicable
        if (trialNum > 0 && trialNum < trials.size()) {
            drawPathBetweenTargets(trials.get(trialNum - 1), trials.get(trialNum));
        }

        // Draw all buttons
        for (int i = 0; i < 16; i++) {
            drawButton(i);
        }

        // Draw cursor representation
        fill(255, 0, 0, 200);
        ellipse(mouseX, mouseY, 30, 30);
    }

    // Method called when the mouse is pressed
    public void mousePressed() {
        if (trialNum >= trials.size()) return; // Exit if all trials are completed

        Rectangle bounds = getButtonLocation(trials.get(trialNum)); // Get current button bounds

        // Check if the mouse click is within the bounds of the target button
        if ((mouseX >= bounds.x && mouseX <= bounds.x + bounds.width) &&
                (mouseY >= bounds.y && mouseY <= bounds.y + bounds.height)) {
            System.out.println("HIT! " + trialNum);
            hits++; // Increment hit count
            trialNum++; // Move to the next trial
            if (trialNum == 1) {
                startTime = millis(); // Record start time at the first hit
            }
            if (trialNum == trials.size()) {
                finishTime = millis(); // Record finish time at the last hit
            }
        } else {
            System.out.println("MISSED! " + trialNum);
            misses++; // Increment miss count
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') { // Check if spacebar is pressed
            mousePressed(); // Perform the same action as mousePressed()
        }
    }

    // Method to get the location of a button given its index
    public Rectangle getButtonLocation(int i) {
        int x = (i % 4) * (padding + buttonSize) + margin;
        int y = (i / 4) * (padding + buttonSize) + margin;

        return new Rectangle(x, y, buttonSize, buttonSize);
    }

    // Method to draw a button given its index
    public void drawButton(int i) {
        Rectangle bounds = getButtonLocation(i);

        // Change fill color based on button state (current, next, or other)
        if (trials.get(trialNum) == i) {
            if ((mouseX > bounds.x && mouseX < bounds.x + bounds.width) && (mouseY > bounds.y && mouseY < bounds.y + bounds.height))
                fill(0, 255, 0); // Green for hover
            else
                fill(0, 255, 255); // Cyan for current target
        } else if (trialNum + 1 < trials.size() && trials.get(trialNum + 1) == i) {
            fill(255, 0, 0); // Red for next target
        } else {
            fill(200); // Gray for other buttons
        }

        rect(bounds.x, bounds.y, bounds.width, bounds.height); // Draw the button
    }

    // Method to draw a path between two targets
    public void drawPathBetweenTargets(int current, int next) {
        Rectangle currentBounds = getButtonLocation(current);
        Rectangle nextBounds = getButtonLocation(next);

        // Calculate center points of current and next buttons
        float currentCenterX = currentBounds.x + currentBounds.width / 2;
        float currentCenterY = currentBounds.y + currentBounds.height / 2;
        float nextCenterX = nextBounds.x + nextBounds.width / 2;
        float nextCenterY = nextBounds.y + nextBounds.height / 2;

        // Calculate direction and draw line
        float dx = nextCenterX - currentCenterX;
        float dy = nextCenterY - currentCenterY;
        float angle = atan2(dy, dx);

        float arrowOffset = 20; // Offset for the arrow from the target center
        float adjustedNextCenterX = nextCenterX - cos(angle) * arrowOffset;
        float adjustedNextCenterY = nextCenterY - sin(angle) * arrowOffset;

        stroke(255); // Set stroke color to white
        line(currentCenterX, currentCenterY, adjustedNextCenterX, adjustedNextCenterY); // Draw line

        drawArrow(adjustedNextCenterX, adjustedNextCenterY, angle); // Draw arrow at the end of the line
    }

    // Method to draw an arrow at the end of the line
    public void drawArrow(float x, float y, float angle) {
        pushMatrix(); // Save the current transformation matrix
        translate(x, y); // Translate to the end of the line
        rotate(angle); // Rotate to align with the line

        float arrowSize = 20; // Size of the arrow
        fill(255); // Set fill color to white
        noStroke(); // Disable stroke
        triangle(-arrowSize, arrowSize / 2, -arrowSize, -arrowSize / 2, 0, 0); // Draw arrow

        popMatrix(); // Restore the previous transformation matrix
    }

    // Method to display the results at the end of all trials
    private void displayResults() {
        float timeTaken = (finishTime - startTime) / 1000f; // Calculate total time taken
        float accuracy = (float) hits * 100f / (float) (hits + misses); // Calculate accuracy
        float penalty = constrain(((95f - accuracy) * .2f), 0, 100); // Calculate penalty

        // Display results
        fill(255); // Set fill color to white
        text("Finished!", width / 2, height / 2);
        text("Hits: " + hits, width / 2, height / 2 + 20);
        text("Misses: " + misses, width / 2, height / 2 + 40);
        text("Accuracy: " + accuracy + "%", width / 2, height / 2 + 60);
        text("Total time taken: " + timeTaken + " sec", width / 2, height / 2 + 80);
        text("Average time for each button: " + nf((timeTaken) / (float) (hits + misses), 0, 3) + " sec", width / 2, height / 2 + 100);
        text("Average time for each button + penalty: " + nf(((timeTaken) / (float) (hits + misses) + penalty), 0, 3) + " sec", width / 2, height / 2 + 140);
    }
}
  