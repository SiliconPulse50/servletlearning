import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebServlet("/demo4")
public class ServletDemo4 implements Servlet {
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
      //逻辑处理：根据请求方式的不同，进行分别处理
        HttpServletRequest request=(HttpServletRequest) servletRequest;

        //获取请求方式
        String method = request.getMethod();

        //判断请求的
        if("GET".equals(method)){
            doGet(servletRequest,servletResponse);
        }else if("POST".equals(method))
        {
            doPost(servletRequest,servletResponse);
        }
    }

    /*
    *但是 Http 协议才有 getMethod ()！父接口 ServletRequest 里面没有 getMethod () 这个方法
    *所以必须**向下强转成子实现类`HttpServletRequest`**之后，才能拿到请求方式
     */
    protected void doPost (ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("post。。。");
    }

    protected  void doGet(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("get。。。");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {

    }
}
