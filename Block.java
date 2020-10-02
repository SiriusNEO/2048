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


public class Block extends Rectangle {
	public int value,x,y,tmp;
	public Label number = new Label();
	
	public final int sceneWidth=650,sceneHeight=650;
	public final int initX=100,initY=80,everyBlock=120;
	public final Color[] valueColor = {null,Color.PURPLE,Color.BLUEVIOLET,Color.BLUE,Color.DARKCYAN,Color.GREEN,Color.YELLOWGREEN,Color.GOLD,Color.ORANGE,Color.ORANGERED,Color.RED,Color.DEEPPINK};
	public final int[] valueFontSize = {0,50,45,40,33};
	public final int[] numberPositionX= {0,38,23,13,8},numberPositionY= {0,17,19,21,24};
	
	public Block(int x,int y) {
		moveTo(x,y);
		this.setWidth(105);
		this.setHeight(105);
		this.number.setTextFill(Color.WHITE);
	}
	
	public int log2(int number) {
		int cnt=0;
		for (int i=1;i<number;i*=2) ++cnt;
		return cnt;
	}
	
	public int getDigit(int number) {
		if(number < 10) return 1;
		else if(number < 100) return 2;
		else if(number < 1000) return 3;
		else return 4;
	}
	
	public void moveTo(int x,int y) {
		this.x = x;
		this.y = y;
		this.number.setLayoutX(initX+everyBlock*(x-1)+numberPositionX[this.getDigit(this.value)]);
		this.number.setLayoutY(initY+everyBlock*(y-1)+numberPositionY[this.getDigit(this.value)]);
		this.setLayoutX(initX+everyBlock*(x-1));
		this.setLayoutY(initY+everyBlock*(y-1));
	}
	
	public void updateValue(int v) {
		this.value = v;
		this.number.setText(String.valueOf(v));
		this.number.setLayoutX(initX+everyBlock*(x-1)+numberPositionX[this.getDigit(v)]);
		this.number.setLayoutY(initY+everyBlock*(y-1)+numberPositionY[this.getDigit(v)]);
		this.number.setFont(Font.font("Arial Black",valueFontSize[getDigit(v)]));
		this.setFill(valueColor[log2(v)]);
	}
}
