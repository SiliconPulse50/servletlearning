import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String username="张三";
        String encode = URLEncoder.encode(username, "utf-8");
        System.out.println(encode);
//        String decode = URLDecoder.decode(encode, "utf-8");
        String decode = URLDecoder.decode(encode, "ISO-8859-1");
        //我的操作 3.转换为字节数据
        byte[] bytes = decode.getBytes("ISO-8859-1");
//        for (byte b : bytes) {
//            System.out.print(b+" ");
//        }
        //把字节数组转化为字符串，解码的过程，tomcat进行解码的过程，默认的字符ISO-8859-1，乱码
        String s=new String(bytes,"utf-8");
        System.out.println(s);

    }
}
