package showfree.commoncore;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import showfree.commoncore.http.HttpResponse;
import showfree.commoncore.db.DBSession;

@Configuration
@SpringBootApplication
public class Application {

    public synchronized static void main(String[] args) {
        Master.startup();
        SpringApplication.run(Application.class, args);
        DBSession db = Master.getDBSession("core");
        try {
            JSONObject obj = db.queryFirst("select * from promotionuser");
            HttpResponse response = Master.remoteCall("inoherb", "order");
            JSONObject data = response.getJSONObject();
            System.out.println("obj:" + obj);
            System.out.println("data:" + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}