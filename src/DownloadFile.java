import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static conf.ApplicationProperties.FILE_UPLOADS_BASE_URL;

public class DownloadFile extends HttpServlet {
    private static final int ARBITRARY_SIZE = 1048;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("filename");
        String filetype = request.getParameter("filetype");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType(filetype);

        readFileFromDiskAndDownload(request, response, fileName);
    }

    private void readFileFromDiskAndDownload(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        try (InputStream in = request.getServletContext().getResourceAsStream(FILE_UPLOADS_BASE_URL + "/" + fileName);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[ARBITRARY_SIZE];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }
    }
}
