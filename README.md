# 一、WheelView简介 

用ListView实现的滚轮视图。关于其原理解析请参见下方我的博客地址链接。

>[ListView实现类似WheelView效果的探究](https://blog.csdn.net/yankebin/article/details/51580683)

**因为博客发布时间比较久，可能部分代码会有所出入，以github上代码为准。**

当前demo里效果如下图所示

![](https://raw.githubusercontent.com/ykbjson/WheelView/master/wheelview.gif)




# 二、如何引用

maven

'

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>




	<dependency>
	    <groupId>com.github.ykbjson</groupId>
	    <artifactId>WheelView</artifactId>
	    <version>v1.0.0</version>
	</dependency>


'


gradle

首先在你的项目根目录的build.gradle里加入

'

	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}



'

然后在app目录的build.gradle里加入

'

	dependencies {
		        compile 'com.github.ykbjson:WheelView:v1.0.0'
		}


'


# 三、使用注意事项


3.1 要想实现demo里的效果时，在布局floatView的高度时，要注意与item的高度差距大不大，比如我就吧floatView的高度和item的高度设置成了一样高。


3.2 关于WheelListView里面的setUp方法里

'

	/**
	     * 设置相关属性
	     *
	     * @param floatView     悬浮视图
	     * @param topPadding    WheelAdapter的item里要显示在悬浮框内的区域的顶部的padding高度 px
	     * @param bottomPadding  WheelAdapter的item里要显示在悬浮框内的区域的底部的padding高度 px
	     */
	    protected void setUp(View floatView, int topPadding, int bottomPadding) {
	        if (null == floatView ) {
	            throw new NullPointerException("floatView or rootView can not be null");
	        }
	        //让listview的显示区域高度和悬浮框的的高度一样。topPadding和bottomPadding用来增大或减小listView			 //的显示区域
	        setPadding(0, floatView.getTop() - topPadding, 0,
	                getBottom() - floatView.getBottom() - bottomPadding);
	        //获取悬浮框在屏幕的绝对位置
	        int location[] = new int[2];
	        floatView.getLocationOnScreen(location);
	
	        topY = location[1];
	        middleY = topY + floatView.getMeasuredHeight() / 2;
	        bottomY = topY + floatView.getMeasuredHeight();
	
	        setUpScroll();
	    }
	    

'

topPadding和bottomPadding，这里要理解到这两个值实际的意义后再去使用。我的demo里是为了实现那样的效果，所以控制了item的高度和floatview的高度，所以这两个值传0也不影响。如果你想实现的效果里item高度严重不统一，且与floatview高度差距过大，可能本库就不太适合做你需要的效果。当然，你也可以下载源码后自行改造。

