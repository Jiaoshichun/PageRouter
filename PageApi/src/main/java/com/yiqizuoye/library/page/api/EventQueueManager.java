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
 * 事件队列处理
 */
class EventQueueManager {
    private static class RouterQueueData {
        public PageQueue queue;
        public RouterData routerData;

        public RouterQueueData(PageQueue queue, RouterData routerData) {
            this.queue = queue;
            this.routerData = routerData;
        }
    }

    //页面等待队列
    private final static Map<Integer, List<RouterQueueData>> queueWaitActionData = new HashMap<>();
    //正在展示的队列
    private final static Map<Integer, Boolean> isProcessQueue = new HashMap<>();
    private final static Comparator<RouterQueueData> comparator = new Comparator<RouterQueueData>() {
        @Override
        public int compare(RouterQueueData o1, RouterQueueData o2) {
            return o2.queue.getPriority() - o1.queue.getPriority();
        }
    };

    /**
     * 处理页面跳转
     */
    public synchronized static boolean processPageData(PageQueue queue, RouterData routerData) {
        //如果当前的页面不是队列页面 或者被执行过
        if (!queue.isQueue() || routerData.isExecuted) return false;

        //如果当前队列没有正在展示的页面 不作处理，保存状态
        if (isProcessQueue.get(queue.getId()) == null || !isProcessQueue.get(queue.getId())) {
            routerData.isExecuted = true;
            isProcessQueue.put(queue.getId(), true);
            return false;
        }

        List<RouterQueueData> dataList = queueWaitActionData.get(queue.getId());

        //当前队列有正在展示的界面，保存页面数据
        if (dataList == null) {
            dataList = new ArrayList<>();
            queueWaitActionData.put(queue.getId(), dataList);
        }
        dataList.add(new RouterQueueData(queue, routerData));
        //按照优先级排序
        Collections.sort(dataList, comparator);

        return true;
    }

    /**
     * 页面关闭时，调用该方法
     */
    public synchronized static void pageDataFinish(ActionData pageData) {
        //不是队列界面 不做处理
        PageQueue queue = pageData.getQueue();
        if (!queue.isQueue()) return;

        List<RouterQueueData> dataList = queueWaitActionData.get(queue.getId());

        if (dataList == null || dataList.size() == 0) {
            //如果没有等待队列，将正在展示状态置为false
            isProcessQueue.put(queue.getId(), false);
        } else {
            //直接启动下一个page
            RouterQueueData remove = dataList.remove(0);
            remove.routerData.isExecuted = true;
            new ActionLauncher().start(remove.routerData);
        }
    }

    /**
     * 清除队列
     */
    public synchronized static void clear() {
        queueWaitActionData.clear();
        isProcessQueue.clear();
    }

}
