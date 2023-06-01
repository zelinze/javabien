 package game.tauren;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.*;

 public class GameContent {

    public GameContent(){
        init();
    }

    private  List<Integer> allCards = new ArrayList<>();
    private Map<Integer, Image> allCardImage = new HashMap<>();

    private int totalCardCount = 64;

    private List<List<Integer>> boardCards= new ArrayList<>();

    private Player player ;
    private Player cpu ;

    public static final  Image BACKGROUND_IMAGE = new Image(loadResources("image/background.jpg"));
    public static final  Image INTRODUCTION_IMAGE = new Image(loadResources("image/introduction.png"));
    public static final  Image CARD_BACKGROUND_IMAGE = new Image(loadResources("image/cards/backside.png"));

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
            allCardImage.put(i,new Image(loadResources("image/cards/" +i+".png")));
        }
        player = new Player("player");
        cpu = new Player("AI");
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
        index = random.nextInt(allCards.size());
        card = allCards.remove(index);
        List<Integer> thirdCardList = new ArrayList<>();
        thirdCardList.add(card);
        boardCards.add(thirdCardList);
        index = random.nextInt(allCards.size());
        card = allCards.remove(index);
        List<Integer> fourthCardList = new ArrayList<>();
        fourthCardList.add(card);
        boardCards.add(fourthCardList);
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

     public static InputStream loadResources(String path){
         return GameContent.class.getResourceAsStream(path);
     }

     public Map<Integer, Image> getAllCardImage() {
         return allCardImage;
     }

     public void setAllCardImage(Map<Integer, Image> allCardImage) {
         this.allCardImage = allCardImage;
     }
 }
