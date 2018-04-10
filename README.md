# WheelView
用ListView实现的滚轮视图。关于其原理解析请参见下方我的博客地址链接。

>[ListView实现类似WheelView效果的探究](https://blog.csdn.net/yankebin/article/details/51580683)

**因为博客发布时间比较久，可能部分代码会有所出入，以github上代码为准。**

当前demo里效果如下图所示

![]()


# 使用注意事项

1.要想实现demo里的效果时，在布局floatView的高度时，要注意与item的高度差距大不大，比如我就吧floatView的高度和item的高度设置成了一样高。

2.