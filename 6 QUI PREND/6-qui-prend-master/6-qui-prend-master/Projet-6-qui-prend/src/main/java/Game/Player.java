package Game;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Integer> cardList = new ArrayList<>();

    private int score;

    private String name;

    private Integer currentSelectCard;

    private Integer currentOfferCard;

    public Player(String name) {
        this.score = 0 ;
        this.name = name;
    }

    public List<Integer> getCardList() {
        return cardList;
    }

    public void setCardList(List<Integer> cardList) {
        this.cardList = cardList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Integer getCurrentSelectCard() {
        return currentSelectCard;
    }

    public void setCurrentSelectCard(Integer currentSelectCard) {
        this.currentSelectCard = currentSelectCard;
    }

    public Integer getCurrentOfferCard() {
        return currentOfferCard;
    }

    public void setCurrentOfferCard(Integer currentOfferCard) {
        this.currentOfferCard = currentOfferCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
