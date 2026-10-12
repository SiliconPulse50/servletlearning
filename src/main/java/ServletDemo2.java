import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(urlPatterns = "/demo2",loadOnStartup = 1 )
public class ServletDemo2 implements Servlet {
    private ServletConfig servletConfig;
    /**
     * 初始化方法，调用时机：默认情况下，servlet被第一次访问时调用 loadonstartup
     * 调用次数，1次
     * @param servletConfig
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig=servletConfig;
        System.out.println("init ");
    }

    /**
     * 提供服务
     * 1.调用时机 ：每一次servlet被访问时调用这个方法
     * 2.调用次数 ： 多次
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("servlet hello world ");
    }

    /**
     * 调用时机：内存释放或者服务器关闭的时候，servlet会被销毁
     * 调用次数：
     */
    @Override
    public void destroy() {
        System.out.println("destroy");
    }


    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }
    @Override
    public String getServletInfo() {
        return "";
    }
}
