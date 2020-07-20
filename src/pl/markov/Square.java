package pl.markov;

public class Square {
    private double utility;
    private double reward;
    private SquareType type;
    private SquareAction optimalAction;

    public double getUtility() {
        return utility;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public SquareType getType() {
        return type;
    }

    public void setType(SquareType type) {
        this.type = type;
    }

    public SquareAction getOptimalAction() {
        return optimalAction;
    }

    public void setOptimalAction(SquareAction optimalAction) {
        this.optimalAction = optimalAction;
    }
}
