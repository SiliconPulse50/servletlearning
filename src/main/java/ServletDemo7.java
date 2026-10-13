import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
//Request使用通用的方式获取请求参数03-
@WebServlet("/demo7")
public class ServletDemo7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //注意：setCharacterEncoding 只对 POST 请求体生效；
        //GET 的 query string 由 server.xml 的 URIEncoding 决定（Tomcat 8+ 默认 UTF-8），所以 GET 一般不乱码
        req.setCharacterEncoding("UTF-8");
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
        //2.获取参数值，是一个数组;没勾选 hobby 时返回 null，不加判断直接 for 会空指针
        String[] hobbies = req.getParameterValues("hobby");
        if (hobbies != null) {
            for (String hobby : hobbies) {
                System.out.println(hobby);
            }
        }

        //根据key获取单个的参数值
        String username = req.getParameter("username");

        //解决乱码
        byte[] bytes = username.getBytes(StandardCharsets.ISO_8859_1);
        username = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("后："+username);

        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);

    }
    //http://localhost:8080/tomcatdemo1_war_exploded/req.html
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //解决乱码：必须在获取参数之前设置请求体字符集，顺序反了就不生效
        req.setCharacterEncoding("UTF-8");
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
        //2.获取参数值，是一个数组;没勾选 hobby 时返回 null，不加判断直接 for 会空指针
        String[] hobbies = req.getParameterValues("hobby");
        if (hobbies != null) {
            for (String hobby : hobbies) {
                System.out.println(hobby);
            }
        }
        //根据key获取单个的参数值
        String username = req.getParameter("username");


        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("用户名："+username);
    }
}
