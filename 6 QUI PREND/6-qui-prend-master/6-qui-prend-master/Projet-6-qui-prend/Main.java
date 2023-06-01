package game.tauren;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

import java.util.List;

public class Main extends Application {

    public static final double START_GAME_BUTTON_WIDTH = 200 ;
    public static final double START_GAME_BUTTON_HEIGHT = 50 ;

    public static final double PROMPT_WIDTH  = 150;
    public static final double WIDTH = 1000;
    public static final double HEIGHT = 700;
    public static final Color BACK_GROUND_COLOR = Color.rgb(182,212,186);
    public static final double START_X = 50;
    public static final double START_Y = 50;
    public static final double CARD_WIDTH = 60 ;
    public static final double CARD_HEIGHT = 80 ;
    public static final double OFFER_CARD_CONFIRM_BUTTON_WIDTH = 100 ;
    public static final double OFFER_CARD_CONFIRM_BUTTON_HEIGHT = 50 ;
    public static final double OFFER_CARD_CONFIRM_BUTTON_START_Y = 450 ;
    public static final int FPS = 50 ;
    public static final double CARD_INTERVAL = 5 ;
    public GameContent gameContent = new GameContent();
    public static final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    public static boolean isGameOver = false;
    public static boolean isStartGame = false;

    public static MediaPlayer mediaPlayer;
    @Override
    public void start(Stage primaryStage){

        // Ajoutez le code pour lire la musique
        String musicFile = "C:\\Users\\xzlle\\Desktop\\tauren\\src\\game\\tauren\\music\\Hearthstone (2014) - Main Title.mp3/";
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);

        // Autres configurations du lecteur audio (volume, boucle, etc.)
        mediaPlayer.setVolume(0.5); // Volume à 50%
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lecture en boucle

        // Lecture de la musique
        mediaPlayer.play();

        // Arrêt de la musique
        //mediaPlayer.stop();
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
                if(!isStartGame){
                    if(y>=(HEIGHT-START_GAME_BUTTON_HEIGHT)/2 && y<=(HEIGHT-START_GAME_BUTTON_HEIGHT)/2+START_GAME_BUTTON_HEIGHT && x>= (WIDTH-START_GAME_BUTTON_WIDTH)/2 && x<= (WIDTH-START_GAME_BUTTON_WIDTH)/2+START_GAME_BUTTON_WIDTH){
                        isStartGame = true;
                    }
                    return;
                }
                //选牌
                //获取选择牌的位置
                int index = -1;
                if(gameContent.getPlayer().getCurrentSelectCard() != null){
                    for (int i = 0; i < gameContent.getPlayer().getCardList().size(); i++) {
                        if(gameContent.getPlayer().getCardList().get(i) == gameContent.getPlayer().getCurrentSelectCard()){
                            index = i;
                            break;
                        }
                    }
                }
                if(index != -1 && y>= HEIGHT- START_Y - CARD_HEIGHT-20 && y<= HEIGHT- START_Y-20 && x >= START_X+(CARD_WIDTH +CARD_INTERVAL)*index && x<= START_X +(CARD_WIDTH +CARD_INTERVAL)*(index+1)){
                    GameControl.selectCard(index,gameContent);
                }else if(y>= HEIGHT- START_Y - CARD_HEIGHT && y<= HEIGHT- START_Y && x >= START_X && x<= START_X +(CARD_WIDTH +CARD_INTERVAL)*10 && ((int)((x- START_X) / (CARD_WIDTH +CARD_INTERVAL)))!=index){
                    index = (int)((x- START_X) / (CARD_WIDTH +CARD_INTERVAL));
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
        primaryStage.setTitle("tauren");
        primaryStage.setResizable(false);
        primaryStage.show();
        //发牌
        GameControl.licensing(gameContent);
    }
    @Override
    public void stop() throws Exception {
        super.stop();

        // Arrête la musique lorsque l'application se ferme
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
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
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();


        if(!isStartGame){
            //绘制说明
            graphicsContext2D.drawImage(GameContent.INTRODUCTION_IMAGE,0,0,WIDTH,HEIGHT);
            //绘制开始游戏按钮
            drawRect(graphicsContext2D,(WIDTH-START_GAME_BUTTON_WIDTH)/2,(HEIGHT-START_GAME_BUTTON_HEIGHT)/2,"",Color.WHITESMOKE,START_GAME_BUTTON_WIDTH,START_GAME_BUTTON_HEIGHT);
            graphicsContext2D.setFont(Font.font(20));
            graphicsContext2D.setFill(Color.BLACK);
            graphicsContext2D.fillText("start game",(WIDTH-START_GAME_BUTTON_WIDTH)/2+50,(HEIGHT-START_GAME_BUTTON_HEIGHT)/2+32);
        }else{
            //绘制背景
            graphicsContext2D.drawImage(GameContent.BACKGROUND_IMAGE,0,0,WIDTH,HEIGHT);
/*          graphicsContext2D.setFill(BACK_GROUND_COLOR);
            graphicsContext2D.fillRect(0,0, WIDTH, HEIGHT);*/
            //绘制显示边框
            graphicsContext2D.setStroke(Color.BLACK);
            graphicsContext2D.setLineWidth(1);
            graphicsContext2D.strokeRect(0,0,WIDTH-PROMPT_WIDTH,HEIGHT);
            //绘制剩余牌数
//        drawRect(graphicsContext2D,WIDTH-PROMPT_WIDTH-100,(HEIGHT- CARD_HEIGHT)/2,"剩余牌量 :\n\n      "+gameContent.getAllCards().size(),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
            graphicsContext2D.drawImage(GameContent.CARD_BACKGROUND_IMAGE,WIDTH-PROMPT_WIDTH-100,(HEIGHT- CARD_HEIGHT)/2,CARD_WIDTH,CARD_HEIGHT);
            graphicsContext2D.setFont(Font.font(30));
            graphicsContext2D.setFill(Color.BLACK);
            graphicsContext2D.fillText(gameContent.getAllCards().size()+"",WIDTH-PROMPT_WIDTH-85,(HEIGHT- CARD_HEIGHT)/2+50);
            //绘制玩家手牌
            List<Integer> cardList = gameContent.getPlayer().getCardList();
            for (int i = 0; i < cardList.size(); i++) {
                Color color = Color.WHITE;
                boolean flag = false;
                if(cardList.get(i) == gameContent.getPlayer().getCurrentSelectCard()){
                    color = Color.grayRgb(100);
                    flag = true;
                }
                if(gameContent.getAllCardImage().get(cardList.get(i)) != null){
                    if(flag){
                        graphicsContext2D.drawImage(gameContent.getAllCardImage().get(cardList.get(i)),(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i,HEIGHT- START_Y - CARD_HEIGHT-20, CARD_WIDTH, CARD_HEIGHT);
                    }else{
                        graphicsContext2D.drawImage(gameContent.getAllCardImage().get(cardList.get(i)),(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i,HEIGHT- START_Y - CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);
                    }
                }else{
                    drawRect(graphicsContext2D,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i,HEIGHT- START_Y - CARD_HEIGHT,"牌面值:"+cardList.get(i)+"\n\n牛头数:"+GameControl.getTaurenCount(cardList.get(i)),color, CARD_WIDTH, CARD_HEIGHT);
                }        }
            //绘制选牌按钮
            if(gameContent.getPlayer().getCurrentSelectCard() !=null){
                drawRect(graphicsContext2D,(WIDTH-PROMPT_WIDTH-OFFER_CARD_CONFIRM_BUTTON_WIDTH)/2,OFFER_CARD_CONFIRM_BUTTON_START_Y,"confirm",Color.grayRgb(150),OFFER_CARD_CONFIRM_BUTTON_WIDTH,OFFER_CARD_CONFIRM_BUTTON_HEIGHT);
            }
            //绘制电脑手牌
            cardList = gameContent.getCpu().getCardList();
            for (int i = 0; i < cardList.size(); i++) {
            /*if(gameContent.getAllCardImage().get(cardList.get(i)) != null){
                graphicsContext2D.drawImage(gameContent.getAllCardImage().get(cardList.get(i)),(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i, START_Y, CARD_WIDTH, CARD_HEIGHT);
            }else{
                drawRect(graphicsContext2D,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i, START_Y,"",Color.GRAY,CARD_WIDTH,CARD_HEIGHT);
            }*/
                graphicsContext2D.drawImage(GameContent.CARD_BACKGROUND_IMAGE,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*i, START_Y, CARD_WIDTH, CARD_HEIGHT);
            }
            //绘制面板上的牌
            List<List<Integer>> boardCards = gameContent.getBoardCards();
            for (int i = 0; i < boardCards.size(); i++) {
                for (int j = 0; j < boardCards.get(i).size(); j++) {
                    cardList = boardCards.get(i);
                    if(gameContent.getAllCardImage().get(cardList.get(j)) != null){
                        graphicsContext2D.drawImage(gameContent.getAllCardImage().get(cardList.get(j)),(START_X)+(CARD_WIDTH +CARD_INTERVAL)*j,START_Y + CARD_HEIGHT + 20 + (20+CARD_HEIGHT)*i, CARD_WIDTH, CARD_HEIGHT);
                    }else{
                        drawRect(graphicsContext2D,(START_X)+(CARD_WIDTH +CARD_INTERVAL)*j,START_Y + CARD_HEIGHT + START_Y + (START_Y+CARD_HEIGHT)*i,"point:"+cardList.get(j)+"\n\nnum:"+GameControl.getTaurenCount(cardList.get(j)),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
                    }
                }
            }
            // 绘制实时得分
            graphicsContext2D.setFont(Font.font(15));
            graphicsContext2D.setFill(Color.BLACK);
            graphicsContext2D.fillText(gameContent.getCpu().getName()+"points:  " + gameContent.getCpu().getScore(),WIDTH-PROMPT_WIDTH+20,START_X);
            graphicsContext2D.fillText(gameContent.getPlayer().getName()+"points:  "+ gameContent.getPlayer().getScore(),WIDTH-PROMPT_WIDTH+20,HEIGHT-START_X);
            //绘制上一张出的牌
            if(gameContent.getCpu().getCurrentOfferCard() != null){
                if(gameContent.getAllCardImage().get(gameContent.getCpu().getCurrentOfferCard()) != null){
                    graphicsContext2D.drawImage(gameContent.getAllCardImage().get(gameContent.getCpu().getCurrentOfferCard()),WIDTH-PROMPT_WIDTH+20,START_X+CARD_HEIGHT/2, CARD_WIDTH, CARD_HEIGHT);
                }else{
                    drawRect(graphicsContext2D,WIDTH-PROMPT_WIDTH+20,START_X+CARD_HEIGHT/2,"num:"+gameContent.getCpu().getCurrentOfferCard()+"\n\n牛头数:"+GameControl.getTaurenCount(gameContent.getCpu().getCurrentOfferCard()),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
                }
            }
            if(gameContent.getPlayer().getCurrentOfferCard()!=null){
                if(gameContent.getAllCardImage().get(gameContent.getPlayer().getCurrentOfferCard()) != null){
                    graphicsContext2D.drawImage(gameContent.getAllCardImage().get(gameContent.getPlayer().getCurrentOfferCard()),WIDTH-PROMPT_WIDTH+20,HEIGHT-START_X-CARD_HEIGHT/2-CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);
                }else{
                    drawRect(graphicsContext2D,WIDTH-PROMPT_WIDTH+20,HEIGHT-START_X-CARD_HEIGHT/2-CARD_HEIGHT,"num:"+gameContent.getPlayer().getCurrentOfferCard()+"\n\n牛头数:"+GameControl.getTaurenCount(gameContent.getPlayer().getCurrentOfferCard()),Color.WHITE, CARD_WIDTH, CARD_HEIGHT);
                }
            }
            //绘制游戏结果
            if(isGameOver){
                if(gameContent.getPlayer().getScore() > gameContent.getCpu().getScore()){
                    graphicsContext2D.fillText(gameContent.getCpu().getName()+"win",WIDTH-PROMPT_WIDTH+20,HEIGHT/2);

                }else if(gameContent.getPlayer().getScore() == gameContent.getCpu().getScore()){
                    graphicsContext2D.fillText("tie",WIDTH-PROMPT_WIDTH+20,HEIGHT/2);
                }else{
                    graphicsContext2D.fillText(gameContent.getPlayer().getName()+"win",WIDTH-PROMPT_WIDTH+20,HEIGHT/2);
                }
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
