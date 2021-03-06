package com.bluemobi.sys.filter;

import org.apache.commons.lang.StringUtils;

import com.bluemobi.utils.YqssUtils;
import com.bluemobi.utils.encryption.BackAES;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Administrator on 2016/7/3 0003.
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> newParams;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> maps) {
        super(request);
        this.newParams = maps;
        renewParameterMap(request);
    }

    @Override
    public String getParameter(String name) {
        String result = "";
        Object v = newParams.get(name);
        if (v == null) {
            result = null;
        } else if (v instanceof String[]) {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                result = strArr[0];
            } else {
                result = null;
            }
        } else if (v instanceof String) {
            result = (String) v;
        } else {
            result = v.toString();
        }

        return result;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return newParams;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<String>(newParams.keySet()).elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result = null;

        Object v = newParams.get(name);
        if (v == null) {
            result = null;
        } else if (v instanceof String[]) {
            result = (String[]) v;
        } else if (v instanceof String) {
            result = new String[]{(String) v};
        } else {
            result = new String[]{v.toString()};
        }

        return result;
    }

    private void renewParameterMap(HttpServletRequest req) {
        String params = req.getQueryString();
        if (StringUtils.isNotBlank(params)) {
            try {
                String encode = BackAES.decrypt(params.replaceAll(" ", "+"), BackAES.TYPE_ECB);
                System.out.println("解密之后:" + encode);
                if (StringUtils.isNotBlank(encode)) {
                    Map<String, Object> map = YqssUtils.jsontoMap(encode);
                    for (Map.Entry<String, Object> param : map.entrySet()) {
                        this.newParams.put(param.getKey(), new String[]{param.getValue().toString()});
                    }
                }
                else {
                	this.newParams.put("error", new String[] {"error"});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
        	this.newParams.put("error", new String[] {"error"});
        }
    }
}
