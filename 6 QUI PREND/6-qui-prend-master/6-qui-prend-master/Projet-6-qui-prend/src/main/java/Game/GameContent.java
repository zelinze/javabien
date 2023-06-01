package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameContent {

    public GameContent(){
        init();
    }

    private  List<Integer> allCards = new ArrayList<>();

    private int totalCardCount = 64;

    private List<List<Integer>> boardCards= new ArrayList<>();

    private Player player ;
    private Player cpu ;

    public int getTotalCardCount() {
        return totalCardCount;
    }

    public void setTotalCardCount(int totalCardCount) {
        this.totalCardCount = totalCardCount;
    }

    public List<List<Integer>> getBoardCards() {
        return boardCards;
    }

    public void setBoardCards(List<List<Integer>> boardCards) {
        this.boardCards = boardCards;
    }

    public void init(){
        allCards.clear();
        for (int i = 1; i <= totalCardCount; i++) {
            allCards.add(i);
        }
        player = new Player("玩家");
        cpu = new Player("电脑");
        //随机牌头
        Random random = new Random();
        int index = random.nextInt(allCards.size());
        Integer card = allCards.remove(index);
        List<Integer> firstCardList = new ArrayList<>();
        firstCardList.add(card);
        boardCards.add(firstCardList);
        index = random.nextInt(allCards.size());
        card = allCards.remove(index);
        List<Integer> secondCardList = new ArrayList<>();
        secondCardList.add(card);
        boardCards.add(secondCardList);
    }

    public List<Integer> getAllCards() {
        return allCards;
    }

    public void setAllCards(List<Integer> allCards) {
        this.allCards = allCards;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getCpu() {
        return cpu;
    }

    public void setCpu(Player cpu) {
        this.cpu = cpu;
    }
}
