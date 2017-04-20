### 每周简单自定义 View (2)-秒表控件

之前看文章无数, 但觉得这方面的进步还是比较慢, 
遂制定每周一个简单的自定义控件的决定 ( 这里只阐述思路 ) .   
废话不说看效果 (gif 有点不流畅凑合下):  
![](http://i.imgur.com/9GvJgd5.gif)   

<!-- more -->
### 分析
-	控件的外形主要由两部分组成
	-	外面的环
	-	里面的文字
-	对外的方法
	-	开始
	-	暂停
	-	重置

### 开始动手
由于本人目前能力有限 onMeasure() 我只是取了 MeasureSpec.EXACTLY 模式来获取控件的大小

#### 自定义控件继承 View 重写View的方法
1. 重写 onMeasure() 方法获取布局文件中控件定义的宽高,最后设置setMeasuredDimension(width, height);
2. 重写 onSizeChanged() 获取控件最终的大小
3. onDraw()	//绘制控件

#### 首先是静态控件的实现
-	外面的环  
这里使用了 canvas.rotate() 方法, 这个方法的意思是旋转画布,需要传三个参数角度 ( 360 度为一圈 ) , 旋转的中心点的 x 坐标,和 y 坐标. 有了这个方法我们只需在同一处画下刻度, 然后旋转画布就可以画出一个圆环.  
-	里面的文字  
由于他们是相当于一个整体居中, 在这里我们把它看成一个整体来绘制.  
1.首先求出它们的需要的宽和高  
2.所以他们整体的宽高: 宽为它们俩宽的和 . 高为它们俩的最大值

#### 添加动画
-	这里使用 ValueAnimator 
-	定义ValueAnimator
```
	valueAnimator = ValueAnimator.ofInt(0, 99);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(1000);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置无限循环
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    value = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    time = time + 1;//时间秒数
                    invalidate();
                }
            });
```

##### 绘制动态文本
-	然后根据 value 值绘制小的 text
-	根据 time 的值转化成 "00:00" 的格式 设置到大的 text 上面

##### 外环发亮效果
-	最前面的值为 value 依次向后的 4 个发亮,也就是value-1,value-2,value-3,value-4;
-	但是在 value 值小于 4 时, 发亮的光环衔接不上
-	这里我使用最暴力的方法
```
        int value1 = value - 1;
        int value2 = value - 2;
        int value3 = value - 3;
        int value4 = value - 4;
        if (time > 0) {
            if (value == 0) {
                value1 = 99;
                value2 = 98;
                value3 = 97;
                value4 = 96;
            } else if (value == 1) {
                value1 = 99;
                value2 = 98;
                value3 = 97;
                value4 = 0;
            } else if (value == 2) {
                value1 = 99;
                value2 = 98;
                value3 = 0;
                value4 = 1;
            } else if (value == 3) {
                value1 = 99;
                value2 = 0;
                value3 = 1;
                value4 = 2;
            }
        }
```
-	绘制圆环
```
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {//绘制大节点
                if (value == i || value1 == i || value2 == i || value3 == i || value4 == i) {
					//绘制发亮效果
                    ringNodePaint.setColor(ringRunningColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 10), ringNodePaint);
                } else {
					//绘制普通效果
                    ringNodePaint.setColor(ringNormalColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 10), ringNodePaint);
                }
            } else {//绘制小节点
                if (value == i || value1 == i || value2 == i || value3 == i || value4 == i) {
					//绘制发亮效果
                    ringNormalPaint.setColor(ringRunningColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 6), ringNormalPaint);
                } else {
					//绘制普通效果
                    ringNormalPaint.setColor(ringNormalColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 6), ringNormalPaint);
                }
            }
            canvas.rotate(360f / 100f, width / 2, height / 2);
        }
```
#### 添加对外的方法
-	开始计时
-	暂停计时
-	重置计时
### 其他

绘制文字时由于 1 的宽比较小,开始时候我是测量动态变化的数值,就使得里面的文字发生抖动, 所以后来就测量 "00:00" 和 "00" 固定值的宽高  

