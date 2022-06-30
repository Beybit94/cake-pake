package kz.cake.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ImageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        String imagePath = System.getenv().get("images_path");
        String path = imagePath + url.split("files")[1];

        File file = new File(path);
        String mimeType = file.getName().substring(file.getName().lastIndexOf(".")+1);
        if(mimeType.equals("jpg")){
            response.setContentType("image/jpeg");
        }else{
            response.setContentType("image/" + mimeType);
        }

        ServletOutputStream servletOutputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
        bufferedOutputStream.write(bufferedInputStream.readAllBytes());

        bufferedInputStream.close();
        fileInputStream.close();
        bufferedOutputStream.close();
        servletOutputStream.close();
    }
}
