# 1. CircleVolume

## 重要参数说明：

- unitCount 整个圈中小弧的数量
- unitSplit 每个弧的间距度数 must < 360/unit_count) 
- normalColor 普通颜色
- focusColor音量键大小Focus颜色 
- strokeWidth 小弧的宽度
- centerIcon 中心图片
- centerIconMargin 中心图片间距
- setVolume 设置volume，范围 0~unitCount

## 主要逻辑

1. setAttrs(AttributeSet attrs) 设置属性值
2. initView() 初始化paint
3. initSize()  初始化 size, radius等
4.  onDraw画弧和icon

    坐标线移到中心点，然后根据当前的位置画弧。图像画在中间
    
          @Override
            protected void onDraw(Canvas canvas) {
                // 0,0 move to (halfWidth, halfHeight)
                canvas.translate(halfWidth, halfHeight);
                // draw arcs
                drawArcs(canvas);
                // draw center image
                canvas.drawBitmap(centerIcon, null, iconRect, arcPaint);
            }
        
            private void drawArcs(Canvas canvas) {
                // draw normal arc
                arcPaint.setColor(normalColor);
                for (int i = mVolume; i < unitCount; i++) {
                    canvas.drawArc(arcRect, i * (unitSize + unitSplit) - 90, unitSize, false, arcPaint);
                }
                // draw focus arc
                arcPaint.setColor(focusColor);
                for (int i = 0; i < mVolume; i++) {
                    canvas.drawArc(arcRect, i * (unitSize + unitSplit) - 90, unitSize, false, arcPaint);
                }
            }

# 权限

无

# 项目

学习练手

# 作者

- Vivian Sun(孫壯麗_華碩杭州)

# 时间

2016年

