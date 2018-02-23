package gaj.apps.simulator.nc;

/**
 * Implements a simple, linear regression model of the game. The weights and
 * features represent the advantage of each board position to the last player
 * who made a move.
 */
public class NCSimpleModel extends NCBaseModel {

    private final double[] weights = new double[NCMovePosition.values().length];
    private final double discountRate;
    private final double learningRate;

    private double averageScore = 0;
    private int numTrainingExamples = 0;


    /**
     * Randomally initialises the model weights, and sets up the model for
     * future training.
     * 
     * @param discountRate
     *            - The discount rate, 0 < beta <= 1, which helps reduce weights
     *            with large magnitude.
     * @param learningRate
     *            - The learning rate, 0 < eta <= 1, which controls the
     *            step-size of the gradient ascent.
     */
    public NCSimpleModel(double discountRate, double learningRate) {
        this.discountRate = discountRate;
        this.learningRate = learningRate;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 2 * Math.signum(Math.random() - 0.5) * Math.random();
        }
    }

    @Override
    protected double scoreGame(NCGame game) {
        return scoreFeatures(getFeatures(game));
    }

    private double[] getFeatures(NCGame game) {
        double[] features = new double[NCMovePosition.values().length];
        if (game.numMoves() > 0) {
            NCMoveType lastType = game.getLastMove().getType();
            for (NCMove move : game) {
                features[move.getPosition().ordinal()] = (move.getType() == lastType) ? +1 : -1;
            }
        }
        return features;
    }

    private double scoreFeatures(double[] features) {
        double score = 0;
        for (int i = 0; i < weights.length; i++) {
            score += weights[i] * features[i];
        }
        return score;
    }

    @Override
    public double train(NCGame game) {
        if (game.isComplete()) {
            numTrainingExamples++;
            double actualScore = game.isWon() ? 10 : 0;
            double[] features = getFeatures(game);
            double estimatedScore = scoreFeatures(features);
            double err = actualScore - estimatedScore;
            averageScore += (err * err - actualScore) / numTrainingExamples;
            for (int i = 0; i < weights.length; i++) {
                weights[i] = discountRate * weights[i] + learningRate * err * features[i];
            }
        }
        return averageScore;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        for (double weight : weights) {
            buf.append(weight).append(", ");
        }
        buf.setLength(buf.length() - 2);
        return buf.toString();
    }

}
