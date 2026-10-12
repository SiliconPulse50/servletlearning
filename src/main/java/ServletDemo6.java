import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/demo6")
public class ServletDemo6 extends HttpServlet {
    //浏览器地址直接访问，超链接->Get，参数拼接在URL后面，直接在地址栏里面看见，GET只用来查询，不需要修改数据库数据
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method=req.getMethod();
        System.out.println(method);

        String contestPath=req.getContextPath();
        System.out.println(contestPath);

        StringBuffer url=req.getRequestURL();
        System.out.println(url);
        //获取请求参数方式，GET
        String queryString=req.getQueryString();
        System.out.println(queryString);
        //获取请求头
        String agent =req.getHeader("user-agent");
        System.out.println(agent);

    }
    //表单Method="Post"提交，请求参数放在请求体，POST 用来提交新增修改
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取字符输入流
        BufferedReader br=req.getReader();

        //2.读取数据
        String line=br.readLine();
        System.out.println(line);

    }
}
// 测试
// http://localhost:8080/tomcatdemo1_war_exploded/demo6?name=zhangsan
// http://localhost:8080/tomcatdemo1_war_exploded/req.html
// 改进
// Map<String,String[]> getParameterMap(): 获取所有参数Map集合
// getParameterValues(String name); 根据名称获取参数值
// getParameter(String name) 根据名称获取参数值(单个值)

