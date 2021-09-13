import java.io.*;
import java.net.InetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.net.DatagramSocket;

@WebServlet("/test")
public class MyTestServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        String inputFileName = "file_ip.txt";
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
        int k = 0;
        String ip = "";

        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002); // 8.8.8.8 можно заменить на то, чего может достичь каждый хост
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            out.println("Failed to retrieving ip address " + e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (ip.equals(line)) {
                    k = 1;
                    //out.println("<h1>" + ip + " " + k + "</h1>");
                }
            }
            if (k == 1) {
                out.println("Access disallowed");
            } else {
                dispatcher.forward(request, response);
                //response.sendRedirect("/index.html");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void destroy() {
    }
}