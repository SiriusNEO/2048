package application;
	
import java.util.*;
import java.math.*;

import javafx.application.Platform;
import javafx.application.Application;

import javafx.event.*;
import javafx.stage.*;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;
import javafx.scene.effect.*;
import javafx.scene.text.*;

public class Main extends Application {
	
	public final int sceneWidth=650,sceneHeight=650;
	public final int initX=100,initY=80,everyBlock=120;
	public final Color[] valueColor = {null,Color.PURPLE,Color.BLUEVIOLET,Color.BLUE,Color.DARKCYAN,Color.GREEN,Color.YELLOWGREEN,Color.GOLD,Color.ORANGE,Color.ORANGERED,Color.RED,Color.DEEPPINK};
	public final int[] valueFontSize = {0,50,45,40,33};
	public final int[] numberPositionX= {0,38,23,13,8},numberPositionY= {0,17,19,21,24};
	
	public int[][] map = new int[8][8];
	public int blockCnt=0,initCnt=6,score=0;
	public int[] flag = new int[8];
	
	Pane root = new Pane();
	Scene scene = new Scene(root,sceneWidth,sceneHeight);
	
	Timer timer = new Timer();
	
	Rectangle background = new Rectangle();
	List<Rectangle> backBlock = new ArrayList<>();
	List<Block> block = new ArrayList<>();
	
	Label scoreBoard = new Label();
	
	public int getDigit(int number) {
		if(number < 10) return 1;
		else if(number < 100) return 2;
		else if(number < 1000) return 3;
		else return 4;
	}
	
	public void move(int direction,int toPosition,int index) {
		switch (direction) {
			case 1:{
				block.get(index).tmp = initY + everyBlock*(block.get(index).y-1);
				map[block.get(index).x][block.get(index).y] = 0;
				timer.schedule(new TimerTask() {
						public void run() {
							block.get(index).setLayoutY(block.get(index).tmp);
							block.get(index).number.setLayoutY(block.get(index).tmp+numberPositionY[getDigit(block.get(index).value)]);
							if(block.get(index).tmp <= initY+everyBlock*(toPosition-1)) {
								flag[direction]++;
								this.cancel();
							}
							block.get(index).tmp -= 20;
						}
					}, 0, 15);
				block.get(index).y = toPosition;
				map[block.get(index).x][block.get(index).y] = index;
				break;
			}
			case 2:{
				block.get(index).tmp = initX + everyBlock*(block.get(index).x-1);
				map[block.get(index).x][block.get(index).y] = 0;
				timer.schedule(new TimerTask() {
						public void run() {
							block.get(index).setLayoutX(block.get(index).tmp);
							block.get(index).number.setLayoutX(block.get(index).tmp+numberPositionX[getDigit(block.get(index).value)]);
							if(block.get(index).tmp <= initX+everyBlock*(toPosition-1)) {
								flag[direction]++;
								this.cancel();
							}
							block.get(index).tmp -= 20;
						}
				}, 0, 15);
				block.get(index).x = toPosition;
				map[block.get(index).x][block.get(index).y] = index;
				break;
			}
			case 3:{
				block.get(index).tmp = initY + everyBlock*(block.get(index).y-1);
				map[block.get(index).x][block.get(index).y] = 0;
				timer.schedule(new TimerTask() {
						public void run() {
							block.get(index).setLayoutY(block.get(index).tmp);
							block.get(index).number.setLayoutY(block.get(index).tmp+numberPositionY[getDigit(block.get(index).value)]);
							if(block.get(index).tmp >= initY+everyBlock*(toPosition-1)) {
								flag[direction]++;
								this.cancel();
							}
							block.get(index).tmp += 20;
						}
					}, 0, 15);
				block.get(index).y = toPosition;
				map[block.get(index).x][block.get(index).y] = index;
				break;
			}
			case 4:{
				block.get(index).tmp = initX + everyBlock*(block.get(index).x-1);
				map[block.get(index).x][block.get(index).y] = 0;
				timer.schedule(new TimerTask() {
						public void run() {
							block.get(index).setLayoutX(block.get(index).tmp);
							block.get(index).number.setLayoutX(block.get(index).tmp+numberPositionX[getDigit(block.get(index).value)]);
							if(block.get(index).tmp >= initX+everyBlock*(toPosition-1)) {
								flag[direction]++;
								this.cancel();
							}
							block.get(index).tmp += 20;
						}
				}, 0, 15);
				block.get(index).x = toPosition;
				map[block.get(index).x][block.get(index).y] = index;
				break;
			}
		}
	}
	
	public void addBlock(int x,int y) {
		++blockCnt;
		block.add(new Block(x,y));
		root.getChildren().add(block.get(blockCnt));
		map[x][y] = blockCnt;
		block.get(blockCnt).updateValue((Math.random()<0.9) ? 2:4);
		block.get(blockCnt).number.setText(String.valueOf(block.get(blockCnt).value));
		root.getChildren().add(block.get(blockCnt).number);
	}
	
	public void randomAdd() {
		int rndX=(int)Math.floor((Math.random()*4)+1),rndY=(int)Math.floor((Math.random()*4)+1);
		while(map[rndX][rndY]!=0) {
			rndX=(int)Math.floor((Math.random()*4)+1);
			rndY=(int)Math.floor((Math.random()*4)+1);
		}
		addBlock(rndX,rndY);
	}
	
	public void removeBlock(int x,int y) {
		int index=map[x][y];
		map[x][y]=0;
		root.getChildren().remove(block.get(index));
		root.getChildren().remove(block.get(index).number);
		block.remove(index);
		--blockCnt;
		for(int i=1;i<=blockCnt;++i)
			map[block.get(i).x][block.get(i).y]=i;
	}
	
	@Override
	public void start(Stage primaryStage) {
		background.setLayoutX(initX-10);
		background.setLayoutY(initY-10);
		background.setWidth(initX+3*everyBlock+25);
		background.setHeight(initX+3*everyBlock+25);
		background.setFill(Color.WHITE);
		root.getChildren().add(background);
		
		scoreBoard.setLayoutX(initX+everyBlock/2);
		scoreBoard.setLayoutY(initY+everyBlock*4);
		scoreBoard.setTextFill(Color.WHITE);
		scoreBoard.setFont(Font.font("Arial Black",40));
		scoreBoard.setText("Score£º"+String.valueOf(score));
		root.getChildren().add(scoreBoard);
		
		for (int i=1;i<=4;++i)
			for (int j=1;j<=4;++j)
				map[i][j] = 0;
		for (int i=0;i<=5;++i) map[i][0]=map[i][5]=map[0][i]=map[5][i]=17;
		
		for (int i=1;i<=16;++i) {
			Rectangle tmp = new Rectangle();
			backBlock.add(tmp);
		}
			
		for (int i=0;i<4;++i)
			for (int j=0;j<4;++j) {
				backBlock.get(j*4+i).setLayoutX(initX+everyBlock*i);
				backBlock.get(j*4+i).setLayoutY(initY+everyBlock*j);
				backBlock.get(j*4+i).setFill(Color.SILVER);
				backBlock.get(j*4+i).setWidth(105);
				backBlock.get(j*4+i).setHeight(105);
				root.getChildren().add(backBlock.get(j*4+i));
			}
		
		block.add(null);
		
		for (int i=1;i<=initCnt;++i) randomAdd();
		
		scene.setOnKeyPressed(e->{
			if(flag[1] == 0 && flag[2] == 0 && flag[3] == 0 && flag[4] == 0) {
					switch(e.getCode()) {
					case UP:{
						for (int i=1;i<=4;++i)
							for (int j=1;j<=4;++j) {
								if(map[j][i]==0) continue;
								int yy=i-1;
								while(map[j][yy]==0)--yy;
								++yy;
								move(1,yy,map[j][i]);
							}
						break;
					}
					case DOWN:{
						for (int i=4;i>=1;--i)
							for (int j=1;j<=4;++j) {
								if(map[j][i]==0) continue;
								int yy=i+1;
								while(map[j][yy]==0)++yy;
								--yy;
								move(3,yy,map[j][i]);
							}
						break;
					}
					case LEFT:{
						for (int i=1;i<=4;++i)
							for (int j=1;j<=4;++j) {
								if(map[i][j]==0) continue;
								int xx=i-1;
								while(map[xx][j]==0)--xx;
								++xx;
								move(2,xx,map[i][j]);
							}
						break;
					}
					case RIGHT:{
						for (int i=4;i>=1;--i)
							for (int j=1;j<=4;++j) {
								if(map[i][j]==0) continue;
								int xx=i+1;
								while(map[xx][j]==0)++xx;
								--xx;
								move(4,xx,map[i][j]);
							}
						break;
					}
				}
			}
		});
		
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if(flag[1] == blockCnt || flag[2] == blockCnt || flag[3] == blockCnt || flag[4] == blockCnt) {
							if(flag[1] == blockCnt) {
									for (int i=2;i<=4;++i)
										for (int j=1;j<=4;++j) {
											if(map[j][i]==0) continue;
											if(map[j][i-1]>16||map[j][i-1]<=0) continue;
											if(block.get(map[j][i]).value!=block.get(map[j][i-1]).value) continue;
											score+=block.get(map[j][i-1]).value;
											block.get(map[j][i-1]).updateValue(block.get(map[j][i-1]).value<<1);
											removeBlock(j,i);
										}
									for (int i=2;i<=4;++i)
										for (int j=1;j<=4;++j) 
											if(map[j][i-1]==0&&map[j][i]!=0) {
												map[j][i-1]=map[j][i];
												map[j][i]=0;
												block.get(map[j][i-1]).moveTo(j, i-1);
										}
								}
							else if(flag[2] == blockCnt) {
								for (int i=2;i<=4;++i)
									for (int j=1;j<=4;++j) {
										if(map[i][j]==0) continue;
										if(map[i-1][j]>16||map[i-1][j]<=0) continue;
										if(block.get(map[i][j]).value!=block.get(map[i-1][j]).value) continue;
										score+=block.get(map[i-1][j]).value;
										block.get(map[i-1][j]).updateValue(block.get(map[i-1][j]).value<<1);
										removeBlock(i,j);
									}
								for (int i=2;i<=4;++i)
									for (int j=1;j<=4;++j) 
										if(map[i-1][j]==0&&map[i][j]!=0) {
											map[i-1][j]=map[i][j];
											map[i][j]=0;
											block.get(map[i-1][j]).moveTo(i-1, j);
									}
							}
							else if(flag[3] == blockCnt) {
								for (int i=3;i>=1;--i)
									for (int j=1;j<=4;++j) {
										if(map[j][i]==0) continue;
										if(map[j][i+1]>16||map[j][i+1]<=0) continue;
										if(block.get(map[j][i]).value!=block.get(map[j][i+1]).value) continue;
										score+=block.get(map[j][i+1]).value;
										block.get(map[j][i+1]).updateValue(block.get(map[j][i+1]).value<<1);
										removeBlock(j,i);
									}
								for (int i=3;i>=1;--i)
									for (int j=1;j<=4;++j) 
										if(map[j][i+1]==0&&map[j][i]!=0) {
											map[j][i+1]=map[j][i];
											map[j][i]=0;
											block.get(map[j][i+1]).moveTo(j, i+1);
									}
							}
							else if(flag[4] == blockCnt) {
								for (int i=3;i>=1;--i)
									for (int j=1;j<=4;++j) {
										if(map[i][j]==0) continue;
										if(map[i+1][j]>16||map[i+1][j]<=0) continue;
										if(block.get(map[i][j]).value!=block.get(map[i+1][j]).value) continue;
										score+=block.get(map[i+1][j]).value;
										block.get(map[i+1][j]).updateValue(block.get(map[i+1][j]).value<<1);
										removeBlock(i,j);
									}
								for (int i=3;i>=1;--i)
									for (int j=1;j<=4;++j) 
										if(map[i+1][j]==0&&map[i][j]!=0) {
											map[i+1][j]=map[i][j];
											map[i][j]=0;
											block.get(map[i+1][j]).moveTo(i+1, j);
									}
							}
							scoreBoard.setText("Score£º"+String.valueOf(score));
							flag[1] = flag[2] = flag[3] = flag[4] = 0;
							randomAdd();
						}
					}
				});
			}
		}, 0, 200);

		root.setStyle("-fx-background-color:lightblue");
		primaryStage.setScene(scene);
		primaryStage.setTitle("2048");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
