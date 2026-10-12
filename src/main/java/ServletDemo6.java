import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/demo6")
public class ServletDemo6 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method=req.getMethod();
        System.out.println(method);

        String contestPath=req.getContextPath();
        System.out.println(contestPath);

        StringBuffer url=req.getRequestURL();
        System.out.println(url);

        String queryString=req.getQueryString();
        System.out.println(queryString);
        //获取请求头
        String agent =req.getHeader("user-agent");
        System.out.println(agent);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取字符输入流
        BufferedReader br=req.getReader();

        //2.读取数据
        String line=br.readLine();
        System.out.println(line);

    }
}

//http://localhost:8080/tomcatdemo1_war_exploded/demo6?name=zhangsan
//http://localhost:8080/tomcatdemo1_war_exploded/req.html