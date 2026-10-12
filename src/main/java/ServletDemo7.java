import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
//Request使用通用的方式获取请求参数03-
@WebServlet("/demo7")
public class ServletDemo7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //GET请求逻辑
        System.out.println("GET ");
        //1.获取所有参数的Map集合
        Map<String, String[]> map = req.getParameterMap();
        for (String key : map.keySet()) {
            //获取key
            System.out.println(key+": ");
            //值
            String[] values = map.get(key);
            for (String value : values) {
                System.out.print(value+" ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
        //2.获取参数值，是一个数组
        String[] hobbies = req.getParameterValues("hobby");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }
        //根据key获取单个的参数值
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);

    }
//http://localhost:8080/tomcatdemo1_war_exploded/req.html
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //POST请求逻辑
        System.out.println("POST ");
        //1.获取所有参数的Map集合
        Map<String, String[]> map = req.getParameterMap();
        for (String key : map.keySet()) {
            //获取key
            System.out.println(key+": ");
            //值
            String[] values = map.get(key);
            for (String value : values) {
                System.out.print(value+" ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
        //2.获取参数值，是一个数组
        String[] hobbies = req.getParameterValues("hobby");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }
        //根据key获取单个的参数值
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);
    }
}
