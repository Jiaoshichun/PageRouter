package com.yiqizuoye.library.page.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 * 页面队列处理
 * 默认队列关闭，只有配置{@link com.yiqizuoye.library.page.annotation.PageQueue}是才开启
 * 相同队列，如果上一个未完成，则等待。 等待队列根据优先级排队
 */
class PageQueueManager {
    private static class RouterQueueData {
        public PageQueueData queue;
        public RouterData routerData;

        public RouterQueueData(PageQueueData queue, RouterData routerData) {
            this.queue = queue;
            this.routerData = routerData;
        }
    }

    //页面等待队列
    private final static Map<Integer, List<RouterQueueData>> queueWaitPageData = new HashMap<>();
    //正在展示的队列
    private final static Map<Integer, Boolean> isShowingQueue = new HashMap<>();
    private final static Comparator<RouterQueueData> comparator = new Comparator<RouterQueueData>() {
        @Override
        public int compare(RouterQueueData o1, RouterQueueData o2) {
            return o2.queue.getPriority() - o1.queue.getPriority();
        }
    };

    /**
     * 处理页面跳转，准备处理页面数据时，调用该方法
     */
    public synchronized static boolean processPageData(PageQueueData queue, RouterData routerData) {
        //如果当前的页面不是队列页面 或者被执行过
        if (!queue.isQueue() || routerData.isExecuted) return false;

        //如果当前队列没有正在展示的页面 不作处理，保存状态
        if (isShowingQueue.get(queue.getId()) == null || !isShowingQueue.get(queue.getId())) {
            routerData.isExecuted = true;
            isShowingQueue.put(queue.getId(), true);
            return false;
        }

        List<RouterQueueData> dataList = queueWaitPageData.get(queue.getId());

        //当前队列有正在展示的界面，保存页面数据
        if (dataList == null) {
            dataList = new ArrayList<>();
            queueWaitPageData.put(queue.getId(), dataList);
        }
        dataList.add(new RouterQueueData(queue, routerData));
        //按照优先级排序
        Collections.sort(dataList, comparator);

        return true;
    }

    /**
     * 页面关闭时，调用该方法
     */
    public synchronized static void pageDataFinish(PageData pageData) {
        //不是队列界面 不做处理
        PageQueueData queue = pageData.getQueue();
        if (!queue.isQueue()) return;

        List<RouterQueueData> dataList = queueWaitPageData.get(queue.getId());

        if (dataList == null || dataList.size() == 0) {
            //如果没有等待队列，将正在展示状态置为false
            isShowingQueue.put(queue.getId(), false);
        } else {
            //直接启动下一个page
            RouterQueueData remove = dataList.remove(0);
            remove.routerData.isExecuted = true;
            new PageLauncher().start(remove.routerData);
        }
    }

    /**
     * 清除队列
     */
    public synchronized static void clear() {
        queueWaitPageData.clear();
        isShowingQueue.clear();
    }

}
