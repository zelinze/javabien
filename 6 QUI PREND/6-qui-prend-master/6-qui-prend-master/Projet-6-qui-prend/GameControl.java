
package game.tauren;

import java.util.List;
import java.util.Random;

public class GameControl {


    public static void licensing(GameContent content){
        Random random = new Random();
        List<Integer> allCards = content.getAllCards();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(allCards.size());
            Integer card = allCards.remove(index);
            content.getCpu().getCardList().add(card);
            index = random.nextInt(allCards.size());
            card = allCards.remove(index);
            content.getPlayer().getCardList().add(card);
        }
    }

    public static int getTaurenCount(int card){
        int result = 1;
        if(card%5==0 && card %10 != 0 ){
            result=2;
        }else if(card%10 == 0){
            result=3;
        }else if(card%11 == 0){
            result=5;
        }
        if(card == 55){
            result=7;
        }
        return result;
    }


    public static void selectCard(int index, GameContent content){
        if(content.getPlayer().getCardList().size() >index){
            if(content.getPlayer().getCurrentSelectCard() != content.getPlayer().getCardList().get(index)){
                content.getPlayer().setCurrentSelectCard( content.getPlayer().getCardList().get(index));
            }else{
                content.getPlayer().setCurrentSelectCard(null);
            }
        }
    }

    public static void offerCard(GameContent content) {
        cpuSelected(content);
        Integer cpuCurrentSelectCard = content.getCpu().getCurrentSelectCard();
        Integer playerCurrentSelectCard = content.getPlayer().getCurrentSelectCard();
        if(cpuCurrentSelectCard.intValue() > playerCurrentSelectCard.intValue()){
            int index = getPlaceLocation(content,playerCurrentSelectCard);
            dealOfferCard(index,content.getPlayer(),content.getBoardCards(),playerCurrentSelectCard);
            index = getPlaceLocation(content,cpuCurrentSelectCard);
            dealOfferCard(index,content.getCpu(),content.getBoardCards(),cpuCurrentSelectCard);
        }else{
            int index = getPlaceLocation(content,cpuCurrentSelectCard);
            dealOfferCard(index,content.getCpu(),content.getBoardCards(),cpuCurrentSelectCard);
            index = getPlaceLocation(content,playerCurrentSelectCard);
            dealOfferCard(index,content.getPlayer(),content.getBoardCards(),playerCurrentSelectCard);
        }
    }

    public static void dealOfferCard(int index, Player player, List<List<Integer>> boardCards, Integer card) {
        if(index == -1){
            int resultIndex = -1 ;
            int resultAddScore = 0 ;
            for (int i = 0; i < boardCards.size(); i++) {
                List<Integer> row = boardCards.get(i);
                int currentTotalScore = 0 ;
                for (int j = 0; j < row.size(); j++) {
                    currentTotalScore = currentTotalScore + getTaurenCount(row.get(j));
                }
                if(resultAddScore == 0 ){
                    resultAddScore = currentTotalScore;
                    resultIndex = i;
                }else{
                    if(currentTotalScore<resultAddScore){
                        resultAddScore = currentTotalScore;
                        resultIndex = i;
                    }
                }
            }
            boardCards.get(resultIndex).clear();
            boardCards.get(resultIndex).add(card);
            player.setScore(player.getScore()+resultAddScore);
        }else{
            if(boardCards.get(index).size() ==5){
                int resultAddScore = 0 ;
                for (int i = 0; i < 5; i++) {
                    resultAddScore = resultAddScore + getTaurenCount(boardCards.get(index).get(i));
                }
                boardCards.get(index).clear();
                boardCards.get(index).add(card);
                player.setScore(player.getScore()+resultAddScore);
                boardCards.get(index).clear();
            }
            boardCards.get(index).add(card);
        }
        player.getCardList().remove(card);
        player.setCurrentSelectCard(null);
        player.setCurrentOfferCard(card);
    }

    public static int getPlaceLocation(GameContent content,Integer card){
        List<List<Integer>> boardCards = content.getBoardCards();
        int rowIndex = -1 ;
        for (int i = 0; i < boardCards.size(); i++) {
            List<Integer> row = boardCards.get(i);
            if(card>row.get(row.size()-1)){
                if(rowIndex == -1){
                    rowIndex = i;
                }else{
                    int diff = Math.abs(boardCards.get(rowIndex).get(boardCards.get(rowIndex).size()-1) - card);
                    int currentDiff = Math.abs(row.get(row.size()-1) - card);
                    if(currentDiff <diff){
                        rowIndex = i;
                    }
                    if(currentDiff == diff){
                        if(row.get(row.size()-1) < boardCards.get(rowIndex).get(boardCards.get(rowIndex).size()-1)){
                            rowIndex = i ;
                        }
                    }
                }
            }
        }
        return rowIndex;
    }

    public static void cpuSelected(GameContent content){
        List<Integer> cpuCardList = content.getCpu().getCardList();
        Random random = new Random();
        int index = random.nextInt(cpuCardList.size());
        content.getCpu().setCurrentSelectCard(cpuCardList.get(index));
    }
}
