package Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    public static final double PROMPT_WIDTH  = 150;
    public static final double WIDTH = 1500;//1000
    public static final double HEIGHT = 900;//600
    public static final Color BACK_GROUND_COLOR = Color.rgb(182,212,186);
    public static final double START_X = 50;
    public static final double START_Y = 50;
    public static final double CARD_WIDTH = 90 ;//60
    public static final double CARD_HEIGHT = 120 ;//80
    public static final double OFFER_CARD_CONFIRM_BUTTON_WIDTH = 100 ;
    public static final double OFFER_CARD_CONFIRM_BUTTON_HEIGHT = 50 ;
    public static final double OFFER_CARD_CONFIRM_BUTTON_START_Y = 400 ;
    public static final int FPS = 50 ;
    public static final double CARD_INTERVAL = 5 ;
    public GameContent gameContent = new GameContent();
    public static final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    public static boolean isGameOver = false;
    @Override
    public void start(Stage primaryStage){
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isGameOver){
                    return;
                }
                double x = event.getX() ;
                double y = event.getY();
                //选牌
                if(y>= HEIGHT- START_Y - CARD_HEIGHT && y<= HEIGHT- START_Y && x >= START_X && x<= START_X +(CARD_WIDTH +CARD_INTERVAL)*10){
                    int index = (int)((x- START_X) / (CARD_WIDTH +CARD_INTERVAL));
                    GameControl.selectCard(index,gameContent);
                }
                //出牌
                if(y>= OFFER_CARD_CONFIRM_BUTTON_START_Y && y<= OFFER_CARD_CONFIRM_BUTTON_START_Y+OFFER_CARD_CONFIRM_BUTTON_HEIGHT && x >= (WIDTH-PROMPT_WIDTH-OFFER_CARD_CONFIRM_BUTTON_WIDTH)/2 && x<=(WIDTH-PROMPT_WIDTH-OFFER_CARD_CONFIRM_BUTTON_WIDTH)/2+OFFER_CARD_CONFIRM_BUTTON_WIDTH){
                    GameControl.offerCard(gameContent);
                    if(gameContent.getAllCards().size() <20 &&(gameContent.getCpu().getCardList().size() ==0 || gameContent.getPlayer().getCardList().size() ==0) ){
                        isGameOver = true;
                    }
                    if(gameContent.getPlayer().getScore() >= 66 || gameContent.getCpu().getScore()>= 66){
                        isGameOver = true;
                    }
                    if(!isGameOver){
                        if(gameContent.getCpu().getCardList().size() ==0 || gameContent.getPlayer().getCardList().size() ==0){
                            GameControl.licensing(gameContent);
                        }
                    }
                }
            }
        });
        AnchorPane root = new AnchorPane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("6 QUI PREND");
        primaryStage.setResizable(false);
        primaryStage.show();
        //发牌
        GameControl.licensing(gameContent);
    }


    @Override
    public void init() throws Exception {
        super.init();
        new Thread(() -> {
            while (true){
                draw();
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //绘制游戏内容
    private void draw() {
        //绘制背景
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(BACK_GROUND_COLOR);
        graphicsContext2D.fillRect(0,0, WIDTH, HEIGHT);
        //绘制显示边框
        graphicsContext2D.setStroke(Color.BLACK);
        graphicsContext2D.setLineWidth(1);
        graphicsContext2D.strokeRect(0,0,WIDTH-PROMPT_WIDTH,HEIGHT);
        //绘制剩余牌数
        drawRect(graphicsContext2D,WIDTH-PROMPT_WIDTH-100,(HEIGHT- CARD_HEIGHT)/2,"剩余牌量 :\n\n      "+gameContent.getAllCards().size(),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
        //绘制玩家手牌
        List<Integer> cardList = gameContent.getPlayer().getCardList();
        for (int i = 0; i < cardList.size(); i++) {
            Color color = Color.WHITE;
            if(cardList.get(i) == gameContent.getPlayer().getCurrentSelectCard()){
                color = Color.grayRgb(100);
            }
            drawRect(graphicsContext2D,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i,HEIGHT- START_Y - CARD_HEIGHT,"牌面值:"+cardList.get(i)+"\n\n牛头数:"+GameControl.getTaurenCount(cardList.get(i)),color, CARD_WIDTH, CARD_HEIGHT);
        }
        //绘制选牌按钮
        if(gameContent.getPlayer().getCurrentSelectCard() !=null){
            drawRect(graphicsContext2D,(WIDTH-PROMPT_WIDTH-OFFER_CARD_CONFIRM_BUTTON_WIDTH)/2,OFFER_CARD_CONFIRM_BUTTON_START_Y,"           确  定",Color.grayRgb(150),OFFER_CARD_CONFIRM_BUTTON_WIDTH,OFFER_CARD_CONFIRM_BUTTON_HEIGHT);
        }
        //绘制电脑手牌
        cardList = gameContent.getCpu().getCardList();
        for (int i = 0; i < cardList.size(); i++) {
            drawRect(graphicsContext2D,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i, START_Y,"",Color.GRAY,CARD_WIDTH,CARD_HEIGHT);
        }
        //绘制面板上的牌
        List<List<Integer>> boardCards = gameContent.getBoardCards();
        for (int i = 0; i < boardCards.size(); i++) {
            for (int j = 0; j < boardCards.get(i).size(); j++) {
                cardList = boardCards.get(i);
                drawRect(graphicsContext2D,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*j,START_Y + CARD_HEIGHT + START_Y + (START_Y+CARD_HEIGHT)*i,"牌面值:"+cardList.get(j)+"\n\n牛头数:"+GameControl.getTaurenCount(cardList.get(j)),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
            }
        }
        // 绘制实时得分
        graphicsContext2D.setFont(Font.font(15));
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillText(gameContent.getCpu().getName()+"得分:  " + gameContent.getCpu().getScore(),WIDTH-PROMPT_WIDTH+20,START_X);
        graphicsContext2D.fillText(gameContent.getPlayer().getName()+"得分:  "+ gameContent.getPlayer().getScore(),WIDTH-PROMPT_WIDTH+20,HEIGHT-START_X);
        //绘制上一张出的牌
        if(gameContent.getCpu().getCurrentOfferCard() != null){
            drawRect(graphicsContext2D,WIDTH-PROMPT_WIDTH+20,START_X+CARD_HEIGHT/2,"牌面值:"+gameContent.getCpu().getCurrentOfferCard()+"\n\n牛头数:"+GameControl.getTaurenCount(gameContent.getCpu().getCurrentOfferCard()),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
        }
        if(gameContent.getPlayer().getCurrentOfferCard()!=null){
            drawRect(graphicsContext2D,WIDTH-PROMPT_WIDTH+20,HEIGHT-START_X-CARD_HEIGHT/2-CARD_HEIGHT,"牌面值:"+gameContent.getPlayer().getCurrentOfferCard()+"\n\n牛头数:"+GameControl.getTaurenCount(gameContent.getPlayer().getCurrentOfferCard()),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
        }
        //绘制游戏结果
        if(isGameOver){
            if(gameContent.getPlayer().getScore() > gameContent.getCpu().getScore()){
                graphicsContext2D.fillText(gameContent.getCpu().getName()+"获胜",WIDTH-PROMPT_WIDTH+20,HEIGHT/2);

            }else if(gameContent.getPlayer().getScore() == gameContent.getCpu().getScore()){
                graphicsContext2D.fillText("平局",WIDTH-PROMPT_WIDTH+20,HEIGHT/2);
            }else{
                graphicsContext2D.fillText(gameContent.getPlayer().getName()+"获胜",WIDTH-PROMPT_WIDTH+20,HEIGHT/2);

            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void drawRect(GraphicsContext graphicsContext2D, double x, double y, String content, Color color, double width, double height) {
        graphicsContext2D.setFill(color);
        graphicsContext2D.fillRect(x,y, width, height);
        graphicsContext2D.setStroke(Color.BLACK);
        graphicsContext2D.setLineWidth(1);
        graphicsContext2D.strokeRect(x,y, width, height);
        graphicsContext2D.setFont(Font.font(10));
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillText(content,x+5,y+30);
    }
}



