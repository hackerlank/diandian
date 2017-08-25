package com.dingli.diandiaan.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.WebView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SafeWebView extends WebView implements Pullable {
//    private static final boolean DEBUG = false;
//    private static final String VAR_ARG_PREFIX = "arg";
//    private static final String MSG_PROMPT_HEADER = "MyApp:";
//    private static final String KEY_INTERFACE_NAME = "obj";
//    private static final String KEY_FUNCTION_NAME = "func";
//    private static final String KEY_ARG_ARRAY = "args";
//    private static final String[] mFilterMethods = {"getClass", "hashCode", "notify", "notifyAll", "equals",
//            "toString", "wait",};

    private final HashMap<String, Object> mJsInterfaceMap = new HashMap<String, Object>();
    private String mJsStringCache = null;

    private boolean isCanPullDown = true;//默认可以下拉

    public SafeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SafeWebView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // 删除掉Android默认注册的JS接口
        removeSearchBoxImpl();
    }

    private void removeSearchBoxImpl() {
        try {
            //if (hasHoneycomb() && !hasJellyBeanMR1()) {
            invokeMethod("removeJavascriptInterface", "searchBoxJavaBridge_");
            //}
        } catch (Exception e) {
        }

        try {
            //if (hasHoneycomb() && !hasJellyBeanMR1()) {
            invokeMethod("removeJavascriptInterface", "accessibility");
            //}
        } catch (Exception e) {
        }

        try {
            //if (hasHoneycomb() && !hasJellyBeanMR1()) {
            invokeMethod("removeJavascriptInterface", "accessibilityTraversal");
            //}
        } catch (Exception e) {
        }
    }

    private void invokeMethod(String method, String param) {
        Method m;
        try {
            m = WebView.class.getDeclaredMethod(method, String.class);
            m.setAccessible(true);
            m.invoke(this, param);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= 17;
    }

    private boolean invokeJSInterfaceMethod(JsPromptResult result, String interfaceName, String methodName,
                                            Object[] args) {

        boolean succeed = false;
        final Object obj = mJsInterfaceMap.get(interfaceName);
        if (null == obj) {
            result.cancel();
            return false;
        }

        Class<?>[] parameterTypes = null;
        int count = 0;
        if (args != null) {
            count = args.length;
        }

        if (count > 0) {
            parameterTypes = new Class[count];
            for (int i = 0; i < count; ++i) {
                parameterTypes[i] = getClassFromJsonObject(args[i]);
            }
        }

        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            Object returnObj = method.invoke(obj, args); // 执行接口调用
            boolean isVoid = returnObj == null || returnObj.getClass() == void.class;
            String returnValue = isVoid ? "" : returnObj.toString();
            result.confirm(returnValue); // 通过prompt返回调用结果
            succeed = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.cancel();
        return succeed;
    }

    private Class<?> getClassFromJsonObject(Object obj) {
        Class<?> cls = obj.getClass();

        // js对象只支持int boolean string三种类型
        if (cls == Integer.class) {
            cls = Integer.TYPE;
        } else if (cls == Boolean.class) {
            cls = Boolean.TYPE;
        } else {
            cls = String.class;
        }

        return cls;
    }

    private void loadJavascriptInterfaces() {
        try {
            if (!TextUtils.isEmpty(mJsStringCache)) {
                this.loadUrl(mJsStringCache);
            }
        } catch (Exception e) {

        }
    }
    /**
     * 设置是否可以下拉
     *
     * @param canPullDown
     */
    public void setCanPullDown(boolean canPullDown) {
        isCanPullDown = canPullDown;
    }

    @Override
    public boolean canPullDown() {
        if (isCanPullDown) {
            if (getScrollY() == 0)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
}
