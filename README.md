# 单Activity 页面View路由(Page)与事件路由(Action)  

## 1 支持功能   

### 1.1 页面View路由(Page)

1. 注解配置页面(下文统称Page)key和type类型，支持不同type对应相同key的多个类型页面。
2. 注解配置Page的View需要挂载的父View的id和index。
3. 创建Page时需要继承BasePage，通过泛型声明需要的数据类型和Presenter类型，使用MVP架构
4. Page泛型声明的数据类型为跳转到该Page时携带的数据的类型，数据不正确，跳转失败
5. 支持配置格式转换器，当提供的格式不是页面需要的数据格式，可通过配置格式转换器，转成对应类型。支持注解配置和代码配置。  
6. 支持页面队列和优先级配置，相同队列的VIew，必须等待上一个页面关闭后才可使用  
7. 支持拦截器功能，支持注解配置和代码配置
8. 切换type时，支持保存当前Page的状态，并恢复相同key不同type的Page现场。
9. 通过注解方式，自动生成获取Page对外提供管道接口的方法，用于跨Page交互。
10. PageA跳转PageB时，支持PageB通过setResult传递结果到PageA。
11. 跳转Page时除传递需要的数据格式外，还支持传递额外数据。
12. 当某个Page被打开时，再次调用，不会打开新的界面，只会调用onNewOpen接口。(后期可考虑增加启动模式)
13. 支持通过key获取Page。

### 1.2 事件路由  (Action)

1. 注解配置事件(下文统称Action)的key和type类型，支持不同type对应相同key的多个类型事件
2. 创建Action时需要实现BaseAction接口，通过泛型声明需要的数据类型，当数据格式不正确时，路由失败
3. 支持配置格式转换器，当提供的格式不是Action需要的数据格式，可通过配置格式转换器，转成对应类型。支持注解配置和代码配置。  
4. 支持Action队列和优先级配置，相同队列的Action，必须等待上一个Action处理完毕后才会继续处理  
5. 注解配置处理Action的线程(当前线程/工作线程/主线程/主线程异步)。

## 2 集成方式  
```groovy
   implementation project(path: ':PageApi')
   kapt project(':PageCompile')
```

## 3 初始化

```kotlin
//PageCreatorImpl 为自动生成的类，需要添加Page注解后，buid生成
PageConfig.init(activity, PageCreatorImpl())

//非必选
//设置类型工厂类，获取当前类型
PageConfig.setTypeFactory( object : TypeFactory {
            override fun getType(): Int {
                return type
            }
        })
//添加全局拦截器
PageConfig.addGlobalInterceptor(PageInterceptor { pageData, data, otherData ->
            Log.d(
                TAG,
                "addGlobalInterceptor-->pageData:$pageData   data:$data  otherData:$otherData"
            )
            false
        })
```

## 4 Page路由的使用

### 4.1 创建Page 

```kotlin
//要挂载的父View的id
@PageParent(android.R.id.content)
// 页面队列id和priority优先级，默认优先级为100，越大优先级越高。 不添加PageQueue注解即为非队列页面
@PageQueue(id = 11,priority = 101)
//页面路由规则，key，type数组，拦截器数组，数据转换类数组
@PageRule(
    "key",
    type = [1],
    interceptors = [TestInterceptor::class],
    transforms = [DataTransform::class, DataTransform2::class]
)
class DemoPage : BasePage<TestBean, TestPresenter>(), DemoView {
    private val TAG = "TestPage"

    override fun onCreate(data: TestBean?, otherData: Bundle?) {
        Log.d(TAG, "onCreate  data:$data  otherData:$otherData")
      	//设置view的布局，自动添加到父View中
        setContentView(R.layout.page_test)
 			  val txt = findViewById<TextView>(R.id.txt)
        txt.setOnClickListener {
          //打开新的Page，当开启的Page调用setResult()方法时，会调用setResultCallBack设置的回调接口
            val open =
                PageRouter.create(mContext, "key2", "TEST2新来的").setResultCallBack { result, data ->
                    Log.d(TAG, "setResultCallBack  result:$result  data:$data")
                }.open()
            Log.d(TAG, "open Result:$open")
//            关闭当前界面
//            finish()

        }
    }
		//类型改变时，可以通过otherData保存当前状态，在onCreate的otherData可以接收到该参数
    override fun onTypeChanged(otherData: Bundle): Boolean {
        otherData.putString("typeChanged", "咯");
        return true
    }
		//是否拦截返回按键，true拦截
    override fun onBackPressed(): Boolean {
        Log.d(TAG, "onBackPressed")
        return super.onBackPressed()
    }
		//当页面再次被打开时，不创建新的Page，只调用该方法
    override fun onNewOpen(data: TestBean?, otherData: Bundle?) {
        super.onNewOpen(data, otherData)
    }
		//页面销毁
    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
    }

		//DemoView 接口定义的方法，用于在Presenter中调用View
    override fun loginSuccess() {
        Log.d(TAG, "loginSuccess")
    }
		
  	//自动在PagePipeManager类中生成public static TestPipe getTestNamePipe()方法，获取page对外暴露的管道接口
    @PagePipe("getTestNamePipe")
    override fun getPipe(): TestPipe {
        return object : TestPipe {
            override fun getName(): Int {
                return 1111111
            }

        }
    }
}
```

### 4.3 创建TestPresenter和DemoView类

```kotlin
class TestPresenter : BasePresenter<TestBean, DemoView>() {
    fun toLogin() {
        Log.d("TestPresenter", "toLogin")
        mView.loginSuccess()
    }
}

interface DemoView : IView<TestBean> {
    fun loginSuccess()
}
```

## 5 Action路由的使用

### 5.1 创建Action路由

```java
//Action处理队列，不配置，默认无队列。 队列id和优先级priority(默认100)
@PageQueue(id = 11,priority=101)
//Action路由 key,type数组，transforms数组
@ActionRule(value = "key", type = 1,transforms = DataTransform.class)
//必须继承BaseAction类
public class TestAction implements BaseAction<String> {
    private final static String TAG = "TestAction";
		//处理Action的方法，ActionThread注解处理该方法的线程，不配置则在发送线程。（workThread/mainThread/mainThreadAsync/defaultThread)
    @ActionThread(ActionThread.Thread.workThread)
    @Override
    public void handleEvent(String data) {
        Log.d(TAG, "handleEvent   -->" + data + "---" + Thread.currentThread().getName());
    }
}
```

### 5.2 Action路由调用

```kotlin
val open = PageRouter.createAction("key", "事件咯").open()
Log.d(TAG, "openAction Result:$open")
```

