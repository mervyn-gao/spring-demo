import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengran.gao on 2017/8/28.
 */
public class MyTest {

    @Test
    public void test() {
        Map<String, Object> result = new HashMap<>();
        result.put("resDescription", "请求成功");
        result.put("resCode", "200");
        Page page = new Page(1, 10);
        page.setPages(100);
        page.setTotal(10000);

        result.put("data", page);
        result.put("ok", true);
        System.out.println(JSONObject.toJSONString(result));
    }
}
