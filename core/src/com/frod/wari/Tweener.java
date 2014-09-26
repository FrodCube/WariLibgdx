package com.frod.wari;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Tweener implements TweenAccessor<Sprite> {

	public static final int POSITION = 0;
	public static final int ROTATION = 1;
	public static final int SCALE = 2;
	public static final int ALPHA = 3;

	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
			case POSITION:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
			case ROTATION:
				returnValues[0] = target.getRotation();
				return 1;
			case SCALE:
				returnValues[0] = target.getScaleX();
				returnValues[1] = target.getScaleY();
				return 2;
			case ALPHA:
				returnValues[0] = target.getColor().a;
				return 1;
		}
		return 0;
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {
			case POSITION:
				target.setPosition(newValues[0], newValues[1]);
				break;
			case ROTATION:
				target.setRotation(newValues[0]);
				break;
			case SCALE:
				target.setScale(newValues[0], newValues[1]);
				break;
			case ALPHA:
				Color c = target.getColor();
				target.setColor(c.r, c.g, c.b, newValues[0]);
				break;
		}
	}
}
